package service;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ReplicableService extends Replicable, Remote {
   public void increment() 
      throws RemoteException;

   public void decrement() 
      throws RemoteException;

   public int getValue() 
      throws RemoteException;    
}