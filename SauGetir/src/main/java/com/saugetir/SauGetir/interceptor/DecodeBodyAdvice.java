package com.saugetir.SauGetir.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

@ControllerAdvice
public class DecodeBodyAdvice extends RequestBodyAdviceAdapter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return methodParameter.hasParameterAnnotation(Encrypted.class);
    }

/*    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        try (InputStream inputStream = inputMessage.getBody()) {
            byte[] body = StreamUtils.copyToByteArray(inputStream);
            System.out.println("body = " + new String(body));
            EncryptedRequest requestBody = objectMapper.readValue(body, EncryptedRequest.class);
            var encryptedData = requestBody.getData().getBytes();
            var decryptedData = CryptoUtils.decrypt(encryptedData);
            return new DecodedHttpInputMessage(inputMessage.getHeaders(), new ByteArrayInputStream(decryptedData));
        }
    }*/
}