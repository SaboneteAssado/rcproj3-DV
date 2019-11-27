import java.lang.Integer;

/**
 * The <code>Router</code> class represents a router. Each Router
 * references its own routing algorithm class, whose name was provided
 * in the constructor. A Router looks up the
 * class from its name and instantiates it using java reflection.
 */
public class Router 
{
	private int id;
	private Integer current_time;
	private int num_interfaces;
	private String classname;
	private Link[] link;
	private RoutingAlgorithm ralg;
	private int update_interval = 1;
	private int[] counter = new int[4];

	private int SENT = 0;
	private int RECV = 1;
	private int DROP = 2;
	private int FORW = 3;

	private boolean preverse = false;
	private boolean expire = false;

	/**
	 * <code>Router</code> constructor takes the router id, the number of
	 * interfaces, the class name of the routing algorithm to load and 
	 * the update interval, in addition to two flags indicating the level
	 * of optimization.
	 * If the routing algorithm class is not found, prints an exception.
	 * @param i router id
	 * @param n number of interfaces
	 * @param c class name of the routing algorithm to load.
	 * @param u the update interval in seconds
	 * @param pr if split horizon with poison reverse is enabled or not.
	 * @param e if entry expire is enabled or not.
	 */
	public Router(int i, int n, String c, int u, boolean pr, boolean e) 
	{
		id = i;
		num_interfaces = n;
		current_time = 0; //new Integer(0); vad:deprecated
		classname = c;
		update_interval = u;
		preverse = pr;
		expire = e;

		link = new Link[n];

		try {
			ralg = (RoutingAlgorithm)(Class.forName(classname)).getDeclaredConstructor().newInstance();
			//ralg = (RoutingAlgorithm)(Class.forName(classname)).newInstance();  vad: deprecated
			ralg.setRouterObject(this);
			ralg.setUpdateInterval(update_interval);
			ralg.setAllowPReverse(preverse);
			ralg.setAllowExpire(expire);
			ralg.initalise();
		} catch (Exception exp) {
			exp.printStackTrace();
		}

		counter[SENT] = 0;
		counter[RECV] = 0;
		counter[DROP] = 0;
		counter[FORW] = 0;

		System.out.println("Created "+this);
	}

	/**
	 * Add a link to the <code>Router</code> so the router knows about
	 * its attached links.
	 * @param l the link to attach.
	 */
	public void addLinks(Link l)
	{
		int i0 = l.getInterface(0);
		int r0 = l.getRouter(0);
		int i1 = l.getInterface(1);	
		int r1 = l.getRouter(1);
		System.out.println("Adding link "+l);
		if (r0 == id) {
			link[i0] = l;
		}
		else if (r1 == id) {
			link[i1] = l;
		}
	}

	/**
	 * Returns an array of links attached to a router
	 * @return Link[]
	 */
	public Link[] getLinks()
	{
		return link;
	}

	/**
	 * Generic toString method
	 * @return String
	 */ 
	public String toString() {
		return "Router "+id+" has "+num_interfaces+" interfaces and uses class "+classname+" update interval "+update_interval + ", split horizon + poison reverse: " + (preverse? "on " : "off ")
				+ "expire entries " + (expire? "on": "off");

	}

	/**
	 * Gets the router id
	 * @return int
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * Sends a packet, this method just calls the more complex send method
	 * below with an argument -3 for the interface signifying that the 
	 * packet is routed using the routing table. This is the default method
	 * to call.
	 * @param p Packet to sent.
	 */
	public void send(Packet p)
	{
		send(p,-3);
	}

	/**
	 * Sends a packet, either directly on an interface (if the interface is 
	 * specified as a value other than -3) or looks the interface up in the 
	 * routing table. If the interface is not known in the routing table
	 * -2 is returned and the packet is dropped. Each packet has a time to 
	 * live specified which is used to drop packets if there are routing
	 * loops. If the time to live reaches 0 the packet is dropped.
	 * @param p the packet to send
	 * @param iface the interface to send it on
	 */
	public void send(Packet p, int iface)
	{
		//System.out.println("sending "+p+" on "+iface);
		// Direct transmission
		if (p.getSource() == id) {
			counter[SENT]++;
		}

		if (iface != -3) {
	        //System.out.println("-3");
			link[iface].enqueuePackets(id,p);
			return;
		}
		int i = ralg.getNextHop(p.getDestination());
		// Unknown interface
		if (i == -2) {
			//System.out.println("Unknown interface");
			System.out.println("Dropping packet "+p);
			counter[DROP]++;
			return;
		}

		if (p.get_ttl() < 1)
		{
			System.out.println("Dropping packet "+p);
			counter[DROP]++;
		}
		else
		{
			p.decrement_ttl();
			System.out.println("Forwarded packet "+p);
			link[i].enqueuePackets(id,p);
			if (p.getSource() != id) {
				counter[FORW]++;
			}
		}
	}

	/**
	 * Receives a packet on the interface specified, if no packet is 
	 * available null is returned.
	 * @param iface to check
	 * @return Packet next packet to receive on the interface.
	 */
	public Packet recv(int iface)
	{
		Packet p;
		synchronized(link[iface]) {
			p = link[iface].dequeuePackets(id);
		}

		return p;
	}

	/**
	 * Returns the interface weight for the specified interface.
	 * @param iface
	 * @return int (weight)
	 */
	public int getInterfaceWeight(int iface)
	{
		return link[iface].getInterfaceWeight(id);
	}

	/**
	 * Returns the interface state for the specified interface, is it up or down.
	 * @param iface
	 * @return boolean (true if is Up)
	 */
	public boolean getInterfaceState(int iface)
	{
		// If the interface value is -1, it is the loop back interface, so
		// it is always up.
		if (iface == -1) { return true; }
		return link[iface].isUp();
	}

	/**
	 * Loop through all the interfaces checking to see if there is 
	 * a packet to receive and process it. If it is destined for us
	 * print a message. If it is a ROUTING packet pass it to the 
	 * routing algorithm to decode.
	 * Otherwise we forward the packet.
	 */
	private void process_packets()
	{
		Packet p;
		for (int i = 0; i<link.length; i++) {
			while((p = recv(i)) != null) {
				if (p.getDestination() == id) {
					System.out.println("Received packet "+p);
					counter[RECV]++;
				}
				else if (p.getType() == Packet.ROUTING) {
					//vad: consider type==ROUTING;s was p.getDestination() == Packet.BROADCAST
					ralg.processRoutingPacket(p,i);
					counter[RECV]++;
				}
				else {
					send(p);		    
				}
			}
		}
	}

	/**
	 * Sets the current simulation time step
	 * @param time current time step
	 */
	public void setTimeStep(int time)
	{
		synchronized(current_time) {
			current_time = time; //new Integer(time);   vad: deprecated
		}
	}

	/**
	 * Gets the current simulation time 
	 * @return int current time 
	 */
	public int getCurrentTime()
	{
		synchronized(current_time) {
			return current_time.intValue();
		}
	}

	/**
	 * Calls the routing algorithm to generate a routing
	 * table packet for each interface and sends it on that
	 * interface.
	 */
	private void sendRoutingTable()
	{
		Packet p;
		for (int i = 0; i<link.length; i++)
			// vad: if link is up and it's time to  update...
			if ( getInterfaceState(i) && (current_time%update_interval)==0) {
				p = ralg.generateRoutingPacket(i);
				if (p != null ) { 
					send(p,i);
				}
			}		
	}

	/**
	 * This method is called by the simulator each time step.
	 * It sends the routing table to all neighbors and
	 * processes all packets. We only send a routing table 
	 * every <code>update_interval</code>
	 */
	public void go()
	{
		process_packets();
		ralg.tidyTable();
		sendRoutingTable();
	}
	/**
	 * Dump the routing table to stdout
	 */
	public void dumpRoutingTable()
	{
		ralg.showRoutes();
	}

	/**
	 * Dump packet Stats to stdout for both the router and each link.
	 * s : sent , r : recv , d : drop , f : forw
	 */
	public void dumpPacketStats()
	{
		String s = "Pkt stats for "+id+" : ";
		s = s + " s "+counter[SENT]; 
		s = s + " r "+counter[RECV];
		s = s + " d "+counter[DROP];
		s = s + " f "+counter[FORW];
		s = s + "\n";
		for (int i = 0; i<link.length; i++)
		{
			s = s + link[i].dumpPacketStats()+ "\n";
		}
		System.out.print(s);
	}

	/**
	 * Returns the number of interfaces the router has.
	 * @return int, number of interfaces the router has
	 */
	public int getNumInterfaces()
	{
		return num_interfaces; 
	}
}
