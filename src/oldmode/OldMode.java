package oldmode;

import java.awt.EventQueue;
import java.util.Scanner;

public class OldMode {
	static ConverterGUI window;
	static String content;
	static String newline = "\n";
	
public static void main(String[] args){
		
		window = new ConverterGUI();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {	
					window.getFrame().setVisible(true);
					window.setVersion("alpha 1.1");
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
				parse();
				window.resetButtonKey();
			}
			
			if (window.getButtonKey().equals("reset")){
				window.clean();
				window.resetButtonKey();
			}
			
			if (window.getButtonKey().equals("set_params")){
				window.appendOutput("Not in old mode!");
				window.resetButtonKey();
			}
			
			if (window.getButtonKey().equals("validate")){
				window.appendOutput("Not in old mode!");
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
	Scanner scan = new Scanner(content);
	while (scan.hasNextLine())
	{
		String a = scan.nextLine();
		if (!scan.hasNextLine()){
			temp = temp + a;
			break;
		}
		String b = scan.nextLine();
		temp = temp + b + '\n' + a;
		if (scan.hasNextLine()){
			temp = temp + '\n';
		}
	}
	window.setInput(temp);
	window.appendOutput("Swapping every other line...");
	content = temp;
}

public static void parse(){
	if (content != null 
		&& content.contains("<string>")
		&& content.contains("</string>")
		&& content.contains(",")
		&& content.contains("<key>")
		&& content.contains("</key>"))
	{	
		
	content = content.replace("&amp;","&");
	content = content.replace("&apos;","\'");
	content = content.replace("<string>","{"+newline+"\"ids\":[\"");
	content = content.replace("</string>","\"]");
	content = content.replace("," , "\",\"");
	
	content = content.replace("<key>",",\"name\":\"");
	content = content.replace("</key>","\""+newline+"},"+newline);

	int begin = content.indexOf("{");
	int end = content.lastIndexOf(",");
	content = content.substring(begin,end);
	content = "[" + newline + content + newline + "]";
	content.trim();
	window.setOutput(content);

	}
	else window.appendOutput("Invalid input!");
}
	
}
