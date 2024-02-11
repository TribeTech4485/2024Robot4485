package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RepeatCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.StartEndCommand;

public class RobotContainer {
  public RobotContainer() {
    configureBindings();
  }

  public Command getAutonomousCommand() {
    return null;
  }

  public void configureBindings() {
    // CommandScheduler.getInstance().getActiveButtonLoop().clear();
    // CommandScheduler.getInstance().getDefaultButtonLoop().clear();

    Robot.doOnAllControllers(
        (controller) -> {
          controller.A.get().onTrue(new InstantCommand(() -> Robot.Shooter.sedPID(
              SmartDashboard.getNumber("Shooter target", 0))));
          controller.B.get().onTrue(new InstantCommand(() -> Robot.Shooter.stopCommands()));
          controller.Y.get().onTrue(new InstantCommand(() -> Robot.Shooter.sedPID(0)));
          controller.X.get().onTrue(new InstantCommand(() -> Robot.Shooter.adjust()));
        });

    // Auto aim, end on button release
    Robot.Zero.LeftBumper.get().whileTrue(new StartEndCommand(
        () -> {
          Robot.TeleDriveCommand.cancel();
          Robot.CamCommand.schedule();
        },
        () -> {
          Robot.CamCommand.cancel();
          Robot.TeleDriveCommand.schedule();
        }));

    // Auto aim and shoot, end on button release
    Robot.Zero.RightBumper.get().whileTrue(new StartEndCommand(
        () -> {
          Robot.TeleDriveCommand.cancel();
          Robot.shootCommand = new SequentialCommandGroup(
              // turn towards target
              Robot.CamCommand,
              // spin up shooter
              new InstantCommand(() -> Robot.Shooter.sedPID(
                  SmartDashboard.getNumber("Shooter target", 0))),
              // wait for shooter to spin up
              Robot.Shooter.getSpeedCommand()
                  .setEndOnTarget(true),
              // TODO: release the note for 2 seconds
              new RepeatCommand(new InstantCommand(() -> System.out.println("TODO: turn on belt drive")))
                  .withTimeout(2),
              // stop shooter
              new InstantCommand(() -> Robot.Shooter.stopCommands()),
              // TODO: turn off belt drive
              new InstantCommand(() -> System.out.println("TODO: turn off belt drive")),
              // return to teleop
              Robot.TeleDriveCommand);
          Robot.shootCommand.schedule();
        },

        () -> {
          // "unschedule" all commands
          CommandScheduler.getInstance().removeComposedCommand(Robot.TeleDriveCommand);
          CommandScheduler.getInstance().removeComposedCommand(Robot.CamCommand);
          CommandScheduler.getInstance().removeComposedCommand(Robot.Shooter.getSpeedCommand());
          Robot.shootCommand.cancel();
          Robot.CamCommand.cancel();
          Robot.Shooter.stopCommands();
          Robot.TeleDriveCommand.schedule();
        }));

    // sudo kill -f *
    Robot.doOnAllControllers(
        (controller) -> controller.LeftBumper.get()
            .and(controller.LeftTrigger.get())
            .and(controller.RightBumper.get())
            .and(controller.RightTrigger.get())
            .onTrue(new InstantCommand(() -> Robot.KILLIT())));
  }
}
