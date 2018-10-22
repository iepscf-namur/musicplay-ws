package WS.utils;

import java.util.Calendar;

public class DateTimeUtils {
    public static java.sql.Date getSqlCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        java.util.Date currentDate = calendar.getTime();
        return new java.sql.Date(currentDate.getTime());
    }
}
