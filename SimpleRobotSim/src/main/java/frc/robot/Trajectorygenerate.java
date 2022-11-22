package frc.robot;

import java.util.ArrayList;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.util.Units;

public class Trajectorygenerate {
    public static Trajectory generateTrajectory() {
    
        // 2018 cross scale auto waypoints.
        var start = new Pose2d(Units.feetToMeters(0), Units.feetToMeters(0),
            Rotation2d.fromDegrees(0));
        var end = new Pose2d(Units.feetToMeters(0), Units.feetToMeters(0),
            Rotation2d.fromDegrees(-180));
    
        var interiorWaypoints = new ArrayList<Translation2d>();
        interiorWaypoints.add(new Translation2d(Units.feetToMeters(0), Units.feetToMeters(15)));
        interiorWaypoints.add(new Translation2d(Units.feetToMeters(0), Units.feetToMeters(30)));
        interiorWaypoints.add(new Translation2d(Units.feetToMeters(15), Units.feetToMeters(30)));
        interiorWaypoints.add(new Translation2d(Units.feetToMeters(30), Units.feetToMeters(30)));
        interiorWaypoints.add(new Translation2d(Units.feetToMeters(30), Units.feetToMeters(15)));
        interiorWaypoints.add(new Translation2d(Units.feetToMeters(30), Units.feetToMeters(0)));
        interiorWaypoints.add(new Translation2d(Units.feetToMeters(15), Units.feetToMeters(0)));

    
        TrajectoryConfig config = new TrajectoryConfig(Units.feetToMeters(12), Units.feetToMeters(12));
        config.setReversed(true);
    
        var trajectory = TrajectoryGenerator.generateTrajectory(
            start,
            interiorWaypoints,
            end,
            config);
            return trajectory;
    }
}
