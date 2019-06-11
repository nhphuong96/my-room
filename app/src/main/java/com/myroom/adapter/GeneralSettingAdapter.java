package com.myroom.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myroom.R;
import com.myroom.activity.GeneralSettingOtherActivity;
import com.myroom.activity.GeneralSettingUtilityActivity;
import com.myroom.core.GeneralSettingId;
import com.myroom.dto.GeneralSettingItem;

import java.util.List;

public class GeneralSettingAdapter extends RecyclerView.Adapter<GeneralSettingAdapter.GeneralSettingViewHolder> {
    private Context parentContext;
    List<GeneralSettingItem> generalSettingItemList;

    public GeneralSettingAdapter(Context context, List<GeneralSettingItem> generalSettingItemList) {
        this.parentContext = context;
        this.generalSettingItemList = generalSettingItemList;
    }

    @NonNull
    @Override
    public GeneralSettingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_general_setting, viewGroup, false);
        GeneralSettingViewHolder generalSettingViewHolder = new GeneralSettingViewHolder(view);
        return generalSettingViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GeneralSettingViewHolder generalSettingViewHolder, int i) {
        GeneralSettingItem generalSettingItem = generalSettingItemList.get(i);
        generalSettingViewHolder.id = generalSettingItem.getId();
        generalSettingViewHolder.tvGeneralSettingName.setText("   " + generalSettingItem.getName());
        generalSettingViewHolder.tvGeneralSettingName.setCompoundDrawablesWithIntrinsicBounds(generalSettingItem.getImageRes(), 0, 0, 0);
    }

    @Override
    public int getItemCount() {
        return generalSettingItemList.size();
    }

    public class GeneralSettingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvGeneralSettingName;
        private GeneralSettingId id;

        public GeneralSettingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGeneralSettingName = itemView.findViewById(R.id.item_general_setting_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (id) {
                case UTILITY:
                    Intent utilityIntent = new Intent(parentContext, GeneralSettingUtilityActivity.class);
                    parentContext.startActivity(utilityIntent);
                    break;
                case OTHER:
                    Intent otherIntent = new Intent(parentContext, GeneralSettingOtherActivity.class);
                    parentContext.startActivity(otherIntent);
                default:
            }
        }
    }
}
