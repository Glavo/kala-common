package kala.function;

import java.util.function.BiPredicate;

@SuppressWarnings("unchecked")
public interface Balance<T> extends BiPredicate<T, T> {
    static <T> Balance<T> defaultBalance() {
        return (Balance<T>) Balances.DEFAULT;
    }
    static <T> Balance<T> optimizedBalance() {
        return (Balance<T>) Balances.OPTIMIZED;
    }

    static <T> Balance<T> identityBalance() {
        return (Balance<T>) Balances.IDENTITY;
    }


    int hash(T obj);

    boolean test(T t1, T t2);
}
