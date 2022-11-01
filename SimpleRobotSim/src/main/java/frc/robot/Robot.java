// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {

    private Field2d field = new Field2d();
    private final DifferentialDriveOdometry odometry = new DifferentialDriveOdometry(new Rotation2d());

    @Override
    public void robotInit() {
        SmartDashboard.putData("Field", field);
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

        odometry.update(Rotation2d.fromDegrees(-RobotState.angle),
                RobotState.leftDistance,
                RobotState.rightDistance);

        field.setRobotPose(odometry.getPoseMeters());
    }

    @Override
    public void autonomousInit() {

    }

    @Override
    public void autonomousPeriodic() {

    }

    @Override
    public void teleopPeriodic() {

    }
}
