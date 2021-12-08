package StarterCode;
/**********
 * BiglyIntFactory
 * Author: Christian Duncan
 *
 * This simple class creates one of several BiglyIntegers
 * from the given String... based on the argument provided.
 * Just a convenience class.
 *
 * It also generates random large "numbers"
 ***********/
import java.util.Random;

public class BiglyIntFactory {
    static Random rand = new Random();

    /**
     * Create a BiglyInt from the given String
     * The type of BiglyInt depends on type.
     * @param val The number as a String, e.g. "123456789"
     * @param type The type (0=BiglyIntA, 1=BiglyIntB, 2=BiglyIntC)
     * @returns A BiglyInt of the given type and value
     **/
    public static BiglyInt createBiglyInt(String val, int type) {
        return switch (type) {
            case 0 -> new BiglyIntA(val);
            case 1 -> new BiglyIntB(val);
            case 2 -> new BiglyIntC(val);
            default -> null;
        };
    }

    /**
     * Generate a "number" as a String of length digits 
     * @param length The number of digits to generate
     * @param positiveOnly If true, number is not signed.  If false, it can also inlcude a + or - sign in front
     **/
    public static String generateNumber(int length, boolean positiveOnly) {
        StringBuilder result;
        
        if (!positiveOnly) {
            result = new StringBuilder(length+1);
            if (rand.nextBoolean())
                result.append('-');
            else
                result.append('+');
        } else {
            result = new StringBuilder(length);
        }

        // Now generate a bunch of random digits
        result.append(1+rand.nextInt(9));  // No 0 for first character
        for (int i = 1; i < length; i++) result.append(rand.nextInt(10));
        return result.toString();
    }
}
