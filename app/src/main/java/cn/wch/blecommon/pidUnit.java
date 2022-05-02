package cn.wch.blecommon;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.constraintlayout.widget.ConstraintLayout;

public class pidUnit extends ConstraintLayout {
    public pidUnit(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        LayoutInflater.from(context).inflate(R.layout.pid_unit,this);
    }
}
