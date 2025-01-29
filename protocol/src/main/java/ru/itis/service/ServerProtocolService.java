package ru.itis.service;

import ru.itis.Content;
import ru.itis.request.Request;
import ru.itis.request.RequestContentFactory;
import ru.itis.request.RequestType;
import ru.itis.response.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Optional;

public class ServerProtocolService implements MessageService<Response, Request> {
    @Override
    public void send(Response message, Socket socket) {

        int responseType = message.responseType().getIndex();
        byte[] data = message.content().toByteArray();
        ByteBuffer byteBuffer = ByteBuffer.allocate(8 + data.length);
        byteBuffer.putInt(data.length);
        byteBuffer.putInt(responseType);
        byteBuffer.put(data);
        try {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(byteBuffer.array());
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Optional<Request> read(Socket socket) {
        try {

            InputStream inputStream = socket.getInputStream();

            byte[] lengthBytes = new byte[4];
            if (inputStream.read(lengthBytes) != 4) {
                return Optional.empty();
            }
            int dataLength = ByteBuffer.wrap(lengthBytes).getInt();

            byte[] typeBytes = new byte[4];
            if (inputStream.read(typeBytes) != 4) {
                return Optional.empty();
            }

            int requestTypeIndex = ByteBuffer.wrap(typeBytes).getInt();
            RequestType requestType = RequestType.fromIndex(requestTypeIndex)
                    .orElseThrow(() -> new RuntimeException("Unknown request type"));

            byte[] data = inputStream.readNBytes(dataLength);

            Content requestContent = RequestContentFactory.createRequestContent(requestType, data);

            return Optional.of(new Request(requestType, requestContent));

        } catch (IOException e) {
            throw new RuntimeException("Error reading from socket", e);
        }
    }
}
