package skenja.menstrualnikalendar;

import java.util.Date;
import java.util.Calendar;

import skenja.menstrualnikalendar.Repositories.DateDotNet;

public class Helper {

    public static Date Parse(DateDotNet d)
    {
        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(d.Year, d.Month - 1, d.Day);
        return new Date(c.getTimeInMillis());
    }

    public static DateDotNet Parse(Date d)
    {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(d.getTime());

        DateDotNet date = new DateDotNet();
        date.Year = c.get(Calendar.YEAR);
        date.Month = c.get(Calendar.MONTH) + 1;
        date.Day = c.get(Calendar.DAY_OF_MONTH);

        return date;
    }

}
