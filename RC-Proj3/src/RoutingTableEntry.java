public interface RoutingTableEntry
{
    /**
     * returns destination field of this table entry.
     * @return the destination
     */
	public int getDestination();
	
    /**
     * sets destination field of this table entry.
     * @param d the destination
     */
	public void setDestination(int d);
	
	/**
	 * returns interface field of this table entry.
	 * @return the interface field
	 */
	public int getInterface();
	
	/**
	 * sets interface field of this table entry.
	 * @param i the interface
	 */
	public void setInterface(int i);
	
	/**
	 * gets metric field of this table entry.
	 * @return the metric
	 */
	public int getMetric();
	
	/**
	 * sets metric field of this table entry.
	 * @param m the metric
	 */
	public void setMetric(int m);
	
	/**
	 * gets time field (time added to table) of this table entry.
	 * @return the time
	 */
	public int getTime();
	
	/**
	 * sets time field (time added to table) of this table entry.
	 * @param t the time
	 */
	public void setTime(int t);
	
	/**
	 * returns printable version of entire table entry as String.
	 * @return the String
	 */
	public String toString();
}
