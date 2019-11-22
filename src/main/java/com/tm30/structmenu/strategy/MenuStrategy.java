package com.tm30.structmenu.strategy;

public class MenuStrategy extends Strategy {

    public MenuStrategy() {
        this.setSlug("menu");
        this.setUniqueId(0);

        this.setChildStrategies(new TravelStrategy(), new HealthStrategy());
    }

    @Override
    public String implement() {
        String menu = this.generateMenu();
        return "Welcome to Test Menu Service\n" + menu;
    }
}
