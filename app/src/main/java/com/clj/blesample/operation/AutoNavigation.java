package com.clj.blesample.operation;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothGattCharacteristic;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
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
import com.clj.fastble.callback.BleRssiCallback;
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
    int total_rssi = 0;
    int Rssi[] ={0,0,0,0,0,0,0,0,0,0};
    int i = 0;//rssi循环
    //int flag = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_operation, null);
        initView(v);
        return v;
    }

    private void initView(View v) {
        layout_container = (LinearLayout) v.findViewById(R.id.layout_container_operation);
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
        text_axis.setMovementMethod(ScrollingMovementMethod.getInstance());
        final ImageView axis = (ImageView) view_add.findViewById(R.id.image_axis);
        final MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), R.raw.am);
        //txt.setMovementMethod(ScrollingMovementMethod.getInstance());
        //LinearLayout layout_add = (LinearLayout) view_add.findViewById(R.id.layout_add);

        //final Paint paint = new Paint();
        final Paint paint_normal = new Paint();
        final Paint paint_block = new Paint();
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.axis);
        final Bitmap tempbitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);;
        final Canvas canvas = new Canvas(tempbitmap);
        canvas.drawBitmap(bitmap, 0, 0, null);
        paint_normal.setColor(Color.BLACK);
        paint_normal.setStyle(Paint.Style.FILL);
        paint_normal.setStrokeWidth(20f);
        paint_block.setColor(Color.RED);
        paint_block.setStyle(Paint.Style.FILL);
        paint_block.setStrokeWidth(30f);

//        static int pre_x = 0;        //记录上一个点， 用于判断障碍
//        static int pre_y = 0;


        Button button0 = (Button) view_add.findViewById(R.id.button_go);
        Button button1 = (Button) view_add.findViewById(R.id.button_picture);
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
                                //total_rssi = 0;

                            if(tempx < 100 && tempy < 100 && tempx > 0 && tempy > 0) {
                                x = tempx;
                                y = tempy;
                                addText(text_axis, "(" + String.valueOf(x) + "," + String.valueOf(y) + ")");
                                //axis.draw(canvas);
//                                canvas.drawPoint(e.getX(),e.getY(),paint);
                                //for (i = 0;i < 5;) {
//                                    BleManager.getInstance().readRssi(bleDevice, new BleRssiCallback() {
//                                        @Override
//                                        public void onRssiFailure(BleException exception) {
//                                            //Log.i(TAG, "onRssiFailure" + exception.toString());
//                                            //addText(text_axis, "?");
//                                           // i --;
//                                        }
//
//                                        @Override
//                                        public void onRssiSuccess(int rssi) {
//                                            //Log.i(TAG, "onRssiSuccess: " + rssi);
//                                            //addText(text_axis, "信号强度" + String.valueOf(rssi));
//                                            total_rssi = total_rssi + Math.abs(rssi);
//                                            Rssi[i++] = Math.abs(rssi);
//
//                                            //if(i == 4){
//
//                                           // }
//                                        }
//                                    });
//                                    try {
//                                        Thread.sleep(200);
//                                    } catch (InterruptedException ex) {
//                                    }

                               // }

                            }
                            else
                                addText(text_axis, "Invalid Point");

//                            if(i == 10){
//                                double average_rssi = (double)(total_rssi / 10 );
//                                for(int j = 0;j < 10;j ++){
//                                    if((Rssi[j] > average_rssi + 3 )||(Rssi[j] < average_rssi - 3 )){
//                                        total_rssi = total_rssi -Rssi[j];
//                                        i--;
//                                    }
//                                }
//                                if(i < 5){
//                                    addText(text_axis,"Error");
//                                }
//                                else {
//                                    double power = ((double) (total_rssi / i) - 85) / 20.0;
//                                    double distance = Math.pow(10, power);
//                                    addText(text_axis, String.valueOf(i));
//                                    addText(text_axis, String.valueOf((double) (total_rssi / i)));
//                                    addText(text_axis, String.valueOf(distance));
//                                    i = 0;
//                                    total_rssi = 0;
//                                }
//                            }
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
                        addText(text_axis,"出发，驶向" + "(" + String.valueOf(x) + "," + String.valueOf(y) + ")");

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


        button1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String hex0 = "fe";
                        String hex1 = "ec";
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
                        addText(text_axis,"建图区域:(0,0) - " + "(" + String.valueOf(x) + "," + String.valueOf(y) + ")");

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
                    //int pre_x = 0;        //记录上一个点， 用于判断障碍
                    //int pre_y = 0;
                    Paint paint = new Paint(paint_normal);

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
                    public void





                    onCharacteristicChanged(byte[] data) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String signal = HexUtil.formatHexString(characteristic.getValue(), true);

                                if(signal.equals("01")) {
                                    mediaPlayer.start();
                                    Toast.makeText((OperationActivity)getActivity(),"Alarm!!!  温度过高！！！" , Toast.LENGTH_LONG).show();
                                }
                                if(signal.equals("02")) {
                                    Toast.makeText((OperationActivity)getActivity(),"解除警报" , Toast.LENGTH_LONG).show();
                                    //if (mediaPlayer.isPlaying()){
                                    if(mediaPlayer != null){
                                        mediaPlayer.stop();
                                        mediaPlayer.release();
                                    }
                                }
                                if(signal.contains("fe")) {
                                    String x = signal.substring(3,5);
                                    String y = signal.substring(6,8);

                                    addText(text_axis,"当前坐标：" + "(" + x + "," + y + ")");
                                   // addText(text_axis,"当前坐标：" + "(" + signal + ")");
                                    //if(pre_x == Integer.parseInt(x) && pre_y == Integer.parseInt(y)) {

                                        //canvas.drawPoint(Integer.parseInt(x) * 10, (70 - Integer.parseInt(y)) * 10, paint_block);
                                    //}
                                    //else {
                                    if(Integer.parseInt(x) >= 0 && (70 - Integer.parseInt(y)) >= 0) {
                                        canvas.drawPoint(Integer.parseInt(x) * 10, (70 - Integer.parseInt(y)) * 10, paint);
                                        //}
                                        axis.setImageDrawable(new BitmapDrawable(getResources(), tempbitmap));
                                        //pre_x = Integer.parseInt(x);
                                        //pre_y = Integer.parseInt(y);
                                        //pre_x = Integer.parseInt(x);
                                        //pre_y = Integer.parseInt(y);
                                    }
                                    //axis.draw(canvas);
                                    //axis.getDrawable().setHotspot(Integer.parseInt(x) * 10,(70 - Integer.parseInt(y)) * 10);
                                }
                                if(signal.equals("fb")) {

                                    addText(text_axis,"到达指定位置" + "(" + String.valueOf(x) + "," + String.valueOf(y) + ")");
                                    //flag = 0;
                                }
                                if(signal.equals("f9")){
                                    paint = new Paint(paint_block);
                                }
                                if(signal.equals("f8")){
                                    paint = new Paint(paint_normal);
                                }
                                //if(signal.contains("fa")) {
//                                    String x = signal.substring(3,5);
//                                    String y = signal.substring(6,8);
//                                    canvas.drawPoint(Integer.parseInt(x) * 10, (70 - Integer.parseInt(y)) * 10, paint_block);
//                                    axis.setImageDrawable(new BitmapDrawable(getResources(), tempbitmap));
//
//                                    //flag = 0;
//                                }

                            }
                        });
                    }
                });


        layout_container.addView(view_add);
    }

    
//    protected void onDraw(Canvas canvas,String x, String y,Paint paint) {
//        layout_container.draw(canvas);
//        canvas.drawPoint(Integer.parseInt(x) * 10,(70 - Integer.parseInt(y)) * 10,paint);
//    }
}
