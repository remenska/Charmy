/*
 * Created on 15-lug-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package core.internal.plugin;

/**
 * @author Ezio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PluginModel {

	/**
	 * spazio dei nomi del plug-in, rappresenta un identificatore
	 * unico del plug in
	 */
	protected String identifier = null;

	/**
		 * stringa indicante la versione del plug
		 */
	protected String version = null;
	/**
	 * 
	 */
	
	/**
	 * lista degli oggetti da cui dipende quello rappresentato da questa classe
	 */
	protected PluginModel[] dependenceList = null;
	
	/**
	   * se è true allora il plugin sarà istanziato dal sistema in fase di sturt up
	   */
	protected boolean isInFeature = false;
	
	
	public PluginModel() {

	}

	public PluginModel(String identifier,String version ) {

		this.identifier=identifier;
		this.version=version;
	}
	
	
	/**
	 * @return Returns the dependenceList.
	 */
	public PluginModel[] getDependenceList() {
		
		if (dependenceList==null)
			return new PluginModel[0];
		
		return dependenceList;
	}
	/**
	 * @param dependenceList The dependenceList to set.
	 */
	public void setDependenceList(PluginModel[] dependenceList) {
		this.dependenceList = dependenceList;
	}
	/**
	 * @return Returns the identifier.
	 */
	public String getIdentifier() {
		return identifier;
	}
	/**
	 * @param identifier The identifier to set.
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	/**
	 * @return Returns the version.
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version The version to set.
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	/**
	 * ritorna true se il plugmodel rappresentato da questooggetto ha id e versione uguale
	 * al plugmodel passato come argomento.
	 * false altrimenti.
	 * @param plug
	 * @return
	 * @author Ezio
	 */
	public boolean matchModel(PluginModel plugObject) {

		if (plugObject == null)
				return false;
		if ((plugObject.getIdentifier()== null) ||(this.identifier==null))
			return false;

		if((this.getIdentifier().compareTo(plugObject.getIdentifier()) == 0)
		&& ((this.getVersion()==null) &&(plugObject.getVersion()==null)))
			return true;
		
		if ((this.getVersion()==null)||(plugObject.getVersion()==null))
			return false;
		
		return (
			(this.getIdentifier().compareTo(plugObject.getIdentifier()) == 0)
				&& (this.getVersion().compareTo(plugObject.getVersion()) == 0));

	}

	public boolean matchModel(String idPlugin, String versionPlugin) {

	
		if ((idPlugin== null) ||(this.identifier==null))
			return false;

		if((this.getIdentifier().compareTo(idPlugin) == 0)
		&& ((this.getVersion()==null) &&(versionPlugin==null)))
			return true;
		
		if ((this.getVersion()==null)||(versionPlugin==null))
			return false;
		
		return (
			(this.getIdentifier().compareTo(idPlugin) == 0)
				&& (this.getVersion().compareTo(versionPlugin) == 0));

	}
	
	/**
	 * @return Returns the isInFeature.
	 */
	public boolean isInFeature() {
		return isInFeature;
	}

	/**
	 * @param isInFeature The isInFeature to set.
	 */
	public void setInFeature(boolean isInFeature) {
		this.isInFeature = isInFeature;
	}

}
