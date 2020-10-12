package oss.member.infra;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import oss.core.event.AbstractEventListener;
import oss.core.event.Event;
import oss.core.event.EventProjector;
import oss.order.model.event.OrderRawEvent;
import oss.product.model.event.ProductRawEvent;

/**
 * Created by jaceshim on 2017. 3. 27..
 */
@Component
@Slf4j
public class MemberEventListener extends AbstractEventListener {

	@Autowired
	private ObjectMapper objectMapper;

	// projector는 event가 발생하는 domain단위로 분리하여 구현할 수도 있다
	@Autowired
	EventProjector eventProjector;
	
	@org.springframework.kafka.annotation.KafkaListener(topics = "product-event-topic")
	public void productEventListener(String message) {
		log.debug("receive message from product : {}", message);

		try {
			ProductRawEvent rawEvent = objectMapper.readValue(message, ProductRawEvent.class);
			final Class<?> eventType = Class.forName(rawEvent.getType());
			final Event event = (Event) objectMapper.readValue(rawEvent.getPayload(), eventType);

			//this.handle(event);

			eventProjector.handle(event);

		} catch (IOException | ClassNotFoundException e) {
			log.warn(e.getMessage(), e);
		}
	}

	@org.springframework.kafka.annotation.KafkaListener(topics = "order-event-topic")
	public void orderEventListener(String message) {
		log.debug("receive message from order : {}", message);

		try {
			OrderRawEvent rawEvent = objectMapper.readValue(message, OrderRawEvent.class);
			final Class<?> eventType = Class.forName(rawEvent.getType());
			final Event event = (Event) objectMapper.readValue(rawEvent.getPayload(), eventType);

			//this.handle(event);

			eventProjector.handle(event);

		} catch (IOException | ClassNotFoundException e) {
			log.warn(e.getMessage(), e);
		}
	}
}
