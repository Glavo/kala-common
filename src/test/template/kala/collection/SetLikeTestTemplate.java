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
package kala.collection;

import kala.collection.immutable.ImmutableArray;
import kala.collection.immutable.ImmutableSet;
import kala.function.Predicates;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.Set;

import static kala.ExtendedAssertions.assertSetElements;
import static org.junit.jupiter.api.Assertions.*;

public interface SetLikeTestTemplate extends CollectionLikeTestTemplate {

    @Override
    @SuppressWarnings("unchecked")
    <E> SetLike<E> of(E... elements);

    @Override
    <E> SetLike<E> from(E[] elements);

    @Override
    <E> SetLike<E> from(Iterable<? extends E> elements);

    @Test
    default void iteratorTest() {
        assertFalse(of().iterator().hasNext());
        assertThrows(NoSuchElementException.class, () -> of().iterator().next());

        List<String> values = List.of("str0", "str1", "str2", "str3", "str0", "str1");
        HashSet<String> expected = new HashSet<>(values);
        SetLike<String> set0 = from(values);

        int n = 0;
        for (var v : set0) {
            assertTrue(expected.contains(v), v);
            n++;
        }
        assertEquals(expected.size(), n);
    }

    @Test
    default void addedTest() {
        assertSetElements(Set.of("str"), this.<String>of().added("str"));
        assertSetElements(Set.of("str"), this.of("str").added("str"));
        assertSetElements(Collections.singleton(null), this.<String>of().added(null));
        assertSetElements(Set.of("str1", "str2"), of("str1").added("str2"));
        assertSetElements(Set.of("str1", "str2"), of("str1").added("str2").added("str1"));

        final int last = 12345;
        for (Integer[] data : data1()) {
            final Set<Integer> al = new HashSet<>(Arrays.asList(data));
            al.add(last);
            assertSetElements(al, from(data).added(last));
        }
    }

    @Test
    default void addedAllTest() {
        assertSetElements(Set.of(), of().addedAll(List.of()));
        assertSetElements(Set.of("str"), of("str").addedAll(List.of()));
        assertSetElements(Set.of("str"), of("str").addedAll());
        assertSetElements(Set.of("str"), this.<String>of().addedAll("str"));

        for (Integer[] data : data1()) {
            assertSetElements(Set.of(data), this.<Integer>of().addedAll(data));
            assertSetElements(Set.of(data), this.<Integer>of().addedAll(Arrays.asList(data)));
            assertSetElements(Set.of(data), this.<Integer>of().addedAll(ImmutableArray.from(data)));
        }

        for (int i = 0; i < data1().length - 1; i++) {
            final Integer[] data = data1()[i];
            final Integer[] data2 = data1()[i + 1];

            final Set<Integer> tmp = new HashSet<>(Arrays.asList(data));

            assertSetElements(tmp, this.<Integer>of().addedAll(data));
            assertSetElements(tmp, this.<Integer>of().addedAll(Arrays.asList(data)));
            assertSetElements(tmp, this.<Integer>of().addedAll(ImmutableArray.from(data)));

            tmp.addAll(Arrays.asList(data2));

            assertSetElements(tmp, from(data).addedAll(data2));
            assertSetElements(tmp, from(data).addedAll(Arrays.asList(data2)));
            assertSetElements(tmp, from(data).addedAll(ImmutableArray.from(data2)));
        }
    }

    @Test
    default void removedTest() {
        var empty = this.<String>of();
        assertSetElements(List.of(), empty.removed(null));
        assertSetElements(List.of(), empty.removed("A"));

        assertSetElements(List.of("str"), of("str").removed(null));
        assertSetElements(List.of("str"), of("str").removed("not str"));
        assertSetElements(List.of(), of("str").removed("str"));
    }

    @Test
    default void removedAllTest() {
        var empty = this.<String>of();
        assertSetElements(List.of(), empty.removedAll((String) null));
        assertSetElements(List.of(), empty.removedAll("A"));
        assertSetElements(List.of(), empty.removedAll(List.of()));

        assertSetElements(List.of(1, 2, 3, 4, 5), of(1, 2, 3, 4, 5).removedAll());
        assertSetElements(List.of(1, 2, 3, 4, 5), of(1, 2, 3, 4, 5).removedAll(List.of()));
        assertSetElements(List.of(1, 2, 3, 4, 5), of(1, 2, 3, 4, 5).removedAll(java.util.Set.of()));
        assertSetElements(List.of(1, 2, 3, 4, 5), of(1, 2, 3, 4, 5).removedAll(ImmutableSet.of()));
        assertSetElements(List.of(1, 2, 4, 5), of(1, 2, 3, 4, 5).removedAll(3));
        assertSetElements(List.of(1, 2, 4, 5), of(1, 2, 3, 4, 5).removedAll(List.of(3)));
        assertSetElements(List.of(1, 2, 4, 5), of(1, 2, 3, 4, 5).removedAll(java.util.Set.of(3)));
        assertSetElements(List.of(1, 2, 4, 5), of(1, 2, 3, 4, 5).removedAll(ImmutableSet.of(3)));
        assertSetElements(List.of(1, 4, 5), of(1, 2, 3, 4, 5).removedAll(0, 3, 2, 6));
        assertSetElements(List.of(1, 4, 5), of(1, 2, 3, 4, 5).removedAll(List.of(0, 3, 2, 6)));
        assertSetElements(List.of(1, 4, 5), of(1, 2, 3, 4, 5).removedAll(java.util.Set.of(0, 3, 2, 6)));
        assertSetElements(List.of(1, 4, 5), of(1, 2, 3, 4, 5).removedAll(ImmutableSet.of(0, 3, 2, 6)));
    }

    @Test
    default void filterTest() {
        assertSetElements(List.of(), of().filter(Predicates.alwaysTrue()));
        assertSetElements(List.of(), of().filter(Predicates.alwaysFalse()));

        assertSetElements(List.of(), of(1, 2, 3, 4, 5).filter(Predicates.alwaysFalse()));
        assertSetElements(List.of(1, 2, 3, 4, 5), of(1, 2, 3, 4, 5).filter(Predicates.alwaysTrue()));
        assertSetElements(List.of(1, 3, 5), of(1, 2, 3, 4, 5).filter(it -> it % 2 == 1));
    }

    @Test
    default void filterNotTest() {
        assertSetElements(List.of(), of().filterNot(Predicates.alwaysTrue()));
        assertSetElements(List.of(), of().filterNot(Predicates.alwaysFalse()));

        assertSetElements(List.of(), of(1, 2, 3, 4, 5).filterNot(Predicates.alwaysTrue()));
        assertSetElements(List.of(1, 2, 3, 4, 5), of(1, 2, 3, 4, 5).filterNot(Predicates.alwaysFalse()));
        assertSetElements(List.of(1, 3, 5), of(1, 2, 3, 4, 5).filterNot(it -> it % 2 == 0));
    }
}
