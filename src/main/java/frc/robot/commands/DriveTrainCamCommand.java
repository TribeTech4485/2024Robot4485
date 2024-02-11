package frc.robot.commands;

import java.util.List;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.SyncedLibraries.SystemBases.DriveTrainBase;

public class DriveTrainCamCommand extends Command {
  PhotonCamera camera;
  DriveTrainBase driveTrain;
  PIDController pidController;
  AHRS gyro;
  boolean onTarget = false;
  double tolerance = 0.1;
  int onTargetCounterStart = 5;
  int onTargetCounter = onTargetCounterStart;
  boolean endOnTarget = false;
  double gyroOffset = 0;
  /** Hopefully not necessary */
  double camAngleMultiplier = 1;
  double maxSpeed = 0.05;

  public DriveTrainCamCommand() {
    addRequirements(Robot.DriveTrain);
    camera = Robot.camera;
    driveTrain = Robot.DriveTrain;
    gyro = Robot.DriveTrain.getGyro();
    pidController = new PIDController(0.5, 0.01, 0);
    pidController.setTolerance(tolerance);
    pidController.setSetpoint(0);
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
    PhotonPipelineResult result = camera.getLatestResult();
    if (result.hasTargets()) {
      List<PhotonTrackedTarget> targets = result.getTargets();
      PhotonTrackedTarget target = targets.get(0);
      Transform3d transform3d = target.getBestCameraToTarget();
      Rotation3d rotation3d = transform3d.getRotation();
      angle = rotation3d.getAngle() * 180 / Math.PI - 175;
      angle *= camAngleMultiplier;
    } else {
      gyroOffset = gyro.getAngle();
      angle = gyro.getAngle() - gyroOffset;
    }

    SmartDashboard.putNumber("Target angle aaa", angle);
    double speed = pidController.calculate(angle);
    speed *= 0.05;
    SmartDashboard.putNumber("Turn speed", speed);
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
