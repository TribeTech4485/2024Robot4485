package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;

public class RobotContainer {
  public RobotContainer() {
    configureBindings();
  }

  public Command getAutonomousCommand() {
    return null;
  }

  public void configureBindings() {
    CommandScheduler.getInstance().getActiveButtonLoop().clear();

    // Robot.Zero.LeftBumper.or(Robot.Primary.RightBumper)
    // .onTrue(new InstantCommand(() -> {
    // Robot.DriveTrain.doSlowMode(true);
    // }))
    // .onFalse(new InstantCommand(() -> {
    // Robot.DriveTrain.doSlowMode(false);
    // }));

    // Robot.Zero.RightTrigger
    // .onTrue(new InstantCommand(() -> {
    // Robot.DriveTrain.setBrakeMode(true);
    // }))
    // .onFalse(new InstantCommand(() -> {
    // Robot.DriveTrain.setBrakeMode(false);
    // }));

    // Robot.Zero.LeftTrigger
    // .onTrue(new InstantCommand(() -> {
    // Robot.DriveTrain.setBrakeMode(true);
    // }))
    // .onFalse(new InstantCommand(() -> {
    // Robot.DriveTrain.setBrakeMode(false);
    // }));

    int lowSpeed = 1000;
    int highSpeed = 5000;
    Robot.Zero.RightBumper.get().whileTrue(new StartEndCommand(() -> {
      // Robot.Shooter.sendIt(lowSpeed);
      System.out.println("Sending it");
    }, () -> {
      Robot.Shooter.stopCommands();
      System.out.println("Stopping it intake");
    }));

    Robot.Zero.RightTrigger.get().whileTrue(new StartEndCommand(() -> {
      // Robot.Shooter.sendIt(highSpeed);
      System.out.println("Sending it");
    }, () -> {
      Robot.Shooter.stopCommands();
      System.out.println("Stopping it shooter");
    }));

    Robot.Zero.LeftBumper.get().whileTrue(new StartEndCommand(() -> {
      // Robot.Shooter.sendIt(-lowSpeed);
      System.out.println("Sending it");
    }, () -> {
      Robot.Shooter.stopCommands();
      Robot.Intake.stopCommands();
      System.out.println("Stopping it shooter");
    }));

    Robot.Zero.LeftTrigger.get().whileTrue(new StartEndCommand(() -> {
      // Robot.Shooter.sendIt(-highSpeed);
      System.out.println("Sending it");
    }, () -> {
      Robot.Shooter.stopCommands();
      System.out.println("Stopping it shooter");
    }));

    Robot.Zero.A.get().onTrue(new InstantCommand(() -> Robot.Shooter.sedPID(
        SmartDashboard.getNumber("Shooter target", 0))));
    Robot.Zero.B.get().onTrue(new InstantCommand(() -> Robot.Shooter.stopCommands()));
    Robot.Zero.Y.get().onTrue(new InstantCommand(() -> Robot.Shooter.sedPID(0)));
    Robot.Zero.X.get().onTrue(new InstantCommand(() -> Robot.Shooter.adjust()));

    // t.Primary.X
    // nTrue(new InstantCommand(() -> {
    // Robot.Limelight.alignTag();
    // }));
  }
}
