// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.Constants.DeviceConstants;
import frc.robot.SyncedLibraries.SystemBases.ManipulatorMoveCommand;
import frc.robot.subsystems.Turret;

public class TurretCamRotateCommand extends Command {
  Turret turret = Robot.Turret;
  ManipulatorMoveCommand moveCommand;
  int staticAngle = 0;

  public TurretCamRotateCommand() {
    addRequirements(Robot.Turret);
  }

  @Override
  public void initialize() {
    moveCommand = new ManipulatorMoveCommand(turret, turret.getPosition(),
        DeviceConstants.turretPositionTolerance,
        DeviceConstants.turretPositionKP,
        DeviceConstants.turretPositionKI,
        DeviceConstants.turretPositionKD)
        .setEndOnTarget(true);
    moveCommand.schedule();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    moveCommand.setTargetPosition(Robot.PhotonVision.targetXYAngles[1] +
        0 // TODO: trig to find turret angle to target
    );
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    moveCommand.cancel();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return moveCommand.isFinished();
  }
}
