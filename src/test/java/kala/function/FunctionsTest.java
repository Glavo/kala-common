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
package kala.function;

import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import org.junit.jupiter.api.*;

import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("DataFlowIssue")
public class FunctionsTest {
    private final Object obj = new Object();

    @Test
    void identityTest() {
        assertSame(obj, Functions.identity().apply(obj));
        assertNull(Functions.identity().apply(null));
        assertSame(obj, Functions.identity().andThen(Optional::of).apply(obj).get());
        assertSame(obj, Functions.identity().<Optional<Object>>compose(Optional::get).apply(Optional.of(obj)));
    }

    @Test
    void memoizedTest() {
        final var context = new Object() {
            int count = 0;
        };

        Function<Object, Object> fun = Functions.memoized((Object obj) -> {
            context.count++;
            return obj;
        });

        assertThrows(NullPointerException.class, () -> Functions.memoized(null));

        assertSame(obj, fun.apply(obj));
        assertEquals(1, context.count);
        assertSame(obj, fun.apply(obj));
        assertEquals(1, context.count);

        assertNull(fun.apply(null));
        assertEquals(2, context.count);
        assertNull(fun.apply(null));
        assertEquals(2, context.count);
    }

    @Test
    void tupledTest() {
        assertThrows(NullPointerException.class, () -> Functions.tupled(null));

        assertSame(obj, Functions.tupled((Object a, Object b) -> a).apply(Tuple.of(obj, null)));
        assertNull(Functions.tupled((Object a, Object b) -> b).apply(Tuple.of(obj, null)));
    }

    @Test
    void untupledTest() {
        assertThrows(NullPointerException.class, () -> Functions.untupled(null));

        assertSame(obj, Functions.untupled(Tuple2::component1).apply(obj, null));
        assertNull(Functions.untupled(Tuple2::component2).apply(obj, null));
    }

    @Test
    void curriedTest() {
        assertThrows(NullPointerException.class, () -> Functions.curried(null));

        assertSame(obj, Functions.curried((Object a, Object b) -> a).apply(obj).apply(null));
        assertNull(Functions.curried((Object a, Object b) -> b).apply(obj).apply(null));
    }

    @Test
    void uncurriedTest() {
        assertThrows(NullPointerException.class, () -> Functions.uncurried(null));

        assertSame(obj, Functions.uncurried(a -> b -> a).apply(obj, null));
        assertNull(Functions.uncurried(a -> b -> b).apply(obj, null));
    }
}
