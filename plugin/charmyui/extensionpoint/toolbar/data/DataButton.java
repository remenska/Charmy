package plugin.charmyui.extensionpoint.toolbar.data;

import javax.swing.AbstractButton;
import javax.swing.border.Border;

public class DataButton {

	
	private String idButton=null;
	private AbstractButton reference=null;
	private Border releaseButtonBorder=null;
	private boolean pressed = false;
	
	public DataButton(AbstractButton reference) {

		this.reference=reference;
	}

	public String getIdButton() {
		return idButton;
	}

	public void setIdButton(String idButton) {
		this.idButton = idButton;
	}

	public AbstractButton getReference() {
		return reference;
	}

	
	public Border getReleaseButtonBorder() {
		return releaseButtonBorder;
	}

	public void setReleaseButtonBorder(Border releaseButtonBorder) {
		this.releaseButtonBorder = releaseButtonBorder;
	}

	public boolean isPressed() {
		return pressed;
	}

	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}

}
