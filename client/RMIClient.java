
import java.rmi.Naming;

public class RMIClient {

	protected Replicable counter;

	public static void main(String[] arg) {
		RMIClient app = new RMIClient();
		app.procesCounter();
	}

	public void procesCounter() {
		int count = 0;
		try {

			//ReplicableService service = (ReplicableService)
			// Naming.lookup("//192.168.31.108:1099/SERVICE");
			ReplicableService service = (ReplicableService) Naming
					.lookup("//localhost:1099/SERVICE");

			ReplicationManager manager = new ReplicationManager("MAN");
			/*
			 * Part of testing code Examing methods ''getReplicableInterfaces'' &
			 * ''getReplicableMethods''
			 * 
			 * Class[] r_i = manager.getReplicableInterfaces( service.getClass() );
			 * System.out.println("* List of Replicable Interfaces:"); for (int
			 * i = 0; i < r_i.length; i++) { System.out.println(" " + (i+1) + ".
			 * interface - " + r_i[i].getName() ); } Method[] r_i_m =
			 * manager.getReplicableMethods( service.getClass() );
			 * System.out.println("* List of Replicable Methods:"); for (int j =
			 * 0; j < r_i_m.length; j++) System.out.println(" " + (j+1) + ".
			 * method - " + r_i_m[j].getName() );
			 */

			System.out.println();

			Replicable rep = (Replicable) manager.newReplicable(service);
			ReplicableService replica = null;
			if (rep instanceof ReplicableService)
				replica = (ReplicableService) rep;
			else
				System.out.println("NEE");
			//Replicable replica2 = manager.newReplicable(service);
			System.out.println(" * starting");
			while (true) {
				System.out.println(" * " + ++count + ". try:");
				//System.out.println(" COUNTER value: " + replica.getValue() );
				System.out
						.println(" **** COUNTER VALUE: " + replica.getValue());
				replica.increment();
				try {
					System.in.read();
				} catch (Exception e) {
				}
				//System.out.println(" COUNTER increment: " + replica.getValue() );
				//               replica.decrement();
				try {
					System.in.read();
				} catch (Exception e) {
				}

				//System.out.println(" COUNTER decrement: " + replica.getValue() );
				//System.out.println(" COUNTER value: " + replica.getValue() );

				java.lang.Thread.sleep(1000);
			}
		} catch (Exception e) {
			System.out.println(" Error while building remote service!");
			e.printStackTrace();
		}
	}
}