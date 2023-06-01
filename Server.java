// The java.net class library provides classes Socket and ServerSocket for message passing for TCP/IP
import java.net.*;
import java.io.*;

public class Server { 
  public double[] calculateTargetCords(int[] point1, int[] point2, double angle1, double angle2) {
        // convert from degrees to radians
        double angleRadian1 = (90-angle1)*(Math.PI/180); // to consider that it's from y-axis not x-axis
        double angleRadian2 = (90-angle2)*(Math.PI/180);
        double slope1 = Math.tan(angleRadian1);
        double slope2 = Math.tan(angleRadian2);
        // Calculate the y-intercepts of the lines
        double intercept1 = point1[1] - slope1 * point1[0];
        double intercept2 = point2[1] - slope2 * point2[0];
        //point[0] is x and point[1] is y
        // Calculate the x-coordinate of the intersection point
        double x = (intercept2 - intercept1) / (slope1 - slope2); // the x of the target

        // Calculate the y-coordinate of the intersection point using one of the lines
        double y = slope1 * x + intercept1; // the y of the target
        return new double[]{x, y};
  }
  public void run() {
    int[] sensor1 = new int[]{0,0};
    int[] sensor2 = new int[]{0,0};
    int counter = 0;
    double angle1 = 0; 
    double angle2 = 0;
	try {
		int serverPort = 4030;
        //  An exception occurs if the port is already bound by another application. 
		ServerSocket serverSocket = new ServerSocket(serverPort);
		serverSocket.setSoTimeout(10000); 
		while(true) {
            if(counter==2){
                counter = 0;
            }
			System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "..."); 
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
			System.out.println("Server received: " + line); 
            String[] words = line.split(" ");
            double angle = Double.parseDouble(words[words.length -1]);
            int targetY = Integer.parseInt(words[words.length -5]);
            int targetX = Integer.parseInt(words[words.length -6]);
            counter++;
            if (counter == 1){
                sensor1[0] = targetX;
                sensor1[1] = targetY;
                angle1 = angle;
            }
            else if (counter == 2){
                sensor2[0] = targetX;
                sensor2[1] = targetY;
                angle2 = angle;
                double[] target = calculateTargetCords(sensor1, sensor2, angle1, angle2);
                System.out.println(String.format("Calculated target coordinates are:  %.2f %.2f", target[0], target[1])); 
            }
			toClient.println("Message received to Server" + server.getLocalSocketAddress()); 
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
		Server server = new Server();
		server.run();
  }
}
