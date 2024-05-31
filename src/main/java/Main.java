import java.awt.*;
//import Satellite;
import java.util.Scanner; // Import the Scanner class


import asteroid.Window;

public class Main {
    public static void main(String[] args) {
        Window window = Window.get();
        window.run();
    }



    public static void methods() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Input your mass a: ");
        double a = scanner.nextDouble(); // Read the value for mass a

        System.out.print("Input your mass b: ");
        double b = scanner.nextDouble(); // Read the value for mass b


        Satellite satellite = new Satellite();
        satellite.setMass(a, b);
    }
    
}
