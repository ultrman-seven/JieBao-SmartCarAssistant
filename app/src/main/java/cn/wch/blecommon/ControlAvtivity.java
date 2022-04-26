package cn.wch.blecommon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.SeekBar;

public class ControlAvtivity extends GeneralBLE {
    private SeekBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_avtivity);
        getConnect();
        bar = findViewById(R.id.seekBar_angleModify);

    }
}