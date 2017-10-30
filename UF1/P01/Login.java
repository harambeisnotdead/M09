import java.util.Scanner;
import java.util.ArrayList;
import java.util.Base64;

class Login {

    public static String getInput(String text) {
        Scanner s = new Scanner(System.in);
        System.out.print(text + ": ");
        return s.nextLine();
    }

    public static boolean checkUser(String inputUser, String inputPasswd,
            ArrayList<String> users) {
        String del = ":";
        for (String line : users) {
            String[] fields = line.split(del);
            String user = fields[0], hash = fields[2];
            int salt = Integer.parseInt(fields[1]);
            String inputPasswdHash = Base64.getEncoder().encodeToString(
                MD5.getMD5(user+salt));
            if (user.equals(inputUser)) {
                if (hash.equals(inputPasswdHash)) return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        String user = getInput("Introduce tu usuario");
        String passwd = getInput("Introduce tu contraseña");
        ArrayList<String> file = Crypt.readLines(
            MD5.getFileName(args, "<archivo>"));
        if (checkUser(user, passwd, file)) {
            System.out.println("Usuario encontrado y validado correctamente");
        } else {
            System.out.println(
                "Usuario no encontradoo o no validado correctamente");
        }
    }
}
