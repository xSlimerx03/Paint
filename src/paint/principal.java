/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paint;

import paneles.PaintFlow;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.*;
import paneles.Ex3EllipsesSlider;
import paneles.Hello2D;
import paneles.Hello2DV2;
import paneles.paint.CustomDrawPanel;
import paneles.paint.ShapeSelectorPanel;

/**
 *
 * @author alex5
 */
public class principal extends JFrame {

    JPanel hello2dP = new Hello2D();
    JPanel paintFlowP = new PaintFlow();
    JPanel hello2DV2P = new Hello2DV2();
    JPanel Ex3EllipsesSliderP = new Ex3EllipsesSlider();
    CustomDrawPanel drawPanel = new CustomDrawPanel();
    ShapeSelectorPanel PanelPaint = new ShapeSelectorPanel(drawPanel);

    public principal() {
        initComponents();
    }

    private void initComponents() {
        this.setSize(1100, 800);
        this.setTitle("Principal");
        this.setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        hello2dP.setPreferredSize(new Dimension(1200, 1000)); // Tamaño preferido para el JPanel
        paintFlowP.setPreferredSize(new Dimension(1100, 800)); // Tamaño preferido para el JPanel
        hello2DV2P.setPreferredSize(new Dimension(1100, 800)); // Tamaño preferido para el JPanel
        Ex3EllipsesSliderP.setPreferredSize(new Dimension(1100, 800)); // Tamaño preferido para el JPanel

        scrollPane = new JScrollPane();

        menuOpciones = new JMenuBar();
        menuTrabajos = new JMenu("Trabajos");

        hello2d = new JMenuItem("Hello Java 2d");
        hello2d.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hello2d(e);
            }
        });

        paintFlow = new JMenuItem("paintFlow");
        paintFlow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paintFlow(e);
            }
        });

        hello2DV2 = new JMenuItem("hello2DV2");
        hello2DV2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hello2DV2(e);
            }
        });

        Ex3EllipsesSlider = new JMenuItem("Elipses");
        Ex3EllipsesSlider.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Ex3EllipsesSlider(e);
            }
        });

        PanelPaintOp = new JMenuItem("Paint");
        PanelPaintOp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paintPanel(e);
            }
        });

        menuTrabajos.add(hello2d);
        menuTrabajos.add(hello2DV2);
        menuTrabajos.add(paintFlow);
        menuTrabajos.add(Ex3EllipsesSlider);
        menuTrabajos.add(PanelPaintOp);
        PanelPaint.add(drawPanel);

        menuOpciones.add(menuTrabajos);

        setLayout(new BorderLayout());
        setJMenuBar(menuOpciones);

        add(scrollPane, BorderLayout.CENTER);
    }

    private void hello2d(ActionEvent evt) {
        setTitle("Hola 2d");
        scrollPane.setViewportView(hello2dP);
        hello2dP.revalidate();
        hello2dP.repaint();
    }

    private void paintFlow(ActionEvent evt) {
        setTitle("paintFlow");
        scrollPane.setViewportView(paintFlowP);
    }

    private void hello2DV2(ActionEvent evt) {
        setTitle("paintFlow");
        scrollPane.setViewportView(hello2DV2P);
        hello2DV2P.revalidate();
        hello2DV2P.repaint();
    }

    private void Ex3EllipsesSlider(ActionEvent evt) {
        setTitle("Elipses");
        scrollPane.setViewportView(Ex3EllipsesSliderP);
        Ex3EllipsesSlider.revalidate();
        Ex3EllipsesSlider.repaint();
    }

    private void paintPanel(ActionEvent evt) {
        setTitle("Paint");
        scrollPane.setViewportView(PanelPaint);
        PanelPaint.revalidate();
        PanelPaint.repaint();
    }

    public static void main(String[] args) {
        
        new principal().setVisible(true);
    }

    private JScrollPane scrollPane;
    private JMenuBar menuOpciones;
    private JMenu menuTrabajos;
    private JMenuItem hello2d, paintFlow, hello2DV2, Ex3EllipsesSlider, PanelPaintOp;
}
