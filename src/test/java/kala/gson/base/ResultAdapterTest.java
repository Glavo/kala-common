package kala.gson.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import kala.control.Result;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ResultAdapterTest {
    private final Gson gson = new GsonBuilder().registerTypeAdapterFactory(ResultAdapter.factory()).create();

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
