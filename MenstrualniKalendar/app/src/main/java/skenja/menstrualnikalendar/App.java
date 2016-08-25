package skenja.menstrualnikalendar;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import com.raizlabs.android.dbflow.config.FlowManager;

import java.util.Calendar;
import java.util.Random;

import skenja.menstrualnikalendar.Repositories.DateDotNet;
import skenja.menstrualnikalendar.Repositories.PreferenceRepository;
import skenja.menstrualnikalendar.Repositories.RecordRepository;
import skenja.menstrualnikalendar.model.RecordDTO;

public class App extends Application {

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    @Override
    public void onCreate()
    {
        super.onCreate();
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        FlowManager.init(this);

        SetAlarm();

        //testing purposes only
        if(!PreferenceRepository.DBExists(this))
            new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            Seed();
                        }
                    }
            ).run();
    }

    public void SetAlarm() {
        Context context = getApplicationContext();

        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent("noviJeDan");
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        DateDotNet date = DateDotNet.Today();
        calendar.set(date.Year, date.Month, date.Day+1, 0, 0, 0);

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 60 * 24, alarmIntent);
    }

    public void Seed()
    {
        PreferenceRepository.DBDoesExist(this);

        for(int i = 1; i < 32; i++)
        {
            RecordDTO r = new RecordDTO();
            r.date.Day = i;
            r.date.Month = 5;
            r.date.Year = 2016;
            r.Coitus = new Random().nextInt(3);
            r.Headache = new Random().nextInt(4);
            r.Period = new Random().nextInt(4);
            r.Pill = new Random().nextBoolean();
            RecordRepository.Insert(r);
        }

        for(int i = 1; i < 31; i++)
        {
            RecordDTO r = new RecordDTO();
            r.date.Day = i;
            r.date.Month = 6;
            r.date.Year = 2016;
            r.Coitus = new Random().nextInt(3);
            r.Headache = new Random().nextInt(4);
            r.Period = new Random().nextInt(4);
            r.Pill = new Random().nextBoolean();
            RecordRepository.Insert(r);
        }

        for(int i = 1; i < 32; i++)
        {
            RecordDTO r = new RecordDTO();
            r.date.Day = i;
            r.date.Month = 7;
            r.date.Year = 2016;
            r.Coitus = new Random().nextInt(3);
            r.Headache = new Random().nextInt(4);
            r.Period = new Random().nextInt(4);
            r.Pill = new Random().nextBoolean();
            RecordRepository.Insert(r);
        }
    }
}
