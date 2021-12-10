package Experiment;

import StarterCode.BiglyInt;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Random;

/**
 * So basically.. this is the biggest, the baddest, and the...
 * Well maybe not, there's a ton more optimization that I will probably be going back and making FASTER! :)
 * <br>
 * Anyways... Hopefully my comments help make it more easy to understand. Here I will put the basic idea.
 *
 * <p>
 *     So first optimization from the original implementation: Strings! We hate strings! Specifically, we hate string concatenation. Therefore, we
 *     hate making new strings! So let's reuse them! Since we are able to use merely 15 numbers to create 100 unique multiplications, or 100
 *     numbers to create even 5,000 unique multiplications, we only need to make an array of 15 numbers (in string format, well, StringBuilder
 *     format). Each time the digit count is increased, we just append to the end of those strings. If any, this makes our experiment more
 *     "consistent" between digit counts, and all three algorithms are tested on the same set of data.
 *<p>
 *     Second optimization, and the big one, is threads. Ever notice how computers can have multiple cores? Ever noticed that Java uses ONLY ONE?
 *     well, fear no more, because this experiment USES THEM ALL. WARNING: YOUR SYSTEM WILL FREEZE UNTIL EXPERIMENT IS DONE IF YOU MAKE THE JAVA
 *     PROCESS TOP PRIORITY... I AM TOTALLY NOT SPEAKING FROM EXPERIENCE.
 *     And yes, I totally uh... just realized a way to improve this further, but here's the general idea:
 *     1) Strings are updated until they are all the correct length. Next, we set loose the threads, hungry for data. There are two "stacks" of
 *     calculations. First, they need to convert all the StringBuilders to each of the BiglyInt implementations. Second, they need to do the actual
 *     multiplications. What happens is that there is a counter for both calculations. All the threads first go chop down on the conversion digits,
 *     and once there are no more to be done (or at least, there's a thread doing each one), they then do the same thing for each of the individual
 *     calculations for each method.
 *     (Yes this got wordy, but HOPEFULLY I comment better below)
 *
 *
 *     <br><br><br>
 *     Yes, I do realize that I can optimize it more
 *     and yes, I wil be doing that during my free time... (Instead of maybe doing stuff I actually should but we're going to ignore that)
 * @author Thomas Kwashnak
 */
public class Experiment implements Runnable{

    public static void main(String[] args) throws FileNotFoundException {
        new Experiment(6,500000,100,n -> ((int) Math.ceil(n * 1.5)),new PrintStream("results5.csv")).runExperiment();
    }

    private static final boolean USE_NEGATIVE = false;
    /**
     * This is the method that's used to indicate what happens to the current digits after each step
     */
    private Step step;
    /**
     * The output to print to
     */
    private PrintStream out;
    /*
    Digits is the number of digits
    lastDigits is the number of digits in the previous iteration
     */
    private int digits, lastDigits;
    /*
    maxDigits is the maximum number of digits before the experiment ends, and trials is the number of trials for each digit count
     */
    private final int maxDigits, trials;
    /*
    The total time taken, basically it's an array to store each of the method's times (as sometimes half of the threads is finishing up one method
    while the other half begin the next method)
     */
    private long[] time;
    /*
    The converted Integers. Each row is dedicated to the correlating BiglyInteger method, allowing for all of the needed numbers to be created at
    the same time
     */
    private BiglyInt[][] integers;
    /*
    Two arrays. These are arrays because the values need to be separated by method
    integersAdded is the number of integers that have been converted from StringBuilder to the BiglyInt type, currentTrial indicates what trial #
    that method is on
     */
    private int[] integersAdded, currentTrial;
    /*
    The allmighty list of our integers, as strings (CURSE YOU STRING CONCATENATION)
     */
    private StringBuilder[] intStrings;
    /*
    Random... so is this comment random?
     */
    private Random random = new Random();


    public Experiment(int minDigits, int maxDigits, int trials, Step step, PrintStream out) {
        this.digits = minDigits;
        this.maxDigits = maxDigits;
        this.step = step;
        this.out = out;
        this.trials = trials;

        /*
        Basically initializing the StringBuilder array based on the trial count, and putting a '+' or '-' on each one randomly if negatives are
        allowed
         */

        intStrings = new StringBuilder[(int) (Math.ceil(Math.sqrt(2 * trials)))];
        for(int i = 0; i < intStrings.length; i++) {
            intStrings[i] = new StringBuilder().append(USE_NEGATIVE && random.nextBoolean() ? '-': '+');
        }
    }

    public void runExperiment() {
        //Printing to teh file
        out.print("digits");
        for(Algorithm algorithm : Algorithm.values()) {
            out.print("," + algorithm.name);
        }
        out.println();

        //Do-While loop
        do {
            //Go to next digit count
            digits = step.next(digits);
            //Update strings to that digits
            updateString();
            /*
            Tell the user that something is happening (otherwise user sits for hours and freaks out that their code hates them, speaking from
             experience... We totally didn't let this run for hours)
             */
            System.out.println("Starting " + digits);

            //Clear vals
            integersAdded = new int[3];
            currentTrial = new int[3];
            time = new long[3];
            integers = new BiglyInt[3][intStrings.length];

            //Start threads.
            ThreadGroup threads = new ThreadGroup("main"); //Grouping the threads into a single threadGroup
            int threadCount = Runtime.getRuntime().availableProcessors(); //gets the number of logical cores
            for(int i = 0; i < threadCount; i++) {
                new Thread(threads,this,Integer.toString(i)).start(); //Start!
            }

//            run(); <-- Thomas realized adding this method alone would make the Base thread ACTUALLY USEFUL AT THIS TIME.... (Whoops)
            //Waits until all threads in the group are cleared.. Honestly.. maybe I should make this wait because it probably takes up data too much
            while(threads.activeCount() > 0);

            out.print(digits); //Print the digits and times
            for(int i = 0; i < Algorithm.values().length; i++) {
                out.print("," + ((double) time[i] / (double) trials * 1e-9));
            }
            out.println();

            //Update
            lastDigits = digits;
        } while(digits < maxDigits); //Yada yada yada..
        out.close();
    }

    /**
     * Basically adds a number of random characters to the end of each StringBuilder a number of times equal to the difference between the previous
     * and current digit count
     */
    private void updateString() {
        int count = digits - lastDigits;
        for(int i = 0; i < intStrings.length; i++) {
            for(int j = 0; j < count; j++) {
                intStrings[i].append((char) ('1' + random.nextInt(9)));
            }
        }
    }

    /**
     * The almighty method that does stuffz
     */
    public void run() {
        //uhh.... basically the number of algorithm methods we have
        int algorithmCount = Algorithm.values().length;

        //For each algorithm
        for(int i = 0; i < algorithmCount; i++) {
            Algorithm algorithm = Algorithm.values()[i];
            //while the number of integers converted is less than the number of integers used
            while(integersAdded[i] < intStrings.length) {
                /*
                Save the index and increment the count (so we avoid the possibility of two threads doing one, or
                 so I hope)
                 */
                int index = integersAdded[i]++;
                //Convert the string.... And yes, I totally did think that this was the hardest part when in reality the most intense part is
                // probably the actual adding of characters to the strings CURSE YOU STRING CONCATENATION
                integers[i][index] = algorithm.factory.get(intStrings[index].toString());
            }
        }

        //Once all numbers are done (or being done), start work on the multiplications
        for(int i = 0; i < algorithmCount; i++) {
            //While there are still calculations to be done
            while(currentTrial[i] < trials) {

                //Save trial and increment the counter
                int trial = currentTrial[i]++;
                //Basically this code somehow (I totally forget how I managed to do this...) converts the trial # to two integers representing the
                // two unique numbers to calculate...
                /*
                Basically how the counting goes is
                0,1
                0,2
                0,3
                0,4
                0,5
                ...
                0,END
                1,2
                1,3
                ...
                 */
                int a = 0;
                while(trial + a >= integers[i].length) {
                    trial -= (integers[i].length - a);
                    a++;
                }
                int b = a + trial;

                /*
                Basically check to make sure that these integers actually exist, and if they don't, then wait for the thread that's taking its
                 sweet time to convert them
                 */
                while(integers[i][a] == null || integers[i][b] == null);

                /*
                MULTIPLY.. Yes for the longest of time I was using System.currentTimeMillis() and didn't realize that nanoTime was used until late
                one night.. No wonder it looked like this method was doing it in E-6 less time..
                 */
                long start = System.nanoTime();
                integers[i][a].multiply(integers[i][b]);
                long end = System.nanoTime();
                //Add value to the corresponding time
                time[i] += (end - start);
            }
        }
    }


    interface Step {
        int next(int prev);
    }
}
