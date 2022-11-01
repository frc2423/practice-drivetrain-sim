package frc.robot;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim;

public class Sim {

    public static DifferentialDrivetrainSim driveSim = new DifferentialDrivetrainSim(
            // Create a linear system from our identification gains.
            LinearSystemId.identifyDrivetrainSystem(Constants.KvLinear, Constants.KaLinear,
                    Constants.KvAngular,
                    Constants.KaAngular),
            DCMotor.getNEO(2), // 2 NEO motors on each side of the drivetrain.
            Constants.GEAR_REDUCTION, // 7.29:1 gearing reduction.
            Constants.TRACK_WIDTH, // The track width is 0.7112 meters.
            Constants.WHEEL_RADIUS, // The robot uses 3" radius wheels.
            // The standard deviations for measurement noise:
            // x and y: 0.001 m
            // heading: 0.001 rad
            // l and r velocity: 0.1 m/s
            // l and r position: 0.005 m
            VecBuilder.fill(0.001, 0.001, 0.001, 0.1, 0.1, 0.005, 0.005));

    public static void update() {
        // Update sim drivetrain
        driveSim.setInputs(RobotState.leftMotorVoltage,
                RobotState.rightMotorVoltage);

        driveSim.update(0.02);

        // Get drivetrain state from sim drivetrain
        RobotState.leftDistance = driveSim.getLeftPositionMeters();
        RobotState.rightDistance = driveSim.getRightPositionMeters();
        RobotState.leftVelocity = driveSim.getLeftVelocityMetersPerSecond();
        RobotState.rightVelocity = driveSim.getRightVelocityMetersPerSecond();
        RobotState.angle = -driveSim.getHeading().getDegrees();
    }
}
