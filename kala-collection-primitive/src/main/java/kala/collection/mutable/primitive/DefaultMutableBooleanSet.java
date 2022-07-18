package kala.collection.mutable.primitive;

import kala.collection.base.primitive.BooleanIterator;
import kala.collection.base.primitive.BooleanTraversable;
import kala.collection.factory.primitive.BooleanCollectionFactory;
import kala.function.BooleanConsumer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

final class DefaultMutableBooleanSet extends AbstractMutableBooleanSet implements Serializable {
    private static final long serialVersionUID = 0L;

    private static final Factory FACTORY = new Factory();

    //region Static Factories

    @Contract(pure = true)
    static @NotNull BooleanCollectionFactory<?, DefaultMutableBooleanSet> factory() {
        return FACTORY;
    }

    @Contract(value = "-> new")
    static @NotNull MutableBooleanSet create() {
        return new DefaultMutableBooleanSet();
    }

    @Contract(value = "-> new")
    static @NotNull MutableBooleanSet of() {
        return new DefaultMutableBooleanSet();
    }

    @Contract(value = "_ -> new")
    static @NotNull MutableBooleanSet of(boolean value1) {
        DefaultMutableBooleanSet res = new DefaultMutableBooleanSet();
        res.add(value1);
        return res;
    }

    @Contract(value = "_, _ -> new")
    static @NotNull MutableBooleanSet of(boolean value1, boolean value2) {
        DefaultMutableBooleanSet res = new DefaultMutableBooleanSet();
        res.add(value1);
        res.add(value2);
        return res;
    }

    @Contract(value = "_, _, _ -> new")
    static @NotNull MutableBooleanSet of(boolean value1, boolean value2, boolean value3) {
        DefaultMutableBooleanSet res = new DefaultMutableBooleanSet();
        res.add(value1);
        res.add(value2);
        res.add(value3);
        return res;
    }

    @Contract(value = "_, _, _, _ -> new")
    static @NotNull MutableBooleanSet of(boolean value1, boolean value2, boolean value3, boolean value4) {
        DefaultMutableBooleanSet res = new DefaultMutableBooleanSet();
        res.add(value1);
        res.add(value2);
        res.add(value3);
        res.add(value4);
        return res;
    }

    @Contract(value = "_, _, _, _, _ -> new")
    static @NotNull MutableBooleanSet of(boolean value1, boolean value2, boolean value3, boolean value4, boolean value5) {
        DefaultMutableBooleanSet res = new DefaultMutableBooleanSet();
        res.add(value1);
        res.add(value2);
        res.add(value3);
        res.add(value4);
        res.add(value5);
        return res;
    }

    @Contract(value = "_ -> new")
    static @NotNull MutableBooleanSet of(boolean... values) {
        return from(values);
    }

    @Contract(value = "_ -> new")
    static @NotNull MutableBooleanSet from(boolean @NotNull [] values) {
        DefaultMutableBooleanSet res = new DefaultMutableBooleanSet();
        res.addAll(values);
        return res;
    }

    @Contract(value = "_ -> new")
    static @NotNull MutableBooleanSet from(@NotNull BooleanTraversable values) {
        DefaultMutableBooleanSet res = new DefaultMutableBooleanSet();
        res.addAll(values);
        return res;
    }

    //endregion

    private boolean hasFalse = false;
    private boolean hasTrue = false;

    @Override
    public @NotNull BooleanIterator iterator() {
        if (hasFalse) {
            return hasTrue ? BooleanIterator.of(false, true) : BooleanIterator.of(false);
        }
        if (hasTrue) {
            return BooleanIterator.of(true);
        }

        return BooleanIterator.empty();
    }

    //region Size Info

    @Override
    public boolean isEmpty() {
        return !hasFalse && !hasTrue;
    }

    @Override
    public int size() {
        if (hasFalse) {
            return hasTrue ? 2 : 1;
        }

        return hasTrue ? 1 : 0;
    }

    @Override
    public int knownSize() {
        return size();
    }

    //endregion

    @Override
    public boolean contains(boolean value) {
        return value ? hasTrue : hasFalse;
    }

    @Override
    public boolean add(boolean value) {
        if (value) {
            if (hasTrue) return false;
            hasTrue = true;
        } else {
            if (hasFalse) return false;
            hasFalse = true;
        }
        return true;
    }

    @Override
    public boolean addAll(@NotNull BooleanTraversable values) {
        boolean ht = this.hasTrue;
        boolean hf = this.hasFalse;

        if (ht && hf)
            return false;

        BooleanIterator it = values.iterator();
        while (it.hasNext()) {
            if (it.nextBoolean()) {
                ht = true;
            } else {
                hf = true;
            }

            if (ht && hf)
                return true;
        }

        boolean res = false;
        if (ht != this.hasTrue) {
            this.hasTrue = ht;
            res = true;
        }
        if (hf != this.hasFalse) {
            this.hasFalse = hf;
            res = true;
        }
        return res;
    }

    @Override
    public boolean remove(boolean value) {
        if (value) {
            if (!hasTrue) return false;
            hasTrue = false;
        } else {
            if (!hasFalse) return false;
            hasFalse = false;
        }
        return true;
    }

    @Override
    public boolean removeAll(@NotNull BooleanTraversable values) {
        boolean ht = this.hasTrue;
        boolean hf = this.hasFalse;

        if (!ht && !hf)
            return false;

        BooleanIterator it = values.iterator();
        while (it.hasNext()) {
            if (it.nextBoolean()) {
                ht = false;
            } else {
                hf = false;
            }

            if (!ht && !hf)
                return true;
        }

        boolean res = false;
        if (ht != this.hasTrue) {
            this.hasTrue = ht;
            res = true;
        }
        if (hf != this.hasFalse) {
            this.hasFalse = hf;
            res = true;
        }
        return res;
    }

    @Override
    public void clear() {
        hasFalse = false;
        hasTrue = false;
    }

    @Override
    public void forEach(@NotNull BooleanConsumer action) {
        if (hasFalse)
            action.accept(false);
        if (hasTrue)
            action.accept(true);
    }

    private static final class Factory extends AbstractMutableBooleanSetFactory<DefaultMutableBooleanSet> {
        @Override
        public DefaultMutableBooleanSet newBuilder() {
            return new DefaultMutableBooleanSet();
        }
    }
}
