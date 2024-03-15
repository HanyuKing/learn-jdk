package jol;

/*

Note: all classes agree at where A.a super-class field is. This allows blind casts to A from any subtype, and then accessing a field there, without looking back at the actual type of the object. That is, ((A)o).a would always go to the same offset, regardless of whether we are dealing with instance of A, B, or C.

This looks as if superclass fields are always taken care of first. Does it mean superclass fields are always first in the hierarchy? That is an implementation detail: prior JDK 15, the answer is "yes"; after JDK 15 the answer is "no". We shall quantify that with a few observations.

 */
public class Hierarchy {
  static class A {
    int a;
  }
  static class B extends A {
    int b;
  }
  static class C extends A {
    int c;
  }
}