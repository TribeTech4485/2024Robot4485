// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.List;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.SyncedLibraries.SystemBases.PhotonVisionBase;

public class PhotonVision2024 extends PhotonVisionBase {
  /** Creates a new PhotonVision2024. */
  public PhotonVision2024(PhotonCamera camera) {
    super(camera);
  }

  @Override
  public void periodic() {
    double x;
    double y;
    latestResult = camera.getLatestResult();
    targets = latestResult.getTargets();
    hasTarget = latestResult.hasTargets();
    if (hasTarget) {
      mainTarget = latestResult.getBestTarget();
      // if targeting side target on speaker
      if (mainTarget.getFiducialId() == 3 || mainTarget.getFiducialId() == 8) {
        PhotonTrackedTarget tempTarget = containsTarget(targets, 4, 8);
        if (tempTarget != null) {
          // if middle speaker is available
          x = tempTarget.getYaw();
          y = tempTarget.getPitch();
        } else {
          // apply generic offset for centering
          // not as bad as you think tho
          x = mainTarget.getYaw() - 4;
          y = mainTarget.getPitch();
        }
      } else {
        x = mainTarget.getYaw();
        y = mainTarget.getPitch();
      }

      targetXYAngles[0] = x;
      targetXYAngles[1] = y;

      SmartDashboard.putNumber("TargetX", x);
      SmartDashboard.putNumber("TargetY", y);
    }
  }
}