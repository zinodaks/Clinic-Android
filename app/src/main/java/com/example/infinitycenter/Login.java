package com.example.infinitycenter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    final static String TAG = "myTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void openRegister(View view) {
        finish();
        startActivity(new Intent(this, Register.class));
    }

    public void Login(View view) {
        String url = getResources().getString(R.string.httpURL) + "login.php";
        final String username = ((EditText) findViewById(R.id.l_username)).getText().toString().trim();
        final String password = ((EditText) findViewById(R.id.l_password)).getText().toString().trim();
        final ProgressBar progressBar = new ProgressBar(Login.this, null, android.R.attr.progressBarStyleLarge);
        if (!username.isEmpty() && !password.isEmpty()) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG , response);
                    try {
                        JSONObject serverResponse = new JSONObject(response);
                        if (serverResponse.getBoolean("success") == true) {
                            User user = null;
                            JSONObject userJSON = serverResponse.getJSONObject("user");
                            if (serverResponse.getBoolean("manager") == true) {
                                user = new User(userJSON.getString("username"));
                            } else if (serverResponse.getBoolean("manager") == false) {
                                user = new User(userJSON.getString("username"), userJSON.getString("f_name"), userJSON.getString("l_name"), userJSON.getString("dob"));
                            }
                            SharedPreferencesManager.getInstance(getApplicationContext()).userLogin(user);
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else if(serverResponse.getBoolean("success") == false){
                            TextView errorUP = findViewById(R.id.wrongUP);
                            errorUP.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }
                    } catch (JSONException ex) {
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Network error !", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("username", username);
                    params.put("password", password);
                    return params;
                }
            };
            RelativeLayout layout = findViewById(R.id.rel_log_lay);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(250, 250);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            layout.addView(progressBar, params);
            progressBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
            progressBar.setBackgroundResource(R.drawable.progressbar_background);
            progressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            hideSoftKeyboard(this);
            VolleyInstance.getInstance(this).addToRequestQueue(stringRequest);
        }
    }

    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
