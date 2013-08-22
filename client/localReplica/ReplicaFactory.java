/**
 * 
 * ReplicaFactory.java <br>
 * <br>
 * Rozhrann� n�vrhov�ho vzoru factory. Metoda {@link getReplica}vrac�
 * p��slu�nou instanci lok�ln� slu�by.
 * 
 * @author Radim Koudela
 * @version 1.1 @
 */

public interface ReplicaFactory {

	/**
	 * Metoda {@link getReplica} vrac� p��slu�nou instanci lok�ln� slu�by.
	 * 
	 * @param type
	 *            Typ t��dy vzd�len� slu�by.
	 * @return Instanci p��slu�n� lok�ln� t��dy.
	 */
	public Replica getReplica(java.lang.Class type);

}