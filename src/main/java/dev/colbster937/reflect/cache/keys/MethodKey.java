package dev.colbster937.reflect.cache.keys;

import java.util.Arrays;

public final class MethodKey {
  private final String name;
  private final Class<?>[] params;

  public MethodKey(String name, Class<?>[] params) {
    this.name = name;
    this.params = params.clone();
  }

  @Override
  public boolean equals(Object obj) {
    if (this != obj) {
      if (obj instanceof MethodKey) {
        final MethodKey key = (MethodKey) obj;
        return name.equals(key.name) && Arrays.equals(params, key.params);
      } else {
        return false;
      }
    } else {
      return true;
    }
  }

  @Override
  public int hashCode() {
    int ret = name.hashCode();
    ret = 31 * ret + Arrays.hashCode(params);
    return ret;
  }
}
