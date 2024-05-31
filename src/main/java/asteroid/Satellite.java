package asteroid;

public class Satellite {

    private double aMass;
    private double aRadius;
    private double bMass;
    private double bRadius;
    private double distance;
    private double G = 6.67e-11; // Gravitational constant
    private long initialTime;

    public Satellite() {
        // Empty constructor
    }

    public void setMass(double a, double b) {
        aMass = a;
        bMass = b;
    }

    public void setDistances(double a, double b, double d) {
        aRadius = a;
        bRadius = b;
        distance = d + aRadius + bRadius;
    }

    public double baryCenter() {
        // Returns the distance of the center of mass from the more massive body
        if (aMass > bMass) {
            return (distance * bMass) / (aMass + bMass);
        } else if (bMass > aMass) {
            return (distance * aMass) / (bMass + aMass);
        } else {
            return distance / 2;
        }
    }

    public double forceGrav() {
        // Returns the gravitational force between the two bodies
        return G * ((aMass * bMass) / (distance * distance));
    }

    public double period() {
        // Returns the orbital period of the system
        double rCubed = Math.pow(distance, 3.0);
        return Math.sqrt(((4 * Math.PI * Math.PI) / (G * (aMass + bMass))) * rCubed);
    }

    public double angVelo() {
        // Returns the angular velocity of the system
        return (2 * Math.PI) / period();
    }

    public void initializeTime() {
        initialTime = System.currentTimeMillis();
    }

    public double[] getPositions(long currentTime) {
        double elapsedTime = (currentTime - initialTime) / 1000.0; // seconds
        double period = period();
        double angle = (elapsedTime / period) * 2 * Math.PI; // radians

        // Assuming circular orbits for simplicity
        double aOrbitRadius = baryCenter(); // Distance of aMass from barycenter
        double bOrbitRadius = distance - aOrbitRadius; // Distance of bMass from barycenter

        double aX = aOrbitRadius * Math.cos(angle);
        double aY = aOrbitRadius * Math.sin(angle);
        double bX = -bOrbitRadius * Math.cos(angle);
        double bY = -bOrbitRadius * Math.sin(angle);

        return new double[]{aX, aY, bX, bY};
    }

    public double getAMassRadius() {
        return aRadius;
    }

    public double getBMassRadius() {
        return bRadius;
    }

    public double getDistance() {
        return distance;
    }

    public long getInitialTime() {
        return initialTime;
    }
}
