import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.util.Animator;

import javax.swing.*;

class TestRenderer extends JFrame implements GLEventListener {
    private GLCanvas canvas;
    private Animator animator;

    // For specifying the positions of the clipping planes (increase/decrease the distance) modify this variable.
    // It is used by the glOrtho method.
    private double v_size = 1.0;

    // Application main entry point
    public static void main(String args[])
    {
        new TestRenderer();
    }

    // Default constructor
    public TestRenderer()
    {
        super("Java OpenGL");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setSize(800, 600);

        this.initializeJogl();

        this.setVisible(true);
    }

    private void initializeJogl()
    {
        // Creating a new GL profile.
        GLProfile glprofile = GLProfile.getDefault();
        // Creating an object to manipulate OpenGL parameters.
        GLCapabilities capabilities = new GLCapabilities(glprofile);

        // Setting some OpenGL parameters.
        capabilities.setHardwareAccelerated(true);
        capabilities.setDoubleBuffered(true);

        // Try to enable 2x anti aliasing. It should be supported on most hardware.
        capabilities.setNumSamples(2);
        capabilities.setSampleBuffers(true);

        // Creating an OpenGL display widget -- canvas.
        this.canvas = new GLCanvas(capabilities);

        // Adding the canvas in the center of the frame.
        this.getContentPane().add(this.canvas);

        // Adding an OpenGL event listener to the canvas.
        this.canvas.addGLEventListener(this);

        // Creating an animator that will redraw the scene 40 times per second.
        this.animator = new Animator(this.canvas);

        // Starting the animator.
        this.animator.start();
    }

    public void init(GLAutoDrawable canvas)
    {
        // Obtaining the GL instance associated with the canvas.
        GL2 gl = canvas.getGL().getGL2();

        // Setting the clear color -- the color which will be used to erase the canvas.
        gl.glClearColor(0, 0, 0, 0);

        // Selecting the modelview matrix.
        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);

    }

    public void display(GLAutoDrawable canvas)
    {
        GL2 gl = canvas.getGL().getGL2();

        // Erasing the canvas -- filling it with the clear color.
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();

        // Define the vertices and colors of the square
        float[] vertices = {
                -0.5f, -0.5f, // Vertex 1 (x, y)
                0.5f, -0.5f, // Vertex 2 (x, y)
                0.5f,  0.5f, // Vertex 3 (x, y)
                -0.5f,  0.5f  // Vertex 4 (x, y)
        };
        float[] colors = {
                1.0f, 0.0f, 0.0f, // Vertex 1: Red
                0.0f, 1.0f, 0.0f, // Vertex 2: Green
                0.0f, 0.0f, 1.0f, // Vertex 3: Blue
                1.0f, 1.0f, 0.0f  // Vertex 4: Yellow
        };

        // Draw the square using GL_LINES
        gl.glBegin(GL2.GL_LINES);
        for (int i = 0; i < 4; i++) {
            gl.glColor3fv(colors, i * 3);
            gl.glVertex2fv(vertices, i * 2);
            gl.glColor3fv(colors, ((i + 1) % 4) * 3);
            gl.glVertex2fv(vertices, ((i + 1) % 4) * 2);
        }
        gl.glEnd();
        gl.glMatrixMode(GL2.GL_MODELVIEW);  // switch to the modelview matrix
        gl.glLoadIdentity();               // reset the matrix to the identity matrix

        gl.glTranslatef(0.5f, 0.3f, 0.0f);
        gl.glBegin(GL2.GL_LINE_STRIP);

// Draw the square using GL_QUADS
        gl.glBegin(GL2.GL_QUADS);
        for (int i = 0; i < 4; i++) {
            // Specify the color for the current vertex
            gl.glColor3fv(colors, i * 3);
            // Specify the coordinates for the current vertex
            gl.glVertex2fv(vertices, i * 2);
        }
// Repeat the first vertex to close the quad
        gl.glVertex2fv(vertices, 0);
        gl.glEnd();

        gl.glTranslatef(0.3f, 0.1f, 0.0f);
        gl.glBegin(GL2.GL_LINE_LOOP);
        for (int i = 0; i < 4; i++) {
            gl.glColor3fv(colors, i * 3);  // set the color for the current vertex
            gl.glVertex2fv(vertices, i * 2); // setam coordonatele varfului patratului
        }
        gl.glEnd();



        // Forcing the scene to be rendered.
        gl.glFlush();
    }

    public void reshape(GLAutoDrawable canvas, int left, int top, int width, int height)
    {
        GL2 gl = canvas.getGL().getGL2();

        // Selecting the viewport -- the display area -- to be the entire widget.
        gl.glViewport(0, 0, width, height);

        // Determining the width to height ratio of the widget.
        double ratio = (double) width / (double) height;

        // Selecting the projection matrix.
        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);

        gl.glLoadIdentity();

        // Selecting the view volume to be x from 0 to 1, y from 0 to 1, z from -1 to 1.
        // But we are careful to keep the aspect ratio and enlarging the width or the height.
        if (ratio < 1)
            gl.glOrtho(-v_size, v_size, -v_size, v_size / ratio, -1, 1);
        else
            gl.glOrtho(-v_size, v_size * ratio, -v_size, v_size, -1, 1);

        // Selecting the modelview matrix.
        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
    }

    public void displayChanged(GLAutoDrawable canvas, boolean modeChanged, boolean deviceChanged)
    {
        return;
    }

    @Override
    public void dispose(GLAutoDrawable arg0) {
        // TODO Auto-generated method stub
    }
}