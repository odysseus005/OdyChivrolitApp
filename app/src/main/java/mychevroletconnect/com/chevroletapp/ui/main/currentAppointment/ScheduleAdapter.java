package mychevroletconnect.com.chevroletapp.ui.main.currentAppointment;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mychevroletconnect.com.chevroletapp.R;
import mychevroletconnect.com.chevroletapp.databinding.ItemAppointmentTimeBinding;
import mychevroletconnect.com.chevroletapp.model.data.Schedule;
import mychevroletconnect.com.chevroletapp.util.FunctionUtils;


public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private List<Schedule> schedule;
    private final Context context;
    private final AppointmentView view;
    private static final int VIEW_TYPE_DEFAULT = 0;
    private int chooseSchedule;



    public ScheduleAdapter(Context context, AppointmentView view) {
        this.context = context;
        this.view = view;
        schedule = new ArrayList<>();
        chooseSchedule = -1;


    }


    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_DEFAULT;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ItemAppointmentTimeBinding itemScheduleBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_appointment_time,
                parent,
                false);





        return new ViewHolder(itemScheduleBinding);
    }

    @Override
    public void onBindViewHolder(final ScheduleAdapter.ViewHolder holder, final int position) {



        int slot = (Integer.parseInt(schedule.get(position).getScheduleLimit()) - Integer.parseInt(schedule.get(position).getScheduleReserve()));
        if(slot<0)
            slot=0;


            Log.d("slot>>>",slot+"");



            holder.itemScheduleBinding.timeslotReserveC.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {



                    int slot = (Integer.parseInt(schedule.get(position).getScheduleLimit()) - Integer.parseInt(schedule.get(position).getScheduleReserve()));

                    if(slot == 0)
                    {
                        view.showError("Selected slot is unavailable!");
                    }else {

                        if (chooseSchedule == position) {
                            chooseSchedule = -1;
                            holder.itemScheduleBinding.timeslotReserveC.setCardBackgroundColor(Color.parseColor("#f3bc00"));
                        } else {
                            chooseSchedule = position;
                            holder.itemScheduleBinding.timeslotReserveC.setCardBackgroundColor(Color.parseColor("#bb8c00"));
                            notifyDataSetChanged();

                        }
                    }

                }
            });


        checkClickStatus(position,holder,slot);

        holder.itemScheduleBinding.timeslott.setText(FunctionUtils.hour24to12hour(schedule.get(position).getScheduleTime()));
        holder.itemScheduleBinding.timeslotRemain.setText(slot+" slot");
        holder.itemScheduleBinding.setSchedule(schedule.get(position));
        holder.itemScheduleBinding.setView(view);

    }

    public void checkClickStatus(int position,ScheduleAdapter.ViewHolder holder,int slot)
    {


        if(slot == 0)
        {
            holder.itemScheduleBinding.timeslotReserveC.setCardBackgroundColor(Color.parseColor("#70000000"));
        }
        else {
            if (chooseSchedule != position) {
                holder.itemScheduleBinding.timeslotReserveC.setCardBackgroundColor(Color.parseColor("#f3bc00"));

            } else {
                holder.itemScheduleBinding.timeslotReserveC.setCardBackgroundColor(Color.parseColor("#bb8c00"));
            }

        }


    }

    public void reset()
    {
        chooseSchedule = -1;
    }


    public void clear() {
        schedule.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return schedule.size();
    }


    public int getChoosenSchedule()
    {
        if(chooseSchedule != -1)
        return schedule.get(chooseSchedule).getScheduleId();
        else
            return 0;
    }

    public String getChoosenScheduleValue()
    {
        if(chooseSchedule != -1)
        return schedule.get(chooseSchedule).getScheduleTime();
        else
            return "false";
    }





    public void setScheduleResult(List<Schedule> event) {
        this.schedule = event;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemAppointmentTimeBinding itemScheduleBinding;

        public ViewHolder(ItemAppointmentTimeBinding itemScheduleBinding) {
            super(itemScheduleBinding.getRoot());
            this.itemScheduleBinding = itemScheduleBinding;
        }



    }
}
