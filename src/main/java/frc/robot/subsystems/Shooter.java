// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.DeviceConstants;
import frc.robot.SyncedLibraries.SystemBases.ManipulatorBase;

public class Shooter extends ManipulatorBase {
  private static final double kP = DeviceConstants.shooterSpeedKP;
  private static final double kI = DeviceConstants.shooterSpeedKI;
  private static final double kD = DeviceConstants.shooterSpeedKD;
  private static final double tolerance = DeviceConstants.shooterSpeedTolerance;
  int targetSpeed = 20000;

  public Shooter() {
    addMotors(new CANSparkMax(DeviceConstants.shooterMotorId, CANSparkMax.MotorType.kBrushless));

    setSpeedPID(kP, kI, kD, tolerance);
    setSpeedMultiplier(1);
    setBrakeMode(false);
    // SmartDashboard.putNumber("Shooter target", targetSpeed);
  }

  public void sendIt(int speed) {
    setSpeedPID(kP, kI, kD, tolerance);
    setTargetSpeed(speed);
    // getSpeedCommand().andThen(new InstantCommand(() -> fullStop()));
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Shooter speed", getCurrentSpeed());
    SmartDashboard.putNumber("Shooter target", targetSpeed);
  }
}
