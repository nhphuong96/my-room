package com.myroom.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.myroom.R;
import com.myroom.application.BaseApplication;
import com.myroom.database.repository.UtilityRepository;
import com.myroom.database.dao.Utility;

import java.util.List;

import javax.inject.Inject;

public class UtilityAdapter extends RecyclerView.Adapter<UtilityAdapter.UtilityViewHolder> {
    private Context context;
    private List<Utility> utilityList;

    @Inject
    public UtilityRepository utilityRepository;

    public UtilityAdapter(Context context) {
        this.context = context;
        BaseApplication.getRepositoryComponent(context).inject(this);
        loadAllUtilityList();
    }

    private void loadAllUtilityList() {
        utilityList = utilityRepository.findAll();
    }

    @NonNull
    @Override
    public UtilityViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_utility, viewGroup, false);
        UtilityViewHolder utilityViewHolder = new UtilityViewHolder(view);
        return utilityViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UtilityViewHolder utilityViewHolder, int i) {
        Utility utility = utilityList.get(i);
        utilityViewHolder.utilityKey = utility.getUtilityKey();
        utilityViewHolder.tvUtilityName.setText(utility.getName());
    }

    @Override
    public int getItemCount() {
        return utilityList.size();
    }

    public class UtilityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView tvUtilityName;
        private long utilityKey;
        public UtilityViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUtilityName = itemView.findViewById(R.id.item_utility_name);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context,  tvUtilityName.getText().toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean onLongClick(View v) {
            new AlertDialog.Builder(context)
                    .setTitle("Delete").setMessage("Are you sure you want to delete this utility?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int pos = getAdapterPosition();
                            Utility utility = utilityList.get(pos);
                            if (utilityRepository.delete(utility.getUtilityKey())) {
                                utilityList.remove(pos);
                                Toast.makeText(context, "Delete utility " + utility.getName() + " successfully.", Toast.LENGTH_SHORT).show();
                                notifyDataSetChanged();
                            }
                            else {
                                Toast.makeText(context, "Could not delete this utility!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton("No", null)
                    .setIcon(R.drawable.ic_alert)
                    .show();
            return true;
        }
    }
}
