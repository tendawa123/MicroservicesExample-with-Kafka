package com.cts.books.catalog.service.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaSender {

	public static final Logger logger = LoggerFactory.getLogger(KafkaSender.class);
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	@Value("${app.topic.foo}")
	private String topicFoo;
	public void sendTopicFoo(String message) {
		logger.info("sending message='{}' to topic='{}'", message, topicFoo);
		kafkaTemplate.send(topicFoo, message);
	}
	@Value("${app.topic.bar}")
	private String topicBar;
	public void sendTopicBar(String message) {
		logger.info("sending message='{}' to topic='{}'", message, topicBar);
		kafkaTemplate.send(topicBar, message);
	}

}