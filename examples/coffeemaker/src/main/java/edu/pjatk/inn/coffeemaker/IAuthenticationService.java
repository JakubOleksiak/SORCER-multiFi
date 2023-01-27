package edu.pjatk.inn.coffeemaker;

import sorcer.service.Context;
import sorcer.service.ContextException;

import java.rmi.RemoteException;
public interface IAuthenticationService {

    public Context giveCode(Context context) throws RemoteException, ContextException;
    public Context checkCode(Context context) throws RemoteException, ContextException;

}
