package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RepeatCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;

/**
 * This class represents a command group for performing a full shoot operation using camera targeting.
 * It sequentially executes a series of commands including turning towards the target, spinning up the shooter,
 * releasing the note for a specified duration, stopping the shooter, turning off the belt drive, and returning to teleop.
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

        // TODO: release the note for 2 seconds below
        new RepeatCommand(new InstantCommand(() -> System.out.println("TODO: turn on belt drive")))
            .withTimeout(2),

        // stop shooter
        new InstantCommand(() -> Robot.Shooter.stopCommands()),

        // TODO: turn off belt drive below
        new InstantCommand(() -> System.out.println("TODO: turn off belt drive")),

        // return to teleop
        Robot.TeleDriveCommand);
  }
}
