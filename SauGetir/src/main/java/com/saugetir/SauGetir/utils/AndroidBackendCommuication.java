package com.saugetir.SauGetir.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saugetir.SauGetir.request.EncryptedPaymentRequest;
import org.springframework.stereotype.Component;

import static com.saugetir.SauGetir.utils.Constant.SECRET_KEY_CLIENT;

@Component
public class AndroidBackendCommuication {

    private final ObjectMapper objectMapper;
    private final EncryptionUtil encryptionUtil;

    public AndroidBackendCommuication(ObjectMapper objectMapper, EncryptionUtil encryptionUtil) {
        this.objectMapper = objectMapper;
        this.encryptionUtil = encryptionUtil;
    }


    public String GetAndroidToBackendEncryptedAndSignatureDataTransaction(EncryptedPaymentRequest encryptedPaymentRequest, String signature, String randomKey)
    {
        try {
            String json = objectMapper.writeValueAsString(encryptedPaymentRequest); // Convert body to json
            System.out.println("Encrypted Request -Mobile: " + json);
            Boolean isSignatureValid = encryptionUtil.checkSignature(signature, randomKey, json, SECRET_KEY_CLIENT); // Verify signature
            System.out.println("Signature is Valid-Mobile: " + isSignatureValid);
            if (!isSignatureValid) {
                throw new RuntimeException("Signature is not valid-Mobile");
            }
            String decryptedData = encryptionUtil.decrypt(encryptedPaymentRequest.getData(), SECRET_KEY_CLIENT); // When signature is valid, decrypt data
            System.out.println("Decrypted Request-Mobile: " + decryptedData);

            return decryptedData;
        }
        catch (Exception e)
        {
            throw new RuntimeException("AndroidToBackendEncryptedAndSignatureDataTransaction error: " + e.getMessage());
        }
    }
}
