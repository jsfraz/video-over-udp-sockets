package com.example;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.core.Rect;

public class MainWindow extends JFrame {
    private UdpClientThread thread; // udp thread
    private BufferedImage defaultImage; // default image when client is not receiving data
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
            picLabel = new JLabel(new ImageIcon(defaultImage));
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        input = showFace(input);
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

    // face detection: https://www.geeksforgeeks.org/image-processing-in-java-face-detection/
    private byte[] showFace(byte[] input) {
        // Mat from bytes https://stackoverflow.com/questions/33493941/how-to-convert-byte-array-to-mat-object-in-java
        Mat image = Imgcodecs.imdecode(new MatOfByte(input), Imgcodecs.IMREAD_UNCHANGED);

        // face detector from source cascades
        CascadeClassifier faceDetector = new CascadeClassifier();
        faceDetector.load("src/main/resources/haarcascade_frontalface_alt.xml");

        // detecting faces
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image,
                faceDetections);

        // rectangular box around face
        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(
                    image, new Point(rect.x, rect.y),
                    new Point(rect.x + rect.width,
                            rect.y + rect.height),
                    new Scalar(0, 0, 255), 2);
        }

        // https://stackoverflow.com/questions/28426927/mat-to-byte-conversion-not-working-in-java
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", image, matOfByte);
        return matOfByte.toArray();
    }
}
