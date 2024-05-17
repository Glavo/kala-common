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
package kala.gson.base;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import kala.tuple.primitive.BooleanTuple2;
import kala.tuple.primitive.IntObjTuple2;
import kala.tuple.primitive.IntTuple3;
import kala.tuple.primitive.PrimitiveTuple;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

@SuppressWarnings("unchecked")
public final class PrimitiveTupleTypeAdapter {

    private static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        @Override
        @SuppressWarnings("rawtypes")
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            Class<? super T> rawClass = type.getRawType();
            if (PrimitiveTuple.class.isAssignableFrom(rawClass)) {
                if (BooleanTuple2.class.isAssignableFrom(rawClass)) {
                    return (TypeAdapter<T>) ForBooleanTuple2.INSTANCE;
                } else if (IntObjTuple2.class.isAssignableFrom(rawClass)) {
                    return (TypeAdapter<T>) new ForIntObjTuple2(gson, type);
                } else if (IntTuple3.class.isAssignableFrom(rawClass)) {
                    return (TypeAdapter<T>) ForIntTuple3.INSTANCE;
                }
            }

            return null;
        }
    };

    public static TypeAdapterFactory factory() {
        return FACTORY;
    }

    public static final class ForBooleanTuple2 extends TypeAdapter<BooleanTuple2> {

        public static final ForBooleanTuple2 INSTANCE = new ForBooleanTuple2();

        private ForBooleanTuple2() {
        }

        @Override
        public void write(JsonWriter out, BooleanTuple2 value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }

            out.beginArray();
            out.value(value.component1());
            out.value(value.component2());
            out.endArray();
        }

        @Override
        public BooleanTuple2 read(JsonReader in) throws IOException {
            if (in.peek() == null) {
                in.nextNull();
                return null;
            }

            in.beginArray();
            boolean v1 = in.nextBoolean();
            boolean v2 = in.nextBoolean();
            in.endArray();

            return BooleanTuple2.of(v1, v2);
        }
    }

    public static final class ForIntObjTuple2<T> extends TypeAdapter<IntObjTuple2<T>> {
        private final TypeAdapter<T> adapter;

        public ForIntObjTuple2(Gson gson, TypeToken<IntObjTuple2<T>> type) {
            if (!IntObjTuple2.class.isAssignableFrom(type.getRawType()))
                throw new IllegalArgumentException(type.toString());

            Type javaType = type.getType();
            if (javaType instanceof ParameterizedType) {
                this.adapter = (TypeAdapter<T>) gson.getAdapter(TypeToken.get(((ParameterizedType) javaType).getActualTypeArguments()[0]));
            } else {
                this.adapter = (TypeAdapter<T>) gson.getAdapter(Object.class);
            }
        }

        public ForIntObjTuple2(TypeAdapter<T> adapter) {
            this.adapter = Objects.requireNonNull(adapter);
        }

        @Override
        public void write(JsonWriter out, IntObjTuple2<T> value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }

            out.beginArray();
            out.value(value.component1());
            adapter.write(out, value.component2());
            out.endArray();
        }

        @Override
        public IntObjTuple2<T> read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }

            in.beginArray();
            int v1 = in.nextInt();
            T v2 = adapter.read(in);
            in.endArray();

            return IntObjTuple2.of(v1, v2);
        }
    }

    public static final class ForIntTuple3 extends TypeAdapter<IntTuple3> {

        public static final ForIntTuple3 INSTANCE = new ForIntTuple3();

        private ForIntTuple3() {
        }

        @Override
        public void write(JsonWriter out, IntTuple3 value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }

            out.beginArray();
            out.value(value.component1());
            out.value(value.component2());
            out.value(value.component3());
            out.endArray();
        }

        @Override
        public IntTuple3 read(JsonReader in) throws IOException {
            if (in.peek() == null) {
                in.nextNull();
                return null;
            }

            in.beginArray();
            int v1 = in.nextInt();
            int v2 = in.nextInt();
            int v3 = in.nextInt();
            in.endArray();

            return IntTuple3.of(v1, v2, v3);
        }
    }
}
