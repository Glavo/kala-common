package kala.collection.primitive;

import kala.collection.base.primitive.AbstractByteIterator;
import kala.collection.base.primitive.ByteIterator;
import kala.function.ByteConsumer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.NoSuchElementException;

@ApiStatus.Internal
public abstract class AbstractDefaultByteSet extends AbstractByteSet implements Serializable {
    private static final long serialVersionUID = -2058604240590412070L;

    protected int size = 0;
    protected long bits0, bits1, bits2, bits3;

    public long bits0() {
        return bits0;
    }

    public long bits1() {
        return bits1;
    }

    public long bits2() {
        return bits2;
    }

    public long bits3() {
        return bits3;
    }

    @Override
    public @NotNull ByteIterator iterator() {
        if (size == 0)
            return ByteIterator.empty();

        return new Itr(bits0, bits1, bits2, bits3);
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int knownSize() {
        return size;
    }

    @Override
    public boolean contains(byte value) {
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

        return ((bits >> bitOffset) & 1) != 0;
    }

    @Override
    public void forEach(@NotNull ByteConsumer action) {
        if (size == 0) return;

        int cursor = 0;
        do {
            if (cursor > 255) return;

            int bitsNumber = cursor / Long.SIZE;
            int bitOffset = cursor % Long.SIZE;

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

            if (((bits >> bitOffset) & 1) != 0) {
                action.accept((byte) (cursor++ - 128));
                continue;
            }

            if (bits == 0) {
                cursor = (bitsNumber + 1) * Long.SIZE;
                continue;
            }

            cursor++;
        } while (true);
    }

    private static final class Itr extends AbstractByteIterator {
        private int cursor = 0;
        private final long bits0, bits1, bits2, bits3;

        Itr(long bits0, long bits1, long bits2, long bits3) {
            this.bits0 = bits0;
            this.bits1 = bits1;
            this.bits2 = bits2;
            this.bits3 = bits3;
        }

        @Override
        public boolean hasNext() {
            do {
                if (cursor > 255) return false;

                int bitsNumber = cursor / Long.SIZE;
                int bitOffset = cursor % Long.SIZE;

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

                if (((bits >> bitOffset) & 1) != 0)
                    return true;

                if (bits == 0) {
                    cursor = (bitsNumber + 1) * Long.SIZE;
                    continue;
                }

                cursor++;
            } while (true);
        }

        @Override
        public byte nextByte() {
            if (!hasNext()) throw new NoSuchElementException();

            return (byte) (cursor++ - 128);
        }
    }
}
