package Experiment;

import StarterCode.BiglyInt;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Random;

/*
Why do I feel like this is actually skipping the results of many things?
 */
public class Experiment implements Runnable{

    public static void main(String[] args) throws FileNotFoundException {
        new Experiment(10,500000,5000,n -> ((int) Math.ceil(n * 1.5)),new PrintStream("results3.csv")).runExperiment();
    }

    private static final boolean USE_NEGATIVE = false;

    private Step step;
    private PrintStream out;
    private int digits, lastDigits, threadCount;
    private final int maxDigits, trials;
    private long[] time;
    private BiglyInt[][] integers;
    private int[] integersAdded, currentTrial;
    private StringBuilder[] intStrings;
    private Random random = new Random();

    public Experiment(int minDigits, int maxDigits, int trials, Step step, PrintStream out) {
        this.digits = minDigits;
        this.maxDigits = maxDigits;
        this.step = step;
        this.out = out;
        this.trials = trials;
        intStrings = new StringBuilder[(int) (Math.ceil(Math.sqrt(2 * trials)))];
        for(int i = 0; i < intStrings.length; i++) {
            intStrings[i] = new StringBuilder().append(USE_NEGATIVE && random.nextBoolean() ? '-': '+');
        }
    }

    public void runExperiment() {
        out.print("digits");
        for(Algorithm algorithm : Algorithm.values()) {
            out.print("," + algorithm.name);
        }
        out.println();
        do {
            digits = step.next(digits);
            updateString();
            System.out.println("Starting " + digits);

            //Clear vals
            integersAdded = new int[3];
            currentTrial = new int[3];
            time = new long[3];
            integers = new BiglyInt[3][intStrings.length];

            //Start threads
            ThreadGroup threads = new ThreadGroup("main");
            threadCount = Runtime.getRuntime().availableProcessors();
            for(int i = 0; i < threadCount; i++) {
                new Thread(threads,this,Integer.toString(i)).start();
            }

            while(threads.activeCount() > 0);

            out.print(digits);
            for(int i = 0; i < Algorithm.values().length; i++) {
                out.print("," + ((double) time[i] / (double) trials * 1e-9));
            }
            out.println();

            lastDigits = digits;
        } while(digits < maxDigits);
        out.close();
    }

    private void updateString() {
        int count = digits - lastDigits;
        for(int i = 0; i < intStrings.length; i++) {
            char[] add = new char[count];
            for(int j = 0; j < add.length; j++) {
                add[j] = (char) ('1' + random.nextInt(9));
            }
            intStrings[i].append(add);
        }
    }

    public void run() {
        int algorithmCount = Algorithm.values().length;
        for(int i = 0; i < algorithmCount; i++) {
            Algorithm algorithm = Algorithm.values()[i];
            while(integersAdded[i] < intStrings.length) {
                int index = integersAdded[i]++;
                integers[i][index] = algorithm.factory.get(intStrings[index].toString());
            }
        }
        for(int i = 0; i < algorithmCount; i++) {
            while(currentTrial[i] < trials) {
                int trial = currentTrial[i]++;
                int a = 0;
                while(trial + a >= integers[i].length) {
                    trial -= (integers[i].length - a);
                    a++;
                }
                int b = a + trial;
                while(integers[i][a] == null || integers[i][b] == null);
                long start = System.currentTimeMillis();
                integers[i][a].multiply(integers[i][b]);
                long end = System.currentTimeMillis();
                time[i] += (end - start);
            }
        }
    }


    interface Step {
        int next(int prev);
    }
}
