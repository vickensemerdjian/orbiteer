package asteroid;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static java.sql.Types.NULL;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Window {
    private int width, height;
    private String title;
    private long glfwWindow;
    private Satellite satellite;

    private static Window window = null;

    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "simpleOrbit";
        this.satellite = new Satellite(); // Initialize the satellite object
    }

    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window();
        }
        return Window.window;
    }

    public void run(double aMass, double aRadius, double bMass, double bRadius, double distance) {
        System.out.println("Opened your visualisation in a new window: LWJGL Version" + Version.getVersion());

        // Set satellite properties
        satellite.setMass(aMass, bMass); // Earth and Moon masses for example
        satellite.setDistances(aRadius, bRadius, distance); // Earth radius, Moon radius, and average distance

        init();
        loop();
    }

    public void init() {
        // Error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Init GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        // Configure
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // Create window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Unable to create the GLFW window");
        }

        // Make OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        // VSync
        glfwSwapInterval(1);

        // Show window
        glfwShowWindow(glfwWindow);

        GL.createCapabilities();
    }

    public void loop() {
        while (!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents();

            glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            long currentTime = System.currentTimeMillis();
            double[] positions = satellite.getPositions(currentTime);

            double aX = positions[0];
            double aY = positions[1];
            double bX = positions[2];
            double bY = positions[3];

            // Render satellites
            renderSatellite(aX, aY, satellite.getAMassRadius());
            renderSatellite(bX, bY, satellite.getBMassRadius());

            glfwSwapBuffers(glfwWindow);
        }
    }

    private void renderSatellite(double x, double y, double radius) {
        glPushMatrix();
        glTranslated(x, y, 0);
        // Draw circle representing the satellite
        glBegin(GL_POLYGON);
        int segments = 100;
        for (int i = 0; i < segments; i++) {
            double theta = 2.0 * Math.PI * i / segments;
            double dx = radius * Math.cos(theta);
            double dy = radius * Math.sin(theta);
            glVertex2d(dx, dy);
        }
        glEnd();
        glPopMatrix();
    }
}
