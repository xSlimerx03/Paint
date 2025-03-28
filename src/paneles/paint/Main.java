/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paneles.paint;



import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Custom Draw Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CustomDrawPanel drawPanel = new CustomDrawPanel();
        ShapeSelectorPanel selectorPanel = new ShapeSelectorPanel(drawPanel);

        frame.setLayout(new BorderLayout());
        frame.add(selectorPanel, BorderLayout.NORTH);

        frame.pack();
        frame.setVisible(true);
    }
}