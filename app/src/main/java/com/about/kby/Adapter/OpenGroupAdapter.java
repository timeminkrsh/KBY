package com.about.kby.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.about.kby.R;
import com.about.kby.model.OpenModel;
import com.about.kby.model.SubCategoryModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class OpenGroupAdapter extends RecyclerView.Adapter<OpenGroupAdapter.Holder> {
    public Context context;
    public List<OpenModel> papularModelList;
    private List<PieEntry> entries = new ArrayList<>();
    final ArrayList<String> idList = new ArrayList<>();

    public OpenGroupAdapter(Context context, List<OpenModel> itemList) {
        this.context = context;
        this.papularModelList = itemList;
    }

    @NonNull
    @Override
    public OpenGroupAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_chart, parent, false);
        return new Holder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull OpenGroupAdapter.Holder holder, int position) {
        OpenModel model = papularModelList.get(position);
        holder.name.setText(model.getUser_name());
        List<SubCategoryModel> subCategories = model.getSubcategory(); // get the list of subcategories
        System.out.println("subcategory==" + model.getSubcategory());
        setupPieChart(holder.pieChart, model.getSubcategory());

        try {
            if(!model.getDescription().equals("null")) {
                holder.report_work.setText(model.getDescription());
            }else{
                holder.report_work.setVisibility(View.GONE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void setupPieChart(PieChart pieChart, List<SubCategoryModel> subcategories) {
        List<PieEntry> entries = new ArrayList<>();

        for (SubCategoryModel subcategory : subcategories) {
            float timeInDecimal = convertTimeStringToDecimal(subcategory.getTime());
            entries.add(new PieEntry(timeInDecimal, subcategory.getSub_category()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);

        pieChart.setData(data);
        pieChart.getDescription().setEnabled(false);
        dataSet.setValueTextSize(16f); // Set text size
        dataSet.setValueTextColor(Color.BLACK);
        pieChart.invalidate();
    }

    private float convertTimeStringToDecimal(String timeString) {
        String[] timeParts = timeString.split(":");
        if (timeParts.length == 2) {
            try {
                float hours = Float.parseFloat(timeParts[0]);
                float minutes = Float.parseFloat(timeParts[1]);
                return hours + minutes / 60;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return 0f;
    }
    @Override
    public int getItemCount() {
        List<OpenModel> list = this.papularModelList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView report_work, name;
        PieChart pieChart;

        public Holder(@NonNull View itemView) {
            super(itemView);
            report_work = itemView.findViewById(R.id.report_work);
            pieChart = itemView.findViewById(R.id.piechart);
            name = itemView.findViewById(R.id.name);
        }
    }

}
