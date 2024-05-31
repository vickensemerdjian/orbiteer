package asteroid;

public class Satellite {

    private double aMass;
    private double aRadius;
    private double bMass;
    private double bRadius;
    private double distance;
    private double G = 6.67*(Math.pow(10, -11));
    private long initialTime;

    public Satellite() {
        initialTime = System.currentTimeMillis();
    }

    public void setMass(double a, double b){
        aMass = a;
        bMass = b;
    }

    public void setDistances(double a, double b, double d){
        aRadius = a;
        bRadius = b;
        distance = d+aRadius+bRadius;
    }

    public double baryCenter(){
//the double returned by baryCenter is the distance of the center of mass from the more massive body
        if(aMass > bMass){
            return (distance*bMass)/(aMass+bMass);
        }
        if(bMass > aMass){
            return (distance*aMass)/(bMass+aMass);
        }
        else{
            return distance/2;
        }
    }
    public double ForceGrav(){
        return G*((aMass*bMass)/(distance*distance));
    }
    private double period(){
        double rcubed = Math.pow(distance, 3.0);
        return Math.sqrt(((4*9.8696)/(G*(aMass+bMass)))*rcubed);
    }
    public double angVelo(){
        return 6.18/period();
    }





    // Method to get current position based on time
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

}

