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

public class EitherAdapterTest {
    private final Gson gson = new GsonBuilder().registerTypeAdapterFactory(EitherAdapter.factory()).create();

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
