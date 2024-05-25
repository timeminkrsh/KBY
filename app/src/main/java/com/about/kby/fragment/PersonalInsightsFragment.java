package com.about.kby.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.about.kby.Bsession;
import com.about.kby.ProductConfig;
import com.about.kby.R;
import com.about.kby.model.InputInsightModel;
import com.about.kby.model.PersonalModel;
import com.about.kby.model.PersonalPojo;
import com.about.kby.model.PieEntryModel;
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
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonalInsightsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalInsightsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String id;
    PieChart pieChart;
    EditText fromcalator;
    EditText tocalentor;
    EditText today;
    ImageView out_share;
    String selectedDateStr;
    ArrayList<String> pincodelist = new ArrayList<String>();

    private List<InputInsightModel> PojoList = new ArrayList<>();
    private List<PersonalPojo> PojoLists = new ArrayList<>();

    public PersonalInsightsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonalInsightsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonalInsightsFragment newInstance(String param1, String param2) {
        PersonalInsightsFragment fragment = new PersonalInsightsFragment();
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

    final ArrayList<String> idList = new ArrayList<>();
    final ArrayList<String> idListc = new ArrayList<>();
    final ArrayList<String> idListsub = new ArrayList<>();
    final ArrayList<String> idListdu = new ArrayList<>();
    private List<PieEntry> entries = new ArrayList<>();
    private ArrayList<PersonalModel> countries_list = new ArrayList<>();
    EditText editText;
    String idString;
    RelativeLayout layutss;
    RecyclerView recycle;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_insights, container, false);
        pieChart = view.findViewById(R.id.piechart);
        fromcalator = view.findViewById(R.id.fromcalantor);
        tocalentor = view.findViewById(R.id.tocalentor);
        TextView shares = view.findViewById(R.id.shares);
        today = view.findViewById(R.id.today);
        recycle = view.findViewById(R.id.recycle);
        out_share = view.findViewById(R.id.out_share);
        editText = view.findViewById(R.id.editText);
        layutss = view.findViewById(R.id.layutss);
        editText.setText("  Select Type");
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -2);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);

        pieChart.setHoleRadius(40f); // default is 50f
        pieChart.setTransparentCircleRadius(50f); // default is 55f
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 2);
        pieChart.clear();
        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);
        final Calendar defaultSelectedDate = Calendar.getInstance();
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.buttonadds);
        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(view, R.id.calendarView)
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
                Log.i("onDateSelected", selectedDateStr + " - Position = " + position);
                personal_user_input_fetch(selectedDateStr);

            }

        });

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getContext(), v);
                popupMenu.inflate(R.menu.type);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.categorys) {
                            personal_user_inut_fetch(true);
                        }
                        if (menuItem.getItemId() == R.id.subcategorys) {
                            personal_user_inut_fetch(false);
                        }
                        return false;

                    }
                });
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

                fromcalator.setText("From : ");
                tocalentor.setText("To : ");
                editText.setText("  Select Type");
            }
        });
        fromcalator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("  Select Type");
                openDatePicker(); // Open date picker dialog

            }
        });
        tocalentor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("  Select Type");
                openDatePickers(); // Open date picker dialog
                PojoLists.clear();
                PojoList.clear();
                pieChart.clear();
                entries.clear();
                today.setText("Today");
            }
        });
        shares.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("  Select Type");
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
                                            methods(idString, editText.getText().toString());
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
                            System.out.println("baseurl===" + baseUrl);
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
                                                                        // Handle OK button click

                                                                        insertgroup(description,groupId,idListc,idListsub,idListdu);
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

                                                                String description = editText.getText().toString();
                                                                Log.d("description=", editText.getText().toString());
                                                                insertgroup(description, groupId, idListc, idListsub, idListdu);
                                                                alertDialog.dismiss();
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


        out_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "APP LINK : " + "https://play.google.com/store/apps/details?id=com.about.kby";
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        return view;
    }

    private void insertgroup(String description, String groupId, ArrayList<String> idListc, ArrayList<String> idListsub, ArrayList<String> idListdu) {
        final Map<String, String> params = new HashMap<>();
        System.out.println("description===" + description);
        String idListcs = TextUtils.join(",", idListc);
        String idListsubs = TextUtils.join(",", idListsub);
        String idListdus = TextUtils.join(",", idListdu);
        String para_str2 = "&group_id=" + groupId;
        String para_str3 = "&category=" + idListcs;
        String para_str4 = "&subcategory=" + idListsubs;
        String para_str5 = "&duriation=" + idListdus;
        String para_str6 = "&description=" + description;
        String baseUrl = ProductConfig.kby_groupinsert + "?user_id=" + Bsession.getInstance().getUser_id(getContext()) + para_str2 + para_str3 + para_str4 + para_str5 + para_str6;
        System.out.println("baseurl===" + baseUrl);

        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Response", response.toString());
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                        Toast.makeText(getContext(), " Shared Successfully ", Toast.LENGTH_SHORT).show();
                    } else {
                         Toast.makeText(getContext(), "Activity not found", Toast.LENGTH_SHORT).show();
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

    private void showPopupMenu(View anchor) {
        PopupMenu popupMenu = new PopupMenu(getContext(), anchor);
        for (String name : pincodelist) { // Assuming pincodelist is the list of names
            popupMenu.getMenu().add(name);
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                editText.setText(item.getTitle());
                return true;
            }
        });

        popupMenu.show();
    }

    private void subcategory() {
        final Map<String, String> params = new HashMap<>();
        String baseUrl = ProductConfig.subcategory;
        System.out.println("baseurl===" + baseUrl);

        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                        countries_list = new ArrayList<>();
                        JSONArray jsonlist = jsonResponse.getJSONArray("subcategory_list");
                        for (int j = 0; j < jsonlist.length(); j++) {
                            JSONObject jsonObject1 = jsonlist.getJSONObject(j);
                            PersonalModel model1 = new PersonalModel();
                            model1.setId(jsonObject1.getString("Subcategory_id"));
                            model1.setSubcategory(jsonObject1.getString("name"));
                            pincodelist.add(jsonObject1.getString("name"));
                            countries_list.add(model1);

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


    }

    private void methods(String idList, String description) {
        final Map<String, String> params = new HashMap<>();
        String para_str2 = "&type=" + "opencircle";
        String para_str3 = "&idss=" + idList;
        System.out.println("ID==: " + idList);
        String para_str4 = "&descriptions=" + description;

        String baseUrl = ProductConfig.opencircle_update + "?user_id=" + Bsession.getInstance().getUser_id(getContext()) + para_str2 + para_str3 + para_str4;
        System.out.println("baseurl===" + baseUrl);
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                        Toast.makeText(getContext(), " Shared Successfully ", Toast.LENGTH_SHORT).show();
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

    public void personal_user_input_fetch(String selectedDateStr) {
        final Map<String, String> params = new HashMap<>();
        recycle.setVisibility(View.VISIBLE);
        String baseUrl = ProductConfig.personal_user_input_fetch + "?user_id=" + Bsession.getInstance().getUser_id(getContext()) +
                "&date=" + selectedDateStr;
        System.out.println("baseurl===" + baseUrl);
        pieChart.clear();
        PojoLists.clear();
        PojoList.clear();
        pieChart.clear();
        entries.clear();
        idList.clear();

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
                            String[] timeParts;
                                personalPojo.setId(jsonObject1.getString("id"));
                                personalPojo.setCategory(jsonObject1.getString("category"));
                                personalPojo.setSubategory(jsonObject1.getString("sub_category"));
                                personalPojo.setDuriation(jsonObject1.getString("time"));
                                String timeString = personalPojo.getDuriation(); // Assuming timeString is in the format HH:mm
                                timeParts = timeString.split(":"); // Splitting hours and minutes
                                PojoLists.add(personalPojo);

                            idList.add(personalPojo.getId());
                            idListc.add(personalPojo.getCategory());
                            idListsub.add(personalPojo.getSubategory());
                            idListdu.add(personalPojo.getDuriation());
                            entries.add(new PieEntry(Float.parseFloat(timeParts[0] + "." + timeParts[1])));

                        }
                        String[] idArray = idList.toArray(new String[0]);
                        for (String id : idArray) {
                            System.out.println("ID: " + id);
                            idString = TextUtils.join(",", idArray);
                            System.out.println("ID==: " + idString);
                           // return;
                        }
                        ArrayList<Integer> colors = new ArrayList<>();

                        for (int color : ColorTemplate.MATERIAL_COLORS) {
                            colors.add(color);
                        }

                        for (int color : ColorTemplate.JOYFUL_COLORS) {
                            colors.add(color);
                        }

                        PieDataSet dataSet = new PieDataSet(entries, "");
                        dataSet.setColors(colors);
                        dataSet.setValueFormatter(new PercentFormatter(pieChart));
                        dataSet.setValueTextSize(16f); // Set text size
                        dataSet.setValueTextColor(Color.BLACK);
                        PieData data = new PieData(dataSet);
                        pieChart.setData(data);

                        pieChart.isDrawEntryLabelsEnabled();
                        pieChart.invalidate();
                        pieChart.animateXY(1000, 1000);

                        List<PieEntryModel> entryModels = new ArrayList<>();
                        for (int i = 0; i < PojoLists.size(); i++) {
                            PersonalPojo model = PojoLists.get(i);
                            Log.i("pojoLists--", PojoLists.toString());

                            int color = colors.get(i % colors.size()); // Assuming colors.size() > 0
                            PieEntryModel entryModel = new PieEntryModel(model.getSubategory(), model.getDuriation(), color);
                            entryModels.add(entryModel);
                        }
                        recycle.setLayoutManager(new LinearLayoutManager(getContext()));
                        PieEntryAdapter adapter = new PieEntryAdapter(entryModels);
                        recycle.setAdapter(adapter);
                    } else {
                        recycle.setVisibility(View.GONE);
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
        getContext();
    }


    public void personal_user_inut_fetch(final boolean isCategory) {
        final Map<String, String> params = new HashMap<>();
        recycle.setVisibility(View.VISIBLE);
        String baseUrl = ProductConfig.personal_user_input_fetch + "?user_id=" + Bsession.getInstance().getUser_id(getContext()) +
                "&date=" + selectedDateStr;
        System.out.println("baseurl===" + baseUrl);
        pieChart.clear();
        PojoLists.clear();
        PojoList.clear();
        pieChart.clear();
        entries.clear();
        idList.clear();
        idListc.clear();
        idListsub.clear();
        idListdu.clear();

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
                            /*String checkAlreayAdded = isTimeAlreadyAdded(PojoLists, jsonObject1.getString("sub_category"));
                            int position = alreadyAddedPositionOfValue(PojoLists, jsonObject1.getString("sub_category"));
                            String[] timeParts;

                            if (checkAlreayAdded.isEmpty()) {*/
                            String[] timeParts;
                                personalPojo.setId(jsonObject1.getString("id"));
                                personalPojo.setCategory(jsonObject1.getString("category"));
                                personalPojo.setSubategory(jsonObject1.getString("sub_category"));
                                personalPojo.setDuriation(jsonObject1.getString("time"));
                                String timeString = personalPojo.getDuriation(); // Assuming timeString is in the format HH:mm
                                timeParts = timeString.split(":"); // Splitting hours and minutes
                                PojoLists.add(personalPojo);
                           /* } else {
                                LocalTime time1 = null;
                                LocalTime time2 = null;
                                LocalTime sum = null;
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    time1 = LocalTime.parse(jsonObject1.getString("time"));
                                    time2 = LocalTime.parse(checkAlreayAdded);
                                    // Calculate the difference in minutes
                                    sum = time1.plusHours(time2.getHour()).plusMinutes(time2.getMinute());
                                }
                                personalPojo.setId(jsonObject1.getString("id"));
                                personalPojo.setCategory(jsonObject1.getString("category"));
                                personalPojo.setSubategory(jsonObject1.getString("sub_category"));
                                personalPojo.setDuriation(String.valueOf(sum));
/// overall time change
                                String timeString = (jsonObject1.getString("time"));// Assuming timeString is in the format HH:mm
                                timeParts = timeString.split(":"); // Splitting hours and minutes
                                PojoLists.remove(position);
                                PojoLists.add(position, personalPojo);
                            }*/
                            idList.add(personalPojo.getId());
                            idListc.add(personalPojo.getCategory());
                            idListsub.add(personalPojo.getSubategory());
                            idListdu.add(personalPojo.getDuriation());

                            entries.add(new PieEntry(Float.parseFloat(timeParts[0] + "." + timeParts[1])));

                        }
                        String[] idArray = idList.toArray(new String[0]);

                        for (String id : idArray) {
                            System.out.println("ID: " + id);

                        }
                        ArrayList<Integer> colors = new ArrayList<>();

                        for (int color : ColorTemplate.MATERIAL_COLORS) {
                            colors.add(color);
                        }

                        for (int color : ColorTemplate.JOYFUL_COLORS) {
                            colors.add(color);
                        }

                        PieDataSet dataSet = new PieDataSet(entries, "");
                        dataSet.setColors(colors);
                        dataSet.setValueFormatter(new PercentFormatter(pieChart));
                        dataSet.setValueTextSize(16f); // Set text size
                        dataSet.setValueTextColor(Color.BLACK);
                        PieData data = new PieData(dataSet);
                        pieChart.setData(data);

                        pieChart.isDrawEntryLabelsEnabled();
                        pieChart.invalidate();
                        pieChart.animateXY(1000, 1000);

                        List<PieEntryModel> entryModels = new ArrayList<>();
                        for (int i = 0; i < PojoLists.size(); i++) {
                            PersonalPojo model = PojoLists.get(i);
                            int color = colors.get(i % colors.size()); // Assuming colors.size() > 0
                            PieEntryModel entryModel;

                            if (isCategory) {
                                // Load category data into recycle
                                entryModel = new PieEntryModel(model.getCategory(), model.getDuriation(), color);
                            } else {
                                // Load subcategory data into recycle
                                entryModel = new PieEntryModel(model.getSubategory(), model.getDuriation(), color);
                            }

                            entryModels.add(entryModel);
                        }

                        recycle.setLayoutManager(new LinearLayoutManager(getContext()));
                        PieEntryAdapter adapter = new PieEntryAdapter(entryModels);
                        recycle.setAdapter(adapter);
                    } else {
                        recycle.setVisibility(View.GONE);
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
        getContext();
    }

    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // Set the picked date to your TextView or any other view
                //fromcalator.setText(year + "-" + (month + 1) + "-" + day); // Month starts from 0, so adding 1
                fromcalator.setText(String.format("%d-%02d-%02d", year, month + 1, day));

                //Showing the picked value in the textView
                // e.setText(String.valueOf(year)+ "."+String.valueOf(month)+ "."+String.valueOf(day));
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void personal_user_input_fetch(EditText fromcalator, EditText tocalentor, String date) {
        final Map<String, String> params = new HashMap<>();
        recycle.setVisibility(View.VISIBLE);
        PojoLists.clear();
        PojoList.clear();
        pieChart.clear();
        idList.clear();
        entries.clear();
        idListsub.clear();
        idListdu.clear();
        String para_str1 = "&from_date=" + fromcalator.getText().toString();
        String para_str2 = "&to_date=" + tocalentor.getText().toString();
        String para_str3 = "&date=" + date;
        String baseUrl = ProductConfig.personal_user_Insights_fetch + "?user_id=" + Bsession.getInstance().getUser_id(getContext()) + para_str1 + para_str2 + para_str3;
        System.out.println("baseurl" + baseUrl);
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                        PojoList = new ArrayList<>();
                        JSONArray jsonlist = jsonResponse.getJSONArray("store_list");
                        for (int j = 0; j < jsonlist.length(); j++) {
                            JSONObject jsonObject1 = jsonlist.getJSONObject(j);
                            InputInsightModel inputInsightModel = new InputInsightModel();

                          //  String checkAlreayAdded = isTimeAlreadyAddedToDate(PojoList, jsonObject1.getString("sub_category"));
                          //  int position = alreadyAddedPositionOfValueToDate(PojoList, jsonObject1.getString("sub_category"));
                            String[] timeParts;

                         //   if (checkAlreayAdded.isEmpty()) {
                                inputInsightModel.setId(jsonObject1.getString("id"));
                                inputInsightModel.setCategory(jsonObject1.getString("category"));
                                inputInsightModel.setSubategory(jsonObject1.getString("sub_category"));
                                inputInsightModel.setDuriation(jsonObject1.getString("time"));
                                PojoList.add(inputInsightModel);
                          //  } else {
                          //      LocalTime time1 = null;
                           //     LocalTime time2 = null;
                           //     LocalTime sum = null;
                            //    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                             /*       time1 = LocalTime.parse(jsonObject1.getString("time"));
                                    time2 = LocalTime.parse(checkAlreayAdded);
                                    // Calculate the plus in minutes
                                    sum = time1.plusHours(time2.getHour()).plusMinutes(time2.getMinute());
                                }*/

/// overall time change
                                String timeString = (jsonObject1.getString("time"));// Assuming timeString is in the format HH:mm
                                timeParts = timeString.split(":"); // Splitting hours and minutes
                            idList.add(inputInsightModel.getId());
                            idListc.add(inputInsightModel.getCategory());
                            idListsub.add(inputInsightModel.getSubategory());
                            idListdu.add(inputInsightModel.getDuriation());

                            entries.add(new PieEntry(Float.parseFloat(timeParts[0] + "." + timeParts[1])));

                        }
                        String[] idArray = idList.toArray(new String[0]);
                        for (String id : idArray) {
                            System.out.println("ID: " + id);
                        }
                        ArrayList<Integer> colors = new ArrayList<>();

                        for (int color : ColorTemplate.MATERIAL_COLORS) {
                            colors.add(color);
                        }

                        for (int color : ColorTemplate.JOYFUL_COLORS) {
                            colors.add(color);
                        }
                        PieDataSet dataSet = new PieDataSet(entries, "");
                        dataSet.setColors(colors);
                        dataSet.setValueFormatter(new PercentFormatter(pieChart));
                        dataSet.setValueTextSize(16f); // Set text size
                        dataSet.setValueTextColor(Color.BLACK);
                        PieData data = new PieData(dataSet);
                        pieChart.setData(data);
                        pieChart.isDrawEntryLabelsEnabled();
                        pieChart.invalidate();
                        pieChart.animateXY(1000, 1000);

                        List<PieEntryModel> entryModels = new ArrayList<>();
                        for (int i = 0; i < PojoList.size(); i++) {
                            InputInsightModel model = PojoList.get(i);
                            int color = colors.get(i % colors.size()); // Assuming colors.size() > 0
                            PieEntryModel entryModel = new PieEntryModel(model.getSubategory(), model.getDuriation(), color);
                            entryModels.add(entryModel);
                        }

                        recycle.setLayoutManager(new LinearLayoutManager(getContext()));
                        PieEntryAdapter adapter = new PieEntryAdapter(entryModels);
                        recycle.setAdapter(adapter);
                    } else {
                        recycle.setVisibility(View.GONE);
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

    private void openDatePickers() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                tocalentor.setText(String.format("%d-%02d-%02d", year, month + 1, day));
                personal_user_input_fetch(fromcalator, tocalentor, "");
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void onResume() {
        pieChart.clear();
        PojoList.clear();
        PojoLists.clear();
        idList.clear();
        entries.clear();
        idListsub.clear();
        idListdu.clear();
        personal_user_input_fetch(selectedDateStr);
        super.onResume();
    }

    public class PieEntryAdapter extends RecyclerView.Adapter<PieEntryAdapter.ViewHolder> {

        private List<PieEntryModel> entryList;

        public PieEntryAdapter(List<PieEntryModel> entryList) {
            this.entryList = entryList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pie_entry, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            PieEntryModel entry = entryList.get(position);
            holder.colorBlock.setBackgroundColor(entry.getColor());
            String textPiechart = entry.getSubcategory() + " - " + entry.getDuriation();
            holder.tvEntryText.setText(textPiechart);
        }

        @Override
        public int getItemCount() {
            return entryList.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            View colorBlock;
            TextView tvEntryText;

            ViewHolder(View view) {
                super(view);
                colorBlock = view.findViewById(R.id.colorBlock);
                tvEntryText = view.findViewById(R.id.tvEntryText);
            }
        }
    }
}