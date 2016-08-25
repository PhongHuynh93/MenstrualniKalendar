package skenja.menstrualnikalendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import skenja.menstrualnikalendar.Repositories.DateDotNet;
import skenja.menstrualnikalendar.Repositories.PreferenceRepository;
import skenja.menstrualnikalendar.Repositories.RecordRepository;
import skenja.menstrualnikalendar.model.RecordDTO;

public class NewDayAlarmHandler extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        RecordDTO record = RecordRepository.GetModel(DateDotNet.Today());

        if(record == null) {
            record = new RecordDTO();
            record.date = DateDotNet.Today();
            record.Pill = PreferenceRepository.PillStatus(context);
            RecordRepository.Insert(record);
        }
        else
            Toast.makeText(context, "aaaaaaaaaaaa", Toast.LENGTH_LONG).show();
    }
}
