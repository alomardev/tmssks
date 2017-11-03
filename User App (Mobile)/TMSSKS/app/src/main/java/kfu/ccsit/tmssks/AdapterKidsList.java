package kfu.ccsit.tmssks;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import kfu.ccsit.tmssks.data.DataMedium;
import kfu.ccsit.tmssks.data.Kid;

public class AdapterKidsList extends RecyclerView.Adapter<AdapterKidsList.ViewHolder> {

    private Context mContext;
    private List<Kid> mKids;

    public AdapterKidsList(Context context) {
        this.mContext = context;
        mKids = DataMedium.getKids();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.list_kids_item, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nameTv.setText(mKids.get(position).getName());
        holder.detailsTv.setText("National ID: " + mKids.get(position).getId()
                + "\nSchool: " + (mKids.get(position).getSchool() != null ? mKids.get(position).getSchool().getName()
                + ", Level: " + mKids.get(position).getLevel() : "none")
                + "\nTransportation: " + (mKids.get(position).getTrans() != null ?
                mKids.get(position).getTrans().getNumPlate() : "none"));
    }

    @Override
    public int getItemCount() {
        return mKids == null ? 0 : mKids.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv;
        TextView detailsTv;
        ImageView avatarIv;

        public ViewHolder(View v) {
            super(v);
            nameTv = (TextView) v.findViewById(R.id.tv_name);
            detailsTv = (TextView) v.findViewById(R.id.tv_details);
            avatarIv = (ImageView) v.findViewById(R.id.iv_avatar);
        }
    }
}
