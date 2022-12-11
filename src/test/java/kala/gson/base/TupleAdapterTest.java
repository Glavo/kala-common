package kala.gson.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import kala.tuple.*;
import kala.tuple.primitive.BooleanTuple2;
import kala.tuple.primitive.IntObjTuple2;
import kala.tuple.primitive.IntTuple3;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public final class TupleAdapterTest {
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(TupleAdapter.factory())
            .registerTypeAdapterFactory(PrimitiveTupleAdapter.factory())
            .create();

    @Test
    public void serializeTest() {
        assertEquals("[]", gson.toJson(Tuple.empty()));
        assertEquals("[1]", gson.toJson(Tuple.of(1)));
        assertEquals("[1,2]", gson.toJson(Tuple.of(1, 2)));
        assertEquals("[1,2,3]", gson.toJson(Tuple.of(1, 2, 3)));
        assertEquals("[1,2,3,4]", gson.toJson(Tuple.of(1, 2, 3, 4)));
        assertEquals("[1,2,3,4,5]", gson.toJson(Tuple.of(1, 2, 3, 4, 5)));
        assertEquals("[1,2,3,4,5,6]", gson.toJson(Tuple.of(1, 2, 3, 4, 5, 6)));
        assertEquals("[1,2,3,4,5,6,7]", gson.toJson(Tuple.of(1, 2, 3, 4, 5, 6, 7)));
        assertEquals("[1,2,3,4,5,6,7,8]", gson.toJson(Tuple.of(1, 2, 3, 4, 5, 6, 7, 8)));
        assertEquals("[1,2,3,4,5,6,7,8,9]", gson.toJson(Tuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9)));
        //TODO: assertEquals("[1,2,3,4,5,6,7,8,9,10]", gson.toJson(Tuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)));

        assertEquals("[true,true]", gson.toJson(BooleanTuple2.of(true, true)));
        assertEquals("[10,[0,1,2,3]]", gson.toJson(IntObjTuple2.of(10, List.of(0, 1, 2, 3))));
        assertEquals("[10,20,30]", gson.toJson(IntTuple3.of(10, 20, 30)));
    }

    @Test
    public void deserializeTest() {
        assertEquals(Tuple.empty(), gson.fromJson("[]", new TypeToken<EmptyTuple>() {}));
        assertEquals(Tuple.empty(), gson.fromJson("[]", new TypeToken<Unit>() {}));
        assertEquals(Tuple.of(1), gson.fromJson("[1]", new TypeToken<Tuple1<Integer>>() {}));
        assertEquals(Tuple.of(1, 2), gson.fromJson("[1,2]", new TypeToken<Tuple2<Integer, Integer>>() {}));
        assertEquals(Tuple.of(1, 2, 3), gson.fromJson("[1,2,3]", new TypeToken<Tuple3<Integer, Integer, Integer>>() {}));
        assertEquals(Tuple.of(1, 2, 3, 4), gson.fromJson("[1,2,3,4]", new TypeToken<Tuple4<Integer, Integer, Integer, Integer>>() {}));
        assertEquals(Tuple.of(1, 2, 3, 4, 5), gson.fromJson("[1,2,3,4,5]", new TypeToken<Tuple5<Integer, Integer, Integer, Integer, Integer>>() {}));
        assertEquals(Tuple.of(1, 2, 3, 4, 5, 6), gson.fromJson("[1,2,3,4,5,6]", new TypeToken<Tuple6<Integer, Integer, Integer, Integer, Integer, Integer>>() {}));
        assertEquals(Tuple.of(1, 2, 3, 4, 5, 6, 7), gson.fromJson("[1,2,3,4,5,6,7]", new TypeToken<Tuple7<Integer, Integer, Integer, Integer, Integer, Integer, Integer>>() {}));
        assertEquals(Tuple.of(1, 2, 3, 4, 5, 6, 7, 8), gson.fromJson("[1,2,3,4,5,6,7,8]", new TypeToken<Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>>() {}));
        assertEquals(Tuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9), gson.fromJson("[1,2,3,4,5,6,7,8,9]", new TypeToken<Tuple9<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>>() {}));
        assertEquals(Tuple.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0), gson.fromJson("[1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0]", new TypeToken<Tuple>() {}));

        assertEquals(BooleanTuple2.of(true, true), gson.fromJson("[true,true]", BooleanTuple2.class));
        assertEquals(IntObjTuple2.of(10, List.of(0, 1, 2, 3)), gson.fromJson("[10,[0, 1, 2, 3]]", new TypeToken<IntObjTuple2<List<Integer>>>() {}));
        assertEquals(IntTuple3.of(10, 20, 30), gson.fromJson("[10,20,30]", IntTuple3.class));
    }
}
