package kala.gson.base;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import kala.tuple.*;
import kala.tuple.primitive.PrimitiveTuple;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

@SuppressWarnings("unchecked")
public final class TupleAdapter {

    private static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        @Override
        @SuppressWarnings("rawtypes")
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            Class<? super T> rawClass = type.getRawType();
            if (Tuple.class.isAssignableFrom(rawClass)) {
                if (EmptyTuple.class.isAssignableFrom(rawClass)) {
                    return (TypeAdapter<T>) ForUnit.INSTANCE;
                } else if (Tuple1.class.isAssignableFrom(rawClass)) {
                    return new ForTuple1(gson, type);
                } else if (Tuple2.class.isAssignableFrom(rawClass)) {
                    return new ForTuple2(gson, type);
                } else if (Tuple3.class.isAssignableFrom(rawClass)) {
                    return new ForTuple3(gson, type);
                } else if (Tuple4.class.isAssignableFrom(rawClass)) {
                    return new ForTuple4(gson, type);
                } else if (Tuple5.class.isAssignableFrom(rawClass)) {
                    return new ForTuple5(gson, type);
                } else if (Tuple6.class.isAssignableFrom(rawClass)) {
                    return new ForTuple6(gson, type);
                } else if (Tuple7.class.isAssignableFrom(rawClass)) {
                    return new ForTuple7(gson, type);
                } else if (Tuple8.class.isAssignableFrom(rawClass)) {
                    return new ForTuple8(gson, type);
                } else if (Tuple9.class.isAssignableFrom(rawClass)) {
                    return new ForTuple9(gson, type);
                } else {
                    return (TypeAdapter<T>) new ForAnyTuple(gson);
                }
            }

            if (PrimitiveTuple.class.isAssignableFrom(rawClass)) {

            }

            return null;
        }
    };

    public static TypeAdapterFactory factory() {
        return FACTORY;
    }

    public static final class ForUnit extends TypeAdapter<EmptyTuple> {
        public static final ForUnit INSTANCE = new ForUnit();

        private ForUnit() {
        }

        @Override
        public void write(JsonWriter out, EmptyTuple value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }

            out.beginArray();
            out.endArray();
        }

        @Override
        public EmptyTuple read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }

            in.beginArray();
            in.endArray();

            return Unit.INSTANCE;
        }
    }

    public static final class ForTuple1<T1> extends TypeAdapter<Tuple1<T1>> {
        private final TypeAdapter<T1> adapter1;

        public ForTuple1(Gson gson, TypeToken<Tuple1<T1>> type) {
            if (!Tuple1.class.isAssignableFrom(type.getRawType()))
                throw new IllegalArgumentException(type.toString());

            Type javaType = type.getType();

            if (javaType instanceof ParameterizedType) {
                Type[] typeArguments = ((ParameterizedType) javaType).getActualTypeArguments();

                this.adapter1 = (TypeAdapter<T1>) gson.getAdapter(TypeToken.get(typeArguments[0]));
            } else {
                this.adapter1 = (TypeAdapter<T1>) gson.getAdapter(Object.class);
            }
        }

        public ForTuple1(TypeAdapter<T1> adapter1) {
            this.adapter1 = Objects.requireNonNull(adapter1);
        }

        @Override
        public void write(JsonWriter out, Tuple1<T1> value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }

            out.beginArray();
            adapter1.write(out, value.component1());
            out.endArray();
        }

        @Override
        public Tuple1<T1> read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }

            in.beginArray();
            T1 v1 = adapter1.read(in);
            in.endArray();

            return Tuple.of(v1);
        }
    }

    public static final class ForTuple2<T1, T2> extends TypeAdapter<Tuple2<T1, T2>> {
        private final TypeAdapter<T1> adapter1;
        private final TypeAdapter<T2> adapter2;

        public ForTuple2(Gson gson, TypeToken<Tuple2<T1, T2>> type) {
            if (!Tuple2.class.isAssignableFrom(type.getRawType()))
                throw new IllegalArgumentException(type.toString());

            Type javaType = type.getType();

            if (javaType instanceof ParameterizedType) {
                Type[] typeArguments = ((ParameterizedType) javaType).getActualTypeArguments();

                this.adapter1 = (TypeAdapter<T1>) gson.getAdapter(TypeToken.get(typeArguments[0]));
                this.adapter2 = (TypeAdapter<T2>) gson.getAdapter(TypeToken.get(typeArguments[1]));
            } else {
                TypeAdapter<Object> adapter = gson.getAdapter(Object.class);
                this.adapter1 = (TypeAdapter<T1>) adapter;
                this.adapter2 = (TypeAdapter<T2>) adapter;
            }
        }

        public ForTuple2(TypeAdapter<T1> adapter1, TypeAdapter<T2> adapter2) {
            this.adapter1 = Objects.requireNonNull(adapter1);
            this.adapter2 = Objects.requireNonNull(adapter2);
        }

        @Override
        public void write(JsonWriter out, Tuple2<T1, T2> value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }

            out.beginArray();
            adapter1.write(out, value.component1());
            adapter2.write(out, value.component2());
            out.endArray();
        }

        @Override
        public Tuple2<T1, T2> read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }

            in.beginArray();
            T1 v1 = adapter1.read(in);
            T2 v2 = adapter2.read(in);
            in.endArray();

            return Tuple.of(v1, v2);
        }
    }

    public static final class ForTuple3<T1, T2, T3> extends TypeAdapter<Tuple3<T1, T2, T3>> {
        private final TypeAdapter<T1> adapter1;
        private final TypeAdapter<T2> adapter2;
        private final TypeAdapter<T3> adapter3;

        public ForTuple3(Gson gson, TypeToken<Tuple3<T1, T2, T3>> type) {
            if (!Tuple3.class.isAssignableFrom(type.getRawType()))
                throw new IllegalArgumentException(type.toString());

            Type javaType = type.getType();

            if (javaType instanceof ParameterizedType) {
                Type[] typeArguments = ((ParameterizedType) javaType).getActualTypeArguments();

                this.adapter1 = (TypeAdapter<T1>) gson.getAdapter(TypeToken.get(typeArguments[0]));
                this.adapter2 = (TypeAdapter<T2>) gson.getAdapter(TypeToken.get(typeArguments[1]));
                this.adapter3 = (TypeAdapter<T3>) gson.getAdapter(TypeToken.get(typeArguments[2]));
            } else {
                TypeAdapter<Object> adapter = gson.getAdapter(Object.class);
                this.adapter1 = (TypeAdapter<T1>) adapter;
                this.adapter2 = (TypeAdapter<T2>) adapter;
                this.adapter3 = (TypeAdapter<T3>) adapter;
            }
        }

        public ForTuple3(TypeAdapter<T1> adapter1, TypeAdapter<T2> adapter2, TypeAdapter<T3> adapter3) {
            this.adapter1 = Objects.requireNonNull(adapter1);
            this.adapter2 = Objects.requireNonNull(adapter2);
            this.adapter3 = Objects.requireNonNull(adapter3);
        }

        @Override
        public void write(JsonWriter out, Tuple3<T1, T2, T3> value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }

            out.beginArray();
            adapter1.write(out, value.component1());
            adapter2.write(out, value.component2());
            adapter3.write(out, value.component3());
            out.endArray();
        }

        @Override
        public Tuple3<T1, T2, T3> read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }

            in.beginArray();
            T1 v1 = adapter1.read(in);
            T2 v2 = adapter2.read(in);
            T3 v3 = adapter3.read(in);
            in.endArray();

            return Tuple.of(v1, v2, v3);
        }
    }

    public static final class ForTuple4<T1, T2, T3, T4> extends TypeAdapter<Tuple4<T1, T2, T3, T4>> {
        private final TypeAdapter<T1> adapter1;
        private final TypeAdapter<T2> adapter2;
        private final TypeAdapter<T3> adapter3;
        private final TypeAdapter<T4> adapter4;

        public ForTuple4(Gson gson, TypeToken<Tuple4<T1, T2, T3, T4>> type) {
            if (!Tuple4.class.isAssignableFrom(type.getRawType()))
                throw new IllegalArgumentException(type.toString());

            Type javaType = type.getType();

            if (javaType instanceof ParameterizedType) {
                Type[] typeArguments = ((ParameterizedType) javaType).getActualTypeArguments();

                this.adapter1 = (TypeAdapter<T1>) gson.getAdapter(TypeToken.get(typeArguments[0]));
                this.adapter2 = (TypeAdapter<T2>) gson.getAdapter(TypeToken.get(typeArguments[1]));
                this.adapter3 = (TypeAdapter<T3>) gson.getAdapter(TypeToken.get(typeArguments[2]));
                this.adapter4 = (TypeAdapter<T4>) gson.getAdapter(TypeToken.get(typeArguments[3]));
            } else {
                TypeAdapter<Object> adapter = gson.getAdapter(Object.class);
                this.adapter1 = (TypeAdapter<T1>) adapter;
                this.adapter2 = (TypeAdapter<T2>) adapter;
                this.adapter3 = (TypeAdapter<T3>) adapter;
                this.adapter4 = (TypeAdapter<T4>) adapter;
            }
        }

        public ForTuple4(TypeAdapter<T1> adapter1, TypeAdapter<T2> adapter2, TypeAdapter<T3> adapter3, TypeAdapter<T4> adapter4) {
            this.adapter1 = Objects.requireNonNull(adapter1);
            this.adapter2 = Objects.requireNonNull(adapter2);
            this.adapter3 = Objects.requireNonNull(adapter3);
            this.adapter4 = Objects.requireNonNull(adapter4);
        }

        @Override
        public void write(JsonWriter out, Tuple4<T1, T2, T3, T4> value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }

            out.beginArray();
            adapter1.write(out, value.component1());
            adapter2.write(out, value.component2());
            adapter3.write(out, value.component3());
            adapter4.write(out, value.component4());
            out.endArray();
        }

        @Override
        public Tuple4<T1, T2, T3, T4> read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }

            in.beginArray();
            T1 v1 = adapter1.read(in);
            T2 v2 = adapter2.read(in);
            T3 v3 = adapter3.read(in);
            T4 v4 = adapter4.read(in);
            in.endArray();

            return Tuple.of(v1, v2, v3, v4);
        }
    }

    public static final class ForTuple5<T1, T2, T3, T4, T5> extends TypeAdapter<Tuple5<T1, T2, T3, T4, T5>> {
        private final TypeAdapter<T1> adapter1;
        private final TypeAdapter<T2> adapter2;
        private final TypeAdapter<T3> adapter3;
        private final TypeAdapter<T4> adapter4;
        private final TypeAdapter<T5> adapter5;

        public ForTuple5(Gson gson, TypeToken<Tuple5<T1, T2, T3, T4, T5>> type) {
            if (!Tuple5.class.isAssignableFrom(type.getRawType()))
                throw new IllegalArgumentException(type.toString());

            Type javaType = type.getType();

            if (javaType instanceof ParameterizedType) {
                Type[] typeArguments = ((ParameterizedType) javaType).getActualTypeArguments();

                this.adapter1 = (TypeAdapter<T1>) gson.getAdapter(TypeToken.get(typeArguments[0]));
                this.adapter2 = (TypeAdapter<T2>) gson.getAdapter(TypeToken.get(typeArguments[1]));
                this.adapter3 = (TypeAdapter<T3>) gson.getAdapter(TypeToken.get(typeArguments[2]));
                this.adapter4 = (TypeAdapter<T4>) gson.getAdapter(TypeToken.get(typeArguments[3]));
                this.adapter5 = (TypeAdapter<T5>) gson.getAdapter(TypeToken.get(typeArguments[4]));
            } else {
                TypeAdapter<Object> adapter = gson.getAdapter(Object.class);
                this.adapter1 = (TypeAdapter<T1>) adapter;
                this.adapter2 = (TypeAdapter<T2>) adapter;
                this.adapter3 = (TypeAdapter<T3>) adapter;
                this.adapter4 = (TypeAdapter<T4>) adapter;
                this.adapter5 = (TypeAdapter<T5>) adapter;
            }
        }

        public ForTuple5(TypeAdapter<T1> adapter1, TypeAdapter<T2> adapter2, TypeAdapter<T3> adapter3, TypeAdapter<T4> adapter4, TypeAdapter<T5> adapter5) {
            this.adapter1 = Objects.requireNonNull(adapter1);
            this.adapter2 = Objects.requireNonNull(adapter2);
            this.adapter3 = Objects.requireNonNull(adapter3);
            this.adapter4 = Objects.requireNonNull(adapter4);
            this.adapter5 = Objects.requireNonNull(adapter5);
        }

        @Override
        public void write(JsonWriter out, Tuple5<T1, T2, T3, T4, T5> value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }

            out.beginArray();
            adapter1.write(out, value.component1());
            adapter2.write(out, value.component2());
            adapter3.write(out, value.component3());
            adapter4.write(out, value.component4());
            adapter5.write(out, value.component5());
            out.endArray();
        }

        @Override
        public Tuple5<T1, T2, T3, T4, T5> read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }

            in.beginArray();
            T1 v1 = adapter1.read(in);
            T2 v2 = adapter2.read(in);
            T3 v3 = adapter3.read(in);
            T4 v4 = adapter4.read(in);
            T5 v5 = adapter5.read(in);
            in.endArray();

            return Tuple.of(v1, v2, v3, v4, v5);
        }
    }

    public static final class ForTuple6<T1, T2, T3, T4, T5, T6> extends TypeAdapter<Tuple6<T1, T2, T3, T4, T5, T6>> {
        private final TypeAdapter<T1> adapter1;
        private final TypeAdapter<T2> adapter2;
        private final TypeAdapter<T3> adapter3;
        private final TypeAdapter<T4> adapter4;
        private final TypeAdapter<T5> adapter5;
        private final TypeAdapter<T6> adapter6;

        public ForTuple6(Gson gson, TypeToken<Tuple6<T1, T2, T3, T4, T5, T6>> type) {
            if (!Tuple6.class.isAssignableFrom(type.getRawType()))
                throw new IllegalArgumentException(type.toString());

            Type javaType = type.getType();

            if (javaType instanceof ParameterizedType) {
                Type[] typeArguments = ((ParameterizedType) javaType).getActualTypeArguments();

                this.adapter1 = (TypeAdapter<T1>) gson.getAdapter(TypeToken.get(typeArguments[0]));
                this.adapter2 = (TypeAdapter<T2>) gson.getAdapter(TypeToken.get(typeArguments[1]));
                this.adapter3 = (TypeAdapter<T3>) gson.getAdapter(TypeToken.get(typeArguments[2]));
                this.adapter4 = (TypeAdapter<T4>) gson.getAdapter(TypeToken.get(typeArguments[3]));
                this.adapter5 = (TypeAdapter<T5>) gson.getAdapter(TypeToken.get(typeArguments[4]));
                this.adapter6 = (TypeAdapter<T6>) gson.getAdapter(TypeToken.get(typeArguments[5]));
            } else {
                TypeAdapter<Object> adapter = gson.getAdapter(Object.class);
                this.adapter1 = (TypeAdapter<T1>) adapter;
                this.adapter2 = (TypeAdapter<T2>) adapter;
                this.adapter3 = (TypeAdapter<T3>) adapter;
                this.adapter4 = (TypeAdapter<T4>) adapter;
                this.adapter5 = (TypeAdapter<T5>) adapter;
                this.adapter6 = (TypeAdapter<T6>) adapter;
            }
        }

        public ForTuple6(TypeAdapter<T1> adapter1, TypeAdapter<T2> adapter2, TypeAdapter<T3> adapter3, TypeAdapter<T4> adapter4, TypeAdapter<T5> adapter5, TypeAdapter<T6> adapter6) {
            this.adapter1 = Objects.requireNonNull(adapter1);
            this.adapter2 = Objects.requireNonNull(adapter2);
            this.adapter3 = Objects.requireNonNull(adapter3);
            this.adapter4 = Objects.requireNonNull(adapter4);
            this.adapter5 = Objects.requireNonNull(adapter5);
            this.adapter6 = Objects.requireNonNull(adapter6);
        }

        @Override
        public void write(JsonWriter out, Tuple6<T1, T2, T3, T4, T5, T6> value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }

            out.beginArray();
            adapter1.write(out, value.component1());
            adapter2.write(out, value.component2());
            adapter3.write(out, value.component3());
            adapter4.write(out, value.component4());
            adapter5.write(out, value.component5());
            adapter6.write(out, value.component6());
            out.endArray();
        }

        @Override
        public Tuple6<T1, T2, T3, T4, T5, T6> read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }

            in.beginArray();
            T1 v1 = adapter1.read(in);
            T2 v2 = adapter2.read(in);
            T3 v3 = adapter3.read(in);
            T4 v4 = adapter4.read(in);
            T5 v5 = adapter5.read(in);
            T6 v6 = adapter6.read(in);
            in.endArray();

            return Tuple.of(v1, v2, v3, v4, v5, v6);
        }
    }

    public static final class ForTuple7<T1, T2, T3, T4, T5, T6, T7> extends TypeAdapter<Tuple7<T1, T2, T3, T4, T5, T6, T7>> {
        private final TypeAdapter<T1> adapter1;
        private final TypeAdapter<T2> adapter2;
        private final TypeAdapter<T3> adapter3;
        private final TypeAdapter<T4> adapter4;
        private final TypeAdapter<T5> adapter5;
        private final TypeAdapter<T6> adapter6;
        private final TypeAdapter<T7> adapter7;

        public ForTuple7(Gson gson, TypeToken<Tuple7<T1, T2, T3, T4, T5, T6, T7>> type) {
            if (!Tuple7.class.isAssignableFrom(type.getRawType()))
                throw new IllegalArgumentException(type.toString());

            Type javaType = type.getType();

            if (javaType instanceof ParameterizedType) {
                Type[] typeArguments = ((ParameterizedType) javaType).getActualTypeArguments();

                this.adapter1 = (TypeAdapter<T1>) gson.getAdapter(TypeToken.get(typeArguments[0]));
                this.adapter2 = (TypeAdapter<T2>) gson.getAdapter(TypeToken.get(typeArguments[1]));
                this.adapter3 = (TypeAdapter<T3>) gson.getAdapter(TypeToken.get(typeArguments[2]));
                this.adapter4 = (TypeAdapter<T4>) gson.getAdapter(TypeToken.get(typeArguments[3]));
                this.adapter5 = (TypeAdapter<T5>) gson.getAdapter(TypeToken.get(typeArguments[4]));
                this.adapter6 = (TypeAdapter<T6>) gson.getAdapter(TypeToken.get(typeArguments[5]));
                this.adapter7 = (TypeAdapter<T7>) gson.getAdapter(TypeToken.get(typeArguments[6]));
            } else {
                TypeAdapter<Object> adapter = gson.getAdapter(Object.class);
                this.adapter1 = (TypeAdapter<T1>) adapter;
                this.adapter2 = (TypeAdapter<T2>) adapter;
                this.adapter3 = (TypeAdapter<T3>) adapter;
                this.adapter4 = (TypeAdapter<T4>) adapter;
                this.adapter5 = (TypeAdapter<T5>) adapter;
                this.adapter6 = (TypeAdapter<T6>) adapter;
                this.adapter7 = (TypeAdapter<T7>) adapter;
            }
        }

        public ForTuple7(TypeAdapter<T1> adapter1, TypeAdapter<T2> adapter2, TypeAdapter<T3> adapter3, TypeAdapter<T4> adapter4, TypeAdapter<T5> adapter5, TypeAdapter<T6> adapter6, TypeAdapter<T7> adapter7) {
            this.adapter1 = Objects.requireNonNull(adapter1);
            this.adapter2 = Objects.requireNonNull(adapter2);
            this.adapter3 = Objects.requireNonNull(adapter3);
            this.adapter4 = Objects.requireNonNull(adapter4);
            this.adapter5 = Objects.requireNonNull(adapter5);
            this.adapter6 = Objects.requireNonNull(adapter6);
            this.adapter7 = Objects.requireNonNull(adapter7);
        }

        @Override
        public void write(JsonWriter out, Tuple7<T1, T2, T3, T4, T5, T6, T7> value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }

            out.beginArray();
            adapter1.write(out, value.component1());
            adapter2.write(out, value.component2());
            adapter3.write(out, value.component3());
            adapter4.write(out, value.component4());
            adapter5.write(out, value.component5());
            adapter6.write(out, value.component6());
            adapter7.write(out, value.component7());
            out.endArray();
        }

        @Override
        public Tuple7<T1, T2, T3, T4, T5, T6, T7> read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }

            in.beginArray();
            T1 v1 = adapter1.read(in);
            T2 v2 = adapter2.read(in);
            T3 v3 = adapter3.read(in);
            T4 v4 = adapter4.read(in);
            T5 v5 = adapter5.read(in);
            T6 v6 = adapter6.read(in);
            T7 v7 = adapter7.read(in);
            in.endArray();

            return Tuple.of(v1, v2, v3, v4, v5, v6, v7);
        }
    }

    public static final class ForTuple8<T1, T2, T3, T4, T5, T6, T7, T8> extends TypeAdapter<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> {
        private final TypeAdapter<T1> adapter1;
        private final TypeAdapter<T2> adapter2;
        private final TypeAdapter<T3> adapter3;
        private final TypeAdapter<T4> adapter4;
        private final TypeAdapter<T5> adapter5;
        private final TypeAdapter<T6> adapter6;
        private final TypeAdapter<T7> adapter7;
        private final TypeAdapter<T8> adapter8;

        public ForTuple8(Gson gson, TypeToken<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> type) {
            if (!Tuple8.class.isAssignableFrom(type.getRawType()))
                throw new IllegalArgumentException(type.toString());

            Type javaType = type.getType();

            if (javaType instanceof ParameterizedType) {
                Type[] typeArguments = ((ParameterizedType) javaType).getActualTypeArguments();

                this.adapter1 = (TypeAdapter<T1>) gson.getAdapter(TypeToken.get(typeArguments[0]));
                this.adapter2 = (TypeAdapter<T2>) gson.getAdapter(TypeToken.get(typeArguments[1]));
                this.adapter3 = (TypeAdapter<T3>) gson.getAdapter(TypeToken.get(typeArguments[2]));
                this.adapter4 = (TypeAdapter<T4>) gson.getAdapter(TypeToken.get(typeArguments[3]));
                this.adapter5 = (TypeAdapter<T5>) gson.getAdapter(TypeToken.get(typeArguments[4]));
                this.adapter6 = (TypeAdapter<T6>) gson.getAdapter(TypeToken.get(typeArguments[5]));
                this.adapter7 = (TypeAdapter<T7>) gson.getAdapter(TypeToken.get(typeArguments[6]));
                this.adapter8 = (TypeAdapter<T8>) gson.getAdapter(TypeToken.get(typeArguments[7]));
            } else {
                TypeAdapter<Object> adapter = gson.getAdapter(Object.class);
                this.adapter1 = (TypeAdapter<T1>) adapter;
                this.adapter2 = (TypeAdapter<T2>) adapter;
                this.adapter3 = (TypeAdapter<T3>) adapter;
                this.adapter4 = (TypeAdapter<T4>) adapter;
                this.adapter5 = (TypeAdapter<T5>) adapter;
                this.adapter6 = (TypeAdapter<T6>) adapter;
                this.adapter7 = (TypeAdapter<T7>) adapter;
                this.adapter8 = (TypeAdapter<T8>) adapter;
            }
        }

        public ForTuple8(TypeAdapter<T1> adapter1, TypeAdapter<T2> adapter2, TypeAdapter<T3> adapter3, TypeAdapter<T4> adapter4, TypeAdapter<T5> adapter5, TypeAdapter<T6> adapter6, TypeAdapter<T7> adapter7, TypeAdapter<T8> adapter8) {
            this.adapter1 = Objects.requireNonNull(adapter1);
            this.adapter2 = Objects.requireNonNull(adapter2);
            this.adapter3 = Objects.requireNonNull(adapter3);
            this.adapter4 = Objects.requireNonNull(adapter4);
            this.adapter5 = Objects.requireNonNull(adapter5);
            this.adapter6 = Objects.requireNonNull(adapter6);
            this.adapter7 = Objects.requireNonNull(adapter7);
            this.adapter8 = Objects.requireNonNull(adapter8);
        }

        @Override
        public void write(JsonWriter out, Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }

            out.beginArray();
            adapter1.write(out, value.component1());
            adapter2.write(out, value.component2());
            adapter3.write(out, value.component3());
            adapter4.write(out, value.component4());
            adapter5.write(out, value.component5());
            adapter6.write(out, value.component6());
            adapter7.write(out, value.component7());
            adapter8.write(out, value.component8());
            out.endArray();
        }

        @Override
        public Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }

            in.beginArray();
            T1 v1 = adapter1.read(in);
            T2 v2 = adapter2.read(in);
            T3 v3 = adapter3.read(in);
            T4 v4 = adapter4.read(in);
            T5 v5 = adapter5.read(in);
            T6 v6 = adapter6.read(in);
            T7 v7 = adapter7.read(in);
            T8 v8 = adapter8.read(in);
            in.endArray();

            return Tuple.of(v1, v2, v3, v4, v5, v6, v7, v8);
        }
    }

    public static final class ForTuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> extends TypeAdapter<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> {
        private final TypeAdapter<T1> adapter1;
        private final TypeAdapter<T2> adapter2;
        private final TypeAdapter<T3> adapter3;
        private final TypeAdapter<T4> adapter4;
        private final TypeAdapter<T5> adapter5;
        private final TypeAdapter<T6> adapter6;
        private final TypeAdapter<T7> adapter7;
        private final TypeAdapter<T8> adapter8;
        private final TypeAdapter<T9> adapter9;

        public ForTuple9(Gson gson, TypeToken<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> type) {
            if (!Tuple9.class.isAssignableFrom(type.getRawType()))
                throw new IllegalArgumentException(type.toString());

            Type javaType = type.getType();

            if (javaType instanceof ParameterizedType) {
                Type[] typeArguments = ((ParameterizedType) javaType).getActualTypeArguments();

                this.adapter1 = (TypeAdapter<T1>) gson.getAdapter(TypeToken.get(typeArguments[0]));
                this.adapter2 = (TypeAdapter<T2>) gson.getAdapter(TypeToken.get(typeArguments[1]));
                this.adapter3 = (TypeAdapter<T3>) gson.getAdapter(TypeToken.get(typeArguments[2]));
                this.adapter4 = (TypeAdapter<T4>) gson.getAdapter(TypeToken.get(typeArguments[3]));
                this.adapter5 = (TypeAdapter<T5>) gson.getAdapter(TypeToken.get(typeArguments[4]));
                this.adapter6 = (TypeAdapter<T6>) gson.getAdapter(TypeToken.get(typeArguments[5]));
                this.adapter7 = (TypeAdapter<T7>) gson.getAdapter(TypeToken.get(typeArguments[6]));
                this.adapter8 = (TypeAdapter<T8>) gson.getAdapter(TypeToken.get(typeArguments[7]));
                this.adapter9 = (TypeAdapter<T9>) gson.getAdapter(TypeToken.get(typeArguments[8]));
            } else {
                TypeAdapter<Object> adapter = gson.getAdapter(Object.class);
                this.adapter1 = (TypeAdapter<T1>) adapter;
                this.adapter2 = (TypeAdapter<T2>) adapter;
                this.adapter3 = (TypeAdapter<T3>) adapter;
                this.adapter4 = (TypeAdapter<T4>) adapter;
                this.adapter5 = (TypeAdapter<T5>) adapter;
                this.adapter6 = (TypeAdapter<T6>) adapter;
                this.adapter7 = (TypeAdapter<T7>) adapter;
                this.adapter8 = (TypeAdapter<T8>) adapter;
                this.adapter9 = (TypeAdapter<T9>) adapter;
            }
        }

        public ForTuple9(TypeAdapter<T1> adapter1, TypeAdapter<T2> adapter2, TypeAdapter<T3> adapter3, TypeAdapter<T4> adapter4, TypeAdapter<T5> adapter5, TypeAdapter<T6> adapter6, TypeAdapter<T7> adapter7, TypeAdapter<T8> adapter8, TypeAdapter<T9> adapter9) {
            this.adapter1 = Objects.requireNonNull(adapter1);
            this.adapter2 = Objects.requireNonNull(adapter2);
            this.adapter3 = Objects.requireNonNull(adapter3);
            this.adapter4 = Objects.requireNonNull(adapter4);
            this.adapter5 = Objects.requireNonNull(adapter5);
            this.adapter6 = Objects.requireNonNull(adapter6);
            this.adapter7 = Objects.requireNonNull(adapter7);
            this.adapter8 = Objects.requireNonNull(adapter8);
            this.adapter9 = Objects.requireNonNull(adapter9);
        }

        @Override
        public void write(JsonWriter out, Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }

            out.beginArray();
            adapter1.write(out, value.component1());
            adapter2.write(out, value.component2());
            adapter3.write(out, value.component3());
            adapter4.write(out, value.component4());
            adapter5.write(out, value.component5());
            adapter6.write(out, value.component6());
            adapter7.write(out, value.component7());
            adapter8.write(out, value.component8());
            adapter9.write(out, value.component9());
            out.endArray();
        }

        @Override
        public Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }

            in.beginArray();
            T1 v1 = adapter1.read(in);
            T2 v2 = adapter2.read(in);
            T3 v3 = adapter3.read(in);
            T4 v4 = adapter4.read(in);
            T5 v5 = adapter5.read(in);
            T6 v6 = adapter6.read(in);
            T7 v7 = adapter7.read(in);
            T8 v8 = adapter8.read(in);
            T9 v9 = adapter9.read(in);
            in.endArray();

            return Tuple.of(v1, v2, v3, v4, v5, v6, v7, v8, v9);
        }
    }

    public static final class ForAnyTuple extends TypeAdapter<AnyTuple> {
        private final TypeAdapter<Object> adapter;

        public ForAnyTuple(Gson gson) {
            this.adapter = gson.getAdapter(Object.class);
        }

        @Override
        public void write(JsonWriter out, AnyTuple value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }
            int arity = value.arity();

            out.beginArray();
            for (int i = 0; i < arity; i++) {
                adapter.write(out, value.elementAt(i));
            }
            out.endArray();
        }

        @Override
        public AnyTuple read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }

            ArrayList<Object> tmp = new ArrayList<>();

            in.beginArray();
            while (in.peek() != JsonToken.END_ARRAY) {
                tmp.add(adapter.read(in));
            }
            in.endArray();

            return Tuple.of(tmp.toArray());
        }
    }
}
