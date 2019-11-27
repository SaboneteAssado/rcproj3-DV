/**
 * The <code>RoutingAlgorithm</code> interface should be implemented by any
 * class whose instances are intended to provide the routing algorithm. The
 * class that implements this algorithm must have a zero argument constructor.
 *
 * @author  Adam Greenhalgh
 * @version 1.0, 16/02/07
 * @since   JDK1.0
 */
public interface RoutingAlgorithm 
{
	static final int LOCAL = -1;
	static final int UNKNOWN = -2;
	static final int BROADCAST = 255;
	static final int INFINITY = 60;
	
	/**
	 * Given a destination address, returns the out going interface for that address. 
	 * @param destination the destination address
	 * @return the interface, or
	 *         LOCAL (-1) for a local address, or UNKNOWN (-2) for an unknown address.
	 */
	public int getNextHop(int destination);
	
	/**
	 * Passes the router object to the Routing algorithm, so that the algorithm can retrieve values from the router.
	 * @param obj
	 */
	public void setRouterObject(Router obj);
	
	/**
	 * Sets the routing update interval in time slots.
	 * @param interval the update interval
	 */
	public void setUpdateInterval(int interval);
	
	/** 
	 * Generates a routing packet from the routing table.
	 * @param iface interface id for this packet to go out
	 * @return Packet
	 */
	public Packet generateRoutingPacket(int iface);
	
	/**
	 * Given a routing packet from another host process it and add it to the routing table. 
	 * @param p the packet to process
	 * @param iface the interface it came in on
	 */
	public void processRoutingPacket(Packet p, int iface);
	
	/**
	 * A periodic task to tidy up the routing table. This method is called 
	 * at each time step and before processing any new packets.
	 */
	public void tidyTable();
	
	/**
	 * Initialize the routing algorithm. This is called once the 
	 * <code>setRouterObject</code>, setUpdateInterval,
	 * setAllowPRevers and setAllowExpire have been called.
	 */
	public void initalise();
	
	/**
	 * Prints the routing table to the screen.
	 * The output format is:
	 * <pre>
	 * Router <i>id</i>
	 * d <i>destination</i> i <i>interface</i> m <i>metric</i>
	 * d <i>destination</i> i <i>interface</i> m <i>metric</i>
	 * ...
	 * </pre>
	 */
	public void showRoutes();

	/**
	 *Enables or disables split horizon with poison reverse.
	 *@param flag a boolean indicating whether split horizon
	 *with poison reverse is on or off.
	 */
	public void setAllowPReverse(boolean flag); 

	/**
	 *Enables or disables routing table entry expiration.
	 *@param flag a boolean indicating whether entry expiration
	 *is on or off.
	 */
	public void setAllowExpire(boolean flag);
}
