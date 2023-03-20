package com.example;

public class App {
    public static void main(String[] args) {
        // loading OpenCV library: https://stackoverflow.com/questions/38262346/java-lang-unsatisfiedlinkerror-no-opencv-java2411-in-java-library-path
        nu.pattern.OpenCV.loadLocally();
        new MainWindow();
    }
}
