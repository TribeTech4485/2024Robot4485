package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.SyncedLibraries.SystemBases.PhotonVisionBase;
import frc.robot.subsystems.DriveTrain2024;

public class DriveTrainMoveCamCommand extends Command {
  PhotonVisionBase photon;
  DriveTrain2024 driveTrain;
  PIDController pidController;
  boolean onTarget = false;
  final double tolerance = 0.2;
  final int onTargetCounterStart = 5;
  int onTargetCounter = onTargetCounterStart;
  boolean endOnTarget = false;
  double memoryDistance = 0;
  /** Hopefully not necessary */
  final double camAngleMultiplier = 2;
  final double maxSpeed = 0.25;
  Command driveCommand;
  boolean hasEverSeenTarget = false;
  final boolean wasInBrakeMode;

  public DriveTrainMoveCamCommand() {
    // addRequirements(Robot.DriveTrain);
    // photon = Robot.PhotonVision;
    // driveTrain = Robot.DriveTrain;
    // gyro = Robot.DriveTrain.getGyro();
    // pidController = new PIDController(0.05, 0.01, 0);
    // pidController.setTolerance(tolerance);
    // pidController.setSetpoint(0);
    wasInBrakeMode = false;
  }

  public DriveTrainMoveCamCommand(Command teleDriveCommand) {
    addRequirements(Robot.DriveTrain);
    photon = Robot.PhotonVision;
    driveTrain = Robot.DriveTrain;
    pidController = new PIDController(0.2, 0.01, 0);
    pidController.setTolerance(tolerance);
    pidController.setSetpoint(1.3);
    driveCommand = teleDriveCommand;
    wasInBrakeMode = driveTrain.getBrakeMode();
  }

  @Override
  public void initialize() {
    // driveTrain.stop();
    driveTrain.setBrakeMode(true);
    pidController.reset();
    System.out.println("DriveTrainMoveCamCommand: init");
  }

  @Override
  public void execute() {
    double distance = 0;
    if (hasEverSeenTarget) {
      if (photon.hasTarget) {
        distance = photon.targetDistance;
        memoryDistance = distance;
        driveTrain.resetEncoders();
      } else {
        distance = memoryDistance; // - (driveTrain.getLeftEncoderCount() * camAngleMultiplier);
      }
    } else if (photon.hasTarget) {
      hasEverSeenTarget = true;
      driveCommand.cancel();
      execute();
      return;
      // its easier to just restart the command
      // than to redo the logic
    }

    SmartDashboard.putNumber("LastSeen at", memoryDistance);
    // SmartDashboard.putNumber("Gyro offset", gyroOffset);
    SmartDashboard.putNumber("Distance val", distance);
    // SmartDashboard.putNumber("Gyro final", (currGyro - gyroOffset));
    SmartDashboard.putBoolean("has targ", photon.hasTarget);

    // SmartDashboard.putNumber("Target angle", angle);
    double speed = pidController.calculate(distance);
    speed *= 1;
    SmartDashboard.putNumber("Dist Turn speed", speed);

    // clamp
    if (Math.abs(speed) > maxSpeed) {
      speed = maxSpeed * Math.signum(speed);
    }

    driveTrain.doTankDrive(speed, speed);
    if (Math.abs(distance) - 1.5 < tolerance * 1.5) {
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
    System.out.println("DriveTrainMoveCamCommand: end");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (endOnTarget) {
      return onTarget;
    }
    return false;
  }

  public DriveTrainMoveCamCommand setEndOnTarget(boolean endOnTarget) {
    this.endOnTarget = endOnTarget;
    return this;
  }
}
