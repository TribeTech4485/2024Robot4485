package frc.robot.commands;

import org.photonvision.targeting.PhotonTrackedTarget;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.SyncedLibraries.SystemBases.DriveTrainBase;
import frc.robot.SyncedLibraries.SystemBases.PhotonVisionBase;

public class DriveTrainCamCommand extends Command {
  PhotonVisionBase photon;
  DriveTrainBase driveTrain;
  PIDController pidController;
  AHRS gyro;
  boolean onTarget = false;
  final double tolerance = 0.05;
  final int onTargetCounterStart = 5;
  int onTargetCounter = onTargetCounterStart;
  boolean endOnTarget = false;
  double gyroOffset = 0;
  /** Hopefully not necessary */
  final double camAngleMultiplier = 1;
  final double maxSpeed = 0.05;
  Command driveCommand;
  boolean hasEverSeenTarget = false;

  public DriveTrainCamCommand(Command teleDriveCommand) {
    addRequirements(Robot.DriveTrain);
    photon = Robot.PhotonVision;
    driveTrain = Robot.DriveTrain;
    gyro = Robot.DriveTrain.getGyro();
    pidController = new PIDController(0.05, 0.01, 0);
    pidController.setTolerance(tolerance);
    pidController.setSetpoint(0);
    driveCommand = teleDriveCommand;
  }

  @Override
  public void initialize() {
    driveTrain.stop();
    pidController.reset();
    System.out.println("Doing cam Turn");
  }

  @Override
  public void execute() {
    double angle = 0;
    double currGyro = -gyro.getAngle();
    // PhotonTrackedTarget mainTarget = photon.mainTarget;
    if (hasEverSeenTarget) {
      if (photon.hasTarget) {
        // if (mainTarget.getFiducialId() == 3 || mainTarget.getFiducialId() == 8) {
          // for (int i = 0; i < photon.targets.size(); i++) {
            // int target = photon.targets.get(i).getFiducialId();
            // if (target != 3 && target != 8) {
              // mainTarget = photon.targets.get(i);
              // break;
            // }
          // }
        // }
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
    if (angle < tolerance * 1.5) {
      if (onTargetCounter-- < 0) {
        onTarget = true;
      } else {
        onTarget = false;
        onTargetCounter = onTargetCounterStart;
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (endOnTarget) {
      return onTarget;
    }
    return false;
  }
}
