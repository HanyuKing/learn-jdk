package juc.spinlock;

class QNode {
  boolean locked = false;
  QNode   next   = null;
}
