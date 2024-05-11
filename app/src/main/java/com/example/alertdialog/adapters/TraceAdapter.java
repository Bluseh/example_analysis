package com.example.alertdialog.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.alertdialog.R;
import com.example.alertdialog.pojo.ExpressTrack;

import java.util.ArrayList;
import java.util.List;

public class TraceAdapter extends RecyclerView.Adapter<TraceAdapter.TraceViewHolder> implements IDataAdapter<List<ExpressTrack>> {

    private Context mContext;
    private List<ExpressTrack> mList;
    private LayoutInflater inflater;

    public TraceAdapter(Context mContext) {
        this.mContext = mContext;
        this.mList = new ArrayList<>();
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public TraceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TraceViewHolder(inflater.inflate(R.layout.item_trace, parent, false));
    }

    @Override
    public void onBindViewHolder(TraceViewHolder holder, int position) {
        //设置相关数据
        ExpressTrack trace = mList.get(position);
        int type = trace.getType();
        if (type == ExpressTrack.STATUS.TYPE_CURRENT) {
            //holder.acceptStationTv.setTextColor(mContext.getResources().getColor(R.color.color_c03));
            holder.dotIv.setImageResource(R.drawable.dot_red);
        } else if (type == ExpressTrack.STATUS.TYPE_PAST) {
            //holder.acceptStationTv.setTextColor(mContext.getResources().getColor(R.color.color_6));
            holder.dotIv.setImageResource(R.drawable.dot_black);
        }
        holder.acceptTimeTv.setText(trace.getAcceptTime());
        holder.acceptStationTv.setText(trace.getAcceptStation());
        if (position == mList.size() - 1) {
            //最后一条数据，隐藏时间轴的竖线和水平的分割线
            holder.timeLineView.setVisibility(View.INVISIBLE);
            holder.dividerLineView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public List<ExpressTrack> getData() {
        return this.mList;
    }

    @Override
    public void setData(List<ExpressTrack> data) {
        mList = data;
    }

    public class TraceViewHolder extends RecyclerView.ViewHolder {

        private TextView acceptTimeTv;  //接收时间
        private TextView acceptStationTv;  //接收地点
        private ImageView dotIv; //当前位置
        private View dividerLineView; //时间轴的竖线
        private View timeLineView; //水平的分割线


        public TraceViewHolder(View itemView) {
            super(itemView);
            acceptTimeTv = (TextView) itemView.findViewById(R.id.accept_time_tv_);
            acceptStationTv = (TextView) itemView.findViewById(R.id.accept_station_tv_);
            dotIv = (ImageView) itemView.findViewById(R.id.dot_iv_);
            dividerLineView = itemView.findViewById(R.id.divider_line_view_);
            timeLineView = itemView.findViewById(R.id.time_line_view_);
        }
    }
}
