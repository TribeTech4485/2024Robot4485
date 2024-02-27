package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.Constants.DeviceConstants;
import frc.robot.SyncedLibraries.SystemBases.ManipulatorBase;

public class Conveyor extends ManipulatorBase {
    public ColorSensorV3 colorSensor = new ColorSensorV3(Port.kOnboard);

    public Conveyor() {
        addMotors(new CANSparkMax(DeviceConstants.conveyorMotorId, CANSparkMax.MotorType.kBrushless));
        setMaxPower(DeviceConstants.conveyorPower);
        
    }

    @Override
    public void periodic() {
        SmartDashboard.putBoolean("Note in conveyor", isNoteIn());
        SmartDashboard.putNumber("Conveyor Proximity", colorSensor.getProximity());
        SmartDashboard.putBoolean("Conveyor running", isRunning());
    }

    @Override
    public void ESTOP() {
        setBrakeMode(false);
        fullStop();
    }

    @Override
    public Command test() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> setPower(0.25)),
                new WaitCommand(1),
                new InstantCommand(() -> setPower(0)));
    }

    public void intake() {
        intakeCommand().schedule();
    }

    public SequentialCommandGroup intakeCommand() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> setPower(1)),
                new WaitUntilCommand(this::isNoteIn),
                new InstantCommand(() -> setPower(0)));
    }

    public void sendToShooter() {
        sendToShooterCommand().schedule();
    }

    public SequentialCommandGroup sendToShooterCommand() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> setPower(0.5)),
                new WaitCommand(1),
                new InstantCommand(() -> setPower(0)));
    }

    public boolean isNoteIn() {
        return colorSensor.getProximity() > DeviceConstants.conveyorProximity;
    }
}
