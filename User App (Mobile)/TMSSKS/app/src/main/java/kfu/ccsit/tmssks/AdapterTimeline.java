package kfu.ccsit.tmssks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kfu.ccsit.tmssks.data.Kid;
import kfu.ccsit.tmssks.data.TimelineRecord;


public class AdapterTimeline extends BaseAdapter {

    private Context mContext;
    private List<TimelineRecord> mRecords;

    public AdapterTimeline(Context context) {
        mContext = context;
        mRecords = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mRecords.size();
    }

    @Override
    public Object getItem(int position) {
        return getActualItem(position);
    }

    private TimelineRecord getActualItem(int position) {
        return mRecords.get(getCount() - position - 1);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.timeline_list_item, parent,
                    false);
            holder = new ViewHolder();
            holder.messageTv = (TextView) convertView.findViewById(R.id.tv_message);
            holder.timeTv = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.messageTv.setText(getActualItem(position).getMessage());
        holder.timeTv.setText(TimeUtils.formatTime(getActualItem(position).getTime()));

        return convertView;
    }

    public void updateData(Kid kid) {
        if (kid == null) {
            mRecords = null;
        } else {
            mRecords = kid.getRecords();
        }
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView messageTv, timeTv;
    }
}
