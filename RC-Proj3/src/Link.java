import java.util.Vector;
/**
 * A <code>Link</code> class that represents a link between two routers. 
 * It contains four packet queues, and in bound and out bound queue for 
 * each end. It also simulates the moving of packets from one end to 
 * the other end.
 */
public class Link
{
	private int[] router = new int[2];
	private int[] iface = new int[2];
	private int[] weight = new int[2];
	private int[][] counter = new int[2][2];
	@SuppressWarnings("unchecked") private Vector<Packet>[] in = new Vector[2];
	@SuppressWarnings("unchecked") private Vector<Packet>[] out = new Vector[2];
	private boolean up;

	private int SENT = 0;
	private int RECV = 1;
	//private int DROP = 2;

	/* Constructor that takes router id, interface id, and interface weight.
	 * for both ends of the link.
	 * @param r0 router 0's id
	 * @param i0 router 0's interface
	 * @param w0 weight associated with iterface 0.
	 * @param r1 router 1's id
	 * @param i1 router 1's interface
	 * @param w1 weight associated with iterface 1.
	 */
	public Link(int r0, int i0, int w0, int r1, int i1, int w1)
	{
		router[0] = r0;
		iface[0] = i0;
		weight[0] = w0;
		router[1] = r1;
		iface[1] = i1;
		weight[1] = w1;
		in[0] = new Vector<Packet>();
		out[0] = new Vector<Packet>();
		in[1] = new Vector<Packet>();
		out[1] = new Vector<Packet>();
		counter[0][SENT] = 0;
		counter[0][RECV] = 0;
		counter[1][SENT] = 0;
		counter[1][RECV] = 0;
		up = true;
	}

	/**
	 * Get the router attached to a particular end of the link, 0 specifies
	 * one end and 1 the other.
	 * @param id which end of the link (0, 1)
	 * @return the router id.
	 */
	public int getRouter(int id)
	{
		if (id == 0)
			return router[0];
		else
			return router[1];
	}

	/**
	 * Get the interface attached to a particular end of the link, 0 specifies
	 * one end and 1 the other.
	 * @param id which end of the link (0, 1)
	 * @return the interface id.
	 */
	public int getInterface(int id)
	{
		if (id == 0)
			return iface[0];
		else
			return iface[1];
	}

	/**
	 * Is the link up or down.
	 * @return showing the links status.
	 */
	public boolean isUp()
	{
		return up;
	}

	/**
	 * Sets the link status.
	 * @param s setting the links status.
	 */
	public void setState(boolean s)
	{
		up = s;
	}

	/**
	 * If the link is up moves the packets from the out queue of one end
	 * to the in queue of the other end.
	 */
	public void movePackets()
	{
		Packet p;
		Payload payload;
		synchronized (this) {
			if (isUp()) {
				while (out[0].size() > 0) {
					p = out[0].firstElement();
					// for data packets we mark them with the path.
					if (p.getType() == Packet.DATA) {
						payload = p.getPayload();
						payload.addEntry(new String(""+router[0]+"."+iface[0]+"->"+router[1]+"."+iface[1]));
						p.setPayload(payload);
					}
					in[1].addElement(p);
					out[0].removeElement(p);
				}
				while (out[1].size() > 0) {
					p = out[1].firstElement();
					// for data packets we mark them with the path.
					if (p.getType() == Packet.DATA) {
						payload = p.getPayload();
						payload.addEntry(new String(""+router[1]+"."+iface[1]+"->"+router[0]+"."+iface[0]));
						p.setPayload(payload);
					}
					in[0].addElement(p);
					out[1].removeElement(p);
				}
			}

			if (out[0].size() != 0) { System.out.println("link "+getRouter(0)+"<->"
						+getRouter(1)+" queue size of out0 "+out[0].size()); 
			}
			if (out[1].size() != 0) { System.out.println("link "+getRouter(0)+"<->"
						+getRouter(1)+" queue size of out1 "+out[1].size()); 
			}
		}
	}

	/**
	 * Places the <code>Packet</code> p, in the out bound queue for the 
	 * router specified by router id.
	 * @param routerid the router whose out bound queue to place 
	 * the packet in.
	 * @param p the packet being sent.
	 */
	public void enqueuePackets(int routerid, Packet p)
	{
		if (routerid == router[0]) { 
			out[0].addElement(p); 
			counter[0][SENT]++;
		}
		else { 
			out[1].addElement(p); 
			counter[1][SENT]++;
		}
	}

	/**
	 * Gets teh interface weight associated with the router specified
	 * by router id for this link.
	 * @param routerid
	 * @return the interface weight
	 */
	public int getInterfaceWeight(int routerid)
	{
		if (routerid == router[0]) { return weight[0]; }
		else { return weight[1]; }
	}


	/**                                                                         
	 * Retreives a <code>Packet</code>, from the in bound queue for the 
	 * router specified by router id. If no packet is present returns null.
	 * @param routerid the router whose in bound queue to remove the packet from.                                                    
	 * @return the packet being retrieved.   
	 */

	public Packet dequeuePackets(int routerid)
	{
		Packet p;
		if (routerid == router[0]) {
			//System.out.println("iface 0 size "+in[0].size()); 
			if (in[0].size() >0) {
				p = in[0].firstElement();
				in[0].removeElementAt(0);
				counter[0][RECV]++;
				return p;
			}
		}
		else {
			if (in[1].size() >0) {
				//System.out.println("iface 1 size "+in[1].size()); 
				p = in[1].firstElement();
				in[1].removeElementAt(0);
				counter[1][RECV]++;
				return p;
			}
		}
		return null;
	}

	/**
	 * Returns the queue length for a particular direction and end of 
	 * the link. 
	 * @param iface 0, 1 specifies the end.
	 * @param inbound specifies whether in it the in or out
	 * queue.
	 * @return the length of the queue
	 */
	public int queueLength(int iface, boolean inbound)
	{
		if (inbound) {
			return (in[iface]).size();
		}
		else {
			return (out[iface]).size();
		}
	}

	/**
	 * Generic to string method
	 * @return string representation
	 */
	public String toString()
	{
		String s = "Link (R0:"+router[0]+" I0:"+iface[0]+" W0:"+weight[0]+")";
		s = s + "<-->";
		s = s + "(R1:"+router[1]+" I1:"+iface[1]+" W1:"+weight[1]+")";
		return s;	
	}

	/**
	 * Returns the packet counters for this link.
	 * @return string representation of packet counters.
	 */
	public String dumpPacketStats()
	{
		String s = "(R0:"+router[0]+" I0:"+iface[0]+")";
		s = s + " s "+counter[0][SENT]+" r "+counter[0][RECV];
		s = s + "<-->";
		s = s + "(R1:"+router[1]+" I1:"+iface[1]+")";
		s = s + " s "+counter[1][SENT]+" r "+counter[1][RECV];
		return s;	
	}

}
