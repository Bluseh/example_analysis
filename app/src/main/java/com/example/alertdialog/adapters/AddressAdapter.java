package com.example.alertdialog.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alertdialog.R;
import com.example.alertdialog.pojo.Address;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    private List<String> addresses;
    private List<String> names;
    private List<String> telCodes;

    private OnItemClickListener listener;

    public AddressAdapter(List<String> names, List<String> telCodes, List<String> addresses) {
        this.names = names;
        this.telCodes = telCodes;
        this.addresses = addresses;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_address_rec, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, final int position) {
        // holder.getAdapterPosition()动态获取地址
        String address = addresses.get(holder.getAdapterPosition());
        holder.addressTextView.setText(address);
        holder.nameAndTelCodeTextView.setText(names.get(holder.getAdapterPosition()) + " " + telCodes.get(holder.getAdapterPosition()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    Log.e("click", Integer.toString(holder.getAdapterPosition()));
                    // adapter绑定监听器后点击不触发，改为在onBindViewHolder中主动触发了
                    listener.onItemClick(holder.getAdapterPosition());
                }
            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    Log.e("click", Integer.toString(holder.getAdapterPosition()));
                    // adapter绑定监听器后点击不触发，改为在onBindViewHolder中主动触发了
                    listener.onItemLongClick(holder.getAdapterPosition());
                }
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onItemLongClick(int position);
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView addressTextView;
        TextView nameAndTelCodeTextView;
        Button deleteButton;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            addressTextView = itemView.findViewById(R.id.textViewAddress);
            nameAndTelCodeTextView = itemView.findViewById(R.id.textViewNameAndTelCode);
            deleteButton = itemView.findViewById(R.id.buttonAction);

        }
    }
}

