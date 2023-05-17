package com.saugetir.SauGetir.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saugetir.SauGetir.request.EncryptedPaymentRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static com.saugetir.SauGetir.utils.Constant.SECRET_KEY_BACKEND;

@Component
public class BackendBackendCommunication {

    private final ObjectMapper objectMapper;
    private final EncryptionUtil encryptionUtil;

    public BackendBackendCommunication(ObjectMapper objectMapper, EncryptionUtil encryptionUtil) {
        this.objectMapper = objectMapper;
        this.encryptionUtil = encryptionUtil;
    }

    public String SendBackendToBackendEncryptedAndSignatureDataTransaction(String decryptedData,String randomKey)
    {
        try {
            // çözülen datayı tekrar şifrele
            String encryptedData = encryptionUtil.encrypt(decryptedData,SECRET_KEY_BACKEND);
            System.out.println("Encrypted Data Request-Backend: " + encryptedData );

            // şifrelenen datayı EncryptedPaymentRequest objesine set et
            EncryptedPaymentRequest encryptedPaymentRequestBackend = new EncryptedPaymentRequest();
            encryptedPaymentRequestBackend.setData(encryptedData);


            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<EncryptedPaymentRequest> requestEntity = new HttpEntity<>(encryptedPaymentRequestBackend, headers); // EncryptedPaymentRequest objesini HttpEntity objesine set et (Body)
            String requestBody = objectMapper.writeValueAsString(requestEntity.getBody());


            System.out.println("Random Key-Backend: " + randomKey );
            String signatureData = encryptionUtil.signature(randomKey,requestBody,SECRET_KEY_BACKEND);
            System.out.println("Signature Data -Backend: " + signatureData );

            headers.set("x-signature", signatureData);
            headers.set("x-rnd-key", randomKey);

            String response = restTemplate.postForObject("http://localhost:8888/v1/transaction/generatePaymentToken",requestEntity, String.class);
            System.out.println("Response(TOKEN) -Backend: " + response );

            return response;
        }
        catch (Exception e)
        {
            throw new RuntimeException("BackendToBackendEncryptedAndSignatureDataTransaction error: " + e.getMessage());
        }
    }


    public String GetBackendToBackendSignatureDataTransaction(String encryptedToken)
    {
        try {
            System.out.println("Encrypted Reponse -Backend:  " + encryptedToken);
            String decryptedData = encryptionUtil.decrypt(encryptedToken, SECRET_KEY_BACKEND); // When signature is valid, decrypt data
            System.out.println("Decrypted Reponse-Backend: " + decryptedData);
            return decryptedData;
        }
        catch (Exception e)
        {
            throw new RuntimeException("BackendToBackendEncryptedAndSignatureDataTransaction error: " + e.getMessage());
        }
    }

}
