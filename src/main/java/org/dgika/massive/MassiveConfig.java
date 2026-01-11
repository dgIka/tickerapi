package org.dgika.massive;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(MassiveProperties.class)
public class MassiveConfig {

    @Bean
    public RestClient massiveRestClient(MassiveProperties props) {
        return RestClient.builder().baseUrl(props.baseUrl()).build();
    }
}
