package no.ntnu.sjakkarena.utils;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

/**
 * Handles public and private keys
 */
public class KeyHelper {

    private static SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static String secretKeyFileName = "secretKey.txt";

    private static final KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.ES256);
    private static final String  KEYPAIR_FILENAME = "keypair.ser";

    // https://stackoverflow.com/questions/30445997/loading-raw-64-byte-long-ecdsa-public-key-in-java
    private static byte[] P256_HEAD = Base64.getDecoder()
            .decode("MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgA");


    /**
     * Return secret key
     *
     * @return secret key
     */
    public static Key getKey() {
        return readKeyFromFile(secretKeyFileName);
    }

    /**
     * Writes a secret key to a file
     */
    public static void writeKeyToFile() {
        try {
            Path path = Paths.get(secretKeyFileName);
            if (!Files.exists(path)) {
                FileOutputStream fileOutputStream = new FileOutputStream(new File(secretKeyFileName));
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(key);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads a key from a file
     *
     * @param filename the file to read the key from
     * @return a key from a file
     */
    public static Key readKeyFromFile(String filename) {
        Key readKey = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(filename));
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            readKey = (Key) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return readKey;
    }

    public static void writeKeyPairToFile(){
        Path path = Paths.get(KEYPAIR_FILENAME);
        if(!Files.exists(path)) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(new File(KEYPAIR_FILENAME));
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(key);
            } catch (IOException io) {
                io.printStackTrace();
            }
        };
    }

    public static PrivateKey getPrivateKey() {
//        System.out.println("PRIVATE KEY");
//        System.out.println("------------------------------------------------------");
//        System.out.println(keyPair.getPrivate().toString());
//        System.out.println("------------------------------------------------------");
//        System.out.println("------------------------------------------------------");
        return keyPair.getPrivate();
    }

    /*
     * Adapted from: https://golb.hplar.ch/2019/08/webpush-java.html
     */
    public static String publicKey() {
//        System.out.println(Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(keyPair.getPublic().getEncoded()));
//        System.out.println("-------------------------------------------------------");
//        System.out.println("-------------------------------------------------------");
//        System.out.println(Arrays.toString(keyPair.getPublic().getEncoded()));
//        System.out.println("-------------------------------------------------------");
//        System.out.println("-------------------------------------------------------");
//        System.out.println(Base64.getMimeEncoder(8, ",".getBytes()).encodeToString(keyPair.getPublic().getEncoded()));
//        System.out.println("-------------------------------------------------------");
//        System.out.println("-------------------------------------------------------");
//        System.out.println(keyPair.getPublic().getFormat());
//        System.out.println("-------------------------------------------------------");
//        System.out.println("-------------------------------------------------------");
//        System.out.println(keyPair.getPublic().getAlgorithm());
//        System.out.println("-------------------------------------------------------");
//        System.out.println("-------------------------------------------------------");
//        System.out.println(keyPair.getPublic().toString());
//        System.out.println("-------------------------------------------------------");
//        System.out.println("-------------------------------------------------------");
//        X509EncodedKeySpec publicKeyX509 = new X509EncodedKeySpec(keyPair.getPublic().getEncoded());
//        System.out.println(Arrays.toString(ost.getEncoded()));
//        System.out.println("-------------------------------------------------------");
//        System.out.println("-------------------------------------------------------");
//        System.out.println(Arrays.toString(publicKeyX509.getEncoded()));
        byte[] encoded = keyPair.getPublic().getEncoded();
        byte[] result = new byte[65];
        System.arraycopy(encoded, P256_HEAD.length, result, 0,
                encoded.length - P256_HEAD.length);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(result);
    }

    public static PublicKey getPublicKey(){
        return keyPair.getPublic();
    }
}
