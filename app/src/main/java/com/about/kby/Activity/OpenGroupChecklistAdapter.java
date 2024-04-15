package com.about.kby.Activity;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.about.kby.R;
import com.about.kby.model.OpenModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class OpenGroupChecklistAdapter extends RecyclerView.Adapter<OpenGroupChecklistAdapter.Holder> {
    public Context context;
    public List<OpenModel> papularModelList;
    private List<PieEntry> entries = new ArrayList<>();
    final ArrayList<String> idList = new ArrayList<>();

    public OpenGroupChecklistAdapter(OpenGroupActivity openGroupActivity, List<OpenModel> itemList) {
        this.context = context;
        this.papularModelList = itemList;
    }

    @NonNull
    @Override
    public OpenGroupChecklistAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_chart, parent, false);
        return new OpenGroupChecklistAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OpenGroupChecklistAdapter.Holder holder, int position) {
        OpenModel model = papularModelList.get(position);
        holder.name.setText(model.getUser_name());
        System.out.println("subcategoryy==" + model.getTask());
        int active = Integer.parseInt(model.getActive());
        int inactive = Integer.parseInt(model.getInactive());
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(active, "Active"));
        entries.add(new PieEntry(inactive, "Inactive"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        PieData data = new PieData(dataSet);
        ArrayList<Integer> colors = new ArrayList<>();
        try {
            holder.report_work.setText(model.getDescription());
        }catch (Exception e){
            e.printStackTrace();
        }
        for (int color : ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        for (int color : ColorTemplate.JOYFUL_COLORS) {
            colors.add(color);
        }

        dataSet.setColors(colors);
        holder.pieChart.setData(data);
        holder.pieChart.invalidate(); // refresh chart
        dataSet.setValueTextSize(16f); // Set text size
        dataSet.setValueTextColor(Color.BLACK);

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
