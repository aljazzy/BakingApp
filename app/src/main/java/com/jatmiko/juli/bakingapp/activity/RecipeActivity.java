package com.jatmiko.juli.bakingapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.jatmiko.juli.bakingapp.MainApp;
import com.jatmiko.juli.bakingapp.R;
import com.jatmiko.juli.bakingapp.event.EventRecipeStep;
import com.jatmiko.juli.bakingapp.fragment.DetailFragment;
import com.jatmiko.juli.bakingapp.fragment.StepDetailFragment;
import com.jatmiko.juli.bakingapp.model.Ingredient;
import com.jatmiko.juli.bakingapp.model.Recipe;
import com.jatmiko.juli.bakingapp.model.Step;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.ButterKnife;

import static com.jatmiko.juli.bakingapp.utility.Constant.Data.EXTRA_INGREDIENTS;
import static com.jatmiko.juli.bakingapp.utility.Constant.Data.EXTRA_IS_RECIPE_MENU;
import static com.jatmiko.juli.bakingapp.utility.Constant.Data.EXTRA_RECIPE;
import static com.jatmiko.juli.bakingapp.utility.Constant.Data.EXTRA_STEP;
import static com.jatmiko.juli.bakingapp.utility.Constant.Data.EXTRA_STEPS;
import static com.jatmiko.juli.bakingapp.utility.Constant.Data.EXTRA_STEP_FIRST;
import static com.jatmiko.juli.bakingapp.utility.Constant.Data.EXTRA_STEP_LAST;
import static com.jatmiko.juli.bakingapp.utility.Constant.Data.EXTRA_STEP_NUMBER;
import static com.jatmiko.juli.bakingapp.utility.Constant.Data.EXTRA_STEP_POSITION;

/**
 * Created by Miko on 20/10/2017.
 */

public class RecipeActivity extends AppCompatActivity{
    private Recipe mDetailRecipe;
    private FragmentManager fragmentManager;
    private boolean mIsRecipeMenu = false;
    private EventBus eventBus;
    private List<Ingredient> mRecipeIngredients;
    private List<Step> mRecipeSteps;
    private int mSelectedPosition = -1;
    private boolean isTwoPanel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list);

        eventBus = MainApp.getInstance().getEventBus();

        mDetailRecipe = MainApp.getInstance().getGson().fromJson(this.getIntent().getExtras().getString(EXTRA_RECIPE), Recipe.class);
        mRecipeIngredients = mDetailRecipe.getIngredients();
        mRecipeSteps = mDetailRecipe.getSteps();

        initView();

        isTwoPanel = findViewById(R.id.fragment_menu) != null;

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.recipe_fragment, new Fragment()).commit();

        showRecipeMenu();

        if (savedInstanceState != null) {
            mIsRecipeMenu = savedInstanceState.getBoolean(EXTRA_IS_RECIPE_MENU);
            mSelectedPosition = savedInstanceState.getInt(EXTRA_STEP_POSITION);
            if (mIsRecipeMenu) {
                showRecipeMenu();
            } else {
                showRecipeStepFragment(mSelectedPosition);
            }
        }
    }

    private void showRecipeMenu() {
        setTitle(mDetailRecipe.getName());

        DetailFragment fragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_INGREDIENTS, MainApp.getInstance().getGson().toJson(mRecipeIngredients));
        bundle.putString(EXTRA_STEPS, MainApp.getInstance().getGson().toJson(mRecipeSteps));
        fragment.setArguments(bundle);

        if (isTwoPanel)
            fragmentManager.beginTransaction().replace(R.id.recipe_fragment, fragment).commit();
        else fragmentManager.beginTransaction().replace(R.id.recipe_fragment, fragment).commit();
        mIsRecipeMenu = true;
    }

    private void initView() {
        ButterKnife.bind(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        eventBus.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        eventBus.unregister(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showRecipeStep(EventRecipeStep event) {
        if (mSelectedPosition != event.getSelectedPosition()) {
            mSelectedPosition = event.getSelectedPosition();
            showRecipeStepFragment(mSelectedPosition);
        }
    }

    private void showRecipeStepFragment(int stepNumber) {
        Step step = mRecipeSteps.get(stepNumber);
        setTitle(mDetailRecipe.getName() + " - " + step.getShortDescription());

        StepDetailFragment fragment = new StepDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_STEP, MainApp.getInstance().getGson().toJson(step));
        bundle.putInt(EXTRA_STEP_NUMBER, stepNumber);
        bundle.putBoolean(EXTRA_STEP_FIRST, stepNumber == 0);
        bundle.putBoolean(EXTRA_STEP_LAST, stepNumber == (mRecipeSteps.size() - 1));
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.recipe_fragment, fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
        mIsRecipeMenu = false;
    }

    @Override
    public void onBackPressed() {
        if (isTwoPanel) {
            super.onBackPressed();
            return;
        }
        Fragment fragmentById = fragmentManager.findFragmentById(R.id.recipe_fragment);
        if (fragmentById instanceof StepDetailFragment) {
            showRecipeMenu();
            mSelectedPosition = -1;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_STEP_POSITION, mSelectedPosition);
        outState.putBoolean(EXTRA_IS_RECIPE_MENU, mIsRecipeMenu);
    }
}
