package com.example.user.project1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public List<WiFi> myWifi = new ArrayList<WiFi>();
    ArrayList<WiFi> wifis = new ArrayList<WiFi>();
    public WifiManager myWiFiManager;
    MyListAdapter adapter;
    ListView listWifi;
    Button btScanWifi, btdistance;
    public WifiManager wifiManager;
    WifiScanReceiver wifiScanReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // add mywifi1
        WiFi myWiFi1 = new WiFi();
        myWiFi1.setSSID("CafeNhaGo");
        myWiFi1.x = 17.8;
        myWiFi1.y = 11.6;
        myWiFi1.z = 3.5;
        myWifi.add(myWiFi1);
        // add mywifi2
        WiFi myWiFi2 = new WiFi();
        myWiFi2.setSSID("CafeNhaGo_Lau1");
        myWiFi2.x = 0.2;
        myWiFi2.y = 11.6;
        myWiFi2.z = 3.5;
        myWifi.add(myWiFi2);
        // add mywifi3
        WiFi myWiFi3 = new WiFi();
        myWiFi3.setSSID("ReWa_NVQ");
        myWiFi3.x = 16.2;
        myWiFi3.y = 0.2;
        myWiFi3.z = 3.5;
        myWifi.add(myWiFi3);
        // add mywifi4
        WiFi myWiFi4 = new WiFi();
        myWiFi4.setSSID("Ngan Cuong");
        myWiFi4.x = 0.2;
        myWiFi4.y = 11.6;
        myWiFi4.z = 3.5;
        myWifi.add(myWiFi4);
        // add mywifi5
        WiFi myWiFi5 = new WiFi();
        myWiFi5.setSSID("TelecomAP2");
        myWiFi5.x = 17;
        myWiFi5.y = 12;
        myWiFi5.z = 3.5;
        myWifi.add(myWiFi5);

        // add mywifi6
        WiFi myWiFi6 = new WiFi();
        myWiFi6.setSSID("DIRECT-ol-BRAVIA");
        myWiFi6.x = 18;
        myWiFi6.y = 0.2;
        myWiFi6.z = 0;
        myWifi.add(myWiFi6);
        // add mywifi6
        WiFi myWiFi7 = new WiFi();
        myWiFi7.setSSID("CH_NET");
        myWiFi7.x = 18;
        myWiFi7.y = 0.2;
        myWiFi7.z = 0;
        myWifi.add(myWiFi7);

        WiFi myWiFi8 = new WiFi();
        myWiFi8.setSSID("Thay Co");
        myWiFi8.x = 18;
        myWiFi8.y = 0.2;
        myWiFi8.z = 0;
        myWifi.add(myWiFi8);

        WiFi myWiFi9 = new WiFi();
        myWiFi9.setSSID("hmtri");
        myWiFi9.x = 18;
        myWiFi9.y = 0.2;
        myWiFi9.z = 0;
        myWifi.add(myWiFi9);

        WiFi myWiFi10 = new WiFi();
        myWiFi10.setSSID("TP-LINK_B371CA");
        myWiFi10.x = 18;
        myWiFi10.y = 0.2;
        myWiFi10.z = 0;
        myWifi.add(myWiFi10);

        WiFi myWiFi11 = new WiFi();
        myWiFi11.setSSID("Tenda_351C90");
        myWiFi11.x = 18;
        myWiFi11.y = 0.2;
        myWiFi11.z = 0;
        myWifi.add(myWiFi11);

        WiFi myWiFi12 = new WiFi();
        myWiFi12.setSSID("Rang Su");
        myWiFi12.x = 18;
        myWiFi12.y = 0.2;
        myWiFi12.z = 0;
        myWifi.add(myWiFi12);

        WiFi myWiFi13 = new WiFi();
        myWiFi13.setSSID("Van Thanh L4");
        myWiFi13.x = 18;
        myWiFi13.y = 0.2;
        myWiFi13.z = 0;
        myWifi.add(myWiFi13);


        // Anh xa su kien
        btScanWifi = (Button) findViewById(R.id.btnscanwifi);
        btdistance = (Button) findViewById(R.id.btndistance);
        listWifi = (ListView) findViewById(R.id.wifiListview);
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiScanReceiver = new WifiScanReceiver();
        btScanWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wifiManager.startScan();
                adapter = new MyListAdapter(MainActivity.this, R.layout.item_wifi, myWifi);
                listWifi.setAdapter(adapter);
            }
        });
        btdistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (WiFi myWiFi : myWifi) {
                    if (myWiFi.level != 0) {
                        wifis.add(myWiFi);
                    }
                }
                Collections.sort(wifis, new Comparator<WiFi>() {
                    @Override
                    public int compare(WiFi myWiFi, WiFi t) {
                        if (myWiFi.level < t.level) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                });

                Intent mh2 = new Intent(MainActivity.this, Distance.class);
                Intent mh3 = new Intent(MainActivity.this,MapView.class);
                // truyen wifi co gia tri lon thu 1
                Bundle bundle = new Bundle();
                bundle.putString("WiFiName1", wifis.get(0).SSID.toString());
                bundle.putInt("Rss1", wifis.get(0).level);
                bundle.putDouble("x1", wifis.get(0).x);
                bundle.putDouble("y1", wifis.get(0).y);
                bundle.putDouble("z1", wifis.get(0).z);
                //truyen wifi co gia tri lon thu 2
                bundle.putString("WiFiName2", wifis.get(1).SSID.toString());
                bundle.putInt("Rss2", wifis.get(1).level);
                bundle.putDouble("x2", wifis.get(1).x);
                bundle.putDouble("y2", wifis.get(1).y);
                bundle.putDouble("z2", wifis.get(1).z);
                //truyen wifi co gia tri lon thu 3
                bundle.putString("WiFiName3", wifis.get(2).SSID.toString());
                bundle.putInt("Rss3", wifis.get(2).level);
                bundle.putDouble("x3", wifis.get(2).x);
                bundle.putDouble("y3", wifis.get(2).y);
                bundle.putDouble("z3", wifis.get(2).z);
                //Đưa Bundle vào Intent
                mh2.putExtra("MyPackage", bundle);
                mh3.putExtra("MyPackage", bundle);
                startActivity(mh2);

            }
        });

    }

    private class WifiScanReceiver extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {
            List<ScanResult> wifiScanList = wifiManager.getScanResults();
            wifis.clear();
            for (ScanResult scanResult : wifiScanList) {
                for (int i = 0; i < myWifi.size(); i++) {
                    if (scanResult.SSID.equals(myWifi.get(i).SSID))
                        myWifi.get(i).setLevel(scanResult.level);
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    // Adapter Wifi
    private class MyListAdapter extends ArrayAdapter<WiFi> {
        public MyListAdapter(MainActivity mainActivity, int item_view, List<WiFi> myWifi) {
            super(MainActivity.this, R.layout.item_wifi, myWifi);
        }

        @Override
        public int getCount() {
            return myWifi.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // make sure we have a view to work with ( may have been given null
            View itemview = convertView;
            if (itemview == null) {
                itemview = getLayoutInflater().inflate(R.layout.item_wifi, parent, false);
            }
            // find the wifi to work with
            WiFi currentWifi = myWifi.get(position);
            // name wifi
            TextView nameWifi = (TextView) itemview.findViewById(R.id.txtname_wifi);
            nameWifi.setText(currentWifi.getSSID());
            // Level Wifi
            TextView levelWifi = (TextView) itemview.findViewById(R.id.txtRSS);
            levelWifi.setText("" + currentWifi.getLevel() + " dB");
            return itemview;
        }
    }

    protected void onPause() {
        if (wifiScanReceiver != null) {
            unregisterReceiver(wifiScanReceiver);
        }
        super.onPause();
    }

    protected void onResume() {
        if (wifiScanReceiver != null) {
            registerReceiver(wifiScanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        }
        super.onResume();
    }

}

