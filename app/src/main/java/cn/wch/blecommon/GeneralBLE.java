package cn.wch.blecommon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.Bundle;

import com.touchmcu.ui.DialogUtil;

import java.util.List;
import java.util.UUID;

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

public class GeneralBLE extends BLEBaseActivity {
    private List<BluetoothGattService> serviceList;
    protected String add;
    protected String ser;
    protected String rx;
    protected String tx;
    protected BluetoothGattCharacteristic c;
    protected boolean isConnected = false;
    protected final String cmdStartEnd[] = {"97", "f0a5"};

    @Override
    protected void initWidget() {

    }

    @Override
    protected void setView() {
    }

    @Override
    protected void onActivitySomeResult(int requestCode, int resultCode, @Nullable Intent data) {
    }

    @Override
    protected void onConnecting() {
        showToast("正在连接蓝牙。。。");
    }

    @Override

    protected void onDiscoverService(List<BluetoothGattService> services) {
        LogUtil.d("onDiscoverService: " + services.size());
        serviceList = services;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DialogUtil.getInstance().hideLoadingDialog();
            }
        });
    }

    @Override

    protected void onConnectSuccess() {
        isConnected = true;
    }

    @Override
    protected void onConnectError(String message) {
    }

    @Override
    protected void onDisconnect() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected BluetoothGattCharacteristic getCurrentCharacteristic(String serviceUUID, String charUUID) {
        UUID uuid1 = UUID.fromString(serviceUUID);
        UUID uuid2 = UUID.fromString(charUUID);

        for (BluetoothGattService service : serviceList) {
            if (service.getUuid().toString().equalsIgnoreCase(uuid1.toString())) {
                return service.getCharacteristic(uuid2);
            }
        }
        return null;
    }

    protected void writeData(BluetoothGattCharacteristic characteristic) {
        try {
            byte[] bytes = null;
            String s = "970301f0a5";
            if (!s.matches("([0-9|a-f|A-F]{2})*")) {
                showToast("发送内容不符合HEX规范");
                return;
            }
            bytes = FormatUtil.hexStringToBytes(s);

            write(characteristic, bytes);
        } catch (Exception e) {
            LogUtil.d(e.getMessage());
        }
    }

    protected void write(BluetoothGattCharacteristic characteristic, String data) {
        byte d[] = FormatUtil.hexStringToBytes(data);
        write(characteristic, d);
    }

    protected void write(BluetoothGattCharacteristic characteristic, byte[] data) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<String> emitter) throws Exception {
                try {
                    int write = WCHBluetoothManager.getInstance().write(characteristic, data, data.length);
                    if (write < 0) {
                        emitter.onError(new Throwable("发送失败"));
                    } else if (write == data.length) {
                        emitter.onComplete();
                    } else {
                        emitter.onComplete();
                    }

                } catch (BLELibException e) {
                    emitter.onError(new Throwable(e.getMessage()));
                }

            }
        }).subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull String s) {
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
//                        tvSendCount.setText(String.format(Locale.getDefault(), "%d 字节", count_W));
                    }
                });
    }

    protected void updateValueTextView(byte[] data) {
    }

    private void read(BluetoothGattCharacteristic characteristic) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<String> emitter) throws Exception {
                try {
                    byte[] read = WCHBluetoothManager.getInstance().read(characteristic, true);

                    updateValueTextView(read);
                } catch (Exception e) {
                    emitter.onError(new Throwable(e.getMessage()));
                }
                emitter.onComplete();

            }
        }).subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                        DialogUtil.getInstance().showLoadingDialog(GeneralBLE.this, "正在读取");
                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull String s) {

                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        DialogUtil.getInstance().hideLoadingDialog();
                    }

                    @Override
                    public void onComplete() {
                        DialogUtil.getInstance().hideLoadingDialog();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        try {
            WCHBluetoothManager.getInstance().disconnect(false);
        } catch (BLELibException e) {
            e.printStackTrace();
        }
        super.onBackPressed();
    }

    protected void getConnect() {
        Intent i = getIntent();
        add = i.getStringExtra("address");
        ser = i.getStringExtra("service");
        rx = i.getStringExtra("rx");
        tx = i.getStringExtra("tx");
        connectBLE(add);
    }

    protected void sendCmd(String name) {
        write(c, cmdStartEnd[0] + name + cmdStartEnd[1]);
    }

    protected void send8Cmd(String name, int val) {
        write(c, cmdStartEnd[0] + name);
        byte tmp[] = {(byte) val};
        write(c, tmp);
        write(c, cmdStartEnd[1]);
    }

    protected void send16Cmd(String name, int val) {
        write(c, cmdStartEnd[0] + name);
        write(c, int16ToByteArray(val));
        write(c, cmdStartEnd[1]);
    }

    protected void send32Cmd(String name, int val) {
        write(c, cmdStartEnd[0] + name);
        write(c, int32ToByteArray(val));
        write(c, cmdStartEnd[1]);
    }

    protected byte[] int16ToByteArray(int a) {
        return new byte[]{
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

    protected byte[] int32ToByteArray(int a) {
        return new byte[]{
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }
}