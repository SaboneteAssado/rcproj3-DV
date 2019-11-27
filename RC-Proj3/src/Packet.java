import java.util.Vector;
/**
 * The <code>Packet</code> class models a network packet. 
 */
public class Packet
{
	/**
	 * The unknown packet type, used before a packet is classified.
	 */ 
	public static int UNKNOWN = 0;
	/**
	 * The data packet type.
	 */
	public static int DATA = 1;
	/**
	 * A Routing Packet
	 */
	public static int ROUTING = 2;

	/**
	 * The Broadcast address
	 */ 
	public static int BROADCAST = 255;
	/**
	 * The unknown address.
	 */
	public static int UNKNOWNADDR = -1;

	protected int src;
	protected int dst;
	protected Payload data;
	protected int ttl = 255;
	protected int type;
	protected int seq;

	/**
	 * <code>Packet</code> constructor for the super class. This defaults to
	 * setting the packet type to be the UNKNOWN type. 
	 * @param s source address
	 * @param d destination address
	 */
	public Packet(int s, int d)
	{
		src = s;
		dst = d;
		type = UNKNOWN;
		data = new Payload();
		seq = 0;
	}

	/**
	 * Gets the source address
	 * @return int source address
	 */ 
	public int getSource()
	{
		return src;
	}

	/**
	 * Gets the packet type
	 * @return int packet type
	 */
	public int getType()
	{
		return type;
	}

	/**
	 * Sets the packet type
	 * @param t packet type
	 */
	public void setType(int t)
	{
		type = t;
	}

	/**
	 * Gets the destination address
	 * @return int destination address
	 */
	public int getDestination()
	{
		return dst;
	}

	/**
	 * Sets the packet sequence number, this is for marking purposes.
	 * @param s sequence number
	 */
	public void setSequenceNumber(int s)
	{
		seq = s;
	}

	/**
	 * Simple to string method.
	 * @return String string representation
	 */
	public String toString()
	{
		String s;
		Vector<Object> d;
		s = "src "+src+" dst "+dst+" type "+type+" ttl "+ttl+" seq "+seq;
		if (type == DATA) {
			s = s + " path";
			d = data.getData();
			for (int i=0; i<d.size();i++) {
				s = s + " (" + ((String)d.elementAt(i)).toString()+")";
			}
		}
		return s;
	}

	/**
	 * Sets the Payload for the packet.
	 * @param d the packets payload
	 */ 
	public void setPayload(Payload d)
	{
		data = d;
	}

	/**
	 * Gets the Payload of the packet.
	 * @return Payload the packets's payload.
	 */ 
	public Payload getPayload()
	{
		return data;
	}

	/** 
	 * Reduces the ttl by 1, ttl is set as 255 when a packet is created.
	 */
	public void decrement_ttl()
	{
		ttl--;
	}

	/**
	 * Gets the current packet ttl
	 * @return int current ttl
	 */
	public int get_ttl()
	{
		return ttl;
	}
}
