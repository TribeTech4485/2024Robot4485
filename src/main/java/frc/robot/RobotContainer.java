package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
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

<<<<<<< HEAD
    Robot.Zero.commObjectX.a().whileTrue(new StartEndCommand(() -> {
      Robot.Intake.sendIt(35000);
      System.out.println("Sending it intake");
=======
    Robot.Zero.A.get().whileTrue(new StartEndCommand(() -> {
      Robot.Intake.sendIt(15000);
      System.out.println("Sending it");
>>>>>>> 1fc01430e9689aae5b691564a232ce9b0df74d41
    }, () -> {
      Robot.Intake.stopCommands();
      System.out.println("Stopping it intake");
    }));

<<<<<<< HEAD
    // Robot.Zero.commObjectX.y().onTrue(new InstantCommand(() -> Robot.Intake.setPower(1, false)));

    Robot.Zero.commObjectX.b().whileTrue(new StartEndCommand(() -> {
      Robot.Shooter.sendIt(35000);
      System.out.println("Sending it shooter");
=======
    Robot.Zero.B.get().whileTrue(new StartEndCommand(() -> {
      Robot.Shooter.sendIt(15000);
      System.out.println("Sending it");
>>>>>>> 1fc01430e9689aae5b691564a232ce9b0df74d41
    }, () -> {
      Robot.Shooter.stopCommands();
      System.out.println("Stopping it shooter");
    }));

    // t.Primary.X
    // nTrue(new InstantCommand(() -> {
    // Robot.Limelight.alignTag();
    // }));
  }
}
