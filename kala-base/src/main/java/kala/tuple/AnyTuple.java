package kala.tuple;

import java.io.Serializable;

public interface AnyTuple extends Serializable {
    int arity();

    <U> U elementAt(int index);
}
