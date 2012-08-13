package mcornell;

import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JWindow;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class MarxdownGUI implements ActionListener, KeyListener{
	
	public final static String newline = "\n";

	private JFrame frame;
	private JWindow popUp;
	private String button = "";
	
    private Font monospaced;

	
	private static JTextPane input;
	JTextArea output;
	
	StyledDocument doc;

	public static final String[] keywords = 
		{
			"keyword", 
			"mikecornell",
			"schema",
			"namespace",
			"xml",
			"json",
			"test"
		};
	
	public MarxdownGUI(){
		init();
	}

	
	public void init(){
		
		monospaced = new Font(Font.MONOSPACED, 0, 12);

		frame = new JFrame();
		frame.setBounds(0, 0, 810, 620);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Marx, the People's XML Authoring tool:");
		frame.getContentPane().setLayout(null);
		frame.addKeyListener(this); 

		
		JLabel inLbl = new JLabel("INPUT");
		inLbl.setBounds(165,5, 50, 15);
		frame.getContentPane().add(inLbl);
		
		JLabel outLbl = new JLabel("OUTPUT");
		outLbl.setBounds(600,5, 55, 15);
		frame.getContentPane().add(outLbl);
		
        input = new JTextPane();
        input.setFont(monospaced);
        input.addKeyListener(this);
        doc = input.getStyledDocument();
        JScrollPane inScrollPane = new JScrollPane(input);
        inScrollPane.setBounds(5,25,350,570);
        inScrollPane.addKeyListener(this);   
        frame.getContentPane().add(inScrollPane);
		
		output = new JTextArea();
		output.setLineWrap(true);
		output.setEditable(false);
		JScrollPane errScrollPane = new JScrollPane(output);
		errScrollPane.setBounds(455, 25, 350, 570);
		frame.getContentPane().add(errScrollPane);
		
		JButton btnJSON = new JButton("JSON");
		btnJSON.setBounds(355, 25, 100, 25);
	    btnJSON.setActionCommand("json_convert");
	    btnJSON.addActionListener(this);
		frame.getContentPane().add(btnJSON);
			
		JButton btnXML = new JButton("XML");
		btnXML.setBounds(355, 55, 100, 25);
	    btnXML.setActionCommand("xml_convert");
	    btnXML.addActionListener(this);
		frame.getContentPane().add(btnXML);
 
		JButton btnValidate = new JButton("Validate");
		btnValidate.setBounds(355, 85, 100, 25);
	    btnValidate.setActionCommand("validate");
	    btnValidate.addActionListener(this);
		frame.getContentPane().add(btnValidate);
		
		JButton btnCheatSheet = new JButton("Cheat Sheet");
		btnCheatSheet.setBounds(355, 540, 100, 25);
	    btnCheatSheet.setActionCommand("pop_cheat");
	    btnCheatSheet.addActionListener(this);
		frame.getContentPane().add(btnCheatSheet);
		
	    JButton btnReset = new JButton("Reset");
		btnReset.setBounds(355, 570, 100, 25);
	    btnReset.setActionCommand("reset");
	    btnReset.addActionListener(this);
		frame.getContentPane().add(btnReset);
		
	    initStyles();

	}
	
	private void initStyles(){
		Style style = input.addStyle("Keyword", null);
		StyleConstants.setForeground(style, new Color(70, 0, 70));
		StyleConstants.setBold(style,true);
		StyleConstants.setFontSize(style, 12);
		
		style = input.addStyle("Normal", null);
		StyleConstants.setForeground(style, new Color(0, 0, 0));
		StyleConstants.setBold(style,false);
		StyleConstants.setFontSize(style, 12);
		
		style = input.addStyle("Italic", null);
		StyleConstants.setItalic(style, true);
		
		style = input.addStyle("24pts", null);
		StyleConstants.setFontSize(style, 24);
		
		style = input.addStyle("Monospaced", null);
		StyleConstants.setFontFamily(style, Font.MONOSPACED);
		
	}
	
	public Window getFrame() {
		return frame;
	}
	
	public JEditorPane getInput() {
		return input;
	}
	
	public String getButtonKey() {
		return button;
	}
	
	public void resetButtonKey() {
		button = "";
	}
	
	public void makeKeystyle(int begin, int end){
        doc.setCharacterAttributes(begin, end-begin, input.getStyle("Keyword"), true);
	}
	public void refreshTextStyle(){		//refresh text formatting

		if (input.getText() !=null)
		{
			doc.setCharacterAttributes(0, input.getText().length(),input.getStyle("Normal"), true);
		
			List<IndexWrapper> list;			//highlighting keywords
			list = findIndexesForKeywords(keywords);
			if (list != null)
			{
				for (IndexWrapper wrapper : list)
				{
					makeKeystyle(wrapper.getStart(), wrapper.getEnd());
				}
			}
		}
	}
	
	public void popAlert(String notif, String title){
		if (title == null)
			title = "Hey! Listen!";
		if (notif == null)
			notif = "Hey! Listen!";
		JOptionPane.showMessageDialog(frame, notif, title, JOptionPane.PLAIN_MESSAGE);
	}
	
	public void popError(int code){
		logError(code, 0);
		popAlert("ERROR "+code, "ERROR");
	}
	
	public void popError(int code, int index){
		logError(code, index);
		popAlert("ERROR at index "+index, "ERROR");
	}

	public void logError(int code, int index){
			String message = "";
			switch (code){
						
			case(1):
				message = "No text entered";
				break;			
			case(100):
				message = "XML is malformed at index:"+index+", unclosed tag";
				break;
			case(101):
				message = "XML is malformed at index:"+index+", unopened tag";
				break;
			case(102):
				message = "XML is malformed at index:"+index+", missing closing tag";	
				break;
			case(103):
				message = "XML is malformed at index:"+index+", missing opening tag";
				break;
			case(104):
				message = "XML is malformed at index:"+index+", extra comment opener";
				break;
			case(105):
				message = "XML is malformed at index:"+index+", extra comment closer";	
				break;	
			case(106):
				message = "XML is malformed at index:"+index+", hanging quote";
				break;	
			case(200):
				message = "XML doesn't conform to validation params at index:"+index;
				break;
			case(201):
				message = "XML tagnames are mismatched at index:"+index;
				break;
			case(300):
				message = "You submitted something that isn't XML";
				break;	
			default: 
				message = "An unknown error occured";
				break;	        

		}
			appendOutput(message);
	}
	
	public void setInput(String message){
		input.setText(message+newline);
	}
	public void setOutput(String message){
		output.setText(message+newline);
	}
	
	public void appendInput(String message){
		input.setText(input.getText()+message+newline);
	}
	
	public void appendOutput(String message){
		output.setText(output.getText()+message+newline);
	}
	
	public void clean(){
		input.setText(null);
		output.setText(null);
	}
	
	public void popCheatSheet()
	{
		popUp = new JWindow();
		popUp.setBounds(0, 0, 400, 300);
		popUp.setLocationRelativeTo(null);
		popUp.setName("Cheat Sheet");
		popUp.getContentPane().setLayout(null);
		
		
		//TODO make this just a text view, not a table
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("");
		model.addColumn("");
		model.addRow(new Object[]{"Marx Symbol", "what it does"});
		model.addRow(new Object[]{"duh", "bluh"});
		model.addRow(new Object[]{"1", "2"});

		JTable params = new JTable(model);
		params.getTableHeader().setReorderingAllowed(false);
		params.getTableHeader().setResizingAllowed(false);
		params.setCellSelectionEnabled(false);
		JScrollPane paramsScrollPane = new JScrollPane(params);
		paramsScrollPane.setBounds(5, 5, 390, 262);
		popUp.getContentPane().add(paramsScrollPane);		
		//
		
		JButton btnPopOk = new JButton("OK");
		btnPopOk.setBounds(300, 270, 100, 25);
		btnPopOk.setActionCommand("pop_cheat_ok");
		btnPopOk.addActionListener(MarxdownGUI.this);
		popUp.getContentPane().add(btnPopOk);	
		
		popUp.setVisible(true);
		
	}
	public void dismissCheatSheet(){
		popUp.setVisible(false);
		popUp.dispose();
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		   button = e.getActionCommand();     		
	}
	
	public void setVersion(String v){
		frame.setTitle(frame.getTitle()+" "+v);
	}
	
	public static List<IndexWrapper> findIndexesForKeywords(String[] keywords)
	{
		String regex = "";
		for (String str:keywords){
			regex = regex +"|" + "\\b"+str+"\\b";
		}
		Pattern pattern = Pattern.compile(regex.substring(1));
		Matcher matcher = pattern.matcher(input.getText());
		List<IndexWrapper> wrappers = new ArrayList<IndexWrapper>();

		while(matcher.find() == true)
		{
			int end = matcher.end();
			int start = matcher.start();
			IndexWrapper wrapper = new IndexWrapper(start, end);
			wrappers.add(wrapper);
		}
		return wrappers;
	}


	//KEY LISTENER
	@Override
	public void keyPressed(KeyEvent arg0) {
	}


	@Override
	public void keyReleased(KeyEvent arg0) {
		refreshTextStyle();
	}


	@Override
	public void keyTyped(KeyEvent arg0) {		
	}
}
