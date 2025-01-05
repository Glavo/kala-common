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
package kala.gson.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import kala.collection.Seq;
import kala.collection.Set;
import kala.collection.SortedSet;
import kala.collection.mutable.MutableList;
import kala.collection.mutable.MutableSeq;
import kala.gson.collection.CollectionTypeAdapter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CollectionTypeAdapterTest {
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(CollectionTypeAdapter.factory())
            .create();

    @Test
    public void serializeTest() {
        assertEquals("[]", gson.toJson(Seq.empty()));
        assertEquals("[0]", gson.toJson(Seq.of(0)));
        assertEquals("[[]]", gson.toJson(Seq.of(Seq.empty())));
    }

    @Test
    public void deserializeTest() {
        TypeToken<?> type = TypeToken.getParameterized(Seq.class, Integer.class);
        assertEquals(Seq.empty(), gson.fromJson("[]", type));
        assertEquals(Seq.of(0), gson.fromJson("[0]", type));

        assertInstanceOf(MutableSeq.class, gson.fromJson("[]", MutableSeq.class));
        assertInstanceOf(MutableList.class, gson.fromJson("[]", MutableList.class));
        assertInstanceOf(Set.class, gson.fromJson("[]", Set.class));
        assertInstanceOf(SortedSet.class, gson.fromJson("[]", SortedSet.class));
    }
}
