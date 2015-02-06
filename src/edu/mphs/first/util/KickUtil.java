/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mphs.first.util;

import edu.mphs.first.interfaces.DigitalSideCarInterface;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;

/*
 *
 * @author Chris
 */
public class KickUtil {
        boolean m_timerRunning, m_primed, m_retryTimeRunning;
    public KickUtil()
    {
        m_retryTimeRunning = false;
        m_timerRunning = false;
        m_primed = false;
    }
    
// <--------------------- TeleOp Methods Follow ------------------------------------->
    public void manageKick(Joystick m_controlStick, Timer m_kickRetryTimer,
                            Timer m_kickReturnTimer,
                            DigitalInput m_retractedSwitch, DigitalInput m_kickedSwitch,
                            Solenoid m_primeSolenoid, Solenoid m_returnSolenoid,
                            Solenoid m_fireSolenoid,
                            Relay m_primer, AnalogChannel m_pot){
        if (!m_retryTimeRunning){
                m_kickRetryTimer.reset();
                m_retryTimeRunning = true;
            }
        if(m_retractedSwitch.get()){
            m_kickReturnTimer.reset();
            primeKickPNEU(m_returnSolenoid, m_primeSolenoid);
         if(m_controlStick.getRawButton(DigitalSideCarInterface.KICK_LATCH_RELEASE)){
        // Do not allow another key until the kick arm is retracted for at least 2 seconds!
            if ((DigitalSideCarInterface.KICK_WAIT) <= m_kickRetryTimer.get()){
                     m_fireSolenoid.set(true);
                     m_retryTimeRunning = false;
                }
         }
        }
        if(m_kickedSwitch.get()){
            resetPrimer(m_primer, m_pot);
            // Keep the m_timerRunning so that the Pot has time to unload the springs
            // before we attempt to return the kick arm to kick ready position
            if(m_timerRunning){
                if(m_kickReturnTimer.get() > DigitalSideCarInterface.RETRACT_WAIT){
                    returnKickArm(m_primeSolenoid, m_fireSolenoid, m_returnSolenoid);
                    m_kickReturnTimer.reset();
                    m_timerRunning  = false;
                }
            }else{
                m_kickReturnTimer.reset();
                m_timerRunning = true;
                }
        }
    }
    
    public void managePrimer(AnalogChannel m_pot, Joystick m_controlStick,
                             Relay m_primer){
        if (m_controlStick.getRawButton(DigitalSideCarInterface.PRIME_1) ||
                m_controlStick.getRawButton(DigitalSideCarInterface.PRIME_2))
        {
            if(m_controlStick.getRawButton(DigitalSideCarInterface.PRIME_1))
            {
                //m_primer.set(Relay.Value.kForward);
                loadHalfPrime(m_primer, m_pot);
            }
            else
            {
                //m_primer.set(Relay.Value.kReverse);
                loadFullPrime(m_primer, m_pot);
            }
        }
        else
        {
            m_primer.set(Relay.Value.kOff);
            resetPrimer(m_primer, m_pot);
        }
    }
    private void resetPrimer(Relay m_primer, AnalogChannel m_pot){
        //if (m_primed){
            if (m_pot.getAverageValue() >= DigitalSideCarInterface.PRIME_REST_UPPER_LIMIT)
                {
                    m_primer.set(Relay.Value.kReverse);
                }
            if (m_pot.getAverageValue() <= DigitalSideCarInterface.PRIME_REST_LOWER_LIMIT)
                {
                     m_primer.set(Relay.Value.kForward);
                }
            if (m_pot.getAverageValue() <= DigitalSideCarInterface.PRIME_REST_UPPER_LIMIT &&
                m_pot.getAverageValue() >= DigitalSideCarInterface.PRIME_REST_LOWER_LIMIT)
                {
                    m_primer.set(Relay.Value.kOff);
                    m_primed = false;
                }
            
         //}
    }
    private void loadHalfPrime(Relay m_primer, AnalogChannel m_pot){
        if (!m_primed){
            if (m_pot.getAverageValue() >= DigitalSideCarInterface.PRIME_HALF_UPPER_LIMIT)
                {
                    m_primer.set(Relay.Value.kReverse);
                }
            if (m_pot.getAverageValue() <= DigitalSideCarInterface.PRIME_HALF_LOWER_LIMIT)
                {
                    m_primer.set(Relay.Value.kForward);
                }
            if (m_pot.getAverageValue() <= DigitalSideCarInterface.PRIME_HALF_UPPER_LIMIT &&
                m_pot.getAverageValue() >= DigitalSideCarInterface.PRIME_HALF_LOWER_LIMIT)
                {
                    m_primer.set(Relay.Value.kOff);
                    m_primed = true;
                }
         }
    }
    private void loadFullPrime(Relay m_primer, AnalogChannel m_pot){
        if (!m_primed)
            {
            if (m_pot.getAverageValue() >= DigitalSideCarInterface.PRIME_FULL_UPPER_LIMIT)
                {
                    m_primer.set(Relay.Value.kReverse);
                }
            if (m_pot.getAverageValue() <= DigitalSideCarInterface.PRIME_FULL_LOWER_LIMIT)
                {
                    m_primer.set(Relay.Value.kForward);
                }
            if (m_pot.getAverageValue() <= DigitalSideCarInterface.PRIME_FULL_UPPER_LIMIT &&
                m_pot.getAverageValue() >= DigitalSideCarInterface.PRIME_FULL_LOWER_LIMIT)
                {
                    m_primer.set(Relay.Value.kOff);
                    m_primed = true;
                }
         }
    }

    // <---------------------------- Autonomous Mode Methods ------------------------------>
    private void manageKick(int m_autonomousArea, int m_myPeriodicLoops,
                            Solenoid m_primeSolenoid, Solenoid m_returnSolenoid,
                            Solenoid m_fireSolenoid,
                            Relay m_primer, AnalogChannel m_pot){

                   if (m_myPeriodicLoops == DigitalSideCarInterface.AUTO_KICK_LOOP_PRIME){
                       primeKickPNEU(m_returnSolenoid, m_primeSolenoid);
                    }
                    if (m_myPeriodicLoops == DigitalSideCarInterface.AUTO_KICK_LOOP_FIRE){
                        Kick(m_fireSolenoid);
                    }
                    if (m_myPeriodicLoops == DigitalSideCarInterface.AUTO_KICK_LOOP_UNLOAD){
                        unloadPrimer(m_autonomousArea, m_primer, m_pot);
                    }
                    if (m_myPeriodicLoops == DigitalSideCarInterface.AUTO_KICK_LOOP_RETURN){
                        returnKickArm(m_primeSolenoid, m_fireSolenoid, m_returnSolenoid);
                    }
    }
    private void Kick(Solenoid m_fireSolenoid){
             m_fireSolenoid.set(true);
    }

    private void primeKickPNEU (Solenoid m_returnSolenoid, Solenoid m_primeSolenoid){
            m_returnSolenoid.set(false);
            m_primeSolenoid.set(true);
    }
    private void returnKickArm(Solenoid m_primeSolenoid, Solenoid m_fireSolenoid,
                               Solenoid m_returnSolenoid){
                    m_primeSolenoid.set(false);
                    m_fireSolenoid.set(false);
                    m_returnSolenoid.set(true);
    }

    public void unloadPrimer(int m_autonomousArea, Relay m_primer, AnalogChannel m_pot){
            resetPrimer(m_primer, m_pot);
    }

    public void loadHalfPrime(int m_autonomousArea, Relay m_primer, AnalogChannel m_pot){
            loadHalfPrime(m_primer, m_pot);
    }
    public void loadFullPrime(int m_autonomousArea, Relay m_primer, AnalogChannel m_pot){
                loadFullPrime(m_primer, m_pot);
    }

    public void autoKick(int m_autonomousArea, int m_myPeriodicLoops,
                            Solenoid m_primeSolenoid, Solenoid m_returnSolenoid,
                            Solenoid m_fireSolenoid,
                            Relay m_primer, AnalogChannel m_pot){

        switch (m_autonomousArea){

            case DigitalSideCarInterface.M_AUTONOMOUS_DEFENSE:
                 manageKick(m_autonomousArea, m_myPeriodicLoops,
                            m_primeSolenoid, m_returnSolenoid,
                            m_fireSolenoid,
                            m_primer, m_pot);
            break;

            case DigitalSideCarInterface.M_AUTONOMOUS_MIDFIELD:

            break;

            case DigitalSideCarInterface.M_AUTONOMOUS_OFFENSE:

            break;
        }
    }

}