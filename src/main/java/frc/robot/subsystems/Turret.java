package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Constants.DeviceConstants;
import frc.robot.SyncedLibraries.SystemBases.ManipulatorBase;

/* 
 * For the positional angle, the turret is 0 degrees when 
 * it is level with the ground. <br>
 * AKA the mathematical angle when facing towards
 * the right side of the robot.
 */

public class Turret extends ManipulatorBase {
  private static final double kP = DeviceConstants.turretPositionKP;
  private static final double kI = DeviceConstants.turretPositionKI;
  private static final double kD = DeviceConstants.turretPositionKD;
  private static final double tolerance = DeviceConstants.turretPositionTolerance;

  public Turret() {
    addMotors(new CANSparkMax(DeviceConstants.turretMotorId, CANSparkMax.MotorType.kBrushless));

    setPositionMultiplier(DeviceConstants.turretPositionMultiplier);
    setBrakeMode(true);
    setPositionPID();
    setMaxPower(DeviceConstants.turretMaxPower);
    setCurrentLimit(20);
  }

  public void sendIt(int angle) {
    setPositionPID();
    moveToPosition(angle);
  }

  public void setPositionPID() {
    setPositionPID(kP, kI, kD, tolerance);
  }

  public void sudoMode(boolean on) {
    if (on) {
      setMaxPower(1);
      for (CANSparkMax motor : getMotors()) {
        motor.setSmartCurrentLimit(40);
      }
    } else {
      setMaxPower(DeviceConstants.turretMaxPower);
      for (CANSparkMax motor : getMotors()) {
        motor.setSmartCurrentLimit(20);
      }
    }
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Turret position", getPosition());
    SmartDashboard.putNumber("Turret target", getMoveCommand().getTargetPosition());
  }

  @Override
  public Command home() {
    _setPosition(38);
    System.out.println("Turret NEEDS HOMED!");
    return new InstantCommand();
  }

  @Override
  public void ESTOP() {
    setBrakeMode(true);
    fullStop();
  }

  @Override
  public Command test() {
    System.out.println("hiii");
    setPositionPID();
    System.out.println("hi");
    return getMoveCommand().setTargetPosition(0);
  }

  public void adjustTargetPos(double adjustment) {
    getMoveCommand().setTargetPosition(getMoveCommand().getTargetPosition() + adjustment);
  }
}
