/*
 * Created on 27-lug-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package core.internal.plugin.file;

/**
 * @author Ezio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IIdCollection {

	
	public void restoreFromFile();
	
	public void resetForNewFile();
	
	public boolean setIdElemento(long identificativo, Class classElement);
	
	public long getIdElemento(Class classElement);
	
}
