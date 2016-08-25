package skenja.menstrualnikalendar.StatsActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Exchanger;

import skenja.menstrualnikalendar.R;
import skenja.menstrualnikalendar.Repositories.RecordRepository;
import skenja.menstrualnikalendar.model.RecordDTO;

public class activity_stats extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Toast.makeText(this, "Računamo! Potrajat će!", Toast.LENGTH_LONG).show();

        calculateTheMassOfTheEarth();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    public void onClickMI(MenuItem mi) {
        onBackPressed();
    }

    public void calculateTheMassOfTheEarth() {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        List<RecordDTO> list = RecordRepository.GetAll();

                        int currentLength = 0;
                        int maxLength = 0;
                        int minLength = 100;
                        int sumPeriodIntensity = 0;
                        int sumHeadacheIntensity = 0;
                        int sumLengths = 0;
                        int nPeriodIntensities = 0;
                        int nHeadacheIntensities = 0;
                        int nPeriods = 0;
                        int nHeadacheDuring = 0;
                        int nHeadacheOut = 0;
                        boolean period = false;

                        for (RecordDTO record: list) {
                            if(record.Period > 0) {
                                sumPeriodIntensity += record.Period;
                                nPeriodIntensities ++;

                                if(record.Headache > 0) {
                                    nHeadacheDuring++;
                                    sumHeadacheIntensity += record.Headache;
                                    nHeadacheIntensities ++;
                                }

                                if (!period) {
                                    period = true;
                                    currentLength = 0;
                                }
                                currentLength++;
                            }
                            else{
                                if(record.Headache > 0){
                                    nHeadacheOut++;
                                    sumHeadacheIntensity += record.Headache;
                                    nHeadacheIntensities ++;
                                }

                                if(period){
                                    period = false;
                                    sumLengths += currentLength;
                                    nPeriods ++;
                                    if(currentLength > maxLength)
                                        maxLength = currentLength;
                                    if(currentLength < minLength)
                                        minLength = currentLength;
                                }
                            }
                        }

                        final float a = (float) sumLengths / nPeriods;
                        final View v = findViewById(R.id.stat_avg);
                        v.post(new Runnable() {
                            @Override
                            public void run() {
                                ((TextView) v).setText(trunc(a, 2) + " dan/a");
                            }
                        });

                        final int a2 = maxLength;
                        final View v2 = findViewById(R.id.stat_max);
                        v2.post(new Runnable() {
                            @Override
                            public void run() {
                                ((TextView) v2).setText(a2 + " dan/a");
                            }
                        });

                        final int a3 = minLength;
                        final View v3 = findViewById(R.id.stat_min);
                        v3.post(new Runnable() {
                            @Override
                            public void run() {
                                ((TextView) v3).setText(a3 + " dan/a");
                            }
                        });

                        final float a4 = (float) sumPeriodIntensity / nPeriodIntensities;
                        final View v4 = findViewById(R.id.stat_avg_period_intensity);
                        v4.post(new Runnable() {
                            @Override
                            public void run() {
                                ((TextView) v4).setText(trunc(a4, 2) + " / 3");
                            }
                        });

                        final float a5 = ((float) nHeadacheDuring / (nHeadacheDuring + nHeadacheOut)) * 100;
                        final View v5 = findViewById(R.id.stat_headache_during_period);
                        v5.post(new Runnable() {
                            @Override
                            public void run() {
                                ((TextView) v5).setText(trunc(a5, 2) + "%");
                            }
                        });

                        final float a6 = (float) sumHeadacheIntensity / nHeadacheIntensities;
                        final View v6 = findViewById(R.id.stat_avg_headache_intensity);
                        v6.post(new Runnable() {
                            @Override
                            public void run() {
                                ((TextView) v6).setText(trunc(a6, 2) + " / 3");
                            }
                        });
                    }
                }
        ).run();
    }

    private static BigDecimal trunc(double x, int numberofDecimals)    {
        if ( x > 0) {
            return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_FLOOR);
        } else {
            return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_CEILING);
        }
    }
}
