package com.jatmiko.juli.bakingapp.controller;

import com.jatmiko.juli.bakingapp.MainApp;
import com.jatmiko.juli.bakingapp.event.EventRecipe;
import com.jatmiko.juli.bakingapp.model.Recipe;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.jatmiko.juli.bakingapp.utility.Constant.Data.BAKING_APP_URL;

/**
 * Created by Miko on 09/10/2017.
 */

public class MainController {
    private EventBus eventBus = MainApp.getInstance().getEventBus();
    private EventRecipe event = new EventRecipe();

    public void getRecipes() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().get().url(BAKING_APP_URL).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonResponse = response.body().string();
                List<Recipe> recipes = Arrays.asList(MainApp.getInstance().getGson().fromJson(jsonResponse, Recipe[].class));
                event.setMessage(response.message());
                event.setRecipes(recipes);
                if (response.code() == 200) {
                    event.setSuccess(true);
                } else {
                    event.setSuccess(false);
                }
                eventBus.post(event);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                event.setMessage(e.getMessage());
                event.setSuccess(false);
                eventBus.post(event);
            }
        });
    }
}
