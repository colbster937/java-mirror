package dev.colbster937.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class MirrorSafe {
  public static Field getField(Class<?> clazz, String name) {
    try {
      return Mirror.getField(clazz, name);
    } catch (ReflectiveOperationException ex) {
      return null;
    }
  }

  public static Field getField(Object obj, String name) {
    return getField(obj.getClass(), name);
  }

  public static <T> T getFieldValue(Class<?> clazz, Object obj, String name) {
    try {
      return Mirror.getFieldValue(clazz, obj, name);
    } catch (ReflectiveOperationException ex) {
      return null;
    }
  }

  public static <T> T getFieldValue(Object obj, String name) {
    return getFieldValue(obj.getClass(), obj, name);
  }

  public static <T> T getFieldValue(Class<?> clazz, String name) {
    return getFieldValue(clazz, null, name);
  }

  public static void setFieldValue(Class<?> clazz, Object obj, String name,
      final Object value) {
    try {
      Mirror.setFieldValue(clazz, obj, name, value);
    } catch (ReflectiveOperationException ex) {
    }
  }

  public static void setFieldValue(Object obj, String name, Object value) {
    setFieldValue(obj.getClass(), obj, name, value);
  }

  public static void setFieldValue(Class<?> clazz, String name, Object value) {
    setFieldValue(clazz, null, name, value);
  }

  public static boolean getFieldExists(Class<?> clazz, String name) {
    return getField(clazz, name) != null;
  }

  public static boolean getFieldExists(Object obj, String name) {
    return getFieldExists(obj.getClass(), name);
  }

  public static Method getMethod(Class<?> clazz, String name, Class<?>... params) {
    try {
      return Mirror.getMethod(clazz, name, params);
    } catch (ReflectiveOperationException ex) {
      return null;
    }
  }

  public static Method getMethod(Object obj, String name, Class<?>... params) {
    return getMethod(obj.getClass(), name, params);
  }

  public static Method getMethod(Class<?> clazz, String name) {
    return getMethod(clazz, name, new Class<?>[0]);
  }

  public static Method getMethod(Object obj, String name) {
    return getMethod(obj.getClass(), name);
  }

  public static <T> T invokeMethod(Class<?> clazz, Object obj, String name,
      final Object... params) {
    try {
      return Mirror.invokeMethod(clazz, obj, name, params);
    } catch (ReflectiveOperationException ex) {
      return null;
    }
  }

  public static <T> T invokeMethod(Object obj, String name, Object... params) {
    return invokeMethod(obj.getClass(), obj, name, params);
  }

  public static <T> T invokeMethod(Class<?> clazz, String name, Object... params) {
    return invokeMethod(clazz, null, name, params);
  }

  public static <T> T invokeConstructor(Class<?> clazz, Object... params) {
    try {
      return Mirror.invokeConstructor(clazz, params);
    } catch (ReflectiveOperationException ex) {
      return null;
    }
  }

  public static Class<?> getClass(String clazz) {
    try {
      return Class.forName(clazz);
    } catch (ClassNotFoundException ex) {
      return null;
    }
  }

  public static boolean getClassExists(String clazz) {
    return getClass(clazz) != null;
  }
}
