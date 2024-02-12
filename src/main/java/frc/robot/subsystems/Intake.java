// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.DeviceConstants;
import frc.robot.SyncedLibraries.SystemBases.ManipulatorBase;

public class Intake extends ManipulatorBase {
  private static final double kP = DeviceConstants.intakeSpeedKP;
  private static final double kI = DeviceConstants.intakeSpeedKI;
  private static final double kD = DeviceConstants.intakeSpeedKD;
  private static final double tolerance = DeviceConstants.intakeSpeedTolerance;

  public Intake() {
    addMotors(new CANSparkMax(DeviceConstants.intakeMotorId, CANSparkMax.MotorType.kBrushless));

    setSpeedPID(kP, kI, kD, tolerance);
    setSpeedMultiplier(1);
    setBrakeMode(false);
  }

  public void sendIt(int speed) {
    setPositionPID(kP, kI, kD, tolerance);
    setTargetSpeed(speed);
    // getSpeedCommand().andThen(new InstantCommand(() -> fullStop()));
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Intake speed", getCurrentSpeed());
    SmartDashboard.putNumber("Intake target", getSpeedCommand().getTargetSpeed());
  }

  @Override
  public void home() {
    // do nothing
  }

  @Override
  public void ESTOP() {
    setBrakeMode(true);
    fullStop();
  }
}
