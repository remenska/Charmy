package plugin.EsempioPlugin1.data;
import core.internal.runtime.data.IPlugData;
import core.internal.runtime.data.PlugDataManager;
public class PlugData implements  IPlugData{

	
	PlugDataManager plugDataManager;
	
	private String informazione1=null;
	private String informazione2=null;
	
	public PlugData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void activateListeners() {
		// TODO Auto-generated method stub
		
	}

	public void clearAll() {
		// TODO Auto-generated method stub
		
	}

	public PlugDataManager getPlugDataManager() {
		// TODO Auto-generated method stub
		return this.plugDataManager;
	}

	public void setPlugDataManager(PlugDataManager pdm) {
		this.plugDataManager=pdm;
		
	}

	public String getInformazione1() {
		return informazione1;
	}

	public void setInformazione1(String informazione1) {
		this.informazione1 = informazione1;
	}

	public String getInformazione2() {
		return informazione2;
	}

	public void setInformazione2(String informazione2) {
		this.informazione2 = informazione2;
	}

}
