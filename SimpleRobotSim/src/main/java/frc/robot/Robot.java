// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.MatBuilder;
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.Nat;
import edu.wpi.first.math.controller.LinearPlantInversionFeedforward;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N2;
import edu.wpi.first.math.system.LinearSystem;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class Robot extends TimedRobot {

  private final SendableChooser<String> autoChooser = new SendableChooser<>();

  private Field2d field = new Field2d();
  private final DifferentialDriveOdometry odometry = new DifferentialDriveOdometry(new Rotation2d());
  private final DifferentialDriveKinematics kinematics = new DifferentialDriveKinematics(2);
  private final LinearSystem<N2, N2, N2> m_drivetrainSystem = LinearSystemId.identifyDrivetrainSystem(
      Constants.KvLinear,
      Constants.KaLinear, Constants.KvAngular, Constants.KaAngular);
  private final LinearPlantInversionFeedforward<N2, N2, N2> feedforward = new LinearPlantInversionFeedforward<>(
      m_drivetrainSystem, .02);
  private XboxController controller = new XboxController(0);
  RamseteController ramseteController = new RamseteController();
  private Timer autoTimer = new Timer();

  @Override
  public void robotInit() {
    autoChooser.setDefaultOption(Constants.GO_FORWARD_AUTO, Constants.GO_FORWARD_AUTO);
    autoChooser.addOption(Constants.TRAVEL_PATH_AUTO, Constants.TRAVEL_PATH_AUTO);
    SmartDashboard.putData("Auto choices", autoChooser);
    SmartDashboard.putData("Field", field);
    field.getObject("trajectory4").setTrajectory(Trajectories.trajectory);
    System.out.println(getPeriod());
  }

  private void setWheelVoltages(DifferentialDriveWheelSpeeds speeds) {
    Matrix<N2, N1> voltages = feedforward.calculate(
        new MatBuilder<>(
            Nat.N2(),
            Nat.N1())
                .fill(
                  speeds.leftMetersPerSecond,
                  speeds.rightMetersPerSecond));
    double leftVoltage = voltages.get(0, 0);
    double rightVoltage = voltages.get(1, 0);

    RobotState.leftMotorVoltage = leftVoltage;
    RobotState.rightMotorVoltage = rightVoltage;
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
    autoTimer.start();
    autoTimer.reset();
  }

  @Override
  public void autonomousPeriodic() {
    var selected = autoChooser.getSelected();
    if (selected.equals(Constants.GO_FORWARD_AUTO)) {
      if (RobotState.leftDistance < 5) {
        RobotState.leftMotorVoltage = .5 * RobotController.getInputVoltage();
        RobotState.rightMotorVoltage = .5 * RobotController.getInputVoltage();
      } else {
        RobotState.leftMotorVoltage = 0;
        RobotState.rightMotorVoltage = 0;
      }
    } else if (selected.equals(Constants.TRAVEL_PATH_AUTO)) {
      var goal = Trajectories.trajectory.sample(autoTimer.get());
      var currentPose = odometry.getPoseMeters();
      var speeds = ramseteController.calculate(currentPose, goal);
      var wheelSpeeds = kinematics.toWheelSpeeds(speeds);
      setWheelVoltages(wheelSpeeds);
    }
  }

  @Override
  public void teleopPeriodic() {
    RobotState.leftMotorVoltage = -controller.getLeftY() * RobotController.getInputVoltage();
    RobotState.rightMotorVoltage = -controller.getRightY() * RobotController.getInputVoltage();
  }
}
