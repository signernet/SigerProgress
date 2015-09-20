package com.siger.example.progresspie;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.SeekBar;


public class MainActivity extends ActionBarActivity implements SeekBar.OnSeekBarChangeListener {

    private ProgressPie mProgresPie,mProgressDefault;
    private SeekBar mSeekBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgresPie = (ProgressPie) findViewById(R.id.main_ios_progressPie);
        mProgressDefault = (ProgressPie) findViewById(R.id.main_default_progressPie);
        mSeekBar = (SeekBar) findViewById(R.id.main_seekBar);
        mSeekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mProgresPie.setProgress(progress/100.0f);
        mProgressDefault.setProgress(progress/100.0f);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
