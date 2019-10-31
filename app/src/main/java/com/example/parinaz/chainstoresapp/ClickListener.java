package com.example.parinaz.chainstoresapp;

import android.view.View;

/**
 * Created by parinaz on 7/21/19.
 */
public interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
