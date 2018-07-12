package com.clj.blesample.operation;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
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

import com.clj.blesample.R;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;



@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class ManulOperation extends Fragment{
    private LinearLayout layout_container;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_direction_operation, null);
        initView(v);
        return v;
    }

    private void initView(View v) {
        layout_container = (LinearLayout) v.findViewById(R.id.layout_container_direction);
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

    public void showData() {
        final BleDevice bleDevice = ((OperationActivity) getActivity()).getBleDevice();
        final BluetoothGattCharacteristic characteristic = ((OperationActivity) getActivity()).getCharacteristic();
        final View view_add = LayoutInflater.from(getActivity()).inflate(R.layout.layout_direction_operation, null);
        //final TextView txt = (TextView) view_add.findViewById(R.id.txt);
        //txt.setMovementMethod(ScrollingMovementMethod.getInstance());
        //LinearLayout layout_add = (LinearLayout) view_add.findViewById(R.id.layout_add);

        Button button0 = (Button) view_add.findViewById(R.id.button0);
        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hex = "0x00";
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
                                        //addText(txt, "执行成功");
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
        });

        Button button1 = (Button) view_add.findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hex = "0x01";
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
                                        //addText(txt, "执行成功");
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
        });

        Button button2 = (Button) view_add.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hex = "0x02";
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
                                        //addText(txt, "执行成功");
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
        });

        Button button3 = (Button) view_add.findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hex = "0x03";
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
                                        //addText(txt, "执行成功");
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
        });

        Button button4 = (Button) view_add.findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hex = "0x04";
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
                                        //addText(txt, "执行成功");
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
        });

        Button button5 = (Button) view_add.findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hex = "0x05";
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
                                        //addText(txt, "执行成功");
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
        });

        Button button6 = (Button) view_add.findViewById(R.id.button6);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hex = "0x06";
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
                                        //addText(txt, "执行成功");
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
        });

        Button button7 = (Button) view_add.findViewById(R.id.button7);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hex = "0x07";
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
                                        //addText(txt, "执行成功");
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
        });

        Button button8 = (Button) view_add.findViewById(R.id.button8);
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hex = "0x08";
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
                                        //addText(txt, "执行成功");
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
        });
        layout_container.addView(view_add);
        //layout_add.addView(view_add);
    }

}
