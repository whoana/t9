package lemon.balm.core.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class CipherUtil {
    private static int len = 256;
    private static char[] algorithm = { 65, 69, 83 };
    private static char[] cipherAlgorithm = { 65, 69, 83, 47, 67, 66, 67, 47, 80, 75, 67, 83, 53, 80, 65, 68, 68, 73, 78, 71 };

    public static byte[] encode(byte[] data, SecretKey key, byte[] iv) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(new String(cipherAlgorithm));
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
        return cipher.doFinal(data);
    }

    public static byte[] decode(byte[] data, SecretKey key, byte[] iv) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(new String(cipherAlgorithm));
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
        return cipher.doFinal(data);
    }

    public static byte[] createInitializationVector() {

        // Used with encryption
        byte[] initializationVector = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(initializationVector);
        return initializationVector;
    }

    public static SecretKey generateSecretKey() throws Exception {

        SecureRandom random = new SecureRandom();
        KeyGenerator keyGenerator = KeyGenerator.getInstance(new String(algorithm));
        keyGenerator.init(len, random);
        return keyGenerator.generateKey();

    }

    public static void printCharToByte(String str){
        byte[] d = str.getBytes();
        System.out.print("[");
        for (int i = 0 ; i < d.length ; i++) {
            if(i == d.length - 1){
                System.out.print(d[i]);
            }else{
                System.out.print(d[i] + ",");
            }
        }
        System.out.print("]\n");
    }

    public static void exportObject(String pathAndName, Object data) throws FileNotFoundException, IOException{
        
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(pathAndName));
            oos.writeObject(data);
            oos.flush();
        }finally{
            if(oos != null)
                try {
                    oos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }
    }

    public static Object importObject(String pathAndName) throws FileNotFoundException, IOException, ClassNotFoundException{
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(pathAndName));
            return ois.readObject();
        } finally {
            if(ois != null)
                try {
                    ois.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }
    }

    public static void main(String[] args) {
        //printCharToByte("AES/CBC/PKCS5PADDING");
       
 
        try {
            String dataString = "I am a student.";
            byte[] data = dataString.getBytes();
            SecretKey key = generateSecretKey();
            exportObject("iwannakey.dat", key); 
            byte[] iv = createInitializationVector();
            exportObject("iv.dat", iv);
            //System.out.println("The Symmetric Key is1 :" + DatatypeConverter.printHexBinary(key.getEncoded()));
            SecretKey okey = (SecretKey)importObject("iwannakey.dat");             
            //System.out.println("The Symmetric Key is2 :" + DatatypeConverter.printHexBinary(okey.getEncoded()));
            byte[] oiv = (byte[])importObject("iv.dat");
            byte[] endodingData = encode(data, okey, oiv);
            String endodingDataString = new String(endodingData);
            byte[] decodedData = decode(endodingData, key, iv);
            String decodedDataString = new String(decodedData);

            System.out.println("origin data string:" + dataString);
            System.out.println("encoding data string["+endodingData.length+"]:" + endodingDataString);
            System.out.println("decoded data string:" + decodedDataString);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
