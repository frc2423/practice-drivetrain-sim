// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.sql.Time;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {
    private final Field2d field = new Field2d();
    DifferentialDriveOdometry m_odometry = new DifferentialDriveOdometry(
        new Rotation2d());
    private XboxController notAdriansController = new XboxController(0);
    private Timer timber = new Timer();
    private Trajectory trajectory = ExampleTramory.generateTrajectory();

    @Override
    public void robotInit() {
    SmartDashboard.putData("Field", field);
    field.getObject("moo").setTrajectory(trajectory);
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

        m_odometry.update(Rotation2d.fromDegrees(-RobotState.angle), RobotState.leftDistance, RobotState.rightDistance);
        field.setRobotPose(m_odometry.getPoseMeters());
    }

    @Override
    public void autonomousInit() {
        // timber.reset();
        // timber.start();
    }

    @Override
    public void autonomousPeriodic() {
    //if ( RobotState.angle >= 90){
    //RobotState.leftMotorVoltage = 0;
    //RobotState.rightMotorVoltage = 0;
    //}
    //else {
    //RobotState.leftMotorVoltage = .7;
    //RobotState.rightMotorVoltage = -.7;
        // if (timber.get() >= 5){
        //     RobotState.leftMotorVoltage = 0;
        //     RobotState.rightMotorVoltage = 0;
        // }
        // else {
        //     RobotState.leftMotorVoltage = 4.7;
        //     RobotState.rightMotorVoltage = -4.7;
        // }
    // if (RobotState.leftDistance >= 10){
    // RobotState.leftMotorVoltage = 0;
    // RobotState.rightMotorVoltage = 0;
    // }
    // else {
    // RobotState.leftMotorVoltage = 4.7;
    // RobotState.rightMotorVoltage = 4.7;
    //}
    }
    

    @Override
    public void teleopPeriodic() {
    RobotState.leftMotorVoltage = -notAdriansController.getLeftY() * RobotController.getBatteryVoltage(); 
    RobotState.rightMotorVoltage = -notAdriansController.getRightY() * RobotController.getBatteryVoltage(); 
    }
}