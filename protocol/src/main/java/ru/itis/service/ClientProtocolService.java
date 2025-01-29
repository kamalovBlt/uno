package ru.itis.service;

import ru.itis.Content;
import ru.itis.request.Request;
import ru.itis.response.Response;
import ru.itis.response.ResponseContentFactory;
import ru.itis.response.ResponseType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Optional;

public class ClientProtocolService implements MessageService<Request, Response> {

    @Override
    public void send(Request message, Socket socket) {

        int requestType = message.requestType().getIndex();
        byte[] data = message.content().toByteArray();

        ByteBuffer byteBuffer = ByteBuffer.allocate(8 + data.length);
        byteBuffer.putInt(data.length);
        byteBuffer.putInt(requestType);
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
    public Optional<Response> read(Socket socket) {

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

            int responseTypeIndex = ByteBuffer.wrap(typeBytes).getInt();
            ResponseType responseType = ResponseType.fromIndex(responseTypeIndex)
                    .orElseThrow(() -> new RuntimeException("Unknown request type"));

            byte[] data = inputStream.readNBytes(dataLength);

            Content responseContent = ResponseContentFactory.createResponseContent(responseType, data);

            return Optional.of(new Response(responseType, responseContent));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
