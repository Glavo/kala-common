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

import kala.ExtendedAssertions;
import kala.collection.immutable.ImmutableArray;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.Set;

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
        ExtendedAssertions.assertSetElements(Set.of("str"), this.<String>of().added("str"));
        ExtendedAssertions.assertSetElements(Set.of("str"), this.of("str").added("str"));
        ExtendedAssertions.assertSetElements(Collections.singleton(null), this.<String>of().added(null));
        ExtendedAssertions.assertSetElements(Set.of("str1", "str2"), of("str1").added("str2"));
        ExtendedAssertions.assertSetElements(Set.of("str1", "str2"), of("str1").added("str2").added("str1"));

        final int last = 12345;
        for (Integer[] data : data1()) {
            final Set<Integer> al = new HashSet<>(Arrays.asList(data));
            al.add(last);
            ExtendedAssertions.assertSetElements(al, from(data).added(last));
        }
    }

    @Test
    default void addedAllTest() {
        ExtendedAssertions.assertSetElements(Set.of(), of().addedAll(List.of()));
        ExtendedAssertions.assertSetElements(Set.of("str"), of("str").addedAll(List.of()));
        ExtendedAssertions.assertSetElements(Set.of("str"), of("str").addedAll(new String[0]));
        ExtendedAssertions.assertSetElements(Set.of("str"), this.<String>of().addedAll(new String[]{"str"}));

        for (Integer[] data : data1()) {
            ExtendedAssertions.assertSetElements(Set.of(data), this.<Integer>of().addedAll(data));
            ExtendedAssertions.assertSetElements(Set.of(data), this.<Integer>of().addedAll(Arrays.asList(data)));
            ExtendedAssertions.assertSetElements(Set.of(data), this.<Integer>of().addedAll(ImmutableArray.from(data)));
        }

        for (int i = 0; i < data1().length - 1; i++) {
            final Integer[] data = data1()[i];
            final Integer[] data2 = data1()[i + 1];

            final Set<Integer> tmp = new HashSet<>(Arrays.asList(data));

            ExtendedAssertions.assertSetElements(tmp, this.<Integer>of().addedAll(data));
            ExtendedAssertions.assertSetElements(tmp, this.<Integer>of().addedAll(Arrays.asList(data)));
            ExtendedAssertions.assertSetElements(tmp, this.<Integer>of().addedAll(ImmutableArray.from(data)));

            tmp.addAll(Arrays.asList(data2));

            ExtendedAssertions.assertSetElements(tmp, from(data).addedAll(data2));
            ExtendedAssertions.assertSetElements(tmp, from(data).addedAll(Arrays.asList(data2)));
            ExtendedAssertions.assertSetElements(tmp, from(data).addedAll(ImmutableArray.from(data2)));
        }
    }

}
