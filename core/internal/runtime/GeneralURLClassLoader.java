package core.internal.runtime;

import java.net.URLClassLoader;
import java.net.URL;

public class GeneralURLClassLoader extends URLClassLoader {

	
	public  GeneralURLClassLoader(){
		
		super(new URL[0]);
		
	}

	public void addURL(URL arg0) {
		// TODO Auto-generated method stub
		super.addURL(arg0);
	}
	
	public void addURLs(URL[] urls) {
		
		if (urls ==null)
			return;
		
		for(int i = 0 ; i < urls.length; i ++ ){
			
			addURL(urls[i]);			
		}
		
	}
	
	
	
	
}
