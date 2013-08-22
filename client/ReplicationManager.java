
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import java.rmi.RemoteException;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Collection;

public class ReplicationManager {
	class ReplicationEntry {
		Replicable proxy;

		ReplicationHandler handler;
	};

	private Vector replicable_interfaces = new Vector();

	private Collection replicable_methods = new Vector();

	private String name;

	private Hashtable replicables = new Hashtable();

	public ReplicationManager() {
	}

	public ReplicationManager(String name) {
		this.name = name;
	}

	public Replicable newReplicable(Replicable replicable)
			throws IllegalAccessException, InstantiationException,
			RemoteException {

		ReplicationEntry entry = (ReplicationEntry) replicables.get(replicable);
		if (entry != null) {
			// Proxy already exists.
			System.out.println("* Replicable service is already inserted.");
			return entry.proxy;
		}

		// Initialize new ``entry'' for the ``replicable''. The ``entry'' holds
		// proxy and handler of the ``replicable''.
		Class type = replicable.getClass();
		System.out.println("* Class type: " + type.getName());

		entry = new ReplicationEntry();
		// entry.proxy = replicable; // object without proxy, method is invoked
		// directly
		/*
		 * Solution without ReplicationHandler
		 * 
		 * InvocationHandler handler = new
		 * ReplicationInvocationHandler2(replicable, this);
		 * System.out.println("* ReplicationInvocationHandler created");
		 *  
		 */

		ClassLoader loader = Replicable.class.getClassLoader();
		System.out.println("* ClassLoader created");

		entry.proxy = (ReplicableService) Proxy.newProxyInstance(loader,
				new Class[] { ReplicableService.class },
				new ReplicationInvocationHandler2(replicable, this));
		System.out.println("* Proxy instance created");

		/*
		 * Old way how to create handler .... get the purpose Purpose: Get te
		 * right Class to for the RemoteService
		 * 
		 * Class handler = getReplicationHandlerClass(replicable); entry.handler =
		 * (ReplicationHandler)handler.newInstance();
		 */
		replicables.put(replicable, entry);
		System.out.println("* Replicable service created and restored.");

		entry.handler = getReplicationHandler(replicable);
		System.out.println("* ReplicationHandler returned");
		/*
		 * Inserting methods into list of replicable
		 */
		addReplicableMethods(replicable.getClass());
		System.out.println("* Class added into list "
				+ entry.proxy.getClass().getName());
		return entry.proxy;
	}

	public ReplicationHandler getReplicationHandler(Replicable replicable) {
		//System.out.println(" * Method getReplicationHandler");
		// gets entry class of reblicable instance
		ReplicationEntry entry = (ReplicationEntry) replicables.get(replicable);
		//System.out.println(" * Method getReplicationHandler: checking if
		// handler already exist");
		// checks if handler exists or NOT
		if (entry.handler == null) {
			//System.out.println(" * Method getReplicationHandler: handler
			// doesn't exist");
			// if NOT creates it
			entry.handler = (ReplicationHandler) new ReplicationHandlerImpl();
			//System.out.println(" * Method getReplicationHandler: handler
			// created");
		} // if

		//System.out.println(" * Method getReplicationHandler: returning
		// handler");
		return entry.handler;
	}

	public static Class[] getReplicableInterfaces(Class type) {
		Vector replicables = new Vector();
		// Scan all interfaces up to a root.
		while (type != null) {
			Class[] interfaces = type.getInterfaces();
			for (int i = 0; i < interfaces.length; i++) {
				if (!(Replicable.class.isAssignableFrom(interfaces[i])))
					// This isn't a Replicable interface.
					continue;

				replicables.add(interfaces[i]);
			}

			type = type.getSuperclass();
		}

		Class[] result = new Class[replicables.size()];
		replicables.copyInto(result);

		return result;
	}

	public Object replicate(Replicable replicable, Method method,
			Object[] arguments) throws Throwable {
		//System.out.println(" * ReplicationManager: method Replicate");
		ReplicationHandler handler = getReplicationHandler(replicable);
		if (handler == null)
			// (FIXME):
			throw new Exception();
		//System.out.println(" * ReplicationManager: invoking replicate method
		// on ReplicationHandler");
		return handler.replicate(replicable, method, arguments);
	}

	public static Method[] getReplicableMethods(Class type) {
		Class[] interfaces = getReplicableInterfaces(type);
		Vector remotes = new Vector();
		for (int i = 0; i < interfaces.length; i++) {
			Method[] methods = interfaces[i].getMethods();
			for (int j = 0; j < methods.length; j++)
				remotes.add(methods[j]);
		}

		Method[] result = new Method[remotes.size()];
		remotes.copyInto(result);

		return result;
	}

	public boolean isReplicable(Class type, Method method) {
		//System.out.println(" * isReplicable: sizeOf(replicable)-" +
		// replicable_methods.size() + " method - " + method.getName() + "
		// result=" + replicable_methods.contains( method ) );
		return replicable_methods.contains(method);
	}

	private void addReplicableMethods(Class type) {
		Method[] methods = getReplicableMethods(type);
		//System.out.println("* List of Replicable Methods:");
		for (int i = 0; i < methods.length; i++)
			replicable_methods.add(methods[i]);
	}
}