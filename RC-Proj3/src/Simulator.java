import java.util.Vector;

/**  
 * The <code>Simulator</code> class loads the configuration and runs each
 * router with the correct class for that router as specified in the 
 * configurtion file. At each time step the Simulator carries out (in order)
 * event processing <code>process_events</code>, routers tasks 
 * <code>process_routers</code>, packet forwarding <code>process_packets</code>
 *
 * @author  Adam Greenhalgh                                                    
 * @version 1.0, 16/02/07  
 * @since   JDK1.0                                                             
 */
public class Simulator
{
	private String config_file;
	private int stop_time=20;
	private Router[] routers;
	private Event[] events;
	private Link[] links;
	private int packet_counter=0;

	/**
	 * <code>Simulator</code> constructor, loads the configuration given
	 * the config file cf.
	 * @param cf configuration file
	 */
	public Simulator(String cf)
	{
		config_file = cf;
		System.out.println("Loading configuration : "+config_file);
		new Config(config_file,this);
	}

	/** 
	 * Simple toString method.
	 * @return String
	 */
	public String toString()
	{
		return "UCL CS Routing Sim";
	}

	/**
	 * Sets the duration for the simulation.
	 * @param t duration of the simulation
	 */ 
	public void set_stop_time(int t)
	{
		stop_time = t;
	}

	/**
	 * Adds the routers created in the <code>Config</code> class to the 
	 * the simulator.
	 * @param r a list of routers.
	 */
	public void add_routers(Vector<Router> r)
	{
		routers = new Router[r.size()];
		for (Router tr : r) {
			routers[tr.getId()] = tr;
		}
	}

	/**
	 * Adds the events created in the <code>Config</code> class to the 
	 * the simulator.
	 * @param e a list of events.
	 */
	public void add_events(Vector<Event> e)
	{
		events = new Event[e.size()]; 
		int count = 0;
		for (Event te : e) {
			System.out.println("Adding "+te);
			events[count] = te;
			count++;
		}
	}

	/**
	 * Adds the links created in the <code>Config</code> class to the 
	 * the simulator and attaches them to the router objects.
	 * @param l a list of links.
	 */
	public void add_links(Vector<Link> l)
	{
		links = new Link[l.size()];
		int count = 0;
		for (Link ml : l) {
			links[count] = ml;
			count++;
		}
		for (int i=0; i<links.length; i++) {
			routers[links[i].getRouter(0)].addLinks(links[i]); 
			routers[links[i].getRouter(1)].addLinks(links[i]); 
		}

	}

	/** 
	 * Process the events scheduled for the time <code>now</code>
	 * @param now current time
	 */
	private void process_events(int now)
	{
		Event event;
		Packet packet;

		//	while (myenum.hasMoreElements()) {
		for (int count = 0; count < events.length ; count++) {
			event = events[count];
			if ((event.getTime() <= now) && (event.getState() != true)) {
				if (event.getOperation().equals("send")) {
					packet = new Packet(Integer.parseInt(event.getArgument(0)),Integer.parseInt(event.getArgument(1)));
					packet.setType(Packet.DATA);
					packet_counter++;
					packet.setSequenceNumber(packet_counter);
					routers[packet.getSource()].send(packet);

				}
				else if (event.getOperation().equals("uplink")) {
					for (int i=0; i<links.length;i++) {
						if (links[i].getRouter(0) == Integer.parseInt(event.getArgument(0)) && 
								links[i].getInterface(0) == Integer.parseInt(event.getArgument(1)) && 
								links[i].getRouter(1) == Integer.parseInt(event.getArgument(2)) && 
								links[i].getInterface(1) == Integer.parseInt(event.getArgument(3)) ) {
							System.out.println("Setting link status to up "+links[i]);
							links[i].setState(true);
						}
					}
				}
				else if (event.getOperation().equals("downlink")) {
					for (int i=0; i<links.length;i++) {
						if (links[i].getRouter(0) == Integer.parseInt(event.getArgument(0)) && 
								links[i].getInterface(0) == Integer.parseInt(event.getArgument(1)) && 
								links[i].getRouter(1) == Integer.parseInt(event.getArgument(2)) && 
								links[i].getInterface(1) == Integer.parseInt(event.getArgument(3)) ) {
							System.out.println("Setting link status to down "+links[i]);
							links[i].setState(false);
						}
					}
				}
				else if (event.getOperation().equals("dumprt")) {
					if (event.getArgument(0).equals("all")) {
						for (int i=0; i<routers.length; i++) {
							routers[i].dumpRoutingTable();
						}   
					}
					else {
						routers[Integer.parseInt(event.getArgument(0))].dumpRoutingTable();
					}
				}
				else if (event.getOperation().equals("dumpPacketStats")) {
					System.out.println("event "+event);
					if (event.getArgument(0).equals("all")) {
						for (int i=0; i<routers.length; i++) {
							routers[i].dumpPacketStats();
						}   
					}
					else {
						routers[Integer.parseInt(event.getArgument(0))].dumpPacketStats();
					}
				}
				events[count].setDone();
			}
		}
	}

	/**
	 * Process packets, move them from the out queue of one end of the link
	 * to the in queue of the other end. Do this for both ends.
	 */
	private void process_packets()
	{
		for (int i = 0; i<links.length; i++) {
			links[i].movePackets();	    
		}
	}

	/**
	 * Process routers, set the time step for now and then call 
	 * the <code>go</code> method of the router object.
	 * @param now current time.
	 */
	private void process_routers(int now)
	{
		for (int i = 0; i<routers.length; i++) {
			routers[i].setTimeStep(now);
			routers[i].go();
		}
	}

	/**
	 * Main loop of the simulator that runs through all the tasks at each
	 * time step.  At each time step the Simulator carries out (in order)   
	 * event processing <code>process_events</code>, routers tasks       
	 * <code>process_routers</code>, 
	 * packet forwarding <code>process_packets</code>
	 *                                   
	 */
	public void main_loop()
	{
		for (int now=1; now<stop_time; now++) {
			System.out.println("Time :"+now);
			process_events(now);
			process_routers(now);
			process_packets();
		}
		check_completed();
	}

	/**
	 * Checks that all events have been processed.
	 */
	private void check_completed()
	{
		int unused_events = 0;
		for (int i=0; i<events.length; i++)
		{
			if (events[i].getState() != true) {
				unused_events++;
			}
		}
		if (unused_events > 0) 
		{
			System.out.println("Error, "+unused_events+" events not run.");
		}
	}

	/**
	 * main function called from the command line with one argument
	 * which is the configuration file.
	 * @param args
	 */
	public static void main (String args[])
	{
		if (args.length != 1) {
			System.out.println("Usage : java Simulator <config file>");
			System.exit(1);
		}
		Simulator sim = new Simulator(args[0]);
		sim.main_loop();
	}
}
