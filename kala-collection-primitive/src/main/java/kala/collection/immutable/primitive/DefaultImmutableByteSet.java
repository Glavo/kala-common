/*
 * Copyright 2024 Glavo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kala.collection.immutable.primitive;

import kala.collection.base.primitive.ByteIterator;
import kala.collection.base.primitive.ByteTraversable;
import kala.collection.factory.primitive.ByteCollectionFactory;
import kala.collection.mutable.primitive.MutableByteSet;
import kala.collection.primitive.AbstractDefaultByteSet;
import org.jetbrains.annotations.NotNull;

final class DefaultImmutableByteSet extends AbstractDefaultByteSet implements ImmutableByteSet {
    private static final long serialVersionUID = 6028841354144450491L;

    private static final Factory FACTORY = new Factory();
    static final DefaultImmutableByteSet EMPTY = new DefaultImmutableByteSet(0, 0, 0, 0, 0);

    DefaultImmutableByteSet(int size, long bits0, long bits1, long bits2, long bits3) {
        this.size = size;
        this.bits0 = bits0;
        this.bits1 = bits1;
        this.bits2 = bits2;
        this.bits3 = bits3;
    }

    //region Static Factories

    static ByteCollectionFactory<?, DefaultImmutableByteSet> factory() {
        return FACTORY;
    }

    static @NotNull DefaultImmutableByteSet empty() {
        return EMPTY;
    }

    static @NotNull DefaultImmutableByteSet of() {
        return EMPTY;
    }

    static @NotNull DefaultImmutableByteSet of(byte value1) {
        MutableByteSet builder = FACTORY.newBuilder();
        builder.add(value1);
        return FACTORY.build(builder);
    }

    static @NotNull DefaultImmutableByteSet of(byte value1, byte value2) {
        MutableByteSet builder = FACTORY.newBuilder();
        builder.add(value1);
        builder.add(value2);
        return FACTORY.build(builder);
    }

    static @NotNull DefaultImmutableByteSet of(byte value1, byte value2, byte value3) {
        MutableByteSet builder = FACTORY.newBuilder();
        builder.add(value1);
        builder.add(value2);
        builder.add(value3);
        return FACTORY.build(builder);
    }

    static @NotNull DefaultImmutableByteSet of(byte value1, byte value2, byte value3, byte value4) {
        MutableByteSet builder = FACTORY.newBuilder();
        builder.add(value1);
        builder.add(value2);
        builder.add(value3);
        builder.add(value4);
        return FACTORY.build(builder);
    }

    static @NotNull DefaultImmutableByteSet of(byte value1, byte value2, byte value3, byte value4, byte value5) {
        MutableByteSet builder = FACTORY.newBuilder();
        builder.add(value1);
        builder.add(value2);
        builder.add(value3);
        builder.add(value4);
        builder.add(value5);
        return FACTORY.build(builder);
    }

    static @NotNull DefaultImmutableByteSet of(byte... values) {
        return from(values);
    }

    static @NotNull DefaultImmutableByteSet from(byte @NotNull [] values) {
        if (values.length == 0)
            return EMPTY;

        MutableByteSet builder = FACTORY.newBuilder();
        builder.addAll(values);
        return FACTORY.build(builder);
    }

    static @NotNull DefaultImmutableByteSet from(@NotNull ByteTraversable values) {
        return from(values.iterator());
    }

    static @NotNull DefaultImmutableByteSet from(@NotNull ByteIterator it) {
        if (!it.hasNext())
            return EMPTY;

        MutableByteSet builder = FACTORY.newBuilder();
        while (it.hasNext()) {
            builder.add(it.nextByte());
        }
        return FACTORY.build(builder);
    }

    //endregion

    @Override
    public @NotNull ImmutableByteSet added(byte value) {
        int v = value + 128;

        int bitsNumber = v / Long.SIZE;
        int bitOffset = v % Long.SIZE;

        long bits;
        switch (bitsNumber) {
            case 0:
                bits = bits0;
                break;
            case 1:
                bits = bits1;
                break;
            case 2:
                bits = bits2;
                break;
            case 3:
                bits = bits3;
                break;
            default:
                throw new AssertionError();
        }

        long newBits = bits | (1L << bitOffset);

        if (newBits == bits) {
            return this;
        }

        switch (bitsNumber) {
            case 0:
                return new DefaultImmutableByteSet(size + 1, newBits, bits1, bits2, bits3);
            case 1:
                return new DefaultImmutableByteSet(size + 1, bits0, newBits, bits2, bits3);
            case 2:
                return new DefaultImmutableByteSet(size + 1, bits0, bits1, newBits, bits3);
            case 3:
                return new DefaultImmutableByteSet(size + 1, bits0, bits1, bits2, newBits);
            default:
                throw new AssertionError();
        }
    }

    private Object readResolve() {
        if (size == 0)
            return EMPTY;
        else
            return this;
    }

    private static final class Factory implements ByteCollectionFactory<MutableByteSet, DefaultImmutableByteSet> {
        @Override
        public MutableByteSet newBuilder() {
            return MutableByteSet.create();
        }

        @Override
        public void addToBuilder(@NotNull MutableByteSet builder, byte value) {
            builder.add(value);
        }

        @Override
        public MutableByteSet mergeBuilder(@NotNull MutableByteSet builder1, @NotNull MutableByteSet builder2) {
            return null;
        }

        @Override
        public DefaultImmutableByteSet build(MutableByteSet builder) {
            AbstractDefaultByteSet set = (AbstractDefaultByteSet) builder;
            return set.isEmpty()
                    ? EMPTY
                    : new DefaultImmutableByteSet(set.size(), set.bits0(), set.bits1(), set.bits2(), set.bits3());
        }
    }
}
