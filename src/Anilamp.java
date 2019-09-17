import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

public class Anilamp extends JFrame implements ActionListener{

	private static final int WIDTH = 1024;
	private static final int HEIGHT = 768;
	private static final Dimension dimension = new Dimension(WIDTH, HEIGHT);
	private GLCanvas canvas;
	private Anilamp_GLEventListener glEventListener;
	private final FPSAnimator animator; 
	private Camera camera;
	
	public static void main(String[] args) {
		Anilamp a = new Anilamp("Anilamp");
	    a.getContentPane().setPreferredSize(dimension);
	    a.pack();
	    a.setVisible(true);
	}

	public Anilamp(String textForTitleBar) {
		super(textForTitleBar);
	    GLCapabilities glcapabilities = new GLCapabilities(GLProfile.get(GLProfile.GL3));
	    canvas = new GLCanvas(glcapabilities);
	    camera = new Camera(Camera.DEFAULT_POSITION, Camera.DEFAULT_TARGET, Camera.DEFAULT_UP);
	    glEventListener = new Anilamp_GLEventListener(camera);
	    canvas.addGLEventListener(glEventListener);
	    canvas.addMouseMotionListener(new MyMouseInput(camera));
	    canvas.addKeyListener(new MyKeyboardInput(camera));
	    getContentPane().add(canvas, BorderLayout.CENTER);
	    JMenuBar menuBar=new JMenuBar();
	    this.setJMenuBar(menuBar);
	      JMenu fileMenu = new JMenu("File");
	        JMenuItem quitItem = new JMenuItem("Quit");
	        quitItem.addActionListener(this);
	        fileMenu.add(quitItem);
	    menuBar.add(fileMenu);
	    
	    JPanel p = new JPanel();
	      JButton b = new JButton("Jump");
	      b.addActionListener(this);
	      p.add(b);
	      b = new JButton("RandomPose");
	      b.addActionListener(this);
	      p.add(b);
	      b = new JButton("Turn on");
	      b.addActionListener(this);
	      p.add(b);
	      b = new JButton("Turn off");
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
		if (e.getActionCommand().equalsIgnoreCase("Jump")) {
			glEventListener.randomJump();
		    }
		else if(e.getActionCommand().equalsIgnoreCase("RandomPose")){
			glEventListener.randomPose();
		}else if (e.getActionCommand().equalsIgnoreCase("Turn on")) {
			glEventListener.turnOn();
		}else if (e.getActionCommand().equalsIgnoreCase("Turn off")) {
			glEventListener.turnOff();
		}else if(e.getActionCommand().equalsIgnoreCase("quit"))
		      System.exit(0);
		
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
 
	  public void mouseDragged(MouseEvent e) {
	    Point ms = e.getPoint();
	    float sensitivity = 0.001f;
	    float dx=(float) (ms.x-lastpoint.x)*sensitivity;
	    float dy=(float) (ms.y-lastpoint.y)*sensitivity;
	    if (e.getModifiers()==MouseEvent.BUTTON1_MASK)
	      camera.updateYawPitch(dx, -dy);
	    lastpoint = ms;
	  }

	  public void mouseMoved(MouseEvent e) {   
	    lastpoint = e.getPoint(); 
	  }
	}

}
