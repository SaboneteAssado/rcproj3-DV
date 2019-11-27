import java.util.Vector;
/**
 * The <code>Payload</code> class that represents the payload of a packet.
 */
public class Payload 
{

	private Vector<Object> data;

	/**
	 * The <code>Payload</code> constructor.
	 */
	public Payload()
	{
		data = new Vector<Object>();
	}

	/**
	 * Adds an object to the payload.
	 * @param o object to send
	 */
	public void addEntry(Object o)
	{
		data.addElement(o);
	}

	/**
	 * Returns a vector of the data in the payload.
	 * @return Vector data in the payload.
	 */
	public Vector<Object> getData()
	{
		return data;
	}
}
