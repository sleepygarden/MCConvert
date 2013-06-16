package marx;

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
import javax.swing.JTextPane;
import javax.swing.JWindow;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class MarxdownGUI {

	public final static String newline = "\n";
	public final static String tab = "\t";
	public final static String doublequote = "\"";
	public final static String singlequote = "\'";
	public final static String backslash = "\\";
	public final static String openparen = "(";
	public final static String closeparen = ")";
	public final static String nullparen = "()";
	public final static String opencurlybrace = "{";
	public final static String closecurlybrace = "}";
	public final static String nullcurlybrace = "{}";
	public final static String isNotEscaped = "[^\\\\]"; // TODO find instances
															// of escapes in
															// regex section

	private JFrame frame;
	private JWindow popUp;
	private String button = "";

	private Font monospaced;

	private static JTextPane input;
	private static JTextPane output;

	StyledDocument doc;

	public static final String[] keywords = { "keyword", "mikecornell",
			"schema", "namespace", "xml", "json", "test", "new" };
	public static final String[] symbols = { ":", "=", "\\(", "\\)", "\\{",
			"\\}" };

	public MarxdownGUI() {
		init();
	}

	// INIT
	public void init() {

		monospaced = new Font(Font.MONOSPACED, 0, 12);
		InputListener inputListener = new InputListener();
		ButtonListener buttonListener = new ButtonListener();

		frame = new JFrame();
		frame.setBounds(0, 0, 810, 620);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Marx, the People's XML Authoring tool");
		frame.getContentPane().setLayout(null);

		JLabel inLbl = new JLabel("INPUT");
		inLbl.setBounds(165, 5, 50, 15);
		frame.getContentPane().add(inLbl);

		JLabel outLbl = new JLabel("OUTPUT");
		outLbl.setBounds(600, 5, 55, 15);
		frame.getContentPane().add(outLbl);

		input = new JTextPane();
		input.setFont(monospaced); // make monospaced the constant pane wide
									// font
		input.addKeyListener(inputListener);
		doc = input.getStyledDocument();
		JScrollPane inScrollPane = new JScrollPane(input);
		inScrollPane.setBounds(5, 25, 350, 570);
		frame.getContentPane().add(inScrollPane);

		output = new JTextPane();
		output.setFont(monospaced);
		output.setEditable(false);
		JScrollPane errScrollPane = new JScrollPane(output);
		errScrollPane.setBounds(455, 25, 350, 570);
		frame.getContentPane().add(errScrollPane);

		JButton btnJSON = new JButton("JSON");
		btnJSON.setBounds(355, 25, 100, 25);
		btnJSON.setActionCommand("json_convert");
		btnJSON.addActionListener(buttonListener);
		frame.getContentPane().add(btnJSON);

		JButton btnXML = new JButton("XML");
		btnXML.setBounds(355, 55, 100, 25);
		btnXML.setActionCommand("xml_convert");
		btnXML.addActionListener(buttonListener);
		frame.getContentPane().add(btnXML);

		JButton btnValidate = new JButton("Validate");
		btnValidate.setBounds(355, 85, 100, 25);
		btnValidate.setActionCommand("validate");
		btnValidate.addActionListener(buttonListener);
		frame.getContentPane().add(btnValidate);

		JButton btnCheatSheet = new JButton("Cheat Sheet");
		btnCheatSheet.setBounds(355, 540, 100, 25);
		btnCheatSheet.setActionCommand("pop_cheat");
		btnCheatSheet.addActionListener(buttonListener);
		frame.getContentPane().add(btnCheatSheet);

		JButton btnReset = new JButton("Reset");
		btnReset.setBounds(355, 570, 100, 25);
		btnReset.setActionCommand("reset");
		btnReset.addActionListener(buttonListener);
		frame.getContentPane().add(btnReset);

		initStyles();
	}

	private void initStyles() { // TODO implement color settings menu

		Style style = input.addStyle("Keyword", null);
		StyleConstants.setForeground(style, new Color(80, 0, 80));
		StyleConstants.setBold(style, true);
		StyleConstants.setItalic(style, false);
		StyleConstants.setFontSize(style, 12);

		style = input.addStyle("Symbol", null);
		StyleConstants.setForeground(style, new Color(225, 0, 0));
		StyleConstants.setBold(style, false);
		StyleConstants.setItalic(style, false);
		StyleConstants.setFontSize(style, 12);

		style = input.addStyle("Number", null);
		StyleConstants.setForeground(style, new Color(0, 0, 180));
		StyleConstants.setBold(style, false);
		StyleConstants.setItalic(style, false);
		StyleConstants.setFontSize(style, 12);

		style = input.addStyle("Normal", null);
		StyleConstants.setForeground(style, new Color(0, 0, 0));
		StyleConstants.setBold(style, false);
		StyleConstants.setItalic(style, false);
		StyleConstants.setFontSize(style, 12);

		style = input.addStyle("Comment", null);
		StyleConstants.setForeground(style, new Color(170, 170, 170));
		StyleConstants.setBold(style, false);
		StyleConstants.setItalic(style, true);
		StyleConstants.setFontSize(style, 12);

		style = input.addStyle("String", null);
		StyleConstants.setForeground(style, new Color(0, 175, 0));
		StyleConstants.setBold(style, false);
		StyleConstants.setItalic(style, false);
		StyleConstants.setFontSize(style, 12);

		style = input.addStyle("Tab", null);
		StyleConstants.setForeground(style, new Color(0, 0, 0));
		StyleConstants.setBackground(style, new Color(237, 237, 237));
		StyleConstants.setBold(style, false);
		StyleConstants.setItalic(style, false);
		StyleConstants.setFontSize(style, 12);

		style = input.addStyle("Monospaced", null);
		StyleConstants.setFontFamily(style, Font.MONOSPACED);
	}

	// END INIT

	// STYLE SETTING
	public void makeKeystyle(int begin, int end) {
		doc.setCharacterAttributes(begin, end - begin,
				input.getStyle("Keyword"), true);
	}

	public void makeSymbolstyle(int begin, int end) {
		doc.setCharacterAttributes(begin, end - begin,
				input.getStyle("Symbol"), true);
	}

	public void makeNumberstyle(int begin, int end) {
		doc.setCharacterAttributes(begin, end - begin,
				input.getStyle("Number"), true);
	}

	public void makeCommentstyle(int begin, int end) {
		doc.setCharacterAttributes(begin, end - begin,
				input.getStyle("Comment"), true);
	}

	public void makeStringstyle(int begin, int end) {
		doc.setCharacterAttributes(begin, end - begin,
				input.getStyle("String"), true);
	}

	public void makeTabstyle(int begin, int end) {
		doc.setCharacterAttributes(begin, end - begin, input.getStyle("Tab"),
				true);
	}
	
	public void refreshInputStyles() { // refresh text formatting

		if (input.getText() != null) {
			doc.setCharacterAttributes(0, input.getText().length(),
					input.getStyle("Normal"), true);

			List<IndexWrapper> list;

			String regex = "";

			// SEARCH KEYWORDS
			for (String str : keywords) {
				regex = regex + "|" + "\\b" + str + "\\b"; // any keyword
																				
			}
			regex = regex.substring(1); // cuts off the first OR statement
			System.out.println("REGEX is "+regex);

			list = findIndexesWithRegex(regex,""+input.getText()); //handle begin edge case
			if (list != null) {
				for (IndexWrapper wrapper : list) {
					System.out.println("wrapper "+wrapper.getStart());
					makeKeystyle(wrapper.getStart(), wrapper.getEnd());
				}
			}

			// SEACH SYMBOLS
			regex = "";
			for (String str : symbols) {
				regex = regex + "|" + isNotEscaped + str; // TODO doesn't work
															// right!
			}
			regex = regex.substring(1);
			list = findIndexesWithRegex(regex,input.getText());
			if (list != null) {
				for (IndexWrapper wrapper : list) {
					makeSymbolstyle(wrapper.getStart(), wrapper.getEnd());
				}
			}
			
			// SEARCH NUMS
			regex = "[\\d]+"; // 1 or more digits (0-9)
			list = findIndexesWithRegex(regex, input.getText());
			if (list != null) {
				for (IndexWrapper wrapper : list) {
					makeNumberstyle(wrapper.getStart(), wrapper.getEnd());
				}
			}

			// SEARCH STRINGS
			regex = "\"[\\p{Print}]*\""; // any collection surrounded by "s
			list = findIndexesWithRegex(regex,input.getText());
			if (list != null) {
				for (IndexWrapper wrapper : list) {
					makeStringstyle(wrapper.getStart(), wrapper.getEnd());
				}
			}

			// SEARCH COMMENTS
			regex = "//[\\p{Print}]*\n"; // any collection preceded by // and
											// followed by a newline
			list = findIndexesWithRegex(regex,input.getText()+"\n"); //handle end edge case
			if (list != null) {
				for (IndexWrapper wrapper : list) {
					makeCommentstyle(wrapper.getStart(), wrapper.getEnd());
				}
			}

			// SEARCH TABS
			regex = "[\\t]+"; // 1 or more tabs
			list = findIndexesWithRegex(regex,input.getText());
			if (list != null) {
				for (IndexWrapper wrapper : list) {
					makeTabstyle(wrapper.getStart(), wrapper.getEnd());
				}
			}

			/*
			 * String regex = "()"; //any instance of () 
			 * 
			 * String regex =
			 * "([\\p{Graph}]+:[\\p{Graph}]+)"; //any visible collection with a
			 * :, // with at least one character before //and after the :,
			 * contained within () 
			 * 
			 * String regex = "([\\p{Graph}]+&&[^:])"; //any
			 * visible collection within a () which has no : 
			 * 
			 * String regex =
			 * "{[\\p{Print}]*}"; //any collection contained within {}
			 */

		}
	}

	// END STYLING

	// PUBLIC SETTERS AND GETTERS

	public void setVersion(String v) {
		frame.setTitle(frame.getTitle() + " v:" + v);
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

	public void setInput(String message) {
		input.setText(message + newline);
	}

	public void setOutput(String message) {
		output.setText(message + newline);
	}

	public void appendInput(String message) {
		input.setText(input.getText() + message + newline);
	}

	public void appendOutput(String message) {
		output.setText(output.getText() + message + newline);
	}

	public void clean() {
		input.setText(null);
		output.setText(null);
	}

	// END SETTERS GETTERS

	// POP/ERROR HANDLING

	public void popCheatSheet() {
		popUp = new JWindow();
		popUp.setBounds(0, 0, 400, 300);
		popUp.setLocationRelativeTo(null);
		popUp.setName("Cheat Sheet");
		popUp.getContentPane().setLayout(null);

		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("Marx Keyword");
		model.addColumn("Function");

		model.addRow(new Object[] { "Keyword", "Keyword Definition" });
		model.addRow(new Object[] { "Symbol", "Symbol Definition" });

		JTable params = new JTable(model);
		params.getTableHeader().setReorderingAllowed(false);
		params.getTableHeader().setResizingAllowed(false);
		params.setCellSelectionEnabled(false);
		JScrollPane paramsScrollPane = new JScrollPane(params);
		paramsScrollPane.setBounds(5, 5, 390, 262);
		popUp.getContentPane().add(paramsScrollPane);

		JButton btnPopOk = new JButton("OK");
		btnPopOk.setBounds(300, 270, 100, 25);
		btnPopOk.setActionCommand("pop_cheat_ok");
		btnPopOk.addActionListener(new ButtonListener());
		popUp.getContentPane().add(btnPopOk);

		popUp.setVisible(true);

	}

	public void dismissCheatSheet() {
		popUp.setVisible(false);
		popUp.dispose();
	}

	public void popAlert(String notif, String title) {
		if (title == null)
			title = "Hey! Listen!";
		if (notif == null)
			notif = "Hey! Listen!";
		JOptionPane.showMessageDialog(frame, notif, title,
				JOptionPane.PLAIN_MESSAGE);
	}

	public void popError(int code) {
		logError(code, 0);
		popAlert("ERROR " + code, "ERROR");
	}

	public void popError(int code, int index) {
		logError(code, index);
		popAlert("ERROR " + code, getErrorCodeMessage(code, index));
	}

	public void logError(int code, int index) {
		appendOutput(getErrorCodeMessage(code, index));
	}

	public String getErrorCodeMessage(int code, int index) { // TODO update
																// these to
																// relevant
																// messages
		String message = "";
		switch (code) {

		case (1):
			message = "No text entered";
			break;
		case (100):
			message = "XML is malformed at index:" + index + ", unclosed tag";
			break;
		case (101):
			message = "XML is malformed at index:" + index + ", unopened tag";
			break;
		case (102):
			message = "XML is malformed at index:" + index
					+ ", missing closing tag";
			break;
		case (103):
			message = "XML is malformed at index:" + index
					+ ", missing opening tag";
			break;
		case (104):
			message = "XML is malformed at index:" + index
					+ ", extra comment opener";
			break;
		case (105):
			message = "XML is malformed at index:" + index
					+ ", extra comment closer";
			break;
		case (106):
			message = "XML is malformed at index:" + index + ", hanging quote";
			break;
		case (200):
			message = "XML doesn't conform to validation params at index:"
					+ index;
			break;
		case (201):
			message = "XML tagnames are mismatched at index:" + index;
			break;
		case (300):
			message = "You submitted something that isn't XML";
			break;
		default:
			message = "An unknown error occured";
			break;
		}
		return message;
	}

	// END POP

	// REGEX
	public static List<IndexWrapper> findIndexesWithRegex(String regex, String in) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(in);
		List<IndexWrapper> wrappers = new ArrayList<IndexWrapper>();

		while (matcher.find() == true) {
			int end = matcher.end();
			int start = matcher.start();
			IndexWrapper wrapper = new IndexWrapper(start, end);
			wrappers.add(wrapper);
		}
		return wrappers;
	}
	// END REGEX

	// LISTENERS
	public class ButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			button = e.getActionCommand();
		}
	}

	public class InputListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
		}

		@Override
		public void keyReleased(KeyEvent e) {
			refreshInputStyles();
		}

		@Override
		public void keyTyped(KeyEvent e) {
			refreshInputStyles();
		}

	}
	// END LISTENERS

}
