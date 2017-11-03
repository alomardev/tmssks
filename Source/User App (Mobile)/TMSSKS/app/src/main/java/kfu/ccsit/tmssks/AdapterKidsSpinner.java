package kfu.ccsit.tmssks;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import kfu.ccsit.tmssks.data.DataMedium;
import kfu.ccsit.tmssks.data.Kid;

public class AdapterKidsSpinner extends BaseAdapter {

    private Context mContext;
    private List<Kid> mKids;

    public AdapterKidsSpinner(Context context) {
        this.mContext = context;
        mKids = DataMedium.getKids();
    }

    @Override
    public int getCount() {
        return mKids.size();
    }

    @Override
    public Object getItem(int position) {
        return mKids.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.spinner_kids_item, parent,
                    false);
            holder = new ViewHolder();
            holder.avatarIv = (ImageView) convertView.findViewById(R.id.iv_avatar);
            holder.nameTv = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nameTv.setText(mKids.get(position).getName());
        return convertView;
    }

    private static class ViewHolder {
        ImageView avatarIv;
        TextView nameTv;
    }
}
