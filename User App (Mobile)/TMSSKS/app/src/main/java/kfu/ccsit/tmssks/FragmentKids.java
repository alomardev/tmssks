package kfu.ccsit.tmssks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kfu.ccsit.tmssks.data.DataMedium;

public class FragmentKids extends Fragment {

    private RecyclerView mList;

    public FragmentKids() {
        // Required empty public constructor
    }

    public static FragmentKids newInstance() {
        return new FragmentKids();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_kids, container, false);
        mList = (RecyclerView) root.findViewById(R.id.recycler);
        mList.setHasFixedSize(true);
        mList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mList.setAdapter(new AdapterKidsList(getActivity()));

        updateData();
        return root;
    }

    public void updateData() {
        if (mList != null) {
            mList.getAdapter().notifyDataSetChanged();
        }
    }
}
