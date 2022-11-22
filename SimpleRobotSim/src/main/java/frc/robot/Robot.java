// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

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
    DifferentialDriveOdometry m_odometry = new DifferentialDriveOdometry(new Rotation2d());
    private XboxController m_joystick = new XboxController(0);
    private Timer timer = new Timer();
    Boolean flag = false;
    @Override
    public void robotInit() {
        SmartDashboard.putData("Field", field);
        Trajectory your_mother = Trajectorygenerate.generateTrajectory();
        field.getObject("your_mother").setTrajectory(Trajectorygenerate.generateTrajectory());
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
        m_odometry.update(Rotation2d.fromDegrees(-RobotState.angle),RobotState.leftDistance,RobotState.rightDistance);
        field.setRobotPose(m_odometry.getPoseMeters());
       
    }

    @Override
    public void autonomousInit() {
      timer.start();
    }

    
    @Override
    public void autonomousPeriodic() {
        
       
       
        


        }
       
        
      


    @Override
    public void teleopPeriodic() {
    RobotState.leftMotorVoltage = m_joystick.getLeftY()* RobotController.getBatteryVoltage();
    RobotState.rightMotorVoltage = m_joystick.getRightY()* RobotController.getBatteryVoltage();
   
    }
}
