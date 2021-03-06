/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projektuppgift;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author lukasgu
 */
public class Encrypter {
    
    private int CeasarKey;
    private SecretKeySpec AESkey;
    private Cipher AEScipher;
    private static final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();
    
    public Encrypter() throws NoSuchPaddingException, NoSuchAlgorithmException {
        CeasarKey = 5;
        
        // Skapa nyckel   
        KeyGenerator AESgen = KeyGenerator.getInstance("AES");
        AESgen.init(256);
        AESkey = (SecretKeySpec)AESgen.generateKey();
        // MyAESKey = AESkey.getEncoded();

        // Skapa cipher objekt
        AEScipher = Cipher.getInstance("AES");
    }
    
    public byte[] hexadecimalToBytes(String Input){
        int len = Input.length();
        
        if(len % 2 != 0){
            return null;
        }
        
        byte[] bytes = new byte[len/2];
        byte value = 0;
        char c = ' ';
        char d = ' ';
        
        for(int i = 0; i < len/2; i++){
            int index = 2*i;
            c = Input.charAt(index);
            d = Input.charAt(index+1);
            value = (byte)(16*hexaValue(c) + hexaValue(d));
            bytes[i] = value;  
        }
        return bytes;
    }
    
    public int hexaValue(char c){
        return Character.getNumericValue(c);
    }
    
    public String stringToHexadecimal(String Input){
        if (Input == null) return "";
        try{
        return asHex(Input.getBytes("UTF-8"));
        } catch(UnsupportedEncodingException e){         
        }
        return "";
    }

    public String asHex(byte[] buf)
    {
        char[] chars = new char[2 * buf.length];
        for (int i = 0; i < buf.length; ++i)
        {
            chars[2 * i] = HEX_CHARS[(buf[i] & 0xF0) >>> 4];
            chars[2 * i + 1] = HEX_CHARS[buf[i] & 0x0F];
        }
        return new String(chars);
    }
    
    public byte[] Encrypt(byte[] inputBytes, int cryptotype) {
        if(cryptotype == 0){
            return ceasarEncrypt(inputBytes);
        }
        else{
            return aesEncrypt(inputBytes);
        }
    }

    
    public byte[] ceasarEncrypt(byte[] inputBytes){
        byte[] data = inputBytes;
        
        for(int i = 0; i < data.length; i++){
            data[i] = (byte) (data[i] + CeasarKey);
        }
        
        return data;    
    }
    
    public byte[] ceasarDecrypt(byte[] inputBytes, int key) {
        byte[] data = inputBytes;
        
        for(int i = 0; i < data.length; i++){
            data[i] = (byte) (data[i] - key);
        }
        
        return data;
    }
    
    public byte[] aesEncrypt(byte[] inputBytes){
        try {
            // Kryptera
            AEScipher.init(Cipher.ENCRYPT_MODE, AESkey);
            return AEScipher.doFinal(inputBytes);
        
        // Bunch of catch clauses
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(Encrypter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    return null;    
    }    
    
    
    public byte[] aesDecrypt(byte[] inputBytes, byte[] key) {
        try {
            // Avkryptera            
            SecretKeySpec decodeKey = new SecretKeySpec(key, "AES");
            AEScipher.init(Cipher.DECRYPT_MODE, decodeKey); 
            return AEScipher.doFinal(inputBytes);
            
        // bunch of catch statements
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(Encrypter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public int getCeasarKey(){
         return CeasarKey;   
    }
    
    public byte[] getByteAESKey(){
        return AESkey.getEncoded();    
    }
       
}
