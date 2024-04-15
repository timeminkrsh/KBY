package com.about.kby.Adapter;

import static android.view.Gravity.CENTER;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.about.kby.Activity.ChecklistActivity;
import com.about.kby.Activity.ChecklistAddActivity;
import com.about.kby.Activity.EducationAddActivity;
import com.about.kby.Activity.FamilyActivity;
import com.about.kby.Activity.PersonalExistingActivity;
import com.about.kby.Bsession;
import com.about.kby.ProductConfig;
import com.about.kby.R;
import com.about.kby.model.ChecklistModel;
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

public class PersonalGroupAdapter extends RecyclerView.Adapter<PersonalGroupAdapter.Holder>{
    public Context context;
    public List<ChecklistModel> papularModelList;
    private String groupid="";

    public PersonalGroupAdapter(Context context, List<ChecklistModel> itemList) {
        this.context = context;
        this.papularModelList = itemList;
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkadd, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonalGroupAdapter.Holder holder, @SuppressLint("RecyclerView") int position) {
        ChecklistModel model = papularModelList.get(position);
        groupid = model.getId();
        holder.ll_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(context, FamilyActivity.class);
                intent.putExtra("grup_id",papularModelList.get(position).getId());
                intent.putExtra("team_name",papularModelList.get(position).getName());
                context.startActivity(intent);
            }
        });
        holder.txt_check.setText(model.getName());
        holder.check_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context,view);
                popupMenu.inflate(R.menu.checklist_popup);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.add_edu) {
                            LayoutInflater inflater = LayoutInflater.from(context);
                            View popupView = inflater.inflate(R.layout.popup, null);
                            ChecklistModel model = papularModelList.get(position);
                            EditText et_name = popupView.findViewById(R.id.et_name);
                            Button save = popupView.findViewById(R.id.save);
                            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                            boolean focusable = true;
                            et_name.setText(model.getName());

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
                                        String para2 = "?grp_id=" + groupid;
                                        String para3 = "&grp_name=" + et_name.getText().toString().trim();
                                        String baseUrl = ProductConfig.personal_grp_update + para2 + para3;

                                        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                Log.e("Response", response.toString());
                                                try {
                                                    JSONObject jsonResponse = new JSONObject(response);

                                                    if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                                                        Toast.makeText(context, "Personal group name successfully updated ", Toast.LENGTH_LONG).show();
                                                        popupWindow.dismiss();
                                                        ((PersonalExistingActivity) PersonalGroupAdapter.this.context).personal_fetch();

                                                    } else {
                                                        Toast.makeText(context, "Something went wrong ... Try it again", Toast.LENGTH_LONG).show();
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

                        } else if (menuItem.getItemId() == R.id.exit_edu) {
                            final Map<String, String> params = new HashMap<>();

                            String para2 = "?user_id=" + Bsession.getInstance().getUser_id(context);
                            String para3 = "&grp_id=" +groupid;
                            String baseUrl = ProductConfig.personal_delete + para2 + para3;

                            StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.e("Response", response.toString());
                                    try {
                                        JSONObject jsonResponse = new JSONObject(response);
                                        if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                                            papularModelList.clear();
                                            ((PersonalExistingActivity) PersonalGroupAdapter.this.context).personal_fetch();
                                            Toast.makeText(context, "Personal group deleted", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(context, "Only admin can delete the group", Toast.LENGTH_LONG).show();
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
                        return false;
                    }
                });
            }
        });
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
        RelativeLayout ll_team;
        ImageView check_more;
        TextView txt_check;
        public Holder(@NonNull View itemView) {
            super(itemView);
            ll_team  = itemView.findViewById(R.id.ll_team);
            txt_check  = itemView.findViewById(R.id.txt_check);
            check_more  = itemView.findViewById(R.id.check_more);
        }
    }
}
