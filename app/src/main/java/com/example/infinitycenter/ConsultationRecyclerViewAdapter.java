package com.example.infinitycenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ConsultationRecyclerViewAdapter extends RecyclerView.Adapter<ConsultationRecyclerViewAdapter.ViewHolder> {

    private ArrayList<ConsultationDetails> consultations;
    private Context mContext;

    public ConsultationRecyclerViewAdapter(Context context , ArrayList<ConsultationDetails> consultations) {
        this.mContext = context ;
        this.consultations = consultations;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.m_conultations_list_item , parent , false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String[] date = this.consultations.get(position).getDate().split("-");
        String[] months = {"NULL", "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        holder.day.setText(date[2]);
        holder.month.setText(months[Integer.parseInt(date[1])]);
        holder.year.setText(date[0]);
        holder.medicine.setText(consultations.get(position).getMedicine());
        holder.instructions.setText(consultations.get(position).getInstructions());
    }

    @Override
    public int getItemCount() {
        return consultations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView medicine , instructions , day , month , year ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            medicine = itemView.findViewById(R.id.c_medicine);
            instructions = itemView.findViewById(R.id.mc_instructions);
            day = itemView.findViewById(R.id.c_day);
            month = itemView.findViewById(R.id.c_month);
            year = itemView.findViewById(R.id.c_year);
        }
    }
}
