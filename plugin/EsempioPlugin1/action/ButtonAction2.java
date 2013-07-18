package plugin.EsempioPlugin1.action;

import java.awt.event.ActionEvent;

import plugin.charmyui.extensionpoint.toolbar.ExtensionPointToolbar;
import plugin.charmyui.extensionpoint.toolbar.action.ButtonActionListener;

public class ButtonAction2 extends ButtonActionListener {

	public ButtonAction2() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	public void actionPerformedEvent(ActionEvent arg0) {
		
		((ExtensionPointToolbar)this.toolbarOwner.getExtensionPointOwner()).getEditorAssociated(this.toolbarOwner).notifyMessage(this,2,"Valore dato2");
		
	}

}
