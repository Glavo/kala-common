package kala.control;

import kala.annotations.Covariant;
import kala.annotations.Sealed;
import kala.control.primitive.PrimitiveOption;

import java.io.Serializable;

@Sealed(subclasses = {Option.class, PrimitiveOption.class})
public abstract class AnyOption<@Covariant T> implements Serializable {
    protected static final int HASH_MAGIC = -818206074;
    protected static final int NONE_HASH = 1937147281;

    protected AnyOption() {
    }
}
