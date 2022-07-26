package kala.internal;

import kala.annotations.UnstableName;
import kala.collection.base.MapIterator;
import kala.collection.base.Sized;
import org.jetbrains.annotations.NotNull;

@UnstableName
public interface MapBase<K, V> extends Sized {
    @NotNull MapIterator<K, V> iterator();

    @NotNull String className();
}
