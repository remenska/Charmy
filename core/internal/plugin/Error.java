/*
 * Created on 25-lug-2005
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
public class Error {

	public static final int WARNING = 1;
	public static final int INFO = 2;

	public static final int ERROR = 3;
	public static final int FATAL_ERROR = 4;

	private String message;

	private Throwable exception = null;

	private String pluginId;
	private String pluginVersion;

	private int severity = 0;

	/**
	 * 
	 */
	public Error(
		int severity,
		String pluginId,
		String pluginVersion,
		String message,
		Throwable exception) {

		this.severity = severity;
		this.pluginId = pluginId;
		this.message = message;
		this.exception = exception;
		this.pluginVersion=pluginVersion;

	}

	/**
	 * @return
	 */
	public Throwable getException() {
		return exception;
	}

	/**
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return
	 */
	public String getPluginId() {
		return pluginId;
	}

	/**
	 * @return
	 */
	public int getSeverity() {
		return severity;
	}

	/**
	 * @return
	 */
	public String getPluginVersion() {
		return pluginVersion;
	}

}
