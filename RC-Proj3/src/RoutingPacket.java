/**
 * The <code>RoutingPacket</code> constructor. This is a subclass of the packet
 * super class and merely sets the packet type to ROUTING.
 */ 
public class RoutingPacket extends Packet
{
	/**
	 * <code>RoutingPacket</code> constructor. Calls the super class and 
	 * sets the type to ROUTING.
	 */
	RoutingPacket(int src, int dst)
	{
		super(src,dst);
		type = Packet.ROUTING;
	}
}
