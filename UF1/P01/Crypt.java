import java.util.Scanner;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

class Crypt {
    /*
    1- Hacer un programa que lea un fichero de entrada con el siguiente formato:

        nomusuario1:clave1:
        nomusuario2:clave2:
        nomusuario3:clave3:
        nomusuario4:clave4:

        y nos genere un fichero con el siguiente formato:

        nomusuario1:aleatorio de 6 cifras:[hash md5 en base64 de la clave concatenada con el número aleatorio]:
        nomusuario2:aleatorio de 6 cifras:[hash md5 en base64 de la clave concatenada con el número aleatorio]:
        nomusuario3:aleatorio de 6 cifras:[hash md5 en base64 de la clave concatenada con el número aleatorio]:
        nomusuario3:aleatorio de 6 cifras:[hash md5 en base64 de la clave concatenada con el número aleatorio]:

        ---

        2- Hacer un programa que pida un usuario de la lista del fichero anterior y una clave. Accediendo al segundo fichero ha de comprobar si la clave es correcta o no.

        Ayudas:

        Para separar un string en partes, como en el ejemplo:

        String s[] = str.split(":");
        después de esto tendremos que s[0] es el nomusuario, s[1] es la clave,...
    */

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
        //intervalo efectivo [min, max]
        return (int) (Math.random() * (max-min+1) + min);
    }

    public static String getFileName(String[] args) {
        if (args.length >= 1) {
            return args[0];
        } else {
            System.out.println("Uso: java Crypt <input>");
            System.exit(1);
            return null;
        }
    }

    public static void writeLines(String name) {
        File file;

    }

    public static void main(String[] args) {
        // System.out.println(random(100000, 9999999));
        System.out.println(readLines(getFileName(args)));
        writeLines(getFileName(args));
    }
}
