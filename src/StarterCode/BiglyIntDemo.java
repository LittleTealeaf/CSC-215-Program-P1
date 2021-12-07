package StarterCode;
/**********
 * BiglyIntDemo
 * Author: Christian Duncan
 *
 * This short class just demonstrates how to use the BiglyInts
 ***********/
public class BiglyIntDemo {
    public static void main(String[] args) {
        // Generate two 100-digit numbers as Strings
        String numberA = BiglyIntFactory.generateNumber(100, true);
        String numberB = BiglyIntFactory.generateNumber(100, true);

        // Convert these Strings to a BiglyIntA type (then O(n^2) one)
        BiglyInt a = BiglyIntFactory.createBiglyInt(numberA, 0); // 0=O(n^2) version, 1=Karatsuba Alg (non-optimized version), 2=BigInteger (Java's version)
        BiglyInt b = BiglyIntFactory.createBiglyInt(numberB, 0);

        // Print out the two numbers
        System.out.println("a = " + a);
        System.out.println("b = " + b);

        // Multiply them and print result
        BiglyInt mult = a.multiply(b);
        System.out.println("a*b = " + mult);
    }
}
