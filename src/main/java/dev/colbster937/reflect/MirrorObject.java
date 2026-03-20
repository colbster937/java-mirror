package dev.colbster937.reflect;

public final class MirrorObject<O> {
  private final O obj;

  public MirrorObject(final O obj) {
    this.obj = obj;
  }

  public final <T> T getFieldValue(final String name) {
    return MirrorSafe.getFieldValue(this.obj, name);
  }

  public final <T> T invokeMethod(final String name, final Object... params) {
    return MirrorSafe.invokeMethod(this.obj, name, params);
  }

  public final O getObject() {
    return this.obj;
  }

  @Override
  public final String toString() {
    return this.obj.toString();
  }

  public static final MirrorObject<?> create(final Object obj) {
    return new MirrorObject<>(obj);
  }
}
