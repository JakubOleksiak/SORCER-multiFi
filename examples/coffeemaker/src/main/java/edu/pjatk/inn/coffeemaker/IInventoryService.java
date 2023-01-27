package edu.pjatk.inn.coffeemaker;

import sorcer.service.Context;
import sorcer.service.ContextException;

import java.rmi.RemoteException;

public interface IInventoryService {

    public Context checkInventory(Context context) throws RemoteException, ContextException;


}
