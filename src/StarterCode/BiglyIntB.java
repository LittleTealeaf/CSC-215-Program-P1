package StarterCode;
/**********
 * BiglyIntB
 * Author: Christian Duncan
 *
 * An implementation of BiglyInt
 * This version extends BiglyIntA by replacing the n^2 multiplication by the Karatsuba Algorithm
 *
 * The integers are stored in an array of long but as base-10 decimals.
 * This makes it easier to convert to a base-10 string BUT it isn't as
 * space efficient as storing it in binary!
 ***********/
public class BiglyIntB extends BiglyIntA {
    // For internal use only
    private BiglyIntB() {
    }

    private BiglyIntB(BiglyIntA other) {
        this.number = other.number;  // Steals the other ones information
        this.signum = other.signum;  
    }
    
    // Create a copy of the given BiglyIntB
    public BiglyIntB(BiglyIntB other) {
        this.number = (long[]) other.number.clone();
        this.signum = other.signum;
    }
    
    // Create a BiglyInt with the given String
    public BiglyIntB(String num) { super(num); }

    // Returns this * other (as a new BiglyInt)
    public BiglyInt multiply(BiglyInt other) {
        if (!(other instanceof BiglyIntB)) {
            throw new RuntimeException("Can't mix BiglyInts!  Sorry!");
        }
        BiglyIntB otherA = (BiglyIntB) other;

        // X*0 = 0
        if (otherA.number == null || this.number == null) return new BiglyIntB();

        if (this.number.length <= 1 || otherA.number.length <= 1)
            return super.multiply(other);  // Just a base case!
        else {
            BiglyIntB result = multiplyRec(otherA);  // Compute it recursively
            result.signum = this.signum == otherA.signum;   // Same sign?  Positive.  Else, negative.
            return result;
        }
    }

    private BiglyIntB multiplyRec(BiglyIntB other) {
        // Base case (one of the two has length 1)
        if (this.number == null || other.number == null || this.number.length <= 1 || other.number.length <= 1)
            return new BiglyIntB((BiglyIntA) this.multiply(other));
    
        // Split this (X) into Xh and Xl  (high and low)  --- and Y too
        int len = this.number.length >= other.number.length ? this.number.length/2 : other.number.length/2;
        BiglyIntB[] x = split(this, len);
        BiglyIntB[] y = split(other, len);

        BiglyIntB z0 = x[1].multiplyRec(y[1]);
        BiglyIntB z2 = x[0].multiplyRec(y[0]);
        BiglyIntB x1x0 = new BiglyIntB((BiglyIntA) x[1].add(x[0]));
        BiglyIntB y1y0 = new BiglyIntB((BiglyIntA) y[1].add(y[0]));
        BiglyIntB z1 = new BiglyIntB((BiglyIntA) (x1x0.multiply(y1y0).subtract(z0).subtract(z2)));

        z2 = shift(z2, 2*len);  // z2*BASE^(2l)
        z1 = shift(z1, len);    // z1*BASE^(l)
        return new BiglyIntB((BiglyIntA) (z0.add(z1).add(z2)));
    }

    // Shift by len --- essentially compute x*BASE^len
    protected static BiglyIntB shift(BiglyIntB x, int len) {
        if (x.number == null) return x;  // 0
        BiglyIntB result = new BiglyIntB();
        result.number = new long[x.number.length + len];
        System.arraycopy(x.number, 0, result.number, 0, x.number.length);
        return result;
    }
        
    protected static BiglyIntB[] split(BiglyIntB x, int len) {
        if (x.number.length <= len) {
            // Nothing to "split"
            return new BiglyIntB[] {new BiglyIntB(), new BiglyIntB(x)};
        }
        
        BiglyIntB xh = new BiglyIntB();
        BiglyIntB xl = new BiglyIntB();
        xh.number = new long[x.number.length - len];
        System.arraycopy(x.number, 0, xh.number, 0, xh.number.length);
        xl.number = new long[len];
        System.arraycopy(x.number, xh.number.length, xl.number, 0, xl.number.length);
        return new BiglyIntB[] {xh, xl};
    }
}
