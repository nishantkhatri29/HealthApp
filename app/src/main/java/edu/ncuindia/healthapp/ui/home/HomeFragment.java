package edu.ncuindia.healthapp.ui.home;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import edu.ncuindia.healthapp.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private Context context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        this.context = view.getContext();
        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this.context);
        if (acct != null) {
            String personName = acct.getDisplayName();
        } else {
            Toast.makeText(this.context, "You are not authorized!", Toast.LENGTH_LONG).show();
        }
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText("Logged in as "+acct.getDisplayName());
            }
        });
        return root;
    }
}