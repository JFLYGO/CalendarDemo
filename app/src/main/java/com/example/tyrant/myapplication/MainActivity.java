package com.example.tyrant.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private MyCalendar mc;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SparseBooleanArray array = new SparseBooleanArray();
        array.put(1, true);
        array.put(11, true);
        array.put(22, true);
        array.put(15, true);
        array.put(17, true);
        array.put(0, true);
        mc = new MyCalendar(this);
        setContentView(R.layout.activity_main);
        mc = (MyCalendar) findViewById(R.id.mc);
        mc.setCheckMap(array);
        mc.setOnDateSelectionListener(new MyCalendar.OnDateSelectionListener() {
            @Override
            public void onDateSelect(String date) {
                if (toast == null) {
                    toast = Toast.makeText(MainActivity.this, date, Toast.LENGTH_SHORT);
                } else {
                    toast.setText(date);
                }
                toast.show();
            }
        });
    }
}
