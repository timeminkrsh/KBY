package com.about.kby.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.about.kby.Activity.FamilyActivity;
import com.about.kby.Activity.OpenGroupChecklistAdapter;
import com.about.kby.R;
import com.about.kby.model.OpenModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class PersonalCircleAdapter extends RecyclerView.Adapter<PersonalCircleAdapter.ViewHolder> {
    public Context context;
    public List<OpenModel> papularModelList;
    private List<PieEntry> entries = new ArrayList<>();
    public PersonalCircleAdapter(FamilyActivity familyActivity, List<OpenModel> itemList) {
        this.context = context;
        this.papularModelList = itemList;
    }

    @NonNull
    @Override
    public PersonalCircleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_chart, parent, false);
        return new PersonalCircleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonalCircleAdapter.ViewHolder holder, int position) {
        OpenModel model = papularModelList.get(position);
        holder.name.setText(model.getUser_name());
        System.out.println("subcategoryy==" + model.getTask());
        try {
            holder.report_work.setText(model.getDescription());
        }catch (Exception e){
            e.printStackTrace();
        }
        int active = Integer.parseInt(model.getActive());
        int inactive = Integer.parseInt(model.getInactive());
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(active, "Active"));
        entries.add(new PieEntry(inactive, "Inactive"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        PieData data = new PieData(dataSet);
        ArrayList<Integer> colors = new ArrayList<>();

        for (int color : ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        for (int color : ColorTemplate.JOYFUL_COLORS) {
            colors.add(color);
        }

        dataSet.setColors(colors);
        holder.pieChart.setData(data);
        holder.pieChart.invalidate();
    }

    @Override
    public int getItemCount() {
        List<OpenModel> list = this.papularModelList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView report_work, name;
        PieChart pieChart;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            report_work = itemView.findViewById(R.id.report_work);
            pieChart = itemView.findViewById(R.id.piechart);
            name = itemView.findViewById(R.id.name);
        }
    }
}
