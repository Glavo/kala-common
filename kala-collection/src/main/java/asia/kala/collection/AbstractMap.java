package asia.kala.collection;

public abstract class AbstractMap<K, V> implements Map<K, V> {

    @Override
    public int hashCode() {
        return iterator().hash() + Map.HASH_MAGIC;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Map<?, ?>)
                || !(canEqual(obj))
                || !(((Map<?, ?>) obj).canEqual(this))) {
            return false;
        }

        @SuppressWarnings("unchecked")
        Map<K, ?> other = (Map<K, ?>) obj;
        if (this.size() != other.size()) {
            return false;
        }

        try {
            return allMatch((k, v) -> other.getOption(k).contains(v));
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return className() + '{' + joinToString() + '}';
    }
}
