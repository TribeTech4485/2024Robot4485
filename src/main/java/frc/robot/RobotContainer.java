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
    boolean competitonMode = true;
    if (competitonMode) {
      Robot.Zero.RightBumper.get()
          .onTrue(new InstantCommand(() -> Robot.DriveTrain.doSlowMode(true)))
          .onFalse(new InstantCommand(() -> Robot.DriveTrain.doSlowMode(false)));

      Robot.Zero.RightTrigger.get()
          .onTrue(new InstantCommand(() -> Robot.DriveTrain.setBrakeMode(true)))
          .onFalse(new InstantCommand(() -> Robot.DriveTrain.setBrakeMode(false)));

      // cam command
      Robot.Zero.LeftBumper.get();

      // straight
      Robot.Zero.LeftTrigger.get()
          .onTrue(new InstantCommand(() -> Robot.TeleDriveCommand.straightDrive(true)))
          .onFalse(new InstantCommand(() -> Robot.TeleDriveCommand.straightDrive(false)));
      // Robot.One.LeftBumper.get().onTrue(new InstantCommand(() ->
      // Robot.DriveTrain.doSlowMode(true)))
      // .onFalse(new InstantCommand(() -> Robot.DriveTrain.doSlowMode(false)));


      Robot.Zero.LeftStickPress.get().onTrue(new InstantCommand(() -> Robot.DriveTrain.sudoMode(true)));
      Robot.Zero.RightStickPress.get().onTrue(new InstantCommand(() -> Robot.DriveTrain.sudoMode(false)));

      // Here is xbox controlls
      Robot.One.RightTrigger.get().and(() -> !Robot.One.isJoystick)
          .onTrue(new InstantCommand(() -> Robot.Shooter.sedPID(
              SmartDashboard.getNumber("Shooter target", 0))))
          .onFalse(new InstantCommand(() -> Robot.Shooter.stopCommands()));

      Robot.One.LeftTrigger.get().and(() -> !Robot.One.isJoystick)
          .onTrue(new InstantCommand(() -> Robot.Shooter.sedPID(
              -SmartDashboard.getNumber("Shooter target", 0))))
          .onFalse(new InstantCommand(() -> Robot.Shooter.stopCommands()));

      Robot.One.RightBumper.get().and(() -> !Robot.One.isJoystick)
          .onTrue(new InstantCommand(() -> {
            Robot.Conveyor.setPower(-1);
            Robot.Intake.sendIt(-8000);
          }))
          .onFalse(new InstantCommand(() -> {
            Robot.Conveyor.fullStop();
            Robot.Intake.fullStop();
          }));

      Robot.One.LeftBumper.get().and(() -> !Robot.One.isJoystick)
          .onTrue(new InstantCommand(() -> {
            Robot.Conveyor.setPower(0.5);
            Robot.Intake.sendIt(8000);
          }))
          .onFalse(new InstantCommand(() -> {
            Robot.Conveyor.fullStop();
            Robot.Intake.fullStop();
          }));

      Robot.One.A.get().and(() -> !Robot.One.isJoystick)
          .onTrue(new InstantCommand(() -> Robot.Shooter.sedPID(-2000)))
          .onFalse(new InstantCommand(() -> Robot.Shooter.stopCommands()));
      Robot.One.B.get().and(() -> !Robot.One.isJoystick)
          .onTrue(new InstantCommand(() -> Robot.Shooter.sedPID(2000)))
          .onFalse(new InstantCommand(() -> Robot.Shooter.stopCommands()));

      Robot.One.LeftStickPress.get().and(() -> !Robot.One.isJoystick)
          .onTrue(new InstantCommand(() -> Robot.Turret.sudoMode(true)));

      Robot.One.RightStickPress.get().and(() -> !Robot.One.isJoystick)
          .onTrue(new InstantCommand(() -> Robot.Turret.sudoMode(false)));

      // Joystick controlls
      // trigger
      Robot.One.A.get().and(() -> Robot.One.isJoystick)
          .onTrue(new InstantCommand(() -> Robot.Shooter.sedPID(
              SmartDashboard.getNumber("Shooter target", 0))))
          .onFalse(new InstantCommand(() -> Robot.Shooter.stopCommands()));

      // 2
      Robot.One.B.get().and(() -> Robot.One.isJoystick)
          .onTrue(new InstantCommand(() -> {
            Robot.Conveyor.setPower(-1);
            Robot.Intake.sendIt(-8000);
          }))
          .onFalse(new InstantCommand(() -> {
            Robot.Conveyor.fullStop();
            Robot.Intake.fullStop();
          }));

      // 7
      Robot.One.Share.get().and(() -> Robot.One.isJoystick)
          .onTrue(new InstantCommand(() -> {
            Robot.Conveyor.setPower(0.5);
            Robot.Intake.sendIt(8000);
          }))
          .onFalse(new InstantCommand(() -> {
            Robot.Conveyor.fullStop();
            Robot.Intake.fullStop();
          }));

      // 8
      Robot.One.Options.get().and(() -> Robot.One.isJoystick)
          .onTrue(new InstantCommand(() -> Robot.Shooter.sedPID(-2000)))
          .onFalse(new InstantCommand(() -> Robot.Shooter.stopCommands()));

      // 9
      Robot.One.LeftTrigger.get().and(() -> Robot.One.isJoystick)
          .onTrue(new InstantCommand(() -> Robot.Shooter.sedPID(2000)))
          .onFalse(new InstantCommand(() -> Robot.Shooter.stopCommands()));

    } else {
      Robot.Zero.PovUp.get().onTrue(new InstantCommand(() -> {
        Robot.Conveyor.setPower(-1);
        Robot.Intake.sendIt(-8000);
      }));
      Robot.Zero.PovDown.get().onTrue(new InstantCommand(() -> {
        Robot.Conveyor.fullStop();
        Robot.Intake.fullStop();
      }));

      Robot.Zero.PovLeft.get()
          .onTrue(new InstantCommand(() -> Robot.Turret.moveToPosition(Robot.Turret.getPosition())));
      Robot.Zero.LeftStickPress.get().onTrue(new InstantCommand(() -> Robot.Turret.adjustTargetPos(-5)));
      Robot.Zero.RightStickPress.get().onTrue(new InstantCommand(() -> Robot.Turret.adjustTargetPos(5)));

      Robot.doOnAllControllers(
          (controller) -> {
            controller.A.get().onTrue(new InstantCommand(() -> Robot.Shooter.sedPID(
                SmartDashboard.getNumber("Shooter target", 0))));
            controller.B.get().onTrue(new InstantCommand(() -> Robot.Shooter.stopCommands()));
            controller.Y.get().onTrue(new InstantCommand(() -> Robot.Shooter.sedPID(2000)));
            controller.X.get().onTrue(new InstantCommand(() -> Robot.Shooter.sedPID(-2000)));
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
    }

    // sudo kill -f *
    // all front buttons and and both stick press
    Robot.doOnControllers(
        (controller) -> controller.LeftBumper.get()
            .and(controller.LeftTrigger.get())
            .and(controller.LeftBumper.get())
            .and(controller.RightBumper.get())
            .and(controller.RightTrigger.get())
            .and(controller.LeftStickPress.get())
            .and(controller.RightStickPress.get())
            .onTrue(new InstantCommand(() -> Robot.KILLIT())),
        0, 1, 2, 3, 4);
  }
}
