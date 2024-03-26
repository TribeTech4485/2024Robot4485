package frc.robot.commands;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.SyncedLibraries.SystemBases.PhotonVisionBase;
import frc.robot.subsystems.DriveTrain2024;

public class DriveTrainTurnCamCommand extends Command {
  PhotonVisionBase photon;
  DriveTrain2024 driveTrain;
  PIDController pidController;
  AHRS gyro;
  boolean onTarget = false;
  final double tolerance = 2;
  final int onTargetCounterStart = 5;
  int onTargetCounter = onTargetCounterStart;
  boolean endOnTarget = false;
  double gyroOffset = 0;
  /** Hopefully not necessary */
  final double camAngleMultiplier = 1;
  final double maxSpeed = 0.1;
  Command driveCommand;
  boolean hasEverSeenTarget = false;
  final boolean wasInBrakeMode;

  public DriveTrainTurnCamCommand() {
    // addRequirements(Robot.DriveTrain);
    // photon = Robot.PhotonVision;
    // driveTrain = Robot.DriveTrain;
    // gyro = Robot.DriveTrain.getGyro();
    // pidController = new PIDController(0.05, 0.01, 0);
    // pidController.setTolerance(tolerance);
    // pidController.setSetpoint(0);
    wasInBrakeMode = false;
  }
  public DriveTrainTurnCamCommand(Command teleDriveCommand) {
    addRequirements(Robot.DriveTrain);
    photon = Robot.PhotonVision;
    driveTrain = Robot.DriveTrain;
    gyro = Robot.DriveTrain.getGyro();
    pidController = new PIDController(0.15, 0.00, 0); //0.07 and I = 0.01
    pidController.setTolerance(tolerance);
    pidController.setSetpoint(0);
    driveCommand = teleDriveCommand;
    wasInBrakeMode = driveTrain.getBrakeMode();
  }

  @Override
  public void initialize() {
    // driveTrain.stop();
    driveTrain.setBrakeMode(true);
    pidController.reset();
    System.out.println("DriveTrainCamCommand: init");
  }

  @Override
  public void execute() {
    double angle = 0;
    double currGyro = -gyro.getAngle();
    if (hasEverSeenTarget) {
      if (photon.hasTarget) {
        angle = photon.targetXYAngles[0] * camAngleMultiplier;
        gyroOffset = currGyro - angle;
        SmartDashboard.putNumber("LastSeen at", angle);
      } else {
        angle = currGyro - gyroOffset;
      }
    } else if (photon.hasTarget) {
      hasEverSeenTarget = true;
      driveCommand.cancel();
      execute();
      return;
      // its easier to just restart the command
      // than to redo the logic
    } else {
      angle = 0;
    }

    SmartDashboard.putNumber("Gyro offset", gyroOffset);
    SmartDashboard.putNumber("Gyro val", currGyro);
    SmartDashboard.putNumber("Gyro final", (currGyro - gyroOffset));
    SmartDashboard.putBoolean("has targ", photon.hasTarget);

    SmartDashboard.putNumber("Target angle", angle);
    double speed = pidController.calculate(angle);
    speed *= 0.1;
    SmartDashboard.putNumber("Turn speed", speed);

    // clamp
    if (Math.abs(speed) > maxSpeed) {
      speed = maxSpeed * Math.signum(speed);
    }

    driveTrain.doTankDrive(speed, -speed);
    if (Math.abs(angle) < tolerance * 1.5) {
      SmartDashboard.putNumber("ON TARGER COUNTER", onTargetCounter);
      if (onTargetCounter-- < 0) {
        onTarget = true;
      } else {
        onTarget = false;
        // onTargetCounter = onTargetCounterStart;
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    // driveCommand.schedule();
    driveTrain.setBrakeMode(wasInBrakeMode);
    System.out.println("DriveTrainCamCommand: end");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (endOnTarget) {
      return onTarget;
    }
    return false;
  }

  public DriveTrainTurnCamCommand setEndOnTarget(boolean endOnTarget) {
    this.endOnTarget = endOnTarget;
    return this;
  }
}
