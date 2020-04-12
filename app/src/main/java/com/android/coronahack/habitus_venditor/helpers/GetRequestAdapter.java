package com.android.coronahack.habitus_venditor.helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.coronahack.habitus_venditor.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetRequestAdapter extends RecyclerView.Adapter<GetRequestAdapter.RequestViewHolder> {


    public static List<EnterMeds> requestList;
    RecyclerView.Adapter requestListAdapter;
    public static Boolean isPresent = false;

    AlertDialog.Builder builder;
    AlertDialog alertDialog = null;

    private Context context;
    private List<GetRequest> getRequests;

    DatabaseReference insert, update;

    HashMap<String, Integer> hashMap = new HashMap<>();
    String customerName, customerPhNum, parent;
    int mKey1, countTest;
    boolean already = false;


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
        customerName = gr.getGetName();
        customerPhNum = gr.getGetPhNum();
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
                    getApproval();
                }
            });
        }
    }

    private void getApproval() {
        ViewGroup viewGroup = ((Activity) context).findViewById(android.R.id.content);
        View approveView = LayoutInflater.from(context).inflate(R.layout.approve_request, viewGroup, false);
        builder = new AlertDialog.Builder(context);
        builder.setView(approveView);
        alertDialog = builder
                .setCancelable(true)
                .create();
        alertDialog.show();

        insert = FirebaseDatabase.getInstance().getReference().child("timeslots");

        Button approve;
        final TextView checkText = approveView.findViewById(R.id.checkText);
        approve = approveView.findViewById(R.id.approveRequestButton);

        hashMap.put("9:00 am to 9:30 am", 0);
        hashMap.put("9:30 am to 10:00 am", 0);
        hashMap.put("10:00 am to 10:30 am", 0);
        hashMap.put("10:30 am to 11:00 am", 0);
        hashMap.put("11:00 am to 11:30 am", 0);
        hashMap.put("11:30 am to 12:00 pm", 0);
        hashMap.put("12:00 pm to 12:30 pm", 0);
        hashMap.put("12:30 pm to 1:00 pm", 0);
        hashMap.put("1:00 pm to 1:30 pm", 0);
        hashMap.put("1:30 pm to 2:00 pm", 0);

        DatabaseReference getCount = FirebaseDatabase.getInstance().getReference().child("timeslots");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    UploadTimeSlot up = dataSnapshot1.getValue(UploadTimeSlot.class);
                    if (up.name.equals(GlobalData.name)) {
                        hashMap.replace(up.timeSlot, up.count);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "" + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        };
        getCount.addValueEventListener(valueEventListener);

        final MaterialSpinner spinner = approveView.findViewById(R.id.spinner);
        spinner.setItems("9:00 am to 9:30 am", "9:30 am to 10:00 am", "10:00 am to 10:30 am", "10:30 am to 11:00 am", "11:00 am to 11:30 am",
                "11:30 am to 12:00 pm", "12:00 pm to 12:30 pm", "12:30 pm to 1:00 pm", "1:00 pm to 1:30 pm", "1:30 pm to 2:00 pm");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                checkText.setText("There are " + hashMap.get(item).toString() + " people in this time slot.");
            }
        });

        approve.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if (hashMap.get(spinner.getText().toString()) != 0) {
                    insert.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dss : dataSnapshot.getChildren()) {
                                UploadTimeSlot ur = dss.getValue(UploadTimeSlot.class);
                                if (ur.name.equals(GlobalData.name) && ur.timeSlot.equals(spinner.getText().toString())) {
                                    parent = dss.getKey();
                                    countTest = ur.count;
                                    DatabaseReference approved = FirebaseDatabase.getInstance().getReference().child("timeslots").child(parent).child("count");
                                    approved.setValue(countTest + 1);
                                    updatePrevious();
                                    alertDialog.cancel();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(context, "" + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    UploadTimeSlot uploadTimeSlot = new UploadTimeSlot(GlobalData.name, spinner.getText().toString(), hashMap.get(spinner.getText().toString()) + 1);
                    hashMap.replace(spinner.getText().toString(), hashMap.get(spinner.getText().toString()) + 1);
                    String uploadId = insert.push().getKey();
                    insert.child(uploadId)
                            .setValue(uploadTimeSlot)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(context, "Request approved!", Toast.LENGTH_SHORT).show();
                                    updatePrevious();
                                    alertDialog.cancel();
                                }
                            });
                }
            }
        });

    }

    private void updatePrevious() {
        update = FirebaseDatabase.getInstance().getReference().child(GlobalData.type);
        ValueEventListener valueEventListener1 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dss : dataSnapshot.getChildren()) {
                    UploadRequest ur = dss.getValue(UploadRequest.class);
                    if (ur.shopName.equals(GlobalData.name) && ur.customerName.equals(customerName) && ur.phNum.equals(customerPhNum)) {
                        parent = dss.getKey();
                        mKey1 = ur.mKey;
                        DatabaseReference approved = FirebaseDatabase.getInstance().getReference().child(GlobalData.type).child(parent).child("mKey");
                        approved.setValue(mKey1 + 1);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "" + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        };
        update.addListenerForSingleValueEvent(valueEventListener1);
    }

}
