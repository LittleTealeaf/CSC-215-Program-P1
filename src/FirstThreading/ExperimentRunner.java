package FirstThreading;

import StarterCode.BiglyInt;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Random;

/**
 * The first attempted rewrite of the program. This implementation creates a thread for each logical core of the processor for each method whenever
 * it runs that method.
 * @deprecated More efficient use of threads is used in {@link Experiment.Experiment}
 * @author Thomas Kwashnak
 */
@Deprecated
public class ExperimentRunner {

    public static void main(String[] args) throws IOException {
        new ExperimentRunner(10, n -> ((int) Math.ceil(n * 1.5)), 500000, 100).runExperiment(new PrintStream("results2.csv"));
    }

    private static boolean USE_NEGATIVE = false; //Whether to use a negative
    private static Random RANDOM = new Random();

    private NextInt nextInt; //The method used to increment the digit count
    //Digits is the current digit, maxDigits is the maxDigits, valuesLength is the length of the String values, and Trials is the number of trials
    private int digits, maxDigits, valuesLength, trials;
    //Strings are kept in an array of StringBuilders to be appended on after each time
    private StringBuilder[] values;

    public ExperimentRunner(int startDigits, NextInt nextInt, int maxDigits, int trialsPer) {
        this.digits = startDigits;
        this.maxDigits = maxDigits;
        this.trials = trialsPer;
        this.nextInt = nextInt;


        int valCount = (int) Math.ceil(Math.sqrt(3 * trialsPer));
        values = new StringBuilder[valCount];
        for(int i = 0; i < values.length; i++) {
            values[i] = new StringBuilder().append(USE_NEGATIVE && RANDOM.nextBoolean() ? '-': '+');
        }
        valuesLength = 0;
        updateValues();
    }

    /**
     * Updates the length of the strings until they are all length `digits`
     */
    private void updateValues() {
        while(valuesLength < digits) {
            for(int i = 0; i < values.length; i++) {
                values[i].append((char) ('1' + RANDOM.nextInt(9)));
            }
            valuesLength++;
        }
    }

    /**(
     * Runs the experiment and prints the data to a PrintStream
     * @param out Stream to print the results
     */
    public void runExperiment(PrintStream out) {
        /*
        Did not comment in after-thought, more thought-out comments will be made in the most recent implementation
         */
        out.print("size");
        for(BiglyMethod method : BiglyMethod.values()) {
            out.print("," + method.toString());
        }
        out.println();
        while(digits < maxDigits) {
            digits = nextInt.nextInt(digits);
            updateValues();
            out.print(digits);
            System.out.println("Digits: " + digits);
            for(BiglyMethod method : BiglyMethod.values()) {
                BiglyInt[] integers = generateIntegers(method);
                double time = new DataCollector(integers,trials).runExperiment();
                out.print("," + time);
            }
            out.println();
        }
        out.close();
    }

    public BiglyInt[] generateIntegers(BiglyMethod method) {
        BiglyInt[] intValues = new BiglyInt[values.length];
        for(int i = 0; i < values.length; i++) {
            intValues[i] = method.intGenerator.generate(values[i].toString());
        }
        return intValues;
    }

    public interface NextInt {
        int nextInt(int previous);
    }
}
