package skenja.menstrualnikalendar.KalendarActivity;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import skenja.menstrualnikalendar.Repositories.DateDotNet;
import skenja.menstrualnikalendar.Repositories.RecordRepository;
import skenja.menstrualnikalendar.model.Record;
import skenja.menstrualnikalendar.R;
import skenja.menstrualnikalendar.model.RecordDTO;

public class adapter_grid extends BaseAdapter {

    private fragment_grid fragment;
    private DateDotNet[] datumi;
    private DateDotNet selectedDate;

    public adapter_grid(fragment_grid f, DateDotNet[] d, DateDotNet selected) {
        this.fragment = f;
        this.datumi = d;
        this.selectedDate = selected;
    }

    @Override
    public int getCount() {
        return datumi.length;
    }

    @Override
    public Object getItem(int position) {
        return datumi[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = LayoutInflater.from(fragment.getActivity()).inflate(R.layout.adapter_calendar_grid, null);
        final DateDotNet datum = datumi[position];

        ((TextView) view.findViewById(R.id.adapter_date)).setText(datum.Day + "");

        view.setOnClickListener(this.fragment);

        if(datum.Month != this.selectedDate.Month)
            view.setAlpha(0.75f);

        if(datum.Equals(selectedDate))
            view.setBackgroundColor(Color.CYAN);

        new Thread(new Runnable() {
            public void run(){
                RecordDTO r = RecordRepository.GetModel(datum);

                if(r == null){
                    r = new RecordDTO();
                    r.date = datum;
                    RecordRepository.Insert(r);
                }

                final RecordDTO dan = r;

                view.post(new Runnable() {
                    @Override
                    public void run() {
                        if(dan.Coitus > 0)
                            view.findViewById(R.id.adapter_coitus).setVisibility(View.VISIBLE);
                        if(dan.Period > 0)
                            view.findViewById(R.id.adapter_period).setVisibility(View.VISIBLE);
                        if(dan.Headache > 0)
                            view.findViewById(R.id.adapter_headache).setVisibility(View.VISIBLE);
                        view.setTag(dan);
                    }
                });
            }
        }).start();

        return view;
    }
}
