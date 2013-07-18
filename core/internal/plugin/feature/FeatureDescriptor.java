/*
 * Created on 3-ago-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package core.internal.plugin.feature;

import core.internal.plugin.PluginModel;

/**
 * @author Ezio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FeatureDescriptor extends PluginModel {

	private PluginModel[] listaPluginFeature = new PluginModel[0];

	private boolean featureDefault = false;

	/**
	 * 
	 */
	public FeatureDescriptor() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param identifier
	 * @param version
	 */
	public FeatureDescriptor(String identifier, String version) {
		super(identifier, version);
		// TODO Auto-generated constructor stub
	}

	public void addPluginFeature(String identifier, String version) {

		boolean stop = false;
		if (this.listaPluginFeature != null)
			for (int i = 0; i < listaPluginFeature.length; i++) {

				if (listaPluginFeature[i].matchModel(identifier, version)) {
					stop = true;
					break;
				}

			}

		if (!stop) {

			PluginModel[] list;

			if (this.listaPluginFeature == null) {
				list = new PluginModel[1];
			} else {
				list = new PluginModel[this.listaPluginFeature.length + 1];
				System.arraycopy(listaPluginFeature, 0, list, 0,
						listaPluginFeature.length);
			}

			list[list.length - 1] = new PluginModel(identifier, version);

			this.listaPluginFeature = list;

		}

	}

	/**
	 * @return Returns the featureDefault.
	 */
	public boolean isFeatureDefault() {
		return featureDefault;
	}

	/**
	 * @param featureDefault The featureDefault to set.
	 */
	public void setFeatureDefault(boolean featureDefault) {
		this.featureDefault = featureDefault;
	}
	/**
	 * @return Returns the listaPluginFeature.
	 */
	public PluginModel[] getListaPluginFeature() {
		return listaPluginFeature;
	}
}
