package com.example.infinitycenter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private DatePickerDialog.OnDateSetListener onDateSetListener;
    public static final String TAG = "myTAG";
    String urlHttp;
    InputValidator inputValidator = new InputValidator();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        DatePicker((EditText) findViewById(R.id.date_of_birth));
        inputValidator.RegistrationInputWatcher(((EditText) findViewById(R.id.username)), (EditText) findViewById(R.id.first_name),
                (EditText) findViewById(R.id.last_name), (EditText) findViewById(R.id.ssn),
                (EditText) findViewById(R.id.password), (TextInputLayout) findViewById(R.id.lypassword),
                (EditText) findViewById(R.id.confirm_password), (TextInputLayout) findViewById(R.id.lyconfirm_password) , (Button) findViewById(R.id.register_button));
    }

    public void registerUser(String url, final String username, final String fname, final String lname, final String date, final String password, final String ssn) {
        final  ProgressBar progressBar = new ProgressBar(Register.this , null , android.R.attr.progressBarStyleLarge);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject serverResponse = new JSONObject(response);

                    if (serverResponse.getBoolean("error") == false) {
                        JSONObject userJson = serverResponse.getJSONObject("user");
                        User user = new User(userJson.getString("username"), userJson.getString("f_name"), userJson.getString("l_name"), userJson.getString("dob"));
                        SharedPreferencesManager.getInstance(getApplicationContext()).userLogin(user);
                        finish();
                        startActivity(new Intent(getApplicationContext() ,MainActivity.class));
                    } else if (serverResponse.getBoolean("error") == true) {
                        EditText etusername = (EditText) findViewById(R.id.username);
                        etusername.setError("This username is already taken");
                        etusername.requestFocus();
                        progressBar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                } catch (JSONException ex) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("fname", fname);
                params.put("lname", lname);
                params.put("dateOfBirth", date);
                params.put("password", password);
                params.put("ssn", ssn);
                return params;
            }
        };
        RelativeLayout layout = findViewById(R.id.rel_lay);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(250 , 250);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(progressBar , params);
        progressBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        progressBar.setBackgroundResource(R.drawable.progressbar_background);
        hideSoftKeyboard(this);
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        VolleyInstance.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void onRegister(View view) {
        if (inputValidator.getRegistrationErrors() == true) {
            urlHttp = getResources().getString(R.string.httpURL) + "register.php";
            String username = ((EditText) findViewById(R.id.username)).getText().toString().trim();
            String fName = ((EditText) findViewById(R.id.first_name)).getText().toString().trim();
            String lName = ((EditText) findViewById(R.id.last_name)).getText().toString().trim();
            String dob = ((EditText) findViewById(R.id.date_of_birth)).getText().toString().trim();
            String ssn = ((EditText) findViewById(R.id.ssn)).getText().toString().trim().replaceAll("[\\s\\-()]", "");
            String password = ((EditText) findViewById(R.id.password)).getText().toString().trim();
            registerUser(urlHttp, username, fName, lName, dob, password, ssn);
        }
    }

    public void hideSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void DatePicker(final EditText date) {

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Register.this, R.style.DialogTheme, onDateSetListener, year, month, day);
                hideSoftKeyboard(Register.this);
                datePickerDialog.show();
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String d = year + "/" + (month + 1) + "/" + dayOfMonth;
                date.setText(d);
                date.setSelection(date.getText().length());
            }
        };
    }

    public void openLogin(View view) {
        finish();
        startActivity(new Intent(this, Login.class));
    }

}

