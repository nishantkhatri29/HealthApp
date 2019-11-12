package edu.ncuindia.healthapp.ui.notifications;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.ncuindia.healthapp.FoodViewModel;
import edu.ncuindia.healthapp.ItemModal;
import edu.ncuindia.healthapp.ListAdapter;
import edu.ncuindia.healthapp.R;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private FoodViewModel mViewModel;
    List<ItemModal> itemModalList;
    private RecyclerView recyclerView;
    private ListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Context context;
    TextView count;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        this.context = root.getContext();

        recyclerView = root.findViewById(R.id.my_recycler_view);
        count = root.findViewById(R.id.count);
        recyclerView.setHasFixedSize(true);

        FoodViewModel model = ViewModelProviders.of(this).get(FoodViewModel.class);
        model.getItems().observe(this, new Observer<List<ItemModal>>() {
            @Override
            public void onChanged(List<ItemModal> item) {
                itemModalList = item;
                layoutManager = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(layoutManager);

                mAdapter = new ListAdapter(itemModalList, count);
                recyclerView.setAdapter(mAdapter);
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(FoodViewModel.class);
        // TODO: Use the ViewModel
    }
}