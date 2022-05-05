package cn.wch.blecommon;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.ByteBuffer;

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
                write(cmdStartEnd[0] + "0400");
                for (int i : pid_angle.getPara())
                    write(int16ToByteArray(i));
                write(cmdStartEnd[1]);

                write(cmdStartEnd[0] + "0401");
                for (int i : pid_speed.getPara())
                    write(c, int16ToByteArray(i));
                write(cmdStartEnd[1]);

                write(cmdStartEnd[0] + "0402");
                for (int i : pid_speed.getPara())
                    write(int16ToByteArray(i));
                write(cmdStartEnd[1]);
            }
        });

        Button get = findViewById(R.id.button_get_para);
        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableNotify(true);
                send8Cmd(commands.Get_Para, 1);
                send8Cmd(commands.Get_Para, 2);
            }
        });
    }

    ByteBuffer byteBuffer = ByteBuffer.allocate(20);

    private boolean haveCmdEnd(byte[] data) {
        int len = data.length;
        for (int i = 0; i < len - 1; i++) {
            if (data[i] == (byte) 0xf0 && data[i + 1] == (byte) 0xa5)
                return true;
        }
        return false;
    }

    @Override
    protected void updateValueTextView(byte[] data) {
        super.updateValueTextView(data);

        this.byteBuffer.put(data);
        byte[] c = byteBuffer.array();
        if (c[0] != (byte) 0x97) {
            byteBuffer.clear();
            return;
        }
        if (haveCmdEnd(data)) {
            byteBuffer.clear();
            int p[] = {0, 0, 0};
            byte b0, b1;
            switch ((int) (c[2] & 0xff)) {
                case 1:
                    b0 = (byte) c[3];
                    b1 = (byte) c[4];
                    p[0] = b1 | (b0 & 0xff) << 8;
                    b0 = (byte) c[5];
                    b1 = (byte) c[6];
                    p[1] = b1 | (b0 & 0xff) << 8;
                    b0 = (byte) c[7];
                    b1 = (byte) c[8];
                    p[2] = b1 | (b0 & 0xff) << 8;
                    pid_angle.setPara(p);
                    break;
                case 2:
                    b0 = (byte) c[3];
                    b1 = (byte) c[4];
                    p[0] = b1 | (b0 & 0xff) << 8;
                    b0 = (byte) c[5];
                    b1 = (byte) c[6];
                    p[1] = b1 | (b0 & 0xff) << 8;
                    b0 = (byte) c[7];
                    b1 = (byte) c[8];
                    p[2] = b1 | (b0 & 0xff) << 8;
                    pid_speed.setPara(p);
                    break;
            }
        }
    }
}