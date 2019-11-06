package edu.ncuindia.healthapp.ui.dashboard;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.ncuindia.healthapp.R;
import edu.ncuindia.healthapp.Database.UserModel;

public class DashboardFragment extends Fragment {

    private Context context;
    final Calendar mycalendar = Calendar.getInstance();
    private EditText name;
    private EditText date;
    private RadioGroup radioGroup;
    private RadioButton gender;
    private EditText height;
    private EditText weight;
    private Spinner country;
    private TextView stepGoal;
    private TextView weightGoal;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        final DashboardViewModel dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        this.context = getContext();
        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(context);

        name = (EditText) root.findViewById(R.id.nameField);
        date = (EditText) root.findViewById(R.id.dateField);
        radioGroup = root.findViewById(R.id.gender);
        height = (EditText) root.findViewById(R.id.heightLabel);
        weight = (EditText) root.findViewById(R.id.weightLabel);
        country = root.findViewById(R.id.spinner);
        stepGoal = root.findViewById(R.id.stepGoalField);
        weightGoal = root.findViewById(R.id.weightGoalField);

        LiveData<UserModel> data = dashboardViewModel.getUser(acct.getEmail());
        if (data != null) {
            data.observe(this, new Observer<UserModel>() {
                @Override
                public void onChanged(UserModel userModel) {
                    updateUI(userModel);
                }
            });
        }

        weight.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(3, 2)});
        height.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(3, 2)});

        name.setText(acct.getDisplayName());
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(v.getContext(), dateDialog, mycalendar.get(Calendar.YEAR), mycalendar.get(Calendar.MONTH), mycalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Button save = (Button) root.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserModel tempUserModel = new UserModel();
                tempUserModel.setName(name.getText().toString().trim());
                tempUserModel.setEmail(acct.getEmail());
                tempUserModel.setDate(date.getText().toString().trim());
                int selectedItem = radioGroup.getCheckedRadioButtonId();
                gender = root.findViewById(selectedItem);
                tempUserModel.setGender(gender.getText().toString().trim());
                if(height.getText().toString().isEmpty())
                    return;
                tempUserModel.setHeight(Integer.parseInt(height.getText().toString()));
                if(weight.getText().toString().isEmpty())
                    return;
                tempUserModel.setWeight(Integer.parseInt(weight.getText().toString()));
                tempUserModel.setCountry(country.getSelectedItem().toString().trim());
                if(stepGoal.getText().toString().isEmpty())
                    return;
                tempUserModel.setStepGoal(Integer.parseInt(stepGoal.getText().toString()));
                if(weightGoal.getText().toString().isEmpty())
                    return;
                tempUserModel.setWeightGoal(Integer.parseInt(weightGoal.getText().toString()));
                if(dashboardViewModel.setUser(tempUserModel))
                    Toast.makeText(context, "Database updated!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
        Spinner spinner = (Spinner) root.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.countries_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        return root;
    }

    private void updateUI(UserModel userModel) {
        name.setText(userModel.name);
        date.setText(userModel.date);
        if(userModel.gender.equals("Male"))
            radioGroup.check(R.id.male);
        else if(userModel.gender.equals("Female"))
            radioGroup.check(R.id.female);
        else if(userModel.gender.equals("Other"))
            radioGroup.check(R.id.other);
        height.setText(String.valueOf(userModel.height));
        weight.setText(String.valueOf(userModel.weight));
        stepGoal.setText(String.valueOf(userModel.stepGoal));
        weightGoal.setText(String.valueOf(userModel.weightGoal));
        String[] tempArray = getResources().getStringArray(R.array.countries_array);
        country.setSelection(binarySearch(tempArray, userModel.country));
    }

    public static int binarySearch(String[] sorted, String key) {
        int first = 0;
        int upto  = sorted.length;

        while (first < upto) {
            int mid = (first + upto) / 2;  // Compute mid point.
            if (key.compareTo(sorted[mid]) < 0) {
                upto = mid;       // repeat search in bottom half.
            } else if (key.compareTo(sorted[mid]) > 0) {
                first = mid + 1;  // Repeat search in top half.
            } else {
                return mid;       // Found it. return position
            }
        }
        return first;      // Failed to find key
    }

    class DecimalDigitsInputFilter implements InputFilter {
        private Pattern mPattern;
        DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
            mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?");
        }
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher matcher = mPattern.matcher(dest);
            if (!matcher.matches())
                return "";
            return null;
        }
    }

    DatePickerDialog.OnDateSetListener dateDialog = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dateofMonth) {
        mycalendar.set(year, month, dateofMonth);
        String dateFormat= "dd/MM/YYYY";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        date.setText(simpleDateFormat.format(mycalendar.getTime()));
        }
    };
}