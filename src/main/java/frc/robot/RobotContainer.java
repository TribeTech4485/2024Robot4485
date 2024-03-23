package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.ProxyCommand;
import edu.wpi.first.wpilibj2.command.RepeatCommand;
import frc.robot.commands.DriveTrainTurnCamCommand;
import frc.robot.commands.FullShootCameraCommands;

public class RobotContainer {
  static boolean mode = false;
  SendableChooser<Command> autoChooser;

  public static boolean getMode() {
    return mode;
  }

  public static boolean getInMode() {
    return !mode;
  }

  public static void setMode(boolean newMode) {
    mode = newMode;
  }

  public RobotContainer() {
    configureBindings();
    autoChooser = new SendableChooser<>();
    autoChooser.setDefaultOption("Shoot and back up", new SequentialCommandGroup(
      new PrintCommand("Shoot and back up"),
      new InstantCommand(Robot::resetCamCommand),
      new ProxyCommand(Robot::getCamCommand),
      new PrintCommand("in position"),
      new InstantCommand(() -> Robot.Shooter.shoot()),
      new PrintCommand("spun up"),
      new WaitCommand(2),
      new InstantCommand(() -> Robot.Intake.setPower(-1)),
      new WaitCommand(2),
      new PrintCommand("Done shooting"),
      new InstantCommand(Robot.Shooter::stopCommands),
      new InstantCommand(Robot.Intake::stop),
      new InstantCommand(() -> Robot.DriveTrain.doTankDrive(-0.5, -0.5)),
      new PrintCommand("driving back"),
      new WaitCommand(2),
      new InstantCommand(Robot.DriveTrain::stop),
      new PrintCommand("done")
    ));
    autoChooser.addOption("Back up", new SequentialCommandGroup(
      new InstantCommand(() -> Robot.DriveTrain.doTankDrive(-0.5, -0.5)),
      new WaitCommand(2),
      new InstantCommand(Robot.DriveTrain::stop)
    ));
    SmartDashboard.putData("Autonomus command", autoChooser);
  }

  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }

  public void configureBindings() {
    boolean competitonMode = true;
    if (competitonMode) {

      // ================ Primary ================ //

      // precision drive
      // Robot.Zero.RightTrigger.get()
      // .whileTrue(new InstantCommand(() -> Robot.DriveTrain.doSlowMode(1 -
      // (Robot.Zero.getRightTrigger() * 0.9))))
      // .onFalse(new InstantCommand(() -> Robot.DriveTrain.doSlowMode(false)));

      // brake
      Robot.Zero.RightBumper.get()
          .onTrue(new InstantCommand(() -> Robot.DriveTrain.setBrakeMode(true)))
          .onFalse(new InstantCommand(() -> Robot.DriveTrain.setBrakeMode(false)));

      // cam command
      Robot.Zero.LeftBumper.get().onTrue(new SequentialCommandGroup(
          new InstantCommand(Robot::resetCamCommand),
          new ProxyCommand(Robot::getCamCommand)))
          .onFalse(new InstantCommand(() -> {
            Robot.CamCommand.cancel();
            Robot.TeleDriveCommand.schedule();
          }));

      // straight
      Robot.Zero.LeftTrigger.get()
          .onTrue(new InstantCommand(() -> Robot.TeleDriveCommand.straightDrive(true)))
          .onFalse(new InstantCommand(() -> Robot.TeleDriveCommand.straightDrive(false)));

      // STRONK
      Robot.Zero.LeftStickPress.get().and(Robot.Zero.RightStickPress.get().negate())
          .onTrue(new InstantCommand(() -> Robot.DriveTrain.sudoMode(true)));
      Robot.Zero.RightStickPress.get().and(Robot.Zero.LeftStickPress.get().negate())
          .onTrue(new InstantCommand(() -> Robot.DriveTrain.sudoMode(false)));

      // STRONK + pedal to the metal
      Robot.Zero.LeftStickPress.get().and(Robot.Zero.RightStickPress.get())
          .onTrue(new InstantCommand(() -> {
            Robot.DriveTrain.sudoMode(true);
            Robot.DriveTrain.doTankDrive(1, 1);
            Robot.TeleDriveCommand.cancel();
          }))
          .onFalse(new InstantCommand(() -> {
            Robot.DriveTrain.sudoMode(false);
            Robot.TeleDriveCommand.schedule();
          }));

      // shoot
      Robot.Zero.Y.get()
          .onTrue(new InstantCommand(() -> Robot.Shooter.shoot()))
          .onFalse(new InstantCommand(() -> Robot.Shooter.stopCommands()));
      // unshoot
      Robot.Zero.X.get()
          .onTrue(new InstantCommand(() -> Robot.Shooter.inShoot()))
          .onFalse(new InstantCommand(() -> Robot.Shooter.stopCommands()));

      // top intake
      Robot.Zero.A.get()
          .onTrue(new InstantCommand(() -> Robot.Shooter.prepare(-2000).schedule()))
          .onFalse(new InstantCommand(() -> Robot.Shooter.stopCommands()));
      // top outtake
      Robot.Zero.B.get()
          .onTrue(new InstantCommand(() -> Robot.Shooter.prepare(2000).schedule()))
          .onFalse(new InstantCommand(() -> Robot.Shooter.stopCommands()));

      // unintake
      Robot.Zero.PovLeft.get()
          .onTrue(new InstantCommand(() -> {
            Robot.Conveyor.setPower(0.5);
            Robot.Intake.sendIt(8000);
          }))
          .onFalse(new InstantCommand(() -> {
            Robot.Conveyor.fullStop();
            Robot.Intake.fullStop();
          }));

      // intake
      Robot.Zero.PovRight.get()
          .onTrue(new InstantCommand(() -> {
            Robot.Conveyor.setPower(-1);
            Robot.Intake.sendIt(-8000);
          }))
          .onFalse(new InstantCommand(() -> {
            Robot.Conveyor.fullStop();
            Robot.Intake.fullStop();
          }));

      // pov up and down controls arm

      // ================ Secondary ================ //

      // dominant hand does main controls //

      // shoot
      Robot.One.RightTrigger.get()// .and(() -> !Robot.One.isJoystick)
          .onTrue(new InstantCommand(() -> Robot.Shooter.shoot()))
          .onFalse(new InstantCommand(() -> Robot.Shooter.stopCommands()));

      // unshoot
      Robot.One.LeftTrigger.get()// .and(() -> !Robot.One.isJoystick)
          .onTrue(new InstantCommand(() -> Robot.Shooter.inShoot()))
          .onFalse(new InstantCommand(() -> Robot.Shooter.stopCommands()));

      // intake
      Robot.One.RightBumper.get()// .and(() -> !Robot.One.isJoystick)
          .onTrue(new InstantCommand(() -> {
            Robot.Conveyor.setPower(-1);
            Robot.Intake.sendIt(-8000);
          }))
          .onFalse(new InstantCommand(() -> {
            Robot.Conveyor.fullStop();
            Robot.Intake.fullStop();
          }));

      // unintake
      Robot.One.LeftBumper.get()// .and(() -> !Robot.One.isJoystick)
          .onTrue(new InstantCommand(() -> {
            Robot.Conveyor.setPower(0.5);
            Robot.Intake.sendIt(8000);
          }))
          .onFalse(new InstantCommand(() -> {
            Robot.Conveyor.fullStop();
            Robot.Intake.fullStop();
          }));

      // top intake
      Robot.One.A.get()// .and(() -> !Robot.One.isJoystick)
          .onTrue(new InstantCommand(() -> Robot.Shooter.prepare(-2000).schedule()))
          .onFalse(new InstantCommand(() -> Robot.Shooter.stopCommands()));
      // top outtake
      Robot.One.B.get()// .and(() -> !Robot.One.isJoystick)
          .onTrue(new InstantCommand(() -> Robot.Shooter.prepare(2000).schedule()))
          .onFalse(new InstantCommand(() -> Robot.Shooter.stopCommands()));

      // STRONK
      Robot.One.LeftStickPress.get()// .and(() -> !Robot.One.isJoystick)
          .onTrue(new InstantCommand(() -> Robot.Turret.sudoMode(true)));
      Robot.One.RightStickPress.get()// .and(() -> !Robot.One.isJoystick)
          .onTrue(new InstantCommand(() -> Robot.Turret.sudoMode(false)));

      /*
       * // Joystick controlls
       * // trigger
       * Robot.One.A.get().and(() -> Robot.One.isJoystick)
       * .onTrue(new InstantCommand(() -> Robot.Shooter.sedPID(
       * SmartDashboard.getNumber("Shooter target", 0))))
       * .onFalse(new InstantCommand(() -> Robot.Shooter.stopCommands()));
       * 
       * // 2
       * Robot.One.B.get().and(() -> Robot.One.isJoystick)
       * .onTrue(new InstantCommand(() -> {
       * Robot.Conveyor.setPower(-1);
       * Robot.Intake.sendIt(-8000);
       * }))
       * .onFalse(new InstantCommand(() -> {
       * Robot.Conveyor.fullStop();
       * Robot.Intake.fullStop();
       * }));
       * 
       * // 7
       * Robot.One.Share.get().and(() -> Robot.One.isJoystick)
       * .onTrue(new InstantCommand(() -> {
       * Robot.Conveyor.setPower(0.5);
       * Robot.Intake.sendIt(8000);
       * }))
       * .onFalse(new InstantCommand(() -> {
       * Robot.Conveyor.fullStop();
       * Robot.Intake.fullStop();
       * }));
       * 
       * // 8
       * Robot.One.Options.get().and(() -> Robot.One.isJoystick)
       * .onTrue(new InstantCommand(() -> Robot.Shooter.sedPID(-2000)))
       * .onFalse(new InstantCommand(() -> Robot.Shooter.stopCommands()));
       * 
       * // 9
       * Robot.One.LeftTrigger.get().and(() -> Robot.One.isJoystick)
       * .onTrue(new InstantCommand(() -> Robot.Shooter.sedPID(2000)))
       * .onFalse(new InstantCommand(() -> Robot.Shooter.stopCommands()));
       */

      boolean AAAA = false;
      if (AAAA) {
        // ================ Tertiary ================ //
        // brake
        Robot.Four.RightBumper.get().and(RobotContainer::getMode)
            .onTrue(new InstantCommand(() -> Robot.DriveTrain.setBrakeMode(true)))
            .onFalse(new InstantCommand(() -> Robot.DriveTrain.setBrakeMode(false)));

        // cam command
        Robot.Four.LeftBumper.get().and(RobotContainer::getMode)
            .onTrue(new SequentialCommandGroup(
                new InstantCommand(() -> Robot.CamCommand = new DriveTrainTurnCamCommand(
                    Robot.TeleDriveCommand).setEndOnTarget(true)),
                new ProxyCommand(Robot::getCamCommand)))
            .onFalse(new InstantCommand(() -> Robot.CamCommand.cancel()));

        // straight
        Robot.Four.LeftTrigger.get().and(RobotContainer::getMode)
            .onTrue(new InstantCommand(() -> Robot.TeleDriveCommand.straightDrive(true)))
            .onFalse(new InstantCommand(() -> Robot.TeleDriveCommand.straightDrive(false)));

        // STRONK
        Robot.Four.LeftStickPress.get()
            .onTrue(new InstantCommand(() -> RobotContainer.setMode(true)));
        Robot.Four.RightStickPress.get()
            .onTrue(new InstantCommand(() -> RobotContainer.setMode(false)));

        // STRONK + pedal to the metal
        // Robot.Four.LeftStickPress.get().and(Robot.Four.RightStickPress.get()).and(RobotContainer::getMode)
        // .onTrue(new InstantCommand(() -> {
        // Robot.DriveTrain.sudoMode(true);
        // Robot.DriveTrain.doTankDrive(1, 1);
        // Robot.TeleDriveCommand.cancel();
        // }))
        // .onFalse(new InstantCommand(() -> {
        // Robot.DriveTrain.sudoMode(false);
        // Robot.TeleDriveCommand.schedule();
        // }));

        // ================ Secondary ================ //

        // dominant hand does main controls //

        // shoot
        Robot.Four.RightTrigger.get().and(RobotContainer::getInMode)
            .onTrue(new InstantCommand(() -> Robot.Shooter.shoot()))
            .onFalse(new InstantCommand(() -> Robot.Shooter.stopCommands()));

        // unshoot
        Robot.Four.LeftTrigger.get().and(RobotContainer::getInMode)
            .onTrue(new InstantCommand(() -> Robot.Shooter.inShoot()))
            .onFalse(new InstantCommand(() -> Robot.Shooter.stopCommands()));

        // intake
        Robot.Four.RightBumper.get().and(RobotContainer::getInMode)
            .onTrue(new InstantCommand(() -> {
              Robot.Conveyor.setPower(-1);
              Robot.Intake.sendIt(-8000);
            }))
            .onFalse(new InstantCommand(() -> {
              Robot.Conveyor.fullStop();
              Robot.Intake.fullStop();
            }));

        // unintake
        Robot.Four.LeftBumper.get().and(RobotContainer::getInMode)
            .onTrue(new InstantCommand(() -> {
              Robot.Conveyor.setPower(0.5);
              Robot.Intake.sendIt(8000);
            }))
            .onFalse(new InstantCommand(() -> {
              Robot.Conveyor.fullStop();
              Robot.Intake.fullStop();
            }));

        // top intake
        Robot.Four.A.get().and(RobotContainer::getInMode)
            .onTrue(new InstantCommand(() -> Robot.Shooter.prepare(-2000).schedule()))
            .onFalse(new InstantCommand(() -> Robot.Shooter.stopCommands()));
        // top outtake
        Robot.Four.B.get().and(RobotContainer::getInMode)
            .onTrue(new InstantCommand(() -> Robot.Shooter.prepare(2000).schedule()))
            .onFalse(new InstantCommand(() -> Robot.Shooter.stopCommands()));
      }
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
            controller.A.get().onTrue(new InstantCommand(() -> Robot.Shooter.shoot()));
            controller.B.get().onTrue(new InstantCommand(() -> Robot.Shooter.stopCommands()));
            controller.Y.get().onTrue(new InstantCommand(() -> Robot.Shooter.prepare(2000).schedule()));
            controller.X.get().onTrue(new InstantCommand(() -> Robot.Shooter.prepare(-2000).schedule()));
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
                Robot.CamCommand = new DriveTrainTurnCamCommand(Robot.TeleDriveCommand);
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
        0, 1, 2, 3, 4, 5);
  }
}
