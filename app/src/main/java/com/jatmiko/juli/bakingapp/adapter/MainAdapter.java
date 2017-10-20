package com.jatmiko.juli.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jatmiko.juli.bakingapp.R;
import com.jatmiko.juli.bakingapp.callback.RecipeOnClickListener;
import com.jatmiko.juli.bakingapp.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jatmiko.juli.bakingapp.utility.Constant.Function.setImageResource;

/**
 * Created by Miko on 26/08/2017.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.RecipeViewHolder>{
    private List<Recipe> mRecipes;
    private RecipeOnClickListener mCallback;

    public MainAdapter(RecipeOnClickListener callback) {
        mRecipes = new  ArrayList<>();
        mCallback = callback;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapt_recipes, parent, false);
        return new RecipeViewHolder(contView);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        Recipe recipe = mRecipes.get(holder.getAdapterPosition());
        Context context = holder.itemView.getContext();

        holder.recipeTitle.setText(recipe.getName());

        if (recipe.getIngredients().size() == 1) {
            holder.recipeIngredient.setText(context.getString(R.string.recipe_ing_single, recipe.getIngredients().size()));
        } else {
            holder.recipeIngredient.setText(context.getString(R.string.recipe_ing_multiple, recipe.getIngredients().size()));
        }
        if (recipe.getSteps().size() == 1) {
            holder.recipeStep.setText(context.getString(R.string.recipe_step_single, recipe.getSteps().size()));
        } else {
            holder.recipeStep.setText(context.getString(R.string.recipe_step_multiple, recipe.getSteps().size()));
        }
        if (recipe.getServings() == 1) {
            holder.recipeServing.setText(context.getString(R.string.recipe_serving_single, recipe.getServings()));
        } else {
            holder.recipeServing.setText(context.getString(R.string.recipe_serving_multiple, recipe.getServings()));
        }

        if (!TextUtils.isEmpty(recipe.getImage())) {
            setImageResource(context, recipe.getImage(), holder.recipeImage);
        }
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public void setDataAdapter(List<Recipe> recipes) {
        mRecipes.clear();
        mRecipes.addAll(recipes);
        notifyDataSetChanged();
    }

    public List<Recipe> getDataAdapter() {
        return mRecipes;
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.adapt_rec_title)
        TextView recipeTitle;

        @BindView(R.id.adapt_rec_cview)
        CardView recipeCardView;

        @BindView(R.id.adapt_rec_ingredient)
        TextView recipeIngredient;

        @BindView(R.id.adapt_rec_step)
        TextView recipeStep;

        @BindView(R.id.adapt_rec_serving)
        TextView recipeServing;

        @BindView(R.id.adapt_rec_image)
        ImageView recipeImage;

        RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            recipeCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDetail(mRecipes.get(getAdapterPosition()));
                }
            });
        }
    }

    private void showDetail(Recipe recipe){
        mCallback.onRecipeSelected(recipe);
    }
}
