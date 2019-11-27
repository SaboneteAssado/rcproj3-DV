
/*
 * My Distance-Vector Routing Implementation
 */
public class DV implements RoutingAlgorithm
{
	static final int LOCAL = -1;		// local destination/interface
	static final int UNKNOWN = -2;		// unknown destination
	static final int BROADCAST = 255;	// broadcast address
	static final int INFINITY = 60;		// "infinity" metric
	
	Router thisRouter = null;  // the router were this algorithm is running
	int interval = 0;
	boolean pReverse = false, expire = false;
	
	// declare your routing table here using DVRoutingTableEntry (see end of this file)

	public DV()
	{		
	}

	public void setRouterObject(Router obj)
	{
		thisRouter = obj;
	}

	public void setUpdateInterval(int u)
	{
		interval = u;
	}

	public void setAllowPReverse(boolean flag)
	{
		pReverse = flag;
	}

	public void setAllowExpire(boolean flag)
	{
		expire = flag;
	}

	public void initalise()
	{
	}

	public int getNextHop(int destination)
	{
		return UNKNOWN;
	}

	public void tidyTable()
	{
	}

	public Packet generateRoutingPacket(int iface)
	{
		return null;
	}

	public void processRoutingPacket(Packet p, int iface)
	{
	}

	public void showRoutes()
	{
		System.out.println("Router "+thisRouter.getId() );

	}
}



/*
 * My Routing Table Entry
 */
class DVRoutingTableEntry implements RoutingTableEntry
{

	int d, i, m, t;
	
	public DVRoutingTableEntry(int d, int i, int m, int t)
	{
		this.d = d;
		this.i = i;
		this.m = m;
		this.t = t;
	}

	public int getDestination() { return d; }

	public void setDestination(int d) { this.d = d; }

	public int getInterface() { return i; }

	public void setInterface(int i) { this.i = i; }

	public int getMetric() { return m; }

	public void setMetric(int m) { this.m = m; } 

	public int getTime() { return t; }

	public void setTime(int t) { this.t = t; }

	public String toString() 
	{
		return "" + d + " " + i + " " + m + " " + t;
	}
}

