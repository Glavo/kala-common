package kala.gson.base;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import kala.control.primitive.*;

import java.io.IOException;

public final class PrimitiveOptionTypeAdapter {
    private static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        @Override
        @SuppressWarnings("unchecked")
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (PrimitiveOption.class.isAssignableFrom(type.getRawType())) {
                if (BooleanOption.class.isAssignableFrom(type.getRawType()))
                    return (TypeAdapter<T>) ForBoolean.INSTANCE;
                else if (ByteOption.class.isAssignableFrom(type.getRawType()))
                    return (TypeAdapter<T>) ForByte.INSTANCE;
                else if (ShortOption.class.isAssignableFrom(type.getRawType()))
                    return (TypeAdapter<T>) ForShort.INSTANCE;
                else if (IntOption.class.isAssignableFrom(type.getRawType()))
                    return (TypeAdapter<T>) ForInt.INSTANCE;
                else if (LongOption.class.isAssignableFrom(type.getRawType()))
                    return (TypeAdapter<T>) ForLong.INSTANCE;
                else if (FloatOption.class.isAssignableFrom(type.getRawType()))
                    return (TypeAdapter<T>) ForFloat.INSTANCE;
                else if (DoubleOption.class.isAssignableFrom(type.getRawType()))
                    return (TypeAdapter<T>) ForDouble.INSTANCE;
                else if (CharOption.class.isAssignableFrom(type.getRawType()))
                    return (TypeAdapter<T>) ForChar.INSTANCE;
                else
                    return null;
            }
            return null;
        }
    };

    public static TypeAdapterFactory factory() {
        return FACTORY;
    }

    private PrimitiveOptionTypeAdapter() {
    }

    public static final class ForBoolean extends TypeAdapter<BooleanOption> {
        public static final ForBoolean INSTANCE = new ForBoolean();

        private ForBoolean() {
        }

        @Override
        public void write(JsonWriter out, BooleanOption value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }

            out.beginArray();
            if (value.isDefined())
                out.value(value.get());
            out.endArray();
        }

        @Override
        public BooleanOption read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }

            BooleanOption res;

            in.beginArray();
            if (in.peek() == JsonToken.END_ARRAY)
                res = BooleanOption.none();
            else
                res = BooleanOption.some(in.nextBoolean());
            in.endArray();

            return res;
        }
    }

    public static final class ForByte extends TypeAdapter<ByteOption> {
        public static final ForByte INSTANCE = new ForByte();

        private ForByte() {
        }

        @Override
        public void write(JsonWriter out, ByteOption value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }

            out.beginArray();
            if (value.isDefined())
                out.value(value.get());
            out.endArray();
        }

        @Override
        public ByteOption read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }

            ByteOption res;

            in.beginArray();
            if (in.peek() == JsonToken.END_ARRAY)
                res = ByteOption.none();
            else {
                int v = in.nextInt();
                byte b = (byte) v;
                if (v != b)
                    throw new NumberFormatException("Expected an byte but was " + v);
                res = ByteOption.some(b);
            }
            in.endArray();

            return res;
        }
    }

    public static final class ForShort extends TypeAdapter<ShortOption> {
        public static final ForShort INSTANCE = new ForShort();

        private ForShort() {
        }

        @Override
        public void write(JsonWriter out, ShortOption value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }

            out.beginArray();
            if (value.isDefined())
                out.value(value.get());
            out.endArray();
        }

        @Override
        public ShortOption read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }

            ShortOption res;

            in.beginArray();
            if (in.peek() == JsonToken.END_ARRAY)
                res = ShortOption.none();
            else {
                int v = in.nextInt();
                short s = (short) v;
                if (v != s)
                    throw new NumberFormatException("Expected an short but was " + v);
                res = ShortOption.some(s);
            }
            in.endArray();

            return res;
        }
    }

    public static final class ForInt extends TypeAdapter<IntOption> {
        public static final ForInt INSTANCE = new ForInt();

        private ForInt() {
        }

        @Override
        public void write(JsonWriter out, IntOption value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }

            out.beginArray();
            if (value.isDefined())
                out.value(value.get());
            out.endArray();
        }

        @Override
        public IntOption read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }

            IntOption res;

            in.beginArray();
            if (in.peek() == JsonToken.END_ARRAY)
                res = IntOption.none();
            else {
                res = IntOption.some(in.nextInt());
            }
            in.endArray();

            return res;
        }
    }

    public static final class ForLong extends TypeAdapter<LongOption> {
        public static final ForLong INSTANCE = new ForLong();

        private ForLong() {
        }

        @Override
        public void write(JsonWriter out, LongOption value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }

            out.beginArray();
            if (value.isDefined())
                out.value(value.get());
            out.endArray();
        }

        @Override
        public LongOption read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }

            LongOption res;

            in.beginArray();
            if (in.peek() == JsonToken.END_ARRAY)
                res = LongOption.none();
            else {
                res = LongOption.some(in.nextLong());
            }
            in.endArray();

            return res;
        }
    }

    public static final class ForFloat extends TypeAdapter<FloatOption> {
        public static final ForFloat INSTANCE = new ForFloat();

        private ForFloat() {
        }

        @Override
        public void write(JsonWriter out, FloatOption value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }

            out.beginArray();
            if (value.isDefined())
                out.value(value.get());
            out.endArray();
        }

        @Override
        public FloatOption read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }

            FloatOption res;

            in.beginArray();
            if (in.peek() == JsonToken.END_ARRAY)
                res = FloatOption.none();
            else {
                res = FloatOption.some((float) in.nextDouble());
            }
            in.endArray();

            return res;
        }
    }

    public static final class ForDouble extends TypeAdapter<DoubleOption> {
        public static final ForDouble INSTANCE = new ForDouble();

        private ForDouble() {
        }

        @Override
        public void write(JsonWriter out, DoubleOption value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }

            out.beginArray();
            if (value.isDefined())
                out.value(value.get());
            out.endArray();
        }

        @Override
        public DoubleOption read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }

            DoubleOption res;

            in.beginArray();
            if (in.peek() == JsonToken.END_ARRAY)
                res = DoubleOption.none();
            else {
                res = DoubleOption.some(in.nextDouble());
            }
            in.endArray();

            return res;
        }
    }

    public static final class ForChar extends TypeAdapter<CharOption> {
        public static final ForChar INSTANCE = new ForChar();

        private ForChar() {
        }

        @Override
        public void write(JsonWriter out, CharOption value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }

            out.beginArray();
            if (value.isDefined())
                out.value(String.valueOf(value.get()));
            out.endArray();
        }

        @Override
        public CharOption read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }

            CharOption res;

            in.beginArray();
            if (in.peek() == JsonToken.END_ARRAY)
                res = CharOption.none();
            else {
                String str = in.nextString();
                if (str.length() != 1)
                    throw new NumberFormatException("Expected a char but was " + str);
                res = CharOption.some(str.charAt(0));
            }
            in.endArray();

            return res;
        }
    }
}
