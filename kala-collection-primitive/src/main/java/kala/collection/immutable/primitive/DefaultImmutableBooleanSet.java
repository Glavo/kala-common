package kala.collection.immutable.primitive;

import kala.collection.base.primitive.BooleanIterator;
import kala.collection.base.primitive.BooleanTraversable;
import kala.collection.factory.primitive.BooleanCollectionFactory;
import kala.collection.mutable.primitive.MutableBooleanSet;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@SuppressWarnings("PointlessBooleanExpression")
final class DefaultImmutableBooleanSet extends AbstractImmutableBooleanSet implements Serializable {
    private static final long serialVersionUID = 0L;

    private static final Factory FACTORY = new Factory();

    private static final DefaultImmutableBooleanSet EMPTY = new DefaultImmutableBooleanSet(false, false);
    private static final DefaultImmutableBooleanSet FALSE = new DefaultImmutableBooleanSet(true, false);
    private static final DefaultImmutableBooleanSet TRUE = new DefaultImmutableBooleanSet(false, true);
    private static final DefaultImmutableBooleanSet ALL = new DefaultImmutableBooleanSet(true, true);

    static DefaultImmutableBooleanSet get(boolean containsFalse, boolean containsTrue) {
        if (containsFalse) {
            return containsTrue ? ALL : FALSE;
        }
        if (containsTrue) {
            return TRUE;
        }
        return EMPTY;
    }

    private final boolean containsFalse;
    private final boolean containsTrue;

    private DefaultImmutableBooleanSet(boolean containsFalse, boolean containsTrue) {
        this.containsFalse = containsFalse;
        this.containsTrue = containsTrue;
    }

    //region Static Factories

    static BooleanCollectionFactory<?, DefaultImmutableBooleanSet> factory() {
        return FACTORY;
    }

    static @NotNull DefaultImmutableBooleanSet empty() {
        return EMPTY;
    }

    static @NotNull DefaultImmutableBooleanSet of() {
        return empty();
    }

    static @NotNull DefaultImmutableBooleanSet of(boolean value1) {
        return value1 ? TRUE : FALSE;
    }

    static @NotNull DefaultImmutableBooleanSet of(boolean value1, boolean value2) {
        return value1 == value2 ? of(value1) : ALL;
    }

    static @NotNull DefaultImmutableBooleanSet of(boolean value1, boolean value2, boolean value3) {
        boolean cf = false;
        boolean ct = false;

        //@formatter:off
        if (value1) ct = true; else cf = true;
        if (value2) ct = true; else cf = true;
        if (value3) ct = true; else cf = true;
        //@formatter:on

        return get(cf, ct);
    }

    static @NotNull DefaultImmutableBooleanSet of(boolean value1, boolean value2, boolean value3, boolean value4) {
        boolean cf = false;
        boolean ct = false;

        //@formatter:off
        if (value1) ct = true; else cf = true;
        if (value2) ct = true; else cf = true;
        if (value3) ct = true; else cf = true;
        if (value4) ct = true; else cf = true;
        //@formatter:on

        return get(cf, ct);
    }

    static @NotNull DefaultImmutableBooleanSet of(boolean value1, boolean value2, boolean value3, boolean value4, boolean value5) {
        boolean cf = false;
        boolean ct = false;

        //@formatter:off
        if (value1) ct = true; else cf = true;
        if (value2) ct = true; else cf = true;
        if (value3) ct = true; else cf = true;
        if (value4) ct = true; else cf = true;
        if (value5) ct = true; else cf = true;
        //@formatter:on

        return get(cf, ct);
    }

    static @NotNull DefaultImmutableBooleanSet of(boolean... values) {
        return from(values);
    }

    static @NotNull DefaultImmutableBooleanSet from(boolean @NotNull [] values) {
        if (values.length == 0) return EMPTY;

        boolean cf = false;
        boolean ct = false;

        for (boolean value : values) {
            if (value == false)
                cf = true;
            else
                ct = true;

            if (cf && ct)
                return ALL;
        }

        return get(cf, ct);
    }

    static @NotNull DefaultImmutableBooleanSet from(@NotNull BooleanTraversable values) {
        return from(values.iterator());
    }

    static @NotNull DefaultImmutableBooleanSet from(@NotNull BooleanIterator it) {
        boolean cf = false;
        boolean ct = false;

        while (it.hasNext()) {
            if (it.nextBoolean() == false)
                cf = true;
            else
                ct = true;

            if (cf && ct)
                return ALL;
        }

        return get(cf, ct);
    }

    //endregion

    @Override
    public boolean containsFalse() {
        return containsFalse;
    }

    @Override
    public boolean containsTrue() {
        return containsTrue;
    }

    @Override
    public @NotNull ImmutableBooleanSet added(boolean value) {
        if (value == false) {
            return containsFalse ? this : get(true, containsTrue);
        } else {
            return containsTrue ? this : get(containsFalse, true);
        }
    }

    private Object readResolve() {
        if (containsFalse) {
            return containsTrue ? ALL : FALSE;
        }
        if (containsTrue) {
            return TRUE;
        }
        return EMPTY;
    }

    private static final class Factory implements BooleanCollectionFactory<MutableBooleanSet, DefaultImmutableBooleanSet> {
        @Override
        public DefaultImmutableBooleanSet empty() {
            return DefaultImmutableBooleanSet.EMPTY;
        }

        @Override
        public MutableBooleanSet newBuilder() {
            return MutableBooleanSet.create();
        }

        @Override
        public void addToBuilder(@NotNull MutableBooleanSet builder, boolean value) {
            builder.add(value);
        }

        @Override
        public DefaultImmutableBooleanSet build(MutableBooleanSet builder) {
            return get(builder.containsFalse(), builder.containsTrue());
        }

        @Override
        public MutableBooleanSet mergeBuilder(@NotNull MutableBooleanSet builder1, @NotNull MutableBooleanSet builder2) {
            builder1.addAll(builder2);
            return builder1;
        }
    }
}
