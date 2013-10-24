package uk.ks.jarvis.simple.geometry.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;

import uk.ks.jarvis.simple.geometry.R;
import uk.ks.jarvis.simple.geometry.coordinateplane.SystemInformation;


public class MainActivity extends FragmentActivity implements View.OnTouchListener, View.OnLongClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    @Override
    public boolean onLongClick(View v) {
        return true;
    }

    private void setSystemInformation() {
        DisplayMetrics displayMetrics = getBaseContext().getResources().getDisplayMetrics();
        SystemInformation.DISPLAY_WIDTH = displayMetrics.widthPixels;
        SystemInformation.DISPLAY_HEIGHT = displayMetrics.heightPixels;
    }
}
