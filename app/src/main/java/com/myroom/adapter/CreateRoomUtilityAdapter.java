package com.myroom.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.myroom.R;
import com.myroom.application.BaseApplication;
import com.myroom.database.repository.UtilityRepository;
import com.myroom.database.dao.Utility;
import com.myroom.service.IUtilityService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class CreateRoomUtilityAdapter extends RecyclerView.Adapter<CreateRoomUtilityAdapter.CreateRoomUtilityViewHolder> {

    @Inject public IUtilityService utilityService;
    private List<UtilitySelection> utilitySelectionList;

    public CreateRoomUtilityAdapter(Context context) {
        BaseApplication.getRepositoryComponent(context).inject(this);
        initializeUtilitySelectionList();
    }

    private void initializeUtilitySelectionList() {
        utilitySelectionList = new ArrayList<>();
        List<Utility> allUtilities = utilityService.readAllAvailableUtility();
        for (Utility utility : allUtilities) {
            utilitySelectionList.add(new UtilitySelection(utility, false));
        }
    }

    @Override
    public CreateRoomUtilityViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_create_room_utility, viewGroup, false);
        CreateRoomUtilityViewHolder createRoomUtilityViewHolder = new CreateRoomUtilityViewHolder(view);
        return createRoomUtilityViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CreateRoomUtilityViewHolder createRoomUtilityViewHolder, final int i) {
        UtilitySelection utilitySelection = utilitySelectionList.get(i);
        createRoomUtilityViewHolder.tvUtilityName.setText(utilitySelection.getUtility().getName());
        createRoomUtilityViewHolder.cbUtility.setChecked(utilitySelection.getSelected());
        createRoomUtilityViewHolder.cbUtility.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    UtilitySelection currentUtilitySelection = utilitySelectionList.get(i);
                    currentUtilitySelection.setSelected(true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return utilitySelectionList.size();
    }

    public List<UtilitySelection> getUtilitySelectionList() {
        return utilitySelectionList;
    }

    public class CreateRoomUtilityViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUtilityName;
        private AppCompatCheckBox cbUtility;

        public CreateRoomUtilityViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUtilityName = itemView.findViewById(R.id.item_create_room_utility_name);
            cbUtility = itemView.findViewById(R.id.item_create_room_utility_check_box);
        }
    }

    public class UtilitySelection {
        private Utility utility;
        private Boolean isSelected;

        public UtilitySelection(Utility utility, Boolean isSelected) {
            this.utility = utility;
            this.isSelected = isSelected;
        }

        public Utility getUtility() {
            return utility;
        }

        public void setUtility(Utility utility) {
            this.utility = utility;
        }

        public Boolean getSelected() {
            return isSelected;
        }

        public void setSelected(Boolean selected) {
            isSelected = selected;
        }
    }
}
