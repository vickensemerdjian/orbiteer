package asteroid;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Window {
    private int width, height;
    private String title;
    private long glfwWindow;
    private Satellite satellite;
    private double scalingFactor = 1.0; // Initial scaling factor
    private double maxBodySize = 12742.0; // Maximum body size in kilometers (Earth's diameter)

    private static Window window = null;

    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "Orbit Visualization";
        this.satellite = new Satellite();
    }

    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window();
        }
        return Window.window;
    }

    public void run(double aMass, double aRadius, double bMass, double bRadius, double distance) {
        System.out.println("Opened your visualization in a new window: LWJGL Version " + Version.getVersion());
        double per;
        double G = 6.67e-11;
        double rCubed = Math.pow(distance, 3.0);
        per = Math.sqrt(((4 * Math.PI * Math.PI) / (G * (aMass + bMass))) * rCubed);
        System.out.println("your Orbital Period is " + per + " seconds.");
        // Set satellite properties
        satellite.setMass(aMass, bMass);
        satellite.setDistances(aRadius, bRadius, distance);

        // Initialize the initial time in the Satellite class
        satellite.initializeTime();

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

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        // Create window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, 0, 0);
        if (glfwWindow == 0) {
            throw new IllegalStateException("Failed to create GLFW window");
        }

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        GL.createCapabilities();

        // Set up the projection matrix
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        double aspectRatio = (double) width / height;
        glOrtho(-aspectRatio, aspectRatio, -1.0, 1.0, -1.0, 1.0);

// Calculate the scaling factor based on the maximum body size
        double maxBodyRadius = maxBodySize / 2.0; // Earth's radius in kilometers
        double maxViewportRadius = Math.min(width, height) / 2.0; // Maximum viewport radius in pixels
        scalingFactor = maxViewportRadius / maxBodyRadius; // Scaling factor to fit the Earth within the viewport

        glMatrixMode(GL_MODELVIEW);

        // Set the clear color (background color)
        glClearColor(0.1f, 0.1f, 0.1f, 1.0f); // Dark gray background

        // Enable alpha blending for transparent satellite rendering
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public void loop() {
        // Run the rendering loop until the user closes the window
        while (!glfwWindowShouldClose(glfwWindow)) {
            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();

            // Clear the framebuffer
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);


            // Render satellites
            renderSatellites();

            // Swap the color buffers
            glfwSwapBuffers(glfwWindow);
        }

        // Clean up resources
        glfwTerminate();
    }

    private void renderSatellites() {
        // Get current positions of the satellites
        long currentTime = System.currentTimeMillis();
        double[] positions = satellite.getPositions(currentTime);

        double aX = positions[0];
        double aY = positions[1];
        double bX = positions[2];
        double bY = positions[3];

        // Render Earth (Body A)
        renderSatellite(aX, aY, satellite.getAMassRadius(), 0.0f, 0.5f, 1.0f, 1.0f); // Light blue

        // Calculate the period and angular velocity of Satellite B
        double period = satellite.period();
        double angVelo = satellite.angVelo();

        // Calculate the angle for Satellite B based on its angular velocity
        double bAngle = (currentTime - satellite.getInitialTime()) * angVelo;

        // Calculate the orbit radius of Satellite B
        double bOrbitRadius = satellite.getDistance() - satellite.baryCenter();

        // Calculate the position of Satellite B
        double bXPos = -bOrbitRadius * Math.cos(bAngle);
        double bYPos = -bOrbitRadius * Math.sin(bAngle);

        // Render Moon (Body B)
        renderSatellite(bXPos, bYPos, satellite.getBMassRadius(), 1.0f, 1.0f, 1.0f, 0.5f); // Semi-transparent white
    }

    private void renderSatellite(double x, double y, double radius, float red, float green, float blue, float alpha) {
        glPushMatrix();
        glTranslated(x * scalingFactor, y * scalingFactor, 0);
        glColor4f(red, green, blue, alpha);
        glBegin(GL_POLYGON);
        int segments = 50;
        for (int i = 0; i < segments; i++) {
            double theta = 2.0 * Math.PI * i / segments;
            double dx = (radius * scalingFactor) * Math.cos(theta);
            double dy = (radius * scalingFactor) * Math.sin(theta);
            glVertex2d(dx, dy);
        }
        glEnd();
        glPopMatrix();
    }
}