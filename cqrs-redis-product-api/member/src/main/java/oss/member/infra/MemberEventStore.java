package oss.member.infra;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import oss.core.event.Event;
import oss.core.event.EventProjector;
import oss.core.event.EventPublisher;
import oss.core.event.EventStore;
import oss.member.config.ModelMapperx;
import oss.member.config.RedisCmd;
import oss.member.model.event.MemberRawEvent;

/**
 * Created by jaceshim on 2017. 3. 14..
 */
@Component
@Slf4j
public class MemberEventStore implements EventStore<String> {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private EventPublisher eventPublisher;

	@Autowired
	private EventProjector eventProjector;
	
	@Autowired
	private RedisCmd redisCmd;
	
	@Autowired
	private ModelMapperx modelMapper;
	
	private final String MEMBER_EVENT_ROW = "EVENTROW_Z_MEMBER";

	@Override
	public void saveEvents(final String identifier, Long expectedVersion, final List<Event> events) {
		// 신규 등록이 아닌 경우 version확인 후 처리
		if (expectedVersion > 0) {
			List<Object> redisData = redisCmd.zRange(MEMBER_EVENT_ROW, "*\""+identifier+"\"*" );
			List<MemberRawEvent> rawEvents = redisData.stream().map(data -> (MemberRawEvent) modelMapper.map(data, MemberRawEvent.class)).collect(Collectors.toList());
			Long actualVersion = rawEvents.stream()
				.sorted((e1, e2) -> Long.compare(e2.getVersion(), e1.getVersion()))
				.findFirst().map(MemberRawEvent::getVersion)
				.orElse(-1L);

			if (! expectedVersion.equals(actualVersion)) {
				String exceptionMessage = String.format("Unmatched Version : expected: {}, actual: {}", expectedVersion, actualVersion);
				throw new IllegalStateException(exceptionMessage);
			}
		}

		for (Event event : events) {
			String type = event.getClass().getName();
			String payload = null;

			try {
				payload = objectMapper.writeValueAsString(event);
				log.debug("-> event payload : {}", payload);
			} catch (JsonProcessingException e) {
				log.error(e.getMessage(), e);
			}

			expectedVersion = increaseVersion(expectedVersion);
			String now = DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmssSSS")+String.valueOf(System.nanoTime()).substring(12);
			MemberRawEvent rawEvent = new MemberRawEvent(identifier, type, expectedVersion, payload, now);

			// 이벤트 저장
			redisCmd.zPut(MEMBER_EVENT_ROW, rawEvent, expectedVersion);

			// event 발행
			eventPublisher.publish(rawEvent);

			// event projection
			eventProjector.handle(event);
		}
	}

	private Long increaseVersion(Long expectedVersion) {
		return ++expectedVersion;
	}

	@Override //db
	public List<Event<String>> getEvents(String identifier) {
		List<Object> redisData = redisCmd.zRange(MEMBER_EVENT_ROW, "*\""+identifier+"\"*" );
		if(redisData==null)	return null;
		final List<MemberRawEvent> rawEvents = redisData.stream().map(data -> (MemberRawEvent) modelMapper.map(data, MemberRawEvent.class)).collect(Collectors.toList());
		return convertEvent(rawEvents);
	}

	@Override
	public List<Event<String>> getAllEvents() {
		Set<Object> redisData = redisCmd.zRange(MEMBER_EVENT_ROW, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY );
		if(redisData==null)	return null;
		final List<MemberRawEvent> rawEvents = redisData.stream().map(data -> (MemberRawEvent) modelMapper.map(data, MemberRawEvent.class)).collect(Collectors.toList());
		return convertEvent(rawEvents);
	}

	@Override
	public List<Event<String>> getEventsByAfterVersion(String identifier, Long version) {
		Set<Object> redisData = redisCmd.zRange(MEMBER_EVENT_ROW, version, Double.POSITIVE_INFINITY );
		if(redisData==null)	return null;
		final List<MemberRawEvent> rawEvents = redisData.stream().map(data -> {MemberRawEvent cd = (MemberRawEvent) modelMapper.map(data, MemberRawEvent.class);return cd.getIdentifier().equals(identifier)?cd:null;}).filter(data -> data!=null).collect(Collectors.toList());
		return convertEvent(rawEvents);
	}

	private List<Event<String>> convertEvent(List<MemberRawEvent> rawEvents) {
		return rawEvents.stream().map(rawEvent -> {
			Event<String> event = null;
			try {
				log.debug("-> event info : {}", rawEvent.toString());
				event = (Event) objectMapper.readValue(rawEvent.getPayload(), Class.forName(rawEvent.getType()));
			} catch (IOException | ClassNotFoundException e) {
				String exceptionMessage = String.format("Event Object Convert Error : {} {}", rawEvent.getIdentifier(), rawEvent.getType(),
					rawEvent.getPayload());
				log.error(exceptionMessage, e);
			}
			return event;
		}).collect(Collectors.toList());
	}
	
}
