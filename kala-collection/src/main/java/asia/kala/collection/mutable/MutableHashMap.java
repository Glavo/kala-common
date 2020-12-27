package asia.kala.collection.mutable;

import asia.kala.collection.internal.FromJavaConvert;
import asia.kala.control.Option;
import asia.kala.iterator.MapIterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public final class MutableHashMap<K, V> extends FromJavaConvert.MutableMapFromJava<K, V> {
    public MutableHashMap() {
        super(new HashMap<>());
    }
}
