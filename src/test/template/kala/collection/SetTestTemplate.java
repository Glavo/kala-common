/*
 * Copyright 2025 Glavo
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
package kala.collection;

import kala.SerializationUtils;
import kala.collection.factory.CollectionFactory;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("unchecked")
public interface SetTestTemplate extends SetLikeTestTemplate, CollectionTestTemplate {

    @Override
    <E> CollectionFactory<E, ?, ? extends Set<? extends E>> factory();

    @Override
    <E> Set<E> of(E... elements);

    @Override
    <E> Set<E> from(E[] elements);

    @Override
    <E> Set<E> from(Iterable<? extends E> elements);

    @Override
    default void factoryTest() {

    }

    @Test
    default void serializationTest() throws IOException, ClassNotFoundException {
        try {
            for (Integer[] data : data1()) {
                Collection<?> c = factory().from(data);
                ByteArrayOutputStream out = new ByteArrayOutputStream(4 * 128);
                new ObjectOutputStream(out).writeObject(c);
                byte[] buffer = out.toByteArray();
                ByteArrayInputStream in = new ByteArrayInputStream(buffer);
                var obj = (Set<Integer>) new ObjectInputStream(in).readObject();

                assertEquals(c, obj);
            }
        } catch (NotSerializableException ignored) {
        }

        assertEquals(of(), SerializationUtils.writeAndRead(of()));
        assertEquals(of(0), SerializationUtils.writeAndRead(of(0)));
        assertEquals(of(0, 1, 2), SerializationUtils.writeAndRead(of(0, 1, 2)));

        for (String[] data : data1s()) {
            assertEquals(from(data), SerializationUtils.writeAndRead(from(data)));
        }
    }
}
