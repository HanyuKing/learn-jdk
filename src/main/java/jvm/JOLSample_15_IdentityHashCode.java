/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package jvm;

import org.junit.Test;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

import static java.lang.System.out;

/**
 * @author Aleksey Shipilev
 */
public class JOLSample_15_IdentityHashCode {

    /*
     * The example for identity hash code.
     *
     * The identity hash code, once computed, should stay the same.
     * HotSpot opts to store the hash code in the mark word as well.
     * You can clearly see the hash code bytes in the header once
     * it was computed.
     */

    @Test
    public void testBiasedToThin() {
        out.println(VM.current().details());

        final B b = new B();

        ClassLayout layout = ClassLayout.parseInstance(b);

        synchronized (b) {
            out.println("**** Fresh object");
            out.println(layout.toPrintable());

        }

        out.println("hashCode: " + Integer.toHexString(b.hashCode()));
        out.println();

        synchronized (b) {
            out.println("**** After identityHashCode()");
            out.println(layout.toPrintable());
        }
    }

    @Test
    public void testBiasedToFat() {
        out.println(VM.current().details());

        final B b = new B();

        ClassLayout layout = ClassLayout.parseInstance(b);

//        b.hashCode(); // comment this line

        synchronized (b) {
            out.println("**** Fresh object");
            out.println(layout.toPrintable());

            out.println("hashCode: " + Integer.toHexString(b.hashCode()));
            out.println();

            out.println("**** After identityHashCode()");
            out.println(layout.toPrintable());
        }
    }

    public static class B {
    }


    /**
     * doc: https://developer.huawei.com/consumer/cn/forum/topic/0203924188150280601
     *
     * markword中的hashcode是哪个方法生成的？
     * 很多人误以为，markword中的hashcode是由我们经常覆写的hashcode（）方法生成的。
     *
     * 实际上， markword中的hashcode只由底层 JDK C++ 源码计算得到（java侧调用方法为 System.identityHashCode() ）， 生成后固化到markword中，
     *
     * 如果你覆写了hashcode()方法， 那么每次都会重新调用hashCode()方法重新计算哈希值。
     *
     * 根本原因是因为你覆写hashcode()之后，该方法中很可能会利用被修改的成员来计算哈希值，所以jvm不敢将其存储到markword中。
     *
     * 因此，如果覆写了hashcode（）方法，对象头中就不会生成hashcode，而是每次通过hashcode()方法调用
     */
    public static class A {
        @Override
        public int hashCode() {
            return 0x12345;
        }
    }

}
