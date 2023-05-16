package com.saugetir.SauGetir.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saugetir.SauGetir.request.EncryptedPaymentRequest;
import com.saugetir.SauGetir.response.PaymentResponse;
import com.saugetir.SauGetir.respository.PaymentRepository;
import com.saugetir.SauGetir.utils.EncryptionUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final EncryptionUtil encryptionUtil;
    private final ObjectMapper objectMapper;


    public PaymentService(PaymentRepository paymentRepository, EncryptionUtil encryptionUtil, ObjectMapper objectMapper) {
        this.paymentRepository = paymentRepository;
        this.encryptionUtil = encryptionUtil;
        this.objectMapper = objectMapper;
    }
    public String payment(){
        return "token_id";
    }

    public PaymentResponse payment(EncryptedPaymentRequest encryptedPaymentRequest, String signature, String randomKey){

        try {
            String json = objectMapper.writeValueAsString(encryptedPaymentRequest); // Convert body to json
            Boolean isSignatureValid = encryptionUtil.checkSignature(signature,randomKey,json); // Verify signature
            if(!isSignatureValid){
                throw new RuntimeException("Signature is not valid");
            }
            String decryptedData = encryptionUtil.decrypt(encryptedPaymentRequest.getData()); // When signature is valid, decrypt data


            PaymentResponse paymentResponse = new PaymentResponse();
            System.out.println("paymentRequest: " + decryptedData );

            paymentResponse.setToken("Ben Ömer");

            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject("http://localhost:8888/v1/saupay/generatePaymentToken/"+json, String.class);
            paymentResponse.setToken(response);
            return paymentResponse;










        }catch (Exception e) {
            throw new RuntimeException("Signature is not valid");
        }



















        // TODO sign edilen request Saupay'e gönderilecek







        // TODO Saupay'den dönen sign token çözülüp doğrulanacak








        // TODO doğrulanan token androide response olarak gönderilecek





        //return paymentRequest.getMerchantCode()+" "+paymentRequest.getAmount();
    }



}



