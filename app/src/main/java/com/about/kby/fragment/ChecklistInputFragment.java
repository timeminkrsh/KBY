package com.about.kby.fragment;

import static java.util.Calendar.getInstance;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.about.kby.Bsession;
import com.about.kby.ProductConfig;
import com.about.kby.R;
import com.about.kby.model.CheckModel;
import com.about.kby.model.ChecklistModel;
import com.about.kby.model.DateModel;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChecklistInputFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChecklistInputFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String statuschecklist = "";
    ArrayList<ChecklistModel> namelist = new ArrayList<>();
    ArrayList<DateModel> datemodelist = new ArrayList<>();
    ArrayList<CheckModel> itemList = new ArrayList<>();

    public ChecklistInputFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChecklistInputFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChecklistInputFragment newInstance(String param1, String param2) {
        ChecklistInputFragment fragment = new ChecklistInputFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private static ChecklistInputFragment instance;

    public static ChecklistInputFragment getInstances() {
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            System.out.println("param1:" + mParam1);
        }
    }

    RecyclerView calendarView, recycltask, calendarViewnewlist;
    Spinner fromcalantor;
    int itemLists;
    private DateAdapter dateAdapter;
    List<String> datesList = new ArrayList<>();
    ProgressDialog progressDialog;
    LinearLayout addbutton, tasklay;
    private List<Integer> imageList;
    String taskId, tack_name = "";
    TextView txt;
    EditText tasknames;
    Button addtask_btn;
    ImageView imageview;
    static String ta_id = "";
    String flg = "0";
    private ExecutorService executor;
    private long lastInputsRunTime = 0; // Initialize to 0
    private boolean isInputsRunning = false;
    ProgressBar progressBar;
    HorizontalScrollView horizontalview;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checklist_input, container, false);

        fromcalantor = view.findViewById(R.id.fromcalantor);
        progressBar = view.findViewById(R.id.progressBar);
        calendarView = view.findViewById(R.id.calendarView);
        addbutton = view.findViewById(R.id.addbutton);
        tasknames = view.findViewById(R.id.tasknames);
        imageview = view.findViewById(R.id.imageview);
        calendarViewnewlist = view.findViewById(R.id.calendarViewnewlist);
        addtask_btn = view.findViewById(R.id.addtask_btn);
        recycltask = view.findViewById(R.id.recycltask);
        instance = this;
        executor = Executors.newFixedThreadPool(10);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Please wait ....");
        progressDialog.setIcon(R.drawable.logo);
        Bundle args = getArguments();
        if (args != null) {
            taskId = args.getString("task_id");
            tack_name = args.getString("tack_name");

            // Now you have the task_id value here, you can use it as needed
        }
        addtask_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = tasknames.getText().toString().trim();
                if (name.isEmpty()) {
                    tasknames.setError("*Enter your Task");
                    tasknames.requestFocus();
                    return;
                } else {
                    final Map<String, String> params = new HashMap<>();
                    String para2 = "?task_id=" + taskId;
                    String para1 = "&task_name=" + name;
                    // progressDialog.show();
                    String baseUrl = ProductConfig.checklistmultiple + para2 + para1;

                    StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response.toString());
                            try {
                                // progressDialog.dismiss();
                                JSONObject jsonResponse = new JSONObject(response);

                                if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                                    // Toast.makeText(getContext(), "Your profile has been updated", Toast.LENGTH_LONG).show();
                                    //hideKeyboard(getContext(),v);

                                    name_list();
                                    tasknames.getText().clear();
                                    addtask_btn.setClickable(false);
                                } else {
                                    // Toast.makeText(getContext(), "Something went wrong your ... Try it again.Please check People Details...", Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                //progressDialog.dismiss();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub
                            Log.e("Error", error.toString());
                            // progressDialog.dismiss();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                    requestQueue.add(jsObjRequest);

                }
            }
        });

        final String[] months = new DateFormatSymbols().getMonths();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, months);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromcalantor.setAdapter(adapter);

        currentMonthSetSelectionSpinner(months);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 6);
        calendarView.setLayoutManager(gridLayoutManager);
        dateAdapter = new DateAdapter(new ArrayList<>());
        calendarView.setAdapter(dateAdapter);
        // name_list();
        fromcalantor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedMonth = months[position];
                int monthIndex = position; // Month index from 0 to 11
                fromcalantor.setTag(selectedMonth);
                generateDatesForMonth(monthIndex);
                name_list();
                calendarView.setVisibility(View.GONE);
                calendarViewnewlist.setVisibility(View.GONE);
                //Toast.makeText(getContext(), "1111111", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i("onNothingSelected--", "");

                // Do nothing
            }
        });

        return view;
    }

    private void currentMonthSetSelectionSpinner(String[] months){
        int month = Calendar.getInstance().get(Calendar.MONTH);
        String stringToMatch = new DateFormatSymbols().getMonths()[month];
        // String to match
        int position = -1;
        for (int i = 0; i < months.length; i++) {
            if (months[i].equals(stringToMatch)) {
                position = i;
                break;
            }
        }

        if (position != -1) {
            fromcalantor.setSelection(position);
        }

    }

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private void name_list() {
        final Map<String, String> params = new HashMap<>();
        String para_str = "?task_id=" + taskId;
        String baseUrl = ProductConfig.tasklist + para_str;
        namelist = new ArrayList<>();
        //  progressDialog.show();
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    //  progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                        JSONArray jsonlist = jsonResponse.getJSONArray("task_list");
                        for (int j = 0; j < jsonlist.length(); j++) {
                            JSONObject jsonObject = jsonlist.getJSONObject(j);
                            ChecklistModel model = new ChecklistModel();

                            model.setId(jsonObject.getString("id"));
                            model.setName(jsonObject.getString("tack_name"));
                            namelist.add(model);
                        }
                        recycltask.setVisibility(View.VISIBLE);
                        TaskAdapter packageListAdapter = new TaskAdapter(getContext(), namelist);
                        recycltask.setAdapter(packageListAdapter);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
                        recycltask.setLayoutManager(gridLayoutManager);
                        recycltask.setHasFixedSize(true);
                        addtask_btn.setClickable(true);

                    } else if (jsonResponse.has("text") && jsonResponse.getString("text").equals("Task List Empty!")) {
                        recycltask.setVisibility(View.GONE);
                        calendarView.setVisibility(View.GONE);
                        calendarViewnewlist.setVisibility(View.GONE);
                        imageview.setVisibility(View.VISIBLE);
                        addtask_btn.setClickable(true);

                        //  Toast.makeText(getContext(), "Existing group not found", Toast.LENGTH_SHORT).show();
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

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsObjRequest);
    }

    private void createdatebasedinput(int numberOfItems) {
        itemList = new ArrayList<>();
        CheckModel best = new CheckModel();
        System.out.println("datesList" + numberOfItems);
        for (int i = 0; i < numberOfItems; i++) {
            CheckModel checkModel = new CheckModel(); // Assuming CheckModel is your item model class
            itemList.add(checkModel);
        }
    }


    private void generateDatesForMonth(int monthIndex) {
        datesList.clear();
        // Get the current year
        int year = Calendar.getInstance().get(Calendar.YEAR);
        // Get the number of days in the selected month
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthIndex);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Generate dates for the selected month
        for (int i = 1; i <= daysInMonth; i++) {
            String date = monthIndex + 1 + "/" + i; // Format: MM/DD/YYYY
            datesList.add(date);
            System.out.println("iii=" + i);
            itemLists = i;

        }

        // Update the RecyclerView with the list of dates for the selected month
        dateAdapter.updateDatesList(datesList);
        createdatebasedinput(daysInMonth);
    }

    public class DateAdapter extends RecyclerView.Adapter<DateAdapter.DateViewHolder> {

        private List<String> datesList;
        private List<DateModel> datesmodelList;
        private List<Integer> imageList;
        Context context;

        public DateAdapter(List<String> datesList) {
            this.datesList = datesList;

        }

        public DateAdapter(Context context, ArrayList<DateModel> datemodelist) {
            this.context = context;
            this.datesmodelList = datemodelist;
        }

        @SuppressLint("NotifyDataSetChanged")
        public void updateDatesList(List<String> updatedList) {
            datesList.clear();
            datesList.addAll(updatedList);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.datelist, parent, false);
            return new DateViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
            holder.bind(position);

        }

        @Override
        public int getItemCount() {
            return datesList.size();
        }

        class DateViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            Spinner spinner_image;
            Switch switchButton;
            int i = 0;

            DateViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.textdate);
                switchButton = itemView.findViewById(R.id.switchButton);
            }

            void bind(int position) {
                String date = datesList.get(position);
                textView.setText(date);

            }
        }
    }

    public class DatelistAdapter extends RecyclerView.Adapter<DatelistAdapter.DateViewHolder> {

        private List<DateModel> datesmodelList;
        private List<Integer> imageList;
        Context context;

        public DatelistAdapter(Context context, ArrayList<DateModel> datemodelist) {
            this.context = context;
            this.datesmodelList = datemodelist;
        }

     /*   @SuppressLint("NotifyDataSetChanged")
        public void updateDatesList(List<String> updatedList) {
            datesmodelList.clear();
            datesmodelList.addAll(updatedList);
            notifyDataSetChanged();
        }*/

        @NonNull
        @Override
        public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.datelist, parent, false);
            return new DateViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DateViewHolder holder, @SuppressLint("RecyclerView") int position) {
//            holder.bind(position);
            calendarView.setVisibility(View.GONE);
            DateModel model = datesmodelList.get(position);
            holder.textView.setText(model.getDate());
            if (Objects.equals(model.getStatus(), "0")) {
                holder.switchButton.setChecked(false);
                // Toast.makeText(getContext(), "check11", Toast.LENGTH_SHORT).show();

            } else {
                holder.switchButton.setChecked(true);
                // Toast.makeText(getContext(), "check22", Toast.LENGTH_SHORT).show();
            }

            holder.switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {

                        int i = 1;
                        //inputs(i, date, ta_id);
                        //  Toast.makeText(getContext(), "check11", Toast.LENGTH_SHORT).show();
                        updatedate(datesmodelList.get(position).getId(), i);
                    } else {
                       int i=0;
                        updatedate(datesmodelList.get(position).getId(), i);

                        //  Toast.makeText(getContext(), "check22", Toast.LENGTH_SHORT).show();

                    }
                }
            });


        }

        @Override
        public int getItemCount() {
            return datesmodelList.size();
        }

        class DateViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            Spinner spinner_image;
            Switch switchButton;
            int i = 0;

            DateViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.textdate);
                switchButton = itemView.findViewById(R.id.switchButton);
            }

            /*void bind(int position) {
                calendarView.setVisibility(View.GONE);
                DateModel model = datesmodelList.get(position);
                textView.setText(model.getDate());
                if (Objects.equals(model.getStatus(), "0")) {
                    switchButton.setChecked(false);
                    // Toast.makeText(getContext(), "check11", Toast.LENGTH_SHORT).show();

                } else {
                    switchButton.setChecked(true);
                    // Toast.makeText(getContext(), "check22", Toast.LENGTH_SHORT).show();
                }

                switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {

                            i = 1;
                            //inputs(i, date, ta_id);
                            //  Toast.makeText(getContext(), "check11", Toast.LENGTH_SHORT).show();
                            updatedate(datesmodelList.get(position).getId(), i);
                        } else {
                            i=0;
                            updatedate(datesmodelList.get(position).getId(), i);

                            //  Toast.makeText(getContext(), "check22", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }*/
        }

        public void updateItems(List<DateModel> list){
            this.datesmodelList = list;
            notifyDataSetChanged();

        }
    }

    private void updatedate(String id, int staus) {
        final Map<String, String> params = new HashMap<>();
        String para_str = "?id=" + id;
        String para_str1 = "&status=" + staus;
        String baseUrl = ProductConfig.update_date + para_str + para_str1;
        Log.i("baseUrl--",baseUrl.toString());
        datemodelist = new ArrayList<>();
        progressDialog.show();
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {

                        datelist_check(ta_id, fromcalantor.getTag());

                    } else {
                        // Toast.makeText(getContext(), "Existing group not found", Toast.LENGTH_SHORT).show();
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
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsObjRequest);
    }

/*
    public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.Holder> {
        public Context context;
        public List<ChecklistModel> papularModelList;
        String tack_name, taskid = "";
        private int selectedPos = -1;
        int values = 0;
        int flag = 0;

        public TaskAdapter(Context context, List<ChecklistModel> itemList) {
            this.context = context;
            this.papularModelList = itemList;
        }

        @NonNull
        @Override
        public TaskAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spiner, parent, false);
            return new TaskAdapter.Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskAdapter.Holder holder, @SuppressLint("RecyclerView") int position) {
            ChecklistModel model = papularModelList.get(position);

            holder.task.setText(model.getName());
            ta_id = papularModelList.get(position).getId();
            //datelist_check(ta_id,fromcalantor.getTag());
            methods(ta_id, fromcalantor.getTag());
            final boolean isSelected = position == selectedPos;
            holder.itemView.setBackgroundColor(isSelected ? Color.parseColor("#81B5FA") : Color.parseColor("#FFEB3B"));

            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removem(papularModelList.get(position).getId());
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.itemView.setClickable(false);
                    ta_id = papularModelList.get(position).getId();
                    datelist_check(ta_id, fromcalantor.getTag());
                    methods(papularModelList.get(position).getId(), fromcalantor.getTag());
                    System.out.println("iddd====" + ta_id);
                    //calendarView.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (Objects.equals(statuschecklist, "0")) {
                                calendarView.setVisibility(View.GONE);
                                calendarViewnewlist.setVisibility(View.VISIBLE);
                                imageview.setVisibility(View.GONE);

                            } else {
                                calendarViewnewlist.setVisibility(View.GONE);
                                calendarView.setVisibility(View.VISIBLE);
                                processDatesSequentially(datesList, ta_id, 0);
                                imageview.setVisibility(View.GONE);
                                flag = 0;

                            }

                        }
                    }, 1000);

                    notifyItemChanged(selectedPos);
                    selectedPos = holder.getLayoutPosition();
                    notifyItemChanged(selectedPos);

                }
            });

        }

        private void removem(String id) {
            final Map<String, String> params = new HashMap<>();
            String baseUrl = ProductConfig.delete_task + "?id=" + id;

            final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Response", response.toString());
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {

                            Toast.makeText(context, "Removed", Toast.LENGTH_SHORT).show();
                            ChecklistInputFragment.getInstances().name_list();
                            System.out.println("detletedd=" + id);

                        } else {
                            Toast.makeText(context, "Activity not found", Toast.LENGTH_SHORT).show();
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
                            // Handle the error response here
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

        private void methods(String taId, Object tag) {
            final Map<String, String> params = new HashMap<>();
            String para_str = "?checklist_id=" + taId;
            String para_str1 = "&month=" + tag;
            String baseUrl = ProductConfig.statuschecklist + para_str + para_str1;
            System.out.println("bsecurl=" + baseUrl);
            datemodelist = new ArrayList<>();
            // progressDialog.show();
            final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Response", response.toString());
                    try {
                        progressDialog.dismiss();
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {

                            statuschecklist = jsonResponse.getString("status");
                            System.out.println("statuschecklist=" + statuschecklist);


                        } else {
                            //  Toast.makeText(getContext(), "checkstatus", Toast.LENGTH_SHORT).show();
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

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(jsObjRequest);
        }


        private ExecutorService executor = Executors.newFixedThreadPool(1);

        private void inputs(int status, String date, String ta_id, Runnable callback) {

            if (isInputsRunning) {
                // If inputs method is already running, return to avoid multiple executions
                return;
            }
            isInputsRunning = true;
            String userid = Bsession.getInstance().getUser_id(getContext());
            final Map<String, String> params = new HashMap<>();

            String para_str = "?user_id=" + userid;
            String para_str1 = "&checklist_id=" + ta_id;
            String para_str2 = "&date=" + date;
            String para_str3 = "&status=" + status;
            String para_str4 = "&month=" + fromcalantor.getTag();
            String baseUrl = ProductConfig.check_list_input + para_str + para_str1 + para_str2 + para_str3 + para_str4;

            StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Response", response.toString());
                    try {
                        JSONObject jsonResponse = new JSONObject(response);

                        if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                            jsonResponse.getString("id");
                            jsonResponse.getString("status");

                            if (callback != null) {
                                callback.run();
                            }
                            lastInputsRunTime = System.currentTimeMillis();
                            long lastRunTime = getLastInputsRunTime(); // Call getLastInputsRunTime() here
                            System.out.println("Last inputs method run time: " + lastRunTime);
                            System.out.println("Last inputs method run time: " + isInputsRunning);

                        } else if (jsonResponse.has("success") && jsonResponse.getString("success").equals("0")) {
                            // Handle failure
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (callback != null) {
                            callback.run();
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Error", error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(jsObjRequest);
            isInputsRunning = false;
        }

        private void processDatesSequentially(List<String> datesList, String ta_id, int index) {
            if (index < datesList.size()) {
                String date = datesList.get(index);
                showProgressBar();
                executor.execute(() -> inputs(0, date, ta_id, () -> {
                    // Process next date after current is done
                    processDatesSequentially(datesList, ta_id, index + 1);
                }));
            } else {
                System.out.println("Last index: " + (index - 1));
                values = index - 1;
                datelist_check(ta_id, fromcalantor.getTag());
                progressDialog.dismiss();
                hideProgressBar();
            }
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
            TextView task;
            ImageView remove;

            public Holder(@NonNull View itemView) {
                super(itemView);
                task = itemView.findViewById(R.id.task);
                remove = itemView.findViewById(R.id.remove);
            }
        }
    }
*/
    public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.Holder> {
        public Context context;
        public List<ChecklistModel> papularModelList;
        private int selectedPos = -1;
    private int values;
    private boolean isClickProcessing = false;

        public TaskAdapter(Context context, List<ChecklistModel> itemList) {
            this.context = context;
            this.papularModelList = itemList;
        }

        @NonNull
        @Override
        public TaskAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spiner, parent, false);
            return new TaskAdapter.Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskAdapter.Holder holder, @SuppressLint("RecyclerView") int position) {
            ChecklistModel model = papularModelList.get(position);

            holder.task.setText(model.getName());

            final boolean isSelected = position == selectedPos;
            holder.itemView.setBackgroundColor(isSelected ? Color.parseColor("#81B5FA") : Color.parseColor("#FFEB3B"));

            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removem(model.getId());
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isClickProcessing) {
                        return;
                    }
                    isClickProcessing = true;
                    holder.itemView.setClickable(false);

                    String taId = model.getId();
                    methods(taId, fromcalantor.getTag(),holder);

                   /* datelist_check(taId, fromcalantor.getTag());

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (Objects.equals(statuschecklist, "0")) {
                                calendarView.setVisibility(View.GONE);
                                calendarViewnewlist.setVisibility(View.VISIBLE);
                                imageview.setVisibility(View.GONE);
                            } else {
                                calendarViewnewlist.setVisibility(View.GONE);
                                calendarView.setVisibility(View.VISIBLE);
                                processDatesSequentially(datesList, taId, 0);
                                imageview.setVisibility(View.GONE);
                            }

                            notifyItemChanged(selectedPos);
                            selectedPos = holder.getLayoutPosition();
                            notifyItemChanged(selectedPos);

                            holder.itemView.setClickable(true);
                            isClickProcessing = false;
                        }
                    }, 3000); // Adjust delay as necessary*/
                }
            });
        }

        private void removem(String id) {
            final Map<String, String> params = new HashMap<>();
            String baseUrl = ProductConfig.delete_task + "?id=" + id;

            final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Response", response);
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                            Toast.makeText(context, "Removed", Toast.LENGTH_SHORT).show();
                            ChecklistInputFragment.getInstances().name_list();
                        } else {
                            Toast.makeText(context, "Activity not found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
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

        private void methods(String taId, Object tag, Holder holder) {
            String userid = Bsession.getInstance().getUser_id(context);
            final Map<String, String> params = new HashMap<>();

            String para_str = "?user_id=" + userid;
            String para_str1 = "&checklist_id=" + taId;
            String para_str2 = "&month=" + tag;
            String baseUrl = ProductConfig.statuschecklist + para_str + para_str1 + para_str2;

            datemodelist = new ArrayList<>();

            final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Response", response);
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                            statuschecklist = jsonResponse.getString("status");

                            datelist_check(taId, fromcalantor.getTag());

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (Objects.equals(statuschecklist, "0")) {
                                        calendarView.setVisibility(View.GONE);
                                        calendarViewnewlist.setVisibility(View.VISIBLE);
                                        imageview.setVisibility(View.GONE);
                                    } else {
                                        calendarViewnewlist.setVisibility(View.GONE);
                                        calendarView.setVisibility(View.VISIBLE);
                                        processDatesSequentially(datesList, taId, 0);
                                        imageview.setVisibility(View.GONE);
                                    }

                                    notifyItemChanged(selectedPos);
                                    selectedPos = holder.getLayoutPosition();
                                    notifyItemChanged(selectedPos);

                                    holder.itemView.setClickable(true);
                                    isClickProcessing = false;
                                }
                            }, 1000); // Adjust delay as necessary
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
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

        private ExecutorService executor = Executors.newFixedThreadPool(1);
        private boolean isInputsRunning = false;

        private void inputs(int status, String date, String ta_id, Runnable callback) {
            if (isInputsRunning) {
                return;
            }
            isInputsRunning = true;

            String userid = Bsession.getInstance().getUser_id(context);
            final Map<String, String> params = new HashMap<>();

            String para_str = "?user_id=" + userid;
            String para_str1 = "&checklist_id=" + ta_id;
            String para_str2 = "&date=" + date;
            String para_str3 = "&status=" + status;
            String para_str4 = "&month=" + fromcalantor.getTag();
            String baseUrl = ProductConfig.check_list_input + para_str + para_str1 + para_str2 + para_str3 + para_str4;

            StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Response", response);
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                            if (callback != null) {
                                callback.run();
                            }
                        } else if (jsonResponse.has("success") && jsonResponse.getString("success").equals("0")) {
                            // Handle failure
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (callback != null) {
                            callback.run();
                        }
                    }
                    isInputsRunning = false;
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Error", error.toString());
                    isInputsRunning = false;
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

        private void processDatesSequentially(List<String> datesList, String ta_id, int index) {
            if (index < datesList.size()) {
                String date = datesList.get(index);
                showProgressBar();
                executor.execute(() -> inputs(0, date, ta_id, () -> {
//                    processDatesSequentially(datesList, ta_id, index + 1);
                    processDatesSequentially(datesList, ta_id, datesList.size());
                }));
            } else {
                values = index - 1;
                datelist_check(ta_id, fromcalantor.getTag());
                progressDialog.dismiss();
                hideProgressBar();
            }
        }

        @Override
        public int getItemCount() {
            return papularModelList == null ? 0 : papularModelList.size();
        }

        public class Holder extends RecyclerView.ViewHolder {
            TextView task;
            ImageView remove;

            public Holder(@NonNull View itemView) {
                super(itemView);
                task = itemView.findViewById(R.id.task);
                remove = itemView.findViewById(R.id.remove);
            }
        }
    }

    public long getLastInputsRunTime() {
        return lastInputsRunTime;
    }

    private void datelist_check(String taId, Object tag) {

        String userid = Bsession.getInstance().getUser_id(getContext());
        final Map<String, String> params = new HashMap<>();

        String para_str = "?user_id=" + userid;
        String para_str1 = "&checklist_id=" + taId;
        String para_str2 = "&month=" + tag;
        String baseUrl = ProductConfig.datelist_check + para_str + para_str1 + para_str2;
        Log.i("=======","datemodelist==baseUrl="+baseUrl);
        datemodelist = new ArrayList<>();
        //  progressDialog.show();
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                        JSONArray jsonlist = jsonResponse.getJSONArray("task_list");
                        for (int j = 0; j < jsonlist.length(); j++) {
                            JSONObject jsonObject = jsonlist.getJSONObject(j);
                            DateModel model = new DateModel();

                            model.setId(jsonObject.getString("id"));
                            model.setDate(jsonObject.getString("date"));
                            model.setStatus(jsonObject.getString("status"));
                            datemodelist.add(model);
                            calendarView.setVisibility(View.GONE);
                            calendarViewnewlist.setVisibility(View.VISIBLE);
                            imageview.setVisibility(View.GONE);

                        }

                        Log.i("=======","datemodelist=="+datemodelist.size());
                        Log.i("=======","datemodelist=="+datemodelist);

                        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 6);
                        calendarViewnewlist.setLayoutManager(layoutManager);
                        DatelistAdapter packageListAdapter = new DatelistAdapter(getContext(), datemodelist);
                        calendarViewnewlist.setAdapter(packageListAdapter);
                        calendarViewnewlist.setHasFixedSize(true);

//                        packageListAdapter.updateItems(datemodelist);





                    } else {
                        // Toast.makeText(getContext(), "List Not Found111", Toast.LENGTH_SHORT).show();
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

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsObjRequest);
    }

    private void showProgressBar() {
        progressDialog.show();
    }

    private void hideProgressBar() {
        progressDialog.dismiss();
    }
}