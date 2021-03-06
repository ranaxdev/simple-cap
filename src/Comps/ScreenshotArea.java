package Comps;

import com.sun.awt.AWTUtilities;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/*
    This class creates a borderless transparent frame that covers the entire screen
    The purpose is to draw the area you wish to extract the screenshot from on this frame
 */
public class ScreenshotArea extends JFrame implements ClipboardOwner {

    ToolsPanel.Mode mode;
    static int[] dimensions = null;
    static final Object lock = new Object();
    BufferedImage img;

    public ScreenshotArea(ToolsPanel.Mode mode) {
        this.mode = mode;
        // Get screen dimensions and set selectable area to encompass it
        Rectangle r = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);
        setIconImage(new ImageIcon("res/icon.png").getImage());
        setSize(r.width, r.height);

        // Sets to transparent
        AWTUtilities.setWindowOpaque(this, false);

        // Crop drawing panel
        //setContentPane(new DrawCrop(r.width, r.height));
        DrawCrop crop = new DrawCrop(r.width, r.height);
        add(crop);

        // Minimize tools GUI
        MainFrame.minimize(true);
        setVisible(true);

        /*
        *   Start a new thread and wait for screenshot dimensions to be returned
        *   Thread is observing dimensions array and waits till it has some values
        *   before proceeding.
        *   Finish by processing the screenshot data
        */
        Thread t = new Thread(() -> {
            synchronized (lock){
                while(dimensions == null){
                    try{
                        lock.wait();
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                // ***********************************
                // (CHECKPOINT) AREA CROP DATA RETURNED
                // ***********************************

                    /* Process screenshot data */
                // Remove drawable crop area from frame
                remove(crop);
                revalidate();
                repaint();

                // Capture the screenshot and store it in a buffered image object
                try{
                    Robot robot = new Robot();
                    Rectangle cap = new Rectangle(dimensions[0], dimensions[1], dimensions[2], dimensions[3]);
                    img = robot.createScreenCapture(cap);
                } catch (AWTException e){
                    e.printStackTrace();
                }


                // MODE :   COPY TO CLIPBOARD
                if(mode.equals(ToolsPanel.Mode.COPY)){
                    TransferableImage transferableImg = new TransferableImage(img);
                    Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
                    c.setContents(transferableImg, this);
                }

                // MODE :   SAVE TO COMPUTER
                else if(mode.equals(ToolsPanel.Mode.SAVE)){
                    JFileChooser jfc = new JFileChooser();
                    // Set extensions
                    jfc.setAcceptAllFileFilterUsed(false);
                    jfc.setFileFilter(new FileNameExtensionFilter("png", "png"));
                    jfc.setFileFilter(new FileNameExtensionFilter("jpg", "jpg"));

                    if(jfc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){
                        // Get desired save file location
                        File file = jfc.getSelectedFile();
                        // Save the file
                        try {
                            String ext = jfc.getFileFilter().getDescription();
                            ImageIO.write(img, "png", new File(file.getAbsolutePath()+"."+ext));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else{ // Cancel was pressed, Alert and exit app
                        JOptionPane.showMessageDialog(null, "No File Chosen.");
                    }
                }

                // Shutdown application
                dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            }
        });
        t.start();
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
        System.out.println("Lost Clipboard ownership");
    }
}



/*
    Transferable Image implementation used to transfer to system clipboard
 */
class TransferableImage implements Transferable{

    Image img;
    public TransferableImage(Image img){
        this.img = img;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        DataFlavor[] flavors = new DataFlavor[1];
        flavors[0] = DataFlavor.imageFlavor;
        return flavors;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        DataFlavor[] flavors = getTransferDataFlavors();
        for (DataFlavor dataFlavor : flavors) {
            if (flavor.equals(dataFlavor))
                return true;
        }
        return false;
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if(flavor.equals(DataFlavor.imageFlavor) && img != null)
            return img;
        else
            throw new UnsupportedFlavorException(flavor);
    }
}




// Area you can crop out using the mouse drag
// This class is adapted from https://stackoverflow.com/a/40945778
class DrawCrop extends JPanel {
    int screen_w, screen_h;
    int x1, y1, x2, y2;
    float alpha = 0.5f;
    Color bg_color = new Color(1.0f, 1.0f,1.0f, alpha);
    Color outline = new Color(0.0f,1.0f,0.0f,1.0f);

    public DrawCrop(int screen_w, int scree_h){
        x1 = x2 = y1 = y2 = 0;
        this.screen_w = screen_w;
        this.screen_h= scree_h;

        // Mouse listener
        MouseManager mouse = new MouseManager();
        addMouseListener(mouse);
        addMouseMotionListener(mouse);

        // Panel config
        setOpaque(false);
        setLayout(null);
        repaint();

    }

    // Return dimensions of cropped area in array format [x , y , width, height]
    public int[] getScreenDim(int x1, int y1, int x2, int y2){
        int px = Math.min(x1,x2);
        int py = Math.min(y1, y2);
        int pw = Math.abs(x1-x2);
        int ph = Math.abs(y1-y2);
        return new int[]{px,py,pw,ph};
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw background (translucent white)
        g.setColor(bg_color);
        g.fillRect(0,0,screen_w,screen_h);

        // Draw the clear rectangle (cropping area)
        int[] dims = getScreenDim(x1,y1,x2,y2);
        g.clearRect(dims[0], dims[1], dims[2], dims[3]);

        // Draw green outline
        g.setColor(outline);
        g.drawRect(dims[0]-2, dims[1]-2, dims[2]+4, dims[3]+4);

    }

    // Private class : Mouse adapter
    class MouseManager extends MouseAdapter{
        @Override
        public void mousePressed(MouseEvent e) {
            // Top-left rectangle coords are where mouse was first clicked
            x1 = e.getX();
            y1 = e.getY();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            x2 = e.getX();
            y2 = e.getY();
            repaint();

            // Area locked in - return rectangle dimensions and notify thread
            synchronized (ScreenshotArea.lock){
                ScreenshotArea.dimensions = getScreenDim(x1,y1,x2,y2);
                ScreenshotArea.lock.notify();
            }

        }

        @Override
        public void mouseDragged(MouseEvent e) {
            x2 = e.getX();
            y2 = e.getY();
            repaint();
        }
    }
}