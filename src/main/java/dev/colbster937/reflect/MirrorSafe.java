package dev.colbster937.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class MirrorSafe {
  public static final Field getField(final Class<?> clazz, final String name) {
    try {
      return Mirror.getField(clazz, name);
    } catch (final ReflectiveOperationException ex) {
      return null;
    }
  }

  public static final Field getField(final Object obj, final String name) {
    return getField(obj.getClass(), name);
  }

  public static final <T> T getFieldValue(final Object obj, final Class<?> clazz, final String name) {
    try {
      return Mirror.getFieldValue(obj, clazz, name);
    } catch (final ReflectiveOperationException ex) {
      return null;
    }
  }

  public static final <T> T getFieldValue(final Object obj, final String name) {
    return getFieldValue(obj, obj.getClass(), name);
  }

  public static final <T> T getFieldValue(final Class<?> clazz, final String name) {
    return getFieldValue(null, clazz, name);
  }

  public static final void setFieldValue(final Object obj, final Class<?> clazz, final String name, final Object value) {
    try {
      Mirror.setFieldValue(obj, clazz, name, value);
    } catch (final ReflectiveOperationException ex) {}
  }

  public static final void setFieldValue(final Object obj, final String name, final Object value) {
    setFieldValue(obj, obj.getClass(), name, value);
  }

  public static final void setFieldValue(final Class<?> clazz, final String name, final Object value) {
    setFieldValue(null, clazz, name, value);
  }

  public static final Method getMethod(final Class<?> clazz, final String name) {
    try {
      return Mirror.getMethod(clazz, name);
    } catch (final ReflectiveOperationException ex) {
      return null;
    }
  }

  public static final Method getMethod(final Object obj, final String name) {
    return getMethod(obj.getClass(), name);
  }

  public static final <T> T invokeMethod(final Object obj, final Class<?> clazz, final String name, final Object... params) {
    try {
      return Mirror.invokeMethod(obj, clazz, name, params);
    } catch (final ReflectiveOperationException ex) {
      return null;
    }
  }

  public static final <T> T invokeMethod(final Object obj, final String name, final Object... params) {
    return invokeMethod(obj, obj.getClass(), name, params);
  }

  public static final <T> T invokeMethod(final Class<?> clazz, final String name, final Object... params) {
    return invokeMethod(null, clazz, name, params);
  }

  public static final <T> T invokeConstructor(final Class<?> clazz, final Object... params) {
    try {
      return Mirror.invokeConstructor(clazz, params);
    } catch (final ReflectiveOperationException ex) {
      return null;
    }
  }

  public static final Class<?> getClass(final String clazz) {
    try {
      return Class.forName(clazz);
    } catch (final ClassNotFoundException ex) {
      return null;
    }
  }
}
