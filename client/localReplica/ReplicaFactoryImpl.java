/**
 *
 *	ReplicaFactory.java
 *	<br><br>Tøída implementující rozhranní návrhového vzoru {@link factory}.  
 *	Vrací pøíslušnou instanci lokální služby.
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
	 *	Metoda vrací pøíslušnou instanci lokální služby.
	 *  
	 * @param type Typ tøídy vzdálené služby.
	 * @return Instanci pøíslušné lokální tøídy.
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