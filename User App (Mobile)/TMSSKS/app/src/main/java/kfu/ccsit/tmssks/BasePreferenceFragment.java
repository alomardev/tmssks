package kfu.ccsit.tmssks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.v4.preference.PreferenceFragment;

import java.util.HashMap;

import kfu.ccsit.tmssks.prefs.ListPreference;
import kfu.ccsit.tmssks.prefs.TimePreference;

public class BasePreferenceFragment extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    public interface PreferenceListener {
        boolean onPreferenceChange(Preference preference, Object newValue);

        boolean onPreferenceTreeClick(Preference preference);
    }

    private static final String KEY_PREF_XML_RES_ID = "preference_xml_res_id";
    private HashMap<Preference, String> mAutoSummaries;

    private PreferenceListener mCallback;

    public static BasePreferenceFragment newInstance(int prefResId) {
        BasePreferenceFragment instance = new BasePreferenceFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_PREF_XML_RES_ID, prefResId);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        addPreferencesFromResource(getArguments().getInt(KEY_PREF_XML_RES_ID));

        SharedPreferences defaultSP = PreferenceManager.getDefaultSharedPreferences(getActivity());
        PreferenceScreen prefScreen = getPreferenceScreen();
        mAutoSummaries = new HashMap<>();

        // Maintaining preference objects
        setupPreferences(prefScreen, defaultSP);
    }

    @SuppressLint("NewApi")
    private void setupPreferences(PreferenceGroup prefGroup, SharedPreferences defaultSP) {
        for (int i = 0; i < prefGroup.getPreferenceCount(); i++) {
            Preference pref = prefGroup.getPreference(i);

            // setup preference in preference groups
            if (pref instanceof PreferenceGroup) {
                setupPreferences((PreferenceGroup) pref, defaultSP);
                continue;
            }

            // Apply change listener
            if (pref.isPersistent())
                pref.setOnPreferenceChangeListener(this);

            // Summaries initializing
            CharSequence summary = pref.getSummary();
            if (summary != null && summary.toString().contains("[auto]")) {
                mAutoSummaries.put(pref, summary.toString());
                updateSummary(pref, defaultSP);
            }

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (PreferenceListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    " must implement BasePreferenceFragment.PreferenceListener");
        }
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        return mCallback.onPreferenceTreeClick(preference) ||
                super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference pref, Object newValue) {
        // Update only preferences with "[auto]" summary
        if (mAutoSummaries.containsKey(pref))
            updateSummary(pref, PreferenceManager.getDefaultSharedPreferences(getActivity()));

        return mCallback.onPreferenceChange(pref, newValue);
    }

    private void updateSummary(Preference pref, SharedPreferences defaultSP) {
        BasePreferenceFragment.updateSummary(getActivity(), pref, defaultSP,
                mAutoSummaries.get(pref));
    }

    public static void updateSummary(Context context, Preference pref, SharedPreferences defaultSP,
                                     String originalAutoSummary) {
        String autoSummary, key = pref.getKey();

        // Getting the value
        try {
            autoSummary = defaultSP.getString(key, "");
        } catch (ClassCastException e) {
            autoSummary = Integer.toString(defaultSP.getInt(key, 0));
        }

        // Getting the entry text for list preferences
        if (pref instanceof ListPreference) {
            CharSequence[] entries = ((ListPreference) pref).getEntries();
            CharSequence[] entryValues = ((ListPreference) pref).getEntryValues();

            for (int i = 0; i < entries.length; i++) {
                if (entryValues[i].equals(autoSummary)) {
                    autoSummary = entries[i].toString();
                    break;
                }
            }
        } else if (pref instanceof TimePreference) {
            autoSummary = TimeUtils.getReadableTime(autoSummary);
        }

        pref.setSummary(originalAutoSummary.replace("[auto]", autoSummary));
    }

    public Preference getPreference(String key) {
        return findPreference(key);
    }
}
