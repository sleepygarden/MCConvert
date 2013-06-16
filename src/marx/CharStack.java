package marx;

import java.util.Stack;

public class CharStack {

		  private Stack<Character> theStack;

		  public CharStack() {
		    theStack = new Stack<Character>();
		  }
		  
		  public CharStack(String input) {
			    theStack = new Stack<Character>();
			    for (char c : input.toCharArray()) {
			    	push(c);
			    }
			  }

		  public char peek() {
		    Character temp = (Character) theStack.peek();
		    return temp.charValue();
		  }

		  public void push(char c) {
		    theStack.push(new Character(c));
		  }

		  public char pop() {
		    Character temp = (Character) theStack.pop();
		    return temp.charValue();
		  }
		  
		  public int size() {
			  return theStack.size();
		  }
		  
		  public Stack<Character> getStack() {
			  return theStack;
		  }
		  
		  public boolean empty() {  
		    return theStack.empty();
		  }

		

}
