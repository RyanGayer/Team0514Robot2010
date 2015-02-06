package edu.mphs.first.util;

import edu.mphs.first.interfaces.DigitalSideCarInterface;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
/**
 *
 * @author corey
 */
public class DriveUtil {

    boolean m_driveState;
    boolean m_driveToggle;
    int m_directionToggle; 

    public DriveUtil()
    {
        m_driveState = true;
        m_driveToggle = true;
        m_directionToggle = 1;
    }
// <---------------------- TeleOp Methods Follow -------------------------------->
    private void tankDrive(int directionalToggle, Jaguar m_leftJaguar, Jaguar m_rightJaguar,
                            Joystick m_leftJoystick, Joystick m_rightJoystick){
        // Manage control of the robot in tank drive mode.
        // Map each drive motor to the Y-axis of the cooresponding joystick
        m_leftJaguar.set(directionalToggle * -limitMix(m_leftJoystick.getY()));
        m_rightJaguar.set(directionalToggle * limitMix(m_rightJoystick.getY()));
    }

    private double limitMix(double IntermediateValue){
        // Re-code of the Limit_Mix function from the C++ libraries of days of old.

        if(IntermediateValue <= -1)
        {
            return -1;
        }
        else
        {
            if(IntermediateValue >= 1)
            {
                return 1;
            }
            else
            {
                return IntermediateValue;
            }
        }
    }

    private void arcadeDrive(int directionalToggle,
                            Jaguar m_leftJaguar, Jaguar m_rightJaguar,
                            Joystick m_leftJoystick, Joystick m_rightJoystick){
        // Manage control of the robot in arcade drive mode.
        // Map both drive motors to a single joystick
        m_rightJaguar.set(directionalToggle * limitMix(m_rightJoystick.getY() + m_rightJoystick.getX()));
        m_leftJaguar.set(directionalToggle * -limitMix(m_rightJoystick.getY() - m_rightJoystick.getX()));
    }

    private void toggleDriveState(Joystick toggleJoystick){
        // Toggle DriveState between true and false
        if(m_driveToggle && toggleJoystick.getRawButton(DigitalSideCarInterface.DRIVE_TOGGLE))
        {
            if(m_driveState)
            {
                m_driveState = false;
            }
            else
            {
                m_driveState = true;
            }
        }
        m_driveToggle = !toggleJoystick.getRawButton(DigitalSideCarInterface.DRIVE_TOGGLE);
    }

    public void driveTeleopRobot(Joystick m_leftJoystick, Joystick m_rightJoystick,
                                Jaguar m_leftJaguar, Jaguar m_rightJaguar){
        // Manages the drive motors and maps joysticks.
        // Toggle the DriveState if the toggle button was pressed
        toggleDriveState(m_rightJoystick);

        // Toggle the Directional Drive Toggle
        m_directionToggle = invertDrive(m_rightJoystick);

        // Check the state of DriveState and run the drive accordingly
        if(m_driveState)
        {
            // If DriveState is true, tankDrive
            tankDrive(m_directionToggle, m_leftJaguar, m_rightJaguar,
                        m_leftJoystick, m_rightJoystick);
        }
        else
        {
            // If DriveState is false, arcadeDrive
            arcadeDrive(m_directionToggle, m_leftJaguar, m_rightJaguar,
                        m_leftJoystick, m_rightJoystick);
        }
    }
    public String getDrive(){
        String DriveMode;
        if(m_driveState)
        {
            DriveMode = "Tank";
        }
        else
        {
            DriveMode = "Arcade";
        }
        return DriveMode;
    }

    private int invertDrive(Joystick m_rightJoystick)
    {
        // Drives are running backward because the getZ control is up so the
        // controller fits in the drriver station box.  So we invert it back
        // in the code
        if(m_rightJoystick.getZ() > 0)
        {
            return -1;
        }
        else
        {
            return 1;
        }
    }

    // <-------------------- Autonomous Methods Follow --------------------------->
    private void tankDrive(int directionalToggle, double rate,
                            Jaguar m_leftJaguar, Jaguar m_rightJaguar){
        m_leftJaguar.set(directionalToggle * -limitMix(rate));
        m_rightJaguar.set(directionalToggle * limitMix(rate));
    }
    private void arcadeDrive(int leftDirectionalToggle, int rightDirectionalToggle, double rate,
                            Jaguar m_leftJaguar, Jaguar m_rightJaguar){
        m_rightJaguar.set(rightDirectionalToggle * limitMix(rate));
        m_leftJaguar.set(leftDirectionalToggle * -limitMix(rate));

    }


    public void driveAutoRobot(int m_autonomousArea,
                               boolean m_driveState,
                               int m_leftDirectionalToggle,
                               int m_rightDirectionalToggle,
                               double m_rate,
                               Jaguar m_leftJaguar,
                               Jaguar m_rightJaguar){
        if (m_driveState){
                tankDrive(m_leftDirectionalToggle, m_rate,
                            m_leftJaguar, m_rightJaguar);
            }else{
                arcadeDrive(m_leftDirectionalToggle, m_rightDirectionalToggle, m_rate,
                            m_leftJaguar, m_rightJaguar);
            }
    }

    public void stopDrive(Jaguar m_leftJaguar, Jaguar m_rightJaguar){
         driveAutonomousRobot(1,
                              DigitalSideCarInterface.M_TANKDRIVE,
                              DigitalSideCarInterface.M_DIRECTION_NEUTRAL,
                              DigitalSideCarInterface.M_DIRECTION_NEUTRAL,
                              DigitalSideCarInterface.M_SPEED_STOP,
                              m_leftJaguar,
                              m_rightJaguar);

    }
    public void driveAutonomousRobot (int m_autonomousArea,
                                    boolean m_driveState,
                                    int m_leftDirectionalToggle,
                                    int m_rightDirectionalToggle,
                                    double m_rate,
                                    Jaguar m_leftJaguar,
                                    Jaguar m_rightJaguar){
        switch (m_autonomousArea){
            case DigitalSideCarInterface.M_AUTONOMOUS_DEFENSE:
                driveAutoRobot(m_autonomousArea,
                               m_driveState,
                               m_leftDirectionalToggle,
                               m_rightDirectionalToggle,
                               m_rate,
                               m_leftJaguar,
                               m_rightJaguar);
            break;
            case DigitalSideCarInterface.M_AUTONOMOUS_MIDFIELD:

            break;
            case DigitalSideCarInterface.M_AUTONOMOUS_OFFENSE:

            break;
        }
    }
}