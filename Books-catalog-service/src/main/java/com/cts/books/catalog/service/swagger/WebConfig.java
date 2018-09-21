package com.cts.books.catalog.service.swagger;

import javax.servlet.Servlet;
import org.h2.server.web.WebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {
	@Bean
	ServletRegistrationBean<WebServlet> h2servletRegistration() {
		final ServletRegistrationBean<WebServlet> registrationBean = (ServletRegistrationBean<WebServlet>) new ServletRegistrationBean(
				(Servlet) new WebServlet(), new String[0]);
		registrationBean.addUrlMappings(new String[] { "/console/*" });
		return registrationBean;
	}
}
