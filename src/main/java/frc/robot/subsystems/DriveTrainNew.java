package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.DriveConstants;
import frc.robot.SyncedLibraries.SystemBases.DriveTrainBase;

public class DriveTrainNew extends DriveTrainBase {
  private final static CANSparkMax[] leftDriveMotorsInput = new CANSparkMax[] {
      new CANSparkMax(1, CANSparkMax.MotorType.kBrushless),
      new CANSparkMax(2, CANSparkMax.MotorType.kBrushless) };
  private final static CANSparkMax[] rightDriveMotorsInput = new CANSparkMax[] {
      new CANSparkMax(3, CANSparkMax.MotorType.kBrushless),
      new CANSparkMax(4, CANSparkMax.MotorType.kBrushless) };

  public DriveTrainNew() {
    super(leftDriveMotorsInput, rightDriveMotorsInput, PneumaticsModuleType.CTREPCM,
        new int[] {}, false,
        DriveConstants.drivingMax, DriveConstants.driveAmpsMax, DriveConstants.drivingRamp,
        DriveConstants.WHEEL_DIAMETER, DriveConstants.PULSES_PER_REVOLUTION, true);
    invertAll();
  }

  @Override
  public void resetAll() {
    System.out.println("Resetting DriveTrain");
    stop();
    resetEncoders();
    resetGyro();
    doSlowMode(false);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Left Drive Speed", getLeftSpeed());
    SmartDashboard.putNumber("Right Drive Speed", getRightSpeed());
  }
}
