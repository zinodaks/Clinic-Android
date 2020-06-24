package com.example.infinitycenter.ui.main;

import android.animation.Animator;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.infinitycenter.R;
import com.example.infinitycenter.AppointmentsRecyclerViewAdapter;
import com.example.infinitycenter.SharedPreferencesManager;
import com.example.infinitycenter.VolleyInstance;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class appointments extends Fragment {

    LottieAnimationView loading;

    static Dialog addAppointment;
    private TextView error;
    private EditText symptoms, date;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private ArrayList dates = new ArrayList();
    private ArrayList symptomsList = new ArrayList();
    RecyclerView recyclerView ;
    AppointmentsRecyclerViewAdapter appointmentsRecyclerViewAdapter;
    public final String TAG = "myTAG";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_appointments, container, false);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAppointments();
        addAppointment = new Dialog(getContext(), R.style.popDialogTheme);
        final FloatingActionButton fab = getActivity().findViewById(R.id.addAppointment);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUp();
            }
        });

    }

    public static appointments newInstance() {

        Bundle args = new Bundle();
        appointments fragment = new appointments();
        fragment.setArguments(args);
        return fragment;
    }

    public void showPopUp() {
        addAppointment.setContentView(R.layout.apppointments_popup_layout);
        date = (EditText) addAppointment.findViewById(R.id.appointment_date);
        symptoms = (EditText) addAppointment.findViewById(R.id.symptomsDescription);
        error = (TextView) addAppointment.findViewById(R.id.app_error);
        DatePicker(date, getContext());
        Button add = (Button) addAppointment.findViewById(R.id.appointment_button);
        addAppointments(add);
        addAppointment.show();
    }

    public void addAppointments(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!date.getText().toString().trim().isEmpty() && !symptoms.getText().toString().trim().isEmpty()) {
                    String urlHttp = getResources().getString(R.string.httpURL) + "addAppointment.php";
                    StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, urlHttp, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject serverResponse = new JSONObject(response);
                                if (serverResponse.getBoolean("error") == false) {
                                    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    addAppointment.setContentView(R.layout.loading_layout);
                                    loading = addAppointment.findViewById(R.id.loading);
                                    symptomsList.add(symptoms.getText().toString().trim());
                                    dates.add(date.getText().toString().trim().replaceAll("/" , "-"));
                                    appointmentsRecyclerViewAdapter.notifyDataSetChanged();
                                    loading.playAnimation();
                                    loading.addAnimatorListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animation) {
                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                            addAppointment.dismiss();
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animation) {
                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animation) {
                                        }
                                    });
                                } else if (serverResponse.getBoolean("error") == true) {
                                    error.setVisibility(View.VISIBLE);
                                }


                            } catch (JSONException ex) {

                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("username", SharedPreferencesManager.getInstance(appointments.super.getContext()).getUser().getUsername());
                            params.put("date", date.getText().toString().trim());
                            params.put("symptoms", symptoms.getText().toString().trim());
                            return params;
                        }
                    };
                    VolleyInstance.getInstance(getContext()).addToRequestQueue(stringRequest);
                }
            }
        });

    }


    public void DatePicker(final EditText date, final Context context) {
        Log.d(TAG, date.getText().toString());
        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();

                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, day);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.DialogTheme, onDateSetListener, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
                cal.set(Calendar.YEAR, year + 2);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, day);
                datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
                hideSoftKeyboard(context);
                datePickerDialog.show();

            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String day = (dayOfMonth < 10 ? "0" + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth));
                String d = year + "/" + (month + 1) + "/" + day;
                date.setText(d);
                date.setSelection(date.getText().length());
            }
        };
    }

    public void hideSoftKeyboard(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }


    private void initAppointments() {
        String url = getResources().getString(R.string.httpURL) + "getAppointments.php";
        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject serverResponse = new JSONObject(response);
                    JSONArray dates = serverResponse.getJSONArray("dates");
                    handleDates(dates);
                    JSONArray symptoms = serverResponse.getJSONArray("symptoms");
                    handleSymptoms(symptoms);
                    initRecyclerView();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", SharedPreferencesManager.getInstance(getContext()).getUser().getUsername());
                return params;
            }
        };
        VolleyInstance.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
    private void handleDates(JSONArray dates) {
        for(int i = 0 ; i < dates.length() ; i++){
            try {
                this.dates.add(dates.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private void handleSymptoms(JSONArray symptoms) {
        for (int i = 0  ; i < symptoms.length() ; i++) {
            try {
                this.symptomsList.add(symptoms.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private void initRecyclerView(){
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.appointments_recycler_view);
        appointmentsRecyclerViewAdapter = new AppointmentsRecyclerViewAdapter(getContext() , dates , symptomsList);
        recyclerView.setAdapter(appointmentsRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
