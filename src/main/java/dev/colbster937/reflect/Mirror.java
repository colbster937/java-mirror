package dev.colbster937.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import dev.colbster937.reflect.cache.MirrorCache;
import dev.colbster937.reflect.cache.keys.ConstructorKey;
import dev.colbster937.reflect.cache.keys.MethodKey;

@SuppressWarnings({ "unchecked", "FinalPrivateMethod" })
public final class Mirror {
  private static final Map<Class<?>, Class<?>> PRIMITIVES;

  public static Field getField(Class<?> clazz, String name) throws ReflectiveOperationException {
    final MirrorCache cache = MirrorCache.get(clazz);
    final Map<String, Field> fields = cache.getFields();
    final Field cached = fields.get(name);

    if (cached == null) {
      Field field;

      try {
        field = clazz.getDeclaredField(name);
        field.setAccessible(true);
      } catch (ReflectiveOperationException ex) {
        final Class<?> sup = clazz.getSuperclass();
        if (sup != null) {
          field = getField(sup, name);
        } else {
          throw ex;
        }
      }

      final Field existing = fields.putIfAbsent(name, field);
      if (existing == null) {
        return field;
      } else {
        return existing;
      }
    } else {
      return cached;
    }
  }

  public static Field getField(Object obj, String name) throws ReflectiveOperationException {
    return getField(obj.getClass(), name);
  }

  public static <T> T getFieldValue(Class<?> clazz, Object obj, String name)
      throws ReflectiveOperationException {
    return (T) getField(clazz, name).get(obj);
  }

  public static <T> T getFieldValue(Object obj, String name) throws ReflectiveOperationException {
    return getFieldValue(obj.getClass(), obj, name);
  }

  public static <T> T getFieldValue(Class<?> clazz, String name) throws ReflectiveOperationException {
    return getFieldValue(clazz, null, name);
  }

  public static void setFieldValue(Class<?> clazz, Object obj, String name, Object value) throws ReflectiveOperationException {
    getField(clazz, name).set(obj, value);
  }

  public static void setFieldValue(Object obj, String name, Object value) throws ReflectiveOperationException {
    setFieldValue(obj.getClass(), obj, name, value);
  }

  public static void setFieldValue(Class<?> clazz, String name, Object value) throws ReflectiveOperationException {
    setFieldValue(clazz, null, name, value);
  }

  public static Method getMethod(Class<?> clazz, String name, Class<?>... params) throws ReflectiveOperationException {
    final MirrorCache cache = MirrorCache.get(clazz);
    final Map<MethodKey, Method> methods = cache.getMethods();
    final MethodKey key = new MethodKey(name, params);
    final Method cached = methods.get(key);

    if (cached == null) {
      Method method = null;

      try {
        method = clazz.getDeclaredMethod(name, params);
        method.setAccessible(true);
      } catch (ReflectiveOperationException ex) {
        for (Method m : clazz.getDeclaredMethods()) {
          if (m.getName().equals(name) && isAssignableParam(m.getParameterTypes(), params)) {
            m.setAccessible(true);
            method = m;
            break;
          }
        }

        if (method == null) {
          final Class<?> sup = clazz.getSuperclass();
          if (sup != null) {
            method = getMethod(sup, name, params);
          } else {
            throw ex;
          }
        }
      }

      final Method existing = methods.putIfAbsent(key, method);
      if (existing == null) {
        return method;
      } else {
        return existing;
      }
    } else {
      return cached;
    }
  }

  public static Method getMethod(Object obj, String name, Class<?>... params) throws ReflectiveOperationException {
    return getMethod(obj.getClass(), name, params);
  }

  public static <T> T invokeMethod(Class<?> clazz, Object obj, String name, final Object... params) throws ReflectiveOperationException {
    return (T) getMethod(clazz, name, getTypes(params)).invoke(obj, params);
  }

  public static <T> T invokeMethod(Object obj, String name, Object... params) throws ReflectiveOperationException {
    return invokeMethod(obj.getClass(), obj, name, params);
  }

  public static <T> T invokeMethod(Class<?> clazz, String name, Object... params) throws ReflectiveOperationException {
    return invokeMethod(clazz, null, name, params);
  }

  public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... params) throws ReflectiveOperationException {
    final MirrorCache cache = MirrorCache.get(clazz);
    final Map<ConstructorKey, Constructor<?>> constructors = cache.getConstructors();
    final ConstructorKey key = new ConstructorKey(params);
    final Constructor<?> cached = constructors.get(key);

    if (cached == null) {
      Constructor<?> constructor = null;

      try {
        constructor = clazz.getDeclaredConstructor(params);
        constructor.setAccessible(true);
      } catch (ReflectiveOperationException ex) {
        for (Constructor<?> c : clazz.getDeclaredConstructors()) {
          if (isAssignableParam(c.getParameterTypes(), params)) {
            c.setAccessible(true);
            constructor = c;
            break;
          }
        }

        if (constructor == null) {
          throw ex;
        }
      }

      final Constructor<?> existing = constructors.putIfAbsent(key, constructor);
      if (existing == null) {
        return constructor;
      } else {
        return existing;
      }
    } else {
      return cached;
    }
  }

  public static <T> T invokeConstructor(Class<?> clazz, Object... params) throws ReflectiveOperationException {
    return (T) getConstructor(clazz, getTypes(params)).newInstance(params);
  }

  public static boolean hasField(Class<?> clazz, String name) {
    try {
      getField(clazz, name);
      return true;
    } catch (ReflectiveOperationException ex) {
      return false;
    }
  }

  public static boolean hasField(Object obj, String name) {
    return hasField(obj.getClass(), name);
  }

  public static boolean hasMethod(Class<?> clazz, String name, Class<?>... params) {
    try {
      getMethod(clazz, name, params);
      return true;
    } catch (ReflectiveOperationException ex) {
      return false;
    }
  }

  public static boolean hasMethod(Object obj, String name, Class<?>... params) {
    return hasMethod(obj.getClass(), name, params);
  }

  public static boolean hasConstructor(Class<?> clazz, Class<?>... params) {
    try {
      getConstructor(clazz, params);
      return true;
    } catch (ReflectiveOperationException ex) {
      return false;
    }
  }

  public static Class<?> wrapPrimitiveValue(Class<?> clazz) {
    return PRIMITIVES.getOrDefault(clazz, clazz);
  }

  public static Class<?>[] getTypes(Object... params) {
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

  private static final boolean isAssignableParam(Class<?>[] a, Class<?>[] b) {
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
