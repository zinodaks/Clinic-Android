package com.example.infinitycenter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AppointmentsRecyclerViewAdapter extends RecyclerView.Adapter<AppointmentsRecyclerViewAdapter.ViewHolder> {


    private ArrayList dates = new ArrayList();
    private ArrayList symptoms = new ArrayList();
    private Context mContext;
    private static final String TAG = "mytaggg";

    public AppointmentsRecyclerViewAdapter(Context context, ArrayList<String> dates, ArrayList symptoms) {
        this.mContext = context;
        this.symptoms = symptoms;
        this.dates = dates;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_list_item , parent , false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            final String[] date = this.dates.get(position).toString().split("-");
            String[] months = {"NULL"  , "JAN" , "FEB" , "MAR" , "APR" , "MAY" , "JUN" , "JUL" , "AUG" , "SEP" , "OCT" , "NOV" , "DEC"};
            holder.day.setText(date[2]);
            holder.month.setText(months[Integer.parseInt(date[1])]);
            holder.year.setText(date[0]);
            holder.symptoms.setText(this.symptoms.get(position).toString());

            holder.closeIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   final int positionX =  holder.getLayoutPosition();
                    String urlHttp = mContext.getResources().getString(R.string.httpURL) + "deleteAppointment.php";
                    StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, urlHttp, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, response);
                            try {
                                JSONObject serverResponse = new JSONObject(response);
                                if (serverResponse.getBoolean("error") == false) {
                                    symptoms.remove(positionX);
                                    dates.remove(positionX);
                                    notifyDataSetChanged();
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
                            params.put("username", SharedPreferencesManager.getInstance(mContext).getUser().getUsername());
                            params.put("date" , dates.get(positionX).toString());
                            return params;
                        }
                    };
                    VolleyInstance.getInstance(mContext).addToRequestQueue(stringRequest);
                }
            });
}

    @Override
    public int getItemCount() {
        return dates.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView closeIcon;
        TextView day, month, year, symptoms;
        LinearLayout date_display ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.day);
            month = itemView.findViewById(R.id.month);
            year = itemView.findViewById(R.id.year);
            symptoms = itemView.findViewById(R.id.symptoms_display);
            date_display = itemView.findViewById(R.id.date_display);
            closeIcon = itemView.findViewById(R.id.closeX);
        }
    }
}
