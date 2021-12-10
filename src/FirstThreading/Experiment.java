package FirstThreading;

import StarterCode.BiglyInt;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Random;

@Deprecated
public class Experiment {

    private static boolean USE_NEGATIVE = false;
    private static Random RANDOM = new Random();

    private NextInt nextInt;
    private int digits, maxDigits, valuesLength, trials;
    private StringBuilder[] values;

    public Experiment(int startDigits, NextInt nextInt, int maxDigits, int trialsPer) {
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

    private void updateValues() {
        while(valuesLength < digits) {
            for(int i = 0; i < values.length; i++) {
                values[i].append((char) ('1' + RANDOM.nextInt(9)));
            }
            valuesLength++;
        }
    }

    public void runExperiment(PrintStream out) throws IOException {
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
//                System.out.println("\t" + method.toString());
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
