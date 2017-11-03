package kfu.ccsit.tmssks;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import kfu.ccsit.tmssks.data.DataMedium;
import kfu.ccsit.tmssks.data.Kid;
import kfu.ccsit.tmssks.data.network.NetworkUtils;

public class FragmentTimeline extends Fragment implements AdapterView.OnItemSelectedListener {

    private Spinner mKidsSpinner;
    private ListView mListView;
    private TextView mEmptyView;
    private Timer mTimer;
    private boolean mAllowRequest;

    public FragmentTimeline() {
    }

    public static FragmentTimeline newInstance() {
        return new FragmentTimeline();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_timeline, container, false);
        mKidsSpinner = (Spinner) root.findViewById(R.id.spinner_kids);
        mListView = (ListView) root.findViewById(R.id.list_timeline);
        mEmptyView = (TextView) root.findViewById(R.id.empty_view);

        mListView.setAdapter(new AdapterTimeline(getActivity()));
        mListView.setEmptyView(mEmptyView);
        
        mKidsSpinner.setAdapter(new AdapterKidsSpinner(getActivity()));
        mKidsSpinner.setOnItemSelectedListener(this);

        updateData();
        return root;
    }

    public void updateData() {
        if (mKidsSpinner != null && mListView != null) {
            ((BaseAdapter) mKidsSpinner.getAdapter()).notifyDataSetChanged();
            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAllowRequest = true;
        mTimer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (getActivity() != null && mAllowRequest) {
                    mAllowRequest = false;
                    DataMedium.loadTimelineRecords(getActivity(), new NetworkUtils.OnRequestListener() {
                        @Override
                        public void onFinished(String response, boolean error) {
                            updateData();
                            mAllowRequest = true;
                            if (error) {
                                mEmptyView.setText(response);
                            } else {
                                mEmptyView.setText("No records!");
                            }
                        }
                    });
                }
            }
        };
        mTimer.scheduleAtFixedRate(task, 1000, 1000);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Kid k = (Kid) mKidsSpinner.getSelectedItem();
        ((AdapterTimeline) mListView.getAdapter()).updateData(k);
        updateData();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        ((AdapterTimeline) mListView.getAdapter()).updateData(null);
        updateData();
    }

    public Kid getSelectedKid() {
        return (Kid) mKidsSpinner.getSelectedItem();
    }
}
