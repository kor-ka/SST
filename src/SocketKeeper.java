import java.net.*;
import java.util.Queue;
import java.io.*;
public class SocketKeeper {
	  static int port;
   public static void main(String[] ar)    {
     
     
       try {
    	   
    	  
    	     System.out.println("Gimme a POOORT!");
    	     BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
    	     String portLine = null;
    	   
    	 portLine = keyboard.readLine();
    	 port = Integer.parseInt(portLine);
    	 while(port==-1){
    		 System.out.println("Are f**king kidding me? ");
    		 System.out.println("Try again");	
    		 portLine = keyboard.readLine();    	 
    	 }
    	 ServerSocket ss = new ServerSocket(port); 
    	
    	 Qqueue q = new Qqueue();
    	 Results r = new Results();
    	 
    	 final Counter cntr = new Counter(q,r);
    	 new Thread(cntr).start();
    	
    	 final ListenServer ls = new ListenServer(ss, q, r);
    	 new Thread(ls).start();
    	 while(true){
    		 final String say = keyboard.readLine();
    		 System.out.println("You going 2 say: " + say);
    		 
        	new Thread(new Runnable(){
        		 public void run (){
        			 ls.say(say);
        			 return;
        		}
        	}).start();
    	 }
    	
      } catch(Exception x) { x.printStackTrace(); }
   }
}
