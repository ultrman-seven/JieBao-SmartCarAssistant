package cn.wch.blecommon;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class EditTextSeekbarUnit extends ConstraintLayout {
    private TextView text;
    private SeekBar bar;

    public EditTextSeekbarUnit(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public EditTextSeekbarUnit(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.edit_text_seekbar, this);

        TypedArray ta = context.obtainStyledAttributes(attributeSet, R.styleable.e_s);
        EditText editText = findViewById(R.id.unit_edit);
        bar = findViewById(R.id.unit_bar);
        text = findViewById(R.id.unit_text);

        text.setText(ta.hasValue(R.styleable.e_s_edit_txt)
                ? ta.getString(R.styleable.e_s_edit_txt) : "k");

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                editText.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void setName(String s) {
        text.setText(s);
    }

    public int getVal() {
        int val;
        val = bar.getProgress();
        return val;
    }
}
