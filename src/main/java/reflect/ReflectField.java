package reflect;

import java.lang.reflect.Field;

/**
 * @author Hanyu King
 * @since 2018-06-27 17:19
 */
public class ReflectField {
    public static void main(String[] args) throws IllegalAccessException {
        System.out.println(CouponDataField.getAllField()[1]);
    }

    static class CouponDataField {
        public static String POPCOUPONID = "popCouponId";
        public static String POPCOUPONID2 = "popCouponId2";
        private final static String[] ALL_FIELD;
        static {
            Field[] fields = CouponDataField.class.getFields();
            ALL_FIELD = new String[fields.length];

            for(int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                try {
                    ALL_FIELD[i] = (String) field.get(field.getName());
                } catch (Exception e) {
                }
            }

        }

        public static String[] getAllField() {
            return ALL_FIELD;
        }
    }
}


