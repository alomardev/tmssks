package kfu.ccsit.tmssks;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kfu.ccsit.tmssks.data.DataMedium;


public class NavigationDrawerFragment extends Fragment implements View.OnClickListener {

    private static final String PREF_KEY_USER_LEARNED_DRAWER = "user_learned_drawer";
    private NavigationDrawerListener mCallback;
    // Contains NavigationItem views
    private List<NavigationItem> mNavItems;
    private boolean mUserLearnedDrawer, mFromSavedInstanceState;
    private TextView mUserFullNameTv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mFromSavedInstanceState = true;
        }

        mUserLearnedDrawer = PreferenceManager.getDefaultSharedPreferences(getActivity()).
                getBoolean(PREF_KEY_USER_LEARNED_DRAWER, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Initializing
        ViewGroup root = (ViewGroup) inflater
                .inflate(R.layout.fragment_navigation_drawer, container, false);

        // Adding navigation items
        mNavItems = new ArrayList<>();
        setupNavigationItems(root);

        root.findViewById(R.id.btn_logout).setOnClickListener(this);

        for (NavigationItem item : mNavItems) {
            item.setOnClickListener(this);
        }

        // Check user
        mUserFullNameTv = (TextView) root.findViewById(R.id.tv_user_full_name);

        updateData();
        return root;
    }

    public void updateData() {
        if (mUserFullNameTv != null) {
            mUserFullNameTv.setText(DataMedium.getUser().getName());
        }
    }

    private void setupNavigationItems(ViewGroup vg) {
        for (int i = 0; i < vg.getChildCount(); i++) {
            View v = vg.getChildAt(i);

            if (v instanceof ViewGroup) {
                setupNavigationItems((ViewGroup) v);
                continue;
            }
            if (v instanceof NavigationItem) {
                mNavItems.add((NavigationItem) v);
            }
        }
    }

    // Checking that the activity actually implemented the NavigationListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (NavigationDrawerListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    " must implement NavigationListener");
        }
    }

    @Override
    public void onClick(View v) {
        if (v instanceof NavigationItem) {
            mCallback.onNavItemSelected((NavigationItem) v);
            // Going through all navigation items
            setSelectedItem(v.getId());
        } else if (v.getId() == R.id.btn_logout) {
            DataMedium.logout(getActivity());
        }
    }

    /**
     * Setting up the drawer layout and the drawer toggle
     *
     * @param drawerLayout The drawer layout to setup
     */
    public void setup(DrawerLayout drawerLayout) {
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            drawerLayout.openDrawer(GravityCompat.START);

            if (!mUserLearnedDrawer) {
                mUserLearnedDrawer = true;
                PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().
                        putBoolean(PREF_KEY_USER_LEARNED_DRAWER, true).apply();
            }
        }
    }

    public void setSelectedItem(int viewId) {
        if (viewId == R.id.nav_settings || viewId == R.id.nav_about || viewId == R.id.nav_feedback)
            return;

        for (NavigationItem item : mNavItems) {
            item.setSelected(viewId == item.getId());
        }
    }

    /**
     * Used to communicate with the activity
     */
    public interface NavigationDrawerListener {
        void onNavItemSelected(NavigationItem ni);

        void onDrawerOpened();

        void onDrawerClosed();
    }
}
