package no.ntnu.sjakkarena.utils;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;


/**
 * Handles public and private keys
 */
public class KeyHelper {

    private static SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static String secretKeyFileName = "secretKey.txt";

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

    public static String getPublicKey(){
        return "";
    }

    public static String getPrivateKey(){
        return "";
    }

}
