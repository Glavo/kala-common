package kala.tuple;

import java.io.Serializable;

public interface AnyTuple extends Serializable {
    int HASH_MAGIC = 427632945;

    int arity();

    <U> U elementAt(int index);
}
