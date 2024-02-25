package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
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
    SmartDashboard.putNumber("Shooter target", defaultSpeed);
  }

  public void sedPID(double target) {
    setSpeedPID(kP, kI, kD, tolerance);
    getSpeedCommand().setTargetSpeed(target);
    getSpeedCommand().schedule();
    // getSpeedCommand().setEndOnTarget(true).andThen(new InstantCommand(() ->
    // fullStop()));
  }

  /**
   * Sets target speed and schedules it
   * <p>
   * What you want for normal use
   */
  public void shoot() {
    getSpeedCommand().setTargetSpeed(SmartDashboard.getNumber("Shooter target", defaultSpeed));
    getSpeedCommand().schedule();
  }

  /** Sets target speed and returns command for use in Command groups */
  public ManipulatorSpeedCommand shootCommand() {
    getSpeedCommand().setTargetSpeed(SmartDashboard.getNumber("Shooter target", defaultSpeed));
    return getSpeedCommand();
  }

  public void adjust() {
    getSpeedCommand().setTargetSpeed(SmartDashboard.getNumber("Shooter target", 0));
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Shooter speed: ", getCurrentSpeed());
    SmartDashboard.putNumber("Shooter power: ", getAvePower());
    SmartDashboard.putBoolean("Shooter at speed: ", getSpeedCommand() != null ? getSpeedCommand().atSpeed : false);
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
      shootCommand().setEndOnTarget(true),
      new InstantCommand(() -> System.out.println("Shoot")),
      new WaitCommand(1),
      new InstantCommand(() -> System.out.println("stop")),
      new InstantCommand(() -> fullStop()),
      new InstantCommand(() -> System.out.println("stoped"))
    );
  }
}
