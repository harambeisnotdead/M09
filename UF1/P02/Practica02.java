import java.util.Arrays;
import java.util.concurrent.*;
import java.util.List;

class Practica02 {
    static int[] v = new int[1000];
    static List<Integer> memo;

    static {
        for (int i=0; i<1000; i++) v[i] = i%6 + 1;
    }

    static void delNotUnique(int i, int f) {
        for (; i<=f; i++) {
            for (int j=0; j<memo.size(); j++) {
                if (v[i] != memo.get(j)) memo.add(v[i]);
            }
        }

    }

    public static void main(String[] args) {
        // System.out.println(Arrays.toString(v));
        ExecutorService executor = Executors.newFixedThreadPool(4);
        Runnable task = () -> delNotUnique(0, 250);
        executor.submit(task);
        executor.shutdown();
    }
}
