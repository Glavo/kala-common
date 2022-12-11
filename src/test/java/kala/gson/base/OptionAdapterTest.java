package kala.gson.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import kala.control.Option;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class OptionAdapterTest {
    private final Gson gson = new GsonBuilder().registerTypeAdapterFactory(OptionAdapter.factory()).create();

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
    }

    @Test
    public void deserializeTest() {
        var type = new TypeToken<Option<List<Integer>>>() {};
        Option<List<Integer>> option;

        option = gson.fromJson("null", type);
        assertNull(option);

        option = gson.fromJson("[[0,1,2,3,4,5]]", type);
        assertEquals(Option.some(List.of(0, 1, 2, 3, 4, 5)), option);

        option = gson.fromJson("[]", type);
        assertEquals(Option.none(), option);
    }
}
