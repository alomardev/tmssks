package kfu.ccsit.tmssks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import kfu.ccsit.tmssks.data.DataMedium;
import kfu.ccsit.tmssks.data.Kid;
import kfu.ccsit.tmssks.data.network.NetworkUtils;

public class ActivityMain extends ActivityBase implements
        NavigationDrawerFragment.NavigationDrawerListener {


    private int mSection;

    private NavigationDrawerFragment mNavigationFragment;
    private DrawerLayout mDrawerLayout;
    private FloatingActionButton mFab;

    // Fragments
    private FragmentTimeline mTimelineFragment;
    private FragmentMessages mMessagesFragment;
    private FragmentKids mKidsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        setContentView(R.layout.activity_main);

        // Setting up toolbar
        setSupportActionBar(getToolbar());
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_humburger);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fm = getSupportFragmentManager();
        mNavigationFragment = (NavigationDrawerFragment)
                fm.findFragmentById(R.id.frag_navigation_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

        mTimelineFragment = FragmentTimeline.newInstance();
        mKidsFragment = FragmentKids.newInstance();
        mMessagesFragment = FragmentMessages.newInstance();

        // Sectioning
        changeSection(R.id.nav_timeline);

        // Data Listeners
        if (savedInstanceState == null) {
            loadData();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        } else if (item.getItemId() == R.id.action_sync) {
            loadData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean hasDrawer() {
        return true;
    }

    @Override
    public void onNavItemSelected(NavigationItem ni) {
        switch (ni.getId()) {
            case R.id.nav_timeline:
            case R.id.nav_messages:
            case R.id.nav_kids:
                changeSection(ni.getId());
                break;
            case R.id.nav_settings:
                startActivity(new Intent(this, ActivitySettings.class));
                break;
            case R.id.nav_about:
                startActivity(new Intent(this, ActivityAbout.class));
                break;
            case R.id.nav_feedback:
                MiscUtils.contactDeveloper(this);
                break;

        }
        mDrawerLayout.closeDrawers();
    }

    @Override
    public void onDrawerOpened() {

    }

    @Override
    public void onDrawerClosed() {

    }

    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            Kid k = mTimelineFragment.getSelectedKid();
            if (k != null) {
                Intent intent = new Intent(this, ActivityMaps.class);
                intent.putExtra(ActivityMaps.KEY_KID_ID, k.getId());
                startActivity(intent);
            }
        }
    }

    public void changeSection(int section) {
        mSection = section;
        mNavigationFragment.setSelectedItem(section);

        // change fragment
        Fragment frag = mSection == R.id.nav_messages ? mMessagesFragment :
                mSection == R.id.nav_kids ? mKidsFragment : mTimelineFragment;

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.setCustomAnimations(R.anim.fragment_open_enter, R.anim.fragment_open_exit,
                R.anim.fragment_close_enter, R.anim.fragment_close_exit);

        ft.replace(R.id.fragment_container, frag, null);

        ft.commit();

        // update fab
        mFab.setImageResource(mSection == R.id.nav_messages ?
                R.drawable.ic_edit : R.drawable.ic_location);

        mFab.setVisibility(mSection == R.id.nav_kids ? View.GONE : View.VISIBLE);

    }

    public void loadData() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading data...");
        dialog.setCancelable(false);
        dialog.show();
        DataMedium.loadStaticData(this, new NetworkUtils.OnRequestListener() {
            @Override
            public void onFinished(String response, boolean error) {
                if (error) {
                    LogUtils.d(response);
                    Toast.makeText(ActivityMain.this, "Couldn\'t load data!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    mNavigationFragment.updateData();
                    mTimelineFragment.updateData();
                    mKidsFragment.updateData();
                }
                try {
                    dialog.dismiss();
                } catch (Exception ex) {
                    LogUtils.d(ex.getMessage());
                }
            }
        });
    }
}
