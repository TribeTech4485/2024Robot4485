package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import frc.robot.Constants.DriveConstants;
import frc.robot.SyncedLibraries.SystemBases.DriveTrainBase;

public class DriveTrain2024 extends DriveTrainBase {
  private final static CANSparkMax[] leftDriveMotorsInput = new CANSparkMax[] {
      new CANSparkMax(1, CANSparkMax.MotorType.kBrushless),
      new CANSparkMax(2, CANSparkMax.MotorType.kBrushless) };
  private final static CANSparkMax[] rightDriveMotorsInput = new CANSparkMax[] {
      new CANSparkMax(3, CANSparkMax.MotorType.kBrushless),
      new CANSparkMax(4, CANSparkMax.MotorType.kBrushless) };

  public DriveTrain2024() {
    super(leftDriveMotorsInput, rightDriveMotorsInput, PneumaticsModuleType.CTREPCM,
        new int[] {}, false,
        DriveConstants.drivingMax, DriveConstants.driveAmpsMax, DriveConstants.drivingRamp,
        DriveConstants.WHEEL_DIAMETER, DriveConstants.PULSES_PER_REVOLUTION, true);
    // invertAll();
  }

  @Override
  public void resetAll() {
    System.out.println("Resetting DriveTrain");
    stop();
    resetEncoders();
    resetGyro();
    doSlowMode(false);
  }

  public void sudoMode(boolean on) {
    DriverStation.reportWarning("DRIVETRAIN OVERDRIVE: " + on, false);
    if (on) {
      for (CANSparkMax motor : getAllMotors()) {
        motor.setOpenLoopRampRate(0);
        motor.setClosedLoopRampRate(0);
        motor.setSmartCurrentLimit(40);
        doSlowMode(false);
      }
    } else {
      for (CANSparkMax motor : getAllMotors()) {
        motor.setOpenLoopRampRate(DriveConstants.drivingRamp);
        motor.setClosedLoopRampRate(DriveConstants.drivingRamp);
        motor.setSmartCurrentLimit(DriveConstants.driveAmpsMax);
      }
    }
  }
}
