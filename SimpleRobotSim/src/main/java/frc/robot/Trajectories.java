package frc.robot;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.util.Units;

public class Trajectories {

    static TrajectoryConfig config = new TrajectoryConfig(Units.feetToMeters(12), Units.feetToMeters(12));
    static TrajectoryConfig configReversed = new TrajectoryConfig(Units.feetToMeters(12), Units.feetToMeters(12));
    public static Trajectory trajectory;
    static {
        configReversed.setReversed(true);
        var start = new Pose2d(5, 0, Rotation2d.fromDegrees(0));
        var end = new Pose2d(11, 5, Rotation2d.fromDegrees(90));
       
        trajectory = TrajectoryGenerator.generateTrajectory(
                start,
                List.of(
                    new Translation2d(8, 5),
                    new Translation2d(10, 7)
                ),
                end,
                config);
    }
}
