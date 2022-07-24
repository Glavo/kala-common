package kala.value;

public abstract class AbstractMutableValue<T> extends AbstractValue<T> implements MutableValue<T> {
    @Override
    public String toString() {
        return "MutableValue[" + get() + "]";
    }
}
