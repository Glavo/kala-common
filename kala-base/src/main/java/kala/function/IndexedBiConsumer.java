package kala.function;

@FunctionalInterface
public interface IndexedBiConsumer<T, U> {
    void accept(int index, T t, U u);
}
