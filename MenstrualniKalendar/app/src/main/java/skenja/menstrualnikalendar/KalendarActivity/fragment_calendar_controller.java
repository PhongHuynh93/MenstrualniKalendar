package skenja.menstrualnikalendar.KalendarActivity;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Set;

import skenja.menstrualnikalendar.R;
import skenja.menstrualnikalendar.Repositories.DateDotNet;
import skenja.menstrualnikalendar.Repositories.PreferenceRepository;

public class fragment_calendar_controller extends Fragment implements OnClickListener{

    private View FragmentView;
    private ControllerFragmentListener Listener;
    private DateDotNet selectedDate;

    public fragment_calendar_controller() {}

    public void SetDate(DateDotNet d) {
        this.selectedDate = d;
    }

    public void NewDate(DateDotNet d){
        SetDate(d);
        DisplayDate();
    }

    private void DisplayDate(){
        ((TextView) FragmentView.findViewById(R.id.controller_display)).setText(selectedDate.CalendarPageString());
    }

    public void DisplayFormat() {
        if(Listener == null)
            if(isAdded())
                Listener = (ControllerFragmentListener) getActivity();

        if(PreferenceRepository.LayoutIsGrid((Context) Listener)) {
            ((ImageView) FragmentView.findViewById(R.id.controller_grid)).setImageResource(R.drawable.grid_on);
            ((ImageView) FragmentView.findViewById(R.id.controller_list)).setImageResource(R.drawable.list);
        }
        else {
            ((ImageView) FragmentView.findViewById(R.id.controller_grid)).setImageResource(R.drawable.grid);
            ((ImageView) FragmentView.findViewById(R.id.controller_list)).setImageResource(R.drawable.list_on);
        }
    }

    public void InitOnClick()
    {
        FragmentView.findViewById(R.id.controller_grid).setOnClickListener(this);
        FragmentView.findViewById(R.id.controller_list).setOnClickListener(this);
        FragmentView.findViewById(R.id.controller_next_month).setOnClickListener(this);
        FragmentView.findViewById(R.id.controller_previous_month).setOnClickListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentView = inflater.inflate(R.layout.fragment_calendar_controller, container, false);

        InitOnClick();
        DisplayFormat();

        if(selectedDate != null)
            DisplayDate();

        return FragmentView;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        this.Listener = null;
    }

    public interface ControllerFragmentListener{
        void MonthChange(DateDotNet currentDate, int Value);
        void FormatChange(int buttonId);
    }

    public void onClick(View v)
    {
        if(Listener == null)
            Listener = (ControllerFragmentListener) getActivity();

        switch (v.getId())
        {
            case R.id.controller_previous_month:
                Listener.MonthChange(selectedDate, -1);
                break;
            case R.id.controller_next_month:
                Listener.MonthChange(selectedDate, +1);
                break;
            case R.id.controller_grid:
                Listener.FormatChange(2);
                DisplayFormat();
                break;
            case R.id.controller_list:
                Listener.FormatChange(1);
                DisplayFormat();
                break;
        }
    }
}
