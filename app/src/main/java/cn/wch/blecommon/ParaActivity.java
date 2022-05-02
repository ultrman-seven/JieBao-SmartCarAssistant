package cn.wch.blecommon;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

public class ParaActivity extends GeneralBLE {

    private pidUnit pid_angle;
    private pidUnit pid_speed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_para);
        getConnect();
        pid_angle = findViewById(R.id.pid_angle);
        pid_speed = findViewById(R.id.pid_speed);
        pid_angle.setName("角度环参数");
        pid_speed.setName("速度环参数");
        RadioGroup modeSelect = findViewById(R.id.mode_group);
        modeSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

            }
        });


        Button setPara = findViewById(R.id.button_para);
        setPara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = getCurrentCharacteristic(ser,tx);
                write(c,cmdStartEnd[0]+"0400");
                write(c,int16ToByteArray(10));
                write(c,int16ToByteArray(-5));
                write(c,int16ToByteArray(6));
                write(c,cmdStartEnd[1]);
            }
        });
    }
}