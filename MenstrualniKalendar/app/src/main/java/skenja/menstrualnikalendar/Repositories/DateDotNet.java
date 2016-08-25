package skenja.menstrualnikalendar.Repositories;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateDotNet {

    public int Day;
    public int Month;
    public int Year;

    //Statics
    private static String Month_jan = "Siječanj";
    private static String Month_feb = "Veljača";
    private static String Month_mar = "Ožujak";
    private static String Month_apr = "Travanj";
    private static String Month_may = "Svibanj";
    private static String Month_jun = "Lipanj";
    private static String Month_jul = "Srpanj";
    private static String Month_aug = "Kolovoz";
    private static String Month_sep = "Rujan";
    private static String Month_oct = "Listopad";
    private static String Month_nov = "Studeni";
    private static String Month_dec = "Prosinac";

    private static String Day_mon = "ponedjeljak";
    private static String Day_tue = "utorak";
    private static String Day_wed = "srijeda";
    private static String Day_thu = "četvrtak";
    private static String Day_fri = "petak";
    private static String Day_sat = "subota";
    private static String Day_sun = "nedjelja";

    public DateDotNet() {}

    public DateDotNet(int d, int m, int y)     {
        this.Day = d;
        this.Month = m;
        this.Year = y;
    }

    public DateDotNet(String s){
        if(s != null){
            this.Day = Integer.parseInt(s.substring(0, s.indexOf('.')));
            s = s.substring(s.indexOf('.') + 1);
            this.Month = Integer.parseInt(s.substring(0, s.indexOf('.')));
            s = s.substring(s.indexOf('.') + 1);
            this.Year = Integer.parseInt(s.substring(0, s.indexOf('.')));
        }
    }

    public static DateDotNet Today(){
        Calendar c = Calendar.getInstance();

        int Day = c.get(Calendar.DAY_OF_MONTH);
        int Month = c.get(Calendar.MONTH) + 1;
        int Year = c.get(Calendar.YEAR);

        return new DateDotNet(Day, Month, Year);
    }

    public int NumberOfDaysInMonth()
    {
        return new GregorianCalendar(this.Year, this.Month - 1, this.Day).getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public DateDotNet FirstOfMonth()
    {
        return DateDotNet.FirstOfMonth(this);
    }

    public static DateDotNet FirstOfMonth(DateDotNet d)
    {
        d.Day = 1;
        return d;
    }

    public boolean Equals(DateDotNet d2){
        if(d2.Day != this.Day) return false;
        if(d2.Month != this.Month) return false;
        if(d2.Year != this.Year) return false;
        
        return true;
    }

    public String CalendarPageString(){
        return this.MonthOfYear() + ", " + this.Year + ".";
    }
    
    public String toShortDateString()
    {
        return Day + "." + Month + "." + Year + ".";
    }

    public int DayOfWeek(){
        return DateDotNet.DayOfWeek(this);
    }

    public static int DayOfWeek(DateDotNet d)    {
        int a = new GregorianCalendar(d.Year, d.Month - 1, d.Day).get(Calendar.DAY_OF_WEEK) - 1;
        return a == 0 ? 7 : a;
    }

    public String StringDayOfWeek()    {
        return DateDotNet.StringDayOfWeek(this);
    }

    public static String StringDayOfWeek(DateDotNet d)    {
        switch (d.DayOfWeek())        {
            case 1: return DateDotNet.Day_mon;
            case 2: return DateDotNet.Day_tue;
            case 3: return DateDotNet.Day_wed;
            case 4: return DateDotNet.Day_thu;
            case 5: return DateDotNet.Day_fri;
            case 6: return DateDotNet.Day_sat;
            case 7: return DateDotNet.Day_sun;
            default: return null;
        }
    }
    
    public String MonthOfYear()    {
        return DateDotNet.MonthOfYear(this.Month);
    }
    
    public static String MonthOfYear(int Month)    {
        switch (Month)        {
            case 1: return DateDotNet.Month_jan;
            case 2: return DateDotNet.Month_feb;
            case 3: return DateDotNet.Month_mar;
            case 4: return DateDotNet.Month_apr;
            case 5: return DateDotNet.Month_may;
            case 6: return DateDotNet.Month_jun;
            case 7: return DateDotNet.Month_jul;
            case 8: return DateDotNet.Month_aug;
            case 9: return DateDotNet.Month_sep;
            case 10: return DateDotNet.Month_oct;
            case 11: return DateDotNet.Month_nov;
            case 12: return DateDotNet.Month_dec;
            default: return null;
        }
    }
}