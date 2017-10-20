package com.jatmiko.juli.bakingapp.event;

import com.jatmiko.juli.bakingapp.model.Recipe;

import java.util.List;

/**
 * Created by Miko on 09/10/2017.
 */

public class EventRecipe {
    private String message;
    private boolean success;
    private List<Recipe> recipes;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public String getMessage() {
        return message;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public boolean isSuccess() {
        return success;
    }
}
