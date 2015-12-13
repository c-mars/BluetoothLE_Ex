package c.mars.bluetoothle.helpers;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Build;

import java.util.concurrent.TimeUnit;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Created by Constantine Mars on 12/13/15.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
@Data
public class BleHelperJellyBean extends BleHelper {

    public BleHelperJellyBean(Activity activity, ScanListener listener) {
        super(activity, listener);
        init();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void scan(boolean enable) {
        if(adapter!=null) {
            if(enable) {
                scanning = true;
                adapter.startLeScan(callback);

                rx.Observable.timer(SCAN_PERIOD, TimeUnit.SECONDS).forEach(aLong -> {
                    scanning = false;
                    adapter.stopLeScan(callback);
                    listener.onCompleted(devices);
                });
            }else {
                scanning=false;
                adapter.stopLeScan(callback);
                listener.onCompleted(devices);
            }
        }
    }

    BluetoothAdapter.LeScanCallback callback = (device, rssi, scanRecord) -> addDevice(device, rssi);
}
