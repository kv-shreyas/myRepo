package com.example.redditappnew.ui.inbox;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.redditappnew.R;

public class InboxFragment extends Fragment {

    private InboxViewModel inboxViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        inboxViewModel =
                new ViewModelProvider(this).get(InboxViewModel.class);
        View root = inflater.inflate(R.layout.fragment_inbox, container, false);
        final TextView textView = root.findViewById(R.id.text_inbox);
        inboxViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}