package Experiment;

import StarterCode.BiglyInt;
import StarterCode.BiglyIntA;
import StarterCode.BiglyIntB;
import StarterCode.BiglyIntC;

/**
 * Basically, an enum of factories that generate a BiglyInt from a string...
 * @author Thomas Kwashnak
 */
public enum Algorithm {
    /*
     * something something cool new java implementation yay
     */
    A("Default", BiglyIntA::new),
    B("Karatsuba", BiglyIntB::new),
    C("Java", BiglyIntC::new);

    String name;
    Factory factory;

    Algorithm(String name, Factory factory) {
        this.name = name;
        this.factory = factory;
    }




    interface Factory {
        BiglyInt get(String val);
    }
}
