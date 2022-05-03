package cn.wch.blecommon;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class EditTextSeekbarUnit extends ConstraintLayout {
    private TextView text;
    private SeekBar bar;
    private int bit_count;

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

        bit_count =ta.getInteger(R.styleable.e_s_bit_num,0);

        bar.setMax(ta.getInteger(R.styleable.e_s_max,50));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            bar.setMin(ta.getInteger(R.styleable.e_s_min,-50));
        }
        bar.setProgress(ta.getInteger(R.styleable.e_s_position,0));
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                editText.setText(String.valueOf(i/Math.pow(10,bit_count)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                bar.setProgress((int)
                        (Double.parseDouble(editText.getText().toString().trim())
                                *Math.pow(10,bit_count)));
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
