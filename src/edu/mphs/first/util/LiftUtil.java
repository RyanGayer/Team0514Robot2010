/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mphs.first.util;

import edu.mphs.first.interfaces.DigitalSideCarInterface;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;
/**
 *
 * @author Chris
 */
public class LiftUtil {
    public LiftUtil(){
    }
    public void manageLift(Joystick m_controlStick, Victor m_winchVictor,
                            Solenoid m_boomArmDown, Solenoid m_boomArmUp){
        controlArm(m_controlStick, m_boomArmDown, m_boomArmUp);

        if (m_controlStick.getRawButton(DigitalSideCarInterface.WINCH_UP) ||
                m_controlStick.getRawButton(DigitalSideCarInterface.WINCH_DOWN)){
            controlWinch(m_controlStick, m_winchVictor);
        }else{
            m_winchVictor.set(0);
        }
    }
    private void controlArm(Joystick m_controlStick, Solenoid m_boomArmDown,
                            Solenoid m_boomArmUp){
        if (m_controlStick.getRawButton(DigitalSideCarInterface.WINCH_ARM))
        {
            m_boomArmDown.set(false);
            m_boomArmUp.set(true);
        }else{
            m_boomArmUp.set(false);
            m_boomArmDown.set(true);
        }
    }
    private void controlWinch(Joystick m_controlStick, Victor m_winchVictor){
        if (m_controlStick.getRawButton(DigitalSideCarInterface.WINCH_UP))
        {
            m_winchVictor.set(1);
        }
        if (m_controlStick.getRawButton(DigitalSideCarInterface.WINCH_DOWN))
        {
            m_winchVictor.set(-1);
        }
    }
}
