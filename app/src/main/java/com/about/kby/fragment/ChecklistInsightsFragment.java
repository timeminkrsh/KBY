package com.about.kby.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChecklistInsightsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChecklistInsightsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    PieChart pieChart;
    String taskId = "";
    ArrayList<ChecklistModel> namelist = new ArrayList<>();

    TextView shares;
    AutoCompleteTextView selecttask;
    Spinner selectmonth;
    String selectedMonth;
    TextView countone, countzero;
    ImageView imageview;
    RelativeLayout relativelaouyt;
    CardView cardViewGraph;

    public ChecklistInsightsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChecklistInsightsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChecklistInsightsFragment newInstance(String param1, String param2) {
        ChecklistInsightsFragment fragment = new ChecklistInsightsFragment();
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

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_checklist_insights, container, false);
        pieChart = view.findViewById(R.id.piechart);
        shares = view.findViewById(R.id.shares);
        imageview = view.findViewById(R.id.imageview);
        relativelaouyt = view.findViewById(R.id.relativelaouyt);
        cardViewGraph = view.findViewById(R.id.cardViewGraph);
        countzero = view.findViewById(R.id.countzero);
        countone = view.findViewById(R.id.countone);
        selecttask = view.findViewById(R.id.selecttask);
        selectmonth = view.findViewById(R.id.selectmonth);
        Bundle args = getArguments();
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);
        pieChart.setHoleRadius(40f); // default is 50f
        pieChart.setTransparentCircleRadius(50f);
        if (args != null) {
            taskId = args.getString("task_id");
            System.out.println("taskid=" + taskId);
        }
        name_list();

        shares.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getContext(), view);
                popupMenu.inflate(R.menu.share_popup);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.open) {

                            View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.custom_dialog_layout, null);
                            EditText editText = dialogView.findViewById(R.id.edit_text_id);
                            Log.d("description=", editText.getText().toString());
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setView(dialogView)
                                    .setTitle("OpenCircle")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Handle OK button click
                                            methods(editText.getText().toString());
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Handle Cancel button click
                                        }
                                    });

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                        if (menuItem.getItemId() == R.id.pers) {

                            final Map<String, String> params = new HashMap<>();
                            String para_str = "?user_id=" + Bsession.getInstance().getUser_id(getContext());
                            String para_str1 = "&user=" + Bsession.getInstance().getUser_mobile(getContext());
                            String baseUrl = ProductConfig.personal_exist + para_str + para_str1;
                            final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.e("Response", response.toString());
                                    try {
                                        JSONObject jsonResponse = new JSONObject(response);
                                        if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                                            JSONArray jsonlist = jsonResponse.getJSONArray("personal_grp_list");
                                            PopupMenu newPopupMenu = new PopupMenu(getContext(), view);

                                            // Iterate over personalGroupList to create menu items dynamically
                                            for (int i = 0; i < jsonlist.length(); i++) {
                                                JSONObject group = jsonlist.getJSONObject(i);
                                                String groupName = group.getString("grp_name");
                                                final String groupId = group.getString("grp_id");
                                                newPopupMenu.getMenu().add(Menu.NONE, i, Menu.NONE, groupName);

                                                newPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                    @Override
                                                    public boolean onMenuItemClick(MenuItem item) {
                                                        System.out.println("iddddd=" + groupId);
                                                        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.custom_dialog_layout, null);
                                                        EditText editText = dialogView.findViewById(R.id.edit_text_id);
                                                        String description = editText.getText().toString();
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                        builder.setView(dialogView)
                                                                .setTitle("Personal")
                                                                .setCancelable(false)
                                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                    }
                                                                })
                                                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        // Handle Cancel button click
                                                                    }
                                                                });

                                                        AlertDialog alertDialog = builder.create();
                                                        alertDialog.show();

                                                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                // Retrieve the EditText value when the OK button is clicked
                                                                String description = editText.getText().toString();
                                                                Log.d("description=", editText.getText().toString());
                                                                insertgroup(groupId, description);
                                                                alertDialog.dismiss(); // Dismiss the dialog after handling the click
                                                            }
                                                        });
                                                        return false;
                                                    }
                                                });
                                                newPopupMenu.setGravity(Gravity.BOTTOM);
                                                newPopupMenu.show();
                                            }
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // Handle error
                                }
                            });

                            // Add the request to the RequestQueue to execute it
                            Volley.newRequestQueue(getContext()).add(jsObjRequest);
                        }
                        return false;
                    }
                });
            }
        });
        selecttask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_list();

                selecttask.showDropDown();
            }
        });
        selecttask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                // Do something with the selected item
                selecttask.setText(selectedItem);
                counts(selecttask.getText().toString());
            }
        });

        final String[] months = new DateFormatSymbols().getMonths(); // Get array of all month names
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,months);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectmonth.setAdapter(adapter);

        currentMonthSetSelectionSpinner(months);

        selectmonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedMonth = months[position];
                System.out.println("Selected month: " + selectedMonth);
                // Check if adapter is empty
                if (adapter.getCount() == 0) {
                    Calendar calendar = Calendar.getInstance();
                    int currentMonthIndex = calendar.get(Calendar.MONTH);
                    String currentMonth = months[currentMonthIndex];
                    adapter.add(currentMonth);
                    System.out.println("selectmonth=="+months);
                    for (int i = 0; i < months.length; i++) {
                        if (i != currentMonthIndex) {
                            adapter.add(months[i]);
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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
            selectmonth.setSelection(position);
            System.out.println("selectmonth=="+stringToMatch);
            selectedMonth = stringToMatch;

        }

    }

    private void insertgroup(String groupId, String description) {
        final Map<String, String> params = new HashMap<>();
        String para_str2 = "&group_id=" + groupId;
        String para_str3 = "&active=" + countone.getText().toString();
        String para_str4 = "&task=" + selecttask.getText().toString();
        String para_str5 = "&inactive=" + countzero.getText().toString();
        String para_str6 = "&description=" + description;
        String baseUrl = ProductConfig.checklist_groups + "?user_id=" + Bsession.getInstance().getUser_id(getContext()) + para_str2 + para_str3 + para_str4 + para_str5 + para_str6;
        System.out.println("baseurl===" + baseUrl);
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                    } else {
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

    private void methods(String string) {
        final Map<String, String> params = new HashMap<>();

        String para_str1 = "?inactive=" + countzero.getText().toString();
        String para_str2 = "&name=" + Bsession.getInstance().getUser_name(getContext());
        String para_str3 = "&task=" + selecttask.getText().toString();
        String para_str4 = "&active=" + countone.getText().toString();
        String para_str5 = "&description=" + string;
        String baseUrl = ProductConfig.checklist_open + para_str1 + para_str2 + para_str3 + para_str4 + para_str5;
        System.out.println("baseurl===" + baseUrl);
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {

                        // Toast.makeText(getContext(), "Update On Open Circle", Toast.LENGTH_SHORT).show();

                    } else {
                        // Toast.makeText(getContext(), "Activity not found", Toast.LENGTH_SHORT).show();
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

    private void name_list() {
        final Map<String, String> params = new HashMap<>();
        String para_str = "?task_id=" + taskId;
        String baseUrl = ProductConfig.tasklist + para_str;
        namelist = new ArrayList<>();
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                        JSONArray jsonlist = jsonResponse.getJSONArray("task_list");
                        for (int j = 0; j < jsonlist.length(); j++) {
                            JSONObject jsonObject = jsonlist.getJSONObject(j);
                            ChecklistModel model = new ChecklistModel();

                            model.setId(jsonObject.getString("id"));
                            model.setName(jsonObject.getString("tack_name"));
                            namelist.add(model);

                            List<String> names = new ArrayList<>();
                            for (ChecklistModel models : namelist) {
                                names.add(models.getName());
                            }
                            String[] items = names.toArray(new String[0]);

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, items);

                            selecttask.setAdapter(adapter);

                        }


                    } else {
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

    private void counts(String s) {
        final Map<String, String> params = new HashMap<>();
        String para_str = "?checklist_id=" + s;
        String para_str1 = "&month=" + selectedMonth;
        String baseUrl = ProductConfig.countlist + para_str + para_str1;
        System.out.println("bbb=" + baseUrl);
        namelist = new ArrayList<>();
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {

                        String countZoro = jsonResponse.getString("countzoro");
                        String countOne = jsonResponse.getString("countone");
                        countzero.setText(countZoro);
                        countone.setText(countOne);
                        imageview.setVisibility(View.GONE);
                        cardViewGraph.setVisibility(View.VISIBLE);
                        relativelaouyt.setVisibility(View.VISIBLE);

                        // Use countZoro and countOne directly for pie entries
                        List<PieEntry> entries = new ArrayList<>();
                        entries.add(new PieEntry(Integer.parseInt(countZoro), "unachieved"));
                        entries.add(new PieEntry(Integer.parseInt(countOne), "achieved"));

                        ArrayList<Integer> colors = new ArrayList<>();
                        for (int color : ColorTemplate.MATERIAL_COLORS) {
                            colors.add(color);
                        }
                        for (int color : ColorTemplate.JOYFUL_COLORS) {
                            colors.add(color);
                        }

                        PieDataSet dataSet = new PieDataSet(entries, "Chart Label");
                        PieData data = new PieData(dataSet);
                        dataSet.setColors(colors);

                        ValueFormatter intFormatter = new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value) {
                                // Format the value as an integer
                                return String.valueOf((int) value);
                            }
                        };

                        data.setValueFormatter(intFormatter);
                        pieChart.setUsePercentValues(false);
                        dataSet.setValueTextSize(16f); // Set text size
                        dataSet.setValueTextColor(Color.BLACK);
                        pieChart.setData(data);
                        pieChart.invalidate();
                        pieChart.animateXY(1400, 1400);

                    } else {
                        // Toast.makeText(getContext(), " not found count", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsObjRequest);
    }

}