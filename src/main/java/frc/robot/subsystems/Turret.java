package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.DeviceConstants;
import frc.robot.SyncedLibraries.SystemBases.ManipulatorBase;

public class Turret extends ManipulatorBase {
  private static final double kP = DeviceConstants.turretPositionKP;
  private static final double kI = DeviceConstants.turretPositionKI;
  private static final double kD = DeviceConstants.turretPositionKD;
  private static final double tolerance = DeviceConstants.turretPositionTolerance;

  public Turret() {
    addMotors(new CANSparkMax(DeviceConstants.turretMotorId, CANSparkMax.MotorType.kBrushless));

    setPositionMultiplier(DeviceConstants.turretPositionMultiplier);
    setBrakeMode(true);
    setPositionPID(kP, kI, kD, tolerance);
  }

  public void sendIt(int speed) {
    setPositionPID(kP, kI, kD, tolerance);
    moveToPosition(speed);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Turret position", getPosition());
    SmartDashboard.putNumber("Turret target", getMoveCommand().getTargetPosition());
  }

  @Override
  public void home() {
    System.out.println("Turret NEEDS HOMED!");
  }

  @Override
  public void ESTOP() {
    setBrakeMode(true);
    fullStop();
  }
}
