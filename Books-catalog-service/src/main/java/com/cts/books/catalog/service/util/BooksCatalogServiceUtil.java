package com.cts.books.catalog.service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum BooksCatalogServiceUtil {
	INSTANCE;
	public static final Logger logger = LoggerFactory.getLogger(BooksCatalogServiceUtil.class);

	public String dataFallBack(final String message) {
		return message;
	}
}
