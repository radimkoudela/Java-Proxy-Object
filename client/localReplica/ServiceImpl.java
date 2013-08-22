
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.*;

public class ServiceImpl extends Object implements Service, Replica {
	private Object state;

	private int value;

	public ServiceImpl() {
		super();
		//UnicastRemoteObject.exportObject(this);
	}

	public ServiceImpl(int value) {
		super();
		//UnicastRemoteObject.exportObject(this);

		this.value = value;
	}

	//public void setState(Object state) 
	//   throws RemoteException {

	//   Logger.global.log(Level.INFO, "setting state to: " +
	//      state);
	//   this.state = state;
	//}

	//public Object getState() 
	//   throws RemoteException {

	//   Logger.global.log(Level.INFO, "returning state: " +
	//      state);
	//   return state;
	//}

	public int getValue() {

		//      Logger.global.log(Level.INFO, "returning value: " +
		//         value);
		return value;
	}

	public void decrement() {
		value--;
		//       Logger.global.log(Level.INFO, "decrementing value: " +
		//         value);
	}

	public void increment() {
		value++;
		//Logger.global.log(Level.INFO, "incrementing value: " +
		//         value);
	}
	/*
	 public Class getReplicationHandlerClass()
	 throws RemoteException {
	 return ReplicationHandlerImpl.class;
	 }   
	 */
}