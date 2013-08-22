

import java.lang.reflect.Method;


public interface ReplicationHandler {
   /**
    * The <code>replicate</code> is invoked whenever a method of the
    * <code>replicable</code> object is invoked.
    *
    * @param replicable the proxy of a remote replicable object,
    * @param method    a method that should be invoked on the 
                       <code>replicable</code>,
    * @param arguments arguments passed to the <code>method</code>.
    *
    * @throws Throwable if invocation of the <code>method</code>
    *         cannot be performed.
    */
   public Object replicate(Replicable replicable, Method method, 
         Object[] arguments)
      throws Throwable;
}
