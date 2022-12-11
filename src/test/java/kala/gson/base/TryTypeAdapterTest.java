package kala.gson.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import kala.control.Try;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TryTypeAdapterTest {
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
