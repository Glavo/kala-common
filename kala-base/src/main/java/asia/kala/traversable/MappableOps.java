package asia.kala.traversable;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface MappableOps<T, CC extends Mappable<?>, COLL extends Mappable<T>> {
    @NotNull
    @Contract(pure = true)
    <U> CC map(@NotNull Function<? super T, ? extends U> mapper);
}
