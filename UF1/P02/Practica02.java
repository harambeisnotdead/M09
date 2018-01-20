import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.CountDownLatch;

class Practica02 {
    static int size = 1000;
    static int[] v = new int[size];
    static int index = 0;
    static CountDownLatch filledSignal = new CountDownLatch(2);

    static void fillArray() {
        while (index<size) {
            synchronized(v) {
                int r = ((int) (Math.random() * 6) + 1);
                v[index] = r;
                index++;
            }
        }
        filledSignal.countDown();
    }

    static void shiftOnePos(int i, int f) {
        for (int j = i; j<f; j++)
            v[j] = v[j+1];
        v[f] = 0;
    }

    static void delRepeated(int i, int f) {
        if(i < f) { 
            if (v[i] == v[i+1]) {
                shiftOnePos(i, f);
                delRepeated(i, f-1);
                shiftOnePos(i, f);
            } else delRepeated(i+1, f);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(22);
        executor.submit(() -> fillArray());
        executor.submit(() -> fillArray());
        filledSignal.await(500, TimeUnit.MILLISECONDS);
        System.out.println(Arrays.toString(v) + '\n');
        executor.submit(() -> delRepeated(0, size-1));
        executor.shutdown();
        while(!executor.isTerminated())
            executor.shutdownNow();
        System.out.println(Arrays.toString(v));
    }
}
