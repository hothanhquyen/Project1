package com.example.user.project1;

import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class MapView extends AppCompatActivity {
    FrameLayout relativeLayout;
    ImageView imgView;
    static double ratioX, ratioY;
    double l,w;
    double x,y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        l = 25;
        w = 50;
        relativeLayout = (FrameLayout) findViewById(R.id.rootView);
        imgView = (ImageView)findViewById(R.id.imageView1);

        Intent callerIntent=getIntent();
        Bundle packageFromCaller= callerIntent.getBundleExtra("toado");
        x = packageFromCaller.getDouble("toado_x");
        y = packageFromCaller.getDouble("toado_y");


        Toast.makeText(MapView.this,"Tọa đô x ="+ String.valueOf(x)+"Tọa đô y ="+ String.valueOf(y),Toast.LENGTH_LONG).show();
        imgView.setImageResource(R.drawable.shape_rectangle);

        relativeLayout.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // Get image matrix values and place them in an array
                ratioX = x/w;
                ratioY = y/l;
                float[] f = new float[9];
                imgView.getImageMatrix().getValues(f);

                // Extract the scale values using the constants (if aspect ratio
                // maintained, scaleX == scaleY)
                final float scaleX = f[Matrix.MSCALE_X];
                final float scaleY = f[Matrix.MSCALE_Y];

                // Get the drawable (could also get the bitmap behind the
                // drawable and getWidth/getHeight)

                final Drawable d = imgView.getDrawable();
                final int origW = d.getIntrinsicWidth();
                final int origH = d.getIntrinsicHeight();

                // Calculate the actual dimensions
                final int actW = Math.round(origW * scaleX);
                final int actH = Math.round(origH * scaleY);
                // Log.e("DBG", (d.getBounds().right - d.getBounds().left -
                // actW)
                // / 2 + " = " + actW);
                final int top = Math.abs(actH - imgView.getHeight()) / 2;
                final int left = Math.abs(actW - imgView.getWidth()) / 2;

                Log.e("DBG", "[" + origW + "," + origH + "] -> [" + actW + ","
                        + actH + "] & scales: x=" + scaleX + " y=" + scaleY
                        + "|" + top + " " + left + "Actual "
                        + (d.getBounds().bottom - d.getBounds().top));
                final int X_Draw = (int) (ratioX * actW);
                final int Y_Draw = (int) (ratioY * actH);
                Log.e("Bang", ratioX + " " + ratioY);

                ImageView imgView = new ImageView(getApplicationContext());
                imgView.setImageResource(R.drawable.ic_place_black_24dp);


                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                // params.addRule(FrameLayout.ALIGN_PARENT_LEFT,
                // FrameLayout.TRUE);

                params.setMargins(left + X_Draw - imgView.getWidth(), top
                        + Y_Draw - imgView.getHeight(), 0, 0);

                //Remove old position
                int childcount = relativeLayout.getChildCount();
                for (int i = 0; i < childcount; i++) {
                    View view = relativeLayout.getChildAt(i);
                    if (view instanceof ImageView
                            && view.getId() != R.id.imageView1)
                        relativeLayout.removeViewAt(i);
                }
                //Add new point to pos
                relativeLayout.addView(imgView, params);
                return false;
            }
        });

    }

    private int dpToPx(int dp) {
        float density = getApplicationContext().getResources()
                .getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }


}
