package com.example.infinitycenter;

import android.animation.Animator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MAppointmentsRVAdapter extends RecyclerView.Adapter<MAppointmentsRVAdapter.ViewHolder> {

    private ArrayList<AppointmentDetails> appointments = new ArrayList();
    static Dialog consultationDialog;
    private LottieAnimationView loading;
    private EditText medication, instructions;
    private TextView error;
    private Context mContext;
    private static final String TAG = "constlt";

    public MAppointmentsRVAdapter(Context context, ArrayList<AppointmentDetails> appointments) {
        this.mContext = context;
        this.appointments = appointments;
        consultationDialog = new Dialog(mContext, R.style.popDialogTheme);
        consultationDialog.setContentView(R.layout.consultations_layout);
        medication = consultationDialog.findViewById(R.id.medicine);
        instructions = consultationDialog.findViewById(R.id.instructions);
        error = consultationDialog.findViewById(R.id.consulterror);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.m_appointment_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        String[] date = this.appointments.get(position).getDate().split("-");
        String[] months = {"NULL", "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        holder.day.setText(date[2]);
        holder.month.setText(months[Integer.parseInt(date[1])]);
        holder.year.setText(date[0]);
        holder.username.setText(appointments.get(position).getUsername());
        holder.symptoms.setText(appointments.get(position).getSymptoms());
        holder.closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int positionX = holder.getLayoutPosition();
                deleteAppointment(positionX);
            }
        });
        holder.consultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultationDialog.show();
                Button consultConf = consultationDialog.findViewById(R.id.consultation_button);
                consultConf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG , "clicked " + holder.getLayoutPosition() );
                        consult(holder.getLayoutPosition());
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    private void deleteAppointment(final int positionX) {
        String urlHttp = mContext.getResources().getString(R.string.httpURL) + "deleteAppointment.php";
        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, urlHttp, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject serverResponse = new JSONObject(response);
                    if (serverResponse.getBoolean("error") == false) {
                        appointments.remove(positionX);
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
                params.put("username", appointments.get(positionX).getUsername().toString());
                params.put("date", appointments.get(positionX).getDate().toString());
                return params;
            }
        };
        VolleyInstance.getInstance(mContext).addToRequestQueue(stringRequest);
    }

    private void consult(final int positionX) {
        Log.d(TAG , String.valueOf(positionX));
        if (!medication.getText().toString().trim().isEmpty() && !instructions.getText().toString().trim().isEmpty()) {
            String urlHttp = mContext.getResources().getString(R.string.httpURL) + "addConsultation.php";
            StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, urlHttp, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject serverResponse = new JSONObject(response);

                        if (serverResponse.getBoolean("error") == false) {
                            consultationDialog.setContentView(R.layout.loading_layout);
                            loading = consultationDialog.findViewById(R.id.loading);
                            ((Activity) mContext).getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            loading.playAnimation();
                            loading.addAnimatorListener(new Animator.AnimatorListener() {

                                @Override
                                public void onAnimationStart(Animator animation) {
                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    deleteAppointment(positionX);
                                    notifyDataSetChanged();
                                    ((Activity) mContext).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    consultationDialog.dismiss();
                                    consultationDialog.setContentView(R.layout.consultations_layout);
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {
                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {
                                }
                            });
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
                    params.put("username", appointments.get(positionX).getUsername().toString());
                    params.put("date", appointments.get(positionX).getDate().toString());
                    params.put("medicine", medication.getText().toString());
                    params.put("instructions", instructions.getText().toString());
                    return params;
                }
            };
            VolleyInstance.getInstance(mContext).addToRequestQueue(stringRequest);
        } else {
            error.setVisibility(View.VISIBLE);
        }
    }

    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView closeIcon;
        TextView day, month, year, symptoms, username;
        Button consultButton;
        LinearLayout date_display;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.m_day);
            month = itemView.findViewById(R.id.m_month);
            year = itemView.findViewById(R.id.m_year);
            symptoms = itemView.findViewById(R.id.m_symptoms_display);
            date_display = itemView.findViewById(R.id.m_date_display);
            username = itemView.findViewById(R.id.m_username);
            consultButton = itemView.findViewById(R.id.consult);
            closeIcon = itemView.findViewById(R.id.m_closeX);
        }
    }
}
