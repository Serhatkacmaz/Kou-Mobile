package com.example.yazilimlab.AdminHomeFragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.yazilimlab.Model.AdminAcceptList;
import com.example.yazilimlab.Model.AdminAppItemAdapter;
import com.example.yazilimlab.Model.AdminAppItemInfo;
import com.example.yazilimlab.Model.AdminIncomingList;
import com.example.yazilimlab.Model.AdminRejectedList;
import com.example.yazilimlab.Model.CustomDialog;
import com.example.yazilimlab.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AdminDgsFragment extends Fragment {

    // RecyclerViewIncoming
    RecyclerView admin_DgsRecyclerViewIncoming;
    AdminAppItemAdapter adminAppItemIncomingAdapter;

    // RecyclerViewAccept
    RecyclerView admin_DgsRecyclerViewAccept;
    AdminAppItemAdapter adminAppItemAcceptAdapter;

    // RecyclerViewRejected
    RecyclerView admin_DgsRecyclerViewRejected;
    AdminAppItemAdapter adminAppItemRejectedAdapter;


    // CardView Extand
    private CardView admin_DgsCardViewIncoming, admin_DgsCardViewAccept, admin_DgsCardViewRejected;
    private LinearLayout admin_DgsLinearLayoutIncoming, admin_DgsLinearLayoutAccept, admin_DgsLinearLayoutRejected;

    // Admin IncomingList
    private AdminIncomingList adminIncomingList;

    // Admin AcceptList
    private AdminAcceptList adminAcceptList;

    // Admin RejectedList
    private AdminRejectedList adminRejectedList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_dgs, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // RecyclerView
        admin_DgsRecyclerViewIncoming = (RecyclerView) view.findViewById(R.id.admin_DgsRecyclerViewIncoming);
        admin_DgsRecyclerViewAccept = (RecyclerView) view.findViewById(R.id.admin_DgsRecyclerViewAccept);
        admin_DgsRecyclerViewRejected = (RecyclerView) view.findViewById(R.id.admin_DgsRecyclerViewRejected);

        // CardView
        admin_DgsCardViewIncoming = (CardView) view.findViewById(R.id.admin_DgsCardViewIncoming);
        admin_DgsCardViewAccept = (CardView) view.findViewById(R.id.admin_DgsCardViewAccept);
        admin_DgsCardViewRejected = (CardView) view.findViewById(R.id.admin_DgsCardViewRejected);

        // LinearLayout
        admin_DgsLinearLayoutIncoming = (LinearLayout) view.findViewById(R.id.admin_DgsLinearLayoutIncoming);
        admin_DgsLinearLayoutAccept = (LinearLayout) view.findViewById(R.id.admin_DgsLinearLayoutAccept);
        admin_DgsLinearLayoutRejected = (LinearLayout) view.findViewById(R.id.admin_DgsLinearLayoutRejected);

        // admin IncomingList
        adminIncomingList = new AdminIncomingList(getActivity(), admin_DgsRecyclerViewIncoming, admin_DgsCardViewIncoming, admin_DgsLinearLayoutIncoming, "Dgs", "1");
        admin_DgsCardViewIncoming = adminIncomingList.admin_CardViewIncoming();
        adminAppItemIncomingAdapter = adminIncomingList.adminAppItemAdapter();

        // admin AcceptList
        adminAcceptList = new AdminAcceptList(getActivity(), admin_DgsRecyclerViewAccept, admin_DgsCardViewAccept, admin_DgsLinearLayoutAccept, "Dgs", "2");
        admin_DgsCardViewAccept = adminAcceptList.admin_CardViewIncoming();
        adminAppItemAcceptAdapter = adminAcceptList.adminAppItemAdapter();

        // admin RejectedList
        adminRejectedList = new AdminRejectedList(getActivity(), admin_DgsRecyclerViewRejected, admin_DgsCardViewRejected, admin_DgsLinearLayoutRejected, "Dgs", "3");
        admin_DgsCardViewRejected = adminRejectedList.admin_CardViewIncoming();
        adminAppItemRejectedAdapter = adminRejectedList.adminAppItemAdapter();

        // gelen Basvurular CardView extend event
        admin_DgsCardViewIncoming.setOnClickListener(new View.OnClickListener() {
            @Override
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(View view) {
                refresh();
                // https://www.youtube.com/watch?v=qIJ_U51s4ls&list=PLY0RqCbhFOzJZCQQ07rTTIt2YCGIxsR5-&index=18&t=421s
                int v = (admin_DgsRecyclerViewIncoming.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
                TransitionManager.beginDelayedTransition(admin_DgsLinearLayoutIncoming, new AutoTransition());
                admin_DgsRecyclerViewIncoming.setVisibility(v);
                // https://www.youtube.com/watch?v=qIJ_U51s4ls&list=PLY0RqCbhFOzJZCQQ07rTTIt2YCGIxsR5-&index=18&t=421s
            }
        });

        // onaylanan basvurular CardView extend event
        admin_DgsCardViewAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(View view) {
                refresh();
                // https://www.youtube.com/watch?v=qIJ_U51s4ls&list=PLY0RqCbhFOzJZCQQ07rTTIt2YCGIxsR5-&index=18&t=421s
                int v = (admin_DgsRecyclerViewAccept.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
                TransitionManager.beginDelayedTransition(admin_DgsLinearLayoutAccept, new AutoTransition());
                admin_DgsRecyclerViewAccept.setVisibility(v);
                // https://www.youtube.com/watch?v=qIJ_U51s4ls&list=PLY0RqCbhFOzJZCQQ07rTTIt2YCGIxsR5-&index=18&t=421s
            }
        });

        // red edilen basvurular CardView extend event
        admin_DgsCardViewRejected.setOnClickListener(new View.OnClickListener() {
            @Override
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(View view) {
                refresh();
                // https://www.youtube.com/watch?v=qIJ_U51s4ls&list=PLY0RqCbhFOzJZCQQ07rTTIt2YCGIxsR5-&index=18&t=421s
                int v = (admin_DgsRecyclerViewRejected.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
                TransitionManager.beginDelayedTransition(admin_DgsLinearLayoutRejected, new AutoTransition());
                admin_DgsRecyclerViewRejected.setVisibility(v);
                // https://www.youtube.com/watch?v=qIJ_U51s4ls&list=PLY0RqCbhFOzJZCQQ07rTTIt2YCGIxsR5-&index=18&t=421s
            }
        });



        // gelen basvurlar item click
        adminAppItemIncomingAdapter.setOnItemClickListener(new AdminAppItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AdminAppItemInfo adminAppItemInfo, int position) {
                System.out.println("item");
            }

            @Override
            public void onAcceptClick(AdminAppItemInfo adminAppItemInfo, int position) {
                System.out.println("kabul");
            }

            @Override
            public void onRejectClick(AdminAppItemInfo adminAppItemInfo, int position) {
                System.out.println("red");
            }

            @Override
            public void onDownloadClick(AdminAppItemInfo adminAppItemInfo, int position) {
                System.out.println("indirme");
            }
        });

        // onaylanan basvurlar item click
        adminAppItemAcceptAdapter.setOnItemClickListener(new AdminAppItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AdminAppItemInfo adminAppItemInfo, int position) {
                System.out.println(" onaylanan item");
            }

            @Override
            public void onAcceptClick(AdminAppItemInfo adminAppItemInfo, int position) {
                System.out.println(" onaylanan kabul");
            }

            @Override
            public void onRejectClick(AdminAppItemInfo adminAppItemInfo, int position) {
                System.out.println(" onaylanan red");
            }

            @Override
            public void onDownloadClick(AdminAppItemInfo adminAppItemInfo, int position) {
                System.out.println(" onaylanan indirme");
            }
        });

        // red edilen basvurlar item click
        adminAppItemRejectedAdapter.setOnItemClickListener(new AdminAppItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AdminAppItemInfo adminAppItemInfo, int position) {
                System.out.println(" red edilen item");
            }

            @Override
            public void onAcceptClick(AdminAppItemInfo adminAppItemInfo, int position) {
                System.out.println(" red edilen kabul");
            }

            @Override
            public void onRejectClick(AdminAppItemInfo adminAppItemInfo, int position) {
                System.out.println(" red edilen red");
            }

            @Override
            public void onDownloadClick(AdminAppItemInfo adminAppItemInfo, int position) {
                System.out.println(" red edilen indirme");
            }
        });

    }


    private void refresh() {
        adminIncomingList.refreshIncomingCardView();
        adminAcceptList.refreshIncomingCardView();
        adminRejectedList.refreshIncomingCardView();
    }


}