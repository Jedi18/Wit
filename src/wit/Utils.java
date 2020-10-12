package wit;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

class Utils {
    static String computeSHA1(Object val) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            if(val instanceof byte[]) {
                messageDigest.update((byte[]) val);
            }else if(val instanceof String) {
                messageDigest.update(((String) val).getBytes(StandardCharsets.UTF_8));
            }else{
                throw new IllegalArgumentException("Can't compute SHA-1 of this object type");
            }

            Formatter formatter = new Formatter();
            for(byte b : messageDigest.digest()) {
                formatter.format("%02x", b);
            }

            return formatter.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("System does not support SHA-1");
        }
    }

    static byte[] readContents(File file) {
        if(!file.isFile()) {
            throw new IllegalArgumentException("File argument passed in is not a file.");
        }

        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    static String readContentsAsString(File file) {
        return new String(readContents(file), StandardCharsets.UTF_8);
    }

    static <T extends Serializable> T readObject(File file, Class<T> expectedClass) {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
            T obj = expectedClass.cast(objectInputStream.readObject());
            objectInputStream.close();
            return obj;
        } catch(IOException | ClassCastException | ClassNotFoundException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    static void writeObject(File file, Serializable obj) {
        writeContents(file, serialize(obj));
    }

    static void writeContents(File file, Object content) {
        if(file.isDirectory()) {
            throw new IllegalArgumentException("Cannot write to a directory");
        }

        try {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(Files.newOutputStream(file.toPath()));
            if(content instanceof byte[]) {
                bufferedOutputStream.write((byte[]) content);
            }else{
                bufferedOutputStream.write(((String) content).getBytes(StandardCharsets.UTF_8));
            }

            bufferedOutputStream.close();
        } catch (IOException | ClassCastException e) {
            throw new IllegalArgumentException("File or content passed in to writeContents is invalid");
        }
    }

    /* Serialize object to byte array */
    static byte[] serialize(Serializable obj) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(obj);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
