// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {
    XboxController xbox = new XboxController(0);
    private final Field2d field = new Field2d();
    DifferentialDriveOdometry odometry = new DifferentialDriveOdometry(new Rotation2d());
    Trajectories traj = new Trajectories();
    RamseteController RAMS = new RamseteController();
    Trajectory tra;
    Timer time = new Timer();
    DifferentialDriveKinematics kinematics;

    public void robotInit() {
      SmartDashboard.putData("Field", field);
      tra = traj.generateTrajectory();
      field.getObject("traj").setTrajectory(tra);
      kinematics = new DifferentialDriveKinematics(Constants.TRACK_WIDTH);
    }



    private Rotation2d getGyroHeading() {
        return null;
    }

    @Override
    public void robotPeriodic() {
        if (this.isDisabled()) {
            return;
        }

        if (isSimulation()) {
            Sim.update();
        } else {
            Real.update();
        }
        odometry.update(Rotation2d.fromDegrees(-RobotState.angle), RobotState.leftDistance, RobotState.rightDistance);
        field.setRobotPose(odometry.getPoseMeters()); 
    }

    @Override
    public void autonomousInit() {
time.start();
odometry.resetPosition(tra.getInitialPose(), tra.getInitialPose().getRotation());
    }

    @Override
    public void autonomousPeriodic() {
        
        Trajectory.State goal = tra.sample(time.get()); // sample the trajectory at 3.4 seconds from the beginning
        ChassisSpeeds adjustedSpeeds = RAMS.calculate(odometry.getPoseMeters(), goal);
        DifferentialDriveWheelSpeeds wheelSpeeds = kinematics.toWheelSpeeds(adjustedSpeeds);
        double left = wheelSpeeds.leftMetersPerSecond;
        double right = wheelSpeeds.rightMetersPerSecond;
        RobotState.leftMotorVoltage = left;
        RobotState.rightMotorVoltage = right;
    }

    @Override
    public void teleopPeriodic() {
      RobotState.leftMotorVoltage = -xbox.getLeftY()*RobotController.getBatteryVoltage();
      RobotState.rightMotorVoltage = -xbox.getRightY()*RobotController.getBatteryVoltage();
    }
}
