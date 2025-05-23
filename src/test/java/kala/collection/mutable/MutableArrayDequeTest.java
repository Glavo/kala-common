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
package kala.collection.mutable;

import kala.collection.base.ObjectArrays;
import kala.collection.factory.CollectionFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public final class MutableArrayDequeTest implements MutableListTestTemplate {

    @Override
    public <E> CollectionFactory<E, ?, MutableArrayDeque<E>> factory() {
        return MutableArrayDeque.factory();
    }

    @Override
    public <E> MutableArrayDeque<E> of(E... elements) {
        return this.<E>factory().from(elements);
    }

    @Override
    public <E> MutableArrayDeque<E> from(E[] elements) {
        return this.<E>factory().from(elements);
    }

    @Override
    public <E> MutableArrayDeque<E> from(Iterable<? extends E> elements) {
        return this.<E>factory().from(elements);
    }

    @Test
    public void constructorTest() {
        assertThrows(IllegalArgumentException.class, () -> new MutableArrayDeque<>(-1));

        MutableArrayDeque<String> deque = new MutableArrayDeque<>();
        assertSame(ObjectArrays.EMPTY, deque.elements);
    }

    @Test
    @Override
    public void removeAtTest() {
        MutableListTestTemplate.super.removeAtTest();

        MutableArrayDeque<Object> list = new MutableArrayDeque<>(6);
        list.prepend("value1");
        list.prepend("value0");
        list.append("value2");
        list.append("value3");
        assertIterableEquals(List.of("value0", "value1", "value2", "value3"), list);

        assertEquals("value1", list.removeAt(1));
        assertIterableEquals(List.of("value0", "value2", "value3"), list);

        assertEquals("value2", list.removeAt(1));
        assertIterableEquals(List.of("value0", "value3"), list);

        assertEquals("value0", list.removeAt(0));
        assertIterableEquals(List.of("value3"), list);

        assertEquals("value3", list.removeAt(0));
        assertIterableEquals(List.of(), list);
    }

    @Test
    @Override
    public void insertTest() {
        MutableListTestTemplate.super.insertTest();

        MutableArrayDeque<String> l = new MutableArrayDeque<>(4);
        l.appendAll(List.of("value0", "value1", "value2", "value3"));
        l.removeFirst();
        l.removeFirst();
        l.insert(0, "insertNonFull value");

        assertIterableEquals(List.of("insertNonFull value", "value2", "value3"), l);
    }
}
