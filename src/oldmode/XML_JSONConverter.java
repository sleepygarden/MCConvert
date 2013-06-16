package oldmode;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class XML_JSONConverter {
	
	public final static String newline = "\n";
	
	public static int NO_PARAM = 0;
	
	public static int MUST_CONTAIN_ = 1; //must contain
	public static int MUST_NOT_CONTAIN = 2; //must not contain
	
	public static int DOES_CONTAIN_ = 3; //does <tag> appear
	public static int DOES_NOT_CONTAIN = 4; //does <tag> not appear
	
	public static int EVERY = 5; //every instance of <tag>
	public static String DOCUMENT = "document"; //all input text
	
	static Scanner scan; 
	static String content;
	static String parsedContent;
	static ConverterGUI window;
	List<XmlValidationTask> tasks= new ArrayList<XmlValidationTask>();

	List<String> acceptedTags= new ArrayList<String>();
	List<String> foundTags= new ArrayList<String>();

	
	
	public static void main(String[] args){
		
		window = new ConverterGUI();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {	
					window.getFrame().setVisible(true);
					window.setVersion("alpha 2.0");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		while(true)
		{
			content = window.getInput().getText();
			
			if (window.getButtonKey().equals("swap")) {
				swapEveryOtherLine();	
				window.resetButtonKey();
			}
			
			if (window.getButtonKey().equals("convert")) {
				if (validate()){
					parse(content);
					content = null;
				}
				window.resetButtonKey();
			}
			
			if (window.getButtonKey().equals("reset")){
				window.clean();
				content=null;
				window.resetButtonKey();
			}
			
			if (window.getButtonKey().equals("set_params")){
				setParams();
				window.resetButtonKey();
			}
			
			if (window.getButtonKey().equals("validate")){
				validate();
				window.resetButtonKey();
			}
			
			window.getFrame().repaint();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
				
	}
	
	public static void swapEveryOtherLine(){
		String temp = "";
		scan = new Scanner(content);
		while (scan.hasNextLine())
		{
			String a = scan.nextLine();
			if (!scan.hasNextLine()){
				temp = temp + a;
				break;
			}
			String b = scan.nextLine();
			temp = temp + b + newline + a;
			if (scan.hasNextLine()){
				temp = temp + newline;
			}
		}
		window.clean();
		window.setInput(temp);
		content = temp;
	}
	
	public static String parseItem(String body){
		
		for (String child : getChildren(body)){
			parseItem(child);
		}
		
		//parse the parent too
		
		String sub;
		String tagname;
		String opentag;
		String closetag;
		
		int begin = body.indexOf("<");
		int end = body.indexOf(">");
		sub = body.substring(begin,end);
		
		tagname = sub.substring(1,sub.length());
		System.out.println(tagname);

		opentag = "<"+tagname+">";
		System.out.println(opentag);

		closetag = "</" + tagname + ">";
		System.out.println(closetag);

		sub = body.substring(body.indexOf(opentag)+opentag.length(),body.indexOf(closetag));
		System.out.println(sub);
		
		if (sub.contains("<")){ //has children
			for (String child : getChildren(sub)){
				
			}
		}
		
		
		
		return body;
				
//		while (content.contains("</"))
//		{
//			sub = content.substring(content.indexOf("</"));
//			sub = sub.substring(0,sub.indexOf(">")+1);
//			tag = sub.replace("</","");
//			tag = tag.replace(">","");
//			tag = "\"";
//			System.out.println(sub);
//			System.out.println(tag);
//
//			content = content.replace(sub,tag);
//		}
//		
//		while (content.contains("<"))
//		{
//			sub = content.substring(content.indexOf("<"),content.indexOf(">")+1);
//			tag = sub.substring(1,sub.indexOf(">"));
//			tag = "\""+tag+"\":\"";
//			content = content.replace(sub,tag);
//		}
//
//		//System.out.println(sub);
//		//content = content.replace("<string>","{\"ids\":[\"");
//		//content = content.replace("</string>","\"]");
//		//content = content.replace("," , "\",\"");
//		
//		//content = content.replace("<key>",",\"name\":\"");
//		//content = content.replace("</key>","\"},\n");
//
//		//int begin = content.indexOf("{");
//		//int end = content.lastIndexOf(",");
//		//content = content.substring(begin,end);
//		content = "[" + newline + content + newline + "]";
//		content.trim();
//			
//		//window.clean();
//		window.setOutput(content);
//		
//		content = null;
			
					
		//ORIGINAL CODE
//		//content = content.replace("<string>","{\"ids\":[\"");
//		//content = content.replace("</string>","\"]");
//		//content = content.replace("," , "\",\"");
//		
//		//content = content.replace("<key>",",\"name\":\"");
//		//content = content.replace("</key>","\"},\n");
//
//		//int begin = content.indexOf("{");
//		//int end = content.lastIndexOf(",");
//		//content = content.substring(begin,end);
//		content = "[" + newline + content + newline + "]";
//		content.trim();
	}
	
	public static String[] getChildren (String body){
		
		Stack<String> tagstack = new Stack<String>();
		String[] children = {};
		
		while (body.contains("<"))
		{
			int begin = body.indexOf("<");
			int end = body.indexOf(">");
		
			String sub = body.substring(begin,end);

			if (!tagstack.isEmpty())
			{
				if (sub.equals(tagstack.peek()))
				{
					tagstack.pop();
				}
				else {
					sub = sub.replace("<","</");
					tagstack.push(sub);
				}
			}
		
			else {
				sub = sub.replace("<","</");
				tagstack.push(sub);
			}
		}
		
		if (children != null) return children;
		else return null;
		
		
	
	}
	
	public static String prepForParse(String body){
		String sub;
		
		while (body.contains("<!--") && body.contains("-->")){
			int begin = body.indexOf("<!--");
			int end = body.indexOf("-->")+3;
			sub = body.substring(begin,end);
			body = removeNext(sub,body);
		}
		
		while (body.contains("<!--")){
			window.logError(104, body.indexOf("<!--"));
			body = removeNext("<!--", body);
		}
		
		while (body.contains("-->")){	
			window.logError(105, body.indexOf("-->"));
			body = removeNext("-->",body);			
		}
		
		
//		while (body.contains("'")) {
//			
//		}
		
		body = body.replace("&amp;","&");
		body = body.replace("&apos;","\'");
		//TODO other xml encoded special chars
		
		
		// append manditory parent xml tag for recursive parse
		body = "<xmlparserwrapper>" + body + "</xmlparserwrapper>";
		
		//TODO handle <!CDATA exceptions

		//TODO strip xml format/encoding tag here, handle weird text encoding?
		return body;
	}
	
	public static String postParse(String body){
		body = "[" + newline + body + newline + "]";
		body.trim();
		
		return body;
	}
	
	public static String parse(String body){
		//new XmlElement(body);
		body = prepForParse(body);
		body = parseItem(body); //recursively goes through the xml list
		body = postParse(body);
		
		parsedContent = body;
		return parsedContent;
	}
		
	public static boolean validate(){
		if(content.equals("") || window.getInput().getText().equals(""))	
		{
			window.logError(1, 0);
			return false;
		}
		if (!isXml()){
	        window.logError(300, 0);

			return false;
		}
		if (!isWellFormedXml()){
			return false;
		}
		if (!doesConform(0,0,0)){
			return false;
		}
		window.setOutput("Is Valid!");
		return true;
	}
	
	public static boolean isXml(){
		if (!content.contains("<"))
			return false;
		if (!content.contains("</"))
			return false;
		if (!content.contains(">"))
			return false;
		if (!content.contains("<?xml")) //all xml docs but start with a xml tag
			return false;
		if (content.contains("<xml")) //the tag <xml> is reserved
			return false;
		return true;
	}
	
	public static boolean isWellFormedXml(){
		boolean isValid = true;
		Stack<Integer> braces = new Stack<Integer>(); //locations at which there are < and >
		Stack<String> tags = new Stack<String>(); //tags
		Stack<Integer> tagsLoc = new Stack<Integer>(); //locations at which there is a complete open and closing tag set
		Stack<Integer> quotes = new Stack<Integer>();
		
		for (Integer x = 0; x < content.length(); x++)
		{
			String mini = content.substring(x,x+1);
			System.out.println(mini);
			
			if (mini.equals("\"")){ //found a quote
				if (quotes.isEmpty()) quotes.push(x);
				else quotes.pop();
			}
						
			else if (mini.equals("<"))
			{
				braces.push(x);
			}
			else if (mini.equals(">"))
			{
				if (braces.isEmpty())
				{ //if your stack is empty (you havent found any <'s yet) and you find a >
			        window.logError(101,x);
			        isValid=false;
				}
				else 
				{
					int tagStart = braces.pop();
					int tagEnd = x+1;
					String tag = content.substring(tagStart,tagEnd);
					//got a whole tag!
					
					
					if (tag.startsWith("</") && tags.isEmpty())
					{
						//found an extra closing tag
			        	window.logError(103,tagStart);
			        	isValid = false;
					}
					else if(tag.startsWith("</"))
					{
						//found a closing tag
						String openingTag = tags.pop();
						openingTag = openingTag.replace("<","");
						openingTag = openingTag.replace(">","");
						tag = tag.replace("</","");
						tag = tag.replace(">","");
						
						int tagIndex = tagsLoc.pop();
						if (!openingTag.equals(tag))
						{
							//open/close tag mismatch
							window.logError(201,tagIndex);
							isValid = false;
						}
						
					}
					else 
					{
						//found an opening tag
						tags.push(tag);
						tagsLoc.push(tagStart);
					}
				}
			}

			
		}
		if (!quotes.isEmpty())
		{ //hanging quotes found
			while (!quotes.isEmpty())
			{
				window.logError(106,quotes.pop());
			}
			isValid = false;

		}
		
		if (!tagsLoc.isEmpty())
		{//extra tags found	
			while (!tagsLoc.isEmpty())
			{
				window.logError(102,tagsLoc.pop());
			}
			isValid = false;
		}	
		
		if (!braces.isEmpty())
		{//extra <'s found
			while (!braces.isEmpty())
			{
				window.logError(100,braces.pop());
			}
			isValid = false;
		}
				
		return isValid;
	}
	
	public static boolean doesConform(int arg0, int arg1, int arg2){

		return true;
	}
	
	public static void setParams(){
		window.popSetParams();
	}
	
	public static String removeNext(String target, String body){
		System.out.println(target);
		System.out.println(body);
		String ret = body.substring(body.indexOf(target), body.indexOf(target) + target.length());
		ret = body.substring(0,body.indexOf(target));
		ret = ret + body.substring(body.indexOf(target) + target.length());
		System.out.println(ret);

		return ret;
		
	}
	
}
