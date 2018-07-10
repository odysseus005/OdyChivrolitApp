package mychevroletconnect.com.chevroletapp.ui.main.currentAppointment;

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
import mychevroletconnect.com.chevroletapp.databinding.ItemAppointmentServiceBinding;
import mychevroletconnect.com.chevroletapp.model.data.Service;
import mychevroletconnect.com.chevroletapp.util.FunctionUtils;


public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {
    private List<Service> service;
    private final Context context;
    private final AppointmentView view;
    private static final int VIEW_TYPE_DEFAULT = 0;
    private int chooseService;
    private String selected;
    private List<String> selectedList;
    private boolean checkpms;


    public ServiceAdapter(Context context, AppointmentView view) {
        this.context = context;
        this.view = view;
        service = new ArrayList<>();
        selectedList = new ArrayList<>();
        chooseService = -1;
        checkpms=false;
        selected="";

    }


    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_DEFAULT;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       final ItemAppointmentServiceBinding itemServiceBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_appointment_service,
                parent,
                false);





        return new ViewHolder(itemServiceBinding);
    }

    @Override
    public void onBindViewHolder(final ServiceAdapter.ViewHolder holder, final int position) {



        final String id =String.valueOf(service.get(position).getServiceId());


            holder.itemServiceBinding.setService(service.get(position));
            holder.itemServiceBinding.setView(view);

        holder.itemServiceBinding.checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

              if(service.get(position).getServiceName().contains("Preventive Maintenance") || service.get(position).getServiceName().contains("PMS") )
              {
                  view.loadKms(checkpms);
                  if(checkpms)
                      checkpms=false;
                  else
                      checkpms=true;
              }


              if(selectedList.contains(id))
                selectedList.remove(id);
              else
                  selectedList.add(id);




            }
        });

    }






    public void clear() {
        service.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return service.size();
    }


    public String getSelectedService()
    {
        selected="";
        for (String value : selectedList)
        {
            selected += value+",";
        }

       return FunctionUtils.removeLastChar(selected);
    }


    public void setServiceResult(List<Service> event) {
        this.service = event;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemAppointmentServiceBinding itemServiceBinding;

        public ViewHolder(ItemAppointmentServiceBinding itemServiceBinding) {
            super(itemServiceBinding.getRoot());
            this.itemServiceBinding = itemServiceBinding;
        }



    }
}
