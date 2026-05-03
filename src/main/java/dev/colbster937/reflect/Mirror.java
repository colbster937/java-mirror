package dev.colbster937.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({ "unchecked", "FinalPrivateMethod" })
public final class Mirror {
  private static final Map<Class<?>, Class<?>> PRIMITIVES;

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

  public static final <T> T getFieldValue(final Class<?> clazz, final Object obj, final String name) throws ReflectiveOperationException {
    return (T) getField(clazz, name).get(obj);
  }

  public static final <T> T getFieldValue(final Object obj, final String name) throws ReflectiveOperationException {
    return getFieldValue(obj.getClass(), obj, name);
  }

  public static final <T> T getFieldValue(final Class<?> clazz, final String name) throws ReflectiveOperationException {
    return getFieldValue(clazz, null, name);
  }

  public static final void setFieldValue(final Class<?> clazz, final Object obj, final String name, final Object value) throws ReflectiveOperationException {
    getField(clazz, name).set(obj, value);
  }

  public static final void setFieldValue(final Object obj, final String name, final Object value) throws ReflectiveOperationException {
    setFieldValue(obj.getClass(), obj, name, value);
  }

  public static final void setFieldValue(final Class<?> clazz, final String name, final Object value) throws ReflectiveOperationException {
    setFieldValue(clazz, null, name, value);
  }

  public static final Method getMethod(final Class<?> clazz, final String name, final Class<?>... params) throws ReflectiveOperationException {
    try {
      final Method method = clazz.getDeclaredMethod(name, params);
      method.setAccessible(true);
      return method;
    } catch (final ReflectiveOperationException ex) {
      for (final Method method : clazz.getDeclaredMethods()) {
        if (method.getName().equals(name) && isAssignableParam(method.getParameterTypes(), params)) {
          method.setAccessible(true);
          return method;
        }
      }

      final Class<?> sup = clazz.getSuperclass();
      if (sup != null) {
        return getMethod(sup, name, params);
      } else {
        throw ex;
      }
    }
  }

  public static final Method getMethod(final Object obj, final String name, final Class<?>... params) throws ReflectiveOperationException {
    return getMethod(obj.getClass(), name, params);
  }

  public static final <T> T invokeMethod(final Class<?> clazz, final Object obj, final String name, final Object... params) throws ReflectiveOperationException {
    return (T) getMethod(clazz, name, getTypes(params)).invoke(obj, params);
  }

  public static final <T> T invokeMethod(final Object obj, final String name, final Object... params) throws ReflectiveOperationException {
    return invokeMethod(obj.getClass(), obj, name, params);
  }

  public static final <T> T invokeMethod(final Class<?> clazz, final String name, final Object... params) throws ReflectiveOperationException {
    return invokeMethod(clazz, null, name, params);
  }

  public static final Constructor<?> getConstructor(final Class<?> clazz, final Class<?>... params) throws ReflectiveOperationException {
    try {
      final Constructor<?> constructor = clazz.getDeclaredConstructor(params);
      constructor.setAccessible(true);
      return constructor;
    } catch (final ReflectiveOperationException ex) {
      for (final Constructor<?> constructor : clazz.getDeclaredConstructors()) {
        if (isAssignableParam(constructor.getParameterTypes(), params)) {
          constructor.setAccessible(true);
          return constructor;
        }
      }

      throw ex;
    }
  }

  public static final <T> T invokeConstructor(final Class<?> clazz, final Object... params) throws ReflectiveOperationException {
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

  public static final Class<?> wrapPrimitiveValue(final Class<?> clazz) {
    return PRIMITIVES.getOrDefault(clazz, clazz);
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

  private static final boolean isAssignableParam(final Class<?>[] a, final Class<?>[] b) {
    boolean ret = true;

    if (a.length == b.length) {
      for (int i = 0; i < a.length; i++) {
        if (b[i] == Object.class) {
          if (a[i].isPrimitive()) {
            ret = false;
          } else {
            continue;
          }
        } else if (!wrapPrimitiveValue(a[i]).isAssignableFrom(wrapPrimitiveValue(b[i]))) {
          ret = false;
        }

        if (!ret) {
          break;
        }
      }
    } else {
      ret = false;
    }

    return ret;
  }

  static {
    final Map<Class<?>, Class<?>> map = new HashMap<>();

    map.put(int.class, Integer.class);
    map.put(boolean.class, Boolean.class);
    map.put(double.class, Double.class);
    map.put(float.class, Float.class);
    map.put(long.class, Long.class);
    map.put(short.class, Short.class);
    map.put(byte.class, Byte.class);
    map.put(char.class, Character.class);
    map.put(void.class, Void.class);

    PRIMITIVES = Collections.unmodifiableMap(map);
  }
}
