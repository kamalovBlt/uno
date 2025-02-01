package ru.itis.unogame.thread;

import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.itis.request.Request;
import ru.itis.request.RequestType;
import ru.itis.response.Response;
import ru.itis.service.MessageService;
import ru.itis.unogame.handler.RequestHandler;

import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Setter
@Component
@Scope("prototype")
public class UserThread extends Thread {

    private final MessageService<Response, Request> serverProtocolService;
    private final Map<RequestType, RequestHandler> requestHandlers;
    private Socket socket;

    public UserThread(MessageService<Response, Request> serverProtocolService,
                      List<RequestHandler> requestHandlers) {
        this.serverProtocolService = serverProtocolService;
        this.requestHandlers = requestHandlers.stream()
                .collect(Collectors.toMap(RequestHandler::getType, Function.identity()));
    }

    @Override
    public void run() {
        Optional<Request> read;
        while ((read = serverProtocolService.read(socket)).isPresent()) {
            Request request = read.get();
            RequestHandler requestHandler = requestHandlers.get(request.requestType());
            if (requestHandler == null) {
                requestHandler = requestHandlers.get(RequestType.UNKNOWN);
            }
            requestHandler.handle(socket, request);
        }
    }
}
