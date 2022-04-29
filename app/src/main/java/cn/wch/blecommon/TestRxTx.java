package cn.wch.blecommon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.touchmcu.ui.DialogUtil;

import java.util.List;
import java.util.UUID;

import cn.wch.blecommon.constant.StringConstant;
import cn.wch.blelib.WCHBluetoothManager;
import cn.wch.blelib.exception.BLELibException;
import cn.wch.blelib.utils.FormatUtil;
import cn.wch.blelib.utils.LogUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TestRxTx extends GeneralBLE {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getConnect();
        //c = getCurrentCharacteristic(ser,tx);
        setContentView(R.layout.rxtx_test);
        Button t = findViewById(R.id.tx_test);
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                write(getCurrentCharacteristic(ser, tx), "970301f0a5");
                c = getCurrentCharacteristic(ser,tx);
                sendCmd("00");
//                writeData(c);
            }
        });
        super.onCreate(savedInstanceState);
    }

}