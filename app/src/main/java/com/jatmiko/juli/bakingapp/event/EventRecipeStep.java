package com.jatmiko.juli.bakingapp.event;

/**
 * Created by Miko on 09/10/2017.
 */

public class EventRecipeStep {
    private int selectedPosition;

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }
}
