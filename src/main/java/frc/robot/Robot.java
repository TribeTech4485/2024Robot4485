package frc.robot;

import org.photonvision.PhotonCamera;
import edu.wpi.first.net.PortForwarder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.SyncedLibraries.AutoControllerSelector;
import frc.robot.SyncedLibraries.Controllers;
import frc.robot.SyncedLibraries.Controllers.ControllerBase;
import frc.robot.SyncedLibraries.SystemBases.*;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
import frc.robot.SyncedLibraries.RobotState.*;

public class Robot extends TimedRobot {
  public static RobotContainer RobotContainer;
  public static DriveTrain2024 DriveTrain;
  public static Intake Intake;
  public static Shooter Shooter;
  public static Turret Turret;
  public static Conveyor Conveyor;
  public static PhotonVisionBase PhotonVision;
  public static PowerDistribution PDP;
  public static LedBase LED;

  public static Command AutonomousCommand;
  public static TeleDriveCommandBase TeleDriveCommand;
  public static Command CamCommand = new DriveTrainTurnCamCommand();
  public static FullShootCameraCommands shootCommand;

  public static RobotStateEnum robotState = RobotStateEnum.Disabled;
  public static ManipulatorStateEnum manipulatorState = ManipulatorStateEnum.Empty;

  private static Controllers m_controllers;
  public static ControllerBase Zero;
  public static ControllerBase One;
  public static ControllerBase Two;
  public static ControllerBase Three;
  public static ControllerBase Four;
  public static ControllerBase Five;
  public static AutoControllerSelector DrivingContSelector;
  public static AutoControllerSelector SecondaryContSelector;

  @Override
  public void robotInit() {
    System.out.println("Robot Initializing");
    PortForwarder.add(5800, "10.44.85.21", 5800);
    PortForwarder.add(1182, "10.44.85.21", 1182);

    DriverStation.silenceJoystickConnectionWarning(true);
    UpdateControllers();

    Shooter = new Shooter();
    RobotContainer = new RobotContainer();
    DriveTrain = new DriveTrain2024();
    PhotonVision = new PhotonVision2024(new PhotonCamera("Microsoft_LifeCam_HD-3000"));
    AutonomousCommand = RobotContainer.getAutonomousCommand();
    TeleDriveCommand = new TeleDriveCommand2024(DrivingContSelector, SecondaryContSelector);
    PDP = new PowerDistribution(20, ModuleType.kRev);
    CamCommand = new DriveTrainTurnCamCommand(TeleDriveCommand).setEndOnTarget(true);
    Intake = new Intake();
    Turret = new Turret();
    Conveyor = new Conveyor();
    LED = new LedBase(0, 20, 20, 20);
    LED.sections[0].init(1, 2, Color.kRed, new Color(0, 255, 0), Color.kBlue).doMoveForward();
    LED.sections[1].init(1, 1).doOff();
    LED.sections[2].init(3, 1, Color.kRed, new Color(0, 255, 0), Color.kBlue).doMoveBackward();
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
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

    if (DriverStation.isEStopped()) {
      KILLIT();
    }
  }

  @Override
  public void disabledPeriodic() {
    robotState = RobotStateEnum.Disabled;
  }

  @Override
  public void autonomousInit() {
    System.out.println("Robot in Autonomous Mode");

    AutonomousCommand = RobotContainer.getAutonomousCommand();
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
    RobotContainer.configureBindings();
    Robot.DriveTrain.resetAll();

    Command[] testCommands = new Command[ManipulatorBase.allManipulators.size() + 1];
    for (int i = 0; i < ManipulatorBase.allManipulators.size(); i++) {
      ManipulatorBase subsystem = ManipulatorBase.allManipulators.get(i);
      System.out.println("Adding " + subsystem.getName());
      testCommands[i] = new SequentialCommandGroup(
          new PrintCommand("Testing " + subsystem.getName()),
          subsystem.home().withTimeout(5),
          subsystem.test().withTimeout(5),
          new PrintCommand("Testing of " + subsystem.getName() + " finished"));
    }
    testCommands[testCommands.length - 1] = new InstantCommand(
        () -> DriverStation.reportWarning("Finished testing routine", false));
    new SequentialCommandGroup(testCommands).schedule();
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
    System.out.println("Doing on all controllers");
    doOnControllers(r, 0, 1, 2, 3, 4, 5);
  }

  public static void doOnControllers(ControllerRunnable r, int... controllers) {
    for (int i : controllers) {
      switch (i) {
        case 0:
          r.run(Zero);
          break;
        case 1:
          r.run(One);
          break;
        case 2:
          r.run(Two);
          break;
        case 3:
          r.run(Three);
          break;
        case 4:
          r.run(Four);
          break;
        case 5:
          r.run(Five);
          break;
      }
    }
  }

  /** Ignore this thing */
  public static interface ControllerRunnable {
    public void run(ControllerBase controller);
  }

  /**
   * <b>ONLY FOR USE IN EMERGENCY</b>
   * <p>
   * YES, ACTUALLY
   */
  public static void KILLIT() {
    DriverStation.reportError("KILLING IT", true);
    for (ManipulatorBase manipulator : ManipulatorBase.allManipulators) {
      DriverStation.reportWarning("ESTOP " + manipulator.getName(), false);
      manipulator.ESTOP();
      System.out.println("Done");
    }

    DriverStation.reportWarning("ESTOP DriveTrain", false);
    DriveTrain.ESTOP();
    System.out.println("Done");

    DriverStation.reportError("KILLED IT!!", false);
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

    DrivingContSelector = new AutoControllerSelector(m_controllers);
    DrivingContSelector.addController(0, 2, 3);
    SecondaryContSelector = new AutoControllerSelector(m_controllers);
    SecondaryContSelector.addController(1, 2, 3);
  }

  public static Command getCamCommand() {
    return CamCommand;
  }
}
