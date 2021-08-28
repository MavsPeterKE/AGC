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
import com.example.arcgbot.database.entity.CompletedGame;
import com.example.arcgbot.database.views.GameView;
import com.example.arcgbot.models.ScreenItem;
import com.example.arcgbot.viewmodels.CompletedGameSearchViewModel;
import com.example.arcgbot.viewmodels.SearchItemViewModel;

import java.util.List;

public class CompletedGameSearchAdapter extends RecyclerView.Adapter<CompletedGameSearchAdapter.GenericViewHolder> {

    private int layoutId;
    private List<CompletedGame> screenItemList;
    private CompletedGameSearchViewModel viewModel;

    public CompletedGameSearchAdapter(@LayoutRes int layoutId, CompletedGameSearchViewModel viewModel) {
        this.layoutId = layoutId;
        this.viewModel = viewModel;
    }

    private int getLayoutIdForPosition(int position) {
        return layoutId;
    }

    @Override
    public int getItemCount() {
        return screenItemList == null ? 0 : screenItemList.size();
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

    public void setScreenList(List<CompletedGame> screenList) {
        this.screenItemList = screenList;
        notifyDataSetChanged();
    }

    class GenericViewHolder extends RecyclerView.ViewHolder {
        final ViewDataBinding binding;

        GenericViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(CompletedGameSearchViewModel viewModel, Integer position) {
            binding.setVariable(BR.model, viewModel);
            binding.setVariable(BR.screen, screenItemList.get(position));
            binding.setVariable(BR.position, position);
            binding.executePendingBindings();
            binding.getRoot().findViewById(R.id.layout_main).setOnClickListener(view -> {
                viewModel.onCompletedGameClick(screenItemList.get(position));
            });
        }

    }
}

