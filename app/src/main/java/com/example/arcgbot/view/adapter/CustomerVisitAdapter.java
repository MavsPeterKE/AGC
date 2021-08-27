package com.example.arcgbot.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcgbot.BR;
import com.example.arcgbot.database.entity.CustomerVisit;
import com.example.arcgbot.database.views.CustomerView;
import com.example.arcgbot.viewmodels.CustomerViewModel;

import java.util.List;

public class CustomerVisitAdapter extends RecyclerView.Adapter<CustomerVisitAdapter.GenericViewHolder> {

    private int layoutId;
    private List<CustomerVisit> customerVisitList;
    private CustomerViewModel viewModel;

    public CustomerVisitAdapter(@LayoutRes int layoutId, CustomerViewModel viewModel) {
        this.layoutId = layoutId;
        this.viewModel = viewModel;
    }

    private int getLayoutIdForPosition(int position) {
        return layoutId;
    }

    @Override
    public int getItemCount() {
        return customerVisitList == null ? 0 : customerVisitList.size();
    }

    @NonNull
    public GenericViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false);

        return new GenericViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GenericViewHolder holder, int position) {
        holder.bind(viewModel, position);
    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }

    public void setCustomerVisitList(List<CustomerVisit> customerVisitList) {
        this.customerVisitList = customerVisitList;
        notifyDataSetChanged();
    }

    class GenericViewHolder extends RecyclerView.ViewHolder {
        final ViewDataBinding binding;

        GenericViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(CustomerViewModel viewModel, Integer position) {
            binding.setVariable(BR.model, viewModel);
            binding.setVariable(BR.visit, customerVisitList.get(position));
            binding.executePendingBindings();
           /* binding.getRoot().findViewById(R.id.layout_main).setOnClickListener(view -> viewModel.onCustomerClick(customerList.get(position)));*/
        }

    }
}

