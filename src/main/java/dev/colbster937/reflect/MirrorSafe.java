package dev.colbster937.reflect;

public final class MirrorSafe {
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

  public static final <T> T invokeMethod(final Object obj, final Class<?> clazz, final String name,
      final Object... params) {
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

  public static final Class<?> getClassFromString(final String clazz) {
    try {
      return Class.forName(clazz);
    } catch (final ClassNotFoundException ex) {
      return null;
    }
  }
}
