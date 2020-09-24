package oss.product.infra;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import oss.core.event.Event;
import oss.core.event.EventProjector;
import oss.core.event.EventPublisher;
import oss.core.event.EventStore;
import oss.member.model.event.MemberRawEvent;
import oss.product.config.ModelMapperx;
import oss.product.config.RedisCmd;
import oss.product.model.event.ProductRawEvent;

/**
 * Created by jaceshim on 2017. 3. 14..
 */
@Component
@Slf4j
public class ProductEventStore implements EventStore<Long> {

	@Autowired
	private ProductEventStoreRepository productEventStoreRepository;

	@Autowired
	private EventPublisher eventPublisher;

	@Autowired
	private EventProjector eventProjector;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private RedisCmd redisCmd;
	
	@Autowired
	private ModelMapperx modelMapper;
	
	private final String PRODUCT_EVENT_ROW = "EVENTROW_Z_PRODUCT";


	@Override
	@Transactional
	public void saveEvents(final Long identifier, Long expectedVersion, final List<Event> events) {
		// 신규 등록이 아닌 경우 version확인 후 처리
		if (expectedVersion > 0) {
			List<Object> redisData = redisCmd.zRange(PRODUCT_EVENT_ROW, "*\""+identifier+"\"*" );
			List<ProductRawEvent> rawEvents = redisData.stream().map(data -> (ProductRawEvent) modelMapper.map(data, ProductRawEvent.class)).collect(Collectors.toList());
			Long actualVersion = rawEvents.stream()
				.sorted(Comparator.comparing(ProductRawEvent::getVersion).reversed())
				.findFirst().map(ProductRawEvent::getVersion)
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
			ProductRawEvent rawEvent = new ProductRawEvent(identifier, type, expectedVersion, payload, now);

			redisCmd.zPut(PRODUCT_EVENT_ROW, rawEvent, expectedVersion);

			// event 발행
			eventPublisher.publish(rawEvent);

			// event projection
			eventProjector.handle(event);
		}
	}

	@Override
	public List<Event<Long>> getEvents(Long identifier) {
		List<Object> redisData = redisCmd.zRange(PRODUCT_EVENT_ROW, "*\""+identifier+"\"*" );
		if(redisData==null)	return null;
		final List<ProductRawEvent> rawEvents = redisData.stream().map(data -> (ProductRawEvent) modelMapper.map(data, ProductRawEvent.class)).collect(Collectors.toList());
		return convertEvent(rawEvents);
	}

	@Override
	public List<Event<Long>> getAllEvents() {
		Set<Object> redisData = redisCmd.zRange(PRODUCT_EVENT_ROW, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY );
		if(redisData==null)	return null;
		final List<ProductRawEvent> rawEvents = redisData.stream().map(data -> (ProductRawEvent) modelMapper.map(data, ProductRawEvent.class)).collect(Collectors.toList());
		return convertEvent(rawEvents);
	}

	@Override
	public List<Event<Long>> getEventsByAfterVersion(Long identifier, Long version) {
		Set<Object> redisData = redisCmd.zRange(PRODUCT_EVENT_ROW, version, Double.POSITIVE_INFINITY );
		if(redisData==null)	return null;
		final List<ProductRawEvent> rawEvents = redisData.stream().map(data -> {ProductRawEvent cd = (ProductRawEvent) modelMapper.map(data, ProductRawEvent.class);return cd.getIdentifier().equals(identifier)?cd:null;}).filter(data -> data!=null).collect(Collectors.toList());
		return convertEvent(rawEvents);
	}

	private List<Event<Long>> convertEvent(List<ProductRawEvent> productRawEvents) {
		return productRawEvents.stream().map(productRawEvent -> {
			Event<Long> event = null;
			try {
				event = (Event) objectMapper.readValue(productRawEvent.getPayload(), Class.forName(productRawEvent.getType()));
			} catch (IOException | ClassNotFoundException e) {
				String exceptionMessage = String.format("Event Object Convert Error : {} {}", productRawEvent.getSeq(), productRawEvent.getType(),
					productRawEvent.getPayload());
				log.error(exceptionMessage, e);
			}
			return event;
		}).collect(Collectors.toList());
	}
}
