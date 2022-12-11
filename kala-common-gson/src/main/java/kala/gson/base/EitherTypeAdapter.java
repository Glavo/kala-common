package kala.gson.base;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import kala.control.Either;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

@SuppressWarnings("unchecked")
public class EitherTypeAdapter<A, B> extends TypeAdapter<Either<A, B>> {
    private static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        @Override
        @SuppressWarnings("unchecked")
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (Either.class.isAssignableFrom(type.getRawType())) {
                return new EitherTypeAdapter(gson, type);
            }
            return null;
        }
    };

    public static TypeAdapterFactory factory() {
        return FACTORY;
    }

    private final TypeAdapter<A> leftAdapter;
    private final TypeAdapter<B> rightAdapter;

    public EitherTypeAdapter(Gson gson, TypeToken<Either<A, B>> type) {
        if (!Either.class.isAssignableFrom(type.getRawType()))
            throw new IllegalArgumentException();

        Type javaType = type.getType();

        if (javaType instanceof ParameterizedType) {
            Type[] typeArguments = ((ParameterizedType) javaType).getActualTypeArguments();

            this.leftAdapter = (TypeAdapter<A>) gson.getAdapter(TypeToken.get(typeArguments[0]));
            this.rightAdapter = (TypeAdapter<B>) gson.getAdapter(TypeToken.get(typeArguments[1]));
        } else {
            this.leftAdapter = (TypeAdapter<A>) gson.getAdapter(Object.class);
            this.rightAdapter = (TypeAdapter<B>) gson.getAdapter(Object.class);
        }
    }

    public EitherTypeAdapter(TypeAdapter<A> leftAdapter, TypeAdapter<B> rightAdapter) {
        this.leftAdapter = Objects.requireNonNull(leftAdapter);
        this.rightAdapter = Objects.requireNonNull(rightAdapter);
    }

    @Override
    public void write(JsonWriter out, Either<A, B> either) throws IOException {
        if (either == null) {
            out.nullValue();
            return;
        }

        out.beginObject();
        if (either.isLeft()) {
            out.name("left");
            leftAdapter.write(out, either.getLeftValue());
        } else {
            out.name("right");
            rightAdapter.write(out, either.getRightValue());
        }
        out.endObject();
    }

    @Override
    public Either<A, B> read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        in.beginObject();
        String name = in.nextName();

        Either<A, B> either;
        switch (name) {
            case "left":
                either = Either.left(leftAdapter.read(in));
                break;
            case "right":
                either = Either.right(rightAdapter.read(in));
                break;
            default:
                throw new JsonSyntaxException("Unknown key '" + name + "'");
        }
        in.endObject();
        return either;
    }
}
