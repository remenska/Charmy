/*
 * Created on 12-mar-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package core.internal.extensionpoint.event;

/**
 * @author eziolotta
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EventBase {

	
	private String localHostID = "";
	
	/**
	 * costruttore.
	 *
	 */
	public EventBase(String localHostID) {
					
		this.localHostID = localHostID;
		
	}
	/**
	 * @return Returns the eventID.
	 */
	public String getEventID() {
		
		return this.getClass().getName();
	}
	

	/**
	 * @return
	 */
	public String getLocalHostID() {
		return localHostID;
	}

}
