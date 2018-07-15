package project.com.ningbaoqi.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtil {
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yy-MM-dd", Locale.ENGLISH);

    /**
     * 获取一个简单的时间字符串
     *
     * @param date
     * @return
     */
    public static String getSimpleTime(Date date) {
        return FORMAT.format(date);
    }
}
