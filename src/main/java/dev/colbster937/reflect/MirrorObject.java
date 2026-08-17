package dev.colbster937.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class MirrorObject<O> {
  private final O obj;

  private MirrorObject(final O obj) {
    this.obj = obj;
  }

  public Field getField(final String name) {
    return MirrorSafe.getField(this.obj, name);
  }

  public <T> T getFieldValue(final String name) {
    return MirrorSafe.getFieldValue(this.obj, name);
  }

  public boolean hasField(final String name) {
    return Mirror.hasField(this.obj, name);
  }

  public void setFieldValue(final String name, final Object value) {
    MirrorSafe.setFieldValue(this.obj, name, value);
  }

  public Method getMethod(final String name, final Object ...params) {
    return MirrorSafe.getMethod(this.obj, name, Mirror.getTypes(params));
  }

  public <T> T invokeMethod(final String name, final Object ...params) {
    return MirrorSafe.invokeMethod(this.obj, name, params);
  }

  public boolean hasMethod(final String name, final Class<?> ...params) {
    return Mirror.hasMethod(this.obj, name, params);
  }

  public O getObject() {
    return this.obj;
  }

  @Override
  public String toString() {
    return this.obj.toString();
  }

  public MirrorObject<?> create(final Object obj) {
    return new MirrorObject<>(obj);
  }
}
