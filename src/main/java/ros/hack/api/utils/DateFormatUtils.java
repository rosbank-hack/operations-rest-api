package ros.hack.api.utils;

import org.slf4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.slf4j.LoggerFactory.getLogger;

public class DateFormatUtils {
    private static final Logger logger = getLogger(DateFormatUtils.class);
    private static final String DEFAULT_DATE_FORMAT = "yyyy-mm-dd hh:mm:ss";

    @Nullable
    public static String format(@Nonnull Date dateTime) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
            return dateFormat.format(dateTime);
        } catch (Exception e) {
            logger.warn("Failed to parse date. {}", e.getMessage());
            return null;
        }
    }
}
