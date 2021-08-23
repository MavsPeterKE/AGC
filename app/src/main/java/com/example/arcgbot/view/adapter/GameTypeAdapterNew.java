package com.example.arcgbot.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcgbot.BR;
import com.example.arcgbot.R;
import com.example.arcgbot.database.entity.GameType;
import com.example.arcgbot.viewmodels.GameCountViewModel;
import com.example.arcgbot.viewmodels.GameItemViewModel;

import java.util.List;

public class GameTypeAdapterNew extends RecyclerView.Adapter<GameTypeAdapterNew.GenericViewHolder> {

    private int layoutId;
    private List<GameType> gameTypeList;
    private GameItemViewModel viewModel;

    public GameTypeAdapterNew(@LayoutRes int layoutId, GameItemViewModel viewModel) {
        this.layoutId = layoutId;
        this.viewModel = viewModel;
    }

    private int getLayoutIdForPosition(int position) {
        return layoutId;
    }

    @Override
    public int getItemCount() {
        return gameTypeList == null ? 0 : gameTypeList.size();
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

    public void setGameTypeList(List<GameType> gameTypeList) {
        this.gameTypeList = gameTypeList;
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
            binding.setVariable(BR.gameType, gameTypeList.get(position));
            binding.executePendingBindings();
            binding.getRoot().findViewById(R.id.main).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewModel.onGameTypeClick(gameTypeList.get(position));
                }
            });
        }

    }
}

