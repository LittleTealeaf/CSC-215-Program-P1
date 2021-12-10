package StarterCode;
/**
 * BiglyIntTester
 * Author: Christian Duncan
 *
 * This class tests the BiglyInt implementations.
 * It compares each implementation with the result of using BigInteger from Java APIs
 */
import java.math.BigInteger;

public class BiglyIntTester {
    public static void main(String[] args) {
        // Test each method (nothing is output if there is no "error")
        for (int m = 0; m < 3; m++) {
            // Test constructor and toString
            testConstructor(m);

            // Test adding two BiglyInt numbers
            testAddition(m);

            // Test subtracting two BiglyInt numbers
            testSubtraction(m);

            // Test multiplying two BiglyInt numbers
            testMultiplication(m);
        }
    }

    // Function name says it all.  Type identifies which BiglyInt to test (from Factory)
    public static void testConstructor(int type) {
        for (int size = 10; size < 100; size++) {
            String number = BiglyIntFactory.generateNumber(size, true);
            BiglyInt num = BiglyIntFactory.createBiglyInt(number, type);
            String convertBack = num.toString();

            if (!number.equals(convertBack)) {
                // The two aren't the same???
                System.out.println(number + ":" + convertBack);
            }
        }
    }

    // Function name says it all.  Type identifies which BiglyInt to test (from Factory)
    public static void testAddition(int type) {
        for (int sizeA = 10; sizeA < 100; sizeA++) {
            for (int sizeB = 10; sizeB < 100; sizeB++) {
                // Generate two LARGE random numbers
                String numberA = BiglyIntFactory.generateNumber(sizeA, true);
                BiglyInt numA = BiglyIntFactory.createBiglyInt(numberA, type);
                String numberB = BiglyIntFactory.generateNumber(sizeB, true);
                BiglyInt numB = BiglyIntFactory.createBiglyInt(numberB, type);
                
                // See what the sum is
                BiglyInt sum = numA.add(numB);
                String result = sum.toString();
                
                // See what the sum should be (according to BigInteger in Java)
                BigInteger bigA = new BigInteger(numberA);
                BigInteger bigB = new BigInteger(numberB);
                BigInteger bigS = bigA.add(bigB);
                String bigR = bigS.toString();
                
                if (!result.equals(bigR)) {
                    // The sums aren't the same???
                    System.out.println("Hmm, error: ");
                    System.out.println("   " + numA + "+" + numB + " = " + result);
                    System.out.println("   " + bigA + "+" + bigB + " = " + bigR);
                }
            }
        }
    }

    // Function name says it all.  Type identifies which BiglyInt to test (from Factory)
    public static void testSubtraction(int type) {
        for (int sizeA = 10; sizeA < 100; sizeA++) {
            for (int sizeB = 10; sizeB < 100; sizeB++) {
                // Generate two LARGE random numbers
                String numberA = BiglyIntFactory.generateNumber(sizeA, true);
                BiglyInt numA = BiglyIntFactory.createBiglyInt(numberA, type);
                String numberB = BiglyIntFactory.generateNumber(sizeB, true);
                BiglyInt numB = BiglyIntFactory.createBiglyInt(numberB, type);
                
                // See what the difference is
                BiglyInt diff = numA.subtract(numB);
                String result = diff.toString();
                
                // See what the diff should be (according to BigInteger in Java)
                BigInteger bigA = new BigInteger(numberA);
                BigInteger bigB = new BigInteger(numberB);
                BigInteger bigD = bigA.subtract(bigB);
                String bigR = bigD.toString();
                
                if (!result.equals(bigR)) {
                    // The sums aren't the same???
                    System.out.println("Hmm, error: ");
                    System.out.println("   " + numA + "-" + numB + " = " + result);
                    System.out.println("   " + bigA + "-" + bigB + " = " + bigR);
                }
            }
        }
    }

    // Function name says it all.  Type identifies which BiglyInt to test (from Factory)
    public static void testMultiplication(int type) {
        for (int sizeA = 900; sizeA < 1000; sizeA++) {
            for (int sizeB = 900; sizeB < 1000; sizeB++) {
                // Generate two LARGE random numbers
                String numberA = BiglyIntFactory.generateNumber(sizeA, true);
                BiglyInt numA = BiglyIntFactory.createBiglyInt(numberA, type);
                String numberB = BiglyIntFactory.generateNumber(sizeB, true);
                BiglyInt numB = BiglyIntFactory.createBiglyInt(numberB, type);
                
                // See what the product is
                BiglyInt prod = numA.multiply(numB);
                String result = prod.toString();
                
                // See what the sum should be (according to BigInteger in Java)
                BigInteger bigA = new BigInteger(numberA);
                BigInteger bigB = new BigInteger(numberB);
                BigInteger bigP = bigA.multiply(bigB);
                String bigR = bigP.toString();
                
                if (!result.equals(bigR)) {
                    // The sums aren't the same???
                    System.out.println("Hmm, error: ");
                    System.out.println("   " + numA + "*" + numB + " = " + result);
                    System.out.println("   " + bigA + "*" + bigB + " = " + bigR);
                    System.exit(1);
                }
            }
        }
    }
}
