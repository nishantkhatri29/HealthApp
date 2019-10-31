package edu.ncuindia.healthapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    private Context context;

    final Calendar mycalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

        EditText nameField = findViewById(R.id.nameField);
        EditText emailField = findViewById(R.id.emailField);
        EditText dateField = findViewById(R.id.dateField);

        nameField.setText(acct.getDisplayName());
        emailField.setText(acct.getEmail());
        dateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(v.getContext(), date, mycalendar.get(Calendar.YEAR), mycalendar.get(Calendar.MONTH), mycalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Button save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dateofMonth) {
            EditText editText = findViewById(R.id.dateField);
            mycalendar.set(year, month, dateofMonth);
            String dateFormat= "dd/MM/YYYY";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
            editText.setText(simpleDateFormat.format(mycalendar.getTime()));
        }
    };
}
