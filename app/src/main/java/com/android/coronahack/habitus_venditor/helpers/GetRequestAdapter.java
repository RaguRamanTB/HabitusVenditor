package com.android.coronahack.habitus_venditor.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.coronahack.habitus_venditor.R;

import java.util.ArrayList;
import java.util.List;

public class GetRequestAdapter extends RecyclerView.Adapter<GetRequestAdapter.RequestViewHolder> {


    public static List<EnterMeds> requestList;
    RecyclerView.Adapter requestListAdapter;
    public static Boolean isPresent;

    private Context context;
    private List<GetRequest> getRequests;

    public GetRequestAdapter(Context context, List<GetRequest> getRequests) {
        this.context = context;
        this.getRequests = getRequests;
    }

    @NonNull
    @Override
    public GetRequestAdapter.RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.request_item, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        holder.itemView.setTag(getRequests.get(position));
        GetRequest gr = getRequests.get(position);
        holder.name.setText(gr.getGetName());
        holder.phNum.setText(gr.getGetPhNum());
        holder.address.setText(gr.getGetAddress());
        isPresent = gr.getPres();

        requestList = new ArrayList<>();
//        holder.items.setHasFixedSize(true);
        holder.items.setLayoutManager(new LinearLayoutManager(context));

        for (EnterMeds rl : gr.getRequestList()) {
            requestList.add(new EnterMeds(rl.getName(), rl.getQuantity()));
            requestListAdapter = new EnterMedAdapter(context, requestList);
            holder.items.setAdapter(requestListAdapter);
        }
        requestListAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return getRequests.size();
    }


    public class RequestViewHolder extends RecyclerView.ViewHolder {

        TextView name, phNum, address;
        RecyclerView items;
        Button getPrescription;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.getName);
            phNum = itemView.findViewById(R.id.getPhoneNumber);
            address = itemView.findViewById(R.id.getAddress);
            items = itemView.findViewById(R.id.requestList);
            getPrescription = itemView.findViewById(R.id.getPrescription);

            if (GlobalData.type.equals("groceries")) {
                getPrescription.setVisibility(View.INVISIBLE);
            } else {
                getPrescription.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isPresent) {
                            Toast.makeText(context, "No prescription given", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Prescription given", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

}
