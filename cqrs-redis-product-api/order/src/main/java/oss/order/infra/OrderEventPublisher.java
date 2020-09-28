package oss.order.infra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import oss.core.event.EventPublisher;
import oss.order.model.event.OrderRawEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by jaceshim on 2017. 3. 17..
 */
@Component
@Slf4j
public class OrderEventPublisher implements EventPublisher<OrderRawEvent> {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	KafkaTemplate<String, String> kafkaTemplate;

	@Value("${kafka.bootstrap.servers}")
	private String kafkaBootstrapServers;

	@Value("${kafka.topic.order}")
	private String orderKafkaTopic;

	@Async
	@Override
	public void publish(OrderRawEvent rawEvent) {
		if (rawEvent == null) {
			return;
		}

		try {
			final String sendMessage = objectMapper.writeValueAsString(rawEvent);
			kafkaTemplate.send(orderKafkaTopic, sendMessage);
			log.debug("{} 전송 완료  - {}", this.orderKafkaTopic, sendMessage);
		} catch (final JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}
	}
}
