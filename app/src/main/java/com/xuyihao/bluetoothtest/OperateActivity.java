package com.xuyihao.bluetoothtest;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import dialogFolder.dialog;

public class OperateActivity extends AppCompatActivity {

    private String deviceName;
    private String deviceAdress;

    private ConnectThread connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operate);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        this.deviceName = bundle.getString("DN");
        this.deviceAdress = bundle.getString("DA");
        connect = new ConnectThread(deviceAdress);
        connect.start();
        /*if(connect.connected){
            new dialog(OperateActivity.this, "连接成功!");
        }
        else {
            new dialog(OperateActivity.this, "连接失败!");
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_operate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class ConnectThread extends Thread{

        int port = 1;

        private String macAdress = "";
        private boolean connecting;
        private boolean connected;
        private BluetoothAdapter mBluetoothAdapter = null;
        private BluetoothDevice mBluetoothDevice;
        private BluetoothSocket socket;
        private int connectTime = 0;

        public ConnectThread(String mac){
            macAdress = mac;
        }

        private void initSocket(){
            BluetoothSocket temp = null;
            try{
                Method m = mBluetoothDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
                temp = (BluetoothSocket)m.invoke(mBluetoothDevice, port);
            }catch (SecurityException e){
                e.printStackTrace();
            }catch (NoSuchMethodException e){
                e.printStackTrace();
            }catch (IllegalArgumentException e){
                e.printStackTrace();
            }catch (IllegalAccessException e){
                e.printStackTrace();
            }catch (InvocationTargetException e){
                e.printStackTrace();
            }
            socket = temp;
            port++;
            if(port == 31){
                port = 1;
            }
        }

        @Override
        public void run() {
            connecting = true;
            connected = false;
            if(mBluetoothAdapter == null){
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            }
            mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(macAdress);

            while(!connected && connectTime < 3000){

                initSocket();

                try{
                    socket.connect();
                    connected = true;
                    if(connected){
                        new dialog(OperateActivity.this, "连接成功！");
                    }
                }catch (IOException e1){
                    connectTime++;
                    connected = false;
                    try{
                        socket.close();
                    }catch (IOException closeException){
                        return;
                    }
                }finally {
                    connecting = false;
                }
            }
        }
    }


}
