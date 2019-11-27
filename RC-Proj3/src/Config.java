import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Vector;
/**
 * The <code>Config</code> class reads the configuration file
 * and configures the <code>Simulator</code>.
 * @author  Adam Greenhalgh                                                    
 * @version 1.0, 16/02/07                                                      
 * @since   JDK1.0    
 */
public class Config  
{
	private BufferedReader input;
	private Simulator sim;

	private Vector<Router> routers = new Vector<Router>();
	private Vector<Event> events = new Vector<Event>();
	private Vector<Link> links = new Vector<Link>();

	private int updateInterval = 1;
	private boolean preverse = false;
	private boolean expire = false;

	/**
	 * <code>Config</code> constructor which loads the configuration
	 * file and configures the simulator.
	 * @param String filename of the configuration file
	 * @param Simulator s, reference to the simulator object
	 */
	Config (String filename, Simulator s)
	{
		sim = s;
		System.out.println("Reading file "+filename);
		try {
			input = new BufferedReader(new FileReader(filename));

			String str;
			while ((str = input.readLine()) != null) {
				process(str);
			}

			input.close();
		}catch (IOException e) {
			System.err.println(e.getMessage());
		}
		sim.add_routers(routers);
		sim.add_links(links);
		sim.add_events(events);
	}

	/**
	 * Processes each line of the configuration file and creates the
	 * appropriate objects
	 * @param String line of config from the config file
	 */
	private void process(String s)
	{
		String[] result = s.split("\\s");

		if (result.length == 1) return;


		if (result[0].equals("router")) {
			Router r = new Router(Integer.parseInt(result[1]),Integer.parseInt(result[2]),
					result[3], updateInterval, preverse, expire);
			routers.addElement(r);
		}
		else if (result[0].equals("link")) {
			Link l = new Link(Integer.parseInt(result[1].split("\\.")[0]),
					Integer.parseInt(result[1].split("\\.")[1]),
					Integer.parseInt(result[1].split("\\.")[2]),
					Integer.parseInt(result[2].split("\\.")[0]),
					Integer.parseInt(result[2].split("\\.")[1]),
					Integer.parseInt(result[2].split("\\.")[2]));
			if (result.length == 4) {
				if (result[3].equals("up") ) {
					l.setState(true);
				}
				else if (result[3].equals("down")) {
					l.setState(false);
				}
				else {
					System.out.println("Unknown link state "+result[4]);
				}

			}
			links.addElement(l);
		}
		else if (result[0].equals("send") ) {
			int arg_len = result.length - 2;
			String[] args = new String[arg_len];
			for (int i=0; i< arg_len; i++) {
				args[i] = result[i+2];
			}
			Event e = new Event(result[0],Integer.parseInt(result[1]),args);
			events.addElement(e);
		}
		else if (result[0].equals("uplink") || result[0].equals("downlink")) {
			int arg_len = result.length;
			String[] args = new String[arg_len];
			args[0] = result[2].split("\\.")[0];
			args[1] = result[2].split("\\.")[1];
			args[2] = result[3].split("\\.")[0];
			args[3] = result[3].split("\\.")[1];
			Event e = new Event(result[0],Integer.parseInt(result[1]),args);
			events.addElement(e);
		}
		else if (result[0].equals("stop")) {
			sim.set_stop_time(Integer.parseInt(result[1]));
		}
		else if (result[0].equals("updateInt")){
			//Sanity check the update interval 
			int u = Integer.parseInt(result[1]);
			updateInterval = u;
			if(u <= 0){
				System.out.println("Update interval should be a positive integer");
				System.exit(1);
			}
		}
		else if (result[0].equals("preverse")){
			if(result[1].equals("on")){
				System.out.println("Preverse on");
				preverse = true;
			}
			else if (result[1].equals("off")){
				preverse = false;
			}
			else {
				System.out.println("preverse should be either off or on, not " + result[1]);
				System.exit(1);
			}
		}
		else if (result[0].equals("expire")){
			if(result[1].equals("on")){
				expire = true;
			}
			else if (result[1].equals("off")){
				expire = false;
			}
			else {
				System.out.println("expire should be either off or on, not " + result[1]);
				System.exit(1);
			}
		}
		else if (result[0].equals("dumprt")) {
			String[] args = new String[1];
			args[0] = result[2];
			Event e = new Event(result[0],Integer.parseInt(result[1]),args);
			events.addElement(e);
		}
		else if (result[0].equals("dumpPacketStats")) {
			String[] args = new String[1];
			args[0] = result[2];
			Event e = new Event(result[0],Integer.parseInt(result[1]),args);
			events.addElement(e);
		}
		else if (result[0].startsWith("#")) {
			// skipping comments
		}
		else {
			System.out.println("Something is wrong with this line : "+s);
			System.exit(1);
		}
	}
}
