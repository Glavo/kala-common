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
package kala.collection.mutable.primitive;

import kala.collection.base.primitive.ByteIterator;
import kala.collection.base.primitive.ByteTraversable;
import kala.collection.factory.primitive.ByteCollectionFactory;
import kala.collection.primitive.AbstractDefaultByteSet;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;

final class DefaultMutableByteSet extends AbstractDefaultByteSet implements MutableByteSet {
    @Serial
    private static final long serialVersionUID = 6028841354144450491L;

    private static final Factory FACTORY = new Factory();

    @Contract(pure = true)
    static @NotNull ByteCollectionFactory<?, DefaultMutableByteSet> factory() {
        return FACTORY;
    }

    @Contract(value = "-> new")
    static @NotNull DefaultMutableByteSet create() {
        return new DefaultMutableByteSet();
    }

    @Contract(value = "-> new")
    static @NotNull DefaultMutableByteSet of() {
        return new DefaultMutableByteSet();
    }

    @Contract(value = "_ -> new")
    static @NotNull DefaultMutableByteSet of(byte value1) {
        DefaultMutableByteSet res = new DefaultMutableByteSet();
        res.add(value1);
        return res;
    }

    @Contract(value = "_, _ -> new")
    static @NotNull DefaultMutableByteSet of(byte value1, byte value2) {
        DefaultMutableByteSet res = new DefaultMutableByteSet();
        res.add(value1);
        res.add(value2);
        return res;
    }

    @Contract(value = "_, _, _ -> new")
    static @NotNull DefaultMutableByteSet of(byte value1, byte value2, byte value3) {
        DefaultMutableByteSet res = new DefaultMutableByteSet();
        res.add(value1);
        res.add(value2);
        res.add(value3);
        return res;
    }

    @Contract(value = "_, _, _, _ -> new")
    static @NotNull DefaultMutableByteSet of(byte value1, byte value2, byte value3, byte value4) {
        DefaultMutableByteSet res = new DefaultMutableByteSet();
        res.add(value1);
        res.add(value2);
        res.add(value3);
        res.add(value4);
        return res;
    }

    @Contract(value = "_, _, _, _, _ -> new")
    static @NotNull DefaultMutableByteSet of(byte value1, byte value2, byte value3, byte value4, byte value5) {
        DefaultMutableByteSet res = new DefaultMutableByteSet();
        res.add(value1);
        res.add(value2);
        res.add(value3);
        res.add(value4);
        res.add(value5);
        return res;
    }

    @Contract(value = "_ -> new")
    static @NotNull DefaultMutableByteSet of(byte... values) {
        return from(values);
    }

    @Contract(value = "_ -> new")
    static @NotNull DefaultMutableByteSet from(byte @NotNull [] values) {
        DefaultMutableByteSet res = new DefaultMutableByteSet();
        for (byte value : values) {
            res.add(value);
        }
        return res;
    }

    @Contract(value = "_ -> new")
    static @NotNull DefaultMutableByteSet from(@NotNull ByteTraversable values) {
        return from(values.iterator());
    }

    @Contract(value = "_ -> new")
    static @NotNull DefaultMutableByteSet from(@NotNull ByteIterator it) {
        DefaultMutableByteSet res = new DefaultMutableByteSet();
        while (it.hasNext()) {
            res.add(it.nextByte());
        }
        return res;
    }

    @Override
    public boolean add(byte value) {
        int v = value + 128;

        int bitsNumber = v / Long.SIZE;
        int bitOffset = v % Long.SIZE;

        long bits = switch (bitsNumber) {
            case 0 -> bits0;
            case 1 -> bits1;
            case 2 -> bits2;
            case 3 -> bits3;
            default -> throw new AssertionError();
        };

        long newBits = bits | (1L << bitOffset);

        if (newBits == bits) {
            return false;
        }

        switch (bitsNumber) {
            case 0:
                bits0 = newBits;
                break;
            case 1:
                bits1 = newBits;
                break;
            case 2:
                bits2 = newBits;
                break;
            case 3:
                bits3 = newBits;
                break;
            default:
                throw new AssertionError();
        }
        size++;
        return true;
    }

    @Override
    public boolean remove(byte value) {
        int v = value + 128;

        int bitsNumber = v / Long.SIZE;
        int bitOffset = v % Long.SIZE;

        long bits = switch (bitsNumber) {
            case 0 -> bits0;
            case 1 -> bits1;
            case 2 -> bits2;
            case 3 -> bits3;
            default -> throw new AssertionError();
        };

        long newBits = bits & ~(1L << bitOffset);

        if (newBits == bits) {
            return false;
        }

        switch (bitsNumber) {
            case 0:
                bits0 = newBits;
                break;
            case 1:
                bits1 = newBits;
                break;
            case 2:
                bits2 = newBits;
                break;
            case 3:
                bits3 = newBits;
                break;
            default:
                throw new AssertionError();
        }
        size--;
        return true;
    }

    @Override
    public void clear() {
        size = 0;
        bits0 = 0;
        bits1 = 0;
        bits2 = 0;
        bits3 = 0;
    }

    private static final class Factory extends AbstractMutableByteSetFactory<DefaultMutableByteSet> {
        @Override
        public DefaultMutableByteSet newBuilder() {
            return new DefaultMutableByteSet();
        }
    }
}
