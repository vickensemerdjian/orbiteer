import asteroid.Window;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Input the mass of body A (Earth): ");
        double aMass = scanner.nextDouble();

        System.out.print("Input the radius of body A (Earth): ");
        double aRadius = scanner.nextDouble();

        System.out.print("Input the mass of body B (Moon): ");
        double bMass = scanner.nextDouble();

        System.out.print("Input the radius of body B (Moon): ");
        double bRadius = scanner.nextDouble();

        System.out.print("Input the distance between the two bodies: ");
        double distance = scanner.nextDouble();

        // Initialize the window and start running
        Window window = Window.get();
        window.run(aMass, aRadius, bMass, bRadius, distance);

        // Clean up resources
        scanner.close();
    }
}