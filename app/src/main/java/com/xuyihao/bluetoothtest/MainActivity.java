package com.xuyihao.bluetoothtest;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import dialogFolder.dialog;


/*
* created by johnson_moon on 2016/01/04
* **/
public class MainActivity extends AppCompatActivity {

    private TextView txtMessage;
    private Button btnActiveBluetooth;//激活蓝牙按钮
    private ListView deviceList;//已配对设备列表

    private BluetoothAdapter BTA;//蓝牙适配器对象
    private int REQUEST_ENABLE_BT = 0x01;//请求用户开启蓝牙的请求码
    private ArrayList<HashMap<String, Object>> mDeviceList;//储存已配对的设备名称和物理地址


    public void init(){
        txtMessage = (TextView)findViewById(R.id.textView_MainActivity_Message);
        btnActiveBluetooth = (Button)findViewById(R.id.button_MainActivity_ActiveBluetooth);
        deviceList = (ListView)findViewById(R.id.listView_MainActivity_DeviceList);

        txtMessage.setText("蓝牙未启动");
        initEventFunc();
    }

    public void initEventFunc(){
        this.btnActiveBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.ActiveBluetoothFunc();
            }
        });
        this.deviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView name = (TextView)view.findViewById(R.id.textView_deviceInfo_Name);
                TextView MacAdress = (TextView)view.findViewById(R.id.textView_deviceInfo_Mac_Adress);
                String deviceName, deviceAdress;
                deviceName = name.getText().toString().trim();
                deviceAdress = MacAdress.getText().toString().trim();
                Intent intent = new Intent(MainActivity.this, OperateActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("DN", deviceName);
                bundle.putString("DA", deviceAdress);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    public void ActiveBluetoothFunc(){

        MainActivity.this.BTA = BluetoothAdapter.getDefaultAdapter();
        if(MainActivity.this.BTA == null){
            new dialog(this, "设备不支持蓝牙!!!");
        }
        else{
            if(MainActivity.this.BTA.isEnabled()){//蓝牙启用
                MainActivity.this.txtMessage.setText("已启用，点击设备进行操作");
                new dialog(this, "蓝牙成功启用!");
                Set<BluetoothDevice> pairedDevices = this.BTA.getBondedDevices();//获取已配对蓝牙设备BluetoothDevice对象的集合
                if(pairedDevices.size() > 0){//如果有已配对的设备

                    MainActivity.this.mDeviceList = new ArrayList<HashMap<String, Object>>();
                    for(BluetoothDevice device : pairedDevices){//储存设备列表
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("deviceName", device.getName().toString().trim());
                        map.put("deviceMacAdress", device.getAddress().toString().trim());
                        MainActivity.this.mDeviceList.add(map);
                    }

                    SimpleAdapter simpleAdapter = new SimpleAdapter(
                            MainActivity.this,
                            MainActivity.this.mDeviceList,
                            R.layout.device_infomation_item,
                            new String[]{"deviceName", "deviceMacAdress"},
                            new int[]{R.id.textView_deviceInfo_Name, R.id.textView_deviceInfo_Mac_Adress}
                    );
                    MainActivity.this.deviceList.setAdapter(simpleAdapter);

                }
            }
            else{//蓝牙被禁用
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ENABLE_BT){
            switch (resultCode){
                case RESULT_OK://蓝牙已经开启
                    new dialog(this, "蓝牙成功开启!");
                case RESULT_CANCELED://The user has rejected the request or an error has occurred.
                    new dialog(this, "蓝牙未成功开启!");
            }
        }
    }
}
