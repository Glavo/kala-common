package kala.gson.base;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import kala.control.Option;
import kala.control.Try;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;


/*
 * It is difficult to convert the exception to json. Do we want to provide it?
 */
@SuppressWarnings({"unchecked", "rawtypes"})
final class TryAdapter<T> extends TypeAdapter<Try<T>> {

    private static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (Try.class.isAssignableFrom(type.getRawType())) {
                return new TryAdapter(gson, type);
            }
            return null;
        }
    };

    public static TypeAdapterFactory factory() {
        return FACTORY;
    }

    private final TypeAdapter<T> valueAdapter;
    private final TypeAdapter<Throwable> exceptionAdapter;

    public TryAdapter(Gson gson, TypeToken<Try<T>> type) {
        if (!Try.class.isAssignableFrom(type.getRawType()))
            throw new IllegalArgumentException();

        Type javaType = type.getType();

        if (javaType instanceof ParameterizedType) {
            this.valueAdapter = (TypeAdapter<T>) gson.getAdapter(TypeToken.get(((ParameterizedType) javaType).getActualTypeArguments()[0]));
        } else {
            this.valueAdapter = (TypeAdapter<T>) gson.getAdapter(Object.class);
        }
        this.exceptionAdapter = gson.getAdapter(Throwable.class);
    }

    public TryAdapter(TypeAdapter<T> valueAdapter, TypeAdapter<Throwable> exceptionAdapter) {
        this.valueAdapter = Objects.requireNonNull(valueAdapter);
        this.exceptionAdapter = Objects.requireNonNull(exceptionAdapter);
    }

    @Override
    public void write(JsonWriter out, Try<T> value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        out.beginObject();
        if (value.isSuccess()) {
            out.name("value");
            valueAdapter.write(out, value.get());
        } else {
            out.name("cause");
            exceptionAdapter.write(out, value.getCause());
        }
        out.endObject();
    }

    @Override
    public Try<T> read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        Try<T> res;
        in.beginObject();

        String name = in.nextName();
        if (name.equals("value")) {
            res = Try.success(valueAdapter.read(in));
        } else if (name.equals("cause")) {
            res = Try.failure(exceptionAdapter.read(in));
        } else {
            throw new JsonSyntaxException("Unexpected name: " + name);
        }

        in.endObject();

        return res;
    }
}
