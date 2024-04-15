package com.about.kby.Adapter;

import static android.view.Gravity.CENTER;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.about.kby.Activity.EducationActivity;
import com.about.kby.Activity.EducationExistingActivity;
import com.about.kby.Bsession;
import com.about.kby.ProductConfig;
import com.about.kby.R;
import com.about.kby.fragment.EducationInputFragment;
import com.about.kby.model.ExistingModel;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExistingGroupAdapter extends RecyclerView.Adapter<ExistingGroupAdapter.Holder>{
    public Context context;
    public List<ExistingModel> papularModelList;

    String groupid="";
    public ExistingGroupAdapter(Context context, List<ExistingModel> itemList) {
        this.context = context;
        this.papularModelList = itemList;
    }

    @NonNull
    @Override
    public ExistingGroupAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.existing_group, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExistingGroupAdapter.Holder holder, @SuppressLint("RecyclerView") int position) {
        ExistingModel model = papularModelList.get(position);
        groupid = papularModelList.get(position).getGroup_id();
        holder.ll_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(context, EducationActivity.class);
                intent1.putExtra("grp_id",papularModelList.get(position).getGroup_id());
                intent1.putExtra("name",papularModelList.get(position).getGroup_name());
                intent1.putExtra("admin",papularModelList.get(position).getAdmin());
                context.startActivity(intent1);
            }
        });
                int a =Integer.parseInt(model.getGroup_count());
                int b =a+1;
            holder.team_members.setText("Total members : "+String.valueOf(b));


        holder.team.setText(model.getGroup_name());
        if (papularModelList.get(position).getAdmin().equalsIgnoreCase("1")){
            holder.edit.setVisibility(View.VISIBLE);
        }else{
            holder.edit.setVisibility(View.GONE);
        }
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup(position);
            }
        });

    }

    @SuppressLint("MissingInflatedId")
    private void popup(int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View popupView = inflater.inflate(R.layout.popup, null);
        ExistingModel model = papularModelList.get(position);
        EditText et_name = popupView.findViewById(R.id.et_name);
        Button save = popupView.findViewById(R.id.save);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        et_name.setText(model.getGroup_name());

        PopupWindow popupWindow = new PopupWindow(popupView,width,height,focusable);
        popupWindow.showAtLocation(popupView,CENTER,0,0);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString().trim();
                if (name.isEmpty()) {
                    et_name.setError("*Enter your name");
                    et_name.requestFocus();
                    return;
                }
                else {
                    final Map<String, String> params = new HashMap<>();
                    String para2 = "?user_id=" + Bsession.getInstance().getUser_id(context);
                    String para3 = "&grp_id="  + papularModelList.get(position).getGroup_id();                                                                                        ;
                    String para4 = "&grp_name=" + et_name.getText().toString().trim();
                    System.out.println("gggggg"+para3);
                    System.out.println("gggggg"+para2);
                    String baseUrl = ProductConfig.education_grp_update + para2 + para3+para4;

                    StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response.toString());
                            try {
                                JSONObject jsonResponse = new JSONObject(response);

                                if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                                    Toast.makeText(context, "Group name updated successfully", Toast.LENGTH_LONG).show();
                                    popupWindow.dismiss();
                                    ((EducationExistingActivity) ExistingGroupAdapter.this.context).packagelist();


                                } else {
                                    Toast.makeText(context, "Group name update failed...try it again", Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub
                            Log.e("Error", error.toString());
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(jsObjRequest);

                }
            }
        });
    }


    @Override
    public int getItemCount() {
        List<ExistingModel> list = this.papularModelList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView team;
        TextView team_members;
        ImageView edit;
        LinearLayout ll_group;
        public Holder(@NonNull View itemView) {
            super(itemView);
            team = itemView.findViewById(R.id.team);
            edit = itemView.findViewById(R.id.edit);
            ll_group = itemView.findViewById(R.id.ll_group);
            team_members = itemView.findViewById(R.id.team_members);
        }
    }
}
