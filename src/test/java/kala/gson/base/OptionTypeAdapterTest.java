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
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import kala.control.AnyOption;
import kala.control.Option;
import kala.control.primitive.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public final class OptionTypeAdapterTest {
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(OptionTypeAdapter.factory())
            .registerTypeAdapterFactory(PrimitiveOptionTypeAdapter.factory())
            .create();

    @Test
    public void serializeTest() {
        JsonElement tree;

        tree = gson.toJsonTree(Option.some("value"));
        assertEquals(1, tree.getAsJsonArray().size());
        assertEquals("value", tree.getAsJsonArray().get(0).getAsString());

        List<Integer> value = List.of(0, 1, 2, 3);
        tree = gson.toJsonTree(Option.some(value));
        assertEquals(1, tree.getAsJsonArray().size());
        assertEquals(gson.toJsonTree(value), tree.getAsJsonArray().get(0));

        tree = gson.toJsonTree(Option.none());
        assertEquals(0, tree.getAsJsonArray().size());

        // BooleanOption
        tree = gson.toJsonTree(BooleanOption.some(true));
        assertEquals(1, tree.getAsJsonArray().size());
        assertTrue(tree.getAsJsonArray().get(0).getAsBoolean());

        tree = gson.toJsonTree(BooleanOption.none());
        assertEquals(0, tree.getAsJsonArray().size());

        // ByteOption
        tree = gson.toJsonTree(ByteOption.some((byte) 10));
        assertEquals(1, tree.getAsJsonArray().size());
        assertEquals(10, tree.getAsJsonArray().get(0).getAsInt());

        tree = gson.toJsonTree(ByteOption.none());
        assertEquals(0, tree.getAsJsonArray().size());

        // ShortOption
        tree = gson.toJsonTree(ShortOption.some((short) 10));
        assertEquals(1, tree.getAsJsonArray().size());
        assertEquals(10, tree.getAsJsonArray().get(0).getAsInt());

        tree = gson.toJsonTree(ShortOption.none());
        assertEquals(0, tree.getAsJsonArray().size());

        // IntOption
        tree = gson.toJsonTree(IntOption.some(10));
        assertEquals(1, tree.getAsJsonArray().size());
        assertEquals(10, tree.getAsJsonArray().get(0).getAsInt());

        tree = gson.toJsonTree(IntOption.none());
        assertEquals(0, tree.getAsJsonArray().size());

        // LongOption
        tree = gson.toJsonTree(LongOption.some(10));
        assertEquals(1, tree.getAsJsonArray().size());
        assertEquals(10, tree.getAsJsonArray().get(0).getAsInt());

        tree = gson.toJsonTree(LongOption.none());
        assertEquals(0, tree.getAsJsonArray().size());

        // FloatOption
        tree = gson.toJsonTree(FloatOption.some(10));
        assertEquals(1, tree.getAsJsonArray().size());
        assertEquals(10, tree.getAsJsonArray().get(0).getAsFloat());

        tree = gson.toJsonTree(FloatOption.none());
        assertEquals(0, tree.getAsJsonArray().size());

        // DoubleOption
        tree = gson.toJsonTree(DoubleOption.some(10));
        assertEquals(1, tree.getAsJsonArray().size());
        assertEquals(10, tree.getAsJsonArray().get(0).getAsDouble());

        tree = gson.toJsonTree(DoubleOption.none());
        assertEquals(0, tree.getAsJsonArray().size());

        // CharOption
        tree = gson.toJsonTree(CharOption.some('A'));
        assertEquals(1, tree.getAsJsonArray().size());
        assertEquals("A", tree.getAsJsonArray().get(0).getAsString());

        tree = gson.toJsonTree(CharOption.none());
        assertEquals(0, tree.getAsJsonArray().size());
    }

    @Test
    public void deserializeTest() {
        var type = new TypeToken<Option<List<Integer>>>() {};
        AnyOption<?> option;

        option = gson.fromJson("null", type);
        assertNull(option);

        option = gson.fromJson("[[0,1,2,3,4,5]]", type);
        assertEquals(Option.some(List.of(0, 1, 2, 3, 4, 5)), option);

        option = gson.fromJson("[]", type);
        assertEquals(Option.none(), option);

        // BooleanOption
        option = gson.fromJson("null", BooleanOption.class);
        assertNull(option);

        option = gson.fromJson("[true]", BooleanOption.class);
        assertEquals(BooleanOption.some(true), option);

        option = gson.fromJson("[]", BooleanOption.class);
        assertEquals(BooleanOption.none(), option);

        // ByteOption
        option = gson.fromJson("null", ByteOption.class);
        assertNull(option);

        option = gson.fromJson("[10]", ByteOption.class);
        assertEquals(ByteOption.some((byte) 10), option);

        option = gson.fromJson("[]", ByteOption.class);
        assertEquals(ByteOption.none(), option);

        // ShortOption
        option = gson.fromJson("null", ShortOption.class);
        assertNull(option);

        option = gson.fromJson("[10]", ShortOption.class);
        assertEquals(ShortOption.some((short) 10), option);

        option = gson.fromJson("[]", ShortOption.class);
        assertEquals(ShortOption.none(), option);

        // IntOption
        option = gson.fromJson("null", IntOption.class);
        assertNull(option);

        option = gson.fromJson("[10]", IntOption.class);
        assertEquals(IntOption.some(10), option);

        option = gson.fromJson("[]", IntOption.class);
        assertEquals(IntOption.none(), option);

        // LongOption
        option = gson.fromJson("null", LongOption.class);
        assertNull(option);

        option = gson.fromJson("[10]", LongOption.class);
        assertEquals(LongOption.some(10), option);

        option = gson.fromJson("[]", LongOption.class);
        assertEquals(LongOption.none(), option);

        // FloatOption
        option = gson.fromJson("null", FloatOption.class);
        assertNull(option);

        option = gson.fromJson("[10]", FloatOption.class);
        assertEquals(FloatOption.some(10), option);

        option = gson.fromJson("[]", FloatOption.class);
        assertEquals(FloatOption.none(), option);

        // DoubleOption
        option = gson.fromJson("null", DoubleOption.class);
        assertNull(option);

        option = gson.fromJson("[10]", DoubleOption.class);
        assertEquals(DoubleOption.some(10), option);

        option = gson.fromJson("[]", DoubleOption.class);
        assertEquals(DoubleOption.none(), option);

        // CharOption
        option = gson.fromJson("null", CharOption.class);
        assertNull(option);

        option = gson.fromJson("[\"A\"]", CharOption.class);
        assertEquals(CharOption.some('A'), option);

        option = gson.fromJson("[]", CharOption.class);
        assertEquals(CharOption.none(), option);
    }
}
