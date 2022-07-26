package kala.pprint;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

abstract class RecordAccessor {
    static final RecordAccessor accessor;

    static {
        RecordAccessor a = null;

        try {
            Class<?> cls = Class.forName("java.lang.Record");
            Class<?> rcCls = Class.forName("java.lang.reflect.RecordComponent");

            a = new RecordAccessor.Default(
                    cls,
                    Class.class.getMethod("getRecordComponents"),
                    rcCls.getMethod("getName"),
                    rcCls.getMethod("getAccessor")
            );
        } catch (Throwable ignored) {
        }

        if (a == null) a = new RecordAccessor.Fallback();

        accessor = a;
    }

    abstract boolean isRecord(Object obj);

    abstract ComponentAccessor[] getComponentAccessors(Object obj);

    private static final class Default extends RecordAccessor {
        private final Class<?> recordCls;
        private final Method classGetRecordComponentsMethod;
        private final Method recordComponentGetNameMethod;
        private final Method recordComponentGetAccessorMethod;

        private Default(Class<?> recordCls, Method classGetRecordComponentsMethod, Method recordComponentGetNameMethod, Method recordComponentGetAccessorMethod) {
            this.recordCls = recordCls;
            this.classGetRecordComponentsMethod = classGetRecordComponentsMethod;
            this.recordComponentGetNameMethod = recordComponentGetNameMethod;
            this.recordComponentGetAccessorMethod = recordComponentGetAccessorMethod;
        }

        @Override
        boolean isRecord(Object obj) {
            return recordCls.isInstance(obj);
        }

        @Override
        ComponentAccessor[] getComponentAccessors(Object obj) {
            try {
                Object[] components = (Object[]) classGetRecordComponentsMethod.invoke(obj.getClass());
                ComponentAccessor[] res = new ComponentAccessor[components.length];
                for (int i = 0; i < components.length; i++) {
                    Object c = components[i];
                    res[i] = new ComponentAccessor(
                            (String) recordComponentGetNameMethod.invoke(c),
                            (Method) recordComponentGetAccessorMethod.invoke(c)
                    );
                }
                return res;
            } catch (Throwable e) {
                throw new AssertionError(e);
            }

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
