import java.security.MessageDigest;
import java.util.Scanner;
import java.io.File;

public class MD5 {

    public static void main(String[] args) {
        try {
            String archivo = new Scanner(new File(getFileName(
                args, "<archivo>"))).useDelimiter("\\Z").next();
            System.out.println(byteToHex(getMD5(archivo)));
        } catch(java.io.FileNotFoundException e) {
            System.err.println(e);
        }
    }

    public static String getFileName(String[] args, String params) {
        if (args.length >= 1) return args[0];
        else {
            String caller = Thread.currentThread().
                getStackTrace()[2].getClassName();
            System.out.format("Uso: java %s %s \n", caller, params);
            System.exit(1);
            return null;
        }
    }

    public static String byteToHex(byte[] hash) {
        char[] hexChars = new char[hash.length * 2];
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        for (int i = 0 ; i < hash.length ; i++) {
            int n = hash[i] & 0xFF;
            hexChars[i * 2] = hexArray[n >>> 4];
            hexChars[i * 2 + 1] = hexArray[n & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] getMD5(String s) {
        byte[] hash = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(s.getBytes());
            hash = md5.digest();
        } catch (java.security.NoSuchAlgorithmException e) {
            System.err.println(e);
        }
        return hash;
    }
}
