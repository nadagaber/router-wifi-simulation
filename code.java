// Nada Gaber Mohamed 20217014
// Malak Gamal 20217009
// Zaynab Ahmed ElAgamy 20215016
// Malak Mohamed Sobhi 20215034
// S10

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
class Semaphore {
    protected int value=0;
    protected Semaphore(int limit) {
        value = limit;
    }
    private void writeToFile(String message) {
        try {
            FileWriter writer = new FileWriter("logs_file.txt", true);
            writer.write(message + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public synchronized void P(Device device, boolean arrived) {
        value--;
        if (arrived) {
            if (value < 0) {
                writeToFile("(" + device.name + ") (" + device.type + ") arrived and waiting");
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                writeToFile("(" + device.name + ") (" + device.type + ") arrived");
            }
        } else {
            if (value < 0) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public synchronized void V(Device device, int connectionNumber, boolean onlineActivity) {
        value++;
        if (!onlineActivity) {
            writeToFile("Connection " + connectionNumber + ": " + device.name + " Login");
            writeToFile("Connection " + connectionNumber + ": " + device.name + " performs online activity...");
            writeToFile("Connection " + connectionNumber + ": " + device.name + " Logged out");
        }
        if (value <= 0) notify();
    }
}
class Router {
    public static int totalConncetions;
    private Device connections[];
    private int inptr;
    private int outptr;
    Semaphore spaces;
    Semaphore noOfDevices;
    public Router(int s) {
        totalConncetions = s;
        connections = new Device[totalConncetions];
        outptr = 0;
        inptr = 0;
        spaces = new Semaphore(totalConncetions);
        noOfDevices = new Semaphore(0);
    }
    public int occupyConnection(Device device) {
        spaces.P(device, true);
        connections[inptr] = device;
        int connectionNumber = inptr + 1;
        inptr = (inptr + 1) % totalConncetions;
        noOfDevices.V(device, connectionNumber, true);
        return connectionNumber;
    }
    public int releaseConnection() {
        Device obj = connections[outptr];
        noOfDevices.P(obj, false);
        int connectionNumber = outptr + 1;
        outptr = (outptr + 1) % totalConncetions;
        spaces.V(obj, connectionNumber, false);
        return connectionNumber;
    }
}
class Device extends Thread{
    protected String name;
    protected String type;
    static int connectionNumber = 1;
    protected Router router;

    public Device(String name, String type, Router router){
        this.name = name;
        this.type = type;
        this.router = router;
    }
    private void writeToFile(String message) {
        try {
            FileWriter writer = new FileWriter("logs_file.txt", true);
            writer.write(message + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private int connect()
    {
        return router.occupyConnection(this);
    }
    private void performOnlineActivity() {
        try {
            sleep((int) (Math.random() * 2000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void disconnect()
    {
        router.releaseConnection();
    }
    @Override
    public void run(){
        connectionNumber=connect();
        writeToFile("Connection "+ connectionNumber+ ": "+this.name+" Occupied");
        performOnlineActivity();
        disconnect();
    }
}
class Network {
    public static void main(String [] args){
        Scanner input = new Scanner(System.in);
        System.out.println("What is the number of WI-FI Connections?");
        int N = input.nextInt();
        System.out.println("What is the number of devices Clients want to connect?");
        int TC = input.nextInt();
        ArrayList<Device> devices = new ArrayList<Device>();
        String deviceName;
        String deviceType;
        Router router = new Router(N);
        for(int i=0;i<TC;i++){
            deviceName = input.next();
            deviceType = input.next();
            devices.add(new Device(deviceName,deviceType,router));
        }
        System.out.println("The output is printed in a file called logs_file");
        for (Device device : devices)
        {
            device.start();
        }
    }
}