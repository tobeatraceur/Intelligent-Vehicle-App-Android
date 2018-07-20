package com.clj.blesample.operation;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.blesample.R;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleIndicateCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleRssiCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import android.media.MediaPlayer;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class CharacteristicOperationFragment extends Fragment {

    public static final int PROPERTY_READ = 1;
    public static final int PROPERTY_WRITE = 2;
    public static final int PROPERTY_WRITE_NO_RESPONSE = 3;
    public static final int PROPERTY_NOTIFY = 4;
    public static final int PROPERTY_INDICATE = 5;
    public static final int PROPERTY_OPERATION = 6;

    private LinearLayout layout_container;
    private List<String> childList = new ArrayList<>();

    int total_rssi = 0;
    int Rssi[] ={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    int i = 0;//rssi循环

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_characteric_operation, null);
        initView(v);
        return v;
    }

    private void initView(View v) {
        layout_container = (LinearLayout) v.findViewById(R.id.layout_container);
    }

    public void showData() {
        final BleDevice bleDevice = ((OperationActivity) getActivity()).getBleDevice();
        final BluetoothGattCharacteristic characteristic = ((OperationActivity) getActivity()).getCharacteristic();
        final int charaProp = ((OperationActivity) getActivity()).getCharaProp();
        String child = characteristic.getUuid().toString() + String.valueOf(charaProp);

        for (int i = 0; i < layout_container.getChildCount(); i++) {
            layout_container.getChildAt(i).setVisibility(View.GONE);
        }
        if (childList.contains(child)) {
            layout_container.findViewWithTag(bleDevice.getKey() + characteristic.getUuid().toString() + charaProp).setVisibility(View.VISIBLE);
        } else {
            childList.add(child);

            View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_characteric_operation, null);
            view.setTag(bleDevice.getKey() + characteristic.getUuid().toString() + charaProp);
            LinearLayout layout_add = (LinearLayout) view.findViewById(R.id.layout_add);
            final TextView txt_title = (TextView) view.findViewById(R.id.txt_title);
            //txt_title.setText(String.valueOf(characteristic.getUuid().toString() + getActivity().getString(R.string.data_changed)));
            final TextView txt = (TextView) view.findViewById(R.id.txt);
            txt.setMovementMethod(ScrollingMovementMethod.getInstance());

            switch (charaProp) {
                case PROPERTY_READ: {
                    View view_add = LayoutInflater.from(getActivity()).inflate(R.layout.layout_characteric_operation_button, null);
                    Button btn = (Button) view_add.findViewById(R.id.btn);
                    btn.setText(getActivity().getString(R.string.read));
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            BleManager.getInstance().read(
                                    bleDevice,
                                    characteristic.getService().getUuid().toString(),
                                    characteristic.getUuid().toString(),
                                    new BleReadCallback() {

                                        @Override
                                        public void onReadSuccess(final byte[] data) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    addText(txt, HexUtil.formatHexString(data, true));
                                                }
                                            });
                                        }

                                        @Override
                                        public void onReadFailure(final BleException exception) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    addText(txt, exception.toString());
                                                }
                                            });
                                        }
                                    });
                        }
                    });
                    layout_add.addView(view_add);
                }
                break;

                case PROPERTY_WRITE: {
                    View view_add = LayoutInflater.from(getActivity()).inflate(R.layout.layout_characteric_operation_et, null);
                    final EditText et = (EditText) view_add.findViewById(R.id.et);
                    Button btn = (Button) view_add.findViewById(R.id.btn);
                    btn.setText(getActivity().getString(R.string.write));
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String hex = et.getText().toString();
                            if (TextUtils.isEmpty(hex)) {
                                return;
                            }
                            BleManager.getInstance().write(
                                    bleDevice,
                                    characteristic.getService().getUuid().toString(),
                                    characteristic.getUuid().toString(),
                                    HexUtil.hexStringToBytes(hex),
                                    new BleWriteCallback() {

                                        @Override
                                        public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    addText(txt, "write success, current: " + current
                                                            + " total: " + total
                                                            + " justWrite: " + HexUtil.formatHexString(justWrite, true));
                                                }
                                            });
                                        }

                                        @Override
                                        public void onWriteFailure(final BleException exception) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    addText(txt, exception.toString());
                                                }
                                            });
                                        }
                                    }
                                    );
                        }
                    });
                    layout_add.addView(view_add);
                }
                break;

                case PROPERTY_WRITE_NO_RESPONSE: {
                    View view_add = LayoutInflater.from(getActivity()).inflate(R.layout.layout_characteric_operation_et, null);
                    final EditText et = (EditText) view_add.findViewById(R.id.et);
                    Button btn = (Button) view_add.findViewById(R.id.btn);
                    btn.setText(getActivity().getString(R.string.write));
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String hex = et.getText().toString();
                            if (TextUtils.isEmpty(hex)) {
                                return;
                            }
                            BleManager.getInstance().write(
                                    bleDevice,
                                    characteristic.getService().getUuid().toString(),
                                    characteristic.getUuid().toString(),
                                    HexUtil.hexStringToBytes(hex),
                                    new BleWriteCallback() {

                                        @Override
                                        public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    addText(txt, "write success, current: " + current
                                                            + " total: " + total
                                                            + " justWrite: " + HexUtil.formatHexString(justWrite, true));
                                                }
                                            });
                                        }

                                        @Override
                                        public void onWriteFailure(final BleException exception) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    addText(txt, exception.toString());
                                                }
                                            });
                                        }
                                    });
                        }
                    });
                    layout_add.addView(view_add);
                }
                break;

                case PROPERTY_OPERATION: {

                    String[] ListOperation = new String[]{

                            "手动操作",
                            "红外避障",
                            "巡线灭火",
                            "导航建图",
                            "跟踪监测",
                            "一键寻车",
                            "强制停止"
                    };

                    View view_add = LayoutInflater.from(getActivity()).inflate(R.layout.layout_operation, null);
                    View view_add_add = LayoutInflater.from(getActivity()).inflate(R.layout.layout_detailed_operation, null);
                    //view.setTag(bleDevice.getKey() + characteristic.getUuid().toString() + charaProp);
                    final LinearLayout layout_add_add = (LinearLayout) view_add_add.findViewById(R.id.layout_add_add);
                    //final LinearLayout layout_operation = (LinearLayout) view_add.findViewById(R.id.layout_add);
                    ListView listView_device = (ListView) view_add.findViewById(R.id.list_operation);
                    final  MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), R.raw.am);
                    final MediaPlayer didi_fast = MediaPlayer.create(getActivity(), R.raw.didi_fast);
                    final MediaPlayer didi_mid = MediaPlayer.create(getActivity(), R.raw.didi_mid);
                    final MediaPlayer didi_slow = MediaPlayer.create(getActivity(), R.raw.didi_slow);

                    final ArrayAdapter ResultAdapter = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_list_item_1,
                            ListOperation);
                    listView_device.setAdapter(ResultAdapter);
                    listView_device.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {

                            switch((short)ResultAdapter.getItemId(position)){
                                //手动操作
                                case 0:{
                                    ((OperationActivity) getActivity()).changePage(3);
                                    Toast.makeText((OperationActivity)getActivity(),"进入手动操作模式" , Toast.LENGTH_LONG).show();
                                }
                                break;

                                //红外避障
                                case 1:{
                                    BleManager.getInstance().write(
                                            bleDevice,
                                            characteristic.getService().getUuid().toString(),
                                            characteristic.getUuid().toString(),
                                            HexUtil.hexStringToBytes("0x09"),
                                            new BleWriteCallback() {

                                                @Override
                                                public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText((OperationActivity)getActivity(),"进入红外避障模式" , Toast.LENGTH_LONG).show();

                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onWriteFailure(final BleException exception) {
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            //addText(txt, exception.toString());
                                                        }
                                                    });
                                                }
                                            });
                                }
                                break;

                                //巡线灭火
                                case 2:{
                                    BleManager.getInstance().write(
                                            bleDevice,
                                            characteristic.getService().getUuid().toString(),
                                            characteristic.getUuid().toString(),
                                            HexUtil.hexStringToBytes("0x10"),
                                            new BleWriteCallback() {

                                                @Override
                                                public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText((OperationActivity)getActivity(),"进入巡线灭火模式" , Toast.LENGTH_LONG).show();

                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onWriteFailure(final BleException exception) {
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            //addText(txt, exception.toString());
                                                        }
                                                    });
                                                }
                                            });
                                }
                                break;

                                //导航
                                case 3:{
                                    ((OperationActivity) getActivity()).changePage(4);
                                    Toast.makeText((OperationActivity)getActivity(),"进入自动导航模式" , Toast.LENGTH_LONG).show();

                                }
                                break;

                                //跟踪
                                case 4:{
                                    BleManager.getInstance().write(
                                            bleDevice,
                                            characteristic.getService().getUuid().toString(),
                                            characteristic.getUuid().toString(),
                                            HexUtil.hexStringToBytes("0x11"),
                                            new BleWriteCallback() {

                                                @Override
                                                public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText((OperationActivity)getActivity(),"进入跟踪监测模式" , Toast.LENGTH_LONG).show();

                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onWriteFailure(final BleException exception) {
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            //addText(txt, exception.toString());
                                                        }
                                                    });
                                                }
                                            });
                                }
                                break;

                                case 5:{
//                                    Toast.makeText((OperationActivity)getActivity(),"进入一键寻车模式" , Toast.LENGTH_LONG).show();
//                                    while(true){
//                                        String hex0 = "fe";
//                                        String hex1 = "dc";
//                                        String hex2 ;
//                                        String hex3 ;
//
//
//                                        String hex4 = "fd";
//                                        //flag = 1;
//                                        //addText(text_axis,"建图区域:(0,0) - " + "(" + String.valueOf(x) + "," + String.valueOf(y) + ")");
//
//                                        BleManager.getInstance().write(
//                                                bleDevice,
//                                                characteristic.getService().getUuid().toString(),
//                                                characteristic.getUuid().toString(),
//                                                HexUtil.hexStringToBytes(hex0),
//                                                new BleWriteCallback() {
//
//                                                    @Override
//                                                    public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
//                                                        runOnUiThread(new Runnable() {
//                                                            @Override
//                                                            public void run() {
//                                                                //addText(text_axis, "执行成功" + HexUtil.formatHexString(justWrite, true));
//                                                            }
//                                                        });
//                                                    }
//
//                                                    @Override
//                                                    public void onWriteFailure(final BleException exception) {
//                                                        runOnUiThread(new Runnable() {
//                                                            @Override
//                                                            public void run() {
//                                                                //addText(txt, exception.toString());
//                                                            }
//                                                        });
//                                                    }
//                                                }
//                                        );
//
//                                        try {
//                                            Thread.sleep(100);
//                                        }catch(InterruptedException e){
//                                        }
//
//                                        BleManager.getInstance().write(
//                                                bleDevice,
//                                                characteristic.getService().getUuid().toString(),
//                                                characteristic.getUuid().toString(),
//                                                HexUtil.hexStringToBytes(hex1),
//                                                new BleWriteCallback() {
//
//                                                    @Override
//                                                    public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
//                                                        runOnUiThread(new Runnable() {
//                                                            @Override
//                                                            public void run() {
//                                                                //addText(text_axis, "执行成功" + HexUtil.formatHexString(justWrite, true));
//                                                            }
//                                                        });
//                                                    }
//
//                                                    @Override
//                                                    public void onWriteFailure(final BleException exception) {
//                                                        runOnUiThread(new Runnable() {
//                                                            @Override
//                                                            public void run() {
//                                                                //addText(txt, exception.toString());
//                                                            }
//                                                        });
//                                                    }
//                                                }
//                                        );
//
//                                        try {
//                                            Thread.sleep(100);
//                                        }catch(InterruptedException e){
//                                        }
//
//                                        BleManager.getInstance().write(
//                                                bleDevice,
//                                                characteristic.getService().getUuid().toString(),
//                                                characteristic.getUuid().toString(),
//                                                HexUtil.hexStringToBytes(hex2),
//                                                new BleWriteCallback() {
//
//                                                    @Override
//                                                    public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
//                                                        runOnUiThread(new Runnable() {
//                                                            @Override
//                                                            public void run() {
//                                                                //addText(text_axis, "执行成功" + HexUtil.formatHexString(justWrite, true));
//                                                            }
//                                                        });
//                                                    }
//
//                                                    @Override
//                                                    public void onWriteFailure(final BleException exception) {
//                                                        runOnUiThread(new Runnable() {
//                                                            @Override
//                                                            public void run() {
//                                                                //addText(txt, exception.toString());
//                                                            }
//                                                        });
//                                                    }
//                                                }
//                                        );
//
//                                        try {
//                                            Thread.sleep(100);
//                                        }catch(InterruptedException e){
//                                        }
//
//                                        BleManager.getInstance().write(
//                                                bleDevice,
//                                                characteristic.getService().getUuid().toString(),
//                                                characteristic.getUuid().toString(),
//                                                HexUtil.hexStringToBytes(hex3),
//                                                new BleWriteCallback() {
//
//                                                    @Override
//                                                    public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
//                                                        runOnUiThread(new Runnable() {
//                                                            @Override
//                                                            public void run() {
//                                                                //addText(text_axis, "执行成功" + HexUtil.formatHexString(justWrite, true));
//                                                            }
//                                                        });
//                                                    }
//
//                                                    @Override
//                                                    public void onWriteFailure(final BleException exception) {
//                                                        runOnUiThread(new Runnable() {
//                                                            @Override
//                                                            public void run() {
//                                                                //addText(txt, exception.toString());
//                                                            }
//                                                        });
//                                                    }
//                                                }
//                                        );
//
//                                        try {
//                                            Thread.sleep(100);
//                                        }catch(InterruptedException e){
//                                        }
//
//                                        BleManager.getInstance().write(
//                                                bleDevice,
//                                                characteristic.getService().getUuid().toString(),
//                                                characteristic.getUuid().toString(),
//                                                HexUtil.hexStringToBytes(hex4),
//                                                new BleWriteCallback() {
//
//                                                    @Override
//                                                    public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
//                                                        runOnUiThread(new Runnable() {
//                                                            @Override
//                                                            public void run() {
//                                                                //addText(text_axis, "执行成功" + HexUtil.formatHexString(justWrite, true));
//                                                            }
//                                                        });
//                                                    }
//
//                                                    @Override
//                                                    public void onWriteFailure(final BleException exception) {
//                                                        runOnUiThread(new Runnable() {
//                                                            @Override
//                                                            public void run() {
//                                                                //addText(txt, exception.toString());
//                                                            }
//                                                        });
//                                                    }
//                                                }
//                                        );
//
//                                        try {
//                                            Thread.sleep(1000);
//                                        }catch(InterruptedException e){
//                                        }
//                                    }
                                    didi_slow.start();
                                    Toast.makeText((OperationActivity)getActivity(),"进入一键寻车模式" , Toast.LENGTH_LONG).show();
                                    Timer timer = new Timer();
                                    timer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            BleManager.getInstance().readRssi(bleDevice, new BleRssiCallback() {
                                                @Override
                                                public void onRssiFailure(BleException exception) {
                                                    //Log.i(TAG, "onRssiFailure" + exception.toString());
                                                    //addText(text_axis, "?");
                                                    // i --;
                                                }

                                                @Override
                                                public void onRssiSuccess(int rssi) {
                                                    //Log.i(TAG, "onRssiSuccess: " + rssi);
                                                    //addText(text_axis, "信号强度" + String.valueOf(rssi));
                                                    total_rssi = total_rssi + Math.abs(rssi);
                                                    if(i <= 10)
                                                        Rssi[i++] = Math.abs(rssi);
                                                    else{
                                                        i = 0;
                                                        Rssi[i++] = Math.abs(rssi);
                                                    }


                                                    //if(i == 4){

                                                    // }
                                                }
                                            });


                                            if (i == 10) {
                                                double average_rssi = (double) (total_rssi / 10);
                                                for (int j = 0; j < 10; j++) {
                                                    if ((Rssi[j] > average_rssi + 3) || (Rssi[j] < average_rssi - 3)) {
                                                        total_rssi = total_rssi - Rssi[j];
                                                        i--;
                                                    }
                                                }
                                                if (i < 5) {
                                                    //addText(text_axis,"Error");
                                                    //Toast.makeText( (OperationActivity)getActivity(), "测距失败", Toast.LENGTH_LONG).show();
                                                } else {
                                                    double power = ((double) (total_rssi / i) - 85) / 20.0;
                                                    double distance = Math.pow(10, power);
//                                            addText(text_axis, String.valueOf(i));
//                                            addText(text_axis, String.valueOf((double) (total_rssi / i)));
//                                            addText(text_axis, String.valueOf(distance));
                                                    //Toast.makeText((OperationActivity)getActivity(), "当前距离：" + String.valueOf((int)distance) + "m", Toast.LENGTH_LONG).show();
                                                    if(distance <= 1){
                                                        if(didi_mid.isPlaying())
                                                            didi_mid.pause();
                                                        if(didi_slow.isPlaying())
                                                            didi_slow.pause();
                                                        if(!didi_fast.isPlaying())
                                                            didi_fast.start();
                                                    }
                                                    else if(distance <= 3){
                                                        if(didi_fast.isPlaying())
                                                            didi_fast.pause();
                                                        if(didi_slow.isPlaying())
                                                            didi_slow.pause();
                                                        if(!didi_mid.isPlaying())
                                                            didi_mid.start();
                                                    }else if(distance <= 10){
                                                        if(didi_mid.isPlaying())
                                                            didi_mid.pause();
                                                        if(didi_fast.isPlaying())
                                                            didi_fast.pause();
                                                        if(!didi_slow.isPlaying())
                                                            didi_slow.start();
                                                    }

                                                    i = 0;
                                                    total_rssi = 0;
                                                }
                                            }
                                                                }
                                            },0,100);//延时1s执行
                                    if(!didi_fast.isPlaying() && !didi_mid.isPlaying() && !didi_slow.isPlaying())
                                        didi_slow.start();


                                }
                                break;

                                case 6:{
                                    BleManager.getInstance().write(
                                            bleDevice,
                                            characteristic.getService().getUuid().toString(),
                                            characteristic.getUuid().toString(),
                                            HexUtil.hexStringToBytes("0x00"),
                                            new BleWriteCallback() {

                                                @Override
                                                public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText((OperationActivity)getActivity(),"进入跟踪监测模式" , Toast.LENGTH_LONG).show();

                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onWriteFailure(final BleException exception) {
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            //addText(txt, exception.toString());
                                                        }
                                                    });
                                                }
                                            });
                                }
                                break;

                            }

                        }
                    });
                    BleManager.getInstance().notify(
                            bleDevice,
                            characteristic.getService().getUuid().toString(),
                            characteristic.getUuid().toString(),
                            new BleNotifyCallback() {

                                @Override
                                public void onNotifySuccess() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //addText(txt, "notify success");
                                        }
                                    });
                                }

                                @Override
                                public void onNotifyFailure(final BleException exception) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //addText(txt, exception.toString());
                                        }
                                    });
                                }

                                @Override
                                public void onCharacteristicChanged(byte[] data) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //addText(txt, HexUtil.formatHexString(characteristic.getValue(), true));
                                            if(HexUtil.formatHexString(characteristic.getValue(), true).equals("01")) {
                                                //Context context = arg0.getContext();
                                                //context = context.getApplicationContext();
                                                //addText(text_operation, "Alarm!!!  温度过高！！！");

                                                mediaPlayer.start();
                                                Toast.makeText((OperationActivity)getActivity(),"Alarm!!!  温度过高！！！" , Toast.LENGTH_LONG).show();
                                            }
                                            if(HexUtil.formatHexString(characteristic.getValue(), true).equals("02")) {
                                                //Context context = arg0.getContext();
                                                //context = context.getApplicationContext();
                                                //addText(text_operation, "解除警报");
                                                //MediaPlayer mediaPlayer = MediaPlayer.create((OperationActivity)getActivity(), R.raw.daoxiang);
                                                Toast.makeText((OperationActivity)getActivity(),"解除警报" , Toast.LENGTH_LONG).show();
                                                //if (mediaPlayer.isPlaying()){
                                                if(mediaPlayer != null){
                                                    mediaPlayer.stop();
                                                    mediaPlayer.release();

                                                }

                                                if(HexUtil.formatHexString(characteristic.getValue(), true).equals("03")) {
                                                    //Context context = arg0.getContext();
                                                    //context = context.getApplicationContext();
                                                    //addText(text_operation, "解除警报");
                                                    //MediaPlayer mediaPlayer = MediaPlayer.create((OperationActivity)getActivity(), R.raw.daoxiang);
                                                    Toast.makeText((OperationActivity)getActivity(),"发现火源！" , Toast.LENGTH_LONG).show();
                                                    //if (mediaPlayer.isPlaying()){


                                                    }
                                                // mediaPlayer.stop();
                                                //}
                                            }


                                        }
                                    });
                                }
                            });
                    layout_add.addView(view_add);


                }
                break;

                case PROPERTY_NOTIFY: {
                    View view_add = LayoutInflater.from(getActivity()).inflate(R.layout.layout_characteric_operation_button, null);
                    final Button btn = (Button) view_add.findViewById(R.id.btn);
                    btn.setText(getActivity().getString(R.string.open_notification));
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (btn.getText().toString().equals(getActivity().getString(R.string.open_notification))) {
                                btn.setText(getActivity().getString(R.string.close_notification));
                                BleManager.getInstance().notify(
                                        bleDevice,
                                        characteristic.getService().getUuid().toString(),
                                        characteristic.getUuid().toString(),
                                        new BleNotifyCallback() {

                                            @Override
                                            public void onNotifySuccess() {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        addText(txt, "notify success");
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onNotifyFailure(final BleException exception) {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        addText(txt, exception.toString());
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCharacteristicChanged(byte[] data) {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        addText(txt, HexUtil.formatHexString(characteristic.getValue(), true));
                                                    }
                                                });
                                            }
                                        });
                            } else {
                                btn.setText(getActivity().getString(R.string.open_notification));
                                BleManager.getInstance().stopNotify(
                                        bleDevice,
                                        characteristic.getService().getUuid().toString(),
                                        characteristic.getUuid().toString());
                            }
                        }
                    });
                    layout_add.addView(view_add);
                }
                break;

                case PROPERTY_INDICATE: {
                    View view_add = LayoutInflater.from(getActivity()).inflate(R.layout.layout_characteric_operation_button, null);
                    final Button btn = (Button) view_add.findViewById(R.id.btn);
                    btn.setText(getActivity().getString(R.string.open_notification));
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (btn.getText().toString().equals(getActivity().getString(R.string.open_notification))) {
                                btn.setText(getActivity().getString(R.string.close_notification));
                                BleManager.getInstance().indicate(
                                        bleDevice,
                                        characteristic.getService().getUuid().toString(),
                                        characteristic.getUuid().toString(),
                                        new BleIndicateCallback() {

                                            @Override
                                            public void onIndicateSuccess() {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        addText(txt, "indicate success");
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onIndicateFailure(final BleException exception) {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        addText(txt, exception.toString());
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCharacteristicChanged(byte[] data) {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        addText(txt, HexUtil.formatHexString(characteristic.getValue(), true));
                                                    }
                                                });
                                            }
                                        });
                            } else {
                                btn.setText(getActivity().getString(R.string.open_notification));
                                BleManager.getInstance().stopIndicate(
                                        bleDevice,
                                        characteristic.getService().getUuid().toString(),
                                        characteristic.getUuid().toString());
                            }
                        }
                    });
                    layout_add.addView(view_add);
                }
                break;
            }

            layout_container.addView(view);
        }
    }

    private void runOnUiThread(Runnable runnable) {
        if (isAdded() && getActivity() != null)
            getActivity().runOnUiThread(runnable);
    }

    private void addText(TextView textView, String content) {
        textView.append(content);
        textView.append("\n");
        int offset = textView.getLineCount() * textView.getLineHeight();
        if (offset > textView.getHeight()) {
            textView.scrollTo(0, offset - textView.getHeight());
        }
    }


}
