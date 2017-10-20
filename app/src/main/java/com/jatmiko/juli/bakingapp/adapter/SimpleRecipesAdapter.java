package com.jatmiko.juli.bakingapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jatmiko.juli.bakingapp.R;
import com.jatmiko.juli.bakingapp.callback.SimpleRecipeOnClickListener;
import com.jatmiko.juli.bakingapp.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Miko on 18/10/2017.
 */

public class SimpleRecipesAdapter extends RecyclerView.Adapter<SimpleRecipesAdapter.SimpleViewHolder> {

    private SimpleRecipeOnClickListener Callback;
    private List<Recipe> Recipes;

    public SimpleRecipesAdapter(SimpleRecipeOnClickListener callback) {
        Recipes = new ArrayList<>();
        this.Callback = callback;
    }


    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapt_simple_recipes, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        holder.simpleRecipesTitle.setText(Recipes.get(holder.getAdapterPosition()).getName());
    }

    @Override
    public int getItemCount() {
        return Recipes.size();
    }

    public void setDataAdapter(List<Recipe> recipes) {
        Recipes.clear();
        Recipes.addAll(recipes);
        notifyDataSetChanged();
    }

    public List<Recipe> getDataAdapter() {
        return Recipes;
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.recipes_title)
        TextView simpleRecipesTitle;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            simpleRecipesTitle.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Callback.onRecipeSelected(Recipes.get(getAdapterPosition()));
        }
    }
}
