package mychevroletconnect.com.chevroletapp.ui.main.currentAppointment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mychevroletconnect.com.chevroletapp.R;
import mychevroletconnect.com.chevroletapp.databinding.ItemAppointmentDealerBinding;
import mychevroletconnect.com.chevroletapp.model.data.Appointment;
import mychevroletconnect.com.chevroletapp.model.data.Dealer;
import mychevroletconnect.com.chevroletapp.ui.main.currentAppointment.AppointmentView;


public class DealerAdapter extends RecyclerView.Adapter<DealerAdapter.ViewHolder> {
    private List<Dealer> appointment;
    private final Context context;
    private final AppointmentView view;
    private static final int VIEW_TYPE_DEFAULT = 0;


    public DealerAdapter(Context context, AppointmentView view) {
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
        ItemAppointmentDealerBinding itemDealerBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_appointment_dealer,
                parent,
                false);

        return new ViewHolder(itemDealerBinding);
    }

    @Override
    public void onBindViewHolder(DealerAdapter.ViewHolder holder, int position) {
        holder.itemDealerBinding.setDealer(appointment.get(position));
        holder.itemDealerBinding.setView(view);



    }


    public void clear() {
        appointment.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return appointment.size();
    }

    public void setDealerResult(List<Dealer> event) {
        this.appointment = event;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemAppointmentDealerBinding itemDealerBinding;

        public ViewHolder(ItemAppointmentDealerBinding itemDealerBinding) {
            super(itemDealerBinding.getRoot());
            this.itemDealerBinding = itemDealerBinding;
        }



    }
}
