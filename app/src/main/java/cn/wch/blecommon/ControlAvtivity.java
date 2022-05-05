package cn.wch.blecommon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gcssloop.widget.RockerView;

import java.text.Format;
import java.util.Vector;

public class ControlAvtivity extends GeneralBLE {
    private SeekBar angle;
    private RockerView left;
    private RockerView right;
    private TextView textView;
    private TextView textView2;

    private final String cmdSpeed = "05";
    private final String cmdTurn = "06";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_avtivity);
        getConnect();
        //bar = findViewById(R.id.seekBar_angleModify);
        left = findViewById(R.id.leftRocker);
        right = findViewById(R.id.rightRocker);
        textView = findViewById(R.id.rockText);
        textView2 = findViewById(R.id.rockText2);

        Button start = findViewById(R.id.startRock);
        angle = findViewById(R.id.seekBar_angle);
        TextView textAngle = findViewById(R.id.text_angle);

        angle.setVisibility(View.INVISIBLE);
        textAngle.setVisibility(View.INVISIBLE);
        left.setVisibility(View.INVISIBLE);
        right.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);
        textView2.setVisibility(View.INVISIBLE);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected) {
                    start.setVisibility(View.INVISIBLE);
                    left.setVisibility(View.VISIBLE);
                    right.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    textView2.setVisibility(View.VISIBLE);
                    angle.setVisibility(View.VISIBLE);
                    textAngle.setVisibility(View.VISIBLE);
//                    c = getCurrentCharacteristic(ser, tx);
//                    write(c, cmdStartEnd[0] + "0303" + cmdStartEnd[1]);
                    enableNotify(true);
                    send8Cmd(commands.Set_Mode, 3);
                    send8Cmd(commands.Get_Para, 0);
                } else Toast.makeText(ControlAvtivity.this, "蓝牙未连接！！", Toast.LENGTH_SHORT).show();
            }
        });

        angle.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                double val = (double) i / 10;
                textAngle.setText(String.format("当前平衡点角度：%.1f", val));
                send32Cmd(commands.Set_Balance, i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        left.setListener(new RockerView.RockerListener() {
            @Override
            public void callback(int i, int i1, float v) {
                if (v > 200)
                    v = 200;
                double angle = Math.toRadians(i1);
                double x = v * Math.cos(angle), y = v * Math.sin(angle);
                switch (i) {
                    case RockerView.EVENT_ACTION:
                        textView.setText(String.format("left\nangle:%d\nlength:%.1f\nx:%.1f,y:%.1f", i1, v, x, y));

                        send8Cmd(commands.Set_Angle, (int) (y / 2));
//                        byte dat[] = {(byte) (y / 2)};
//                        write( cmdStartEnd[0] + cmdSpeed);
//                        write( dat);
//                        write( cmdStartEnd[1]);
                        break;
                    case RockerView.EVENT_CLOCK:
                        if (left.getVisibility() == View.VISIBLE && v < 1) {
//                            write(cmdStartEnd[0] + cmdSpeed + "00" + cmdStartEnd[1]);
                            send8Cmd(commands.Set_Angle, 0);
                        }
                        break;
                    default:
                        break;
                }
            }
        });

        right.setListener(new RockerView.RockerListener() {
            @Override
            public void callback(int i, int i1, float v) {
                if (v > 200)
                    v = 200;
                double angle = Math.toRadians(i1);
                double x = v * Math.cos(angle), y = v * Math.sin(angle);
//                byte dat[] = {(byte) (x / 2)};
                if (i == RockerView.EVENT_ACTION) {
                    textView2.setText(String.format("right\nangle:%d\nlength:%.1f\nx:%.1f,y:%.1f", i1, v, x, y));
//                    write(cmdStartEnd[0] + cmdTurn);
//                    write( dat);
//                    write( cmdStartEnd[1]);
                    send8Cmd(commands.Set_Turn, (int) (x / 2));
                }
            }
        });
    }


    @Override
    protected void updateValueTextView(byte[] data) {
        super.updateValueTextView(data);
        if (data == null)
            return;
        int len = data.length;
        if(len!=9)
            return;
        if (data[0]==(byte) 0x97 && data[len-1]==(byte) 0xa5) {
            byte b0 = (byte) data[3];
            byte b1 = (byte) data[4];
            byte b2 = (byte) data[5];
            byte b3 = (byte) data[6];
            int tmp = b3 |
                    (b2 & 0xFF) << 8 |
                    (b1 & 0xFF) << 16 |
                    (b0 & 0xFF) << 24;
            angle.setProgress(tmp);
        }
    }

}