/*
package com.about.kby.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.about.kby.Bsession;
import com.about.kby.ProductConfig;
import com.about.kby.R;
import com.about.kby.model.CheckModel;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

*/
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChecklistInputFragmentold#newInstance} factory method to
 * create an instance of this fragment.
 *//*

public class ChecklistInputFragmentold extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<CheckModel> itemList=new ArrayList<>();
    public ChecklistInputFragmentold() {
        // Required empty public constructor
    }

    */
/**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChecklistInputFragment.
     *//*

    // TODO: Rename and change types and number of parameters
    public static ChecklistInputFragmentold newInstance(String param1, String param2) {
        ChecklistInputFragmentold fragment = new ChecklistInputFragmentold();
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
            System.out.println("param1:"+mParam1);
        }
    }


    RecyclerView calendarView,date_based_inputrecyle;
    Spinner fromcalantor;
    int itemLists;
    private DateAdapter dateAdapter;
    List<String> datesList = new ArrayList<>();
    ProgressDialog progressDialog;
    LinearLayout addbutton;
    private List<Integer> imageList;
    String taskId,tack_name="";
    TextView txt;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checklist_input, container, false);
        fromcalantor=view.findViewById(R.id.fromcalantor);
        calendarView=view.findViewById(R.id.calendarView);
        txt=view.findViewById(R.id.txt);
        addbutton=view.findViewById(R.id.addbutton);
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
         progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading....");
        Bundle args = getArguments();
        if (args != null) {
             taskId = args.getString("task_id");
            tack_name = args.getString("tack_name");
            txt.setText(tack_name);
            // Now you have the task_id value here, you can use it as needed
        }
        final String[] months = new DateFormatSymbols().getMonths();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, months);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fromcalantor.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        calendarView.setLayoutManager(layoutManager);
        dateAdapter = new DateAdapter(new ArrayList<>(),imageList);
        calendarView.setAdapter(dateAdapter);

        fromcalantor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedMonth = months[position];
                int monthIndex = position; // Month index from 0 to 11
                fromcalantor.setTag(selectedMonth);
                generateDatesForMonth(monthIndex);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
        return view;
    }

*/
/*
    private void createdatebasedinput(int numberOfItems) {
        itemList = new ArrayList<>();
        CheckModel best = new CheckModel();
        System.out.println("datesList"+numberOfItems);
        for (int i = 0; i<numberOfItems; i++) {
            CheckModel checkModel = new CheckModel(); // Assuming CheckModel is your item model class
            itemList.add(checkModel);
        }


        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        date_based_inputrecyle.setLayoutManager(linearLayoutManager2);
        ChecklistAdapter bestSaleAdapter = new ChecklistAdapter(getContext(), itemList);
        date_based_inputrecyle.setAdapter(bestSaleAdapter);


    }
*//*


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
            String date = monthIndex + 1 + "/" + i ; // Format: MM/DD/YYYY
            datesList.add(date);
            System.out.println("iii"+i);
            itemLists=i;

        }

        // Update the RecyclerView with the list of dates for the selected month
        dateAdapter.updateDatesList(datesList);
        //createdatebasedinput(daysInMonth);
    }
    public class DateAdapter extends RecyclerView.Adapter<DateAdapter.DateViewHolder> {

        private List<String> datesList;
        int flags[] = {R.drawable.empty_box,R.drawable.check, R.drawable.close};
        private List<Integer> imageList;
        public DateAdapter(List<String> datesList, List<Integer> imageList) {
            this.datesList = datesList;
            this.imageList = imageList;

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

            DateViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.textdate);
                spinner_image = itemView.findViewById(R.id.spinner_image);
            }

            void bind(int position) {
                String date = datesList.get(position);
                textView.setText(date);
                spinner_image.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        Object selectedItem = parent.getItemAtPosition(pos);
                        System.out.println("idd="+pos);
                        System.out.println("idd="+date);
                        if (pos != 0) {
                            inputs(pos,date);
                            int selectedImage = flags[pos]; // Assuming flags contain the image IDs
                            spinner_image.setTag(selectedImage);
                            Toast.makeText(getContext(), "Selected yes: "+selectedImage, Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(getContext(), "Selected  no: ", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Do something when nothing is selected

                    }
                });

                CustomAdapter customAdapter = new CustomAdapter(getContext(), flags,position);
                spinner_image.setAdapter(customAdapter);

            }
        }
    }

    private void inputs(int status ,String date) {
        String userid = Bsession.getInstance().getUser_id(getContext());
        final Map<String, String> params = new HashMap<>();

        String para_str = "?user_id=" + userid;
        String para_str1 = "&checklist_id=" + taskId;
        String para_str2 = "&date=" +date;
        String para_str3 = "&status=" + status ;
        String baseUrl = ProductConfig.check_list_input + para_str + para_str1+para_str2+para_str3;
        System.out.println("para1"+para_str);
        System.out.println("para1"+para_str1);
        System.out.println("para1"+para_str2);
        System.out.println("para1"+para_str3);
        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);

                    if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                        Toast.makeText(getContext(), "  Successfully", Toast.LENGTH_SHORT).show();
                        jsonResponse.getString("id");
                        jsonResponse.getString("status");
                        //personal_user_input_fetch();
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

    */
/*
        public class ChecklistAdapter extends RecyclerView.Adapter<ChecklistInputFragment.ChecklistAdapter.ItemViewHolder>  {
            private List<CheckModel> itemList;
            private Context mContext;

            public ChecklistAdapter(Context mContext, List<CheckModel> itemList) {
                this.mContext = mContext;
                this.itemList = itemList;

            }


            @NonNull
            @Override
            public ChecklistInputFragment.ChecklistAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.enable_disable, parent, false);
                return new ChecklistInputFragment.ChecklistAdapter.ItemViewHolder(itemView);
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onBindViewHolder(@NonNull ChecklistInputFragment.ChecklistAdapter.ItemViewHolder holder, @SuppressLint("RecyclerView") int position) {

                CheckModel model = itemList.get(position);

               *//*

*/
/* holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    // Handle item selection here
                    String selectedItem = parent.getItemAtPosition(pos).toString();
                    // Do something with the selected item
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Do something when nothing is selected
                }
            });*//*
*/
/*



        }



        @Override
        public int getItemCount() {
            return itemList.size();
        }


        public class ItemViewHolder extends RecyclerView.ViewHolder {
            Spinner spinner;


            public ItemViewHolder(View itemView) {
                super(itemView);
                spinner=itemView.findViewById(R.id.spinner_image);


            }
        }
    }
*//*

    public static class CustomAdapter extends BaseAdapter {
        Context context;
        int flags[];
        LayoutInflater inflter;

        public CustomAdapter(Context applicationContext, int[] flags, int position) {
            this.context = applicationContext;
            this.flags = flags;
            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return flags.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflter.inflate(R.layout.spinnerimage, null);
            ImageView icon = (ImageView) view.findViewById(R.id.image);
            icon.setImageResource(flags[i]);
            return view;
        }
    }
}*/
