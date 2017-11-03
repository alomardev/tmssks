package kfu.ccsit.tmssks;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class ActivityAbout extends ActivityBase {


    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Setting up the toolbar
        setSupportActionBar(getToolbar());
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Go back to the main activity when home (UP) button is clicked
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void buttonClick(View v) {
        switch (v.getId()) {
            case R.id.btn_licenses:
                MiscUtils.showLicenseDialog(this);
                break;
            case R.id.btn_contact:
                MiscUtils.contactDeveloper(this);
                break;
        }
    }

}
