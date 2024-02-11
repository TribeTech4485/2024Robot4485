package frc.robot;

import org.photonvision.PhotonCamera;
import edu.wpi.first.net.PortForwarder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.SyncedLibraries.Controllers;
import frc.robot.SyncedLibraries.Controllers.ControllerBase;
import frc.robot.SyncedLibraries.SystemBases.DriveTrainBase;
import frc.robot.SyncedLibraries.SystemBases.TeleDriveCommandBase;
import frc.robot.commands.DriveTrainCamCommand;
import frc.robot.SyncedLibraries.RobotState.*;
import frc.robot.subsystems.*;

public class Robot extends TimedRobot {
  public static RobotContainer m_robotContainer;
  public static DriveTrainBase DriveTrain;
  public static Intake Intake;
  public static Shooter Shooter;
  public static Turret Turret;
  public static PhotonCamera camera = new PhotonCamera("Microsoft_LifeCam_HD-3000");

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

  public static Command AutonomousCommand;
  /** NO TOUCH, READ ONLY */
  public static TeleDriveCommandBase TeleDriveCommand;
  public static DriveTrainCamCommand CamCommand;
  public static SequentialCommandGroup shootCommand;

  static int countgujhbgnu = 0;

  @Override
  public void robotInit() {
    System.out.println("Robot Initializing");
    PortForwarder.add(5800, "10.44.85.21", 5800);
    PortForwarder.add(1182, "10.44.85.21", 1182);

    DriverStation.silenceJoystickConnectionWarning(true);
    UpdateControllers();

    m_robotContainer = new RobotContainer();
    AutonomousCommand = m_robotContainer.getAutonomousCommand();
    DriveTrain = new DriveTrainNew();
    DriveTrain.resetAll();
    DriveTrain.invertAll();
    TeleDriveCommand = new TeleDriveCommandBase(Zero, Two, Three);
    CamCommand = new DriveTrainCamCommand();
    Intake = new Intake();
    Shooter = new Shooter();
    Turret = new Turret();
    camera.setPipelineIndex(0);
    camera.setDriverMode(false);
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
    SmartDashboard.putNumber("Shooter speed:", Intake.getAvePower());
    if (countgujhbgnu++ % 50 == 0) {
      // logStuff();
    }
  }

  @Override
  public void disabledInit() {
    System.out.println("Robot Disabled");
    if (AutonomousCommand != null) {
      AutonomousCommand.cancel();
    }
    if (TeleDriveCommand != null) {
      TeleDriveCommand.cancel();
    }
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
    // DriveTrain.setDefaultCommand(TeleDriveCommand);
    TeleDriveCommand.schedule();
    // CommandScheduler.getInstance().getActiveButtonLoop().clear();
  }

  @Override
  public void teleopPeriodic() {
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
    // Turret.home();
  }

  @Override
  public void testPeriodic() {
  }

  @Override
  public void simulationInit() {
    System.out.println("Robot in Simulation Mode");
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
    DriverStation.reportError("KILLING IT", true);
    // Professor X, add whatever your plan is to stop the robot here
    DriveTrain.setBrakeMode(true);
    Shooter.setBrakeMode(true);
    Intake.setBrakeMode(true);
    Turret.setBrakeMode(true);
    DriveTrain.stop();
    Shooter.stop();
    Intake.stop();
    Turret.stop();

    DriverStation.reportError("KILLED IT, EXITING NOW", true);
    System.exit(0);
  }

  private void UpdateControllers() {
    m_controllers = new Controllers(
        Constants.DriveConstants.controllerJoystickDeadband,
        Constants.DriveConstants.controllerTriggerDeadband);
    m_controllers.fullUpdate();
    Zero = m_controllers.Zero;
    One = m_controllers.One;
    Two = m_controllers.Two;
    Three = m_controllers.Three;
    Three.setJoystickMultiplier(0.5);
    Four = m_controllers.Four;
    Five = m_controllers.Five;
  }
}
