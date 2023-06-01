import java.io.*;
import java.net.*;
import java.util.Random;

public class Target {
    public int[] generateTarget() {
        int spaceWidth = 1000;
        int spaceHeight = 1000;

        // Create a random number generator
        Random random = new Random();

        // Generate random coordinates for the target within the space
        int targetX = random.nextInt(spaceWidth);
        int targetY = random.nextInt(spaceHeight);
        return new int[]{targetX-500, targetY-500}; // -500 to make the axis interval between -500 and +500
    }
  public void run() {
	try {
		int serverPort = 4020;
        int serverPort2 = 4040;
		InetAddress host = InetAddress.getByName("localhost"); 
		System.out.println("Connecting to Sensor1 on port " + serverPort); 
        // the following line will connect to the server or will throw an exception
        // The client obtains a Socket object by instantiating one		
        Socket socket = new Socket(host,serverPort); 
		//Socket socket = new Socket("127.0.0.1", serverPort);
		System.out.println("Just connected to " + socket.getRemoteSocketAddress());
        //now create output stream to send data to the socket at the server's end of the channel 
		PrintWriter toServer = 
			new PrintWriter(socket.getOutputStream(),true);
        //connect to sensor2
        System.out.println("Connecting to Sensor2 on port " + serverPort2); 
        Socket socket2 = new Socket(host,serverPort2); 
		System.out.println("Just connected to " + socket2.getRemoteSocketAddress());
        PrintWriter toServer2 = 
			new PrintWriter(socket2.getOutputStream(),true);        
        // and create an input stream to receive data from client's socket
        //The output stream is connected to the input stream of the remote socket. 
//		BufferedReader fromServer = 
//			new BufferedReader(
//					new InputStreamReader(socket.getInputStream()));
        
        //The input stream is connected to the output stream of the remote socket. 
        int[] t = generateTarget(); // t[0] is pointX and t[1] is pointY
        toServer.println("Target's coordinates are: " + t[0] + " " + t[1]); 
        toServer2.println("Target's coordinates are: " + t[0] + " " + t[1]); 
//		String line = fromServer.readLine();
//		System.out.println("Target received: " + line + " from Server");
		toServer.close();
//		fromServer.close();
		socket.close();
        toServer2.close();
        socket2.close();
	}
	catch(UnknownHostException ex) {
		ex.printStackTrace();
	}//Throw exception if cannot make a connection.
	catch(IOException e){
		e.printStackTrace();
	}
  }
	
  public static void main(String[] args) {
        while(true){
		    Target target = new Target();
		    target.run();
            try {
                    Thread.sleep(5000);
                  } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
        }
  }
}
