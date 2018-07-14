package com.clj.blesample.operation;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothGattCharacteristic;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.blesample.R;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;

import static java.lang.Thread.sleep;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class AutoNavigation extends Fragment{
    private LinearLayout layout_container;
    int x = 0;
    int y = 0;
    //int flag = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_auto_navigation, null);
        initView(v);
        return v;
    }

    private void initView(View v) {
        layout_container = (LinearLayout) v.findViewById(R.id.layout_container_auto_navigation);
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

    @SuppressLint("ClickableViewAccessibility")
    public void showData() {
        final BleDevice bleDevice = ((OperationActivity) getActivity()).getBleDevice();
        final BluetoothGattCharacteristic characteristic = ((OperationActivity) getActivity()).getCharacteristic();
        final View view_add = LayoutInflater.from(getActivity()).inflate(R.layout.layout_auto_navigation, null);
        final TextView text_axis = (TextView) view_add.findViewById(R.id.text_axis);
        final ImageView axis = (ImageView) view_add.findViewById(R.id.image_axis);
        //txt.setMovementMethod(ScrollingMovementMethod.getInstance());
        //LinearLayout layout_add = (LinearLayout) view_add.findViewById(R.id.layout_add);


        Button button0 = (Button) view_add.findViewById(R.id.button_go);
//        axis.setOnClickListener(new View.OnClickListener() {
//            public boolean onSingleTapUp(MotionEvent e) {
//                x = (int)e.getX();
//                y = (int)e.getY();
//                addText(text_axis,"(" + String.valueOf(x) + "," + String.valueOf(y) + ")");
//                return false;
//            }
        axis.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent e) {
                        //if(flag == 0) {
                            int tempx = (int) (e.getX() / 10);
                            int tempy = (int) (70 - e.getY() / 10);
                            if(tempx < 100 && tempy < 100 && tempx > 0 && tempy > 0){
                                x = tempx;
                                y = tempy;
                                addText(text_axis, "(" + String.valueOf(x) + "," + String.valueOf(y) + ")");
                            }
                            else
                                addText(text_axis, "Invalid Point");
                        //}
                        return false;
                    }
                }
        );

        button0.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String hex0 = "fe";
                        String hex1 = "fc";
                        String hex2 ;
                        String hex3 ;
                        if(x < 10)
                            hex2 = "0" + String.valueOf(x);
                        else
                            hex2 = String.valueOf(x);

                        if(y < 10)
                            hex3 = "0" + String.valueOf(y);
                        else
                            hex3 = String.valueOf(y);

                        String hex4 = "fd";
                        //flag = 1;
                        addText(text_axis,"出发");

                        BleManager.getInstance().write(
                                bleDevice,
                                characteristic.getService().getUuid().toString(),
                                characteristic.getUuid().toString(),
                                HexUtil.hexStringToBytes(hex0),
                                new BleWriteCallback() {

                                    @Override
                                    public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //addText(text_axis, "执行成功" + HexUtil.formatHexString(justWrite, true));
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
                                }
                        );

                        try {
                            Thread.sleep(100);
                        }catch(InterruptedException e){
                        }

                        BleManager.getInstance().write(
                                bleDevice,
                                characteristic.getService().getUuid().toString(),
                                characteristic.getUuid().toString(),
                                HexUtil.hexStringToBytes(hex1),
                                new BleWriteCallback() {

                                    @Override
                                    public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //addText(text_axis, "执行成功" + HexUtil.formatHexString(justWrite, true));
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
                                }
                        );

                        try {
                            Thread.sleep(100);
                        }catch(InterruptedException e){
                        }

                        BleManager.getInstance().write(
                                bleDevice,
                                characteristic.getService().getUuid().toString(),
                                characteristic.getUuid().toString(),
                                HexUtil.hexStringToBytes(hex2),
                                new BleWriteCallback() {

                                    @Override
                                    public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //addText(text_axis, "执行成功" + HexUtil.formatHexString(justWrite, true));
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
                                }
                        );

                        try {
                            Thread.sleep(100);
                        }catch(InterruptedException e){
                        }

                        BleManager.getInstance().write(
                                bleDevice,
                                characteristic.getService().getUuid().toString(),
                                characteristic.getUuid().toString(),
                                HexUtil.hexStringToBytes(hex3),
                                new BleWriteCallback() {

                                    @Override
                                    public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //addText(text_axis, "执行成功" + HexUtil.formatHexString(justWrite, true));
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
                                }
                        );

                        try {
                            Thread.sleep(100);
                        }catch(InterruptedException e){
                        }

                        BleManager.getInstance().write(
                                bleDevice,
                                characteristic.getService().getUuid().toString(),
                                characteristic.getUuid().toString(),
                                HexUtil.hexStringToBytes(hex4),
                                new BleWriteCallback() {

                                    @Override
                                    public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //addText(text_axis, "执行成功" + HexUtil.formatHexString(justWrite, true));
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
                                }
                        );
                    }
                }
        );


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

                                    if(HexUtil.formatHexString(characteristic.getValue(), true).equals("fb")) {

                                        addText(text_axis,"到达指定位置" + "(" + String.valueOf(x) + "," + String.valueOf(y) + ")");
                                        //flag = 0;
                                    }
                                }
                            });
                        }
                    });

        layout_container.addView(view_add);
    }

}
