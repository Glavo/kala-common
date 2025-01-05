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
import kala.control.Either;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class EitherTypeAdapterTest {
    private final Gson gson = new GsonBuilder().registerTypeAdapterFactory(EitherTypeAdapter.factory()).create();

    @Test
    public void serializeTest() {
        JsonElement tree;

        tree = gson.toJsonTree(Either.left("leftValue"));
        assertEquals(1, tree.getAsJsonObject().size());
        assertEquals("leftValue", tree.getAsJsonObject().get("left").getAsString());

        List<Integer> value = List.of(0, 1, 2, 3);
        tree = gson.toJsonTree(Either.left(value));
        assertEquals(1, tree.getAsJsonObject().size());
        assertEquals(gson.toJsonTree(value), tree.getAsJsonObject().get("left"));

        tree = gson.toJsonTree(Either.right("rightValue"));
        assertEquals(1, tree.getAsJsonObject().size());
        assertEquals("rightValue", tree.getAsJsonObject().get("right").getAsString());
    }

    @Test
    public void deserializeTest() {
        var type = new TypeToken<Either<String, List<Integer>>>() {};
        Either<String, List<Integer>> either;

        either = gson.fromJson("null", type);
        assertNull(either);

        either = gson.fromJson("{\"left\": \"leftValue\"}", type);
        assertEquals(Either.left("leftValue"), either);

        either = gson.fromJson("{\"right\": [0, 1, 2, 3, 4, 5]}", type);
        assertEquals(Either.right(List.of(0, 1, 2, 3, 4, 5)), either);
    }
}
