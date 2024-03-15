package jol;

/**
 * Note there is the same gap we have seen before, caused by long alignment. Theoretically, B.somethingElse could have taken it, but field layouter implementation quirk makes it impossible. Therefore, we lay out fields of B after the fields of A, and waste 8 bytes.
 */
public class LongIntCarrierSubs {
  static class A {
    long value;
  }
  static class B extends A {
    int somethingElse;
  }
}