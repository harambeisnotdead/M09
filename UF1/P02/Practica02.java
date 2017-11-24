import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Practica02v2 {
    static int[] v = new int[10];
    static int index = 0;

    static void fillArray() {
        while (index<10) {
            synchronized(v) {
                int r = ((int) (Math.random() * 6) + 1);
                v[index] = r;
                System.out.println("[*] " + Thread.currentThread().getName()
                + " -> ["+index+"] = "+ r + " - " + v[index]);
                index++;
            }
            // try {
            //     TimeUnit.MILLISECONDS.sleep(100);
            // } catch (InterruptedException e) {}
        }
    }

    static void delRepetidos(int i, int f) {
        System.out.format("[+]\n");
        if (i < f) {
            if (v[i] == v[i+1]) {
                v[i] = 0;
                delRepetidos(i+1, f);
                v[i+1] = 0;
            } else delRepetidos(i+1, f);
        }
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(v));
        ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.submit(() -> fillArray());
        executor.submit(() -> fillArray());
        executor.submit(() -> delRepetidos(0, 9));
        executor.shutdown();
        while(!executor.isTerminated()) executor.shutdownNow();
        if (executor.isTerminated()) System.out.println(Arrays.toString(v));
    }
}
