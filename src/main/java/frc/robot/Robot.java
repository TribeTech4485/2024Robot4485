package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.SyncedLibraries.Controllers;
import frc.robot.SyncedLibraries.Controllers.ControllerBase;
import frc.robot.SyncedLibraries.SystemBases.DriveTrainBase;
import frc.robot.SyncedLibraries.SystemBases.TeleDriveCommandBase;
import frc.robot.SyncedLibraries.RobotState.*;
import frc.robot.subsystems.*;

public class Robot extends TimedRobot {
  public static RobotContainer m_robotContainer;
  public static Command AutonomousCommand;
  public static DriveTrainBase DriveTrain;
  public static Intake Intake;
  public static Shooter Shooter;
  public static Turret Turret;

  /**
   * The current state of the robot
   * <p>
   * KEEP UPDATED
   */
  public static RobotStateEnum robotState = RobotStateEnum.Disabled;
  public static ManipulatorStateEnum manipulatorState = ManipulatorStateEnum.Empty;

  private static Controllers m_controllers;
  /** Driver */
  public static ControllerBase Zero;
  /** Copiolot */
  public static ControllerBase One;
  /** One-man show */
  public static ControllerBase Two;
  /** Guest controller */
  public static ControllerBase Three;
  /** UNUSED */
  public static ControllerBase Four;
  /** UNUSED */
  public static ControllerBase Five;

  private static TeleDriveCommandBase TeleDriveCommand = new TeleDriveCommandBase(Zero, Two, Three);

  @Override
  public void robotInit() {
    System.out.println("Robot Initializing");

    DriverStation.silenceJoystickConnectionWarning(true);
    m_controllers = new Controllers(Constants.DriveConstants.controllerJoystickDeadband,
        Constants.DriveConstants.controllerTriggerDeadband);
    m_controllers.fullUpdate();

    m_robotContainer = new RobotContainer();
    AutonomousCommand = m_robotContainer.getAutonomousCommand();
    DriveTrain = new DriveTrainNew();
    DriveTrain.resetAll();
    Intake = new Intake();
    Shooter = new Shooter();
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
    SmartDashboard.putNumber("Shooter speed:", Intake.getAvePower());
  }

  @Override
  public void disabledInit() {
    System.out.println("Robot Disabled");
    CommandScheduler.getInstance().cancel(TeleDriveCommand);
    CommandScheduler.getInstance().cancel(m_robotContainer.getAutonomousCommand());
  }

  @Override
  public void disabledPeriodic() {
    robotState = RobotStateEnum.Disabled;
  }

  @Override
  public void autonomousInit() {
    System.out.println("Robot in Autonomous Mode");

    AutonomousCommand = m_robotContainer.getAutonomousCommand();
    if (AutonomousCommand != null) {
      AutonomousCommand.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    System.out.println("Robot in Teleoperated Mode");

    // m_controllers.fullUpdate();
    // m_robotContainer.configureBindings();
    if (AutonomousCommand != null) {
      AutonomousCommand.cancel();
    }

    // start main driving command
    CommandScheduler.getInstance().schedule(TeleDriveCommand);
  }

  @Override
  public void teleopPeriodic() {
    SmartDashboard.putBoolean("B pressed", Zero.B.get().getAsBoolean());
  }

  @Override
  public void testInit() {
    System.out.println("Robot in Test Mode");

    CommandScheduler.getInstance().cancelAll();
    m_controllers.fullUpdate();
    m_robotContainer.configureBindings();
    Robot.DriveTrain.resetAll();
    // Home all motors and sensors
    // spin up shooter
    // turn on intake
  }

  @Override
  public void testPeriodic() {
  }

  @Override
  public void simulationInit() {
    System.out.println("Robot in Simulation Mode");

    m_controllers.fullUpdate();
    m_robotContainer.configureBindings();
  }

  @Override
  public void simulationPeriodic() {
  }

  /**
   * To run:
   * <p>
   * Robot.doOnAllControllers((controller) -> {});
   */
  public static void doOnAllControllers(ControllerRunnable r) {
    r.run(Zero);
    r.run(One);
    r.run(Two);
    r.run(Three);
    r.run(Four);
    r.run(Five);
  }

  /** Ignore this thing */
  public static interface ControllerRunnable {
    public void run(ControllerBase controller);
  }

  public static void KILLIT() {
    System.out.println("KILLING IT");
    // Professor X, add whatever your plan is to stop the robot here

    System.exit(0);
  }
}
