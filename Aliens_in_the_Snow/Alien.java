/* I declare that this code is my own work */
/* Author - Prajwal Patil, Email - pmpatil1@sheffield.ac.uk */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;


public class Alien extends JFrame implements ActionListener {
    
    private static final int Width = 1024;
    private static final int Height = 768;
    private static final Dimension dimension = new Dimension(Width, Height);
    private GLCanvas canvas;
    private AlienGLEventListener glEventListener;
    private final FPSAnimator animator; 
    private Camera camera;

    public Alien(String textForTitleBar){
        super(textForTitleBar);
        GLCapabilities glcapabilities = new GLCapabilities(GLProfile.get(GLProfile.GL3));
        canvas = new GLCanvas(glcapabilities);
        camera = new Camera(Camera.DEFAULT_POSITION, Camera.DEFAULT_TARGET, Camera.DEFAULT_UP);
        glEventListener = new AlienGLEventListener(camera);
        canvas.addGLEventListener(glEventListener);
        canvas.addMouseMotionListener(new MyMouseInput(camera));
        canvas.addKeyListener(new MyKeyboardInput(camera));
        getContentPane().add(canvas, BorderLayout.CENTER);
        
JPanel p = new JPanel(new GridLayout(2, 6));
        JButton b = new JButton("camera X");
        b.addActionListener(this);
        p.add(b);
        b = new JButton("camera Z");
        b.addActionListener(this);
        p.add(b);
        b = new JButton("Let's Rock");
        b.addActionListener(this);
        p.add(b);
        b = new JButton("Let's Roll");
        b.addActionListener(this);
        p.add(b);
        b = new JButton("Stop");
        b.addActionListener(this);
        p.add(b);
        b = new JButton("Reset");
        b.addActionListener(this);
        p.add(b);
        b = new JButton("Light1 OFF!");
        b.addActionListener(this);
        p.add(b);
        b = new JButton("Light1 ON!");
        b.addActionListener(this);
        p.add(b);
        b = new JButton("Light2 OFF!");
        b.addActionListener(this);
        p.add(b);
        b = new JButton("Light2 ON!");
        b.addActionListener(this);
        p.add(b);
        b = new JButton("SpotLight ON!");
        b.addActionListener(this);
        p.add(b);
        b = new JButton("SpotLight OFF!");
        b.addActionListener(this);
        p.add(b);
        this.add(p, BorderLayout.SOUTH);
        
        addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
            animator.stop();
            remove(canvas);
            dispose();
            System.exit(0);
        }
        });
        animator = new FPSAnimator(canvas, 60);
        animator.start();
    }
public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equalsIgnoreCase("camera X")) {
            camera.setCamera(Camera.CameraType.X);
            canvas.requestFocusInWindow();
        }
        else if (e.getActionCommand().equalsIgnoreCase("camera Z")) {
            camera.setCamera(Camera.CameraType.Z);
            canvas.requestFocusInWindow();
        }
        else if (e.getActionCommand().equalsIgnoreCase("Let's Rock")) {
            glEventListener.startRockAnimation();
        }
        else if (e.getActionCommand().equalsIgnoreCase("Let's Roll")) {
            glEventListener.startRollAnimation();
        }
        else if (e.getActionCommand().equalsIgnoreCase("Stop")) {
            glEventListener.stopAnimation();
        }
        else if (e.getActionCommand().equalsIgnoreCase("Reset")) {
            glEventListener.reset();
        }
        else if (e.getActionCommand().equalsIgnoreCase("Light1 OFF!")) {
            glEventListener.light1Off();
        }
        else if (e.getActionCommand().equalsIgnoreCase("Light1 ON!")) {
            glEventListener.light1ON();
        }
        else if (e.getActionCommand().equalsIgnoreCase("Light2 OFF!")) {
            glEventListener.light2Off();
        }
        else if (e.getActionCommand().equalsIgnoreCase("Light2 ON!")) {
            glEventListener.light2ON();
        }
        else if (e.getActionCommand().equalsIgnoreCase("SpotLight ON!")) {
            glEventListener.spotlightsOn();
        }
        else if (e.getActionCommand().equalsIgnoreCase("SpotLight OFF!")) {
            glEventListener.spotlightsOff();
        }
        else if(e.getActionCommand().equalsIgnoreCase("quit"))
            System.exit(0);
    }


    public static void main(String[] args) {
        Alien alien = new Alien("Aliens in the Snow!");
        alien.getContentPane().setPreferredSize(dimension);
        alien.pack();
        alien.setVisible(true);
      }
}

class MyKeyboardInput extends KeyAdapter  {
    private Camera camera;
    
    public MyKeyboardInput(Camera camera) {
        this.camera = camera;
    }
    
    public void keyPressed(KeyEvent e) {
        Camera.Movement m = Camera.Movement.NO_MOVEMENT;
        switch (e.getKeyCode()) {
        case KeyEvent.VK_LEFT:  m = Camera.Movement.LEFT;  break;
        case KeyEvent.VK_RIGHT: m = Camera.Movement.RIGHT; break;
        case KeyEvent.VK_UP:    m = Camera.Movement.UP;    break;
        case KeyEvent.VK_DOWN:  m = Camera.Movement.DOWN;  break;
        case KeyEvent.VK_A:  m = Camera.Movement.FORWARD;  break;
        case KeyEvent.VK_Z:  m = Camera.Movement.BACK;  break;
        }
        camera.keyboardInput(m);
    }
    }

    class MyMouseInput extends MouseMotionAdapter {
    private Point lastpoint;
    private Camera camera;
    
    public MyMouseInput(Camera camera) {
        this.camera = camera;
    }
    
        /**
     * mouse is used to control camera position
     *
     * @param e  instance of MouseEvent
     */    
    public void mouseDragged(MouseEvent e) {
        Point ms = e.getPoint();
        float sensitivity = 0.001f;
        float dx=(float) (ms.x-lastpoint.x)*sensitivity;
        float dy=(float) (ms.y-lastpoint.y)*sensitivity;
        //System.out.println("dy,dy: "+dx+","+dy);
        if (e.getModifiersEx()==MouseEvent.BUTTON1_DOWN_MASK)
        camera.updateYawPitch(dx, -dy);
        lastpoint = ms;
    }

    /**
     * mouse is used to control camera position
     *
     * @param e  instance of MouseEvent
     */  
    public void mouseMoved(MouseEvent e) {   
        lastpoint = e.getPoint(); 
    }
    }   
