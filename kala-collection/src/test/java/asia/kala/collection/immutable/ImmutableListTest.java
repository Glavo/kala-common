package asia.kala.collection.immutable;


import asia.kala.factory.CollectionFactory;

import static org.junit.jupiter.api.Assertions.*;
import static asia.kala.collection.Assertions.*;

public final class ImmutableListTest implements ImmutableSeqTestTemplate {
    @Override
    public final <E> CollectionFactory<E, ?, ImmutableList<E>> factory() {
        return ImmutableList.factory();
    }
}
