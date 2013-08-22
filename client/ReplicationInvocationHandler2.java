
import java.lang.reflect.*;

public class ReplicationInvocationHandler2 implements InvocationHandler {
	Replicable replica;

	ReplicationManager manager;

	public ReplicationInvocationHandler2(Replicable replica,
			ReplicationManager manager) {
		this.replica = replica;
		this.manager = manager;
	}

	public Object invoke(Object proxy, Method method, Object[] arguments)
			throws Throwable {
		if (!manager.isReplicable(proxy.getClass(), method)) {
			//System.out.println("* InvocationHandler: The ``method'' is not defined in a ``Replicable'' interface.");
			// The ``method'' is not defined in a ``Replicable'' interface.

			// Don't contact replication manager at all; do the invocation
			// immediately.
			return method.invoke(replica, arguments);
		}
		// Delegate the invocation to replication manager.
		//System.out.println(" * InvocationHandler: Delegate the invocation to replication manager.");
		return manager.replicate(replica, method, arguments);

	}
}