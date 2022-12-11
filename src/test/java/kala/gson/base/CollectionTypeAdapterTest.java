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
