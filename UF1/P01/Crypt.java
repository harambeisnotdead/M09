import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.util.Base64;

public class Crypt {

    public static ArrayList<String> readLines(String file) {
        String line;
        ArrayList<String> lines = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while ((line = br.readLine()) != null) lines.add(line);
        } catch (java.io.FileNotFoundException e) {
            System.err.println("Archivo '" + file + "' no encontrado");
        } catch (java.io.IOException e) {
            System.err.println("Error al leer el archivo '" + file +"'");
        }
        return lines;
    }

    public static int random(int min, int max) {
        return (int) (Math.random() * (max-min+1) + min);
    }

    public static void writeLines(ArrayList<String> lines, String[] args) {
        String outputFile = "output.txt";
        if (args.length > 1) outputFile = args[1];
        try (Writer writer = new OutputStreamWriter(
                new FileOutputStream(outputFile), "utf-8")) {
            String del = ":";
            for (String line : lines) {
                String[] fields = line.split(del);
                String user = fields[0];
                int salt = random(100000, 999999);
                String hashMD5Base64 = Base64.getEncoder().encodeToString(
                    MD5.getMD5(user+salt));
                writer.write(user + del + salt + del + hashMD5Base64 + "\n");
            }
        } catch (java.io.IOException e) {
            System.err.println("Error al escribir el archivo "+outputFile);
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            System.err.println(
                "Una o mas lineas del archivo tienen errores de formato");
        }
    }

    public static void main(String[] args) {
        ArrayList<String> file = readLines(MD5.getFileName(
            args, "<archivo> [output]"));
        writeLines(file, args);
    }
}
