package com.example.tolatrieuphu.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tolatrieuphu.R;

public class MoneyAdapter extends RecyclerView.Adapter<MoneyAdapter.ViewHolder> {
    private final String[] moneyArray;
    private int currentMoney;

    public MoneyAdapter(String[] moneyArray) {
        this.moneyArray = moneyArray;
        currentMoney = -1;
    }

    public void setCurrentMoney(int currentMoney) {
        this.currentMoney = currentMoney;
    }

    @NonNull
    @Override
    public MoneyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.money_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MoneyAdapter.ViewHolder holder, int position) {
        String money = moneyArray[position];
        holder.moneyItemContainer.setText(money);
        if (position == currentMoney) {
            holder.moneyItemContainer.setBackgroundResource(R.drawable.atp__activity_player_image_money_curent);
        } else if (position == 4 || position == 9 || position == 14) {
            holder.moneyItemContainer.setBackgroundResource(R.drawable.atp__activity_player_image_money_milestone);
        } else {
            holder.moneyItemContainer.setBackgroundResource(0);
        }

    }

    @Override
    public int getItemCount() {
        return moneyArray.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView moneyItemContainer;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            moneyItemContainer = itemView.findViewById(R.id.money_item_container);
        }
    }
}
