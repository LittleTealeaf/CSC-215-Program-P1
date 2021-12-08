package StarterCode;
/**********
 * BiglyIntC
 * Author: Christian Duncan
 *
 * This class is just a wrapper around Java's BigInteger class.
 * It supports a few basic math operators
 * Its main purpose though is to facilitate testing of the
 * various Integer multiplication algorithms
 *
 * random edit
 ***********/
import java.math.BigInteger;

public class BiglyIntC extends BiglyInt {
    BigInteger internal;  // Storing it internally

    // Constructor
    public BiglyIntC(String val) {
        internal = new BigInteger(val, 10);  // Radix=10 for the way the number is represented as String
    }

    private BiglyIntC(BigInteger val) { internal = val; }
    
    // Returns this + other (as a new BiglyInt)
    public BiglyInt add(BiglyInt other) {
        if (!(other instanceof BiglyIntC)) {
            throw new RuntimeException("Can't mix BiglyInts!  Sorry!");
        }
        BiglyIntC b = (BiglyIntC) other;
        return new BiglyIntC(this.internal.add(b.internal));
    }
    
    // Returns this - other (as a new BiglyInt)
    public BiglyInt subtract(BiglyInt other) {
        if (!(other instanceof BiglyIntC)) {
            throw new RuntimeException("Can't mix BiglyInts!  Sorry!");
        }
        BiglyIntC b = (BiglyIntC) other;
        return new BiglyIntC(this.internal.subtract(b.internal));
    }

    // Returns -this (as a new BiglyInt)
    public BiglyInt negate() {
        return new BiglyIntC(this.internal.negate());
    }        

    // Returns this * other (as a new BiglyInt)
    public BiglyInt multiply(BiglyInt other) {
        if (!(other instanceof BiglyIntC)) {
            throw new RuntimeException("Can't mix BiglyInts!  Sorry!");
        }
        BiglyIntC b = (BiglyIntC) other;
        return new BiglyIntC(this.internal.multiply(b.internal));
    }

    // Returns a base-10 (decimal) String representation of this integer
    public String toString() { return this.internal.toString(); }
}
