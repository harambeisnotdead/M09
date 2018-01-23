import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReadWriteLock;

public class Mostrador implements Runnable {
    int numero;
    int tInicio;
    int tLinea;
    String path;
    BlockingQueue<String> file;
    ReadWriteLock lock;

    public Mostrador(int numero, int tInicio, int tLinea, 
            String path, ReadWriteLock lock) {
        this.numero = numero;
        this.tInicio = tInicio;
        this.tLinea = tLinea;
        this.path = path;
        this.lock = lock;
        file = Main.readFile(path);
    }

    /* Lee el archivo en la cola, bloquea, procesa, duerme tLinea ms, desbloquea
    y muestra la linea si no esta en memo, si no muestra una linea nueva en
    maxTries el bucle acaba, finalmente duerme tInicio ms*/
    public void run() {
        String lines = "", memo = "";
        Boolean stop = false;
        int tries = 0, maxTries = 3;
        do {
            file = Main.readFile(path);
            try {
                lock.readLock().lock();
                System.out.printf("[Mostrador %d] Archivo bloqueado\n", numero);
                while (file.peek() != null) {
                    System.out.printf("[Mostrador %d] Procesando linea\n", numero);
                    String[] line = file.poll().split(" ");
                    String tmpLine = String.format("%d %s %s\n", numero, line[1], line[2]);
                    lines += (memo.contains(tmpLine)) ? "":tmpLine;
                    memo += lines;
                    Thread.sleep(tLinea);
                }
                lock.readLock().unlock();
                System.out.printf("[Mostrador %d] Archivo desbloqueado\n", numero);
                if (!lines.isEmpty())
                    System.out.printf("[Mostrador %d] Resultado:\n%s\n", numero, lines);
                else {
                    stop = (tries++ == maxTries) ? true:false;
                    System.out.printf("[Mostrador %d] No hay lineas nuevas\n", numero);
                }
                lines = "";
                Thread.sleep(tInicio);
            } catch (InterruptedException e) { System.err.println(e); }
        } while (!stop);
    }
}
