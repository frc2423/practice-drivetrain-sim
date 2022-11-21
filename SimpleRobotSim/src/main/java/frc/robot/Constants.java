package frc.robot;

import edu.wpi.first.math.util.Units;

public class Constants {
    public static final double WHEEL_RADIUS = Units.inchesToMeters(3);
    public static final double GEAR_REDUCTION = 7.29;
    public static final double TRACK_WIDTH = Units.inchesToMeters(28);

    public static final double KvLinear = 1.98;
    public static final double KaLinear = 0.2;
    public static final double KvAngular = 1.5;
    public static final double KaAngular = 0.3;

    public static final String GO_FORWARD_AUTO = "Go Forward";
    public static final String TRAVEL_PATH_AUTO = "Travel Path";
}
