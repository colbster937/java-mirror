package dev.colbster937.reflect.cache.keys;

import java.util.Arrays;

public final class ConstructorKey {
  private final Class<?>[] params;

  public ConstructorKey(Class<?>[] params) {
    this.params = params.clone();
  }

  @Override
  public boolean equals(Object obj) {
    if (this != obj) {
      if (obj instanceof ConstructorKey) {
        final ConstructorKey key = (ConstructorKey) obj;
        return Arrays.equals(params, key.params);
      } else {
        return false;
      }
    } else {
      return true;
    }
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(params);
  }
}
