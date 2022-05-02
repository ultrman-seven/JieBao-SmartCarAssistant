package cn.wch.blecommon;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class EditTextSeekbarUnit extends ConstraintLayout {
    public EditTextSeekbarUnit(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        LayoutInflater.from(context).inflate(R.layout.edit_text_seekbar,this);
        SeekBar bar = findViewById(R.id.unit_bar);
        EditText editText = findViewById(R.id.unit_edit);
        TextView text = findViewById(R.id.unit_text);

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
}
