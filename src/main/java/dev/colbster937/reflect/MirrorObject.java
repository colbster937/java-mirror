package dev.colbster937.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class MirrorObject<O> {
  private final O obj;

  private MirrorObject(O obj) {
    this.obj = obj;
  }

  public Field getField(String name) {
    return MirrorSafe.getField(this.obj, name);
  }

  public <T> T getFieldValue(String name) {
    return MirrorSafe.getFieldValue(this.obj, name);
  }

  public boolean hasField(String name) {
    return Mirror.hasField(this.obj, name);
  }

  public void setFieldValue(String name, Object value) {
    MirrorSafe.setFieldValue(this.obj, name, value);
  }

  public Method getMethod(String name, Object... params) {
    return MirrorSafe.getMethod(this.obj, name, Mirror.getTypes(params));
  }

  public <T> T invokeMethod(String name, Object... params) {
    return MirrorSafe.invokeMethod(this.obj, name, params);
  }

  public boolean hasMethod(String name, Class<?>... params) {
    return Mirror.hasMethod(this.obj, name, params);
  }

  public O getObject() {
    return this.obj;
  }

  @Override
  public String toString() {
    return this.obj.toString();
  }

  public MirrorObject<?> create(Object obj) {
    return new MirrorObject<>(obj);
  }
}
