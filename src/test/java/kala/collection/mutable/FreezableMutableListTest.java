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
package kala.collection.mutable;

import kala.collection.immutable.ImmutableSeq;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public final class FreezableMutableListTest {
    @Test
    public void test() {
        FreezableMutableList<String> list = FreezableMutableList.create();
        ImmutableSeq<String> frozen = list.freeze();
        assertTrue(frozen.isEmpty());
        list.append("A");
        list.append("B");
        assertTrue(frozen.isEmpty());

        frozen = list.freeze();
        assertIterableEquals(List.of("A", "B"), frozen);
        list.append("C");
        assertIterableEquals(List.of("A", "B"), frozen);
    }
}
