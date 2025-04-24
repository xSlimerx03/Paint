/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paint;

import paneles.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import javax.swing.JFrame;
import javax.swing.*;

import paneles.paint.CustomDrawPanel;
import paneles.paint.ShapeSelectorPanel;

/**
 *
 * @author Raúl
 */
public class principal extends JFrame {

    JPanel hello2dP = new Hello2D();
    JPanel paintFlowP = new PaintFlow();
    JPanel hello2DV2P = new Hello2DV2();
    JPanel Ex3EllipsesSliderP = new Ex3EllipsesSlider();
    JPanel EX4RandomEllipseP = new EX4RandomEllipse();
    JPanel EX5mosaicoP = new EX5mosaico();
    JPanel EX8_10FigurasMejorP = new EX8_10FigurasMejor();
    JPanel EX9_FigurasToolsBarP = new EX9_FigurasToolsBar();
    JPanel EX10FigurasMejoradasP = new EX10FigurasMejoradas();
    JPanel Java2DAttributesPanelP = new Java2DAttributesPanel();
    JPanel MonaLisaPanelP = new MonaLisaPanel();
    JPanel ElGritoPanelP = new ElGritoPanel(); // Ensure that the ElGritoPanel class is implemented in the paneles package
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

        hello2dP.setPreferredSize(new Dimension(1200, 1000));
        paintFlowP.setPreferredSize(new Dimension(1100, 800));
        hello2DV2P.setPreferredSize(new Dimension(1100, 800));
        Ex3EllipsesSliderP.setPreferredSize(new Dimension(1100, 800));
        EX4RandomEllipseP.setPreferredSize(new Dimension(1100, 800));
        EX5mosaicoP.setPreferredSize(new Dimension(1100, 800));
        EX8_10FigurasMejorP.setPreferredSize(new Dimension(1100, 800));
        EX9_FigurasToolsBarP.setPreferredSize(new Dimension(1100, 800));
        EX10FigurasMejoradasP.setPreferredSize(new Dimension(1100, 800));
        Java2DAttributesPanelP.setPreferredSize(new Dimension(1100, 800));
        MonaLisaPanelP.setPreferredSize(new Dimension(1100, 800));
        ElGritoPanelP.setPreferredSize(new Dimension(1100, 800));

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

        EX4RandomEllipse = new JMenuItem("Random Ellipse");
        EX4RandomEllipse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EX4RandomEllipse(e);
            }
        });

        EX5mosaico = new JMenuItem("Mosaico");
        EX5mosaico.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EX5mosaico(e);
            }
        });

        EX8_10FigurasMejor = new JMenuItem("Figuras Mejoradas");
        EX8_10FigurasMejor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EX8_10FigurasMejor(e);
            }
        });

        EX9_FigurasToolBar = new JMenuItem("Figuras ToolBar");
        EX9_FigurasToolBar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EX9_FigurasToolBar(e);
            }
        });

        EX10FigurasMejoradas = new JMenuItem("Figuras Mejoradas 2");
        EX10FigurasMejoradas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EX10FigurasMejoradas(e);
            }
        });

        Java2DAttributesPanel = new JMenuItem("Java2D Attributes");
        Java2DAttributesPanel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Java2DAttributesPanel(e);
            }
        });

        MonaLisaPanel = new JMenuItem("Mona Lisa");
        MonaLisaPanel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MonaLisaPanel(e);
            }
        });

        ElGritoPanel = new JMenuItem("El Grito");
        ElGritoPanel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ElGritoPanel(e);
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
        menuTrabajos.add(EX4RandomEllipse);
        menuTrabajos.add(EX5mosaico);
        menuTrabajos.add(EX8_10FigurasMejor);
        menuTrabajos.add(EX9_FigurasToolBar);
        menuTrabajos.add(EX10FigurasMejoradas);
        menuTrabajos.add(Java2DAttributesPanel);
        menuTrabajos.add(MonaLisaPanel);
        menuTrabajos.add(ElGritoPanel);
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
        Ex3EllipsesSliderP.revalidate();
        Ex3EllipsesSliderP.repaint();
    }

    private void EX4RandomEllipse(ActionEvent evt) {
        setTitle("Random Ellipse");
        scrollPane.setViewportView(EX4RandomEllipseP);
        EX4RandomEllipseP.revalidate();
        EX4RandomEllipseP.repaint();
    }

    private void EX5mosaico(ActionEvent evt) {
        setTitle("Mosaico");
        scrollPane.setViewportView(EX5mosaicoP);
        EX5mosaicoP.revalidate();
        EX5mosaicoP.repaint();
    }

    private void EX8_10FigurasMejor(ActionEvent evt) {
        setTitle("Figuras Mejoradas");
        scrollPane.setViewportView(EX8_10FigurasMejorP);
        EX8_10FigurasMejorP.revalidate();
        EX8_10FigurasMejorP.repaint();
    }

    private void EX9_FigurasToolBar(ActionEvent evt) {
        setTitle("Figuras ToolBar");
        scrollPane.setViewportView(EX9_FigurasToolsBarP);
        EX9_FigurasToolsBarP.revalidate();
        EX9_FigurasToolsBarP.repaint();
    }

    private void EX10FigurasMejoradas(ActionEvent evt) {
        setTitle("Figuras Mejoradas 2");
        scrollPane.setViewportView(EX10FigurasMejoradasP);
        EX10FigurasMejoradasP.revalidate();
        EX10FigurasMejoradasP.repaint();
    }

    private void Java2DAttributesPanel(ActionEvent evt) {
        setTitle("Java2D Attributes");
        scrollPane.setViewportView(Java2DAttributesPanelP);
        Java2DAttributesPanelP.revalidate();
        Java2DAttributesPanelP.repaint();
    }

    private void MonaLisaPanel(ActionEvent evt) {
        setTitle("Mona Lisa");
        scrollPane.setViewportView(MonaLisaPanelP);
        MonaLisaPanelP.revalidate();
        MonaLisaPanelP.repaint();
    }

    private void ElGritoPanel(ActionEvent evt) {
        setTitle("El Grito");
        scrollPane.setViewportView(ElGritoPanelP);
        ElGritoPanelP.revalidate();
        ElGritoPanelP.repaint();
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
    private JMenuItem EX4RandomEllipse, EX5mosaico, EX8_10FigurasMejor, EX9_FigurasToolBar;
    private JMenuItem EX10FigurasMejoradas, Java2DAttributesPanel, MonaLisaPanel, ElGritoPanel;
}