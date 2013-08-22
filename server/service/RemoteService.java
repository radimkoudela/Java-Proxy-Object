package service;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.Vector;

public class RemoteService extends Object implements ReplicableService {
  private Vector v = new Vector();
  protected int value;
  private Service service = new ServiceImpl(5);
  
  public RemoteService() throws RemoteException {
      //super(); // calling constructor of parent's class
      
      UnicastRemoteObject.exportObject(this);
      value=0;
  }
  
  public int getValue() throws RemoteException
    { return service.getValue(); }

  public void increment() throws RemoteException
    { service.increment(); }
  
  public void decrement() throws RemoteException
    { service.decrement(); }
/*  
   public Class getReplicationHandlerClass()
      throws RemoteException {
      return ReplicationHandlerImpl.class;
   }   
 */
}