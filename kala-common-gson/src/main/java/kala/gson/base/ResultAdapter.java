package kala.gson.base;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import kala.control.Result;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

@SuppressWarnings({"unchecked"})
public final class ResultAdapter<T, E> extends TypeAdapter<Result<T, E>> {
    private static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        @Override
        @SuppressWarnings("unchecked")
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (Result.class.isAssignableFrom(type.getRawType())) {
                return new ResultAdapter(gson, type);
            }
            return null;
        }
    };

    public static TypeAdapterFactory factory() {
        return FACTORY;
    }

    private final TypeAdapter<T> valueAdapter;
    private final TypeAdapter<E> errAdapter;

    public ResultAdapter(Gson gson, TypeToken<Result<T, E>> type) {
        if (!Result.class.isAssignableFrom(type.getRawType()))
            throw new IllegalArgumentException();

        Type javaType = type.getType();

        if (javaType instanceof ParameterizedType) {
            Type[] typeArguments = ((ParameterizedType) javaType).getActualTypeArguments();

            this.valueAdapter = (TypeAdapter<T>) gson.getAdapter(TypeToken.get(typeArguments[0]));
            this.errAdapter = (TypeAdapter<E>) gson.getAdapter(TypeToken.get(typeArguments[1]));
        } else {
            this.valueAdapter = (TypeAdapter<T>) gson.getAdapter(Object.class);
            this.errAdapter = (TypeAdapter<E>) gson.getAdapter(Object.class);
        }
    }

    public ResultAdapter(Gson gson, TypeToken<T> valueType, TypeToken<E> errType) {
        this.valueAdapter = gson.getAdapter(valueType);
        this.errAdapter = gson.getAdapter(errType);
    }

    public ResultAdapter(TypeAdapter<T> valueAdapter, TypeAdapter<E> errAdapter) {
        this.valueAdapter = Objects.requireNonNull(valueAdapter);
        this.errAdapter = Objects.requireNonNull(errAdapter);
    }

    @Override
    public void write(JsonWriter out, Result<T, E> result) throws IOException {
        if (result == null) {
            out.nullValue();
            return;
        }

        out.beginObject();
        if (result.isDefined()) {
            out.name("value");
            valueAdapter.write(out, result.get());
        } else {
            out.name("err");
            errAdapter.write(out, result.getErr());
        }
        out.endObject();
    }

    @Override
    public Result<T, E> read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        in.beginObject();
        String name = in.nextName();

        Result<T, E> result;
        switch (name) {
            case "value":
                result = Result.ok(valueAdapter.read(in));
                break;
            case "err":
                result = Result.err(errAdapter.read(in));
                break;
            default:
                throw new JsonSyntaxException("Unknown key '" + name + "'");
        }
        in.endObject();
        return result;
    }
}
