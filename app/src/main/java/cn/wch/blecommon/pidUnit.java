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
        i = findViewById(R.id.ki);
        d = findViewById(R.id.kd);
    }

    public int[] getPara() {
        int r[]= {p.getVal(),i.getVal(),d.getVal()};
        return r;
    }

    public void setPara(int p1,int p2,int p3){
        p.setVal(p1);
        i.setVal(p2);
        d.setVal(p3);
    }
    public void setPara(int[] para){
        p.setVal(para[0]);
        i.setVal(para[1]);
        d.setVal(para[2]);
    }

    public void setName(String name)
    {
        t.setText(name);
    }
}
