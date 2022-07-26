package guava;

import org.junit.Test;

import java.util.Optional;

/**
 * @author Hanyu King
 * @since 2018-06-28 17:16
 */
public class OptionalTest {

    @Test
    public void testOf() {
        Optional<Integer> optional = Optional.of(6);
        if(optional.isPresent()) {
            System.out.println(optional.get());
        }
    }

    @Test
    public void testFromNullable() {
        Optional<Integer> optional = Optional.ofNullable(null);
        System.out.println(optional.isPresent());

    }

    @Test
    public void testEqual() {
        Long id = null;
        Long cityId = null;

        System.out.println(Optional.ofNullable(id).orElse(null));
    }
}
