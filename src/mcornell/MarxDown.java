package mcornell;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarxDown {

	static MarxdownGUI window;
	static String content;
	static String temp;
	
	
public static void main(String[] args){
		
		window = new MarxdownGUI();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {	
					window.getFrame().setVisible(true);
					window.setVersion("alpha 1.0");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		while(true)
		{
			content = window.getInput().getText();
			
			if (window.getButtonKey().equals("json_convert")) {
				window.appendOutput("Not yet!");
				window.resetButtonKey();
			}
			
			if (window.getButtonKey().equals("xml_convert")) {
				window.appendOutput("Not yet!");
				window.resetButtonKey();
			}
			
			if (window.getButtonKey().equals("reset")){
				window.clean();
				content=null;
				window.resetButtonKey();
			}
			
			if (window.getButtonKey().equals("validate")){
				window.appendOutput("Not yet!");
				window.resetButtonKey();
			}
			
			if (window.getButtonKey().equals("pop_cheat")){
				window.popCheatSheet();
				window.resetButtonKey();
			}
			
			if (window.getButtonKey().equals("pop_cheat_ok")){
				window.dismissCheatSheet();
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

	public static void convertToJson()
	{
		
	}
	
}
