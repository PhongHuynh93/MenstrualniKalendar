package skenja.menstrualnikalendar.KalendarActivity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.Date;
import java.util.List;

import skenja.menstrualnikalendar.Repositories.DateDotNet;
import skenja.menstrualnikalendar.Repositories.RecordRepository;
import skenja.menstrualnikalendar.model.Record;
import skenja.menstrualnikalendar.R;
import skenja.menstrualnikalendar.model.RecordDTO;

public class fragment_grid extends Fragment implements OnClickListener{

    private View FragmentView;
    private GridFragmentListener Listener;
    private DateDotNet[] kalendar_datumi;
    private DateDotNet selectedDate;

    public fragment_grid() {}

    public void Initialise(DateDotNet d) {
        kalendar_datumi = new DateDotNet[42];
        this.selectedDate = d;

        DateDotNet currentMonth = new DateDotNet(1, d.Month, d.Year);
        int dayOfTheWeek = currentMonth.DayOfWeek() - 1;
        int daysInCurrentMonth = currentMonth.NumberOfDaysInMonth();

        for(int i = 0; i < daysInCurrentMonth; i++)        {
            kalendar_datumi[i + dayOfTheWeek] = new DateDotNet(i+1, currentMonth.Month, currentMonth.Year);
        }

        if(dayOfTheWeek > 0)        {
            DateDotNet previousMonth = new DateDotNet(1, currentMonth.Month - 1, currentMonth.Year);
            int daysInPreviousMonth = previousMonth.NumberOfDaysInMonth();

            for(int i = dayOfTheWeek - 1; i >= 0; i--)            {
                kalendar_datumi[i] = new DateDotNet(daysInPreviousMonth, previousMonth.Month, previousMonth.Year);
                daysInPreviousMonth--;
            }
        }

        DateDotNet nextMonth = new DateDotNet(1, currentMonth.Month + 1, currentMonth.Year);
        for(int i = daysInCurrentMonth + dayOfTheWeek; i < 42; i++)
        {
            kalendar_datumi[i] = new DateDotNet(nextMonth.Day, nextMonth.Month, nextMonth.Year);
            nextMonth.Day++;
        }
    }

    private void SetNewAdapter()
    {
        ((GridView)FragmentView.findViewById(R.id.calendar_grid)).setAdapter(new adapter_grid(this, this.kalendar_datumi, this.selectedDate));
    }

    public void NewAdapter(DateDotNet d) {
        Initialise(d);
        SetNewAdapter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentView = inflater.inflate(R.layout.fragment_grid, container, false);
        SetNewAdapter();
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

    public interface GridFragmentListener{
        void SelectedDay(RecordDTO record);
    }

    public void onClick(View v) {
        if(this.Listener == null)
            this.Listener = (GridFragmentListener) getActivity();

        this.Listener.SelectedDay((RecordDTO) v.getTag());
    }
}
