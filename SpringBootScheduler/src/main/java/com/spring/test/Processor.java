package com.spring.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class Processor implements ItemProcessor<String, String>{

	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Override
	public String process(String content) throws Exception {
		logger.info("Processor.process()...");
		return content.toUpperCase();
	}
}