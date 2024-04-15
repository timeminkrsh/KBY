package com.about.kby.fragment;

import static androidx.core.app.NotificationCompat.getColor;
import static java.util.Calendar.getInstance;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.about.kby.Bsession;
import com.about.kby.ProductConfig;
import com.about.kby.R;
import com.about.kby.model.PersonalModel;
import com.about.kby.model.PersonalPojo;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.model.CalendarEvent;
import devs.mulham.horizontalcalendar.utils.CalendarEventsPredicate;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonalInputFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalInputFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<PersonalModel> itemList = new ArrayList<>();
    private List<PersonalPojo> PojoList = new ArrayList<>();
    private ArrayList<PersonalModel> countries_list = new ArrayList<>();
    ArrayList<String> pincodelist = new ArrayList<String>();
    ArrayList<String> pincodelists = new ArrayList<String>();
    RecyclerView recycler_table, recycler_tables;
    List<String> teamList = new ArrayList<>();
    TableLayout tableLayouts;
    LinearLayout addbutton, save;
    Button today;
    ProgressDialog progressDialog;
    String selectedValue = "";
    TableLayout tableLayout;
    String subcat_id;
    ArrayList<String> itemLists = new ArrayList();
    List<View> tableRows = new ArrayList<>();

    public PersonalInputFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonalInputFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonalInputFragment newInstance(String param1, String param2) {
        PersonalInputFragment fragment = new PersonalInputFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private static PersonalInputFragment instance;

    public static PersonalInputFragment getInstances() {
        return instance;
    }

    String categorys = "";
    Spinner spinners;
    TextView txt_date;
    EditText simpleTimePicker;
    int tableViewCount = 0;
    String duriation = "";

    List<Spinner> spinnersList = new ArrayList<>();
    List<TextView> categoryList = new ArrayList<>();
    List<EditText> timePickerList = new ArrayList<>();
    public static String selectedDateStr;
    private int addRowCount = 0;
    private PersonalAdapter personalAdapter;
    HorizontalCalendar horizontalCalendar;
    Spinner fromcalantor;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_input, container, false);
        recycler_table = view.findViewById(R.id.recycler_table);
        recycler_tables = view.findViewById(R.id.recycler_tables);
        today = view.findViewById(R.id.today);
        txt_date = view.findViewById(R.id.txt_date);
        addbutton = view.findViewById(R.id.addbutton);
        save = view.findViewById(R.id.save);
        tableLayout = view.findViewById(R.id.tableLayout);
        fromcalantor = view.findViewById(R.id.fromcalantor);
        Calendar startDate = getInstance();
        startDate.add(Calendar.MONTH, -2);

        instance = this;
        ProgressDialog progressDialog2 = new ProgressDialog(getContext());
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading.....");

        /* end after 2 months from now */
        Calendar endDate = getInstance();
        endDate.add(Calendar.MONTH, 2);
        final String[] months = new DateFormatSymbols().getMonths();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, months);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fromcalantor.setAdapter(adapter);
        // Default Date set to Today.
        final Calendar defaultSelectedDate = getInstance();
        Calendar currentCalendar = Calendar.getInstance();
        int currentMonthIndex = currentCalendar.get(Calendar.MONTH);

// Set the spinner to the current month
        fromcalantor.setSelection(currentMonthIndex);
        fromcalantor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedMonth = months[position];
                int monthIndex = position; // Month index from 0 to 11
                fromcalantor.setTag(selectedMonth);
                updateCalendarForMonth(monthIndex);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.buttonadds);

        horizontalCalendar = new HorizontalCalendar.Builder(view, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(7)
                .configure()
                .formatTopText("")
                .formatMiddleText("dd")
                .formatBottomText("EEE")
                .showTopText(true)
                .showBottomText(true)
                .textColor(Color.WHITE, Color.WHITE)
                .colorTextMiddle(Color.WHITE, Color.parseColor("#000000"))
                .selectedDateBackground(drawable)
                .textSize(14, 14, 14)
                .end()
                .defaultSelectedDate(defaultSelectedDate)
                .build();

        Log.i("Default Date", DateFormat.format("EEE, MMM d, yyyy", defaultSelectedDate).toString());
        selectedDateStr = DateFormat.format("yyyy-MM-dd", defaultSelectedDate).toString();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                selectedDateStr = DateFormat.format("yyyy-MM-dd", date).toString();
                //   Toast.makeText(getContext(), selectedDateStr + " selected!", Toast.LENGTH_SHORT).show();
                Log.i("onDateSelected", selectedDateStr + " - Position = " + position);
                txt_date.setText(DateFormat.format("d-MMM-yyyy", date).toString());
                personal_user_input_fetch(selectedDateStr);
            }

        });

        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalCalendar.goToday(false);
                Calendar currentDate = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                selectedDateStr = dateFormat.format(currentDate.getTime());
                System.out.println("yyyy-M-d==" + selectedDateStr);
                txt_date.setText("Today");
                // Do something with today's date, for example, display it in a toast
                // Toast.makeText(getContext(), "Today's date: " + todayDateStr, Toast.LENGTH_SHORT).show();
            }
        });
        personal_user_input_fetch(selectedDateStr);


        LinearLayout addrow = view.findViewById(R.id.addbutton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (duriation.isEmpty()) {
                    Toast.makeText(getContext(), "Duriation is empty!!!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    insertmetod(selectedValue, duriation, categorys);
                    itemList.clear();
                    //duriation="";

                }
            }
        });
        addrow.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                int numberOfRows = itemList.size();
                System.out.println("numberrows" + numberOfRows);
                System.out.println("duriation" + duriation);
                if (itemList.isEmpty()) {
                    PersonalModel newItem = new PersonalModel();
                    itemList.add(newItem);

                    if (personalAdapter == null) {
                        personalAdapter = new PersonalAdapter(getContext(), itemList);
                        recycler_table.setAdapter(personalAdapter);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                        recycler_table.setLayoutManager(layoutManager);
                    } else {
                        personalAdapter.notifyDataSetChanged();
                    }
                    // Toast.makeText(getContext(), "111", Toast.LENGTH_SHORT).show();
                } else {
                    if (duriation.isEmpty()) {
                        Toast.makeText(getContext(), "Duration is empty!!!", Toast.LENGTH_SHORT).show();
                    } else {
                        insertmetod(selectedValue, duriation, categorys);
                        itemList.clear();
                        duriation = "";
                        //  Toast.makeText(getContext(), "222", Toast.LENGTH_SHORT).show();
                        PersonalModel newItem = new PersonalModel();
                        itemList.add(newItem);
                        PersonalAdapter personalAdapter = new PersonalAdapter(getContext(), itemList);
                        recycler_table.setAdapter(personalAdapter);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                        recycler_table.setLayoutManager(layoutManager);

                    }
                }
            }
        });

        return view;
    }

    private void updateCalendarForMonth(int month) {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();

        startDate.set(Calendar.MONTH, month);
        startDate.set(Calendar.DAY_OF_MONTH, 1);

        endDate.set(Calendar.MONTH, month);
        endDate.set(Calendar.DAY_OF_MONTH, endDate.getActualMaximum(Calendar.DAY_OF_MONTH));

        Log.d("UpdateCalendar", "Start Date: " + startDate.getTime().toString());
        Log.d("UpdateCalendar", "End Date: " + endDate.getTime().toString());

        horizontalCalendar.setRange(startDate, endDate);
        horizontalCalendar.refresh();
    }

    private void insertmetod(String selectedValue, String duriation, String categorys) {
        String userid = Bsession.getInstance().getUser_id(getContext());
        System.out.println("phone;;" + userid);
        final Map<String, String> params = new HashMap<>();
        //progressDialog.show();
        if (selectedDateStr == null) {
            Toast.makeText(getContext(), "Please Select Date", Toast.LENGTH_SHORT).show();
            Calendar currentDate = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            selectedDateStr = dateFormat.format(currentDate.getTime());
        }
        String para_str = "?user_id=" + userid;
        String para_str1 = "&cat_id=" + categorys;
        String para_str2 = "&sub_cat_id=" + selectedValue;
        String para_str3 = "&date=" + selectedDateStr;
        String para_str4 = "&time=" + duriation;
        String baseUrl = ProductConfig.personal_user_input_insert + para_str + para_str1 + para_str2 + para_str3 + para_str4;
        System.out.println("para1" + para_str);
        System.out.println("para1" + para_str1);
        System.out.println("para1" + para_str2);
        System.out.println("para1" + para_str3);
        System.out.println("para1" + para_str4);
        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);

                    if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                        personal_user_input_fetch(selectedDateStr);
                    } else if (jsonResponse.has("success") && jsonResponse.getString("success").equals("0")) {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
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

    public void personal_user_input_fetch(String selectedDateStr) {
        final Map<String, String> params = new HashMap<>();
        String baseUrl = ProductConfig.personal_user_input_fetch + "?user_id=" + Bsession.getInstance().getUser_id(getContext()) + "&date=" + selectedDateStr;
        recycler_tables.setVisibility(View.VISIBLE);
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                        PojoList = new ArrayList<>();

                        JSONArray jsonlist = jsonResponse.getJSONArray("member_list");
                        for (int j = 0; j < jsonlist.length(); j++) {
                            JSONObject jsonObject1 = jsonlist.getJSONObject(j);
                            PersonalPojo personalPojo = new PersonalPojo();
                            personalPojo.setId(jsonObject1.getString("id"));
                            personalPojo.setCategory(jsonObject1.getString("category"));
                            personalPojo.setSubategory(jsonObject1.getString("sub_category"));
                            personalPojo.setDuriation(jsonObject1.getString("time"));

                            PojoList.add(personalPojo);
                            PersonalListAdapter personalAdapter = new PersonalListAdapter(getContext(), PojoList);
                            recycler_tables.setAdapter(personalAdapter);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                            recycler_tables.setLayoutManager(layoutManager);

                        }

                    } else {

                        recycler_tables.setVisibility(View.GONE);

                        //Toast.makeText(getContext(), "Activity not found", Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsObjRequest);
    }


    private void subcategory(Spinner spinners) {
        final Map<String, String> params = new HashMap<>();
        String baseUrl = ProductConfig.subcategory;

        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    pincodelist.clear();
                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                        countries_list = new ArrayList<>();
                        JSONArray jsonlist = jsonResponse.getJSONArray("subcategory_list");
                        for (int j = 0; j < jsonlist.length(); j++) {
                            JSONObject jsonObject1 = jsonlist.getJSONObject(j);
                            PersonalModel model1 = new PersonalModel();
                            model1.setId(jsonObject1.getString("Subcategory_id"));
                            subcat_id = model1.getId();
                            model1.setSubcategory(jsonObject1.getString("name"));
                            pincodelist.add(jsonObject1.getString("name"));
                            pincodelists.add(jsonObject1.getString("Subcategory_id"));
                            countries_list.add(model1);
                            createDropdownList(pincodelist, spinners);
                        }

                    } else {
                        // Toast.makeText(getContext(), "Subcategory not found", Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsObjRequest);
    }

    private void createDropdownList(ArrayList<String> pincodelist, Spinner spinners) {

        ArrayAdapter adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, pincodelist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinners.setAdapter(adapter);
        System.out.println("itemlistid==" + pincodelist);
        System.out.println("itemlistid==" + selectedValue);


    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }


    public class PersonalAdapter extends RecyclerView.Adapter<PersonalAdapter.ItemViewHolder> {
        private List<PersonalModel> itemList;
        private List<PersonalPojo> pesrsonalList;
        private Context mContext;

        public PersonalAdapter(Context mContext, List<PersonalModel> itemList) {
            this.mContext = mContext;
            this.itemList = itemList;

        }


        @NonNull
        @Override
        public PersonalAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tablepersonal, parent, false);
            return new PersonalAdapter.ItemViewHolder(itemView);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull PersonalAdapter.ItemViewHolder holder, @SuppressLint("RecyclerView") int position) {

            PersonalModel model = itemList.get(position);

            holder.simpleTimePicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar calendar = Calendar.getInstance();
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                            getContext(),
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    // Display the selected time in HH:MM format
                                    String formattedTime = String.format("%02d:%02d", hourOfDay, minute);
                                    //   Toast.makeText(getContext(), "Selected time: " + formattedTime, Toast.LENGTH_SHORT).show();
                                    holder.simpleTimePicker.setText(formattedTime);
                                    duriation = holder.simpleTimePicker.getText().toString();
                                    System.out.println("Selected tim=" + duriation);
                                    insertmetod(selectedValue, duriation, categorys);
                                    itemList.clear();
                                    holder.simpleTimePicker.setText("");


                                }
                            },
                            hour, minute, true);

                    timePickerDialog.show();
                }
            });
            holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedValue = parent.getItemAtPosition(position).toString();

                    // Do something with the selected value
                    System.out.println("selectedValue==" + selectedValue);
                    System.out.println("selectvv==" + id);
                    model.setSubcategory(selectedValue);

                    categorymethod(selectedValue, holder.category);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Do nothing
                }
            });
            subcategory(holder.spinner);
        }


        private void categorymethod(String selectedItemID, TextView category) {
            final Map<String, String> params = new HashMap<>();
            String para2 = "?name=" + selectedItemID;
            System.out.println("para2==" + para2);
            // progressDialog.show();
            String baseUrl = ProductConfig.category + para2;

            StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Response", response.toString());
                    try {
                        progressDialog.dismiss();
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                            JSONArray jsonlist = jsonResponse.getJSONArray("category_list");

                            // Assuming that the TextView is inside the tableview layout

                            for (int j = 0; j < jsonlist.length(); j++) {
                                JSONObject jsonObject1 = jsonlist.getJSONObject(j);
                                PersonalModel model1 = new PersonalModel();
                                model1.setId(jsonObject1.getString("category_id"));
                                model1.setCategory(jsonObject1.getString("name"));

                                // Set the text to the TextView
                                category.setText(jsonObject1.getString("name"));
                                categorys = model1.getId();
                                countries_list.add(model1);
                            }
                        } else {
                            // Toast.makeText(getContext(), "Category not found", Toast.LENGTH_SHORT).show();
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

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {
            TextView category;
            Spinner spinner;
            EditText simpleTimePicker;
            TableLayout tableLayout;

            public ItemViewHolder(View itemView) {
                super(itemView);
                spinner = itemView.findViewById(R.id.spinner);
                simpleTimePicker = itemView.findViewById(R.id.edittext);
                category = itemView.findViewById(R.id.category);
                tableLayout = itemView.findViewById(R.id.tableLayout);

            }
        }
    }

    public static class PersonalListAdapter extends RecyclerView.Adapter<PersonalListAdapter.ItemViewHolder> {
        private List<PersonalPojo> pesrsonalList;
        private Context mContext;

        public PersonalListAdapter(Context mContext, List<PersonalPojo> pesrsonalList) {
            this.mContext = mContext;
            this.pesrsonalList = pesrsonalList;

        }


        @NonNull
        @Override
        public PersonalListAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tablepersonal, parent, false);
            return new PersonalListAdapter.ItemViewHolder(itemView);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull PersonalListAdapter.ItemViewHolder holder, @SuppressLint("RecyclerView") int position) {

            PersonalPojo model = pesrsonalList.get(position);
            holder.spinner.setVisibility(View.GONE);
            holder.txtspinner.setVisibility(View.VISIBLE);
            holder.txtspinner.setText(model.getSubategory());
            holder.category.setText(model.getCategory());
            holder.simpleTimePicker.setText(model.getDuriation());

            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeid(pesrsonalList.get(position).getId());
                }
            });

        }

        void removeid(String id) {
            final Map<String, String> params = new HashMap<>();
            String baseUrl = ProductConfig.removepersonal + "?id=" + id;

            final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Response", response.toString());
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {

                            Toast.makeText(mContext, "Removed", Toast.LENGTH_SHORT).show();
                            PersonalInputFragment.getInstances().personal_user_input_fetch(selectedDateStr);
                            System.out.println("size==" + pesrsonalList.size());

                        } else {
                            //Toast.makeText(mContext, "Activity not found", Toast.LENGTH_SHORT).show();
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

            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            requestQueue.add(jsObjRequest);
        }


        @Override
        public int getItemCount() {
            return pesrsonalList.size();
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {
            TextView category;
            Spinner spinner;
            EditText simpleTimePicker;
            TableLayout tableLayout;
            TextView txtspinner;
            ImageView edit, remove;

            public ItemViewHolder(View itemView) {
                super(itemView);
                spinner = itemView.findViewById(R.id.spinner);
                simpleTimePicker = itemView.findViewById(R.id.edittext);
                category = itemView.findViewById(R.id.category);
                tableLayout = itemView.findViewById(R.id.tableLayout);
                txtspinner = itemView.findViewById(R.id.txtspinner);
                edit = itemView.findViewById(R.id.edit);
                remove = itemView.findViewById(R.id.remove);

            }
        }
    }

}