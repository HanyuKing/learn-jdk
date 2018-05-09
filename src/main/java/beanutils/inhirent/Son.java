package beanutils.inhirent;

/**
 * @author Hanyu King
 * @since 2018-04-16 14:51
 */
public class Son extends Father {
    private String goSchoolDate;

    public String getGoSchoolDate() {
        return goSchoolDate;
    }

    public void setGoSchoolDate(String goSchoolDate) {
        this.goSchoolDate = goSchoolDate;
    }

    @Override
    public String toString() {
        return super.toString() + "Son{" +
                "goSchoolDate='" + goSchoolDate + '\'' +
                '}';
    }
}
