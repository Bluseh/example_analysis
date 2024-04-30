package com.example.alertdialog.adapters;

// CustomAdapter.java

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.alertdialog.pojo.Customer;
import com.example.alertdialog.pojo.Express;
import com.example.alertdialog.R;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<Express> {

    private Context mContext;
    private int mResource;

    private List<Customer> customerList;
    private Integer mode;

    public CustomAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Express> objects, @NonNull List<Customer> customerList, @NonNull Integer mode) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        this.customerList = customerList;
        this.mode = mode;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(mContext).inflate(mResource, parent, false);
        }

        TextView recipientTextView = listItemView.findViewById(R.id.text_recipient);
        TextView contentTextView = listItemView.findViewById(R.id.text_package_content);

        Express currentItem = getItem(position);
        int rec = currentItem.getReceiver();
        Customer desiredCustomer = null;
        for(Customer customer : this.customerList) {
            if(customer.getId() == rec) {
                desiredCustomer = customer;
                break;
            }
        }

        if (desiredCustomer != null) {
            recipientTextView.setText(desiredCustomer.getName());
        }
        if (mode == 1) {
            contentTextView.setText(currentItem.getReceiverAddress());
        }
        if (mode == 2) {
            contentTextView.setText(currentItem.getSenderAddress());
        }

        return listItemView;
    }
}

