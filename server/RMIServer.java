

/**
 * 
 * RMIServer.java<br><br>
 * Trida vytvari instanci vzdalene sluzby a distribuje pres RMI.
 * 
 * @author Radim Koudela
 *
 */

import service.*;
import java.rmi.RMISecurityManager;
import java.rmi.Naming;


public class RMIServer {
	/**
	 * Staticka metoda main tridy RMIServer.
	 * 
	 * @param arg Vstupni z prikazove radky.
	 */
	public static void main(String[] arg) {
		try {
			//System.setSecurityManager( new RMISecurityManager() );
			RemoteService service = new RemoteService();
			System.out.println("* RemoteService created");
			Naming.rebind("//localhost:1099/SERVICE", service);
			System.out.println("* RMI object binded on //localhost:1099/SERVICE");
		} catch (Exception e) {
			System.out.println(" Error while building remote SERVICE !");
			System.out.println(e);
		}
	}
}