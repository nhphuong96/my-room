package com.myroom.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class UtilityAdapter extends RecyclerView.Adapter<UtilityAdapter.UtilityViewHolder> {
    private Context context;

    public UtilityAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public UtilityViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull UtilityViewHolder utilityViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class UtilityViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUtilityName;

        public UtilityViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
