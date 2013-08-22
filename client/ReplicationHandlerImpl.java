
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import java.rmi.RemoteException;

import java.util.Vector;

import java.util.logging.*;

public class ReplicationHandlerImpl implements ReplicationHandler {

	private class InvocationEntry {
		Method method;

		Object[] arguments;

		boolean commited = false;
	}

	private Vector invocations = new Vector();

	private Method[] methods;

	private ReplicaFactory creator;

	private Replica replica;

	//private ServiceImpl replica;

	public ReplicationHandlerImpl() {
		creator = new ReplicaFactoryImpl();
		//replica = creator.getReplica();
		//replica = new ServiceImpl();
	}

	public Object replicate(Replicable replicable, Method method,
			Object[] arguments) throws InvocationTargetException,
			IllegalAccessException, RemoteException {
		java.lang.Class type = replicable.getClass();
		if (replica == null)
			replica = creator.getReplica(type);
		Object result = new Object();
		type = replicable.getClass();
		//Logger.global.log(Level.INFO, " ------------- replicate invoked ---------");
		System.out.println(" ------------- replicate invoked ---------");
		type = replicable.getClass();
		//Logger.global.log(Level.INFO, "*Class:"+type.getName()+"\tmethod:"+method.getName()+"argc:"+arguments.length);
		System.out.print("*Class type: " + type.getName());
		System.out.print("\tMethod name: " + method.getName());
		System.out.println("\tArgs count: " + arguments);
		type = replica.getClass();
		//Logger.global.log(Level.INFO, "replicate invoked");
		//Object result = new Object();
		synchronized (this) {
			// First, invoke the method on the replica.
			try {
				Method m = type.getDeclaredMethod(method.getName(), method
						.getParameterTypes());
				result = m.invoke(replica, arguments);
				System.out.println("* replica equal method: " + m.getName()
						+ " has been invoked");
			} catch (Exception ex) {
				System.out
						.println("ERROR: while getting name of equal method on replica");
				System.out.println(ex);
			}

			int commited = 0;
			try {
				// If there are some invocations for commitment, try to
				// invoke them.
				//System.out.println("\t* invocations.size(): " + invocations.size());  
				for (int i = 0; i < invocations.size(); i++) {
					Logger.global.log(Level.INFO,
							"going to commit invocation of method {0}", method);
					InvocationEntry entry = (InvocationEntry) invocations
							.elementAt(i);
					entry.method.invoke(replicable, entry.arguments);
					entry.commited = true;
				}
			} catch (Exception ex) {
				Logger.global
						.log(
								Level.WARNING,
								"commitment of invocation of method {0} failed",
								method);
			}

			// Remove the commited methods.
			for (int i = 0; i < invocations.size(); i++) {
				InvocationEntry entry = (InvocationEntry) invocations
						.elementAt(i);
				if (entry.commited)
					invocations.removeElementAt(i--);
			}

			// Try to invoke the method on the real replicable.
			try {
				//Logger.global.log(Level.INFO, "going to invoke method {0}",
				//   method);
				result = method.invoke(replicable, arguments);
			} catch (Exception ex) {
				Throwable th = ex; // prirazeni supertridy
				if (ex instanceof InvocationTargetException)
					th = ((InvocationTargetException) ex).getTargetException();

				//Logger.global.log(Level.INFO, "invocation of {0} failed: {1}",
				//   new Object[] {method, th});

				if (isRecoverable(th)) {
					//Logger.global.log(Level.INFO, "{0} is recoverable", 
					//   th.toString());
					//Logger.global.log(Level.INFO, 
					//   "adding invocation of {0} to collection of invocations",
					//   method);

					InvocationEntry entry = new InvocationEntry();
					entry.method = method;
					entry.arguments = arguments;
					invocations.addElement(entry);
				}
			}
			Logger.global.log(Level.INFO,
					" ------------- replicate finishing ---------");

			return result;
		}
	}

	// Exceptions that are recoverable.
	private static Class[] recoverables = {
	// A connection is refused to the remote host for a remote
			// method call.
			java.rmi.ConnectException.class,
			// An IOException occured while making a connection to the
			// remote host for a remote method call.
			java.rmi.ConnectIOException.class,
			// An IOException occured while marshalling the remote call
			// header, arguments or return value for a remote method call.
			java.rmi.MarshalException.class,
			// An exception occured while unmarshalling the call header.
			java.rmi.UnmarshalException.class };

	private static boolean isRecoverable(Throwable th) {
		if (th == null)
			return false;

		for (int i = 0; i < recoverables.length; i++) {
			Class recoverable = recoverables[i];
			if (recoverable.isInstance(th))
				return true;
		}

		return false;
	}

	//   private InvocationHandler getInvocationHandler(final Replicable replicable) {
	//      return new InvocationHandler() {
	//         public Object invoke(Object proxy, Method method, Object[] arguments)
	//            throws Throwable {
	//
	//            return manager.replicate(replicable, method, arguments);
	//         }
	//      };
	//   }
	//
	//
	//   public static Replicable newReplicable(Replicable replicable, 
	//         ReplicationHandler handler) {
	//
	//      Class type = replicable.getClass();
	//
	//      return (Replicable)Proxy.newProxyInstance(type.getClassLoader(),
	//         getReplicableInterfaces(), getInvocationHandler(replicable, handler);
	//
	//   }
	//
	//   public static Class[] getReplicableInterfaces(Class type) {
	//      Vector replicables = new Vector();
	//      // Scan all interfaces up to a root.
	//      while (type != null) {
	//         Class[] interfaces = type.getInterfaces();
	//         for (int i = 0; i < interfaces.length; i++) {
	//            if (!(Replicable.class.isAssignableFrom(interfaces[i])))
	//               // This isn't a Replicable interface.
	//               continue;
	//
	//            replicables.add(interfaces[i]);
	//         }
	//
	//         type = type.getSuperclass();
	//      }
	//     
	//      Class[] result = new Class[interfaces.size()];
	//      interfaces.copyInto(result);
	//
	//      return result;
	//   }
	//
	//
	//   public Method[] getRemoteMethods(Class type) {
	//      Class[] interfaces = getAllInterfaces(type);
	//      Vector remotes = new Vector();
	//      for (int i = 0; i < interfaces.length; i++) {
	//         if (!(Remotes.class.isAssignableFrom(interfaces[i])))
	//            // The ``i''-th interface isn't a ``Remote''.
	//            continue;
	//
	//         Method[] methods = interfaces[i].getMethods();
	//         for (int j = 0; j < methods.length; j++)
	//            remotes.add(methods[j]);
	//      }
	//      
	//      Method[] result = new Method[remotes.size()];
	//      remotes.copyInto(result);
	//
	//      return result;
	//   }

}