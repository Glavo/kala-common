package kala.gson.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import kala.collection.Seq;
import kala.gson.collection.CollectionAdapter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CollectionAdapterTest {
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(CollectionAdapter.factory())
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
    }
}
