import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import javax.microedition.khronos.egl.EGL11;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL11;

public class GLComp extends Component /* implements Runnable*/{
    private BufferedImage cimg;
    private ImageObserver null_observer;

    private EGL11 egl;
    private EGLDisplay eglDisplay;
    private EGLSurface eglSurface;
    private EGLConfig eglConfig;
    private EGLContext eglContext;
    private GL11 gl;

    private boolean initGLDraw;

    FloatBuffer vertex_buffer;
    FloatBuffer color_buffer;


    public GLComp(int width, int height) {
        super();
        setSize(width, height);
        cimg = GraphicsEnvironment.getLocalGraphicsEnvironment().
            getDefaultScreenDevice().getDefaultConfiguration().
            createCompatibleImage(width, height);
        null_observer = new ImageObserver() {
            public boolean imageUpdate(Image img, int infoflags, int x, int y,
                int width, int height) {
                return false;
            }
        };

        egl = (EGL11)EGLContext.getEGL();
        eglDisplay = egl.eglGetDisplay(EGL11.EGL_DEFAULT_DISPLAY);
        int[] version = new int[2];
        egl.eglInitialize(eglDisplay, version);
        int [] attribs = {
            EGL11.EGL_DEPTH_SIZE, 16,
            EGL11.EGL_RED_SIZE, 8,
            EGL11.EGL_GREEN_SIZE, 8,
            EGL11.EGL_BLUE_SIZE, 8,
            EGL11.EGL_ALPHA_SIZE, 8,
            EGL11.EGL_BIND_TO_TEXTURE_RGBA, EGL11.EGL_TRUE,
            EGL11.EGL_NONE
        };
        EGLConfig[] configs = new EGLConfig[1];
        int[] num_config = new int[1];
        egl.eglChooseConfig(eglDisplay, attribs, configs, 1, num_config);
        eglConfig = configs[0];
        eglContext = egl.eglCreateContext(eglDisplay, eglConfig,
            EGL11.EGL_NO_CONTEXT, null);
        gl = (GL11)eglContext.getGL();
        eglSurface =
            egl.eglCreatePixmapSurface(eglDisplay, eglConfig, cimg, null);

        initGLDraw = true;
    }

    private void initGLDraw() {
        Dimension dim;
        dim = getSize();
        gl.glViewport(0, 0, dim.width, dim.height);
        gl.glMatrixMode(GL11.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrthof(-2.0f, 2.0f, -2.0f, 2.0f, 0.1f, 2.0f);
        gl.glMatrixMode(GL11.GL_MODELVIEW);
        gl.glLoadIdentity();

        gl.glEnable(GL11.GL_DEPTH_TEST);
        gl.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL11.GL_COLOR_ARRAY);

        float[] vertex_array =
            new float[] {
                0.0f, 1.0f, -0.5f,
                -1.0f, -1.0f, -0.5f,
                1.0f, -1.0f, -0.5f
            };
        float[] color_array =
            new float[] {
                1.0f, 0.0f, 0.0f, 1.0f,
                0.0f, 1.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f, 1.0f,
            };

        vertex_buffer =
            ByteBuffer.allocateDirect(vertex_array.length * 4).asFloatBuffer();
        vertex_buffer.put(vertex_array);
        vertex_buffer.rewind();

        color_buffer =
            ByteBuffer.allocateDirect(color_array.length * 4).asFloatBuffer();
        color_buffer.put(color_array);
        color_buffer.rewind();

        initGLDraw = false;
    }

    public void paint(Graphics g) {
        egl.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext);
        if (initGLDraw) {
            initGLDraw();
        }
        gl.glClearColor(0.3f, 0.3f, 0.3f, 1.0f);
        gl.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        gl.glVertexPointer(3, GL11.GL_FLOAT, 0, vertex_buffer);
        gl.glColorPointer(4, GL11.GL_FLOAT, 0, color_buffer);
        gl.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);
        egl.eglWaitGL();
        egl.eglMakeCurrent(eglDisplay, EGL11.EGL_NO_SURFACE,
            EGL11.EGL_NO_SURFACE, EGL11.EGL_NO_CONTEXT);
        g.drawImage(cimg, 0, 0, null_observer);
    }

    public void update(Graphics g) {
        paint(g);
    }

    public static void main(String[] args) {
        Container scene = new Container();
        GLComp t1 = new GLComp(100, 100);
        GLComp t2 = new GLComp(500, 200);
        t1.setLocation(100, 100);
        t2.setLocation(200, 300);
        scene.add(t1);
        scene.add(t2);
        scene.setVisible(true);
    }
}
