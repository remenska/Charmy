/*
 * Created on 12-lug-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package core.internal.extensionpoint;


/**
 * @author Ezio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DeclaredHost {

	private IGenericHost host=null;
	private String idHost=null;
	/**
	 * 
	 */
	public DeclaredHost(IGenericHost host, String idHost) {
		this.host=host;
		this.idHost=idHost;
	}
	
	
	
	

	/**
	 * @return Returns the host.
	 */
	public IGenericHost getHost() {
		return host;
	}
	/**
	 * @return Returns the idHost.
	 */
	public String getIdHost() {
		return idHost;
	}
}
