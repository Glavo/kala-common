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
package kala.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kala.gson.base.*;
import kala.gson.collection.CollectionTypeAdapter;

public final class KalaGson {
    private KalaGson() {
    }

    public static GsonBuilder registerKalaTypeAdapters(GsonBuilder builder) {
        return registerKalaBaseTypeAdapters(registerKalaCollectionTypeAdapters(builder));
    }

    public static GsonBuilder registerKalaBaseTypeAdapters(GsonBuilder builder) {
        return builder
                .registerTypeAdapterFactory(EitherTypeAdapter.factory())
                .registerTypeAdapterFactory(OptionTypeAdapter.factory())
                .registerTypeAdapterFactory(PrimitiveOptionTypeAdapter.factory())
                .registerTypeAdapterFactory(TupleTypeAdapter.factory())
                .registerTypeAdapterFactory(PrimitiveTupleTypeAdapter.factory());
    }

    public static GsonBuilder registerKalaCollectionTypeAdapters(GsonBuilder builder) {
        return builder.registerTypeAdapterFactory(CollectionTypeAdapter.factory());
    }

    public static Gson createKalaGson() {

        

        return registerKalaTypeAdapters(new GsonBuilder()).create();
    }
}
