package com.example.arcgbot.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcgbot.BR;
import com.example.arcgbot.R;
import com.example.arcgbot.database.entity.Customer;
import com.example.arcgbot.viewmodels.CustomerViewModel;
import com.example.arcgbot.viewmodels.GameItemViewModel;

import java.util.List;

public class GamerSuggestAdapter extends RecyclerView.Adapter<GamerSuggestAdapter.GenericViewHolder> {

    private int layoutId;
    private List<Customer> customerList;
    private GameItemViewModel viewModel;

    public GamerSuggestAdapter(@LayoutRes int layoutId, GameItemViewModel viewModel) {
        this.layoutId = layoutId;
        this.viewModel = viewModel;
    }

    private int getLayoutIdForPosition(int position) {
        return layoutId;
    }

    @Override
    public int getItemCount() {
        return customerList == null ? 0 : customerList.size();
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

    public void setCustomerList(List<Customer> customerList) {
        this.customerList = customerList;
        notifyDataSetChanged();
    }

    class GenericViewHolder extends RecyclerView.ViewHolder {
        final ViewDataBinding binding;

        GenericViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(GameItemViewModel viewModel, Integer position) {
           binding.setVariable(BR.model, viewModel);
           binding.setVariable(BR.customer, customerList.get(position));
            binding.executePendingBindings();
            binding.getRoot().findViewById(R.id.layout_main).setOnClickListener(view -> viewModel.onGamerItemClick(customerList.get(position)));
        }

    }
}

