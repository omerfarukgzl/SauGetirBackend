package com.saugetir.SauGetir.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Locale;
import java.util.Objects;

import static com.saugetir.SauGetir.utils.Constant.PAYMENT_URL;
import static com.saugetir.SauGetir.utils.Constant.SECRET_KEY;

@Component
public class EncryptionUtil {
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

    public Boolean checkSignature(String signature,String randomKey,String encryptedPaymentRequest){

        System.out.println("hepsi: " + signature + " " + randomKey );
        try {

            String concatenatedString = PAYMENT_URL + "secretKey" + randomKey + encryptedPaymentRequest;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(concatenatedString.getBytes(StandardCharsets.UTF_8));
            String controlSignature= Base64Utils.encodeToString(encodedHash).trim().toUpperCase(Locale.ENGLISH);
            return Objects.equals(signature, controlSignature);

        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
