package mychevroletconnect.com.chevroletapp.ui.garage;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import mychevroletconnect.com.chevroletapp.R;
import mychevroletconnect.com.chevroletapp.app.Endpoints;
import mychevroletconnect.com.chevroletapp.databinding.ItemGarageListBinding;
import mychevroletconnect.com.chevroletapp.model.data.Garage;
import mychevroletconnect.com.chevroletapp.util.FunctionUtils;


public class GarageListAdapter extends RecyclerView.Adapter<GarageListAdapter.ViewHolder> {
    private List<Garage> garage;
    private final Context context;
    private final GarageListView view;
    private static final int VIEW_TYPE_DEFAULT = 0;


    public GarageListAdapter(Context context, GarageListView view) {
        this.context = context;
        this.view = view;
        garage = new ArrayList<>();
    }


    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_DEFAULT;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemGarageListBinding itemEventBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_garage_list,
                parent,
                false);








        return new ViewHolder(itemEventBinding);
    }

    @Override
    public void onBindViewHolder(GarageListAdapter.ViewHolder holder, final int position) {
        holder.itemEventBinding.setGarage(garage.get(position));
        holder.itemEventBinding.setView(view);



        holder.itemEventBinding.garageListEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                view.setEditGarageList(garage.get(position));

            }
        });


        holder.itemEventBinding.garageListDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                view.setDeleteGarageList(garage.get(position));


            }
        });

        holder.itemEventBinding.garageDop.setText("Date of Purchased: "+FunctionUtils.toReadable(garage.get(position).getGaragePurchase()));


        Glide.with(holder.itemView.getContext())
                .load(Endpoints.URL_GARAGE+garage.get(position).getGarageId()+garage.get(position).getGarageName()+".jpg")
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.placeholder_garage)
                .into(holder.itemEventBinding.garageListImage);



    }


    public void clear() {
        garage.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return garage.size();
    }

    public void setEventResult(List<Garage> garage) {
        this.garage = garage;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemGarageListBinding itemEventBinding;

        public ViewHolder(ItemGarageListBinding itemEventBinding) {
            super(itemEventBinding.getRoot());
            this.itemEventBinding = itemEventBinding;
        }



    }
}
