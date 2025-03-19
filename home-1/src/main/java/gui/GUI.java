//package gui;
//
//import java.awt.*;
//import java.awt.event.*;
//import javax.swing.*;
//
//
//public class GUI {
//
//    class SimpleFrame extends JFrame {
//        private static final int frameWidth = 800;
//        private static final int frameHeight = 600;
//        public SimpleFrame() {
//            setTitle("Simple Frame");
//            setSize(frameWidth,frameHeight);
//        }
//    }
//
//    class EventFrame {
//        EventQueue.invokeLater(()->
//        {
//            SimpleFrame frame = new SimpleFrame();
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setVisible(true);
//        });
//    }
//}
