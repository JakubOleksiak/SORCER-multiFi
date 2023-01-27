package edu.pjatk.inn.coffeemaker;

import sorcer.service.Context;
import sorcer.service.ContextException;

import java.rmi.RemoteException;

public interface INextGenCoffemakerService {

    public Context makeCoffee(Context context) throws RemoteException, ContextException;
    public Context storeCoffee(Context context) throws RemoteException, ContextException;
    public Context giveCoffee(Context context) throws RemoteException, ContextException;
    public Context addRecipe(Context context) throws RemoteException, ContextException;
    public Context getRecipes(Context context) throws RemoteException, ContextException;

}
