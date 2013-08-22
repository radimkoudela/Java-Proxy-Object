/**
 *
 *	ReplicaFactory.java
 *	<br><br>T��da implementuj�c� rozhrann� n�vrhov�ho vzoru {@link factory}.  
 *	Vrac� p��slu�nou instanci lok�ln� slu�by.
 *	
 * @author  Radim Koudela
 * @version 1.1
 * @
 */
public class ReplicaFactoryImpl implements ReplicaFactory {

	/**
	 * Konstruktor
	 *
	 */
	public ReplicaFactoryImpl() {
	}

	/**
	 *	Metoda vrac� p��slu�nou instanci lok�ln� slu�by.
	 *  
	 * @param type Typ t��dy vzd�len� slu�by.
	 * @return Instanci p��slu�n� lok�ln� t��dy.
	 */
	public Replica getReplica(java.lang.Class type) {
		String name = type.getName();
		System.out.println("++++++ CreatorServiceImpl +++++ name of class: "
				+ name);
		if (name == "RemoteService_Stub")
			name = "ServiceImpl";
		Object o = null;
		try {
			Class t = Class.forName(name);
			o = t.newInstance();
			System.out
					.println("++++++ CreatorServiceImpl +++++ instance of locla replica has been created: "
							+ o.getClass());
		} catch (Exception e) {
		}
		return (Replica) o;
	}

}