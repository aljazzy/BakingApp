package com.jatmiko.juli.bakingapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jatmiko.juli.bakingapp.MainApp;
import com.jatmiko.juli.bakingapp.R;
import com.jatmiko.juli.bakingapp.adapter.StepAdapter;
import com.jatmiko.juli.bakingapp.callback.RecipeStepOnClickListener;
import com.jatmiko.juli.bakingapp.event.EventRecipeStep;
import com.jatmiko.juli.bakingapp.model.Ingredient;
import com.jatmiko.juli.bakingapp.model.Step;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jatmiko.juli.bakingapp.utility.Constant.Data.EXTRA_INGREDIENTS;
import static com.jatmiko.juli.bakingapp.utility.Constant.Data.EXTRA_STEPS;

/**
 * Created by Miko on 10/10/2017.
 */

public class DetailFragment extends Fragment implements RecipeStepOnClickListener {
    @BindView(R.id.detail_ingredients)
    TextView DetailIngredients;

    @BindView(R.id.detail_steps)
    RecyclerView DetailSteps;

    private List<Ingredient> RecipeIngredient;
    private List<Step> RecipeStep;

    public DetailFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        initView(rootView);

        Bundle bundle = getArguments();
        String strIngredients = bundle.getString(EXTRA_INGREDIENTS);
        RecipeIngredient = Arrays.asList(MainApp.getInstance().getGson().fromJson(strIngredients, Ingredient[].class));

        String strSteps = bundle.getString(EXTRA_STEPS);
        RecipeStep = Arrays.asList(MainApp.getInstance().getGson().fromJson(strSteps, Step[].class));

        String strIngredient = "";
        for (Ingredient ingredient : RecipeIngredient) {
            DecimalFormat format = new DecimalFormat("#.##");

            strIngredient += "- " + format.format(ingredient.getQuantity())
                    + " " + ingredient.getMeasure() + " of " + ingredient.getIngredient() + ".";
            strIngredient += "\n";
        }

        DetailIngredients.setText(strIngredient);

        LinearLayoutManager recipeStepLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        DetailSteps.setLayoutManager(recipeStepLayoutManager);

        StepAdapter recipeStepsAdapter = new StepAdapter(this);
        DetailSteps.setAdapter(recipeStepsAdapter);
        recipeStepsAdapter.setDataAdapter(RecipeStep);

        ViewCompat.setNestedScrollingEnabled(DetailSteps, false);

        return rootView;
    }

    private void initView(View rootView) {
        ButterKnife.bind(this, rootView);
    }

    @Override
    public void onStepSelected(int selectedPosition) {
        EventBus eventBus = MainApp.getInstance().getEventBus();
        EventRecipeStep event = new EventRecipeStep();
        event.setSelectedPosition(selectedPosition);
        eventBus.post(event);
    }

}
