package com.example.yazilimlab.Model;

import android.content.Context;
import android.widget.LinearLayout;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class AdminAcceptList extends AdminResources {
    public AdminAcceptList(Context context, RecyclerView admin_RecyclerViewIncoming, CardView admin_CardViewIncoming, LinearLayout admin_LinearLayoutIncoming, String type, String state) {
        super(context, admin_RecyclerViewIncoming, admin_CardViewIncoming, admin_LinearLayoutIncoming, type, state);
    }
}
