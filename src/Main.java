import java.math.BigInteger;
import java.util.Arrays;
import java.util.Formatter;

public class Main {

    private static int OBJECTS_COUNT;
    private static double SIZE_INCREASE_PROBABILITY;
    private static int NEW_ROUND_WAIT_TIME;
    private static boolean DO_WAIT;
    private static int fullLoops = 0;

    public static void main(String[] args) {

        init(args);

        Object[] objects = new Object[OBJECTS_COUNT];
        int counter = 0;
        while (true) {
            if (counter == objects.length) {

                if (DO_WAIT) {
                    try {
                        Thread.sleep(NEW_ROUND_WAIT_TIME);
                    } catch (InterruptedException ie) {
                    }
                }
                if (Math.random() > SIZE_INCREASE_PROBABILITY) {
                    System.out.printf("[%2d] >>> INCREASING ", fullLoops);
                    objects = increaseSize(objects);
                } else {
                    System.out.printf("[%2d] >>> CLEANING", fullLoops);
                    randomNull(objects, Math.random());
                    counter = 0;
                }
                printStats(objects);
                fullLoops++;
            }
            objects[counter] = generateObject();
            counter++;
        }

    }

    private static void init(String[] args) {
        OBJECTS_COUNT = parseArgs(args, "objects-count", 100);
        SIZE_INCREASE_PROBABILITY = parseArgs(args, "increase-probability", 0.50);
        NEW_ROUND_WAIT_TIME = parseArgs(args, "wait-time", 1000);
        DO_WAIT = parseArgs(args, "wait", true);
    }

    private static int parseArgs(String[] args, String argName, int defaultValue) {
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equalsIgnoreCase(argName)) {
                return Integer.parseInt(args[i + 1]);
            }
        }
        return defaultValue;
    }

    private static double parseArgs(String[] args, String argName, double defaultValue) {
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equalsIgnoreCase(argName)) {
                return Double.parseDouble(args[i + 1]);
            }
        }
        return defaultValue;
    }

    private static boolean parseArgs(String[] args, String argName, boolean defaultValue) {
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equalsIgnoreCase(argName)) {
                return Boolean.valueOf(args[i + 1]);
            }
        }
        return defaultValue;
    }

    private static void printStats(Object[] objects) {
        int lenght = objects.length;
        int notNullsCount = 0;
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] != null) {
                notNullsCount++;
            }
        }
        System.out.printf("%n[%2d] %s: %d (%d operated )%n", fullLoops, "Obiektów", objects.length, notNullsCount);
        System.out.printf("[%2d] memory%n", fullLoops);
        System.out.printf("[%2d] %-13s : %s%n", fullLoops, "Available",
                prettySize(Runtime.getRuntime().maxMemory()));
        System.out.printf("[%2d] %-13s : %s%n", fullLoops, "Booked",
                prettySize(Runtime.getRuntime().totalMemory()));
        System.out.printf("[%2d] %-13s : %s%n", fullLoops, "Free",
                prettySize(Runtime.getRuntime().freeMemory()));
        System.out.printf("[%2d] %-13s : %s%n", fullLoops, "Operated",
                prettySize(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
    }

    private static String prettySize(long size) {
        return new Formatter().format("%4dG %4dMB", size / (int) Math.pow(1024, 3),
                (size % (int) Math.pow(1024, 3)) / (int) Math.pow(1024, 2)).toString();
    }

    private static Object[] increaseSize(Object[] objects) {
        return Arrays.copyOf(objects, objects.length * 2);
    }

    private static Object generateObject() {
        int probe = (int) (Math.random() * 9);

        switch (probe) {
            case 0:
                return printBeforeReturn(new Object());
            case 1:
                return printBeforeReturn((int) (Integer.MAX_VALUE * Math.random()));
            case 2:
                return printBeforeReturn((int) (2 * Math.random()) != 0);
            case 3:
                return printBeforeReturn(new BigInteger("" + ((int) (Integer.MAX_VALUE * Math.random())))
                        .multiply(new BigInteger("" + ((int) (Integer.MAX_VALUE * Math.random())))));
            case 4:
                return printBeforeReturn(new String(generateChar((int) (100 * Math.random()), 'a', 'z')));
            case 5:
                return printBeforeReturn(new int[(int) (1000 * Math.random())]);
            case 6:
                return printBeforeReturn(new boolean[(int) (5000 * Math.random())]);
            case 7:
                return printBeforeReturn(new Object[(int) (1000 * Math.random())]);
            case 8:
            default:
                return printBeforeReturn(null);
        }
    }

    private static void randomNull(Object[] objects, double threshold) {
        for (int i = 0; i < objects.length; i++) {
            objects[i] = Math.random() > threshold ? null : objects[i];
        }
    }

    private static Object printBeforeReturn(Object obj) {
        //System.out.println(obj);
        return obj;
    }

    private static char[] generateChar(int length, char c, char cc) {
        char[] cs = new char[length];
        for (int i = 0; i < cs.length; i++) {
            cs[i] = (char) (c + ((char) ((cc - c) * Math.random())));
        }
        return cs;
    }
}


