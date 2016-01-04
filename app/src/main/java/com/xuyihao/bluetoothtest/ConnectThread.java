package com.xuyihao.bluetoothtest;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by jonson_moon on 2016/1/4.
 */
public class ConnectThread extends Thread{

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
            temp = (BluetoothSocket)m.invoke(mBluetoothDevice, 1);
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
    }

    @Override
    public void run() {
        connecting = true;
        connected = false;
        if(mBluetoothAdapter == null){
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(macAdress);
        mBluetoothAdapter.cancelDiscovery();
        initSocket();
        while(!connected && connectTime <= 10){
            try{
                socket.connect();
                connected = true;
            }catch (IOException e1){
                connectTime++;
                connected = false;
                try{
                    socket.close();
                    socket = null;
                }catch (IOException e2){
                    Log.e("ConnectThread", "Socket", e2);
                }
            }finally {
                connecting = false;
            }
        }
        //super.run();
    }
}
