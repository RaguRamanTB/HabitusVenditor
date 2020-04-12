package com.android.coronahack.habitus_venditor.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.coronahack.habitus_venditor.R;

import java.util.List;

public class EnterMedAdapter extends RecyclerView.Adapter<EnterMedAdapter.TabsViewHolder> {

    private Context context;
    private List<EnterMeds> enterMeds;

    public EnterMedAdapter(Context context, List<EnterMeds> enterMeds) {
        this.context = context;
        this.enterMeds = enterMeds;
    }

    @NonNull
    @Override
    public EnterMedAdapter.TabsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.request_list, parent, false);
        return new TabsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EnterMedAdapter.TabsViewHolder holder, int position) {
        holder.itemView.setTag(enterMeds.get(position));
        EnterMeds em = enterMeds.get(position);
        holder.nameTab.setText(em.getName());
        holder.quantityTab.setText(em.getQuantity());
    }

    @Override
    public int getItemCount() {
        return enterMeds.size();
    }

    public class TabsViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTab, quantityTab;

        public TabsViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTab = itemView.findViewById(R.id.nameTab);
            quantityTab = itemView.findViewById(R.id.quantityTab);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EnterMeds enterMeds = (EnterMeds) v.getTag();
                    Toast.makeText(v.getContext(), enterMeds.getName()+" -> "+enterMeds.getQuantity(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
