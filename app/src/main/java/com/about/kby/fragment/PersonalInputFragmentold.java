package com.about.kby.fragment;

import static java.util.Calendar.getInstance;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.about.kby.Bsession;
import com.about.kby.ProductConfig;
import com.about.kby.R;
import com.about.kby.model.PersonalModel;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.model.CalendarEvent;
import devs.mulham.horizontalcalendar.utils.CalendarEventsPredicate;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonalInputFragmentold#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalInputFragmentold extends Fragment implements AdapterView.OnItemSelectedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<PersonalModel> itemList = new ArrayList<>();
    private ArrayList<PersonalModel> countries_list = new ArrayList<>();
    ArrayList<String> pincodelist = new ArrayList<String>();
    ArrayList<String> pincodelists = new ArrayList<String>();
    RecyclerView recycler_table;
    List<String> teamList = new ArrayList<>();
    TableLayout tableLayouts;
    LinearLayout addbutton;
    Button today;
    ProgressDialog progressDialog;
    String selectedValue="";
    TableLayout tableLayout;
    String subcat_id;
    ArrayList<String> itemLists = new ArrayList();
    List<View> tableRows = new ArrayList<>();

    public PersonalInputFragmentold() {
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
    public static PersonalInputFragmentold newInstance(String param1, String param2) {
        PersonalInputFragmentold fragment = new PersonalInputFragmentold();
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
    TextView category;
    Spinner spinners;
    EditText simpleTimePicker;
    int tableViewCount = 0;
    String duriation="";

    List<Spinner> spinnersList = new ArrayList<>();
    List<TextView> categoryList = new ArrayList<>();
    List<EditText> timePickerList = new ArrayList<>();
    String selectedDateStr;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_input, container, false);
        recycler_table = view.findViewById(R.id.recycler_table);
        today = view.findViewById(R.id.today);
        addbutton = view.findViewById(R.id.addbutton);
        tableLayout = view.findViewById(R.id.tableLayout);
        Calendar startDate = getInstance();
        startDate.add(Calendar.MONTH, -2);

        ProgressDialog progressDialog2 = new ProgressDialog(getContext());
        this.progressDialog = progressDialog2;
        progressDialog2.setMessage("Loading.....");

        /* end after 2 months from now */
        Calendar endDate = getInstance();
        endDate.add(Calendar.MONTH, 2);

        // Default Date set to Today.
        final Calendar defaultSelectedDate = getInstance();

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(view, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .configure()
                .formatTopText("MMM")
                .formatMiddleText("dd")
                .formatBottomText("EEE")
                .showTopText(true)
                .showBottomText(true)
                .textColor(Color.MAGENTA, Color.BLACK)
                .colorTextMiddle(Color.BLACK, Color.parseColor("#ffd54f"))
                .end()
                .defaultSelectedDate(defaultSelectedDate)
               .addEvents(new CalendarEventsPredicate() {

                    Random rnd = new Random();
                    @Override
                    public List<CalendarEvent> events(Calendar date) {
                        List<CalendarEvent> events = new ArrayList<>();
                        int count = rnd.nextInt(1);

                        for (int i = 0; i <= count; i++){
                            events.add(new CalendarEvent(Color.rgb(rnd.nextInt(200), rnd.nextInt(200), rnd.nextInt(200)), "event"));
                        }

                        return events;
                    }
                })
                .build();


        Log.i("Default Date", DateFormat.format("EEE, MMM d, yyyy", defaultSelectedDate).toString());

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                 selectedDateStr = DateFormat.format("EEE, MMM d, yyyy", date).toString();
                //Toast.makeText(getContext(), selectedDateStr + " selected!", Toast.LENGTH_SHORT).show();
                Log.i("onDateSelected", selectedDateStr + " - Position = " + position);
            }

        });


        LinearLayout addrow=view.findViewById(R.id.addbutton);

        addrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View tableview = getLayoutInflater().inflate(R.layout.tablepersonal, null, false);
                spinners = tableview.findViewById(R.id.spinner);
                category = tableview.findViewById(R.id.category);
                simpleTimePicker = tableview.findViewById(R.id.edittext);

                for (int i=0;i<=timePickerList.size();i++){

                }
               // timePickerList.get(i);
/*
                simpleTimePicker.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void afterTextChanged(Editable s) {}

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        if(s.length() == 4||s.length() == 5)
                            duriation  =simpleTimePicker.getText().toString().trim();
                        System.out.println("Number of table views open: " + simpleTimePicker.getText().toString().trim());

                    }
                });
*/          simpleTimePicker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showTimePickerDialog();
                    }
                });

                if (tableViewCount==0){

                    if (spinners != null && spinners.getAdapter() == null) {
                        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, teamList);
                        spinners.setAdapter(arrayAdapter);
                        subcategory(spinners);
                        tableLayout.addView(tableview);
                        tableViewCount++;
                        tableRows.add(tableview);
                        spinnersList.add(spinners);
                        categoryList.add(category);
                        timePickerList.add(simpleTimePicker);
                       // System.out.println("simpleTimePicker=" + simpleTimePicker.getText().toString().trim());
                        System.out.println("Number of table views open: " + tableViewCount);
                            //input();
                    }


                }else if (tableViewCount>=0) {

                    //  System.out.println("simpleTimePicker=" + simpleTimePicker.getText().toString().trim());
                    if (duriation.isEmpty()){
                        Toast.makeText(getContext(), "Duriation is empty...!!!", Toast.LENGTH_SHORT).show();
                        showTimePickerDialog();
                     return;

                    } else {
                        if (spinners != null && spinners.getAdapter() == null) {
                            ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, teamList);
                            spinners.setAdapter(arrayAdapter);
                            subcategory(spinners);
                            tableLayout.addView(tableview);
                            tableViewCount++;
                            tableRows.add(tableview);
                            spinnersList.add(spinners);
                            categoryList.add(category);
                            timePickerList.add(simpleTimePicker);
                            //System.out.println("simpleTimePicker=" + simpleTimePicker.getText().toString().trim());
                            System.out.println("Number of table views open: " + tableViewCount);
                            input();
                        }
                    }
                }


                spinners.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedValue = parent.getItemAtPosition(position).toString();

                        // Do something with the selected value
                        System.out.println("selectedValue=="+selectedValue);
                        System.out.println("selectvv=="+id);
                        categorymethod(selectedValue);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Do nothing
                    }
                });

            }
        });
        return view;
    }

    private void input() {
       String userid = Bsession.getInstance().getUser_id(getContext());
        System.out.println("phone;;" + userid);
        final Map<String, String> params = new HashMap<>();
        progressDialog.show();

        String para_str = "?user_id=" + userid;
        String para_str1 = "&cat_id=" + category;
        String para_str2 = "&sub_cat_id=" +spinners;
        String para_str3 = "&date=" + selectedDateStr;
        String para_str4 = "&time=" + duriation;
        String baseUrl = ProductConfig.personal_user_input_insert + para_str + para_str1+para_str2+para_str3+para_str4;
            System.out.println("para1"+para_str);
            System.out.println("para1"+para_str1);
            System.out.println("para1"+para_str2);
            System.out.println("para1"+para_str3);
            System.out.println("para1"+para_str4);
        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);

                    if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                        Toast.makeText(getContext(), "  Successfully", Toast.LENGTH_SHORT).show();

                    } else if (jsonResponse.has("success") && jsonResponse.getString("success").equals("0")){
                        Toast.makeText(getContext(), "Incorrect ", Toast.LENGTH_LONG).show();

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

    private void showTimePickerDialog() {
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
                        Toast.makeText(getContext(), "Selected time: " + formattedTime, Toast.LENGTH_SHORT).show();
                        simpleTimePicker.setText(formattedTime);
                        duriation=simpleTimePicker.getText().toString();
                        System.out.println("Selected tim="+duriation);
                    }
                },
                hour, minute, true);

        timePickerDialog.show();
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
                            createDropdownList(pincodelist,spinners);
                        }

                    } else {
                        Toast.makeText(getContext(), "Subcategory not found", Toast.LENGTH_SHORT).show();
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

    private void categorymethod(String selectedItemID) {
        final Map<String, String> params = new HashMap<>();
        String para2 = "?name="+ selectedItemID;
        System.out.println("para2=="+para2);
        progressDialog.show();
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

                            countries_list.add(model1);
                            System.out.println("texxtt=="+category);
                        }
                    } else {
                        Toast.makeText(getContext(), "Category not found", Toast.LENGTH_SHORT).show();
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

    private void createDropdownList(ArrayList<String> pincodelist, Spinner spinners) {

        ArrayAdapter adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item, pincodelist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinners.setAdapter(adapter);
        System.out.println("itemlistid=="+pincodelist);


        System.out.println("itemlistid=="+selectedValue);


    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
      pincodelist.clear();
    }




}