package StarterCode;
/**********
 * BiglyIntA
 * Author: Christian Duncan
 *
 * An implementation of BiglyInt
 * This version has the straightforward Theta(n^2) multiplication algorithm
 * The integers are stored in an array of long but as base-10 decimals.
 * This makes it easier to convert to a base-10 string BUT it isn't as
 * space efficient as storing it in binary!
 ***********/
public class BiglyIntA extends BiglyInt {
    long[] number;   // Numbers stored in base 10^18 (essentially)
    boolean signum;  // true=positive, false=negative
    static final int DIGITS_PER_ITEM = 18;  
    static final long BASE_NUMBER = 1_000_000_000_000_000_000L;
    static final long HALF_BASE   = 1_000_000_000L;
    
    // For internal use only (creates 0)
    protected BiglyIntA() {
        number = null;
        signum = true;
    }
    
    // Create a copy of the given BiglyIntA
    public BiglyIntA(BiglyIntA other) {
        this.number = (long[]) other.number.clone();
        this.signum = other.signum;
    }
    
    // Create a BiglyInt with the given String
    public BiglyIntA(String num) {
        char[] numArray = num.toCharArray();  // Faster processing

        // Check sign and validity of number given
        if (numArray.length == 0)
            throw new NumberFormatException("Invalid format.  Input has length 0.");

        int start = 0;
        if (numArray[0] == '-') {
            signum = false; // Negative number
            start = 1;
        } else if (numArray[0] == '+') {
            signum = true;  // Positive number
            start = 1;
        } else {
            signum = true;
        }

        if (numArray.length < start)
            throw new NumberFormatException("Invalid format.");

        // Skip over leading 0s
        for (; start < numArray.length && numArray[start] == '0'; start++);

        if (start == numArray.length) {
            // Nothing but zeroes
            number = null;  // Special case for 0
            signum = true;  // -0 is still 0
            return;
        }
        
        // Make sure remaining digits are all 0-9
        for (int i = start; i < numArray.length; i++)
            if (numArray[i] < '0' || numArray[i] > '9')
                // These are the only "digits" we allow
                throw new NumberFormatException("Invalid format.  Invalid digit: " + numArray[i]);

        // Compute how many BASE-10^18 digits we'll need (size of our array)
        int length = (numArray.length - start + DIGITS_PER_ITEM - 1) / DIGITS_PER_ITEM;
        number = new long[length];
        
        // Now create the number (from right to left)
        int char_end_position = numArray.length;  // Position of "end" of this portion in character array (exclusive)
        int char_beg_position = numArray.length - DIGITS_PER_ITEM; // Position of "start" of this portion in character array
            if (char_beg_position < start) char_beg_position = start;
        for (int bignum_position = number.length - 1;  bignum_position >= 0; bignum_position--) {
            for (int index = char_beg_position; index < char_end_position; index++)
                number[bignum_position] = number[bignum_position]*10 + (numArray[index] - '0');
            char_end_position = char_beg_position;
            char_beg_position = char_beg_position - DIGITS_PER_ITEM;
            if (char_beg_position < start) char_beg_position = start;
        }
    }

    // Returns a base-10 (decimal) String representation of this integer
    public String toString() {
        if (number == null) return "0";  // Special case for 0

        StringBuilder result = new StringBuilder(number.length * DIGITS_PER_ITEM + 1);
        if (!signum) result.append('-');  // Negative sign
        boolean firstPart = true;   // Avoid leading 0s
        for (long x: number) {
            String n = String.valueOf(x);
            if (!firstPart) {
                // Pad 0s to make it DIGITS_PER_ITEM long
                int len = DIGITS_PER_ITEM - n.length();
                for (int i = 0; i < len; i++) result.append('0');
            } else firstPart = false;
            result.append(n);
        }
        return result.toString();
    }

    // Returns this + other (as a new BiglyInt)
    public BiglyInt add(BiglyInt other) {
        if (!(other instanceof BiglyIntA)) {
            throw new RuntimeException("Can't mix BiglyInts!  Sorry!");
        }
        BiglyIntA otherA = (BiglyIntA) other;

        // X+0 = X
        if (otherA.number == null) return new BiglyIntA(this);
        if (this.number == null) return new BiglyIntA(otherA);
            
        // First deal with signs
        if (this.signum != otherA.signum) {
            if (this.signum) {
                return this.subtract(otherA.negate());
            } else {
                return otherA.subtract(this.negate());
            }
        }

        // Signs are the same so we can add them
        BiglyIntA result = new BiglyIntA();
        result.signum = this.signum;
        int length = 1 + (this.number.length > otherA.number.length ?
                          this.number.length : otherA.number.length);

        // Create space for our summmand
        result.number = new long[length];

        long carry = 0;
        int i = this.number.length - 1;
        int j = otherA.number.length - 1;
        int k = result.number.length - 1;
        while (i >= 0 || j >= 0) {
            long a = i >= 0 ? this.number[i] : 0;
            long b = j >= 0 ? otherA.number[j] : 0;

            long sum = a + b + carry;
            
            if (sum >= BASE_NUMBER || sum < 0) {
                // Carry
                sum -= BASE_NUMBER;
                carry = 1;
            } else {
                carry = 0;
            }
            
            result.number[k] = sum;
            i--; j--; k--;
        }

        if (carry > 0) {
            assert(k == 0);
            result.number[k] = carry;
        }

        // Remove any leading zeros
        cleanUp(result);
        return result;
    }
    
    // Returns this - other (as a new BiglyInt)
    public BiglyInt subtract(BiglyInt other) {
        if (!(other instanceof BiglyIntA)) {
            throw new RuntimeException("Can't mix BiglyInts!  Sorry!");
        }
        BiglyIntA otherA = (BiglyIntA) other;

        // X-0 = X, 0-X=-X
        if (otherA.number == null) return new BiglyIntA(this);
        if (this.number == null) return otherA.negate();
            
        // First deal with signs
        if (this.signum != otherA.signum) {
            if (this.signum) {
                return this.add(otherA.negate());
            } else {
                return otherA.add(this.negate()).negate();
            }
        }

        // Signs are the same so we can subtract them (but in which order)?
        boolean swapped = false;  // If swapped, we use negative
        BiglyIntA a = this;
        BiglyIntA b = otherA;
        if (b.number.length > a.number.length) {
            // B is larger than A (Swap them A-B ==> -(B-A))
            swapped = true;
        } else if (b.number.length == a.number.length) {
            // Oh, same length, now we have to see which is larger
            int i;
            for (i = 0; i < b.number.length; i++) {
                if (b.number[i] > a.number[i]) {
                    // B is larger than A (Swap them A-B ==> -(B-A))
                    swapped = true;
                    break;
                } else if (b.number[i] < a.number[i]) {
                    // A is larger... can stop
                    break;
                }
            }
            if (i == b.number.length) {
                // Same number!  Return 0.
                return new BiglyIntA();
            }
        }

        if (swapped) {
            a = otherA;
            b = this;
        }

        // Now compute A-B  (and A > B)
        BiglyIntA result = new BiglyIntA();
        result.signum = this.signum ^ swapped;
        int length = a.number.length;

        // Create space for our summmand
        result.number = new long[length];

        long borrow = 0;
        int i = a.number.length - 1;
        int j = b.number.length - 1;
        int k = result.number.length - 1;
        while (i >= 0 || j >= 0) {
            long av = i >= 0 ? a.number[i] : 0;
            long bv = j >= 0 ? b.number[j] : 0;

            long diff = av - bv - borrow;
            
            if (diff < 0) {
                // Borrow
                diff += BASE_NUMBER;
                borrow = 1;
            } else {
                borrow = 0;
            }
            
            result.number[k] = diff;
            i--; j--; k--;
        }

        assert(borrow == 0);  // If not 0, then A< B!!!???!!!

        // Remove any leading zeros
        cleanUp(result);
        return result;
    }
    
    // Returns -this (as a new BiglyInt)
    public BiglyInt negate() {
        BiglyIntA result = new BiglyIntA(this);
        result.signum = !result.signum;
        return result;
    }

    // Returns this * other (as a new BiglyInt)
    public BiglyInt multiply(BiglyInt other) {
        if (!(other instanceof BiglyIntA)) {
            throw new RuntimeException("Can't mix BiglyInts!  Sorry!");
        }
        BiglyIntA otherA = (BiglyIntA) other;

        // X*0 = 0
        if (otherA.number == null || this.number == null) return new BiglyIntA();
            
        BiglyIntA result = new BiglyIntA();
        result.signum = this.signum == otherA.signum;   // Same sign?  Positive.  Else, negative.

        // Estimate how many "digits" we'll need
        int length = this.number.length + otherA.number.length + 1;

        // Create space for our product
        result.number = new long[length];

        for (int j = 0; j < otherA.number.length; j++) {
            long multCarry = 0;
            long addCarry = 0;
            for (int i = 0; i < this.number.length; i++) {
                int k = result.number.length - 1 - (i + j);
                long a = this.number[this.number.length - 1 - i];
                long b = otherA.number[otherA.number.length - 1 - j];
                long[] prod = miniMultiplyWithCarry(a, b, multCarry);
                result.number[k] += prod[1] + addCarry;  // Add the low term to this summation
                if (result.number[k] >= BASE_NUMBER) {
                    // Add Carry
                    result.number[k] -= BASE_NUMBER;
                    addCarry = 1;
                } else {
                    addCarry = 0;
                }
                multCarry = prod[0];
                // System.out.println("DEBUG: result(m) = " + result);
            }
            if (multCarry > 0 || addCarry > 0) {
                int k = result.number.length - 1 - (this.number.length + j);
                result.number[k] += (multCarry + addCarry);   // Add the carrys over to the last part
                // System.out.println("DEBUG: result(c) = " + result);
                assert(result.number[k] >= 0 && result.number[k] < BASE_NUMBER);  // Oops?  My calculations must be off... argh!
            }
        }

        cleanUp(result);
        
        return result;
    }

    private static void cleanUp(BiglyIntA a) {
        // Remove the leading 0s if they are there
        int i;
        for (i = 0; i < a.number.length && a.number[i] == 0; i++);
        if (i > 0) {
            // Some to remove
            long[] numberA = new long[a.number.length - i];
            System.arraycopy(a.number, i, numberA, 0, numberA.length);
            a.number = numberA;
        }
    }
    
    // Multiplying two 64-bit numbers is too big for a single calculation
    //   It can overflow and the actual product is lost.
    //   With two 32-bit numbers we could use a single 64-bit number but we don't have a 128-bit option.
    //   So, we are doing it the "hard" way
    //   And we are working with 10^18 numbers not really 64-bit numbers - so can't just do that either!
    //   The carry is also a possible carry-over from the previous term -- so a*b+carry
    private long[] miniMultiplyWithCarry(long a, long b, long carry) {
        long a0 = a % HALF_BASE;
        long a1 = a / HALF_BASE;
        long b0 = b % HALF_BASE;
        long b1 = b / HALF_BASE;

        long a0b0 = a0*b0;
        long a1b1 = a1*b1;
        long a0b1 = a0*b1 + b0*a1;
        long a0b1L = a0b1 % HALF_BASE;
        long a0b1H = a0b1 / HALF_BASE;
        long high = a1b1 + a0b1H;
        long low = a0b0 + a0b1L*HALF_BASE;
        if (low >= BASE_NUMBER || low < 0) {
            // Carry over to the high part
            low -= BASE_NUMBER;
            high += 1;
        }
        low += carry;
        if (low >= BASE_NUMBER || low < 0) {
            // Carry over to the high part
            low -= BASE_NUMBER;
            high += 1;
        }
        
        return new long[] { high, low };
    }
}
