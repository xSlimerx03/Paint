package paneles;

import java.awt.*;
import javax.swing.*;

public class SnippetFrame extends JFrame 
{
    public static void main(String[] args) 
    {
        new SnippetFrame();
    }
    
    public SnippetFrame(Component component, String title, Dimension dimension) 
    {
        super(title);
        createUI(component, dimension);
    } 
    
    public SnippetFrame(Component component, String title) 
    {
        this(component, title, new Dimension(1630, 850));  // Tamaño por defecto
    }
    
    public SnippetFrame() {
        this(null, "Application Frame v1.0");
    }
    
    protected void createUI(Component component, Dimension dimension) {
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        if (component != null)
            getContentPane().add(component, BorderLayout.CENTER);  
        setSize(dimension);  // Establecer tamaño basado en dimension
        center();
        setVisible(true);
    }
    
    public void center() 
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getSize();
        int x = (screenSize.width - frameSize.width) / 2;
        int y = (screenSize.height - frameSize.height) / 2;
        setLocation(x, y);
    }
}
