package com.example.alertdialog.Activity;

import android.app.Activity;
import android.os.AsyncTask;

import com.example.alertdialog.adapters.IDataAdapter;
import com.example.alertdialog.pojo.Express;

public class ExpressTask extends AsyncTask<Void, Void, String> {
    IDataAdapter<Express> adapter;
    private Activity context;
    public ExpressTask(IDataAdapter<Express> adapter, Activity context) {
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    protected String doInBackground(Void... voids) {
        return null;
    }
}
