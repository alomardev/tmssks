package kfu.ccsit.tmssks;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

@SuppressLint("Registered")
public class ActivityBase extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (hasDrawer() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        super.onCreate(savedInstanceState);
    }

    public boolean hasDrawer() {
        return false;
    }

    @Override
    public void recreate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            super.recreate();
        else {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

    public Toolbar getToolbar() {
        return getToolbar(R.id.appbar);
    }

    public Toolbar getToolbar(int id) {
        if (mToolbar != null)
            return mToolbar;

        mToolbar = (Toolbar) findViewById(id);
        return mToolbar;
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
