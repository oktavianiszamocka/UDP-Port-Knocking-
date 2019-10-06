import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;

public class Client{
   private static DatagramSocket client;
    private  static DatagramPacket packet;

    public static void main(String[] args){
        createDatagramSocket();
        sendPacket();
        receivedMessage();
    }


    public static void createDatagramSocket(){
        try {
        	//create udp connection with random port
            client = new DatagramSocket();
            System.out.println("established socket at " + client.getLocalPort());
        } catch (SocketException e) {
            System.err.println("Error creating the socket: " + e);
            System.exit(1);
        }

    }
    
    public static void sendPacket(){
        Scanner sc = new Scanner(System.in);
        System.out.println("you going to knock the server ports, how long the length of the port?");
       //insert the long of sequence
        int length = sc.nextInt();

        try {
            InetAddress address = InetAddress.getByName("localhost");

        for(int i = 0; i<length; i++)  {
            System.out.println("provide the sequence of ports to knock, one by one in order");
            //user will input his guess of sequence knocking port of server
            int port = sc.nextInt();
            //if it is his first message, send message 'first'
            if(i == 0) {
            	String last = "first";
                byte buf [] = last.getBytes();
                System.out.println("sending the first packet");
                packet = new DatagramPacket(buf, buf.length, address , port);
                client.send(packet);
            }
            //if it s his last message, send message 'last'
            if(i == length-1){
                String last = "last";
                byte buf [] = last.getBytes();
                System.out.println("sending the last packet");
                packet = new DatagramPacket(buf, buf.length, address , port);
                client.send(packet);
            }
            //if it is not last either first, send 'hello server'
            else{
                String hello = "hello server";
                byte[] buffer = hello.getBytes();
                System.out.println("sending the packet");
                packet = new DatagramPacket(buffer, buffer.length, address , port);
                client.send(packet);
            }
        }
 
        } catch (IOException e) {
            System.err.println("Error receiving the package: " + e);
            System.exit(1);
        }
        sc.close();
        System.out.println("waiting result");
    }

    public static void receivedMessage(){
        try {
        	//set time out 10 seconds
            client.setSoTimeout(10000);
            byte [] bufor = new byte[256];
            packet = new DatagramPacket(bufor, bufor.length);
            System.out.println("I am waiting for the package");
            
            //waiting for packet, if in 10 s no message then socket will close
            client.receive(packet);
            printpacket(packet);
            
        }  catch (SocketTimeoutException e) {
            System.out.println("Time out reached, socket closed");
            client.close();
        } catch (SocketException e) {
            System.out.println("socket closed" + e);
        } catch(IOException e) {
            System.err.println("Error receiving the package: " + e);
            System.exit(1);
        }


    }

    public static void printpacket(DatagramPacket p){
        String data = new String(p.getData(), 0, p.getLength());
        InetAddress address = p.getAddress();
        int port = p.getPort();
        String msg = "message from"+ address.toString() + " " + String.valueOf(port) + " :" +data;
        System.out.println(msg);
        connectTCP(Integer.parseInt(data));
    }

    public static void connectTCP(int port){
        System.out.println("established socket tcp");
        Socket socket = null;
        try {
            socket = new Socket("localhost", port);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            output.println("i have connected with tcp connection");
            System.out.println( input.readLine());
            input.close();
            output.close();
            socket.close();
            client.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
