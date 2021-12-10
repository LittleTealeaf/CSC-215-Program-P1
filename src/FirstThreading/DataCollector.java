package FirstThreading;

import StarterCode.BiglyInt;

@Deprecated
public class DataCollector implements Runnable {
    private BiglyInt[] integers;
    private int trials, currentTrial;
    private long total;

    public DataCollector(BiglyInt[] integers, int trials) {
        this.trials = trials;
        this.integers = integers;
//        System.out.println(integers.length);
    }


    public void run() {
        while(currentTrial < trials) {
            int trial = currentTrial++;
            int a = 0;
            while(trial + a >= integers.length) {
                trial -= (integers.length - a);
                a++;
            }
            int b = a + trial;
            long start = System.currentTimeMillis();
            integers[a].multiply(integers[b]);
            long end = System.currentTimeMillis();
            total += end - start;
        }
    }

    public double runExperiment() {
        ThreadGroup threadGroup = new ThreadGroup("main");
        Thread[] threads = new Thread[Runtime.getRuntime().availableProcessors()];
        for(int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(threadGroup,this,Integer.toString(i));
            threads[i].start();
        }
        while(threadGroup.activeCount() > 0) {
//            try {
//                Thread.sleep(0);
//            } catch(Exception ignored) {}
        }
        return (double) total / (double) trials * 1e-9;
    }
}
