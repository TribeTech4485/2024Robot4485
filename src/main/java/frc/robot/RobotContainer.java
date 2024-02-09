package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class RobotContainer {
  public RobotContainer() {
    configureBindings();
  }

  public Command getAutonomousCommand() {
    return null;
  }

  public void configureBindings() {
    CommandScheduler.getInstance().getActiveButtonLoop().clear();

    // Robot.Zero.A.get().onTrue(new InstantCommand(() -> Robot.Shooter.sedPID(
    //     SmartDashboard.getNumber("Shooter target", 0))));
    // Robot.Zero.B.get().onTrue(new InstantCommand(() -> Robot.Shooter.stopCommands()));
    // Robot.Zero.Y.get().onTrue(new InstantCommand(() -> Robot.Shooter.sedPID(0)));
    // Robot.Zero.X.get().onTrue(new InstantCommand(() -> Robot.Shooter.adjust()));

    // sudo kill -f *
    Robot.doOnAllControllers(
        (controller) -> controller.LeftBumper.get()
            .and(controller.LeftTrigger.get())
            .and(controller.RightBumper.get())
            .and(controller.RightTrigger.get())
            .onTrue(new InstantCommand(() -> Robot.KILLIT())));
  }
}
