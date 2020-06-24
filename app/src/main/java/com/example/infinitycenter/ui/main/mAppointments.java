package com.example.infinitycenter.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.infinitycenter.AppointmentDetails;
import com.example.infinitycenter.MAppointmentsRVAdapter;
import com.example.infinitycenter.R;
import com.example.infinitycenter.SharedPreferencesManager;
import com.example.infinitycenter.VolleyInstance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class mAppointments extends Fragment {
    private static final String TAG = "myTag";

    private ArrayList<AppointmentDetails> appointments = new ArrayList<>();

    public static mAppointments newInstance() {
        mAppointments fragment = new mAppointments();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAppointments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_m_appointments, container, false);
    }


    private void initAppointments() {
        String url = getResources().getString(R.string.httpURL) + "getAppointments.php";
        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject serverResponse = new JSONObject(response);
                    if (serverResponse.getBoolean("error") == false) {
                        JSONArray appointments = serverResponse.getJSONArray("appointments");
                        handleAppointments(appointments);
                        initRecycleView();
                    }
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

    private void handleAppointments(JSONArray appointments) {
        for (int i = 0; i < appointments.length(); i++) {
            try {
                JSONObject appointmentsJSONObject = appointments.getJSONObject(i);
                JSONArray appointment = appointmentsJSONObject.getJSONArray("appointment");
                AppointmentDetails appointmentDetails = new AppointmentDetails(appointment.get(0).toString(), appointment.get(1).toString(), appointment.get(2).toString());
                this.appointments.add(appointmentDetails);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void initRecycleView() {
        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.m_appointments_recyclerview);
        MAppointmentsRVAdapter mAppointmentsRVAdapter = new MAppointmentsRVAdapter(getContext(), appointments);
        recyclerView.setAdapter(mAppointmentsRVAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
