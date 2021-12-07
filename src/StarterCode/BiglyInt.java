package StarterCode;
/**********
 * BiglyInt
 * Author: Christian Duncan
 *
 * This abstract class represents a Big Integer
 * But not necessarily Java's well-constructed "BigInteger" class.
 * It supports a few basic math operators
 * Its main purpose though is to facilitate testing of the
 * various Integer multiplication algorithms
 * changes
 ***********/

public abstract class BiglyInt {
    // Returns this + other (as a new BiglyInt)
    public abstract BiglyInt add(BiglyInt other);
    
    // Returns this - other (as a new BiglyInt)
    public abstract BiglyInt subtract(BiglyInt other);

    // Returns -this (as a new BiglyInt)
    public abstract BiglyInt negate();

    // Returns this * other (as a new BiglyInt)
    public abstract BiglyInt multiply(BiglyInt other);

    // Returns a base-10 (decimal) String representation of this integer
    public abstract String toString();
}

