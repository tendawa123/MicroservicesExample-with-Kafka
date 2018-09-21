package com.cts.books.catalog.client.kafkaListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
	
	private static final Logger LOG = LoggerFactory.getLogger(KafkaConsumer.class);
	/*
	 * @KafkaListener(topics = "test-topic") public void consume(String message)
	 * { System.out.println("Consumed message: " + message); }
	 */
	@KafkaListener(topics = "${app.topic.foo}")
	public void receiveFirstTopic(@Payload String message, @Headers MessageHeaders headers) {
		LOG.info("received message='{}'", message);

	}
	@KafkaListener(topics = "${app.topic.bar}")
	public void receiveSecondTopic(@Payload String message, @Headers MessageHeaders headers) {
		LOG.info("received message='{}'", message);

	}
}