package cn.wch.blecommon;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
                switch (i) {
                    case R.id.radioButton_angle:
                        send8Cmd(commands.Para_Adj, 2);
                        break;
                    case R.id.radioButton_pwm:
                        send8Cmd(commands.Para_Adj, 0);
                        break;
                    case R.id.radioButton_speed:
                        send8Cmd(commands.Para_Adj, 1);
                        break;
                    default:
                        break;
                }
            }
        });


        Button setPara = findViewById(R.id.button_para);
        setPara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                write(cmdStartEnd[0]+"0400");
                for(int i:pid_angle.getPara())
                    write(int16ToByteArray(i));
                write(cmdStartEnd[1]);

                write(cmdStartEnd[0]+"0401");
                for(int i:pid_speed.getPara())
                    write(c,int16ToByteArray(i));
                write(cmdStartEnd[1]);

                write(cmdStartEnd[0]+"0402");
                for(int i:pid_speed.getPara())
                    write(int16ToByteArray(i));
                write(cmdStartEnd[1]);
            }
        });
    }
}