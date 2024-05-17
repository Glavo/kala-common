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
import kala.control.Option;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

@SuppressWarnings("unchecked")
public final class OptionTypeAdapter<T> extends TypeAdapter<Option<T>> {

    private static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        @Override
        @SuppressWarnings("unchecked")
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (Option.class.isAssignableFrom(type.getRawType())) {
                return new OptionTypeAdapter(gson, type);
            }
            return null;
        }
    };

    public static TypeAdapterFactory factory() {
        return FACTORY;
    }

    private final TypeAdapter<T> valueAdapter;

    public OptionTypeAdapter(Gson gson, TypeToken<Option<T>> type) {
        if (!Option.class.isAssignableFrom(type.getRawType()))
            throw new IllegalArgumentException();

        Type javaType = type.getType();

        if (javaType instanceof ParameterizedType) {
            this.valueAdapter = (TypeAdapter<T>) gson.getAdapter(TypeToken.get(((ParameterizedType) javaType).getActualTypeArguments()[0]));
        } else {
            this.valueAdapter = (TypeAdapter<T>) gson.getAdapter(Object.class);
        }
    }

    public OptionTypeAdapter(TypeAdapter<T> valueAdapter) {
        this.valueAdapter = Objects.requireNonNull(valueAdapter);
    }

    @Override
    public void write(JsonWriter out, Option<T> value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        out.beginArray();
        if (value.isDefined()) {
            valueAdapter.write(out, value.get());
        }
        out.endArray();
    }

    @Override
    public Option<T> read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        Option<T> res;
        in.beginArray();
        if (in.peek() == JsonToken.END_ARRAY)
            res = Option.none();
        else
            res = Option.some(valueAdapter.read(in));
        in.endArray();

        return res;
    }
}
