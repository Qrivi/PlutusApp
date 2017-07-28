package be.plutus.common;

import be.plutus.core.exception.EncryptionException;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class CryptoService{

    public static String sha256( String plainText, String key ){
        String encryptedText;

        try{
            Mac sha256HMAC = Mac.getInstance( "HmacSHA256" );
            SecretKeySpec secret_key = new SecretKeySpec( key.getBytes( "UTF-8" ), "HmacSHA256" );
            sha256HMAC.init( secret_key );

            encryptedText = new String( Hex.encodeHex( sha256HMAC.doFinal( plainText.getBytes( "UTF-8" ) ) ) );
        }catch( Exception e ){
            e.printStackTrace();
            throw new EncryptionException( e );
        }
        return encryptedText;
    }

    public static String encrypt( String plainText, String key ){
        String encryptedText;

        try{
            SecretKeySpec keySpec = new SecretKeySpec( key.getBytes(), "Blowfish" );
            Cipher cipher = Cipher.getInstance( "Blowfish" );
            cipher.init( Cipher.ENCRYPT_MODE, keySpec );
            byte[] encrypted = cipher.doFinal( plainText.getBytes() );
            encryptedText = new String( encrypted );

        }catch( Exception e ){
            e.printStackTrace();
            throw new EncryptionException( e );
        }
        return encryptedText;
    }

    public static String decrypt( String encryptedText, String key ){
        String plainText;

        try{
            SecretKeySpec keySpec = new SecretKeySpec( key.getBytes(), "Blowfish" );
            Cipher cipher = Cipher.getInstance( "Blowfish" );
            cipher.init( Cipher.DECRYPT_MODE, keySpec );
            byte[] decrypted = cipher.doFinal( encryptedText.getBytes() );
            plainText = new String( decrypted );

        }catch( Exception e ){
            e.printStackTrace();
            throw new EncryptionException( e );
        }
        return plainText;
    }
}
