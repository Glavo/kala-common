package asia.kala.control;

import asia.kala.annotations.Covariant;

import java.io.Serializable;

public abstract class OptionAny<@Covariant T> implements Serializable {
    OptionAny() {
    }

}
