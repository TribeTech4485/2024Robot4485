package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.SyncedLibraries.Controllers;
import frc.robot.SyncedLibraries.Controllers.ControllerBase;
import frc.robot.SyncedLibraries.SystemBases.DriveTrainBase;
import frc.robot.SyncedLibraries.SystemBases.LimelightBase;
import frc.robot.SyncedLibraries.SystemBases.TeleDriveCommandBase;
import frc.robot.SyncedLibraries.RobotState.*;
import frc.robot.subsystems.DriveTrainNew;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;

public class Robot extends TimedRobot {
  public static RobotContainer m_robotContainer;
  public static Command AutonomousCommand;
  public static DriveTrainBase DriveTrain;
  public static LimelightBase Limelight;
  public static Intake Intake;
  public static Shooter Shooter;

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

  /** Person controlling driving */
  public static ControllerBase Primary;
  // public static ControllerBase Primary = m_controllers.One;
  /** Person controlling shooting */
  public static ControllerBase Secondary;
  // public static ControllerBase Secondary = m_controllers.Two;

  @Override
  public void robotInit() {
    System.out.println("Robot Initializing");

    DriverStation.silenceJoystickConnectionWarning(true);
    m_controllers = new Controllers(Constants.DriveConstants.controllerJoystickDeadband,
      Constants.DriveConstants.controllerTriggerDeadband);
    m_controllers.fullUpdate();
    updateAutoControllers();
    // Three.setJoystickMultiplier(0.5);
    // m_controllers.addControllersToSelector(m_controllers.primaryControllerSelector, 0, 2, 3);
    // m_controllers.addControllersToSelector(m_controllers.secondaryControllerSelector, 1, 2, 3);
    // m_controllers.updateAutoControllers();

    m_robotContainer = new RobotContainer();
    AutonomousCommand = m_robotContainer.getAutonomousCommand();
    DriveTrain = new DriveTrainNew();
    DriveTrain.resetAll();
    DriveTrain.setDefaultCommand(new TeleDriveCommandBase());
    Limelight = new LimelightBase();
    Intake = new Intake();
    Shooter = new Shooter();
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
    // m_controllers.updateAutoControllers();
    // m_robotContainer.configureBindings();
    SmartDashboard.putNumber("Shooter speed:", Intake.getAvePower());
  }

  @Override
  public void disabledInit() {
    System.out.println("Robot Disabled");
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
    CommandScheduler.getInstance().schedule(new TeleDriveCommandBase());
  }

  @Override
  public void teleopPeriodic() {
    SmartDashboard.putBoolean("B pressed", Zero.B.getAsBoolean());
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

  /** TO ONLY BE CALLED BY m_controllers OBJECT */
  public static void updateAutoControllers() {
    Primary = m_controllers.Primary;
    Secondary = m_controllers.Secondary;

    Zero = m_controllers.Zero;
    One = m_controllers.One;
    Two = m_controllers.Two;
    Three = m_controllers.Three;
    Four = m_controllers.Four;
    Five = m_controllers.Five;
  }
}
