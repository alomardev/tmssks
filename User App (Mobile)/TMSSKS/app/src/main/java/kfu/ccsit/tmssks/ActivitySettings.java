package kfu.ccsit.tmssks;

import android.os.Bundle;
import android.preference.Preference;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import java.util.List;

public class ActivitySettings extends ActivityBase implements BasePreferenceFragment.PreferenceListener {

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Setting up the toolbar
        setSupportActionBar(getToolbar());
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings");

        FragmentManager fm = getSupportFragmentManager();
        List<Fragment> fms = fm.getFragments();

        if (fms == null || fms.size() < 1)
            replaceFragment(R.xml.preferences, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean equality(Preference pref, int keyResId) {
        return pref.getKey().equals(getString(keyResId));
    }

    private void replaceFragment(Fragment frag, boolean backStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        // Define animations
        ft.setCustomAnimations(backStack ? R.anim.fragment_open_enter : 0,
                backStack ? R.anim.fragment_open_exit : 0,
                R.anim.fragment_close_enter,
                R.anim.fragment_close_exit);

        // Replace fragment and storing the resource id as a tag
        ft.replace(R.id.primary_container, frag, null);

        // Add to back stack only if it is not the top level fragment.
        // This solves the empty container issue
        if (backStack)
            ft.addToBackStack(null);

        // Apply changes
        ft.commit();

    }

    private void replaceFragment(int xmlResId, boolean backStack) {
        BasePreferenceFragment frag = BasePreferenceFragment.newInstance(xmlResId);
        replaceFragment(frag, backStack);
    }

    private void replaceFragment(int xmlResId) {
        replaceFragment(xmlResId, true);
    }

    private Preference getPreference(int keyId) {
        String key = getString(keyId);
        return getPreference(key);
    }

    private Preference getPreference(String key) {
        getSupportFragmentManager().executePendingTransactions();
        List<Fragment> frags = getSupportFragmentManager().getFragments();
        if (frags != null) {
            for (Fragment f : frags) {
                if (f != null && f instanceof BasePreferenceFragment) {
                    Preference target = ((BasePreferenceFragment) f).
                            getPreference(key);
                    if (target != null) {
                        return target;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean onPreferenceChange(android.preference.Preference preference, Object newValue) {
        return true;
    }

    @Override
    public boolean onPreferenceTreeClick(android.preference.Preference preference) {
        return true;
    }
}
