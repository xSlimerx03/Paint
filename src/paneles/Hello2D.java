package paneles;

import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;

public class Hello2D extends JPanel 
{
    public static void main(String[] args) 
    {
        new SnippetFrame(new Hello2D(), "Hello2D Panel");
    }
    
    public Hello2D()
    {
        super(true);
        setLayout(new FlowLayout(FlowLayout.LEFT,1,1));
        setBackground(GV.PALE_GREEN_COLOR);
        setBorder(GV.createCompoundBorder("Hello2D Panel", Color.black));
    }
    
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
    
        g2.setColor(Color.blue);
        
        Ellipse2D ellipse = new Ellipse2D.Double(-100, -50, 200, 100);
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.PI / 6.0);
    
        Shape shape = transform.createTransformedShape(ellipse);
    
        g2.translate(300,200);
        g2.scale(2,2);
        g2.draw(shape);
        g2.setColor(Color.red);
        g2.drawString("Hello 2D", 0, 0);
    }
}

