// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Robot;
import frc.robot.SyncedLibraries.AutoControllerSelector;
import frc.robot.SyncedLibraries.SystemBases.TeleDriveCommandBase;

/** Add your docs here. */
public class TeleDriveCommand2024 extends TeleDriveCommandBase {
  public TeleDriveCommand2024(AutoControllerSelector... controllerSelectors) {
    super(Robot.DriveTrain, false, controllerSelectors);
    defaultExecute = false;
  }

  @Override
  public void execute() {
    super.execute();
    if (Robot.Turret.getCurrentCommand() == null) {
      Robot.Turret.setPower(ys[1][2], false);
    }
  }
}
