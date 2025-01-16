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
import kala.control.Try;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class TryTypeAdapterTest {
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(TryTypeAdapter.factory())
            .registerTypeAdapterFactory(PrimitiveOptionTypeAdapter.factory())
            .create();

    // @Test
    public void serializeTest() {
        JsonElement tree;

        tree = gson.toJsonTree(Try.success("value"));
        assertEquals(gson.toJsonTree(Map.of("value", "value")), tree);

        List<Integer> value = List.of(0, 1, 2, 3);
        tree = gson.toJsonTree(Try.success((value)));
        assertEquals(gson.toJsonTree(Map.of("value", List.of(0, 1, 2, 3))), tree);
    }

    @Test
    public void deserializeTest() {

    }
}
