package com.saugetir.SauGetir.service;

import com.saugetir.SauGetir.request.EncryptedPaymentRequest;
import com.saugetir.SauGetir.response.PaymentResponse;
import com.saugetir.SauGetir.respository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import static com.saugetir.SauGetir.utils.Constant.SECRET_KEY;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;


    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }
    public String payment(){
        return "token_id";
    }

    public PaymentResponse payment2(EncryptedPaymentRequest encryptedPaymentRequest){


        PaymentResponse paymentResponse = new PaymentResponse();

        // Log
        System.out.println("paymentRequest: " + encryptedPaymentRequest.getData() );

        String decryptedData = decrypt(encryptedPaymentRequest.getData());

        System.out.println("decryptedData: " + decryptedData );
        paymentResponse.setToken("Ben Ömer");

        RestTemplate restTemplate = new RestTemplate();
       // String response = restTemplate.getForObject("http://localhost:8888/v1/saupay/generatePaymentToken/request", String.class);
        return paymentResponse;

        // TODO request sign edilecek



        // TODO sign edilen request Saupay'e gönderilecek







        // TODO Saupay'den dönen sign token çözülüp doğrulanacak








        // TODO doğrulanan token androide response olarak gönderilecek





        //return paymentRequest.getMerchantCode()+" "+paymentRequest.getAmount();
    }


    public String decrypt(String encryptedData){
        try {
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] encryptedBytes = Base64Utils.decodeFromString(encryptedData);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return "Şifre çözme hatası";
        }
    }
}



