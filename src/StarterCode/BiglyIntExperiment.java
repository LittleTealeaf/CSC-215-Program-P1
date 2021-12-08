package StarterCode;
/**********
 * BiglyIntExperiment
 * Author: Christian Duncan
 * Modified by: ...
 *
 * This class performs a simple experiment on a few BigInteger (BiglyInt) implementations of
 * the integer multiplication algorithm.
 *    0) n^2 algorithm
 *    1) Karatsuba's algorithm
 *    2) Java's BigInteger version - should be most efficient.
 *
 * This experiment uses the following approach: Each row is a different size, each column a different method
 * TO DO:
 *    1) Edit these comments!
 *    2) Make it work for the correct sizes
 *    3) Make it perform multiple repetitions for a given size and method
 *    4) Make it actually run a single experiment (create two numbers, multiply them, measure time taken)
 ***********/
import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class BiglyIntExperiment {
    public final static int NUM_REPS = 1000;
    public final static String FILE_NAME = "results.csv";
    public final static double CUT_OFF_TIME = 1;  // In seconds
    public final static String[] METHOD_NAMES = { "Regular", "Karatsuba", "Java's BigInteger" };
    
    public static void main(String[] args) {
        try {
            // Open the file for saving
            PrintWriter out = new PrintWriter(FILE_NAME);

            // Create header portion
            out.print("Size");
            for (String m: METHOD_NAMES) out.print(", " + m);
            out.println();

            // So we can cut-off the slower methods (ttl = took too long)
            boolean[] ttl = new boolean[METHOD_NAMES.length];  // Default is FALSE for each method
            
            // Now try various sizes
            for (int s = 10; s < 1000; s++) {
                out.print(s);

                // Now try all the different methods
                for (int m = 0; m < METHOD_NAMES.length; m++) {
                    System.out.println("Method: " + METHOD_NAMES[m] + " Size: " + s);
                    if (ttl[m]) {
                        // This method has taken too long already, skip it
                        System.out.println("   Skipping this method");
                        out.print(",");
                        continue;
                    }

                    // Run the experiment for one method and the given size
                    long totalTime = 0;

                    for (int r = 0; r < NUM_REPS; r++) {
                        // For each rep, create two random numbers of given size and the numbers for them
                        totalTime += runExperiment(s, m);
                    }
                    
                    double aveTime = (double) totalTime / (double) NUM_REPS * 1e-9;  // Convert to average time in Seconds
                    out.print(", " + aveTime);
                    if (aveTime > CUT_OFF_TIME)
                        ttl[m] = true;  // Took too long, skip it from now on...
                }
                out.println();
            }

            // Save the file!
            out.close();
        } catch (FileNotFoundException e) {
            System.err.println("Can't open file for saving results: " + FILE_NAME);
            System.err.println("   Message: " + e.getMessage());
        }
    }

    // Run a single experiment for the given size and using the specified method
    // Returns the time taken in nanoseconds
    private static long runExperiment(int size, int method) {
        // Create two random numbers of the given size and the BiglyInt
        //   See the BiglyIntDemo on how to create two numbers of the particular method (0,1,2)

        // Get the start time
        long startTime = System.nanoTime();

        // Multiply the two numbers (see the BiglyIntDemo)

        // Get the end time
        long endTime = System.nanoTime();

        return endTime - startTime;  // Return the time taken for this experiment
    }
}
