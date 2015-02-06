/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.mphs.first.wpilibj;


// Start MPHS Robotics Team 514 Class Imports
import edu.mphs.first.interfaces.DigitalSideCarInterface;
import edu.mphs.first.util.PrintUtil;
import edu.mphs.first.util.DriveUtil;
import edu.mphs.first.util.KickUtil;
import edu.mphs.first.util.LiftUtil;
// End MPHS Robotics Team 514 Class Imports
import edu.wpi.first.wpilibj.AnalogChannel;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Dashboard;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Team0514Robot extends IterativeRobot {
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
     /** Team 0514 Notes:
      * 
      * Global Variables:
      * All Global Variables MUST be placed in the RobotInterface.java.  If you
      * are defining a STATIC FINAL variable then it is a candidate for being
      * placed in the Interface!
      *
      * Common Objects:
      * All Object declarations MUST be placed in the Team0514Robot.java as
      * is being done below.
      *
      */
     // Declare variable for the robot drive system
	DriveUtil m_robotDrive;

    // Declare variable for the robot lift system
	LiftUtil m_robotLift;

    // Declare KickUtil object
        KickUtil m_robotKick;
        boolean m_kickReady, m_kickReset;
        Solenoid m_primeSolenoid, m_returnSolenoid, m_fireSolenoid;
        DigitalInput m_retractedSwitch, m_kickedSwitch;
        Timer m_kickReturnTimer, m_kickRetryTimer;
        boolean m_timerRunning;
        Relay m_primer;
        AnalogChannel m_pot;
        int m_potValue, m_autonomousArea;
        boolean m_primed;
        boolean m_retryTimeRunning;

        Solenoid m_boomArmUp, m_boomArmDown;



	// Declare a variable to use to access the driver station object
	DriverStation m_ds;                     // driver station object
	int m_priorPacketNumber;                // keep track of the most recent packet number from the DS
	int m_dsPacketsReceivedInCurrentSecond;	// keep track of the ds packets received in the current second

        // Declare a variable to use to access the Dashboard object
        Dashboard m_db;
        int m_commitBytes;

	// Declare variables for the two joysticks being used
	Joystick m_rightStick;			// joystick 1 (arcade stick or right tank stick)
	Joystick m_leftStick;			// joystick 2 (tank left stick)
	Joystick m_controller;			// joystick 3 (tank left stick)

        // Declare a the Compressor object
	Compressor m_comp;                     

        // Declare a variable to use to access the Encoder object
        boolean reverseDirection = false;
        private EncodingType m_encodingType = EncodingType.k4X;
        Encoder m_lEncoder, m_rEncoder;                     // Encoder object


        // Declare Timer object and variables
        private double m_accumulatedTime;
        boolean m_running;
	Timer m_timer;                     // Timer object


	// Local variables to count the number of periodic loops performed
	int m_autoPeriodicLoops;
        int m_myPeriodicLoops;
	int m_disabledPeriodicLoops;
	int m_telePeriodicLoops;
        boolean m_autonomous;
        String msg;

        // Instantiate the Jaguars on the left and right side and Victor for Winch
        Jaguar m_leftJaguar;
        Jaguar m_rightJaguar;
        Victor m_winchVictor;

        // Instantiate the spikes for the priming bar and ball magnet
        Relay m_ballMagnet;

        //DASHBOARD CODE-----------------------------------------------------------------2/2/10---------------------------------------------------------
        DriverStationLCD m_dsLCD = DriverStationLCD.getInstance();

        // Digital Input for the Autonomous Switch
        DigitalInput m_autonomousArea2, m_autonomousArea3;
        // Area 1 = Defensive Zone - Default Autonomous Mode
        // Area 2 = Mid Field Zone
        // Area 3 = Offensive Zone
        double m_distance;
        int m_step;



    public Team0514Robot() {
     /**
     * Constructor for this "Team0514Robot" Class.
     *
     * The constructor creates all of the objects used for the different inputs and outputs of
     * the robot.  Essentially, the constructor defines the input/output mapping for the robot,
     * providing named objects for each of the robot interfaces.
     */

        
        // Define joysticks being used at USB port #1 and USB port #2 on the Drivers Station
	m_rightStick = new Joystick(DigitalSideCarInterface.RIGHT_JOYSTICK);
	m_leftStick = new Joystick(DigitalSideCarInterface.LEFT_JOYSTICK);
        m_controller = new Joystick(DigitalSideCarInterface.CONTROLLER);
        m_leftJaguar = new Jaguar(DigitalSideCarInterface.LEFT_JAGUAR);
        m_rightJaguar = new Jaguar(DigitalSideCarInterface.RIGHT_JAGUAR);

        //Create the Compressor
        m_comp = new Compressor (DigitalSideCarInterface.COMP_P_SWITCH_CHANNEL,
                                 DigitalSideCarInterface.COMP_C_RELAY_CHANNEL);

        //Instantiate the Encoder
        m_lEncoder = new Encoder (DigitalSideCarInterface.ENCODER_L_SLOT_A,
                                DigitalSideCarInterface.ENCODER_L_CHANNEL_A,
                                DigitalSideCarInterface.ENCODER_L_SLOT_B,
                                DigitalSideCarInterface.ENCODER_L_CHANNEL_B,
                                reverseDirection,m_encodingType);

        m_rEncoder = new Encoder (DigitalSideCarInterface.ENCODER_R_SLOT_A,
                                DigitalSideCarInterface.ENCODER_R_CHANNEL_A,
                                DigitalSideCarInterface.ENCODER_R_SLOT_B,
                                DigitalSideCarInterface.ENCODER_R_CHANNEL_B,
                                reverseDirection,m_encodingType);

        // Instantiate all KickUtil Objects
        m_primeSolenoid = new Solenoid(DigitalSideCarInterface.PRIME_KICK_PNEU);
        m_returnSolenoid = new Solenoid(DigitalSideCarInterface.RETURN_KICK_PNEU);
        m_fireSolenoid = new Solenoid(DigitalSideCarInterface.FIRE_KICK_PNEU);
        m_kickedSwitch =  new DigitalInput(DigitalSideCarInterface.KICK_KICKED_SW);
        m_retractedSwitch = new DigitalInput(DigitalSideCarInterface.KICK_RETRACTED_SW);
        m_kickReturnTimer = new Timer();
        m_kickRetryTimer = new Timer();
        m_retryTimeRunning = false;
        m_timerRunning = false;
        m_primer = new Relay(DigitalSideCarInterface.PRIME_BAR_RELAY);
        m_primer.setDirection(Relay.Direction.kBoth);
        m_pot = new AnalogChannel(DigitalSideCarInterface.POT1_SLOT);
        m_pot.resetAccumulator();
        m_primed = true;
        m_kickReset = false;

        // Instantiate all LiftUtil Objects
        m_winchVictor = new Victor(DigitalSideCarInterface.WINCH_VICTOR);
        m_boomArmUp = new Solenoid(DigitalSideCarInterface.FIRE_BOOM_PNEU);
        m_boomArmDown = new Solenoid(DigitalSideCarInterface.RETURN_BOOM_PNEU);

        // Acquire the Driver Station object
	m_ds = DriverStation.getInstance();
	m_priorPacketNumber = 0;
	m_dsPacketsReceivedInCurrentSecond = 0;

        //Acquire the Dashboard Packer Object from the Driver Station
        m_db = m_ds.getDashboardPackerLow();

	// Initialize counters to record the number of loops completed in autonomous and teleop modes
	m_autoPeriodicLoops = 0;
        m_myPeriodicLoops = 0;
	m_disabledPeriodicLoops = 0;
	m_telePeriodicLoops = 0;

        // Initialize the relays
        m_ballMagnet = new Relay(DigitalSideCarInterface.DRUM_DRIVE_RELAY);
        m_ballMagnet.setDirection(Relay.Direction.kForward);

        m_kickReady = true;

        //Instantiate Autonomous Mode Switches
        m_autonomousArea2 = new DigitalInput(DigitalSideCarInterface.AUTONOMOUS_MIDFIELD_SW);
        m_autonomousArea3 = new DigitalInput(DigitalSideCarInterface.AUTONOMOUS_OFFENSE_SW);
        m_distance = 0;

        // Create a robot using standard right/left robot drive on PWMS 1 and 2 Only
        m_robotDrive = new DriveUtil();

        // Create a robot using Victor Drive (Winch)on PWM 3
        m_robotLift = new LiftUtil();

        // Instantiate the KickUtil
        m_robotKick = new KickUtil();
            m_step = 0;
            
        System.out.println("Team0514 Robot::Constructor Completed\n");
	}

	/********************************** Init Routines *************************************/

    public void robotInit() {

        //Establish Start Time
        m_timer = new Timer();
       //Reset for Run Timer
        m_timer.reset();
        m_running = false;
        m_kickReturnTimer.start();
        m_kickRetryTimer.start();
        
        //Print Log
        msg ="Compressor started";
        PrintUtil.printMSG(DigitalSideCarInterface.M_DEBUG_LEVEL, msg);

        //Start the compressor thread
        m_comp.start();

    }

  /* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   *  If we choose to replace the MAIN LOOP code we would override the 
   *  IterativeRobot.startCompetition() method.
   *  Right now we are NOT going to change that code!!
   * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   *  public void startCompetition() {
   *      super.startCompetition();
   *  }
 */

    public void autonomousInit() {
        msg = "AutonomousInit!";
        PrintUtil.printMSG(DigitalSideCarInterface.M_DEBUG_LEVEL, msg);

        //Start the Encoder
            m_lEncoder.start();
            m_rEncoder.start();
            m_lEncoder.reset();
            m_rEncoder.reset();

        //Place our autonomousInit() code here!
        
        m_autonomous = true;
        m_autoPeriodicLoops = 0;
        m_myPeriodicLoops = 0;

        //Start the Autonomous Timer
            m_timer.stop();
            m_timer.reset();
            m_timer.start();
            m_kickRetryTimer.reset();
            m_kickReturnTimer.reset();
            m_accumulatedTime = 0;
            m_step = 0;
            

        // Read the new switch to determine what area of the arena we are operating in.
        // Set the default to 1 and read switches to determine override if any.

            m_autonomousArea = DigitalSideCarInterface.M_AUTONOMOUS_DEFENSE;
            /** Comment this in when the switches are installed!!!
            if (m_autonomousArea2.get()){
                    m_autonomousArea = DigitalSideCarInterface.M_AUTONOMOUS_MIDFIELD;
                }else{
                    if (m_autonomousArea3.get()){
                            m_autonomousArea = DigitalSideCarInterface.M_AUTONOMOUS_OFFENSE;
                       }
                }
            **/

    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        // Get Instance of Watchdog and feed
        Watchdog.getInstance().feed();
         //Increment Counter
        m_autoPeriodicLoops++;

        
        msg = "AutonomousPeriodic! Loop Counter = " + m_autoPeriodicLoops;
        PrintUtil.printMSG(DigitalSideCarInterface.M_DEBUG_LEVEL, msg);

       //Place our autonomousPeriodic() code here!
       //  Need to figure out the Dashboard code!
        m_accumulatedTime = m_timer.get();
            if (m_accumulatedTime >= DigitalSideCarInterface.M_AUTO_TIMELIMIT) {
               //Stop the Robot!
                m_robotDrive.stopDrive(m_leftJaguar, m_rightJaguar);
                m_ballMagnet.set(Relay.Value.kOff);
                m_autonomous = false;
                msg = "Auto:OFF";
                PrintUtil.printLCD(m_dsLCD, 2, 1, msg);
            }else{
                msg = "Auto:ON ";
                PrintUtil.printLCD(m_dsLCD, 2, 1, msg);
                msg = "AutoZone:" + m_autonomousArea;
                PrintUtil.printLCD(m_dsLCD, 3, 1, msg);
                }
            if (m_autonomous){
                msg = "AutoRunTime:" + m_accumulatedTime;
                PrintUtil.printLCD(m_dsLCD, 4, 1, msg);
                AutonomousRobot();
            }

    }

    public void teleopInit() {
        msg = "TeleopInit() method running";
        PrintUtil.printMSG(DigitalSideCarInterface.M_DEBUG_LEVEL, msg);

        m_telePeriodicLoops = 0;
        //Start the Encoder
            m_lEncoder.start();

        //Reset the Timer for TeleOp Use
            m_timer.stop();
            m_timer.reset();
            m_timer.start();
            m_accumulatedTime = 0;

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        // Get Instance of Watchdog and feed
        Watchdog.getInstance().feed();
        // Get Match Running Time
        m_accumulatedTime = m_timer.get();
        //Increment Counter
        m_telePeriodicLoops++;
        //Place our teleopPeriodic() code here!

        m_robotDrive.driveTeleopRobot(m_leftStick, m_rightStick,
                                        m_leftJaguar, m_rightJaguar);

        m_robotKick.manageKick(m_controller, 
                                m_kickRetryTimer, m_kickReturnTimer,
                                m_retractedSwitch, m_kickedSwitch,
                                m_primeSolenoid, m_returnSolenoid, m_fireSolenoid,
                                m_primer, m_pot);

        m_robotKick.managePrimer(m_pot, m_controller, m_primer);

        if (m_accumulatedTime <= DigitalSideCarInterface.M_ACTIVATE_SCISSOR)
        {
            m_robotLift.manageLift(m_controller, m_winchVictor, m_boomArmDown, m_boomArmUp);
        }
        if(m_controller.getRawButton(DigitalSideCarInterface.DRUM_DRIVE))
        {
            m_ballMagnet.set(Relay.Value.kOn);
        }
        else
        {
            m_ballMagnet.set(Relay.Value.kOff);
        }

        //The code that follows is from the FIRST WPI supplied DefaultRobot
        //but is in fact refactored to become our robot framework...
	/*
	 * Code placed in here will be called only when a new packet of information
	 * has been received by the Driver Station.  Any code which needs new information
	 * from the DS should go in here
	 */
	m_dsPacketsReceivedInCurrentSecond++;	// increment DS packets received
    }

    public void disabledInit() {

        //Place our disabledInit() code here!
        m_disabledPeriodicLoops = 0;
        m_lEncoder.stop();
        m_lEncoder.reset();
        m_timer.stop();
        m_timer.reset();
    }

    public void disabledPeriodic() {
        // Get Instance of Watchdog and feed
        Watchdog.getInstance().feed();
        //Increment Counter
        m_disabledPeriodicLoops++;
        //Place our disabledPeriodic() code here!

    }
    private void AutonomousRobot(){
        switch (m_autonomousArea){
            case DigitalSideCarInterface.M_AUTONOMOUS_DEFENSE:
                if (m_step == 0){
                    //Drive Forward to kick the ball...
                    m_ballMagnet.set(Relay.Value.kOn);
                    m_robotKick.loadFullPrime(m_autonomousArea, m_primer, m_pot);
                    m_robotDrive.driveAutonomousRobot(m_autonomousArea,
                                                        DigitalSideCarInterface.M_TANKDRIVE,
                                                        DigitalSideCarInterface.M_DIRECTION_FORWARD,
                                                        DigitalSideCarInterface.M_DIRECTION_NEUTRAL,
                                                        DigitalSideCarInterface.M_SPEED_MEDIUM,
                                                        m_leftJaguar,
                                                        m_rightJaguar);

                    msg = "2Ball. D=" + m_lEncoder.getDistance();
                    PrintUtil.printLCD(m_dsLCD, 5, 1, msg);

                    m_distance = m_lEncoder.getDistance();

                    if (m_distance < 0){
                        m_distance = m_distance * -1;
                    }
                    if (m_distance >= DigitalSideCarInterface.M_DISTANCE_TO_BALL){
                        m_robotDrive.stopDrive(m_leftJaguar, m_rightJaguar);
                        m_step++;
                        m_lEncoder.reset();
                    }
                }
                if (m_step == 1){
                    m_myPeriodicLoops++;
                    if (m_myPeriodicLoops <= DigitalSideCarInterface.AUTO_KICK_LIMIT){
                        m_robotKick.autoKick(m_autonomousArea, m_myPeriodicLoops,
                                             m_primeSolenoid, m_returnSolenoid, m_fireSolenoid,
                                             m_primer, m_pot);
                    }else{
                        m_robotKick.unloadPrimer(m_autonomousArea, m_primer, m_pot);
                        m_myPeriodicLoops = 0;
                        m_step++;
                    }
                }
                if (m_step == 2){
                    //Drive Backward to your starting position...
                    m_robotDrive.driveAutonomousRobot(m_autonomousArea,
                                                        DigitalSideCarInterface.M_TANKDRIVE,
                                                        DigitalSideCarInterface.M_DIRECTION_REVERSE,
                                                        DigitalSideCarInterface.M_DIRECTION_NEUTRAL,
                                                        DigitalSideCarInterface.M_SPEED_MEDIUM,
                                                        m_leftJaguar,
                                                        m_rightJaguar);

                    msg = "BackUp. D=" + m_lEncoder.getDistance();
                    PrintUtil.printLCD(m_dsLCD, 5, 1, msg);

                    m_distance = m_lEncoder.getDistance();

                    if (m_distance < 0){
                        m_distance = m_distance * -1;
                    }

                    if (m_distance >= DigitalSideCarInterface.M_DISTANCE_TO_BALL){
                        m_robotDrive.stopDrive(m_leftJaguar, m_rightJaguar);
                        m_step++;
                        m_lEncoder.reset();
                    }
                }

                if (m_step == 3){
                    //Turn Right 90 Degrees...
                    m_robotDrive.driveAutonomousRobot(m_autonomousArea,
                                                     DigitalSideCarInterface.M_ARCADEDRIVE,
                                                     DigitalSideCarInterface.M_DIRECTION_FORWARD,
                                                     DigitalSideCarInterface.M_DIRECTION_REVERSE,
                                                     DigitalSideCarInterface.M_SPEED_LOW,
                                                     m_leftJaguar,
                                                     m_rightJaguar);

                    msg = "TR. D=" + m_lEncoder.getDistance();
                    PrintUtil.printLCD(m_dsLCD, 5, 1, msg);

                    m_distance = m_lEncoder.getDistance();

                    if (m_distance < 0){
                        m_distance = m_distance * -1;
                    }

                    if (m_distance >= DigitalSideCarInterface.M_DISTANCE_TO_90){
                        m_robotDrive.stopDrive(m_leftJaguar, m_rightJaguar);
                        m_step++;
                        m_lEncoder.reset();
                    }

                }
                if (m_step == 4){
                    //Drive Forward to defend the goal...
                    m_robotDrive.driveAutonomousRobot(m_autonomousArea,
                                                        DigitalSideCarInterface.M_TANKDRIVE,
                                                        DigitalSideCarInterface.M_DIRECTION_FORWARD,
                                                        DigitalSideCarInterface.M_DIRECTION_NEUTRAL,
                                                        DigitalSideCarInterface.M_SPEED_MEDIUM,
                                                        m_leftJaguar,
                                                        m_rightJaguar);

                    msg = "2Goal. D=" + m_lEncoder.getDistance();
                    PrintUtil.printLCD(m_dsLCD, 5, 1, msg);

                    m_distance = m_lEncoder.getDistance();

                    if (m_distance < 0){
                        m_distance = m_distance * -1;
                    }

                    if (m_distance >= DigitalSideCarInterface.M_DISTANCE_TO_GOAL){
                        m_robotDrive.stopDrive(m_leftJaguar, m_rightJaguar);
                        m_step++;
                        m_lEncoder.reset();
                    }
                }
            break;
            case DigitalSideCarInterface.M_AUTONOMOUS_MIDFIELD:
                if (m_step == 0){
                    //Drive Forward to kick the ball...
                    m_ballMagnet.set(Relay.Value.kOn);
                    m_robotKick.loadHalfPrime(m_autonomousArea, m_primer, m_pot);
                    m_robotDrive.driveAutonomousRobot(m_autonomousArea,
                                                        DigitalSideCarInterface.M_TANKDRIVE,
                                                        DigitalSideCarInterface.M_DIRECTION_FORWARD,
                                                        DigitalSideCarInterface.M_DIRECTION_NEUTRAL,
                                                        DigitalSideCarInterface.M_SPEED_MEDIUM,
                                                        m_leftJaguar,
                                                        m_rightJaguar);

                    msg = "2Ball. D=" + m_lEncoder.getDistance();
                    PrintUtil.printLCD(m_dsLCD, 5, 1, msg);

                    m_distance = m_lEncoder.getDistance();

                    if (m_distance < 0){
                        m_distance = m_distance * -1;
                    }
                    if (m_distance >= DigitalSideCarInterface.M_DISTANCE_TO_BALL){
                        m_robotDrive.stopDrive(m_leftJaguar, m_rightJaguar);
                        m_step++;
                        m_lEncoder.reset();
                    }
                }
                if (m_step == 1){
                    m_myPeriodicLoops++;
                    if (m_myPeriodicLoops <= DigitalSideCarInterface.AUTO_KICK_LIMIT){
                        m_robotKick.autoKick(m_autonomousArea, m_myPeriodicLoops,
                                             m_primeSolenoid, m_returnSolenoid, m_fireSolenoid,
                                             m_primer, m_pot);
                    }else{
                        m_robotKick.unloadPrimer(m_autonomousArea, m_primer, m_pot);
                        m_myPeriodicLoops = 0;
                        m_step++;
                    }
                }
                if (m_step == 2){
                    //Drive Backward to your starting position...
                    m_robotDrive.driveAutonomousRobot(m_autonomousArea,
                                                        DigitalSideCarInterface.M_TANKDRIVE,
                                                        DigitalSideCarInterface.M_DIRECTION_REVERSE,
                                                        DigitalSideCarInterface.M_DIRECTION_NEUTRAL,
                                                        DigitalSideCarInterface.M_SPEED_MEDIUM,
                                                        m_leftJaguar,
                                                        m_rightJaguar);

                    msg = "BackUp. D=" + m_lEncoder.getDistance();
                    PrintUtil.printLCD(m_dsLCD, 5, 1, msg);

                    m_distance = m_lEncoder.getDistance();

                    if (m_distance < 0){
                        m_distance = m_distance * -1;
                    }

                    if (m_distance >= DigitalSideCarInterface.M_DISTANCE_TO_BALL){
                        m_robotDrive.stopDrive(m_leftJaguar, m_rightJaguar);
                        m_step++;
                        m_lEncoder.reset();
                    }
                }

                if (m_step == 3){
                    //Turn Right 90 Degrees...
                    m_robotDrive.driveAutonomousRobot(m_autonomousArea,
                                                     DigitalSideCarInterface.M_ARCADEDRIVE,
                                                     DigitalSideCarInterface.M_DIRECTION_FORWARD,
                                                     DigitalSideCarInterface.M_DIRECTION_REVERSE,
                                                     DigitalSideCarInterface.M_SPEED_LOW,
                                                     m_leftJaguar,
                                                     m_rightJaguar);

                    msg = "TR. D=" + m_lEncoder.getDistance();
                    PrintUtil.printLCD(m_dsLCD, 5, 1, msg);

                    m_distance = m_lEncoder.getDistance();

                    if (m_distance < 0){
                        m_distance = m_distance * -1;
                    }

                    if (m_distance >= DigitalSideCarInterface.M_DISTANCE_TO_90){
                        m_robotDrive.stopDrive(m_leftJaguar, m_rightJaguar);
                        m_step++;
                        m_lEncoder.reset();
                    }

                }
                if (m_step == 4){
                    //Drive Forward to defend the goal...
                    m_robotDrive.driveAutonomousRobot(m_autonomousArea,
                                                        DigitalSideCarInterface.M_TANKDRIVE,
                                                        DigitalSideCarInterface.M_DIRECTION_FORWARD,
                                                        DigitalSideCarInterface.M_DIRECTION_NEUTRAL,
                                                        DigitalSideCarInterface.M_SPEED_MEDIUM,
                                                        m_leftJaguar,
                                                        m_rightJaguar);

                    msg = "2Goal. D=" + m_lEncoder.getDistance();
                    PrintUtil.printLCD(m_dsLCD, 5, 1, msg);

                    m_distance = m_lEncoder.getDistance();

                    if (m_distance < 0){
                        m_distance = m_distance * -1;
                    }

                    if (m_distance >= DigitalSideCarInterface.M_DISTANCE_TO_GOAL){
                        m_robotDrive.stopDrive(m_leftJaguar, m_rightJaguar);
                        m_step++;
                        m_lEncoder.reset();
                    }
                }

            break;
            case DigitalSideCarInterface.M_AUTONOMOUS_OFFENSE:
                if (m_step == 0){
                    //Drive Forward to kick the ball...
                    m_ballMagnet.set(Relay.Value.kOn);
                    m_robotKick.loadHalfPrime(m_autonomousArea, m_primer, m_pot);
                    m_robotDrive.driveAutonomousRobot(m_autonomousArea,
                                                        DigitalSideCarInterface.M_TANKDRIVE,
                                                        DigitalSideCarInterface.M_DIRECTION_FORWARD,
                                                        DigitalSideCarInterface.M_DIRECTION_NEUTRAL,
                                                        DigitalSideCarInterface.M_SPEED_MEDIUM,
                                                        m_leftJaguar,
                                                        m_rightJaguar);

                    msg = "2Ball. D=" + m_lEncoder.getDistance();
                    PrintUtil.printLCD(m_dsLCD, 5, 1, msg);

                    m_distance = m_lEncoder.getDistance();

                    if (m_distance < 0){
                        m_distance = m_distance * -1;
                    }
                    if (m_distance >= DigitalSideCarInterface.M_DISTANCE_TO_BALL){
                        m_robotDrive.stopDrive(m_leftJaguar, m_rightJaguar);
                        m_step++;
                        m_lEncoder.reset();
                    }
                }
                if (m_step == 1){
                    m_myPeriodicLoops++;
                    if (m_myPeriodicLoops <= DigitalSideCarInterface.AUTO_KICK_LIMIT){
                        m_robotKick.autoKick(m_autonomousArea, m_myPeriodicLoops,
                                             m_primeSolenoid, m_returnSolenoid, m_fireSolenoid,
                                             m_primer, m_pot);
                    }else{
                        m_robotKick.unloadPrimer(m_autonomousArea, m_primer, m_pot);
                        m_myPeriodicLoops = 0;
                        m_step++;
                    }
                }
                if (m_step == 2){
                    //Drive Backward to your starting position...
                    m_robotDrive.driveAutonomousRobot(m_autonomousArea,
                                                        DigitalSideCarInterface.M_TANKDRIVE,
                                                        DigitalSideCarInterface.M_DIRECTION_REVERSE,
                                                        DigitalSideCarInterface.M_DIRECTION_NEUTRAL,
                                                        DigitalSideCarInterface.M_SPEED_MEDIUM,
                                                        m_leftJaguar,
                                                        m_rightJaguar);

                    msg = "BackUp. D=" + m_lEncoder.getDistance();
                    PrintUtil.printLCD(m_dsLCD, 5, 1, msg);

                    m_distance = m_lEncoder.getDistance();

                    if (m_distance < 0){
                        m_distance = m_distance * -1;
                    }

                    if (m_distance >= DigitalSideCarInterface.M_DISTANCE_TO_BALL){
                        m_robotDrive.stopDrive(m_leftJaguar, m_rightJaguar);
                        m_step++;
                        m_lEncoder.reset();
                    }
                }

                if (m_step == 3){
                    //Turn Right 90 Degrees...
                    m_robotDrive.driveAutonomousRobot(m_autonomousArea,
                                                     DigitalSideCarInterface.M_ARCADEDRIVE,
                                                     DigitalSideCarInterface.M_DIRECTION_FORWARD,
                                                     DigitalSideCarInterface.M_DIRECTION_REVERSE,
                                                     DigitalSideCarInterface.M_SPEED_LOW,
                                                     m_leftJaguar,
                                                     m_rightJaguar);

                    msg = "TR. D=" + m_lEncoder.getDistance();
                    PrintUtil.printLCD(m_dsLCD, 5, 1, msg);

                    m_distance = m_lEncoder.getDistance();

                    if (m_distance < 0){
                        m_distance = m_distance * -1;
                    }

                    if (m_distance >= DigitalSideCarInterface.M_DISTANCE_TO_90){
                        m_robotDrive.stopDrive(m_leftJaguar, m_rightJaguar);
                        m_step++;
                        m_lEncoder.reset();
                    }

                }
                if (m_step == 4){
                    //Drive Forward to defend the goal...
                    m_robotDrive.driveAutonomousRobot(m_autonomousArea,
                                                        DigitalSideCarInterface.M_TANKDRIVE,
                                                        DigitalSideCarInterface.M_DIRECTION_FORWARD,
                                                        DigitalSideCarInterface.M_DIRECTION_NEUTRAL,
                                                        DigitalSideCarInterface.M_SPEED_MEDIUM,
                                                        m_leftJaguar,
                                                        m_rightJaguar);

                    msg = "2Goal. D=" + m_lEncoder.getDistance();
                    PrintUtil.printLCD(m_dsLCD, 5, 1, msg);

                    m_distance = m_lEncoder.getDistance();

                    if (m_distance < 0){
                        m_distance = m_distance * -1;
                    }

                    if (m_distance >= DigitalSideCarInterface.M_DISTANCE_TO_GOAL){
                        m_robotDrive.stopDrive(m_leftJaguar, m_rightJaguar);
                        m_step++;
                        m_lEncoder.reset();
                    }
                }
            break;

    }
 }
}