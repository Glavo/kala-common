package kala.collection.mutable;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MutableLinkedHashMapTest implements MutableMapTestTemplate {
    @Override
    public <K, V> MutableLinkedHashMap<K, V> create() {
        return new MutableLinkedHashMap<>();
    }
}
