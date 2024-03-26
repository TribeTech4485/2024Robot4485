package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants.DeviceConstants;
import frc.robot.SyncedLibraries.SystemBases.ManipulatorBase;

public class Intake extends ManipulatorBase {
  private static final double kP = DeviceConstants.intakeSpeedKP;
  private static final double kI = DeviceConstants.intakeSpeedKI;
  private static final double kD = DeviceConstants.intakeSpeedKD;
  private static final double tolerance = DeviceConstants.intakeSpeedTolerance;

  public Intake() {
    addMotors(new CANSparkMax(DeviceConstants.intakeMotorId, CANSparkMax.MotorType.kBrushless));

    setSpeedPID(kP, kI, kD, tolerance);
    // setSpeedMultiplier(1);
    setBrakeMode(false);
    setCurrentLimit(DeviceConstants.intakeAmpsMax);
  }

  public void sendIt(int speed) {
    setPositionPID(kP, kI, kD, tolerance);
    setTargetSpeed(speed);
    // getSpeedCommand().andThen(new InstantCommand(() -> fullStop()));
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Intake power", getAvePower());
    SmartDashboard.putNumber("Intake speed", getCurrentSpeed());
    SmartDashboard.putNumber("Intake target", getSpeedCommand().getTargetSpeed());
  }

  @Override
  public void ESTOP() {
    setBrakeMode(true);
    fullStop();
  }

  @Override
  public Command test() {
    return new SequentialCommandGroup(
        new InstantCommand(() -> sendIt(1000)),
        new WaitCommand(1),
        new InstantCommand(() -> fullStop()));
  }
}
