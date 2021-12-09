package Experiment;

import StarterCode.BiglyInt;
import StarterCode.BiglyIntA;
import StarterCode.BiglyIntB;
import StarterCode.BiglyIntC;

/**
 * STUFF
 * @author Thomas Kwashnak
 */
public enum Algorithm {
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
