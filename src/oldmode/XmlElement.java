package oldmode;

import java.util.Map;
import java.util.Stack;

public class XmlElement {
	String tag;
	String content;
	Map<Integer,XmlElement> children;

	public XmlElement(String body){
		String temp;
		Stack<String> stack = new Stack<String>();

		this.tag = getNextTag(body);
		//stack.push(temp);
		
		body = body.substring(body.indexOf(this.tag)+this.tag.length()+1);
		
	}
	
	public XmlElement(String tag, String content){
		
	}
	
	private String getNextTag(String body){
		body = body.substring(body.indexOf("<")+1,body.indexOf(">"));
		System.out.println(body);
		if (body.contains("=")){ //xml tag has attributes
			String tag = body.substring(0, body.indexOf(" "));
			body = body.substring(body.indexOf(tag));
			String attTag = body.substring(body.indexOf(" ")+1,body.indexOf("="));
			body = body.substring(body.indexOf(attTag));

			String attVal = body.substring(body.indexOf("\"")+1,body.indexOf("="));
			body = body.substring(body.indexOf(attTag));
			System.out.println("TAG("+ tag+")");

			System.out.println("attTAG("+ attTag+")");


		}
		return body;
		
	}
	
}
