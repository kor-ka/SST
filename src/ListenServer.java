import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import static java.awt.event.KeyEvent.*;

public class ListenServer extends Thread {
	protected ServerSocket listenSocket;
	DataOutputStream out;
	Socket socket;
	ArrayList<String> clientIps;
	Qqueue q;
	Results r;
	public ListenServer(ServerSocket listenSocket, Qqueue q, Results r) {
		this.listenSocket = listenSocket;
		this.q = q;
		this.r = r;
	}

	public void run() {
		clientIps = new ArrayList<String>();
		while (true) {
			try {

				System.out
						.println("Waiting for a client... or you can send thmth");

				socket = listenSocket.accept();

				System.out
						.println("Got a client :) ... Finally, someone saw me through all the cover!");
				System.out.println();

				InputStream sin = socket.getInputStream();
				OutputStream sout = socket.getOutputStream();

				DataInputStream in = new DataInputStream(sin);
				DataOutputStream out = new DataOutputStream(sout);

				String line = null;
				
				Robot robot;
				StringSelection stringSelection;
				Clipboard clpbrd = Toolkit.getDefaultToolkit ().getSystemClipboard ();

				line = in.readUTF();
				System.out.println("Client say: " + line);
				protocol prtcl = new protocol();
				switch (prtcl.processInput(line)) {
				case protocol.ab:
					int a= Integer.parseInt(prtcl.outputA);
					int b= Integer.parseInt(prtcl.outputB);
					q.put(new Pare(a, b));
					System.out.println("a:" + a);
					System.out.println("b:" + b);
					
					out.writeUTF("ok");
					out.flush();

					break;
					
				case protocol.click:
					
					try {
						robot = new Robot();
						 // LEFT CLICK
			            robot.mousePress(InputEvent.BUTTON1_MASK);
			            robot.mouseRelease(InputEvent.BUTTON1_MASK);
					} catch (AWTException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 
			           
					break;
					
				case protocol.rclick:
					
					try {
						robot = new Robot();
						 // R CLICK
			            robot.mousePress(InputEvent.BUTTON3_MASK);
			            robot.mouseRelease(InputEvent.BUTTON3_MASK);
					} catch (AWTException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 
			           
					break;
					
				case protocol.dndDown:
					
					try {
						robot = new Robot();
						 // LEFT CLICK
			            robot.mousePress(InputEvent.BUTTON1_MASK);
			            
					} catch (AWTException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 
			           
					break;
					
					case protocol.dndUp:
					
					try {
						robot = new Robot();
						 // LEFT CLICK
			            
			            robot.mouseRelease(InputEvent.BUTTON1_MASK);
					} catch (AWTException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 
			           
					break;
					
					case protocol.launch:
						stringSelection = new StringSelection (prtcl.outputToLounch);
						clpbrd.setContents (stringSelection, null);
						doType(VK_WINDOWS);
					try {
						Thread.sleep(100);
						doType(VK_SLASH);
						Thread.sleep(100);
						doType(VK_BACK_SPACE);
						doType(VK_CONTROL, VK_V);
						doType(VK_ENTER);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
						
						
						break;
					
					case protocol.keyboard:
					try {
						robot = new Robot();
						char chr = prtcl.outputChar.charAt(0);
						boolean isChrEnter = KeyEvent.getExtendedKeyCodeForChar(chr)==VK_ENTER;
						boolean isChrSpace = KeyEvent.getExtendedKeyCodeForChar(chr)==VK_SPACE;
						 if (prtcl.outputChar.equals("bksps")){
							doType(VK_BACK_SPACE);
						}else if (prtcl.outputChar.equals("up")){
							doType(VK_UP);
						}else if (prtcl.outputChar.equals("down")){
							doType(VK_DOWN);
						}else if (prtcl.outputChar.equals("left")){
							doType(VK_LEFT);
						}else if (prtcl.outputChar.equals("right")){
							doType(VK_RIGHT);
						}else if (prtcl.outputChar.equals("esc")){
							doType(VK_ESCAPE);
						}else if (prtcl.outputChar.equals("enter")){
							doType(VK_ENTER);
						}else if (prtcl.outputChar.equals("contextMenu")){
							doType(VK_CONTEXT_MENU);
						} else if(isChrEnter){
							doType(VK_ENTER);							
						}else if(isChrSpace){
							doType(VK_SPACE);							
						}else{
							stringSelection = new StringSelection (prtcl.outputChar);							
							clpbrd.setContents (stringSelection, null);
							
							doType(VK_CONTROL, VK_V);
						}
						
						
						//robot.keyPress(Character.toUpperCase(prtcl.outputChar.charAt(0)));
					} catch (AWTException e) {
						
						e.printStackTrace();
					}
						
					break;

				case protocol.register:
					
						String ip = socket.getInetAddress().getHostAddress();
						if(!clientIps.contains(ip+":"+prtcl.outputPort)){
							System.out.println("rtying to register:"+ip+":"+prtcl.outputPort);
					           
							clientIps.add(ip+":"+prtcl.outputPort);

							out.writeUTF("registred");
							out.flush();

								System.out.println("registred:"+ip+":"+prtcl.outputPort);
						}else{
							clientIps.remove(ip+":"+prtcl.outputPort);
							out.writeUTF("unregistred");
							out.flush();

								System.out.println("unregistred:"+ip+":"+prtcl.outputPort);
						}
						
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}

		}

	}
	
	public void say(String say){
		for(String clientIpPort:clientIps){
			 InputStream sin;
			 String ip=null;
				int port =-1;
				int delimetr;
			try {
				
				
				delimetr=clientIpPort.indexOf(":");
				ip = clientIpPort.substring(0, delimetr);
				port =Integer.parseInt( clientIpPort.substring(delimetr+1));
				
				
				System.out.println("trying say:"+ip+":"+port);
				
				InetAddress ipAddress = InetAddress.getByName(ip);
				Socket clientSocket = new Socket(ipAddress, port);
				sin = clientSocket.getInputStream();
			
	            OutputStream sout = clientSocket.getOutputStream();

	            
	            DataInputStream in = new DataInputStream(sin);
	            DataOutputStream out = new DataOutputStream(sout);

	            say =r.get();
	                out.writeUTF("results:"+say); 
	                out.flush(); 
	                clientSocket.close();
			} catch (IOException e) {
				clientIps.remove(ip+":"+port);
				
				System.out.println("Ooops, that was exeption, i'l better remove it:"+ip+":"+port);
				e.printStackTrace();
			}
		}
	}

	//Util
	private void doType(int... keyCodes) {
		try{
			doType(keyCodes, 0, keyCodes.length);	
		}catch(IllegalArgumentException e){
			e.printStackTrace();
		}
        
    }

    private void doType(int[] keyCodes, int offset, int length) {
        if (length == 0) {
            return;
        }
        Robot robot;
		try {
			robot = new Robot();
			robot.keyPress(keyCodes[offset]);
	        doType(keyCodes, offset + 1, length - 1);
	        robot.keyRelease(keyCodes[offset]);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
}
