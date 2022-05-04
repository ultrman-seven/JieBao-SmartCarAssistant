package cn.wch.blecommon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.Bundle;

import com.touchmcu.ui.DialogUtil;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import cn.wch.blelib.WCHBluetoothManager;
import cn.wch.blelib.exception.BLELibException;
import cn.wch.blelib.host.core.callback.NotifyDataCallback;
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
    private BluetoothGattCharacteristic readService;
    private BluetoothGattCharacteristic sendService;
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

    NotifyDataCallback notifyDataCallback = new NotifyDataCallback() {
        @Override
        public void OnError(String mac, Throwable t) {
            LogUtil.d(t.getMessage());
        }

        @Override
        public void OnData(String mac, byte[] data) {
            updateValueTextView(data);
        }
    };

    protected void enableNotify(boolean enable) {
        enableNotify(readService, enable);
    }

    private void enableNotify(BluetoothGattCharacteristic characteristic, boolean enable) {
        LogUtil.d("改变通知: " + enable);
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<String> emitter) throws Exception {

                try {
                    if (enable) {
                        boolean b = WCHBluetoothManager.getInstance().openNotify(characteristic, notifyDataCallback);
                        if (!b) {
                            emitter.onError(new Throwable("打开通知失败"));
                            return;
                        }
                    } else {
                        boolean b = WCHBluetoothManager.getInstance().closeNotify(characteristic);
                        if (!b) {
                            emitter.onError(new Throwable("关闭通知失败"));
                            return;
                        }
                    }
                } catch (BLELibException e) {
                    emitter.onError(new Throwable(e));
                    return;
                }

                emitter.onComplete();

            }
        }).subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                        DialogUtil.getInstance().showLoadingDialog(GeneralBLE.this, "正在改变通知");
                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull String s) {

                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        DialogUtil.getInstance().hideLoadingDialog();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        DialogUtil.getInstance().hideLoadingDialog();
                    }
                });
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

    protected void write(String data) {
        byte d[] = FormatUtil.hexStringToBytes(data);
        write(sendService, d);
    }

    protected void write(byte[] data) {
        write(sendService, data);
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

    protected void read(BluetoothGattCharacteristic characteristic) {
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

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                sendService = getCurrentCharacteristic(ser, tx);
                readService = getCurrentCharacteristic(ser, rx);
                //enableNotify(true);
            }
        };

        timer.schedule(task, 5000);

    }

    protected enum commands {
        hello, Get_Mpu6050,
        Get_Pic, Set_Mode,
        Para_Adj, Set_Angle,
        Set_Turn, Set_Balance, Get_Para
    }

    private final byte cmdStart = (byte) 0x97;
    private final byte cmdEnd[] = {(byte)0xf0,(byte)0xa5};
    protected void sendCmd(commands c){
        byte tmp[]={cmdStart,(byte) c.ordinal(),cmdEnd[0],cmdEnd[1]};
        write(tmp);
    }

    protected void sendCmd(String name) {
        write(cmdStartEnd[0] + name + cmdStartEnd[1]);
    }

    protected void send8Cmd(commands c, int val) {
        byte tmp[] = {cmdStart,(byte) c.ordinal(), (byte) val,cmdEnd[0],cmdEnd[1]};
        write(tmp);
    }

    protected void send8Cmd(String name, int val) {
        write(cmdStartEnd[0] + name);
        byte tmp[] = {(byte) val};
        write(tmp);
        write(cmdStartEnd[1]);
    }

    protected void send16Cmd(commands c, int val)
    {
        byte tmp[]={cmdStart,(byte) c.ordinal()};
        write(tmp);
        write(int16ToByteArray(val));
        write(cmdEnd);
    }
    protected void send16Cmd(String name, int val) {
        write(cmdStartEnd[0] + name);
        write(int16ToByteArray(val));
        write(cmdStartEnd[1]);
    }

    protected void send32Cmd(commands c, int val)
    {
        byte tmp[]={cmdStart,(byte) c.ordinal()};
        write(tmp);
        write(int32ToByteArray(val));
        write(cmdEnd);
    }
    protected void send32Cmd(String name, int val) {
        write(cmdStartEnd[0] + name);
        write(int32ToByteArray(val));
        write(cmdStartEnd[1]);
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