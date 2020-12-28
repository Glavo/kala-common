package asia.kala.collection.immutable;

import asia.kala.collection.Map;
import org.jetbrains.annotations.NotNull;

public interface ImmutableMap<K, V> extends Map<K, V> {

    @Override
    default @NotNull String className() {
        return "ImmutableMap";
    }
}
