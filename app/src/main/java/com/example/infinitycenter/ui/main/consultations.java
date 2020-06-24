package com.example.infinitycenter.ui.main;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.infinitycenter.ConsultationDetails;
import com.example.infinitycenter.ConsultationRecyclerViewAdapter;
import com.example.infinitycenter.R;
import com.example.infinitycenter.SharedPreferencesManager;
import com.example.infinitycenter.VolleyInstance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class consultations extends Fragment {

    private ArrayList<ConsultationDetails> consultations = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initConsultations();
    }

    public static consultations newInstance() {
        consultations fragment = new consultations();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_consultations, container, false);
    }

    private void initConsultations() {
        String url = getResources().getString(R.string.httpURL) + "getConsultations.php";
        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject serverResponse = new JSONObject(response);
                    if (serverResponse.getBoolean("error") == false) {
                        JSONArray consultations = serverResponse.getJSONArray("consultations");
                        handleConsultations(consultations);
                        initRecyclerView();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String , String> params = new HashMap<String , String>();
                params.put("username" , SharedPreferencesManager.getInstance(getContext()).getUser().getUsername());
                return params;
            }
        };
        VolleyInstance.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    private void handleConsultations(JSONArray consultations) {
        for (int i = 0; i < consultations.length(); i++) {
            try {
                JSONObject consultationsJSONObject = consultations.getJSONObject(i);
                JSONArray consultation = consultationsJSONObject.getJSONArray("consultation");
                ConsultationDetails consultationDetails = new ConsultationDetails(consultation.get(0).toString(), consultation.get(1).toString(), consultation.get(2).toString());
                this.consultations.add(consultationDetails);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.consultations_recycler_view);
        ConsultationRecyclerViewAdapter consultationRecyclerViewAdapter = new ConsultationRecyclerViewAdapter(getContext(), consultations);
        recyclerView.setAdapter(consultationRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
