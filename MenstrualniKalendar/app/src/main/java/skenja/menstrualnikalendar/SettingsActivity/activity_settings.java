package skenja.menstrualnikalendar.SettingsActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import skenja.menstrualnikalendar.R;
import skenja.menstrualnikalendar.Repositories.PreferenceRepository;
import skenja.menstrualnikalendar.Repositories.RecordRepository;

public class activity_settings extends Activity implements OnClickListener {

    private static int MODE_IMPORT = 1;
    private static int MODE_EXPORT = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        InitListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    public void InitListeners()
    {
        findViewById(R.id.pinSave).setOnClickListener(this);
        findViewById(R.id.button_delete).setOnClickListener(this);
        findViewById(R.id.button_export).setOnClickListener(this);
        findViewById(R.id.button_import).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pinSave:
                String oldPin = ((TextView) findViewById(R.id.oldPin)).getText().toString();
                String newPin = ((TextView) findViewById(R.id.newPin)).getText().toString();

                if(PreferenceRepository.ChangePIN(oldPin, newPin, this))
                    Toast.makeText(this, "Uspjeh!", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(this, "Neuspjeh!", Toast.LENGTH_LONG).show();
                break;
            case R.id.button_delete:
                new AlertDialog.Builder(this)
                        .setMessage("Želite li zaista obrisati sve zapise? Neće ih biti moguće vratiti!")
                        .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                RecordRepository.DeleteAll();
                                PreferenceRepository.DBDoesExist(getApplication());
                                Toast.makeText(getApplicationContext(), "Brisanje započeto!", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("Ne", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        }).create().show();
            case R.id.button_export:
                DBOperation(MODE_EXPORT);
                break;
            case R.id.button_import:
                DBOperation(MODE_IMPORT);
                break;
        }
    }

    public void onClickMI(MenuItem mi)    {
        onBackPressed();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }

    public boolean FolderValidator(){
        try{
            File directory = new File(Environment.getExternalStorageDirectory() + "/" + getResources().getString(R.string.app_name));
            if(!directory.exists())
                return directory.mkdir();
            else
                return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public void DBOperation(final int mode)
    {
        if(FolderValidator())
            new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            try {
                                File sd = new File(Environment.getExternalStorageDirectory() + "/" + getResources().getString(R.string.app_name));
                                File data = Environment.getDataDirectory();

                                if (sd.canWrite()) {
                                    String currentDBPath = "";
                                    String backupDBPath = "";

                                    if(mode == MODE_EXPORT){
                                        currentDBPath= "/data/" + "skenja.menstrualnikalendar" + "/databases/" + "RecordDb.db";
                                        backupDBPath  = "/RecordDb.db";
                                    }
                                    else if(mode == MODE_IMPORT){
                                        currentDBPath= "/data/" + "skenja.menstrualnikalendar" + "/databases/" + "RecordDb.db";
                                        backupDBPath  = "/RecordDb.db";
                                    }

                                    File currentDB = new File(data, currentDBPath);
                                    File backupDB = new File(sd, backupDBPath);

                                    FileChannel src = new FileInputStream(currentDB).getChannel();
                                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                                    dst.transferFrom(src, 0, src.size());
                                    src.close();
                                    dst.close();
                                    if(mode == MODE_IMPORT)
                                        Toast.makeText(getBaseContext(), "Uvoz zapisa uspio!", Toast.LENGTH_LONG).show();
                                    else
                                        Toast.makeText(getBaseContext(), "Izvoz zapisa uspio!", Toast.LENGTH_LONG).show();
                                }
                                else
                                    Toast.makeText(getBaseContext(), "Potrebna su mi dozvole za čitanje i pisanje u memoriju!", Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                if(mode == MODE_IMPORT)
                                    Toast.makeText(getBaseContext(), "Uvoz zapisa nije uspio!", Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(getBaseContext(), "Izvoz zapisa nije uspio!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
            ).run();
    }
}
