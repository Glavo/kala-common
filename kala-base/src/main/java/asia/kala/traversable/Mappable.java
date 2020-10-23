package asia.kala.traversable;

import asia.kala.annotations.Covariant;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * A {@code Mappable} is a data structure that can be {@link #map(Function)} to another {@code Mappable}.
 *
 * @param <T> the type of value
 * @author Glavo
 */
public interface Mappable<@Covariant T> {

    /**
     * Returns a container consisting of the results of applying the given
     * function to the elements of this stream.
     *
     * @param <U>    The element type of the new container
     * @param mapper a non-interfering stateless function to apply to each element
     * @return the new container
     */
    @NotNull
    @Contract(pure = true)
    <U> Mappable<U> map(@NotNull Function<? super T, ? extends U> mapper);
}

