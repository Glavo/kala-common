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
package kala.gson.collection;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import kala.collection.*;
import kala.collection.factory.CollectionFactory;
import kala.collection.immutable.*;
import kala.collection.mutable.*;
import kala.gson.internal.TypeUtils;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

@SuppressWarnings("unchecked")
public class CollectionTypeAdapter<E, C extends CollectionLike<E>> extends TypeAdapter<C> {

    private static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        @Override
        @SuppressWarnings("rawtypes")
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (CollectionLike.class.isAssignableFrom(type.getRawType())) {
                return new CollectionTypeAdapter(gson, type);
            }
            return null;
        }
    };

    public static TypeAdapterFactory factory() {
        return FACTORY;
    }

    private final TypeAdapter<E> elementAdapter;
    private final CollectionFactory<E, ?, C> factory;

    public CollectionTypeAdapter(Gson gson, TypeToken<C> type) {
        Class<? super C> rawType = type.getRawType();
        if (!CollectionLike.class.isAssignableFrom(rawType))
            throw new IllegalArgumentException(type.toString());

        Type collectionLikeType = TypeUtils.getSupertype(type.getType(), rawType, CollectionLike.class);
        Type elementType = collectionLikeType instanceof ParameterizedType
                ? ((ParameterizedType) collectionLikeType).getActualTypeArguments()[0]
                : Object.class;

        this.elementAdapter = (TypeAdapter<E>) gson.getAdapter(TypeToken.get(elementType));

        CollectionFactory<E, ?, ? extends CollectionLike<E>> factory = null;

        if (rawType.isAssignableFrom(CollectionView.class)) {
            if (SetView.class.isAssignableFrom(rawType)) {
                factory = Set.<E>factory().mapResult(Set::view);
            } else {
                factory = Seq.<E>factory().mapResult(Seq::view);
                ;
            }
        } else {
            // Builtin Types
            if (rawType.getName().startsWith("kala.collection.")) {
                if (Seq.class.isAssignableFrom(rawType)) {
                    if (ImmutableSeq.class.isAssignableFrom(rawType)) {
                        if (ImmutableArray.class.isAssignableFrom(rawType))
                            factory = ImmutableArray.factory();
                        else if (ImmutableVector.class.isAssignableFrom(rawType))
                            factory = ImmutableVector.factory();
                        else if (ImmutableLinkedSeq.class.isAssignableFrom(rawType))
                            factory = ImmutableLinkedSeq.factory();
                        else
                            factory = ImmutableSeq.factory();
                    } else if (MutableList.class.isAssignableFrom(rawType)) {
                        if (MutableArrayList.class.isAssignableFrom(rawType))
                            factory = MutableArrayList.factory();
                        else if (MutableArrayDeque.class.isAssignableFrom(rawType))
                            factory = MutableArrayDeque.factory();
                        else if (MutableSmartArrayList.class.isAssignableFrom(rawType))
                            factory = MutableSmartArrayList.factory();
                        else if (MutableLinkedList.class.isAssignableFrom(rawType))
                            factory = MutableLinkedList.factory();
                        else if (MutableSinglyLinkedList.class.isAssignableFrom(rawType))
                            factory = MutableSinglyLinkedList.factory();
                        else
                            factory = MutableList.factory();
                    } else if (MutableSeq.class.isAssignableFrom(rawType)) {
                        factory = MutableSeq.factory();
                    } else {
                        if (ArraySeq.class.isAssignableFrom(rawType))
                            factory = ArraySeq.factory();
                        else
                            factory = Seq.factory();
                    }
                } else if (Set.class.isAssignableFrom(rawType)) {
                    if (SortedSet.class.isAssignableFrom(rawType)) {
                        if (ImmutableSet.class.isAssignableFrom(rawType))
                            factory = ImmutableSortedArraySet.factory(null);
                        else if (MutableSet.class.isAssignableFrom(rawType))
                            factory = MutableTreeSet.factory(null);
                        else
                            factory = ImmutableSortedArraySet.factory(null);
                    } else {
                        if (ImmutableSet.class.isAssignableFrom(rawType)) {
                            factory = ImmutableHashSet.factory();
                        } else if (MutableSet.class.isAssignableFrom(rawType)) {

                            factory = MutableHashSet.factory();
                        } else {
                            factory = ImmutableHashSet.factory();
                        }
                    }
                }
            }

            if (factory == null) {
                MethodType factoryMethodType = MethodType.methodType(CollectionFactory.class);
                try {
                    factory = (CollectionFactory<E, ?, ? extends CollectionLike<E>>)
                            MethodHandles.publicLookup().findStatic(rawType, "factory", factoryMethodType).invoke();
                } catch (Throwable ignored) {
                }
            }

            if (factory == null) {
                if (Seq.class.isAssignableFrom(rawType)) {
                    if (ImmutableSeq.class.isAssignableFrom(rawType))
                        factory = ImmutableSeq.factory();
                    else if (MutableList.class.isAssignableFrom(rawType))
                        factory = MutableList.factory();
                    else if (MutableSeq.class.isAssignableFrom(rawType))
                        factory = MutableSeq.factory();
                    else
                        factory = Seq.factory();
                } else if (Set.class.isAssignableFrom(rawType)) {
                    if (SortedSet.class.isAssignableFrom(rawType)) {
                        if (ImmutableSet.class.isAssignableFrom(rawType))
                            factory = ImmutableSortedArraySet.factory(null);
                        else if (MutableSet.class.isAssignableFrom(rawType))
                            factory = MutableTreeSet.factory(null);
                        else
                            factory = ImmutableSortedArraySet.factory(null);
                    } else {
                        if (ImmutableSet.class.isAssignableFrom(rawType)) {
                            factory = ImmutableSet.factory();
                        } else if (MutableSet.class.isAssignableFrom(rawType)) {
                            factory = MutableSet.factory();
                        } else {
                            factory = Set.factory();
                        }
                    }
                } else {
                    factory = Seq.factory();
                }
            }
        }
        this.factory = (CollectionFactory<E, ?, C>) factory;
    }

    public CollectionTypeAdapter(TypeAdapter<E> elementAdapter, CollectionFactory<E, ?, C> factory) {
        this.elementAdapter = Objects.requireNonNull(elementAdapter);
        this.factory = factory;
    }

    @Override
    public void write(JsonWriter out, C value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        out.beginArray();
        for (E e : value) {
            elementAdapter.write(out, e);
        }
        out.endArray();
    }

    @Override
    public C read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        return read(in, factory);
    }

    private <Builder> C read(JsonReader in, CollectionFactory<E, Builder, C> factory) throws IOException {
        in.beginArray();
        if (in.peek() == JsonToken.END_ARRAY) {
            in.endArray();
            return factory.empty();
        }

        Builder builder = factory.newBuilder();
        while (in.peek() != JsonToken.END_ARRAY) {
            factory.addToBuilder(builder, elementAdapter.read(in));
        }
        in.endArray();

        return factory.build(builder);
    }
}
