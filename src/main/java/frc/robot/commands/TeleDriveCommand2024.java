// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Robot;
import frc.robot.SyncedLibraries.AutoControllerSelector;
import frc.robot.SyncedLibraries.BasicFunctions;
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
      double turSpeed = BasicFunctions.smartExp(-ys[1][0], 2);
      if (Math.abs(turSpeed) < 0.1) {
        if (Robot.Zero.PovUp.get().getAsBoolean()) {
          turSpeed = 1;
        } else if (Robot.Zero.PovDown.get().getAsBoolean()) {
          turSpeed = -1;
        }
      }
      Robot.Turret.setPower(turSpeed, false);
    }
    Robot.DriveTrain.doSlowMode(1 - ((1 - BasicFunctions.smartExp(1 - Robot.Zero.getRightTrigger(), 1.5)) * 0.8));
  }
}
