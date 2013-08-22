/**
 * 
 * ReplicaFactory.java <br>
 * <br>
 * Rozhranní návrhového vzoru factory. Metoda {@link getReplica}vrací
 * pøíslušnou instanci lokální služby.
 * 
 * @author Radim Koudela
 * @version 1.1 @
 */

public interface ReplicaFactory {

	/**
	 * Metoda {@link getReplica} vrací pøíslušnou instanci lokální služby.
	 * 
	 * @param type
	 *            Typ tøídy vzdálené služby.
	 * @return Instanci pøíslušné lokální tøídy.
	 */
	public Replica getReplica(java.lang.Class type);

}