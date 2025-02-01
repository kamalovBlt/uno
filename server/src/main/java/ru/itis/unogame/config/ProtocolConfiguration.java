package ru.itis.unogame.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.itis.request.Request;
import ru.itis.response.Response;
import ru.itis.service.MessageService;
import ru.itis.service.ServerProtocolService;

@Configuration
public class ProtocolConfiguration {
    @Bean
    MessageService<Response, Request> serverProtocolService() {
        return new ServerProtocolService();
    }
}
