package kala.pprint;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;

@SuppressWarnings("Since15")
abstract
class RecordAccessor {
    static final RecordAccessor accessor;

    static {
        RecordAccessor a = null;

        try {
            Class.forName("java.lang.Record");
            a = new RecordAccessor.Default();
        } catch (Throwable ignored) {
        }

        if (a == null) a = new RecordAccessor.Fallback();

        accessor = a;
    }

    abstract boolean isRecord(Object obj);

    abstract ComponentAccessor[] getComponentAccessors(Object obj);

    private static final class Default extends RecordAccessor {
        @Override
        boolean isRecord(Object obj) {
            return obj instanceof Record;
        }

        @Override
        ComponentAccessor[] getComponentAccessors(Object obj) {
            RecordComponent[] components = obj.getClass().getRecordComponents();

            ComponentAccessor[] res = new ComponentAccessor[components.length];
            for (int i = 0; i < components.length; i++) {
                res[i] = new ComponentAccessor(components[i].getName(), components[i].getAccessor());
            }
            return res;
        }
    }

    private static final class Fallback extends RecordAccessor {
        @Override
        boolean isRecord(Object obj) {
            return false;
        }

        @Override
        ComponentAccessor[] getComponentAccessors(Object obj) {
            throw new UnsupportedOperationException();
        }
    }

    static final class ComponentAccessor {
        private final String name;
        private final Method accessor;

        ComponentAccessor(String name, Method accessor) {
            this.name = name;
            this.accessor = accessor;
        }

        public String getName() {
            return name;
        }

        public Object getComponent(Object value) {
            try {
                return accessor.invoke(value);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new AssertionError(e);
            }
        }
    }
}
