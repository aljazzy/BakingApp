package com.jatmiko.juli.bakingapp.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jatmiko.juli.bakingapp.R;
import com.jatmiko.juli.bakingapp.callback.RecipeStepOnClickListener;
import com.jatmiko.juli.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Miko on 09/10/2017.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {
    private static int clickedPos;
    private List<Step> mStep;
    private RecipeStepOnClickListener callback;

    public StepAdapter(RecipeStepOnClickListener callback) {
        this.mStep = new ArrayList<>();
        this.callback = callback;
        clickedPos = -1;
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cview = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapt_recipe_step, parent, false);
        return new  StepViewHolder(cview);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        holder.recipeStepTitle.setText(mStep.get(holder.getAdapterPosition()).getShortDescription());

        final Context context = holder.itemView.getContext();
        if (clickedPos == position) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.black));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return mStep.size();
    }
    public void setDataAdapter(List<Step> steps){
        mStep.clear();
        mStep.addAll(steps);
        notifyDataSetChanged();
    }
    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.adapt_rec_step_title)
        TextView recipeStepTitle;

        @BindView(R.id.adapt_rec_step_cardview)
        CardView recipeStepCardView;

        public StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            recipeStepCardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickedPos = getAdapterPosition();
            showRecDetail(clickedPos);
            notifyDataSetChanged();
        }

        private void showRecDetail(int selectedPosition) {
            callback.onStepSelected(selectedPosition);
        }
    }
}
