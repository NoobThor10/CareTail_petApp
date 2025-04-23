package com.example.caretail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ClinicAdapter extends RecyclerView.Adapter<ClinicAdapter.ClinicViewHolder> {
    Context context;
    List<clinicModel> clinicList;

    public ClinicAdapter(Context context, List<clinicModel> clinicList) {
        this.context = context;
        this.clinicList = clinicList;
    }

    @NonNull
    @Override
    public ClinicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_clinic, parent, false);
        return new ClinicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClinicViewHolder holder, int position) {
        clinicModel clinic = clinicList.get(position);
        holder.name.setText(clinic.name);
        holder.location.setText(clinic.location);
        holder.hours.setText("Opens at " + clinic.openingHours);

        Glide.with(context)
                .load(clinic.imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return clinicList.size();
    }

    class ClinicViewHolder extends RecyclerView.ViewHolder {
        TextView name, location, hours;
        ImageView image;

        public ClinicViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.clinic_name);
            location = itemView.findViewById(R.id.clinic_location);
            hours = itemView.findViewById(R.id.clinic_hours);
            image = itemView.findViewById(R.id.clinic_image);
        }
    }
}
