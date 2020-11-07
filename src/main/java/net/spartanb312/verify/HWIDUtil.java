package net.spartanb312.verify;

import com.google.common.hash.Hashing;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class HWIDUtil {

    public static byte[] rawHWID() throws NoSuchAlgorithmException {
        String main = System.getenv("PROCESS_IDENTIFIER")
                + System.getenv("PROCESSOR_LEVEL")
                + System.getenv("PROCESSOR_REVISION")
                + System.getenv("PROCESSOR_ARCHITECTURE")
                + System.getenv("PROCESSOR_ARCHITEW6432")
                + System.getenv("NUMBER_OF_PROCESSORS")
                + System.getenv("COMPUTERNAME");
        byte[] bytes = main.getBytes(StandardCharsets.UTF_8);
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        return messageDigest.digest(bytes);
    }

    public static String Encrypt(String strToEncrypt, String secret) {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, getKey(secret));
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        }
        catch (Exception e)
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }


    public static SecretKeySpec getKey(String myKey) {
        MessageDigest sha;
        try {
            byte[] key = myKey.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
             return new SecretKeySpec(key, "AES");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getEncryptedHWID(String key){
        try {
            String a = Hashing.sha1().hashString(new String(rawHWID(), StandardCharsets.UTF_8), StandardCharsets.UTF_8).toString();
            String b = Hashing.sha256().hashString(a, StandardCharsets.UTF_8).toString();
            String c = Hashing.sha512().hashString(b, StandardCharsets.UTF_8).toString();
            String d = Hashing.sha1().hashString(c, StandardCharsets.UTF_8).toString();
            return Encrypt(d,"spartanB312" + key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "null";
    }

}
