package kala.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kala.gson.base.*;
import kala.gson.collection.CollectionTypeAdapter;

public final class KalaGson {
    private KalaGson() {
    }

    public static GsonBuilder registerKalaTypeAdapters(GsonBuilder builder) {
        return registerKalaBaseTypeAdapters(registerKalaCollectionTypeAdapters(builder));
    }

    public static GsonBuilder registerKalaBaseTypeAdapters(GsonBuilder builder) {
        return builder
                .registerTypeAdapterFactory(EitherTypeAdapter.factory())
                .registerTypeAdapterFactory(OptionTypeAdapter.factory())
                .registerTypeAdapterFactory(PrimitiveOptionTypeAdapter.factory())
                .registerTypeAdapterFactory(TupleTypeAdapter.factory())
                .registerTypeAdapterFactory(PrimitiveTupleTypeAdapter.factory());
    }

    public static GsonBuilder registerKalaCollectionTypeAdapters(GsonBuilder builder) {
        return builder.registerTypeAdapterFactory(CollectionTypeAdapter.factory());
    }

    public static Gson createKalaGson() {
        return registerKalaTypeAdapters(new GsonBuilder()).create();
    }
}
