package com.jatmiko.juli.bakingapp.activity;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.jatmiko.juli.bakingapp.MainApp;
import com.jatmiko.juli.bakingapp.R;
import com.jatmiko.juli.bakingapp.adapter.SimpleRecipesAdapter;
import com.jatmiko.juli.bakingapp.callback.SimpleRecipeOnClickListener;
import com.jatmiko.juli.bakingapp.controller.MainController;
import com.jatmiko.juli.bakingapp.event.EventRecipe;
import com.jatmiko.juli.bakingapp.model.Ingredient;
import com.jatmiko.juli.bakingapp.model.Recipe;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jatmiko.juli.bakingapp.utility.Constant.Data.EXTRA_RECIPE;
import static com.jatmiko.juli.bakingapp.utility.Constant.Data.LIST_DATA;
import static com.jatmiko.juli.bakingapp.utility.Constant.Data.LIST_STATE;
import static com.jatmiko.juli.bakingapp.utility.Constant.Data.WIDGET_ID;

/**
 * Created by Miko on 18/10/2017.
 */

public class WidgetConfigActivity extends AppCompatActivity implements SimpleRecipeOnClickListener {
    @BindView(R.id.recipe_list)
    RecyclerView mBakingRecipeList;
    private SimpleRecipesAdapter mAdapter;
    private EventBus eventBus;
    private int mAppWidgetId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_config);

        eventBus = MainApp.getInstance().getEventBus();

        initView();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mBakingRecipeList.setLayoutManager(layoutManager);
        mBakingRecipeList.setHasFixedSize(true);

        mAdapter = new SimpleRecipesAdapter(this);
        mBakingRecipeList.setAdapter(mAdapter);

        if (savedInstanceState != null) {
            mBakingRecipeList.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(LIST_STATE));
            mAdapter.setDataAdapter(Arrays.asList(MainApp.getInstance().getGson().fromJson(savedInstanceState.getString(LIST_DATA), Recipe[].class)));
            mAppWidgetId = savedInstanceState.getInt(WIDGET_ID);
            return;
        }

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        MainController controller = new MainController();
        controller.getRecipes();
    }

    private void initView() {
        ButterKnife.bind(this);
    }

    @Override
    public void onRecipeSelected(Recipe recipe) {
        putRecipeToWidget(recipe);
    }

    private void putRecipeToWidget(Recipe recipe) {
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_RECIPE, MainApp.getInstance().getGson().toJson(recipe));
        intent.putExtras(bundle);

        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getBaseContext());

        RemoteViews views = new RemoteViews(getBaseContext().getPackageName(), R.layout.ingredient_layout);

        views.setTextViewText(R.id.widg_ingredients_title, getString(R.string.widget_ingredients_title, recipe.getName()));

        String strIngredient = "";
        for (Ingredient ingredient : recipe.getIngredients()) {
            DecimalFormat format = new DecimalFormat("#.##");

            strIngredient += "- " + format.format(ingredient.getQuantity())
                    + " " + ingredient.getMeasure() + " of " + ingredient.getIngredient() + ".";
            strIngredient += "\n";
        }

        views.setTextViewText(R.id.widg_detail_ingredients, strIngredient);

        views.setOnClickPendingIntent(R.id.widg_detail_ingredients, pendingIntent);

        appWidgetManager.updateAppWidget(mAppWidgetId, views);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getRecipes(EventRecipe event) {
        if (event.isSuccess()) {
            mAdapter.setDataAdapter(event.getRecipes());
        } else {
            Toast.makeText(this, getString(R.string.err_widget), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LIST_STATE, mBakingRecipeList.getLayoutManager().onSaveInstanceState());
        outState.putString(LIST_DATA, MainApp.getInstance().getGson().toJson(mAdapter.getDataAdapter()));
        outState.putInt(WIDGET_ID, mAppWidgetId);
    }
}
