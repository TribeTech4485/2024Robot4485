package frc.robot.subsystems;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonTrackedTarget;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.SyncedLibraries.SystemBases.PhotonVisionBase;

public class PhotonVision2024 extends PhotonVisionBase {
  public PhotonVision2024(PhotonCamera camera) {
    super(camera);
  }

  @Override
  public void periodic() {
    double x;
    double y;
    if (camera.isConnected()) {
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
            targetDistance = tempTarget.getBestCameraToTarget().getTranslation().getX();
          } else {
            /*
             * apply generic offset for centering
             * not as bad as you think tho
             * 1.5 jank radians to degrees
             */
            x = mainTarget.getYaw() - 4;
            y = mainTarget.getPitch();
            targetDistance = mainTarget.getBestCameraToTarget().getTranslation().getX();
          }
        } else {
          x = mainTarget.getYaw();
          y = mainTarget.getPitch();
          targetDistance = mainTarget.getBestCameraToTarget().getTranslation().getX();
        }

        targetXYAngles[0] = x;
        targetXYAngles[1] = y;

        SmartDashboard.putNumber("TargetX", x);
        SmartDashboard.putNumber("TargetY", y);
        SmartDashboard.putNumber("Target dist", targetDistance);
      }
    }
  }
}