# Practice Drivetrain Simulation

## Step 1

Since we don't have a real drivetrain we need some representation of it in code in order to run our simulation. For a differential drivetrain (the type of drivetrain we used for our 2022 competition) we use the `DifferentialDrivetrainSim` class and pass in parameters that describe the physical characteristics of our particular robot:

```java
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
```

More on this can be found here: https://docs.wpilib.org/en/stable/docs/software/wpilib-tools/robot-simulation/drivesim-tutorial/drivetrain-model.html

## Step 2

Just like with a real drivetrain, our simulated drivetrain must take some input in order to change its current state. What are the inputs and how can we describe the current state of the drivetrain?

Once you have identified the inputs update the `Sim.update` method so it does the following:

1. Update the drivetrain simulation model by passing in the input values
2. Advance the model forward .02 seconds (the period of our robot loop)
3. Get the latest state from the model.

## Step 3

Having some code that describes our physical robot is great, but for the simulation to be useful we really need to see it changing on the screen. Run the simulator. You should see something like this:

![](./images/sim-no-field-robot.png)

The field's there but where's the robot?! To make the robot show up we need to add the following code to our `Robot.java` file:

```java
private final Field2d field = new Field2d();

public void robotInit() {
  ...
  SmartDashboard.putData("Field", field);
}
```

We also need to keep track of our robot's position and update it in the field. We do that by create an odometry object for our differential drivetrain. More information on this can be found here: https://docs.wpilib.org/en/stable/docs/software/kinematics-and-odometry/differential-drive-odometry.html?highlight=odometry

We can then update the field2d robot in the sim by periodically calling the `odometry.update` method and then passing in the current robot pose into the field object:

```java
field.setRobotPose(odometry.getPoseMeters());
```

More Information on the field2d widget can be found here: https://docs.wpilib.org/en/stable/docs/software/dashboards/glass/field2d-widget.html

## Step 4

Let's now make the robot actually move in the simulator! Let's make it move using an Xbox Controller during the teleop period using tank drive.

Information on the XboxController class can be found here:
https://docs.wpilib.org/en/stable/docs/software/basic-programming/joystick.html?highlight=xbox%20controller#xboxcontroller-class
https://first.wpi.edu/wpilib/allwpilib/docs/release/java/edu/wpi/first/wpilibj/XboxController.html

Information on tank drive and other drive modes can be found here: https://docs.wpilib.org/en/stable/docs/software/hardware-apis/motors/wpi-drive-classes.html?highlight=tank%20drive#drive-modes

Remember the inputs that are needed to change the drivetrain's state! The input variables are the things you need to change in order to make your robot move.

## Step 5

Now let's create a simulated autonomous mode. Create an autonomous mode where the robot moves forward 10 feet and then stops. The WPILib classes and our state variables uses meters, so we will have to convert from feet to meters. You'll want to use the `Units` class to do those conversions: https://first.wpi.edu/wpilib/allwpilib/docs/release/java/edu/wpi/first/math/util/Units.html

## Step 6

Now let's create a second autonomous mode where the robot rotates in place for 5 seconds then stops. We want to be able to choose between different auto modes, so don't remove your auto mode code from step 5! We can select different auto modes by creating a `SendableChooser` in our `Robot.java` file:

```java
  // Create the sendable chooser
  private final SendableChooser<String> autoChooser = new SendableChooser<>();
  
  @Override
  public void robotInit() {
    ...
    // Set the default auto
    autoChooser.setDefaultOption("Auto 1", "Auto 1");
    // Add additional auto modes
    autoChooser.addOption("Auto 2", "Auto 2");
    SmartDashboard.putData("Auto choices", autoChooser);
  }
  
   @Override
  public void autonomousPeriodic() {
    var selected = autoChooser.getSelected();
    if (selected.equals("Auto 1")) {
      ...
    } else if (selected.equals("Auto 2")) {
      ...
    }
  }
```

We can change the auto mode while running the robot code using the `Sendable Chooser Widget` in `Glass`: https://docs.wpilib.org/en/stable/docs/software/dashboards/glass/widgets.html?highlight=sendable%20chooser#sendable-chooser-widget

## Step 7

Knowing how to manually tell our robot to move forward distances and rotate is great, but during competitions its far faster to get to different places on the field using `trajectories`. An explanation on trajectories can be found here: https://docs.wpilib.org/en/stable/docs/software/pathplanning/trajectory-tutorial/trajectory-tutorial-overview.html#why-trajectory-following

To learn how to generate trajectories follow this guide here: https://docs.wpilib.org/en/stable/docs/software/advanced-controls/trajectories/trajectory-generation.html
