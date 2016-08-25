package skenja.menstrualnikalendar.KalendarActivity;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import skenja.menstrualnikalendar.R;
import skenja.menstrualnikalendar.Repositories.DateDotNet;
import skenja.menstrualnikalendar.Repositories.RecordRepository;
import skenja.menstrualnikalendar.model.RecordDTO;

public class adapter_list extends BaseAdapter{

    private fragment_list fragment;
    private DateDotNet[] datumi;
    private DateDotNet selectedDate;

    public adapter_list(fragment_list f, DateDotNet[] d, DateDotNet selected) {
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
        final View view = LayoutInflater.from(fragment.getActivity()).inflate(R.layout.adapter_calendar_list, null);
        final DateDotNet date = datumi[position];

        ((TextView) view.findViewById(R.id.adapter_date)).setText(date.Day + "");

        view.setOnClickListener(this.fragment);

        if(date.Equals(selectedDate))
            view.setBackgroundColor(Color.CYAN);

        new Thread(new Runnable() {
            public void run(){
                final RecordDTO day = RecordRepository.GetModel(date);

                if(day != null)
                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            if(day.Coitus > 0)
                                view.findViewById(R.id.adapter_coitus).setVisibility(View.VISIBLE);
                            if(day.Period > 0)
                                view.findViewById(R.id.adapter_period).setVisibility(View.VISIBLE);
                            if(day.Headache > 0)
                                view.findViewById(R.id.adapter_headache).setVisibility(View.VISIBLE);
                            if(day.Comment != null)
                                ((TextView) view.findViewById(R.id.adapter_comment)).setText(day.Comment);
                            view.setTag(day);
                        }
                    });
                else
                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            view.setAlpha(0.75f);
                        }
                    });
            }
        }).start();

        return view;
    }
}
