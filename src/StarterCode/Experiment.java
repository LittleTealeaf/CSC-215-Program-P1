package StarterCode;

public class Experiment implements Runnable {

//    private Thread[] threads;
//    private int trialsLeft, trialsTotal, threadsCompleted;
//    private int method, size;
//    private long total;
//
//    public Experiment(int method, int size, int trials, int threadCount) {
//        threads = new Thread[Math.max(trials,threadCount)];
//        for (int i = 0; i < threads.length; i++) {
//            threads[i] = new Thread(this);
//        }
//        this.trialsLeft = trials;
//        this.trialsTotal = trials;
//        this.method = method;
//        this.size = size;
//    }
//
//    public double runExperiment() {
//        for(Thread thread : threads) {
//            thread.start();
//        }
//
//        while(threadsCompleted > threads.length) {
//
//        }
//
//        return (double) total / (double) trialsTotal * 1e-9;  // Convert to average time in Seconds
//    }
//
//    public void run() {
//        while(trialsLeft > 0) {
//            BiglyInt numOne = BiglyIntFactory.createBiglyInt(BiglyIntFactory.generateNumber(size,true),method);
//            BiglyInt numTwo = BiglyIntFactory.createBiglyInt(BiglyIntFactory.generateNumber(size,true),method);
//            long start = System.currentTimeMillis();
//            numOne.multiply(numTwo);
//            long end = System.currentTimeMillis();
//            total += start - end;
//            trialsLeft--;
//        }
//        threadsCompleted++;
//    }

    private Thread[] threads;
    private int trials, currentTrial, finishedThreads;
    private long total;
    private BiglyInt[] integers;

    public Experiment(int method, int digits, int trials, int threadCount) {
        threads = new Thread[Math.max(trials,threadCount)];
        this.trials = trials;
        for(int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(this);
        }
        integers = generateIntegers(digits,trials,method);

    }

    public BiglyInt[] getIntegers(int trial) {
        int a = 0, b = 1;
        while(trial > 0) {
            b++;
            if(b >= integers.length) {
                a++;
                b = a + 1;
            }
            trial--;
        }
        return new BiglyInt[] {integers[a], integers[b]};
    }

    public BiglyInt[] generateIntegers(int digits, int trials, int method) {

        int values = (int) Math.ceil(Math.sqrt(2 * trials));
        integers = new BiglyInt[values];
        for(int i = 0; i < integers.length; i++) {
            integers[i] = BiglyIntFactory.createBiglyInt(BiglyIntFactory.generateNumber(digits,true),method);
        }
        return integers;
    }

    public void run() {
        while(currentTrial < trials) {
            int trial = currentTrial++;
            getIntegers(trial);
            System.out.println(trial);
        }
        System.out.println("end");
        finishedThreads++;
    }


    public double runExperiment() {
        for(int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
        while(finishedThreads < threads.length);
        return 0;
    }

}
