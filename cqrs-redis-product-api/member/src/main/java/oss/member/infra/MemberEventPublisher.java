package oss.member.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import oss.core.event.EventPublisher;
import oss.member.model.event.MemberRawEvent;

/**
 * Created by jaceshim on 2017. 3. 17..
 */
@Component
@Slf4j
public class MemberEventPublisher implements EventPublisher<MemberRawEvent> {

	@Value("${kafka.topic.member}")
	String memberKafkaTopic;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	KafkaTemplate<String, String> kafkaTemplate;

	@Value("${kafka.bootstrap.servers}")
	private String kafkaBootstrapServers;

	@Async
	@Override
	public void publish(MemberRawEvent rawEvent) {
		if (rawEvent == null) {
			return;
		}

		try {
			final String sendMessage = objectMapper.writeValueAsString(rawEvent);
			kafkaTemplate.send(memberKafkaTopic, sendMessage);
			log.debug("{} 전송 완료  - {}", this.memberKafkaTopic, sendMessage);
		} catch (final JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}
	}
	
}
