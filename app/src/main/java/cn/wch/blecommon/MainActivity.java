package cn.wch.blecommon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import cn.wch.blecommon.constant.StringConstant;
import cn.wch.blecommon.ui.DeviceListDialog;
import cn.wch.blecommon.ui.MtuDialog;
import cn.wch.blelib.WCHBluetoothManager;
import cn.wch.blelib.exception.BLELibException;
import cn.wch.blelib.host.core.callback.NotifyDataCallback;
import cn.wch.blelib.host.scan.ScanObserver;
import cn.wch.blelib.host.scan.ScanRuler;
import cn.wch.blelib.utils.FormatUtil;
import cn.wch.blelib.utils.Location;
import cn.wch.blelib.utils.LogUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.suke.widget.SwitchButton;
import com.touchmcu.ui.DialogUtil;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private String carAdd = "38:3B:26:2E:3A:F2";
    private String ser = "0000ffe0-0000-1000-8000-00805f9b34fb";
    private String TxSer = "0000ffe2-0000-1000-8000-00805f9b34fb";
    private String RxSer = "0000ffe1-0000-1000-8000-00805f9b34fb";

    private void gotoNextIntent(Class<?> cls) {
        Intent intent = new Intent(MainActivity.this, cls);
        intent.putExtra("address", carAdd);
        intent.putExtra("rx", RxSer);
        intent.putExtra("tx", TxSer);
        intent.putExtra("service", ser);
        startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.main_layout);
        Button test = findViewById(R.id.go_to_test);
        Button ctrl = findViewById(R.id.go_to_pad);
        Button scope = findViewById(R.id.go_to_scope);
        Button pic = findViewById(R.id.go_to_pic);
        Button para = findViewById(R.id.go_to_para);
        ImageButton sjj = findViewById(R.id.sjjButton);

        EditText add = findViewById(R.id.ble_add);
        add.setText(carAdd);
        EditText service = findViewById(R.id.ble_ser);
        service.setText(ser);
        EditText rx = findViewById(R.id.ble_rx);
        rx.setText(RxSer);
        EditText tx = findViewById(R.id.ble_tx);
        tx.setText(TxSer);

        ImageButton re_sjj = findViewById(R.id.sjjReButton);
        ImageButton smile = findViewById(R.id.sjjSmileButton);
        re_sjj.setVisibility(View.INVISIBLE);
        smile.setVisibility(View.INVISIBLE);

        sjj.setVisibility(View.VISIBLE);
        test.setVisibility(View.INVISIBLE);
        ctrl.setVisibility(View.INVISIBLE);
        scope.setVisibility(View.INVISIBLE);
        pic.setVisibility(View.INVISIBLE);
        para.setVisibility(View.INVISIBLE);

        sjj.requestLayout();

        sjj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sjj.setVisibility(View.INVISIBLE);
                test.setVisibility(View.VISIBLE);
                ctrl.setVisibility(View.VISIBLE);
                scope.setVisibility(View.VISIBLE);
                pic.setVisibility(View.VISIBLE);
                para.setVisibility(View.VISIBLE);
                re_sjj.setVisibility(View.VISIBLE);
                smile.setVisibility(View.VISIBLE);

                add.setEnabled(false);
                service.setEnabled(false);
                rx.setEnabled(false);
                tx.setEnabled(false);
                carAdd = add.getText().toString().trim();
                ser = service.getText().toString().trim();
                RxSer = rx.getText().toString().trim();
                TxSer = tx.getText().toString().trim();
                Toast.makeText(MainActivity.this,"蓝牙参数配置完成",Toast.LENGTH_SHORT).show();
            }
        });

        re_sjj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sjj.setVisibility(View.VISIBLE);
                test.setVisibility(View.INVISIBLE);
                ctrl.setVisibility(View.INVISIBLE);
                scope.setVisibility(View.INVISIBLE);
                pic.setVisibility(View.INVISIBLE);
                para.setVisibility(View.INVISIBLE);
                re_sjj.setVisibility(View.INVISIBLE);
                smile.setVisibility(View.INVISIBLE);

                add.setEnabled(true);
                service.setEnabled(true);
                rx.setEnabled(true);
                tx.setEnabled(true);
            }
        });

        smile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"让我看看你车做的怎么样了",Toast.LENGTH_SHORT).show();
            }
        });
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoNextIntent(TestRxTx.class);
            }
        });
        ctrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoNextIntent(ControlAvtivity.class);
            }
        });
        super.onCreate(savedInstanceState);
    }
}