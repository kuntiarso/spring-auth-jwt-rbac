package com.kuntia.springauthjwtrbac.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;

public abstract class EncryptionUtils {

    public static String encrypt(String plain, String appKey, String appAlgorithm, String appIv) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(appKey.getBytes(), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(appIv.getBytes());
            Cipher cipher = Cipher.getInstance(appAlgorithm);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] encryptedBytes = cipher.doFinal(plain.getBytes("UTF-8"));
            String encrypted = Hex.encodeHexString(encryptedBytes);
            return encrypted;
        } catch (Exception e) {
            throw new RuntimeException("encrypt failed");
        }
    }

    public static String decrypt(String encrypted, String appKey, String appAlgorithm, String appIv) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(appKey.getBytes(), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(appIv.getBytes());
            Cipher cipher = Cipher.getInstance(appAlgorithm);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] decryptedBytes = cipher.doFinal(Hex.decodeHex(encrypted));
            String decrypted = new String(decryptedBytes, "UTF-8");
            return decrypted;
        } catch (Exception e) {
            throw new RuntimeException("decrypt failed");
        }
    }

}
