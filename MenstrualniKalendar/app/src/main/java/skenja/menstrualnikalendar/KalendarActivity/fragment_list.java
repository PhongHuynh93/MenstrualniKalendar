package skenja.menstrualnikalendar.KalendarActivity;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import skenja.menstrualnikalendar.R;
import skenja.menstrualnikalendar.Repositories.DateDotNet;
import skenja.menstrualnikalendar.model.RecordDTO;

public class fragment_list extends Fragment  implements OnClickListener {

    private View FragmentView;
    private ListFragmentListener Listener;
    private DateDotNet[] kalendar_datumi;
    private int numberOfDays;
    private DateDotNet selectedDate;

    public fragment_list() {}

    public void Initialise(DateDotNet d) {
        this.numberOfDays = d.NumberOfDaysInMonth();
        this.kalendar_datumi = new DateDotNet[numberOfDays];
        this.selectedDate = d;

        DateDotNet currentMonth = new DateDotNet(1, d.Month, d.Year);

        for(int i = 0; i < numberOfDays; i++) {
            kalendar_datumi[i] = new DateDotNet(i+1, currentMonth.Month, currentMonth.Year);
        }
    }

    private void SetNewAdapter()
    {
        ((ListView)FragmentView.findViewById(R.id.calendar_list)).setAdapter(new adapter_list(this, this.kalendar_datumi, this.selectedDate));
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
        FragmentView = inflater.inflate(R.layout.fragment_list, container, false);
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

    public interface ListFragmentListener{
        void SelectedDay(RecordDTO record);
    }

    public void onClick(View v) {
        if(this.Listener == null)
            this.Listener = (ListFragmentListener) getActivity();

        this.Listener.SelectedDay((RecordDTO) v.getTag());
    }
}
