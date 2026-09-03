package dev.colbster937.reflect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

@SuppressWarnings({ "unused" })
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

    private ClassA(int a) {
      this.valI += a;
    }

    private final int addI(int a, int b) {
      return addS(a, b);
    }

    private static final int addS(int a, int b) {
      return a + b;
    }
  }

  private static class ClassB extends ClassA {
  }

  @Test
  public final void getField() throws Exception {
    for (Class<?> clazz : new Class<?>[] { ClassA.class, ClassB.class }) {
      final Field field = Mirror.getField(clazz, "valI");
      assertSame(field, Mirror.getField(clazz, "valI"));
    }
  }

  @Test
  public final void getFieldValue() throws Exception {
    for (ClassA obj : new ClassA[] { new ClassA(), new ClassB() }) {
      assertEquals(937, (int) Mirror.getFieldValue(obj, "valI"));
    }

    for (Class<?> clazz : new Class<?>[] { ClassA.class, ClassB.class }) {
      assertEquals(937, (int) Mirror.getFieldValue(clazz, "valS"));
    }
  }

  @Test
  public final void setFieldValue() throws Exception {
    for (ClassA obj : new ClassA[] { new ClassA(), new ClassB() }) {
      Mirror.setFieldValue(obj, "valI", 1000);
      assertEquals(1000, obj.valI());
    }

    for (Class<?> clazz : new Class<?>[] { ClassA.class, ClassB.class }) {
      final int i = Mirror.getFieldValue(clazz, "valS");
      Mirror.setFieldValue(clazz, "valS", 1000);
      assertEquals(1000, (int) Mirror.getFieldValue(clazz, "valS"));
      Mirror.setFieldValue(clazz, "valS", i);
    }
  }

  @Test
  public final void getMethod() throws Exception {
    for (Class<?> clazz : new Class<?>[] { ClassA.class, ClassB.class }) {
      final Method method = Mirror.getMethod(clazz, "addI", Integer.class, Integer.class);
      assertSame(method, Mirror.getMethod(clazz, "addI", Integer.class, Integer.class));
    }
  }

  @Test
  public final void invokeMethod() throws Exception {
    for (ClassA obj : new ClassA[] { new ClassA(), new ClassB() }) {
      assertEquals(1000, (int) Mirror.invokeMethod(obj, "addI", 937, 63));
    }

    for (Class<?> clazz : new Class<?>[] { ClassA.class, ClassB.class }) {
      assertEquals(1000, (int) Mirror.invokeMethod(clazz, "addS", 937, 63));
    }
  }

  @Test
  public final void getConstructor() throws Exception {
    final Constructor<?> constructor = Mirror.getConstructor(ClassA.class, Integer.class);
    assertSame(constructor, Mirror.getConstructor(ClassA.class, Integer.class));
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
