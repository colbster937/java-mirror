package dev.colbster937.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@SuppressWarnings({ "unchecked" })
public final class Mirror {
  public static final Field getField(final Class<?> clazz, final String name) throws ReflectiveOperationException {
    try {
      final Field field = clazz.getDeclaredField(name);
      field.setAccessible(true);
      return field;
    } catch (final ReflectiveOperationException ex) {
      final Class<?> sup = clazz.getSuperclass();
      if (sup != null) {
        return getField(sup, name);
      } else {
        throw ex;
      }
    }
  }

  public static final Field getField(final Object obj, final String name) throws ReflectiveOperationException {
    return getField(obj.getClass(), name);
  }

  public static final <T> T getFieldValue(final Object obj, final Class<?> clazz, final String name)
      throws ReflectiveOperationException {
    return (T) getField(clazz, name).get(obj);
  }

  public static final <T> T getFieldValue(final Object obj, final String name) throws ReflectiveOperationException {
    return getFieldValue(obj, obj.getClass(), name);
  }

  public static final Method getMethod(final Class<?> clazz, final String name, final Class<?>... params)
      throws ReflectiveOperationException {
    try {
      final Method method = clazz.getDeclaredMethod(name, params);
      method.setAccessible(true);
      return method;
    } catch (final ReflectiveOperationException ex) {
      final Class<?> sup = clazz.getSuperclass();
      if (sup != null) {
        return getMethod(sup, name, params);
      } else {
        throw ex;
      }
    }
  }

  public static final Method getMethod(final Object obj, final String name, final Class<?>... params)
      throws ReflectiveOperationException {
    return getMethod(obj.getClass(), name, params);
  }

  public static final <T> T invokeMethod(final Object obj, final Class<?> clazz, final String name,
      final Object... params) throws ReflectiveOperationException {
    return (T) getMethod(clazz, name, getTypes(params)).invoke(obj, params);
  }

  public static final <T> T invokeMethod(final Object obj, final String name, final Object... params)
      throws ReflectiveOperationException {
    return invokeMethod(obj, obj.getClass(), name, params);
  }

  public static final Constructor<?> getConstructor(final Class<?> clazz, final Class<?>... params)
      throws ReflectiveOperationException {
    final Constructor<?> constructor = clazz.getDeclaredConstructor(params);
    constructor.setAccessible(true);
    return constructor;
  }

  public static final <T> T invokeConstructor(final Class<?> clazz, final Object... params)
      throws ReflectiveOperationException {
    return (T) getConstructor(clazz, getTypes(params)).newInstance(params);
  }

  public static final boolean hasField(final Class<?> clazz, final String name) {
    try {
      clazz.getDeclaredField(name);
      return true;
    } catch (final NoSuchFieldException ex) {
      final Class<?> sup = clazz.getSuperclass();
      if (sup != null) {
        return hasField(sup, name);
      } else {
        return false;
      }
    }
  }

  public static final boolean hasMethod(final Class<?> clazz, final String name, final Class<?>... params) {
    try {
      clazz.getDeclaredMethod(name, params);
      return true;
    } catch (final NoSuchMethodException ex) {
      final Class<?> sup = clazz.getSuperclass();
      if (sup != null) {
        return hasMethod(sup, name, params);
      } else {
        return false;
      }
    }
  }

  public static final boolean hasConstructor(final Class<?> clazz, final Class<?>... params) {
    try {
      clazz.getDeclaredConstructor(params);
      return true;
    } catch (final NoSuchMethodException ex) {
      return false;
    }
  }

  private static final Class<?>[] getTypes(final Object... params) {
    final Class<?>[] types = new Class[params.length];
    for (int i = 0; i < params.length; i++) {
      if (params[i] != null) {
        types[i] = params[i].getClass();
      } else {
        types[i] = Object.class;
      }
    }
    return types;
  }
}
