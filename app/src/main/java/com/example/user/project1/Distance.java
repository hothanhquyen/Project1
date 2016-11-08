package com.example.user.project1;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Distance extends AppCompatActivity {

    TextView txtAP11,txtAP22,txtAP33,txtRSS11,txtRSS22,txtRSS33,txttdx,txttdy,txtdistance1,
            txtdistance2,txtdistance3;
    Button bttmapview,bttsentdata;
    List<WiFi> newListWiFi = new ArrayList<>();
    String res1,res2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance);

        txtAP11 = (TextView)findViewById(R.id.txtAP11);
        txtAP22 = (TextView)findViewById(R.id.txtAP22);
        txtAP33 = (TextView)findViewById(R.id.txtAP33);
        txtdistance1 = (TextView)findViewById(R.id.txtdistance1);
        txtdistance2 = (TextView)findViewById(R.id.txtdistance2);
        txtdistance3 = (TextView)findViewById(R.id.txtdistance3);
        txttdx = (TextView)findViewById(R.id.txttoadox);
        txttdy = (TextView)findViewById(R.id.txttoadoy) ;
        bttmapview = (Button)findViewById(R.id.buttonmapview);
        bttsentdata = (Button)findViewById(R.id.buttonsent);




        //lấy intent gọi Activity này
        Intent callerIntent=getIntent();
        //có intent rồi thì lấy Bundle dựa vào MyPackage
        Bundle packageFromCaller= callerIntent.getBundleExtra("MyPackage");
        txtAP11.setText(packageFromCaller.getString("WiFiName1"));
        txtAP22.setText(packageFromCaller.getString("WiFiName2"));
        txtAP33.setText(packageFromCaller.getString("WiFiName3"));

        // txtRSS11.setText(String.valueOf(packageFromCaller.getInt("Rss1"))+" dB");
        // txtRSS22.setText(String.valueOf(packageFromCaller.getInt("Rss2"))+" dB");
        // txtRSS33.setText(String.valueOf(packageFromCaller.getInt("Rss3"))+ " dB");

        // add 3 AP gần nhất
        final WiFi AP1 = new WiFi();
        AP1.setSSID(packageFromCaller.getString("WiFiName1"));
        AP1.setLevel(packageFromCaller.getInt("Rss1"));
        AP1.x = packageFromCaller.getDouble("x1");
        AP1.y = packageFromCaller.getDouble("y1");
        AP1.z = packageFromCaller.getDouble("z1");
        newListWiFi.add(AP1);

        final WiFi AP2 = new WiFi();
        AP2.setSSID(packageFromCaller.getString("WiFiName2"));
        AP2.setLevel(packageFromCaller.getInt("Rss2"));
        AP2.x = packageFromCaller.getDouble("x2");
        AP2.y = packageFromCaller.getDouble("y2");
        AP2.z = packageFromCaller.getDouble("z2");
        newListWiFi.add(AP2);

        final WiFi AP3 = new WiFi();
        AP3.setSSID(packageFromCaller.getString("WiFiName3"));
        AP3.setLevel(packageFromCaller.getInt("Rss3"));
        AP3.x = packageFromCaller.getDouble("x3");
        AP3.y = packageFromCaller.getDouble("y3");
        AP3.z = packageFromCaller.getDouble("z3");
        newListWiFi.add(AP3);

        // Tính khoảng cách
        for (int i = 0; i < newListWiFi.size(); i++) {
            newListWiFi.get(i).distance = calculateDistance(newListWiFi.get(i).level);
        }
        // distances
        double r1 = newListWiFi.get(0).distance;
        double r2 = newListWiFi.get(1).distance;
        double r3 = newListWiFi.get(2).distance;
        //r4 = WiFiList.get(3).distance;
        txtdistance1.setText(String.format("%.2f", r1) + " m");
        txtdistance2.setText(String.format("%.2f", r2) + " m");
        txtdistance3.setText(String.format("%.2f", r3) + " m");

        final double[] result = calculatePosition(AP1.x, AP1.y, AP2.x, AP2.y, AP3.x, AP3.y, r1, r2, r3);
        txttdx.setText("" + result[0]);
        txttdy.setText("" + result[1]);

        bttsentdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                res1 = String.valueOf(result[0]);
                res2 = String.valueOf(result[1]);
                Toast.makeText(Distance.this,String.valueOf(res1),Toast.LENGTH_LONG).show();
                new UpdateThingspeakTask().execute();

            }
        });
        bttmapview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent display_mapview = new Intent(Distance.this,MapView.class);
                Bundle bundle = new Bundle();
                bundle.putDouble("toado_x",result[0]);
                bundle.putDouble("toado_y",result[1]);
                display_mapview.putExtra("toado",bundle);
                startActivity(display_mapview);
            }
        });

    }



    // Hàm tính khoảng cách từ 1 điểm tới 1 WiFi Access Point
    public double calculateDistance(int receivedPower) {
        int powerAtD0 = -38;         // declare the path loss exponent
        double n = 3.61;
        //double n = 3.5;
        double exp = (powerAtD0 - receivedPower) / (10 * n);
        double distance = Math.pow(10.0, exp);
        return distance;
    }

    //Hàm tính Det của 1 ma trận 3x3
    public Double DetMatrix(double a11, double a12, double a13, double a21, double a22, double a23, double a31, double a32, double a33) {
        double DetA = a11 * (a22 * a33 - a32 * a23) - a12 * (a21 * a33 - a31 * a23) + a13 * (a21 * a32 - a31 * a22);
        return DetA;
    }

    // Hàm tính tọa độ của 1 điểm dựa vào 3 khoảng cách, và tọa độ của 3 AP
    public  double[] calculatePosition(double a1,double a2,double b1, double b2,double c1,double c2,double r1,double r2,double r3)
    {
        double upY = (b1 - a1) * (c1 * c1 + c2 * c2 - r3 * r3) + (a1 - c1) * (b1 * b1 + b2 * b2 - r2 * r2) + (c1 - b1) * (a1 * a1 + a2 * a2 - r1 * r1);
        double downY = 2 * (c2 * (b1 - a1) + b2 * (a1 - c1) + a2 * (c1 - b1));
        double resultY = upY / downY;
        double upX = (r2 * r2 + a1 * a1 + a2 * a2 - r1 * r1 - b1 * b1 - b2 * b2 - 2 * (a2 - b2) * resultY);
        double downX = 2 * (a1 - b1);
        double resultX = upX / downX;
        double[] result = {resultX,resultY};
        return result;
    }


    // Hàm tính tọa độ của 1 điểm dựa vào 3 khoảng cách, và tọa độ của 3 AP
    public List positioning(double a1, double b1, double a2, double b2, double a3, double b3, double r1, double r2, double r3) {
        List<Double> location = new ArrayList<>();
        double a1Sp = a1 * a1, a2Sp = a2 * a2, a3Sp = a3 * a3, b1Sp = b1 * b1, b2Sp = b2 * b2, b3Sp = b3 * b3, r1Sp = r1 * r1, r2Sp = r2 * r2, r3Sp = r3 * r3;
        double numerator1 = (a2 - a1) * (a3Sp + b3Sp - r3Sp) + (a1 - a3) * (a2Sp + b2Sp - r2Sp) + (a3 - a2) * (a1Sp + b1Sp - r1Sp);
        double denominator1 = 2 * (b3 * (a2 - a1) + b2 * (a1 - a3) + b1 * (a3 - a2));
        // Toa do y
        location.add(numerator1 / denominator1);
        double numerator2 = r2Sp - r1Sp + a1Sp - a2Sp + b1Sp - b2Sp - 2 * (b1-b2) * location.get(0);
        double denumerator2 = 2 * (a1 - a2);         // Toa do x

        location.add(numerator2 / denumerator2);
        return location;
    }
    // API thing speak
    class UpdateThingspeakTask extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(Void... urls) {
            try {
                URL url = new URL("https://api.thingspeak.com/update?key=X870YWODQ020E25L&field1=" +
                        res1 + "&field2=" + res2 );
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                Toast.makeText(Distance.this,"fail",Toast.LENGTH_LONG).show();
                return null;
            }
        }
        protected void onPostExecute(String response) {
            // We completely ignore the response
            // Ideally we should confirm that our update was successful
            //Toast.makeText(ManHinh2.this,"sent thanh cong",Toast.LENGTH_LONG).show();
        }
    }

}