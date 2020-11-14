package com.example.redditappnew.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.redditappnew.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class LocationBottomSheet extends BottomSheetDialogFragment {


//    private EditText searchEt;
//    RecyclerView recyclerView;
//    Service apiInterface;
//    private LocationAdapter restaurantAdapter;
    private BottomSheetBehavior bottomSheetInfoBehavior;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.location_bottom_sheet, container,false);


        v.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return v;
    }






}
