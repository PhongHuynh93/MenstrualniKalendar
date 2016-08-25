package skenja.menstrualnikalendar.LoginActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import skenja.menstrualnikalendar.KalendarActivity.activity_kalendar;
import skenja.menstrualnikalendar.R;
import skenja.menstrualnikalendar.Repositories.DateDotNet;
import skenja.menstrualnikalendar.Repositories.PreferenceRepository;
import skenja.menstrualnikalendar.Repositories.RecordRepository;
import skenja.menstrualnikalendar.model.RecordDTO;

public class activity_login extends Activity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.pin_submit).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public void onClick(View view)
    {
        String input = ((EditText)findViewById(R.id.pin_input)).getText().toString();

        if(PreferenceRepository.ValidatePIN(input, getApplicationContext()))
            startActivity(new Intent(this, activity_kalendar.class));
        else{
            findViewById(R.id.error).setVisibility(View.VISIBLE);
            ((EditText)findViewById(R.id.pin_input)).setText("");
        }
    }
}
