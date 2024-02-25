package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;

/**
 * This class represents a command group for performing a full shoot operation
 * using camera targeting.
 * It sequentially executes a series of commands including turning towards the
 * target, spinning up the shooter,
 * releasing the note for a specified duration, stopping the shooter, turning
 * off the belt drive, and returning to teleop.
 */
public class FullShootCameraCommands extends SequentialCommandGroup {

  public FullShootCameraCommands() {
    addCommands(
        // turn towards target
        new ParallelCommandGroup(
            Robot.CamCommand,
            new TurretCamRotateCommand()),

        // spin up shooter and wait
        Robot.Shooter.shootCommand().setEndOnTarget(true),

        Robot.Conveyor.sendToShooterCommand(),

        // stop shooter
        new InstantCommand(() -> Robot.Shooter.stopCommands()),

        // return to teleop
        Robot.TeleDriveCommand);
  }
}
