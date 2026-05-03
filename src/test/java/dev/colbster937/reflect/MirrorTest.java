package dev.colbster937.reflect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public final class MirrorTest {
  private static class ClassA {
    private static int valS = 937;
    private int valI = 937;

    public final int valI() {
      return this.valI;
    }

    public ClassA() {
      this(0);
    }

    private ClassA(final int a) {
      this.valI += a;
    }

    private final int addI(final int a, final int b) {
      return addS(a, b);
    }

    private static final int addS(final int a, final int b) {
      return a + b;
    }
  }

  private static class ClassB extends ClassA {
  }

  @Test
  public final void getFieldValue() throws Exception {
    for (final ClassA obj : new ClassA[] { new ClassA(), new ClassB() }) {
      assertEquals(937, (int) Mirror.getFieldValue(obj, "valI"));
    }

    for (final Class<?> clazz : new Class<?>[] { ClassA.class, ClassB.class }) {
      assertEquals(937, (int) Mirror.getFieldValue(clazz, "valS"));
    }
  }

  @Test
  public final void setFieldValue() throws Exception {
    for (final ClassA obj : new ClassA[] { new ClassA(), new ClassB() }) {
      Mirror.setFieldValue(obj, "valI", 1000);
      assertEquals(1000, obj.valI());
    }

    for (final Class<?> clazz : new Class<?>[] { ClassA.class, ClassB.class }) {
      final int i = Mirror.getFieldValue(clazz, "valS");
      Mirror.setFieldValue(clazz, "valS", 1000);
      assertEquals(1000, (int) Mirror.getFieldValue(clazz, "valS"));
      Mirror.setFieldValue(clazz, "valS", i);
    }
  }

  @Test
  public final void invokeMethod() throws Exception {
    for (final ClassA obj : new ClassA[] { new ClassA(), new ClassB() }) {
      assertEquals(1000, (int) Mirror.invokeMethod(obj, "addI", 937, 63));
    }

    for (final Class<?> clazz : new Class<?>[] { ClassA.class, ClassB.class }) {
      assertEquals(1000, (int) Mirror.invokeMethod(clazz, "addS", 937, 63));
    }
  }

  @Test
  public final void invokeConstructor() throws Exception {
    final ClassA obj = Mirror.invokeConstructor(ClassA.class, 63);
    assertEquals(1000, (int) obj.valI());
  }

  @Test
  public final void getUnknownFieldValue() {
    assertThrows(ReflectiveOperationException.class, () -> Mirror.getFieldValue(ClassA.class, "unknown"));
  }

  @Test
  public final void setUnknownFieldValue() {
    assertThrows(ReflectiveOperationException.class, () -> Mirror.setFieldValue(ClassA.class, "unknown", null));
  }

  @Test
  public final void invokeUnknownMethod() {
    assertThrows(ReflectiveOperationException.class, () -> Mirror.invokeMethod(ClassA.class, "unknown"));
  }

  @Test
  public final void invokeUnknownConstructor() {
    assertThrows(ReflectiveOperationException.class, () -> Mirror.invokeConstructor(ClassA.class, "unknown"));
  }

  @Test
  public final void getUnknownFieldValueSafe() {
    assertNull(MirrorSafe.getFieldValue(ClassA.class, "unknown"));
  }

  @Test
  public final void invokeUnknownMethodSafe() {
    assertNull(MirrorSafe.invokeMethod(ClassA.class, "unknown"));
  }

  @Test
  public final void invokeUnknownConstructorSafe() {
    assertNull(MirrorSafe.invokeConstructor(ClassA.class, "unknown"));
  }
}
