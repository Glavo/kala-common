package asia.kala.collection.mutable;

import asia.kala.factory.CollectionFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public final class ArrayBufferTest implements BufferTestTemplate {

    @Override
    public final <E> CollectionFactory<E, ?, ArrayBuffer<E>> factory() {
        return ArrayBuffer.factory();
    }

}
