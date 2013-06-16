package marx;

import java.awt.EventQueue;


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
				//Thread.sleep(125); //8 frames per second
				//Thread.sleep(100); //10 frames per second
				//Thread.sleep(62); //16 frames per second
				Thread.sleep(62); //approx 16 frames per second
				//Thread.sleep(45); //approx 24 frames per second
				//Thread.sleep(31); //approx 32 frames per second



					
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
				
	}

	public static void convertToJSON()
	{
		
	}
	
	public static void convertToXML()
	{
		
	}
	
	public static void log(String message){
		System.out.println(message);
	}
	
}
