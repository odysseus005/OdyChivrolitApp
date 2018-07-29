package mychevroletconnect.com.chevroletapp.ui.main.pastAppointment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mychevroletconnect.com.chevroletapp.R;
import mychevroletconnect.com.chevroletapp.databinding.ItemAppointmentPastBinding;

import mychevroletconnect.com.chevroletapp.model.data.PastAppointment;
import mychevroletconnect.com.chevroletapp.util.FunctionUtils;


public class PastAppointmentAdapter extends RecyclerView.Adapter<PastAppointmentAdapter.ViewHolder> {
    private List<PastAppointment> pastappointment;
    private final Context context;
    private final PastAppointmentView view;
    private static final int VIEW_TYPE_DEFAULT = 0;


    public PastAppointmentAdapter(Context context, PastAppointmentView view) {
        this.context = context;
        this.view = view;
        pastappointment = new ArrayList<>();
    }


    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_DEFAULT;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemAppointmentPastBinding itemAppointmentBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_appointment_past,
                parent,
                false);




        return new ViewHolder(itemAppointmentBinding);
    }

    @Override
    public void onBindViewHolder(final PastAppointmentAdapter.ViewHolder holder,final int position) {


        PastAppointment app = pastappointment.get(position);



            holder.itemAppointmentBinding.setPastappointment(app);
            holder.itemAppointmentBinding.setView(view);

            holder.itemAppointmentBinding.appointmentStatusColor.setBackgroundColor(getStatusColor(app.getAppointStatus()));

            holder.itemAppointmentBinding.appointListDate.setText(FunctionUtils.appointListTimestampMonDate(app.getAppointDate()));
            holder.itemAppointmentBinding.appointListYear.setText(FunctionUtils.appointListTimestampYear(app.getAppointDate()));

            holder.itemAppointmentBinding.appointListTime.setText(app.getAppointStatus());



//        }else
//            holder.itemAppointmentBinding.appointCardPast.setVisibility(View.GONE);




    }



    public int getStatusColor(String status)
    {
        int returnColor=0;

        switch (status)
        {
            case "CONFIRMED":
                returnColor = Color.parseColor("#9ccc65");
                break;
            case "CANCELLED":

                returnColor = Color.parseColor("#b95d5d");
                break;

            case "RESCHEDULE":
                returnColor = Color.parseColor("#9ccc65");

                break;

            case "NO SHOW":
                returnColor = Color.parseColor("#424242");

                break;
            case "SUCCESSFUL":
                returnColor = Color.parseColor("#78a741");

                break;

            default:
                returnColor = Color.parseColor("#caad51");
                break;

        }

        return  returnColor;
    }

    public void clear() {
        pastappointment.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return pastappointment.size();
    }

    public void setAppointmentResult(List<PastAppointment> event) {
        this.pastappointment = event;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemAppointmentPastBinding itemAppointmentBinding;

        public ViewHolder(ItemAppointmentPastBinding itemAppointmentBinding) {
            super(itemAppointmentBinding.getRoot());
            this.itemAppointmentBinding = itemAppointmentBinding;
        }



    }
}
