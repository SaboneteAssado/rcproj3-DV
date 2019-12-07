import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

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
	Map<Integer, RoutingTableEntry> rt;
	
	// declare your routing table here using DVRoutingTableEntry (see end of this file)

	public DV()
	{
		rt = new HashMap<Integer, RoutingTableEntry>();
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
		RoutingTableEntry rte = new DVRoutingTableEntry(thisRouter.getId(), LOCAL, 0, 0);
		rt.put(rte.getDestination(), rte);
	}

	public int getNextHop(int destination)
	{
		RoutingTableEntry rte = rt.get(destination);
		if ( rte != null )
			return rte.getInterface();
		return -2;
	}

	public void tidyTable()
	{
		Iterator<Integer> it = rt.keySet().iterator();
		while ( it.hasNext() ) {
			RoutingTableEntry rte = rt.get(it.next());
			boolean isUp = thisRouter.getInterfaceState(rte.getInterface());
			if ( !isUp ) {
				rte.setMetric(INFINITY);
			}
		}
	}

	public Packet generateRoutingPacket(int iface)
	{
		Packet pkt = new RoutingPacket(thisRouter.getId(), BROADCAST);
		Payload pl = new Payload();
		Iterator<Integer> it = rt.keySet().iterator();
		while ( it.hasNext() )
			pl.addEntry(rt.get(it.next()));
		pkt.setPayload(pl);
		
		return pkt;
	}

	public void processRoutingPacket(Packet p, int iface)
	{
		Payload pl = p.getPayload();
		Vector<Object> v = pl.getData();
		Iterator<Object> it = v.iterator();
		//iterar as entradas recebidas
		while ( it.hasNext() ) {
			RoutingTableEntry rte = (RoutingTableEntry) it.next();
			RoutingTableEntry rteLocal = rt.get(rte.getDestination());
			
			//acabei de conhecer um destino novo
			if ( rteLocal == null) {
				rteLocal = new DVRoutingTableEntry(rte.getDestination(), iface, rte.getMetric() + thisRouter.getInterfaceWeight(iface), 0);
				rt.put(rteLocal.getDestination(), rteLocal);
			}
			//o caminho deste e menor que o que conhecia
			else if ( rteLocal.getMetric() > ( rte.getMetric() + thisRouter.getInterfaceWeight(iface)) ){
				rteLocal.setMetric(rte.getMetric() + thisRouter.getInterfaceWeight(iface));
				rteLocal.setInterface(iface);
				rt.put(rteLocal.getDestination(), rteLocal);
			}
		}
	}

	public void showRoutes()
	{
		System.out.println("Router "+thisRouter.getId() );
		Iterator<Integer> it = rt.keySet().iterator();
		while ( it.hasNext() ) {
			RoutingTableEntry rte = rt.get(it.next());
			System.out.println( "d " + rte.getDestination() + " i " + rte.getInterface() + " m " + rte.getMetric());
		}
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
		return null; //"" + d + " " + i + " " + m + " " + t;
	}
}

