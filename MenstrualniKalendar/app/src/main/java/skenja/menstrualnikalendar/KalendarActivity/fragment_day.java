package skenja.menstrualnikalendar.KalendarActivity;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.Set;

import skenja.menstrualnikalendar.R;
import skenja.menstrualnikalendar.model.Record;
import skenja.menstrualnikalendar.model.RecordDTO;

public class fragment_day extends Fragment implements OnClickListener, TextWatcher{
    private View FragmentView;
    private DayFragmentListener Listener;
    private RecordDTO record;

    private int pillState;
    private int periodState;
    private int headacheState;
    private int coitusState;

    private static int STATE_OFF = 1;
    private static int STATE_ON = 2;
    private static int STATE_EDIT = 3;

    public fragment_day() {}

    public void SetRecord(RecordDTO r) {
        this.record = r;
    }

    private void InitForm(){
        SetupOnClickListeners();
        PillModule();
        PeriodModule(0);
        HeadacheModule(0);
        CoitusModule(0);

        ((TextView) FragmentView.findViewById(R.id.datumShortDate)).setText(record.date.toShortDateString());
        ((TextView) FragmentView.findViewById(R.id.datumDayOfWeek)).setText(record.date.StringDayOfWeek());
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentView = inflater.inflate(R.layout.fragment_day, container, false);

        InitForm();

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

    public interface DayFragmentListener{
        void BackPressed();
        void RecordUpdated(RecordDTO record);
    }

    public void onClick(View v) {
        if(this.Listener == null)
            this.Listener = (DayFragmentListener) getActivity();

        int id = v.getId();

        switch (id) {
            case R.id.activator_back:
                Listener.BackPressed();
                break;
            case R.id.activator_pill:
                record.Pill = !record.Pill;
                Listener.RecordUpdated(record);
                PillModule();
                break;
            case R.id.activator_period:
                if(periodState == STATE_EDIT) {
                    record.Period = Math.round(((RatingBar) FragmentView.findViewById(R.id.rate_period)).getRating());
                    Listener.RecordUpdated(record);
                }
                PeriodModule(id);
                break;
            case R.id.activator_headache:
                if(headacheState == STATE_EDIT){
                    record.Headache = Math.round((((RatingBar) FragmentView.findViewById(R.id.rate_headache)).getRating()));
                    Listener.RecordUpdated(record);
                }
                HeadacheModule(id);
                break;
            case R.id.activator_coitus:
                if(coitusState == STATE_EDIT) {
                    if(record.Coitus == 0)
                        record.Coitus = 1;
                    Listener.RecordUpdated(record);
                }
                CoitusModule(id);
                break;
            case R.id.rate_coitus:
                record.Coitus = record.Coitus == 2 ? 1 : 2;
                CoitusModule(id);
                break;
            case R.id.cancel_period:
                record.Period = 0;
                Listener.RecordUpdated(record);
                PeriodModule(id);
                break;
            case R.id.cancel_headache:
                record.Headache = 0;
                Listener.RecordUpdated(record);
                HeadacheModule(id);
                break;
            case R.id.cancel_coitus:
                record.Coitus = 0;
                Listener.RecordUpdated(record);
                CoitusModule(id);
                break;
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count){
        record.Comment = s.toString();

        if(this.Listener == null)
            this.Listener = (DayFragmentListener) getActivity();

        Listener.RecordUpdated(record);
    }

    //must implement, dont need
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
    @Override
    public void afterTextChanged(Editable s) { }

    //visual setup methods for each module
    public void PillModule()
    {
        if(pillState == 0)
            pillState = record.Pill ? STATE_ON : STATE_OFF;

        switch (pillState){
            case 0:
                pillState = record.Pill ? STATE_ON : STATE_OFF;
                if(pillState == STATE_ON)
                    ((ImageButton)FragmentView.findViewById(R.id.activator_pill)).setImageResource(R.drawable.pill_on);
                else if(pillState == STATE_OFF)
                    ((ImageButton)FragmentView.findViewById(R.id.activator_pill)).setImageResource(R.drawable.pill_off);
                break;
            case 1:
                //state = OFF
                pillState = STATE_ON;
                ((ImageButton)FragmentView.findViewById(R.id.activator_pill)).setImageResource(R.drawable.pill_on);
                break;
            case 2:
                //state = ON
                pillState = STATE_OFF;
                ((ImageButton)FragmentView.findViewById(R.id.activator_pill)).setImageResource(R.drawable.pill_off);
                break;
        }
        return;
    }

    public void PeriodModule(int id)
    {
        RatingBar r = (RatingBar)FragmentView.findViewById(R.id.rate_period);

        switch (periodState){
            case 0:
                //initial showing
                periodState = record.Period > 0 ? STATE_ON : STATE_OFF;

                if(periodState == STATE_ON) {
                    ((ImageView)FragmentView.findViewById(R.id.activator_period)).setImageResource(R.drawable.droplet_on);
                    r.setRating(record.Period);
                    r.setIsIndicator(true);
                    r.setFocusable(false);
                    r.setVisibility(View.VISIBLE);
                }
                else{
                    ((ImageView)FragmentView.findViewById(R.id.activator_period)).setImageResource(R.drawable.droplet_off);
                    r.setVisibility(View.INVISIBLE);
                }

                FragmentView.findViewById(R.id.cancel_period).setVisibility(View.INVISIBLE);
                break;
            case 1:
                //state = OFF, activator vidljiv, ostalo invisible
            case 2:
                //state = ON, activator vidljiv, rating bar vidljiv  ali nepromjenjiv
                periodState = STATE_EDIT;
                ((ImageView)FragmentView.findViewById(R.id.activator_period)).setImageDrawable(null);
                ((ImageView)FragmentView.findViewById(R.id.activator_period)).setImageResource(R.drawable.module_confirm);
                r.setRating(record.Period);
                r.setIsIndicator(false);
                r.setFocusable(true);
                r.setVisibility(View.VISIBLE);
                FragmentView.findViewById(R.id.cancel_period).setVisibility(View.VISIBLE);
                break;
            case 3:
                //state = EDIT, vidljivi su activator, rating bar, cancel
                r.setIsIndicator(true);
                r.setFocusable(false);
                FragmentView.findViewById(R.id.cancel_period).setVisibility(View.INVISIBLE);

                if(id == R.id.activator_period) {
                    periodState = STATE_ON;
                    ((ImageView)FragmentView.findViewById(R.id.activator_period)).setImageResource(R.drawable.droplet_on);
                    r.setVisibility(View.VISIBLE);
                    break;
                }

                if(id == R.id.cancel_period){
                    periodState = STATE_OFF;
                    ((ImageView)FragmentView.findViewById(R.id.activator_period)).setImageResource(R.drawable.droplet_off);
                    r.setRating(0);
                    r.setVisibility(View.INVISIBLE);
                    break;
                }
        }
        return;
    }

    public void HeadacheModule(int id)
    {
        RatingBar r = (RatingBar)FragmentView.findViewById(R.id.rate_headache);

        switch (headacheState){
            case 0:
                //initial showing
                headacheState = record.Headache > 0 ? STATE_ON : STATE_OFF;

                if(headacheState == STATE_ON) {
                    ((ImageView)FragmentView.findViewById(R.id.activator_headache)).setImageResource(R.drawable.headache_on);
                    r.setRating(record.Headache);
                    r.setIsIndicator(true);
                    r.setFocusable(false);
                    r.setVisibility(View.VISIBLE);
                }
                else{
                    ((ImageView)FragmentView.findViewById(R.id.activator_headache)).setImageResource(R.drawable.headache_off);
                    r.setVisibility(View.INVISIBLE);
                }

                FragmentView.findViewById(R.id.cancel_headache).setVisibility(View.INVISIBLE);
                break;
            case 1:
                //state = OFF, activator vidljiv, ostalo invisible
            case 2:
                //state = ON, activator vidljiv, rating bar vidljiv  ali nepromjenjiv
                headacheState = STATE_EDIT;
                ((ImageView)FragmentView.findViewById(R.id.activator_headache)).setImageResource(R.mipmap.module_confirm);
                r.setRating(record.Headache);
                r.setIsIndicator(false);
                r.setFocusable(true);
                r.setVisibility(View.VISIBLE);
                FragmentView.findViewById(R.id.cancel_headache).setVisibility(View.VISIBLE);
                break;
            case 3:
                //state = EDIT, vidljivi su activator, rating bar, cancel
                r.setIsIndicator(true);
                r.setFocusable(false);
                FragmentView.findViewById(R.id.cancel_headache).setVisibility(View.INVISIBLE);

                if(id == R.id.activator_headache) {
                    headacheState = STATE_ON;
                    ((ImageView)FragmentView.findViewById(R.id.activator_headache)).setImageResource(R.drawable.headache_on);
                    r.setVisibility(View.VISIBLE);
                    break;
                }

                if(id == R.id.cancel_headache){
                    headacheState = STATE_OFF;
                    ((ImageView)FragmentView.findViewById(R.id.activator_headache)).setImageResource(R.drawable.headache_off);
                    r.setRating(0);
                    r.setVisibility(View.INVISIBLE);
                    break;
                }
        }
        return;
    }

    public void CoitusModule(int id)
    {
        ImageView r = (ImageView) FragmentView.findViewById(R.id.rate_coitus);

        switch (coitusState){
            case 0:
                //initial showing
                coitusState = record.Coitus > 0 ? STATE_ON : STATE_OFF;

                if(coitusState == STATE_ON) {
                    ((ImageView)FragmentView.findViewById(R.id.activator_coitus)).setImageResource(R.drawable.heart_on);
                    if(record.Coitus == 1)
                        r.setImageResource(R.drawable.condom_off);
                    else if(record.Coitus == 2)
                        r.setImageResource(R.drawable.condom_on);
                    r.setClickable(false);
                    r.setFocusable(false);
                    r.setVisibility(View.VISIBLE);
                }
                else{
                    ((ImageView)FragmentView.findViewById(R.id.activator_coitus)).setImageResource(R.drawable.heart_off);
                    r.setVisibility(View.INVISIBLE);
                }

                FragmentView.findViewById(R.id.cancel_coitus).setVisibility(View.INVISIBLE);
                break;
            case 1:
                //state = OFF, activator vidljiv, ostalo invisible
            case 2:
                //state = ON, activator vidljiv, rating bar vidljiv  ali nepromjenjiv
                coitusState = STATE_EDIT;
                ((ImageView)FragmentView.findViewById(R.id.activator_coitus)).setImageResource(R.mipmap.module_confirm);
                if(record.Coitus <= 1)
                    r.setImageResource(R.drawable.condom_off);
                else
                    r.setImageResource(R.drawable.condom_on);
                r.setClickable(true);
                r.setFocusable(true);
                r.setVisibility(View.VISIBLE);
                FragmentView.findViewById(R.id.cancel_coitus).setVisibility(View.VISIBLE);
                break;
            case 3:
                //state = EDIT, vidljivi su activator, rating, cancel
                if(id == R.id.activator_coitus) {
                    coitusState = STATE_ON;
                    ((ImageView)FragmentView.findViewById(R.id.activator_coitus)).setImageResource(R.drawable.heart_on);
                    r.setClickable(false);
                    r.setFocusable(false);
                    FragmentView.findViewById(R.id.cancel_coitus).setVisibility(View.INVISIBLE);
                    break;
                }

                if(id == R.id.rate_coitus) {
                    if(record.Coitus == 1)
                        r.setImageResource(R.drawable.condom_off);
                    else if(record.Coitus == 2)
                        r.setImageResource(R.drawable.condom_on);
                    break;
                }

                if(id == R.id.cancel_coitus){
                    coitusState = STATE_OFF;
                    ((ImageView)FragmentView.findViewById(R.id.activator_coitus)).setImageResource(R.drawable.heart_off);
                    r.setClickable(false);
                    r.setFocusable(false);
                    r.setVisibility(View.INVISIBLE);
                    FragmentView.findViewById(R.id.cancel_coitus).setVisibility(View.INVISIBLE);
                    break;
                }
        }
        return;
    }

    public void SetupOnClickListeners()
    {
        FragmentView.findViewById(R.id.activator_back).setOnClickListener(this);
        FragmentView.findViewById(R.id.activator_pill).setOnClickListener(this);
        FragmentView.findViewById(R.id.activator_period).setOnClickListener(this);
        FragmentView.findViewById(R.id.activator_headache).setOnClickListener(this);
        FragmentView.findViewById(R.id.activator_coitus).setOnClickListener(this);
        FragmentView.findViewById(R.id.rate_coitus).setOnClickListener(this);
        FragmentView.findViewById(R.id.cancel_period).setOnClickListener(this);
        FragmentView.findViewById(R.id.cancel_headache).setOnClickListener(this);
        FragmentView.findViewById(R.id.cancel_coitus).setOnClickListener(this);
        ((TextView) FragmentView.findViewById(R.id.comment_input)).addTextChangedListener(this);
    }
}
