import java.util.stream.Stream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.format("Uso: java %s %s\n", "Main","[archivo]");
            System.exit(1);
        }

        //Setup
        BlockingQueue<String> mainFile = readFile(args[0]);
        ReadWriteLock rwLock = new ReentrantReadWriteLock();
        String repartidorFilePath = mainFile.poll();
        String cajerosFilePath = mainFile.poll();
        int numCajeros = Integer.parseInt(mainFile.poll());
        int numMostradores = Integer.parseInt(mainFile.poll());
        String[][] datosMostradores = new String[numMostradores][2];
        for (int i = 0; i < numMostradores; i++)
            datosMostradores[i] = mainFile.poll().split(" ");

        //Cajeros
        FileWriter writerCajerosFile = writeFile(cajerosFilePath, true);
        Cajero[] cajeros = initCajeros(numCajeros, writerCajerosFile, rwLock);
        
        //Repartidor
        BlockingQueue<String> repartidorFile = readFile(repartidorFilePath);
        Repartidor repartidor = new Repartidor(cajeros, repartidorFile);

        //Mostradores
        BlockingQueue<String> cajerosFile = readFile(cajerosFilePath);
        Mostrador[] mostradores = initMostradores(numMostradores, datosMostradores,
            cajerosFilePath, rwLock);

        //Arrancar hilos
        System.out.println("[+] Arrancando hilos");
        ExecutorService executor = Executors.newFixedThreadPool(1 + numCajeros + numMostradores);
        executor.submit(repartidor);
        for (Cajero cajero : cajeros)
            executor.submit(cajero);
        for (Mostrador mostrador : mostradores)
            executor.submit(mostrador);

        //Parar hilos
        executor.shutdown(); 
        try {
            if (!executor.isTerminated())
                if (!executor.awaitTermination(5, TimeUnit.SECONDS))
                     executor.shutdownNow();
            /* El fichero lo cierro aqui porque siempre paso un puntero al archivo
            con el writeFile() y me es mas conveniente cerrarlo una vez ya que
            solo escribo con los cajeros */
            writerCajerosFile.close(); 
        } catch (InterruptedException e) { System.err.println(e); }
        catch (IOException e) { System.err.println(e); }
    }

    // Metodo que devuelve una cola(con block) con las lineas del un archivo a partir de una ruta
    public static BlockingQueue<String> readFile(String path) {
        BlockingQueue<String> parsedFile = new LinkedBlockingQueue<>();
        try (Stream<String> file = Files.lines(Paths.get(path))) {
            file.forEach(parsedFile::add);
        } catch (IOException e) { System.err.println(e); }
        return parsedFile;
    }

    //Metodo que devuelve un FileWriter a partir de una ruta
    public static FileWriter writeFile(String path, boolean append) {
        FileWriter file = null;
        try {
            file = new FileWriter(path, append);
        } catch (IOException e) { System.err.println(e); }
        return file;
    }

    //Metodo para inicializar cajeros
    public static Cajero[] initCajeros(int n, FileWriter file,
            ReadWriteLock lock) {
        Cajero[] cajeros = new Cajero[n];
        for (int i = 0; i < n; i++)
            cajeros[i] = new Cajero(i, file, lock);
        return cajeros;
    }

    //Metodo para incicializar mostradores
    public static Mostrador[] initMostradores(int n, String[][] datos,
            String path, ReadWriteLock lock) {
        Mostrador[] mostradores = new Mostrador[n];
        for (int i = 0; i < n; i++)
            mostradores[i] = new Mostrador(i, Integer.parseInt(datos[i][0]),
                Integer.parseInt(datos[i][1]), path, lock);
        return mostradores;
    }
}
