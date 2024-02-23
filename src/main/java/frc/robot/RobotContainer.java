package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import frc.robot.commands.DriveTrainCamCommand;
import frc.robot.commands.FullShootCameraCommands;

public class RobotContainer {
  public RobotContainer() {
    configureBindings();
  }

  public Command getAutonomousCommand() {
    return null;
  }

  public void configureBindings() {

    Robot.Zero.PovRight.get().onTrue(new InstantCommand(() -> Robot.Intake.setPower(-1, false)));
    Robot.Zero.PovLeft.get().onTrue(new InstantCommand(() -> Robot.Intake.setPower(1, false)));
    Robot.Zero.PovDown.get().onTrue(new InstantCommand(() -> Robot.Intake.setPower(0, false)));

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

    Robot.Zero.RightTrigger.get().onTrue(new InstantCommand(() -> Robot.CamCommand.execute()));

    // Auto aim and shoot, end on button release
    Robot.Zero.RightBumper.get().and(() -> !Robot.Zero.isJoysticksBeingTouched())
        .whileTrue(new StartEndCommand(
            () -> {
              Robot.CamCommand = new DriveTrainCamCommand(Robot.TeleDriveCommand);
              Robot.shootCommand = new FullShootCameraCommands();
              Robot.shootCommand.schedule();
            },

            () -> {
              // "unschedule" all commands if canceled
              CommandScheduler.getInstance().removeComposedCommand(Robot.TeleDriveCommand);
              CommandScheduler.getInstance().removeComposedCommand(Robot.CamCommand);
              CommandScheduler.getInstance().removeComposedCommand(Robot.Shooter.getSpeedCommand());
              CommandScheduler.getInstance().removeComposedCommand(Robot.shootCommand);
              CommandScheduler.getInstance().removeComposedCommand(Robot.Turret.getMoveCommand());
              Robot.shootCommand.cancel();
              Robot.CamCommand.cancel();
              Robot.Shooter.stopCommands();
              Robot.TeleDriveCommand.schedule();
            }));

    // sudo kill -f *
    // all front buttons and and at least one stick press
    Robot.doOnAllControllers(
        (controller) -> controller.LeftBumper.get()
            .and(controller.LeftTrigger.get())
            .and(controller.RightBumper.get())
            .and(controller.RightTrigger.get())
            .and(controller.LeftStickPress.get().or(controller.RightStickPress.get()))
            .onTrue(new InstantCommand(() -> Robot.KILLIT())));
  }
}
