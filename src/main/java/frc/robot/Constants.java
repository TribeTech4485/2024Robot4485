package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean
 * constants. This class should not be used for any other purpose. All constants
 * should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static final class DriveConstants {
    public static final int rightDrive1Id = 1;
    public static final int rightDrive2Id = 2;
    public static final int leftDrive1Id = 3;
    public static final int leftDrive2Id = 4;
    public static final int GearChangeUp = 0;
    public static final int GearChangeDown = 1;

    public static final int driveAmpsMax = 30;
    public static final int drivingExponent = 1;
    public static final double drivingRamp = 0;
    public static final double controllerJoystickDeadband = 0.15;
    public static final double controllerTriggerDeadband = 0.05;
    public static final double drivingMax = 1;

    public static final double WHEEL_DIAMETER = 3; // inches
    public static final double PULSES_PER_REVOLUTION = 1;
  }

  /**
   * For all non-driving devices
   */
  public static final class DeviceConstants {
    public static final int PCMId = 6;
    public static final int intakeMotorId = 8;
    public static final int shooterMotor2Id = 5;
    public static final int shooterMotor1Id = 7;
    public static final int conveyorMotorId = 6;
    public static final int turretMotor1Id = 9;
    public static final int turretMotor2Id = 10;
    public static final int LightPWM = 0;

    public static final double intakeSpeedKP = 0.00001;
    public static final double intakeSpeedKI = 0.00075;
    public static final double intakeSpeedKD = 0.00000;
    public static final double intakeSpeedTolerance = 0;
    public static final int intakeAmpsMax = 20;

    public static final double shooterSpeedKP = 0.00005;
    public static final double shooterSpeedKI = 0.0005;
    public static final double shooterSpeedKD = 0;
    public static final double shooterSpeedTolerance = 25;
    public static final int shooterDefaultSpeed = 5000;
    public static final int shooterAmpsMax = 20;

    public static final double turretPositionKP = 0.002;
    public static final double turretPositionKI = 0;
    public static final double turretPositionKD = 0;
    public static final double turretPositionTolerance = 0;
    public static final double turretPositionMultiplier = 1 / (360 * 100);
    public static final double turretMaxPower = 0.25;
    public static final int turretAmpsMax = 20;

    public static final double conveyorProximity = 100;
    public static final double conveyorPower = 1;
    public static final int conveyorAmpsMax = 30;
  }

  public static final class WiringConstants {
    public static final int RightDrive1 = 0;
    public static final int RightDrive2 = 1;
    public static final int LeftDrive1 = 15;
    public static final int LeftDrive2 = 14;
    public static final int armExtender = 8;
    public static final int armLifter = 0;
    public static final int VisionLight = 0;
  }

  @Deprecated
  public static final class OIConstants {
    // Xbox Controller button mappings
    public static final int kXboxButtonA = 1;
    public static final int kXboxButtonB = 2;
    public static final int kXboxButtonX = 3;
    public static final int kXboxButtonY = 4;
    public static final int kXboxBumperLeft = 5;
    public static final int kXboxBumperRight = 6;
    public static final int kXboxButtonBack = 7;
    public static final int kXboxButtonStart = 8;
    public static final int kXboxStickPressLeft = 9;
    public static final int kXboxStickPressRight = 10;

    public static final int kXboxAxisLeftStickX = 0; // for .getRawAxis()
    public static final int kXboxAxisLeftStickY = 1;
    public static final int kXboxAxisLeftTrigger = 2;
    public static final int kXboxAxisRightTrigger = 3;
    public static final int kXboxAxisRightStickX = 4;
    public static final int kXboxAxisRightStickY = 5;

    // Logitech Controller axis mappings
    public static final int kLogiAxisLeftStickX = 1;
    public static final int kLogiAxisLeftStickY = 2;
    public static final int kLogiAxisTriggers = 3; // left trigger only=-1.0, right only=1.0, both/none=0.0
    public static final int kLogiAxisRightStickX = 4;
    public static final int kLogiAxisRightStickY = 5;
    public static final int kLogiAxisDpad = 6;

    public static final int kLogiButtonA = 1;
    public static final int kLogiButtonB = 2;
    public static final int kLogiButtonX = 3;
    public static final int kLogiButtonY = 4;
    public static final int kLogiBumperLeft = 5;
    public static final int kLogiBumperRight = 6;
    public static final int kLogiButtonBack = 7;
    public static final int kLogiButtonStart = 8;
    public static final int kLogiStickPressLeft = 9;
    public static final int kLogiStickPressRight = 10;

    // Playstation Controller axis mappings
    public static final int kPlaystationAxisLeftStickX = 0;
    public static final int kPlaystationAxisLeftStickY = 1;
    public static final int kPlaystationAxisLeftTrigger = 3;
    public static final int kPlaystationAxisRightTrigger = 4;
    public static final int kPlaystationAxisRightStickX = 2;
    public static final int kPlaystationAxisRightStickY = 5;
    public static final int kPlaystationAxisDpad = 6;

    public static final int kPlaystationButtonSquare = 1;
    public static final int kPlaystationButtonX = 2;
    public static final int kPlaystationButtonCircle = 3;
    public static final int kPlaystationButtonTriangle = 4;
    public static final int kPlaystationBumperLeft = 5;
    public static final int kPlaystationBumperRight = 6;
    public static final int kPlaystationLeftTrigger = 7;
    public static final int kPlaystationRightTrigger = 8;
    public static final int kPlaystationStickPressLeft = 11;
    public static final int kPlaystationStickPressRight = 12;
    public static final int kPlaystationShareButton = 9;
    public static final int kPlaystationOptions = 10;
    public static final int kPlaystationStartButton = 13;
    public static final int kPlaystationDuelsBigButton = 14;

    // Logitech ATK3 Controller axis mappings
    public static final int kATK3AxisStickyLeftyRighty = 0;
    public static final int kATK3AxisStickyUpDown = 1;
    public static final int kATK3AxisBackSlidey = 2;

    public static final int kATK3BigTrigge = 1;
    public static final int kATK3Button2 = 2;
    public static final int kATK3Button3 = 3;
    public static final int kATK3Button4 = 4;
    public static final int kATK3Button5 = 5;
    public static final int kATK3Button6 = 6;
    public static final int kATK3Button7 = 7;
    public static final int kATK3Button8 = 8;
    public static final int kATK3Button9 = 9;
    public static final int kATK3Button10 = 10;
    public static final int kATK3Button11 = 11;
    public static final int kATK3Button12 = 12;
    public static final int kATK3Button13 = 13;
    public static final int kATK3Button14 = 14;

    // RobotContainer.controller0.getType() Ps4 = kHIDGamepad Xbox = kXInputGamepad
    // ATK3 = kHIDJoystick
    // public static int SmartMap(GenericHID controller, String ButtonName) {
    // int ButtonID = 1;
    // HIDType hidType = null;
    // try {
    // hidType = controller.getType();
    // } catch (java.lang.IllegalArgumentException e) {
    // System.out.println("Exception: " + e + " || " + controller.getPort());
    // return 0;
    // }
    // HIDType isXBox = HIDType.kXInputGamepad;
    // HIDType isPS4 = HIDType.kHIDGamepad;
    // HIDType isJoystick = HIDType.kHIDJoystick;
    // switch (ButtonName) {
    // case "A":
    // if (hidType == isXBox) {
    // ButtonID = 1;
    // } else if (hidType == isPS4) {
    // ButtonID = 2;
    // } else {
    // }
    // break;
    // case "B":
    // if (hidType == isXBox) {
    // ButtonID = 2;
    // } else if (hidType == isPS4) {
    // ButtonID = 3;
    // } else {
    // }
    // break;
    // case "X":
    // if (hidType == isXBox) {
    // ButtonID = 3;
    // } else if (hidType == isPS4) {
    // ButtonID = 1;
    // } else {
    // }
    // break;
    // case "Y":
    // if (hidType == isXBox) {
    // ButtonID = 4;
    // } else if (hidType == isPS4) {
    // ButtonID = 4;
    // } else {
    // }
    // break;
    // case "LBump":
    // if (hidType == isXBox) {
    // ButtonID = 5;
    // } else if (hidType == isPS4) {
    // ButtonID = 5;
    // } else {
    // }
    // break;
    // case "RBump":
    // if (hidType == isXBox) {
    // ButtonID = 6;
    // } else if (hidType == isPS4) {
    // ButtonID = 6;
    // } else {
    // }
    // break;
    // case "LStick":
    // if (hidType == isXBox) {
    // ButtonID = 9;
    // } else if (hidType == isPS4) {
    // ButtonID = 11;
    // } else {
    // }
    // break;
    // case "RStick":
    // if (hidType == isXBox) {
    // ButtonID = 10;
    // } else if (hidType == isPS4) {
    // ButtonID = 12;
    // } else {
    // }
    // break;
    // case "DoubleSquare": // PS4 = share XBOX IS UNSURE
    // if (hidType == isXBox) {
    // ButtonID = 7;
    // } else if (hidType == isPS4) {
    // ButtonID = 9;
    // } else {
    // }
    // break;
    // case "Options":
    // if (hidType == isXBox) {
    // ButtonID = 8;
    // } else if (hidType == isPS4) {
    // ButtonID = 10;
    // } else {
    // }
    // break;
    // case "Xbox": // XBOX IS UNSURE
    // if (hidType == isXBox) {
    // } else if (hidType == isPS4) {
    // ButtonID = 13;
    // } else {
    // }
    // break;
    // case "Touchpad": // XBOX DOES NOT EXIST (map as xbox button once found)
    // if (hidType == isXBox) {
    // } else if (hidType == isPS4) {
    // ButtonID = 14;
    // } else {
    // }
    // break;
    // case "Trigger":
    // if (hidType == isXBox) {
    // } else if (hidType == isPS4) {
    // } else if (hidType == isJoystick) {
    // ButtonID = 1;
    // } else {
    // }
    // break;
    // case "RTrigger":
    // if (hidType == isXBox) {
    // } else if (hidType == isPS4) {
    // ButtonID = 8;
    // } else {
    // }
    // break;
    // case "LTrigger":
    // if (hidType == isXBox) {
    // } else if (hidType == isPS4) {
    // ButtonID = 7;
    // } else {
    // }
    // break;
    // case "":
    // if (hidType == isXBox) {
    // ButtonID = 1;
    // } else if (hidType == isPS4) {
    // ButtonID = 1;
    // } else {
    // }
    // break;
    // default:
    // try {
    // ButtonID = Integer.parseInt(ButtonName);
    // break;
    // } catch (NumberFormatException e) {
    // break;
    // }
    // }
    // // System.out.println("SmartMap " + ButtonName + " Port:" + ButtonID);
    // return ButtonID;
    // }
  }
}
