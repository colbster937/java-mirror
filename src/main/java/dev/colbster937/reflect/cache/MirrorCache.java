package dev.colbster937.reflect.cache;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import dev.colbster937.reflect.cache.keys.ConstructorKey;
import dev.colbster937.reflect.cache.keys.MethodKey;

public final class MirrorCache {
  private static final ClassValue<MirrorCache> CACHE = new ClassValue<MirrorCache>() {
    @Override
    protected MirrorCache computeValue(Class<?> clazz) {
      return new MirrorCache();
    }
  };

  private final Map<String, Field> fields = new ConcurrentHashMap<>();
  private final Map<MethodKey, Method> methods = new ConcurrentHashMap<>();
  private final Map<ConstructorKey, Constructor<?>> constructors = new ConcurrentHashMap<>();

  public static MirrorCache get(Class<?> clazz) {
    return CACHE.get(clazz);
  }

  public static void remove(Class<?> clazz) {
    CACHE.remove(clazz);
  }

  public Map<String, Field> getFields() {
    return this.fields;
  }

  public Map<MethodKey, Method> getMethods() {
    return this.methods;
  }

  public Map<ConstructorKey, Constructor<?>> getConstructors() {
    return this.constructors;
  }
}
