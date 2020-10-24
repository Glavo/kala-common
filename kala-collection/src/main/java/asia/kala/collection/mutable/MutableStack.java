package asia.kala.collection.mutable;

public interface MutableStack<E> {
    void push(E value);

    E pop();

    E peek();

    boolean isEmpty();
}
