package com.example.tolatrieuphu.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tolatrieuphu.R;
import com.example.tolatrieuphu.model.Player;

import java.util.List;
import java.util.Locale;

public class HighScoreAdapter extends RecyclerView.Adapter<HighScoreAdapter.ViewHolder> {
    private final List<Player> playerList;

    public HighScoreAdapter(List<Player> playerList) {
        this.playerList = playerList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.high_score_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position == 0) {
            holder.rank.setBackgroundResource(R.drawable.atp__leaderboard_item_rank_1);
            holder.highScoreBg.setBackgroundResource(R.color.rank1_bg);
            holder.name.setTextColor(Color.parseColor("#bb3e03"));
        } else if (position == 1) {
            holder.rank.setBackgroundResource(R.drawable.atp__leaderboard_item_rank_2);
            holder.highScoreBg.setBackgroundResource(R.color.rank2_bg);
            holder.name.setTextColor(Color.parseColor("#d9ed92"));
        } else if (position == 2){
            holder.rank.setBackgroundResource(R.drawable.atp__leaderboard_item_rank_3);
            holder.highScoreBg.setBackgroundResource(R.color.rank3_bg);
            holder.name.setTextColor(Color.parseColor("#3c096c"));
        } else {
            holder.rank.setBackgroundResource(0);
            holder.name.setTextColor(Color.parseColor("#cbb2fe"));
        }
        holder.name.setText(playerList.get(position).getName());
        holder.score.setText(String.format(Locale.US, "%,d VND", playerList.get(position).getMoneyInt()));
    }

    @Override
    public int getItemCount() {
        return playerList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView rank, name, score;
        public LinearLayout highScoreBg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rank = itemView.findViewById(R.id.high_score_rank);
            name = itemView.findViewById(R.id.high_score_name);
            score = itemView.findViewById(R.id.high_score_score);
            highScoreBg = itemView.findViewById(R.id.high_score_bg);
        }
    }
}
