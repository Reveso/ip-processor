package com.lukasrosz.ipprocessor;

import com.lukasrosz.ipprocessor.config.DataSourceProperties;
import com.lukasrosz.ipprocessor.processing.infrastructure.ProcessingProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableConfigurationProperties({DataSourceProperties.class, ProcessingProperties.class})
@EnableScheduling
@SpringBootApplication
public class IpProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(IpProcessorApplication.class, args);
	}

}
