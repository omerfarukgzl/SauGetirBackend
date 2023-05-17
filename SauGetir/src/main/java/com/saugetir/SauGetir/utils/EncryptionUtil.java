package com.saugetir.SauGetir.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import static com.saugetir.SauGetir.utils.Constant.*;

@Component
public class EncryptionUtil {

    public String encrypt(String data,String secret_Key){
        try {
            SecretKeySpec secretKey = new SecretKeySpec(secret_Key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64Utils.encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return "Decrypting Error";
        }
    }
    public String decrypt(String encryptedData,String secret_Key){
        try {
            SecretKeySpec secretKey = new SecretKeySpec(secret_Key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] encryptedBytes = Base64Utils.decodeFromString(encryptedData);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return "Decrypting Error";
        }
    }

    public String signature( String randomKey, String encryptedData,String secretKey) {
    	try {
    		String concatenatedString =  secretKey + randomKey + encryptedData;
    		MessageDigest digest = MessageDigest.getInstance("SHA-256");
    		byte[] encodedHash = digest.digest(concatenatedString.getBytes(StandardCharsets.UTF_8));
    		String controlSignature= Base64Utils.encodeToString(encodedHash).trim().toUpperCase(Locale.ENGLISH);
    		return controlSignature;
    	}catch (Exception e) {
    		e.printStackTrace();
    		return "Signature Error";
    	}
    }

    public Boolean checkSignature(String signature,String randomKey,String encryptedRequest,String secretKey){
        try {
            String concatenatedString =  secretKey + randomKey + encryptedRequest;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(concatenatedString.getBytes(StandardCharsets.UTF_8));
            String controlSignature= Base64Utils.encodeToString(encodedHash).trim().toUpperCase(Locale.ENGLISH);
            return Objects.equals(signature, controlSignature);

        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String generateRandomKey() {
        return UUID.randomUUID().toString();
    }











}
