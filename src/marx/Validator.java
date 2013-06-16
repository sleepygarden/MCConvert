package marx;

public class Validator {

	
	public static void validate(String input) {
		
		for (char c : input.toCharArray()) {
			switch(c) {
			case('{'):
				break;
			case('}'):
				break;
			case('('):
				break;
			case(')'):
				break;
			case(':'):
				break;
			case('='):
				break;
			default:
				break;
			}
		}
	}
	
	

}
