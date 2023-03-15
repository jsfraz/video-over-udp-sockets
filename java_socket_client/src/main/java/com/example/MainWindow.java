package com.example;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class MainWindow extends JFrame {
    private UdpClientThread thread;     // udp thread
    private BufferedImage defaultImage;     // default image when client is not receiving data
    private JLabel picLabel;

    public MainWindow() {
        super("Received video");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // initializing thread
        thread = new UdpClientThread("127.0.01", 9999, "getFrame", 1000, this);

        // setting default image
        try {
            defaultImage = ImageIO
                    .read(Thread.currentThread().getContextClassLoader().getResourceAsStream("image.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setDefaultImage();

        // adding label to frame
        add(picLabel);

        // resize and show window
        pack();
        setVisible(true);

        // running thread
        thread.start();
    }

    // update 
    public void updateImage(byte[] input) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        try {
            BufferedImage image = ImageIO.read(inputStream);
            picLabel.setIcon(new ImageIcon(image));
            pack();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // set default label image
    public void setDefaultImage() {
        picLabel.setIcon(new ImageIcon(defaultImage));
    }
}
