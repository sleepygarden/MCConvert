package mcornell;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

public class ConverterGUI implements ActionListener{
	
	public final static String newline = "\n";

	private JFrame frame;
	private JFrame popFrame;
	private String button = "";
	
	private JTextArea input;
	JTextArea output;

	public ConverterGUI(){
		init();
	}

	
	public void init(){
		
		frame = new JFrame();
		frame.setBounds(0, 0, 810, 620);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("MC Convert");
		frame.getContentPane().setLayout(null);
		
		JLabel inLbl = new JLabel("INPUT");
		inLbl.setBounds(165,5, 50, 15);
		frame.getContentPane().add(inLbl);
		
		JLabel outLbl = new JLabel("OUTPUT");
		outLbl.setBounds(600,5, 55, 15);
		frame.getContentPane().add(outLbl);

		input = new JTextArea();
		input.setLineWrap(true);
		JScrollPane areaScrollPane = new JScrollPane(input);
		areaScrollPane.setBounds(5, 25, 350, 570);
		frame.getContentPane().add(areaScrollPane);
		
		output = new JTextArea();
		output.setLineWrap(true);
		output.setEditable(false);
		JScrollPane errScrollPane = new JScrollPane(output);
		errScrollPane.setBounds(455, 25, 350, 570);
		frame.getContentPane().add(errScrollPane);
		
		JButton btnSwap = new JButton("Swap");
		btnSwap.setBounds(355, 25, 100, 25);
	    btnSwap.setActionCommand("swap");
	    btnSwap.addActionListener(this);
		frame.getContentPane().add(btnSwap);
			
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.setBounds(355, 55, 100, 25);
	    btnSubmit.setActionCommand("submit");
	    btnSubmit.addActionListener(this);
		frame.getContentPane().add(btnSubmit);
 
		JButton btnValidate = new JButton("Validate");
		btnValidate.setBounds(355, 85, 100, 25);
	    btnValidate.setActionCommand("validate");
	    btnValidate.addActionListener(this);
		frame.getContentPane().add(btnValidate);
		
		JButton btnSetParams = new JButton("Set Params");
		btnSetParams.setBounds(355, 540, 100, 25);
		btnSetParams.setActionCommand("set_params");
		btnSetParams.addActionListener(this);
		frame.getContentPane().add(btnSetParams);
			
	    JButton btnReset = new JButton("Reset");
		btnReset.setBounds(355, 570, 100, 25);
	    btnReset.setActionCommand("reset");
	    btnReset.addActionListener(this);
		frame.getContentPane().add(btnReset);
		

	}
	
	public Window getFrame() {
		return frame;
	}
	
	public JTextArea getInput() {
		return input;
	}
	
	public String getButtonKey() {
		return button;
	}
	
	public void resetButtonKey() {
		button = "";
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
			output.append(message+newline);

	}
	
	public void setInput(String message){
		input.setText(message+newline);
	}
	public void setOutput(String message){
		output.setText(message+newline);
	}
	
	public void appendInput(String message){
		input.append(message+newline);
	}
	
	public void appendOutput(String message){
		output.append(message+newline);
	}
	
	public void clean(){
		input.setText(null);
		output.setText(null);
	}
	
	public void popSetParams(){
		
		popFrame = new JFrame();
		popFrame.setBounds(0, 0, 400, 300);
		popFrame.setLocationRelativeTo(null);
		popFrame.setTitle("Set Params");
		popFrame.getContentPane().setLayout(null);
		
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("");
		model.addColumn("");
		model.addColumn("");
		model.addRow(new Object[]{"v1", "v2"});
		model.addRow(new Object[]{"v1"});
		model.addRow(new Object[]{"v1", "v2", "v3"});


		JTable params = new JTable(model);
		JScrollPane paramsScrollPane = new JScrollPane(params);
		paramsScrollPane.setBounds(5, 5, 390, 200);
		popFrame.getContentPane().add(paramsScrollPane);
		
		JButton btnPopRemove = new JButton("Remove");
		btnPopRemove.setBounds(95, 250, 100, 25);
		btnPopRemove.setActionCommand("pop_param_remove");
		btnPopRemove.addActionListener(this);
		popFrame.getContentPane().add(btnPopRemove);
		
		JButton btnPopAdd = new JButton("Add");
		btnPopAdd.setBounds(195, 250, 100, 25);
		btnPopAdd.setActionCommand("pop_param_add");
		btnPopAdd.addActionListener(this);
		popFrame.getContentPane().add(btnPopAdd);
	
		JButton btnPopOk = new JButton("OK");
		btnPopOk.setBounds(295, 250, 100, 25);
		btnPopOk.setActionCommand("pop_param_ok");
		btnPopOk.addActionListener(this);
		popFrame.getContentPane().add(btnPopOk);	
		
		popFrame.setVisible(true);
		
	}
	public void dismissSetParams(){
		//popFrame.dismiss();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		   button = e.getActionCommand();     		
	}
	
	public void setVersion(String v){
		frame.setTitle(frame.getTitle()+" "+v);
	}
}
