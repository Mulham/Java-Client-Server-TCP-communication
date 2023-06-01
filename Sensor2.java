// Client Side
import java.io.*;
import java.net.*;
import java.util.Random;

public class Sensor2 {
    public int[] generateSensor() {
        int spaceWidth = 1000;
        int spaceHeight = 1000;

        // Create a random number generator
        Random random = new Random();

        // Generate random coordinates for the point within the space
        int pointX = random.nextInt(spaceWidth);
        int pointY = random.nextInt(spaceHeight);
        return new int[]{pointX-500, pointY-500};
    }
    public void run() {
	        try {
		        int sensorServerPort = 4040;
                //  An exception occurs if the port is already bound by another application. 
		        ServerSocket serverSocket = new ServerSocket(sensorServerPort);
		        serverSocket.setSoTimeout(10000); 
		        while(true) {
			        System.out.println("Waiting for target on port " + serverSocket.getLocalPort() + "..."); 
                    // start listening for incoming connection requests from clients
                    //the server obtains a Socket object from the return value of the accept() method
			        Socket server = serverSocket.accept(); 
                //Method accept() wait until a client requests a connection; then it returns a Socket that connects the client to the server
			        System.out.println("Just connected to " + server.getRemoteSocketAddress()); 

			        PrintWriter toClient = 
				        new PrintWriter(server.getOutputStream(),true);
			        BufferedReader fromClient =
				        new BufferedReader(
						        new InputStreamReader(server.getInputStream()));
			        String line = fromClient.readLine();
			        System.out.println("Sensor2 received: " + line); 
                    String[] cords = line.split(" ");
                    int targetY = Integer.parseInt(cords[cords.length -1]);
                    int targetX = Integer.parseInt(cords[cords.length -2]);
                
                    // start as client 

                    int serverPort = 4030;
		            InetAddress host = InetAddress.getByName("localhost"); 
                    try {
                      Thread.sleep(2000);
                    } catch (InterruptedException e) {
                      Thread.currentThread().interrupt();
                    }
		            System.out.println("Connecting to server on port " + serverPort); 
                    // the following line will connect to the server or will throw an exception
                    // The client obtains a Socket object by instantiating one		
                    Socket socket = new Socket(host,serverPort); 
		            //Socket socket = new Socket("127.0.0.1", serverPort);
		            System.out.println("Just connected to " + socket.getRemoteSocketAddress());
                    //now create output stream to send data to the socket at the server's end of the channel 
		            PrintWriter toServer = 
			            new PrintWriter(socket.getOutputStream(),true);
                    // and create an input stream to receive data from client's socket
                    //The output stream is connected to the input stream of the remote socket. 
		            BufferedReader fromServer = 
			            new BufferedReader(
					            new InputStreamReader(socket.getInputStream()));
                    
                    //The input stream is connected to the output stream of the remote socket. 
                    int[] t = generateSensor(); // t[0] is pointX and t[1] is pointY
                   //here we have t and target's coordinates. we can calculate the target's kerteriz
                  double angle = Math.atan2((targetX+500) - (t[0]+500),(targetY+500) - (t[1]+500));    //in radians
                angle = Math.toDegrees(angle);           //in degrees
                angle = (angle + 360) % 360;  //adjust angle, if it's under 0 we add 360, if it's 360 or above we subtract 360
                    toServer.println("Sensor2's coordinates are: " + t[0] + " " + t[1] + " Target's angle is: " + String.format("%.2f", angle)); 
		            String line2 = fromServer.readLine();
		            System.out.println("Sensor2 received: " + line2);
		            toServer.close();
		            fromServer.close();
		            socket.close();

                    
			     //   toClient.println("Thank you for connecting to " + server.getLocalSocketAddress() + "\nGoodbye!"); 
		        }
	        }
	        catch(UnknownHostException ex) {
		        ex.printStackTrace();
	        }
	        catch(IOException e){
		        e.printStackTrace();
	        }
          }

	
  public static void main(String[] args) {
		Sensor2 sensor = new Sensor2();
		sensor.run();
  }
}
