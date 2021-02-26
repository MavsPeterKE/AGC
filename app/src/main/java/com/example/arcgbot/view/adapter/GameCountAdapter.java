package com.example.arcgbot.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcgbot.BR;
import com.example.arcgbot.database.entity.GameCount;
import com.example.arcgbot.models.GameModel;
import com.example.arcgbot.viewmodels.GameCountViewModel;

import java.util.List;

public class GameCountAdapter extends RecyclerView.Adapter<GameCountAdapter.GenericViewHolder> {

    private int layoutId;
    private List<GameModel> gameCountList;
    private GameCountViewModel viewModel;

    public GameCountAdapter(@LayoutRes int layoutId, GameCountViewModel viewModel) {
        this.layoutId = layoutId;
        this.viewModel = viewModel;
    }

    private int getLayoutIdForPosition(int position) {
        return layoutId;
    }

    @Override
    public int getItemCount() {
        return gameCountList == null ? 0 : gameCountList.size();
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

    public void setGameCountList(List<GameModel> gameCountList) {
        this.gameCountList = gameCountList;
        notifyDataSetChanged();
    }

    class GenericViewHolder extends RecyclerView.ViewHolder {
        final ViewDataBinding binding;

        GenericViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(GameCountViewModel viewModel, Integer position) {
            binding.setVariable(BR.model, viewModel);
            binding.setVariable(BR.gameCountModel, gameCountList.get(position));
            binding.setVariable(BR.position, position);
            binding.executePendingBindings();
        }

    }
}

