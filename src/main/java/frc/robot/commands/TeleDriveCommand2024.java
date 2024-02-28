// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Robot;
import frc.robot.SyncedLibraries.Controllers.ControllerBase;
import frc.robot.SyncedLibraries.SystemBases.TeleDriveCommandBase;

/** Add your docs here. */
public class TeleDriveCommand2024 extends TeleDriveCommandBase {
  public TeleDriveCommand2024(ControllerBase... controller) {
    super(Robot.DriveTrain, false, controller);
    defaultExecute = false;
  }

  @Override
  public void execute() {
    super.execute();
    Robot.Turret.setPower(ys[2] / 1, false);
  }
}
