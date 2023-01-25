package edu.pjatk.inn.coffeemaker.impl;

import sorcer.core.context.ServiceContext;
import sorcer.service.Context;
import sorcer.service.ContextException;

import java.io.Serializable;
import java.rmi.RemoteException;

/**
 * Class that represents a single recipe.
 *
 * @author Sarah and Mike
 */
public class Recipe implements Serializable {
    /**
     * Name of the recipe.
     */
    private String name;
    /**
     * Price of the recipe.
     */
    private int price;
    /**
     * Amount of coffee in the recipe.
     */
    private int amtCoffee;
    /**
     * Amount of milk in the recipe.
     */
    private int amtMilk;
    /**
     * Amount of sugar in the recipe.
     */
    private int amtSugar;
    /**
     * Amount of chocolate in the recipe.
     */
    private int amtChocolate;

    /**
     * Class constructor used to create new instances of Recipe initialized with default values.
     */
    public Recipe() {
        this.name = "";
        this.price = 0;
        this.amtCoffee = 0;
        this.amtMilk = 0;
        this.amtSugar = 0;
        this.amtChocolate = 0;
    }

    /**
     * Returns the amount of chocolate present in a particular Recipe instance.
     *
     * @return the amtChocolate.
     */
    public int getAmtChocolate() {
        return amtChocolate;
    }

    /**
     * Sets the amount of chocolate in a particular Recipe instance.
     *
     * @param amtChocolate the amtChocolate to setValue. Negative value will be ignored.
     */
    public void setAmtChocolate(int amtChocolate) {
        if (amtChocolate >= 0) {
            this.amtChocolate = amtChocolate;
        }
    }

    /**
     * Returns amount of coffee present in a particular Recipe instance.
     *
     * @return the amtCoffee.
     */
    public int getAmtCoffee() {
        return amtCoffee;
    }

    /**
     * Sets amount of coffee present in a particular Recipe instance.
     *
     * @param amtCoffee the amtCoffee to setValue.
     */
    public void setAmtCoffee(int amtCoffee) {
        if (amtCoffee >= 0) {
            this.amtCoffee = amtCoffee;
        }
    }

    /**
     * Returns amount of milk present in a particular Recipe instance.
     *
     * @return the amtMilk.
     */
    public int getAmtMilk() {
        return amtMilk;
    }

    /**
     * Sets amount of milk present in a particular Recipe instance.
     *
     * @param amtMilk the amtMilk to setValue.
     */
    public void setAmtMilk(int amtMilk) {
        if (amtMilk >= 0) {
            this.amtMilk = amtMilk;
        }
    }

    /**
     * Returns amount of sugar present in a particular Recipe instance.
     *
     * @return the amtSugar.
     */
    public int getAmtSugar() {
        return amtSugar;
    }

    /**
     * Sets the amount of sugar in a particular Recipe instance.
     *
     * @param amtSugar the amtSugar to setValue.
     */
    public void setAmtSugar(int amtSugar) {
        if (amtSugar >= 0) {
            this.amtSugar = amtSugar;
        }
    }

    /**
     * Returns the name of a particular Recipe instance.
     *
     * @return returns the key.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of a particular Recipe instance.
     *
     * @param name the key to setValue.
     */
    public void setName(String name) {
        if (name != null) {
            this.name = name;
        }
    }

    /**
     * Returns price of the particular Recipe instance.
     *
     * @return the price of the recipe.
     */
    public int getPrice() {
        return price;
    }

    /**
     * Sets price of the particular Recipe instance.
     *
     * @param price the price to setValue.
     */
    public void setPrice(int price) {
        if (price >= 0) {
            this.price = price;
        }
    }

    /**
     * Compares Recipe with another Recipe. Returns boolean value.
     *
     * @param r Recipe to compare to.
     * @return true if recipe names match, otherwise returns false.
     */
    public boolean equals(Recipe r) {
        if ((this.name).equals(r.getName())) {
            return true;
        }
        return false;
    }

    /**
     * Returns string representation of the Recipe object.
     *
     * @return the name of the Recipe.
     */
    public String toString() {
        return name;
    }

    /**
     * Creates a Recipe object from the provided Context.
     *
     * @param context Context object containing information needed for the Recipe.
     * @return a new Recipe object created based on the information from the provided Context.
     * @throws ContextException Throws ContextException in case the remote method has failed.
     */

    static public Recipe getRecipe(Context context) throws ContextException {
        Recipe r = new Recipe();
        try {
            r.name = (String) context.getValue("key");
            r.price = (int) context.getValue("price");
            r.amtCoffee = (int) context.getValue("amtCoffee");
            r.amtMilk = (int) context.getValue("amtMilk");
            r.amtSugar = (int) context.getValue("amtSugar");
            r.amtChocolate = (int) context.getValue("amtChocolate");
        } catch (RemoteException e) {
            throw new ContextException(e);
        }
        return r;
    }

    /**
     * Creates a new instance of Context from a provided Recipe object.
     *
     * @param recipe A Recipe object.
     * @return instance of a Context based on the Recipe object
     * @throws ContextException Throws a ContextException in case the remote method has failed.
     */
    static public Context getContext(Recipe recipe) throws ContextException {
        Context cxt = new ServiceContext();
        cxt.putValue("key", recipe.getName());
        cxt.putValue("price", recipe.getPrice());
        cxt.putValue("amtCoffee", recipe.getAmtCoffee());
        cxt.putValue("amtMilk", recipe.getAmtMilk());
        cxt.putValue("amtSugar", recipe.getAmtSugar());
        cxt.putValue("amtChocolate", recipe.getAmtChocolate());
        return cxt;
    }


}
