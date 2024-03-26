package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Robot;
import frc.robot.Constants.DeviceConstants;
import frc.robot.SyncedLibraries.SystemBases.ManipulatorBase;
import frc.robot.SyncedLibraries.SystemBases.ManipulatorSpeedCommand;

public class Shooter extends ManipulatorBase {
  private static final double kP = DeviceConstants.shooterSpeedKP;
  private static final double kI = DeviceConstants.shooterSpeedKI;
  private static final double kD = DeviceConstants.shooterSpeedKD;
  private static final double tolerance = DeviceConstants.shooterSpeedTolerance;
  private final int defaultSpeed = DeviceConstants.shooterDefaultSpeed;

  public Shooter() {
    addMotors(new CANSparkMax(DeviceConstants.shooterMotor2Id, MotorType.kBrushless),
        new CANSparkMax(DeviceConstants.shooterMotor1Id, MotorType.kBrushless));
    // setSpeedMultiplier(1);
    invertSpecificMotors(true, 1);
    setBrakeMode(false);
    SmartDashboard.putNumber("Shooter target", SmartDashboard.getNumber("Shooter target", defaultSpeed));
  }

  /** prepare speed command */
  public ManipulatorSpeedCommand prepare(double target) {
    setSpeedPID(kP, kI, kD, tolerance);
    getSpeedCommand().setTargetSpeed(target);
    // getSpeedCommand().setEndOnTarget(true).andThen(new InstantCommand(() ->
    // fullStop()));
    setCurrentLimit(DeviceConstants.shooterAmpsMax);
    setRampRate(0);
    return getSpeedCommand();
  }

  /** prepare speed command */
  public ManipulatorSpeedCommand prepare() {
    return prepare(SmartDashboard.getNumber("Shooter target", defaultSpeed));
  }

  public Command prepareStop() {
    return prepare(SmartDashboard.getNumber("Shooter target", defaultSpeed)).setEndOnTarget(true).withTimeout(5);
  }

  /**
   * Sets target speed and schedules it
   * <p>
   * What you want for normal use
   */
  public ManipulatorSpeedCommand shoot() {
    prepare(SmartDashboard.getNumber("Shooter target", defaultSpeed));
    getSpeedCommand().schedule();
    return getSpeedCommand();
  }

  public ManipulatorSpeedCommand inShoot() {
    prepare(-SmartDashboard.getNumber("Shooter target", defaultSpeed));
    getSpeedCommand().schedule();
    return getSpeedCommand();
  }

  // public void adjust() {
  // getSpeedCommand().setTargetSpeed(SmartDashboard.getNumber("Shooter target",
  // 0));
  // }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Shooter speed: ", getCurrentSpeed());
    SmartDashboard.putNumber("Shooter power: ", getAvePower());
    SmartDashboard.putBoolean("Shooter at speed: ", getSpeedCommand() != null ? getSpeedCommand().atSpeed : false);
    SmartDashboard.putNumber("RPM diff", getEncoder(0).getVelocity() - getEncoder(1).getVelocity());
    SmartDashboard.putNumber("RPM Question", 5000 - getEncoder(0).getVelocity());
  }

  @Override
  public void ESTOP() {
    setBrakeMode(true);
    fullStop();
  }

  @Override
  public Command test() {
    setSpeedPID(kP, kI, kD, tolerance);
    return new SequentialCommandGroup(
        prepare().setEndOnTarget(true),
        // new InstantCommand(() -> System.out.println("Shoot")),
        new WaitCommand(1),
        // new InstantCommand(() -> System.out.println("stop")),
        new InstantCommand(() -> fullStop())
    // new InstantCommand(() -> System.out.println("stoped"))
    );
  }
}
