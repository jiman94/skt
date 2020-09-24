package oss.order.infra;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
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
import oss.member.model.event.MemberRawEvent;
import oss.order.config.ModelMapperx;
import oss.order.config.RedisCmd;
import oss.order.model.event.OrderRawEvent;

/**
 * Created by jaceshim on 2017. 3. 14..
 */
@Component
@Slf4j
public class OrderEventStore implements EventStore<Long> {

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
	
	private final String ORDER_EVENT_ROW = "EVENTROW_Z_ORDER";

	@Override
	public void saveEvents(final Long identifier, Long expectedVersion, final List<Event> events) {
		// 신규 등록이 아닌 경우 version확인 후 처리
		if (expectedVersion > 0) {
			List<Object> redisData = redisCmd.zRange(ORDER_EVENT_ROW, "*\""+identifier+"\"*" );
			List<OrderRawEvent> rawEvents = redisData.stream().map(data -> (OrderRawEvent) modelMapper.map(data, OrderRawEvent.class)).collect(Collectors.toList());
			Long actualVersion = rawEvents.stream()
				.sorted(Comparator.comparing(OrderRawEvent::getVersion).reversed())
				.findFirst().map(OrderRawEvent::getVersion)
				.orElse(-1L);

			if (expectedVersion != actualVersion) {
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

			expectedVersion++;
			String now = DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmssSSS")+String.valueOf(System.nanoTime()).substring(12);
			OrderRawEvent rawEvent = new OrderRawEvent(identifier, type, expectedVersion, payload, now);

			redisCmd.zPut(ORDER_EVENT_ROW, rawEvent, expectedVersion);

			// event 발행
			eventPublisher.publish(rawEvent);

			// event projection
			eventProjector.handle(event);
		}
	}

	@Override
	public List<Event<Long>> getEvents(Long identifier) {
		List<Object> redisData = redisCmd.zRange(ORDER_EVENT_ROW, "*\""+identifier+"\"*" );
		if(redisData==null)	return null;
		final List<OrderRawEvent> rawEvents = redisData.stream().map(data -> (OrderRawEvent) modelMapper.map(data, OrderRawEvent.class)).collect(Collectors.toList());
		return convertEvent(rawEvents);
	}

	@Override
	public List<Event<Long>> getAllEvents() {
		Set<Object> redisData = redisCmd.zRange(ORDER_EVENT_ROW, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY );
		if(redisData==null)	return null;
		final List<OrderRawEvent> rawEvents = redisData.stream().map(data -> (OrderRawEvent) modelMapper.map(data, OrderRawEvent.class)).collect(Collectors.toList());
		return convertEvent(rawEvents);
	}

	@Override
	public List<Event<Long>> getEventsByAfterVersion(Long identifier, Long version) {
		Set<Object> redisData = redisCmd.zRange(ORDER_EVENT_ROW, version, Double.POSITIVE_INFINITY );
		if(redisData==null)	return null;
		final List<OrderRawEvent> rawEvents = redisData.stream().map(data -> {OrderRawEvent cd = (OrderRawEvent) modelMapper.map(data, OrderRawEvent.class);return cd.getIdentifier().equals(identifier)?cd:null;}).filter(data -> data!=null).collect(Collectors.toList());
		return convertEvent(rawEvents);
	}

	private List<Event<Long>> convertEvent(List<OrderRawEvent> rawEvents) {
		return rawEvents.stream().map(rawEvent -> {
			Event<Long> event = null;
			try {
				event = (Event) objectMapper.readValue(rawEvent.getPayload(), Class.forName(rawEvent.getType()));
			} catch (IOException | ClassNotFoundException e) {
				String exceptionMessage = String.format("Event Object Convert Error : {} {}", rawEvent.getSeq(), rawEvent.getType(),
					rawEvent.getPayload());
				log.error(exceptionMessage, e);
			}
			return event;
		}).collect(Collectors.toList());
	}

}
