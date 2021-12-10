package FirstThreading;

import StarterCode.*;

@Deprecated
public enum BiglyMethod {
    A(BiglyIntA::new, "Regular"),
    B(BiglyIntB::new, "Karatsuba"),
    C(BiglyIntC::new,"Java BigInt");
    IntGenerator intGenerator;

    String name;
    BiglyMethod(IntGenerator intGenerator, String name) {
        this.intGenerator = intGenerator;
        this.name = name;
    }

    BiglyInt generate(int digits, boolean positiveOnly) {
        return intGenerator.generate(BiglyIntFactory.generateNumber(digits,positiveOnly)); //Optimize this
    }

    public String toString() {
        return name;
    }




    interface IntGenerator {
        BiglyInt generate(String val);
    }
}
