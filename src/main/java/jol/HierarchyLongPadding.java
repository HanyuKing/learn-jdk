package jol;

public class HierarchyLongPadding {
  static class Pad1 {
    long l01, l02, l03, l04, l05, l06, l07, l08;
  }
  static class Carrier extends Pad1 {
    byte pleaseHelpMe;
  }
  static class Pad2 extends Carrier {
    long l11, l12, l13, l14, l15, l16, l17, l18;
  }
  static class UsableObject extends Pad2 {};
}