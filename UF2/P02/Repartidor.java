import java.util.Queue;
import java.util.Vector;
import java.util.Arrays;

public class Repartidor implements Runnable {
    Cajero[] cajeros;
    Queue<String> file;

    public Repartidor(Cajero[] cajeros, Queue<String> file) {
        this.cajeros = cajeros;
        this.file = file;
    }

    /* Lee una linea de la cola, crea una entrada con los valores de esta,
    syncroniza el vector de el cajero en cuestion, la inserta y notifica */
    public void run() {
        System.out.println("[Repartidor] El archivo tiene " + file.size() + " lineas");
        while(file.peek() != null) {
            System.out.println("[Repartidor] Leyendo linea");
            String[] line = file.poll().split(" ");
            System.out.println("[Repartidor] Linea leida: " + Arrays.deepToString(line));
            int nCajero = Integer.parseInt(line[0]);
            String producto = line[1], nItems = line[2], tiempo = line[3];
            Entrada entrada = new Entrada(producto, nItems, tiempo);
            Vector<Entrada> compraCajero = cajeros[nCajero].compra;
            System.out.println("[Repartidor] Entrada creada");
            synchronized (compraCajero) {
                System.out.println("[Repartidor] Insertando entrada para cajero " + nCajero);
                compraCajero.add(entrada);
                compraCajero.notify();
                System.out.println("[Repartidor] Entrada insertada en cajero " + nCajero);
            }
            try { Thread.sleep(200); }
            catch (InterruptedException e) { System.err.println(e); }
        }
    }
}
