package mcornell;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarxDown {

	static MarxdownGUI window;
	static String content;
	
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
			
			if (window.getButtonKey().equals("swap")) {
				window.appendOutput("Not in old mode!");

				window.resetButtonKey();
			}
			
			if (window.getButtonKey().equals("submit")) {
				window.resetButtonKey();
			}
			
			if (window.getButtonKey().equals("reset")){
				window.clean();
				content=null;
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
			
			if (content != null)
			{
				List<IndexWrapper> list = findIndexesForKeyword("keyword");
				if (list != null)
				{
					for (IndexWrapper wrapper : list){
						window.makeKeystyle(wrapper.getStart(), wrapper.getEnd());

					}
				}
			}
	
			window.getFrame().repaint();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
				
	}

	public static List<IndexWrapper> findIndexesForKeyword(String keyword) 
	{
    String regex = "\\b"+keyword+"\\b";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(content);
    List<IndexWrapper> wrappers = new ArrayList<IndexWrapper>();

    while(matcher.find() == true){
        int end = matcher.end();
        int start = matcher.start();
        IndexWrapper wrapper = new IndexWrapper(start, end);
        wrappers.add(wrapper);
    }
    return wrappers;

}

	public static void convert(){
		
	}
	
}
