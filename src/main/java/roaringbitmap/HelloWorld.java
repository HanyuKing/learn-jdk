package roaringbitmap;

import org.junit.Test;
import org.roaringbitmap.RoaringBitmap;

import static org.junit.Assert.assertEquals;

/**
 * @Author Hanyu.Wang
 * @Date 2025/2/20 19:26
 * @Description
 * @Version 1.0
 **/
public class HelloWorld {
    @Test
    public void givenTwoRoaringBitmap_whenUsingOr_thenWillGetSetsUnion() {
        RoaringBitmap expected = RoaringBitmap.bitmapOf(1, 2, 3, 4, 5, 6, 7, 8);
        RoaringBitmap A = RoaringBitmap.bitmapOf(1, 2, 3, 4, 5);
        RoaringBitmap B = RoaringBitmap.bitmapOf(4, 5, 6, 7, 8);
        RoaringBitmap union = RoaringBitmap.or(A, B);
        assertEquals(expected, union);
    }
}
