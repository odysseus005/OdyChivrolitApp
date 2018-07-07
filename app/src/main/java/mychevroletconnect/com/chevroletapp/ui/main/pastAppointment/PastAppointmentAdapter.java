package mychevroletconnect.com.chevroletapp.ui.main.pastAppointment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mychevroletconnect.com.chevroletapp.R;
import mychevroletconnect.com.chevroletapp.databinding.ItemPastAppointmentBinding;
import mychevroletconnect.com.chevroletapp.model.data.Appointment;


public class PastAppointmentAdapter extends RecyclerView.Adapter<PastAppointmentAdapter.ViewHolder> {
    private List<Appointment> appointment;
    private final Context context;
    private final PastAppointmentView view;
    private static final int VIEW_TYPE_DEFAULT = 0;


    public PastAppointmentAdapter(Context context, PastAppointmentView view) {
        this.context = context;
        this.view = view;
        appointment = new ArrayList<>();
    }


    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_DEFAULT;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemPastAppointmentBinding itemAppointmentBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_current_appointment,
                parent,
                false);

        return new ViewHolder(itemAppointmentBinding);
    }

    @Override
    public void onBindViewHolder(PastAppointmentAdapter.ViewHolder holder, int position) {
        holder.itemAppointmentBinding.setAppointment(appointment.get(position));
        holder.itemAppointmentBinding.setView(view);



    }


    public void clear() {
        appointment.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return appointment.size();
    }

    public void setAppointmentResult(List<Appointment> event) {
        this.appointment = event;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemPastAppointmentBinding itemAppointmentBinding;

        public ViewHolder(ItemPastAppointmentBinding itemAppointmentBinding) {
            super(itemAppointmentBinding.getRoot());
            this.itemAppointmentBinding = itemAppointmentBinding;
        }



    }
}