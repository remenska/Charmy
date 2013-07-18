/*
 * Created on 25-lug-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package core.internal.plugin;

import javax.swing.JOptionPane;

/**
 * @author Ezio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InternalError {

	/**
	 * 
	 */
	private boolean ok = true;

	private Error[] listError = null;
	/**
	 * 
	 */
	public InternalError() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void addError(
		int severity,
		String pluginId,
		String pluginVersion,
		String message,
		Throwable exception) {

		Error[] list;

		if (this.listError == null) {
			list = new Error[1];
		} else {
			list = new Error[this.listError.length + 1];
			System.arraycopy(listError, 0, list, 0, listError.length);
		}

		//System.out.println("sev: "+ severity + " plug: "+pluginId+" plugVers: "+pluginVersion+" message: "+message+" exception: "+exception);
		
		Error error = new Error(severity, pluginId,pluginVersion, message, exception);
		list[list.length - 1] = error;
		this.listError = list;

		this.ok = false;
	}

	/**
	 * @return
	 */
	public boolean isOk() {
		return ok;
	}

/**
 * da modificare in base alla versione del plug
 * @param pluginId
 * @return
 */
	public boolean isFatalError(String pluginId) {

		if (listError != null)
			for (int k = 0; k < listError.length; k++) {

				if (listError[k].getPluginId().compareTo(pluginId) == 0) {

					if (listError[k].getSeverity() == Error.FATAL_ERROR)
						return true;
				}
			}

		return false;
	}
	
	public void displayError(){
		
		if (!ok){
			
			String titolo="Charmy core - Internal Error";
			
			int numberFatalError =0;
			int numberError =0;
			int numberWarning =0;
			int numberInfo =0;
						
			for (int k = 0; k < listError.length; k++) {
				
				switch (listError[k].getSeverity()) {
				case Error.FATAL_ERROR :
					numberFatalError++;
				
					break;
				case Error.ERROR :
					numberError++;
					break;
				case Error.WARNING :
					numberWarning++;
					break;
				case Error.INFO :
					numberInfo++;
					break;
				}
				
			}
			
			
			String messaggio="Problem in start-up was occured.";
			
			if (numberFatalError>0)
				messaggio = messaggio +" Fatal Error = "+ numberFatalError+".";
			
			if (numberError>0)
				messaggio = messaggio +" Error = "+ numberError+".";
			
			if (numberWarning>0)
				messaggio = messaggio +" Warning = "+ numberWarning+".";
			
			if (numberInfo>0)
				messaggio = messaggio +" Info = "+ numberInfo+".";
			
			
			messaggio = messaggio + " See the log file for report.";
			
			JOptionPane.showMessageDialog(null, messaggio,titolo,JOptionPane.INFORMATION_MESSAGE);       
		}
		 
	}
}
