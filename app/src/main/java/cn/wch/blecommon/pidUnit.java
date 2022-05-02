package cn.wch.blecommon;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class pidUnit extends ConstraintLayout {
    private EditTextSeekbarUnit p;
    private EditTextSeekbarUnit i;
    private EditTextSeekbarUnit d;
    private TextView t;

    public pidUnit(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.pid_unit, this);
        t = findViewById(R.id.pid_name);
        p = findViewById(R.id.kp);
//        p.setName("Kp");
        i = findViewById(R.id.ki);
//        i.setName("Ki");
        d = findViewById(R.id.kd);
//        d.setName("Kd");
    }

    public void getPara() {

    }

    public void setName(String name)
    {
        t.setText(name);
    }
}
