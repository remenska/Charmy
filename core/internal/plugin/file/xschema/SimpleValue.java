package core.internal.plugin.file.xschema;

public class SimpleValue {
	
	private String value=null;
	
	public SimpleValue(String value) {
		this.value=value;
	}
	public SimpleValue(boolean value) {
		this.value=value ? "true" : "false";
	}
	public SimpleValue(char value) {
		this.value=""+value;
	}
	public SimpleValue(byte value) {
		this.value=""+value;
	}
	public SimpleValue(short value) {
		this.value=""+value;
	}
	public SimpleValue(int value) {
		this.value=""+value;
	}
	public SimpleValue(long value) {
		this.value=""+value;
	}
	public SimpleValue(float value) {
		this.value=""+value;
	}
	
	public SimpleValue(double value) {
		this.value=""+value;
	}
	public String getValue() {
		return value;
	}
	
}
