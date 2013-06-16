package oldmode;

public class XmlValidationTask {
	private String tagA;
	private String tagB;

	private int comparator;

	private boolean didPass;

	public XmlValidationTask(String a, int comp, String b){
		this.didPass = false;
		this.tagA = a;
		this.tagB = b;
		
		switch (comp){
		case(1):
			break;
		case(2):
			break;
		case(3):
			break;
		case(4):
			break;
		case(5):
			break;
		default:
			break;
		}
	}
	
	public String getTagA(){
		return this.tagA;
	}
	public String getTagB(){
		return this.tagB;
	}
	
	public int getComparator(){
		return this.comparator;
	}
	
	public boolean didPass(){
		return this.didPass;
	}
}