// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Constants.DeviceConstants;
import frc.robot.SyncedLibraries.SystemBases.ManipulatorBase;

public class Shooter extends ManipulatorBase {
  private static final double kP = DeviceConstants.shooterSpeedKP;
  private static final double kI = DeviceConstants.shooterSpeedKI;
  private static final double kD = DeviceConstants.shooterSpeedKD;
  private static final double tolerance = DeviceConstants.shooterSpeedTolerance;

  public Shooter() {
    addMotors(new CANSparkMax(DeviceConstants.shooterMotor2Id, MotorType.kBrushless),
        new CANSparkMax(DeviceConstants.shooterMotor1Id, MotorType.kBrushless));
    setSpeedMultiplier(1);
    invertSpecificMotors(false, 1);
    setBrakeMode(false);
    SmartDashboard.putNumber("shooterP", kP);
    SmartDashboard.putNumber("shooterI", kI);
    SmartDashboard.putNumber("shooterD", kD);
    SmartDashboard.putNumber("Shooter target", 5000);
  }

  public void sedPID(double target) {
    setSpeedPID(
        SmartDashboard.getNumber("shooterP", 0),
        SmartDashboard.getNumber("shooterI", 0),
        SmartDashboard.getNumber("shooterD", 0),
        tolerance);
    getSpeedCommand().setTargetSpeed(target);
    getSpeedCommand().schedule();
    // getSpeedCommand().setEndOnTarget(true).andThen(new InstantCommand(() ->
    // fullStop()));
  }

  public void adjust() {
    getSpeedCommand().setTargetSpeed(SmartDashboard.getNumber("Shooter target", 0));
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Shooter speed: ", getCurrentSpeed());
    SmartDashboard.putNumber("Shooter power: ", getAvePower());
    SmartDashboard.putBoolean("Shooter at speed: ", getSpeedCommand() != null ? getSpeedCommand().atSpeed : false);
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
