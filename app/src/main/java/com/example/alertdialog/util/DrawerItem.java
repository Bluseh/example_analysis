package com.example.alertdialog.util;

import android.view.ViewGroup;

import com.example.alertdialog.adapters.DrawerAdapter;

/**
 * 侧滑栏的列表项
 *
 * @author xuexiang
 * @since 2019/1/6 上午12:28
 */
public abstract class DrawerItem<T extends DrawerAdapter.ViewHolder> {

    protected boolean isChecked;

    public abstract T createViewHolder(ViewGroup parent);

    public abstract void bindViewHolder(T holder);

    public DrawerItem setChecked(boolean isChecked) {
        this.isChecked = isChecked;
        return this;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public boolean isSelectable() {
        return true;
    }

}

