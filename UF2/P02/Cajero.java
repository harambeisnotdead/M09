import java.util.Vector;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.locks.ReadWriteLock;

public class Cajero implements Runnable {
    Vector<Entrada> compra;
    int numero;
    FileWriter file;
    ReadWriteLock lock;

    public Cajero (int numero, FileWriter file, ReadWriteLock lock) {
        this.numero = numero;
        this.file = file;
        this.lock = lock;
        compra = new Vector<>();
    }

    /* Espera al notify() del repartidor, coge la primera entrada del vector y
    la elimina, bloquea, escribe la linea, desbloquea y duerme un tiempo */
    public void run() {
        synchronized(compra) {
            try {
                do {
                    compra.wait();
                    Entrada entrada = compra.firstElement();
                    System.out.printf("[Cajero %d] Entrada guardada\n", numero);
                    if (compra.removeElement(entrada))
                        System.out.printf("[Cajero %d] Entrada eliminada de compra\n", numero);
                    lock.writeLock().lock();
                    System.out.printf("[Cajero %d] Lock bloqueado\n", numero);
                    try {
                        System.out.printf("[Cajero %d] Intentando escribir\n", numero);
                        file.write(String.format("%d %s %s\n", numero, 
                            entrada.codigoProducto, entrada.cantidad));
                        file.flush();
                        System.out.printf("[Cajero %d] Entrada escrita\n", numero);
                    } catch (IOException e) { System.err.println(e); }
                    finally {
                        lock.writeLock().unlock();
                        System.out.printf("[Cajero %d] Lock desbloqueado\n", numero);
                        Thread.sleep(Long.parseLong(entrada.tiempoServicio));
                    }
                } while (true);
            } catch (InterruptedException e) { System.err.println(e); }
        }
    }
}
