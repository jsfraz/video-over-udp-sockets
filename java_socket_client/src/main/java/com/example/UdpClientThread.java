package com.example;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UdpClientThread extends Thread {
    private DatagramSocket socket;
    private InetAddress ip;
    private int port;
    private String message;
    private MainWindow mainWindow;

    private final int receiveBufferLength = 65536;
    private byte[] sendBuffer;
    private byte[] receiveBuffer;

    private boolean running;

    public UdpClientThread(String ip, int port, String message, int timeout, MainWindow mainWindow) {
        try {
            this.socket = new DatagramSocket();
            this.socket.setSoTimeout(timeout);
            this.ip = InetAddress.getByName(ip);
            this.port = port;
            this.message = message;
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
        this.mainWindow = mainWindow;
        this.running = true;
    }

    public void run() {
        while (running) {
            sendBuffer = message.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, ip, port);
            try {
                socket.send(sendPacket);

                try {
                    receiveBuffer = new byte[receiveBufferLength];
                    DatagramPacket receivedPacket = new DatagramPacket(receiveBuffer, receiveBuffer.length, ip, port);
                    socket.receive(receivedPacket);
                    byte[] data = receivedPacket.getData();
                    
                    mainWindow.updateImage(data);
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        System.out.println("Exiting thread...");
        mainWindow.setDefaultImage();
    }

    public void stopRunning() {
        running = false;
    }
}
