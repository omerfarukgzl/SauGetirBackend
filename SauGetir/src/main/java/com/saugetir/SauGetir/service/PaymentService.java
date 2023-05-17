package com.saugetir.SauGetir.service;

import com.saugetir.SauGetir.request.EncryptedPaymentRequest;
import com.saugetir.SauGetir.response.PaymentResponse;
import com.saugetir.SauGetir.respository.PaymentRepository;
import com.saugetir.SauGetir.utils.AndroidBackendCommuication;
import com.saugetir.SauGetir.utils.BackendBackendCommunication;
import com.saugetir.SauGetir.utils.EncryptionUtil;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final EncryptionUtil encryptionUtil;
    private final AndroidBackendCommuication androidBackendCommuication;
    private final BackendBackendCommunication backendBackendCommunication;


    public PaymentService(PaymentRepository paymentRepository, EncryptionUtil encryptionUtil, AndroidBackendCommuication androidBackendCommuication, BackendBackendCommunication backendBackendCommunication) {
        this.paymentRepository = paymentRepository;
        this.encryptionUtil = encryptionUtil;
        this.androidBackendCommuication = androidBackendCommuication;
        this.backendBackendCommunication = backendBackendCommunication;
    }
    public String payment(){
        return "token_id";
    }

    public PaymentResponse payment(EncryptedPaymentRequest encryptedPaymentRequest, String signature, String randomKey){

        // Gelen body'i json'a çevir-->Body imzası validmi kontrol et --> valid ise body'i decrypt et -->
        // decrypt edilen body'i tekrar şifrele --> şifrelenen body'i EncryptedPaymentRequest objesine set et --> EncryptedPaymentRequest objesini HttpEntity objesine set et (Body) --> HttpEntity objesini RestTemplate ile Saupay'e gönder
        // --> Saupay'den gelen response'u al --> response'u PaymentResponse objesine set et --> PaymentResponse objesini return et


        /****   Android-Backend   ****/

        String decryptedData = androidBackendCommuication.GetAndroidToBackendEncryptedAndSignatureDataTransaction(encryptedPaymentRequest, signature, randomKey);

        /****   Backend-Backend   ****/
        PaymentResponse paymentResponse = new PaymentResponse();
        String rndmKey = encryptionUtil.generateRandomKey();
        String encryptedAndSignatureTokenResponse = backendBackendCommunication.SendBackendToBackendEncryptedAndSignatureDataTransaction(decryptedData,rndmKey);

        String decryptedTokenResponse = backendBackendCommunication.GetBackendToBackendSignatureDataTransaction(encryptedAndSignatureTokenResponse);

        paymentResponse.setToken(decryptedTokenResponse);
        return paymentResponse;

    }



}




/* try {
            String json = objectMapper.writeValueAsString(encryptedPaymentRequest); // Convert body to json
            System.out.println("Encrypted Request -Mobile: " + json );
            Boolean isSignatureValid = encryptionUtil.checkSignature(signature,randomKey,json,SECRET_KEY_CLIENT); // Verify signature
            System.out.println("Signature is Valid-Mobile: " + isSignatureValid );
            if(!isSignatureValid){
                throw new RuntimeException("Signature is not valid-Mobile");
            }
            String decryptedData = encryptionUtil.decrypt(encryptedPaymentRequest.getData(),SECRET_KEY_CLIENT); // When signature is valid, decrypt data
            System.out.println("Decrypted Request-Mobile: " + decryptedData );

        }catch (Exception e) {
            throw new RuntimeException("Beklenmedik bir hata oluştu.");
        }*/
