package com.lukasrosz.ipprocessor.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;

@ConfigurationProperties(prefix = "datasource")
@Getter
@Setter
@ToString
public class DataSourceProperties {

    private final Properties properties = new Properties();
    private String driverClassName;
    private String url;
    private String username;
    private String password;

}
