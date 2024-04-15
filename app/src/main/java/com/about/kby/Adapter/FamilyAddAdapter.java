package com.about.kby.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.about.kby.Activity.EducationAddActivity;
import com.about.kby.Activity.FamilyAddActivity;
import com.about.kby.Bsession;
import com.about.kby.ProductConfig;
import com.about.kby.R;
import com.about.kby.model.ChecklistModel;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FamilyAddAdapter extends RecyclerView.Adapter<FamilyAddAdapter.Holder>{
    public Context context;
    public List<ChecklistModel> papularModelList;
    String group_id ="";
    String member_id ="";
    private ProgressDialog progressDialog;
    public FamilyAddAdapter(Context context, List<ChecklistModel> namelist) {
        this.context = context;
        this.papularModelList = namelist;
    }

    @NonNull
    @Override
    public FamilyAddAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.addlist, parent, false);
        return new Holder(view);

    }

    public void showProgressDialog() {
        if (context != null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull FamilyAddAdapter.Holder holder, int position) {
        ChecklistModel model = papularModelList.get(position);
        group_id=model.getId();
        member_id = model.getMem_id();
        holder.remove_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context,view);
                popupMenu.inflate(R.menu.remove_popup);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        remove_name();
                        return false;
                    }
                });
            }
        });
        holder.add_name.setText(model.getName());
    }

    private void remove_name() {
        final Map<String, String> params = new HashMap<>();
        String para_str  = "?user_id=" + Bsession.getInstance().getUser_id(context);
        String para_str1 = "&grp_id=" +group_id;
        String para_str2 = "&member_id="+member_id;
        String baseUrl = ProductConfig.personal_grp_delete+ para_str+para_str1+para_str2;
        papularModelList = new ArrayList<>();
        showProgressDialog();
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    hideProgressDialog();
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                        ((FamilyAddActivity) FamilyAddAdapter.this.context).name_list();
                        Toast.makeText(context, "Group member name removed successfully", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(context, " Group member name not found", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.toString());
                        hideProgressDialog();
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

    @Override
    public int getItemCount() {
        List<ChecklistModel> list = this.papularModelList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView add_name;
        ImageView remove_more;
        public Holder(@NonNull View itemView) {
            super(itemView);
            add_name = itemView.findViewById(R.id.add_name);
            remove_more = itemView.findViewById(R.id.remove_more);
        }
    }
}
