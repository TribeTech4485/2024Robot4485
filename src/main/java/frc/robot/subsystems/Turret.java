// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import frc.robot.Robot;
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
}
