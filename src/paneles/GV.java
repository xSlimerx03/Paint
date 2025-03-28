package paneles;

import java.awt.*;
import java.util.Date;
import java.util.Random;
import javax.swing.border.*;

public class GV 
{
    public static final Color PALE_RED_COLOR = new Color(255, 228, 225);       // MistyRose
    public static final Color PALE_BLUE_COLOR = new Color(230, 230, 250);      // Lavender
    public static final Color PALE_GREEN_COLOR = new Color(240, 255, 240);     // Honeydew
    public static final Color PALE_YELLOW_COLOR = new Color(255, 255, 224);    // LightYellow

    private static Random random = new Random(new Date().getTime());
    public static final Color RANDOM_COLOR = new Color
        ((int)(random.nextDouble()*255),
         (int)(random.nextDouble()*255),
         (int)(random.nextDouble()*255));
    
    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    public static Dimension getScreenSize() { return SCREEN_SIZE; }

    private static final int FRAME_WIDTH = SCREEN_SIZE.width - 50;  // HORIZONTAL_GAP
    private static final int FRAME_HEIGHT = SCREEN_SIZE.height - 100; // VERTICAL_GAP
    private static final Dimension FRAME_SIZE = new Dimension(FRAME_WIDTH, FRAME_HEIGHT);
    public static Dimension getFrameSize() { return FRAME_SIZE; }

    private static final int PAINT2D_PANEL_HEIGHT = FRAME_HEIGHT - 50; // PAINT2D_PANEL_GAP
    private static final Dimension PAINT2D_PANEL_SIZE = new Dimension(FRAME_WIDTH, PAINT2D_PANEL_HEIGHT);
    public static Dimension getPaint2DPanelSize() { return PAINT2D_PANEL_SIZE; }

    public static void setPanelSize(Component component, Dimension size) {
        component.setPreferredSize(size);
        component.setMinimumSize(size);
        component.setMaximumSize(size);
        component.setSize(size);
    }

    // Border utility methods
    public static Border createTitledBorder(String title, Color color) {
        return new TitledBorder(new LineBorder(color, 2, true), title);
    }

    public static Border createCompoundBorder(String title, Color color) {
        return new CompoundBorder(new LineBorder(color, 2), new TitledBorder(title));
    }
}