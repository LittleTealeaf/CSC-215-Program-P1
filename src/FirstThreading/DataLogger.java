package FirstThreading;

import java.io.*;

@Deprecated
public class DataLogger {

    public static final int THREAD_COUNT = 1;

    public static void main(String[] args) throws IOException {
        new Experiment(10,n -> ((int) Math.ceil(n * 1.5)),500000,100).runExperiment(new PrintStream("results2.csv"));
    }
}
