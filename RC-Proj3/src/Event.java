/**
 * The <code>Event</code> class which represents an event to occur in the
 * simulator.
 */
public class Event
{
	private int time;
	private String operation;
	private String[] args;
	private boolean done;

	/**
	 * <code>Event</code> constructor, which takes the name of the event,
	 * the time the event is to occur, and a list of arguments to the event.
	 * @param o the name of the event
	 * @param t the time the event is to occur
	 * @param a the arguments to the event.
	 */
	public Event(String o, int t, String[] a)
	{
		operation = o;
		time = t;
		args = a;
		done = false;
	}

	/**
	 * Return the time this event is scheduled to occur at
	 * @return current time
	 */
	public int getTime()
	{
		return time;
	}

	/**
	 * Get the operation set by this event.
	 * @return name of the operation
	 */
	public String getOperation()
	{
		return operation;
	}

	/**
	 * Get the number of arguments this event has.
	 * @return the number of arguments
	 */
	public int getNumberOfArgumentss()
	{
		return args.length;
	}

	/**
	 * Return argument i to the event which was set in the config file.
	 * @param i argument i
	 * @return the argument at i
	 */ 
	public String getArgument(int i) 
	{
		return args[i];
	}

	/**
	 * Generic toString method which describes the event and its arguments.
	 * @return String
	 */ 
	public String toString()
	{
		String s = "Event is " +operation + " to occur at "+time+" with";
		for (int i=0; i<args.length; i++)
		{
			s = s + " " + args[i];
		}
		return s;
	}

	/**
	 * Sets the event as done.
	 */
	public void setDone()
	{
		done = true;
	}

	/**
	 * Gets whether the event has occurred or not.
	 * @return boolean whether the event has occurred or not.
	 */
	public boolean getState()
	{
		return done;
	}
}
