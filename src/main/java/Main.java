import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Main {

    public static final int ARRAY_CAPACITY = 100_000_00;
    public static int[] temp;
    public static final int TO_MS = 1_000_000;

    public static void main(String[] args) {

        ConcurrentHashMap<Integer, Integer> first = new ConcurrentHashMap<>();
        Map<Integer, Integer> second = Collections.synchronizedMap(new HashMap<>());
        Random random = new Random();
        temp = new int[ARRAY_CAPACITY];
        for (int i = 0; i < ARRAY_CAPACITY; i++) {
            temp[i] = random.nextInt(20000);
        }
        Measurer mesure = new Measurer();
        Measurer mesure1 = new Measurer();
        new Thread(() -> mesure.write(first)).start();
        new Thread(() -> mesure.read(first)).start();
        new Thread(() -> mesure1.write(second)).start();
        new Thread(() -> mesure1.read(second)).start();
    }

    public static class Measurer {

        public void write(Map map) {
            long startTime = System.nanoTime();
            for (int i = 0; i < temp.length; i++) {
                map.put(i, temp[i]);
            }
            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / TO_MS;
            System.out.printf("Время на запись %s составило %d мс\n", map.getClass(), duration);
        }

        public void read(Map map) {
            long startTime = System.nanoTime();
            for (int i = 0; i < temp.length; i++) {
                map.get(i);
            }
            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / TO_MS;
            System.out.printf("Время на чтение %s составило %d мс\n", map.getClass(), duration);
        }
    }
}
