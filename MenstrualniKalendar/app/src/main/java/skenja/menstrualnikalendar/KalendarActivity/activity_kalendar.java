package skenja.menstrualnikalendar.KalendarActivity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import skenja.menstrualnikalendar.R;
import skenja.menstrualnikalendar.Repositories.DateDotNet;
import skenja.menstrualnikalendar.Repositories.PreferenceRepository;
import skenja.menstrualnikalendar.Repositories.RecordRepository;
import skenja.menstrualnikalendar.SettingsActivity.activity_settings;
import skenja.menstrualnikalendar.StatsActivity.activity_stats;
import skenja.menstrualnikalendar.KalendarActivity.fragment_grid.GridFragmentListener;
import skenja.menstrualnikalendar.KalendarActivity.fragment_list.ListFragmentListener;
import skenja.menstrualnikalendar.KalendarActivity.fragment_calendar_controller.ControllerFragmentListener;
import skenja.menstrualnikalendar.KalendarActivity.fragment_day.DayFragmentListener;
import skenja.menstrualnikalendar.model.RecordDTO;

public class activity_kalendar extends Activity implements DayFragmentListener, GridFragmentListener, ListFragmentListener, ControllerFragmentListener {

    private Menu menu;
    private DateDotNet selectedDate;
    private int displayMode;

    public static String TAG_GRID = "grid";
    public static String TAG_LIST = "list";
    public static String TAG_CONTROLLER = "controller";
    public static String TAG_DAY = "day";

    public static String KEY_SELECTED_DATE = "selectedDate";
    public static String KEY_DISPLAY = "display";

    public static int CALENDAR = 1;
    public static int DAY = 2;

    @Override
    public void onSaveInstanceState(Bundle bundle){
        bundle.putString(KEY_SELECTED_DATE, selectedDate.toShortDateString());
        bundle.putInt(KEY_DISPLAY, displayMode);

        RemoveFragments();

        super.onSaveInstanceState(bundle);
    }

    @Override
    public void onRestart()    {
        super.onRestart();
        create(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kalendar);
        create(savedInstanceState);
    }

    private void create(Bundle b)
    {
        if(b != null) {
            this.displayMode = b.getInt(KEY_DISPLAY);
            this.selectedDate = new DateDotNet(b.getString(KEY_SELECTED_DATE));
        }
        else {
            this.displayMode = CALENDAR;
            this.selectedDate = DateDotNet.Today();
        }

        if(displayMode == CALENDAR){
            SetupCalendarFragment();
        }
        else if(displayMode == DAY) {
            fragment_day f = new fragment_day();
            f.SetRecord(RecordRepository.GetModel(selectedDate));

            findViewById(R.id.controller_container).setVisibility(View.GONE);

            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, f, TAG_DAY)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_kalendar, menu);
        this.menu = menu;
        initPill();
        return true;
    }

    public void onClickMI(MenuItem mi)
    {
        switch (mi.getItemId())
        {
            case R.id.mi_pill_toggle: pillToggle(); break;
            case R.id.mi_stats: startActivity(new Intent(this, activity_stats.class)); break;
            case R.id.mi_settings: startActivity(new Intent(this, activity_settings.class)); break;
        }
    }

    public boolean pillToggle()
    {
        if(PreferenceRepository.ChangePillStatus(getApplicationContext())) {
            initPill();
            return true;
        }
        else return false;
    }

    public void initPill()
    {
        if (PreferenceRepository.PillStatus(getApplicationContext()))
            menu.findItem(R.id.mi_pill_toggle).setIcon(R.drawable.pill_on);
        else
            menu.findItem(R.id.mi_pill_toggle).setIcon(R.drawable.pill_off);
    }

    public void RemoveFragments()
    {
        Fragment f;
        if(displayMode == CALENDAR)        {
            f = getFragmentManager().findFragmentByTag(TAG_LIST);
            if(f == null)
                f = getFragmentManager().findFragmentByTag(TAG_GRID);
        }
        else        {
            f = getFragmentManager().findFragmentByTag(TAG_DAY);
        }

        Fragment controller = getFragmentManager().findFragmentByTag(TAG_CONTROLLER);

        if(controller != null && f != null)
            getFragmentManager().beginTransaction()
                .remove(f)
                .remove(controller)
                .commit();
        else if(f != null)
            getFragmentManager().beginTransaction()
                    .remove(f)
                    .commit();
    }

    @Override
    public void SelectedDay(RecordDTO record) {
        fragment_day f = new fragment_day();
        f.SetRecord(record);

        displayMode = DAY;
        selectedDate = record.date;

        fragment_calendar_controller c = (fragment_calendar_controller) getFragmentManager().findFragmentByTag(TAG_CONTROLLER);
        findViewById(R.id.controller_container).setVisibility(View.GONE);

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, f, TAG_DAY)
                .remove(c)
                .commit();
    }

    private void SetupCalendarFragment() {
        Fragment calendar;
        String tag;

        displayMode = CALENDAR;

        if(PreferenceRepository.LayoutIsGrid(this)) {
            calendar = new fragment_grid();
            ((fragment_grid) calendar).Initialise(selectedDate);
            tag = TAG_GRID;
        }
        else{
            calendar = new fragment_list();
            ((fragment_list) calendar).Initialise(selectedDate);
            tag = TAG_LIST;
        }

        fragment_calendar_controller controller = new fragment_calendar_controller();
        controller.SetDate(selectedDate);

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, calendar, tag)
                .replace(R.id.controller_container, controller, TAG_CONTROLLER)
                .commit();

        findViewById(R.id.controller_container).setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if(displayMode == DAY){
            SetupCalendarFragment();
        }
        else{
            super.onBackPressed();
            finish();
        }
    }

    @Override
    public void BackPressed(){
        onBackPressed();
    }

    @Override
    public void RecordUpdated(RecordDTO record) {
        RecordRepository.Update(record);
    }

    @Override
    public void MonthChange(DateDotNet d, int value) {
        selectedDate.Month += value;
        if(selectedDate.Month == 0){
            selectedDate.Month = 12;
            selectedDate.Year--;
        }
        else if(selectedDate.Month == 13) {
            selectedDate.Month = 1;
            selectedDate.Year++;
        }
        selectedDate.Day = 1;

        Fragment f;
        if( (f = getFragmentManager().findFragmentByTag(TAG_GRID)) == null) {
            ((fragment_list) getFragmentManager().findFragmentByTag(TAG_LIST)).NewAdapter(selectedDate);
        }
        else{
            ((fragment_grid) f).NewAdapter(selectedDate);
        }

        ((fragment_calendar_controller) getFragmentManager().findFragmentByTag(TAG_CONTROLLER)).NewDate(selectedDate);
    }

    @Override
    public void FormatChange(int buttonId) {
        boolean isGrid = PreferenceRepository.LayoutIsGrid(this);

        if ((buttonId == 1 && isGrid) || (buttonId == 2 && !isGrid)) {
            PreferenceRepository.ChangeLayout(this);

            Fragment calendar;
            String tag;

            if (PreferenceRepository.LayoutIsGrid(this)) {
                calendar = new fragment_grid();
                ((fragment_grid) calendar).Initialise(selectedDate);
                tag = TAG_GRID;
            } else {
                calendar = new fragment_list();
                ((fragment_list) calendar).Initialise(selectedDate);
                tag = TAG_LIST;
            }

            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, calendar, tag)
                    .commit();
        }
    }
}
