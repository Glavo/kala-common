package kala.internal;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class RecordAccessor {

    private static final Class<?> recordClass;

    private static final MethodHandle getRecordComponentsHandle;
    private static final MethodHandle getRecordComponentNameHandle;
    private static final MethodHandle getRecordComponentAccessorHandle;

    static {
        Class<?> rc = null;
        try {
            rc = Class.forName("java.lang.Record");
        } catch (Throwable ignored) {
        }

        recordClass = rc;

        if (rc != null) {
            try {
                MethodHandles.Lookup lookup = MethodHandles.publicLookup();

                Class<?> rcc = Class.forName("java.lang.reflect.RecordComponent");

                getRecordComponentsHandle = lookup
                        .findVirtual(Class.class, "getRecordComponents", MethodType.methodType(Array.newInstance(rcc, 0).getClass()))
                        .asType(MethodType.methodType(Object[].class, Class.class));

                getRecordComponentNameHandle = lookup
                        .findVirtual(rcc, "getName", MethodType.methodType(String.class))
                        .asType(MethodType.methodType(String.class, Object.class));

                getRecordComponentAccessorHandle = lookup
                        .findVirtual(rcc, "getAccessor", MethodType.methodType(Method.class))
                        .asType(MethodType.methodType(Method.class, Object.class));


            } catch (Throwable e) {
                throw new InternalError(e);
            }
        } else {
            getRecordComponentsHandle = null;
            getRecordComponentNameHandle = null;
            getRecordComponentAccessorHandle = null;
        }
    }

    public static boolean isRecord(Object obj) {
        return recordClass != null && recordClass.isInstance(obj);
    }

    public static Object[] getRecordComponents(Class<?> cls) {
        if (getRecordComponentsHandle == null) {
            throw new UnsupportedOperationException();
        }

        try {
            return (Object[]) getRecordComponentsHandle.invokeExact(cls);
        } catch (Throwable e) {
            throw new InternalError(e);
        }
    }

    public static String getName(Object component) {
        if (getRecordComponentNameHandle == null) {
            throw new UnsupportedOperationException();
        }

        try {
            return (String) getRecordComponentNameHandle.invokeExact(component);
        } catch (Throwable e) {
            throw new InternalError(e);
        }
    }

    public static Method getComponentAccessor(Object component) {
        if (getRecordComponentAccessorHandle == null) {
            throw new UnsupportedOperationException();
        }

        try {
            return (Method) getRecordComponentAccessorHandle.invokeExact(component);
        } catch (Throwable e) {
            throw new InternalError(e);
        }
    }

    public static Object getComponent(Object record, Object component) {
        try {
            return getComponentAccessor(component).invoke(record);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new InternalError(e);
        }
    }
}
