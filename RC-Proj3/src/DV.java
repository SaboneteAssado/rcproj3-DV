
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
	}

	public void setAllowPReverse(boolean flag)
	{
	}

	public void setAllowExpire(boolean flag)
	{
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

	public DVRoutingTableEntry(int d, int i, int m, int t)
	{
	}

	public int getDestination() { return 0; }

	public void setDestination(int d) {  }

	public int getInterface() { return 0; }

	public void setInterface(int i) {  }

	public int getMetric() { return 0; }

	public void setMetric(int m) {  } 

	public int getTime() { return 0; }

	public void setTime(int t) {  }

	public String toString() 
	{
		return "";
	}
}

