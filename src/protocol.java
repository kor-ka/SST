

public class protocol
{
final static int ab =0;
final static int register =1;
final static int wat =2;
final static int click =3;
final static int dndDown=4;
final static int dndUp=5;
final static int rclick =6;
final static int keyboard=7;

public String output;
public String outputA;
public String outputB;
public int outputPort;
public String outputChar;
	
	public int processInput(String input){
		
		if(input.contains("ab:") && input.contains("lolParseMe")){
			
			int lolpm = input.indexOf("lolParseMe");
			outputA = input.substring(3, lolpm);
			outputB = input.substring(lolpm+10, input.length());
			return ab;
		} else if(input.contains("registerMe:") ){
			outputPort =Integer.parseInt( input.substring(11, input.length()));
			return register;
		} else if(input.contains("rclick:") ){
			
			return rclick;
		} else if(input.contains("click:") ){
			
			return click;
		} else if(input.contains("dndDown:") ){
			
			return dndDown;
		} else if(input.contains("dndUp:") ){
			
			return dndUp;
		}else if(input.contains("keyboard:") ){
			String outputChars[] = input.split(":");
			outputChar = outputChars[1];
			return keyboard;
		}
		
		return wat;
	}
}
