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
import kala.control.Result;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ResultTypeAdapterTest {
    private final Gson gson = new GsonBuilder().registerTypeAdapterFactory(ResultTypeAdapter.factory()).create();

    @Test
    public void serializeTest() {
        JsonElement tree;

        tree = gson.toJsonTree(Result.ok("ok"));
        assertEquals(1, tree.getAsJsonObject().size());
        assertEquals("ok", tree.getAsJsonObject().get("value").getAsString());

        List<Integer> value = List.of(0, 1, 2, 3);
        tree = gson.toJsonTree(Result.ok(value));
        assertEquals(1, tree.getAsJsonObject().size());
        assertEquals(gson.toJsonTree(value), tree.getAsJsonObject().get("value"));

        tree = gson.toJsonTree(Result.err("err"));
        assertEquals(1, tree.getAsJsonObject().size());
        assertEquals("err", tree.getAsJsonObject().get("err").getAsString());
    }

    @Test
    public void deserializeTest() {
        var type = new TypeToken<Result<String, List<Integer>>>() {};
        Result<String, List<Integer>> result;

        result = gson.fromJson("null", type);
        assertNull(result);

        result = gson.fromJson("{\"value\": \"ok\"}", type);
        assertEquals(Result.ok("ok"), result);

        result = gson.fromJson("{\"err\": [0, 1, 2, 3, 4, 5]}", type);
        assertEquals(Result.err(List.of(0, 1, 2, 3, 4, 5)), result);
    }
}
