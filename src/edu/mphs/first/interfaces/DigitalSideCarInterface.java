/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mphs.first.interfaces;

/**
 * You are to use this interface to define all public static final variables
 * that the Robot will use.  The primary use of this interface is the define
 * all variables that represent device wiring configuration back to the cRIO
 * and Digital Side Car.  These are perfect candidates to take advantage of
 * the Interface pattern.  Use this for other variable declarations as well but
 * if you are unsure, define it to your local class.method() and we can discuss
 * it later.
 *
 * @author marnold
 */
public interface DigitalSideCarInterface {
    
    // Misc Runtime Config Parameters
        //0 = Off, 1 = On.  In the future there may be other levels...
        public static final int M_DEBUG_LEVEL = 0;
        public static final int M_LCD_LEVEL = 1;

    // Declare variables for Potentiometer
        public static final int POT1_CHANNEL = 1;
        public static final int POT1_SLOT = 1;

    // Declare a variable to use to access the Encoder object
        public static final int ENCODER_L_SLOT_A = 4;
        public static final int ENCODER_L_CHANNEL_A = 13;
        public static final int ENCODER_L_SLOT_B = 4;
        public static final int ENCODER_L_CHANNEL_B = 14;
        public static final int ENCODER_R_SLOT_A = 4;
        public static final int ENCODER_R_CHANNEL_A = 9;
        public static final int ENCODER_R_SLOT_B = 4;
        public static final int ENCODER_R_CHANNEL_B = 10;

    // Declare Drive Mode selection
	public static final int UNINITIALIZED_DRIVE = 0;
	public static final int ARCADE_DRIVE = 1;
	public static final int TANK_DRIVE = 2;

    // Declare Joystick variables
        public static final int NUM_JOYSTICK_BUTTONS = 16;
        public static final int LEFT_JOYSTICK = 3;
        public static final int RIGHT_JOYSTICK = 1;
        public static final int CONTROLLER = 2;

    // Joystick Button Mappings:
        public static final int DRIVE_TOGGLE = 1;
        public static final int DRUM_DRIVE = 1;
        public static final int PRIME_1 = 5;
        public static final int PRIME_2 = 7;
        public static final int WINCH_UP = 4;
        public static final int WINCH_DOWN = 2;
        public static final int WINCH_ARM = 3;
        public static final int KICK_LATCH_RELEASE = 8;


    // Declare variables for each of the eight solenoid outputs
	public static final int NUM_SOLENOIDS = 8;

    // Declare a variable to use to access the Compressor object
        public static final int COMP_P_SWITCH_SLOT = 4;
        public static final int COMP_P_SWITCH_CHANNEL = 8;
        public static final int COMP_C_RELAY_SLOT = 4;
        public static final int COMP_C_RELAY_CHANNEL = 7;

    // Declare variables for the Jaguar Drives
        public static final int LEFT_JAGUAR = 1;
        public static final int RIGHT_JAGUAR = 2;
        public static final int WINCH_VICTOR = 3;
        
    // Decalre variables for Solenoids (Pneumatic Bumpers).
    // Currently using Default Slot 8
        public static final int PNEU_SLOT = 8;
        public static final int PRIME_KICK_PNEU = 1;
        public static final int RETURN_KICK_PNEU = 2;
        public static final int FIRE_KICK_PNEU = 3;
        public static final int FIRE_BOOM_PNEU = 4;
        public static final int RETURN_BOOM_PNEU = 5;

    // Declare variables for Relays.
        public static final int LEFT_KICK_RELAY = 1;
        public static final int RIGHT_KICK_RELAY = 2;
        public static final int LATCH_RELAY = 3;
        public static final int DRUM_DRIVE_RELAY = 1;
        public static final int PRIME_BAR_RELAY = 2;


    // Declare variables for DIO.
        public static final int KICK_RETRACTED_SW = 1;
        public static final int KICK_KICKED_SW = 2;
        public static final int AUTONOMOUS_MIDFIELD_SW = 3;
        public static final int AUTONOMOUS_OFFENSE_SW = 4;

    // Declare Potentiometer Range Values
        public static final int PRIME_REST_UPPER_LIMIT = 516;
        public static final int PRIME_REST_LOWER_LIMIT = 512;
        public static final int PRIME_HALF_UPPER_LIMIT = 578;
        public static final int PRIME_HALF_LOWER_LIMIT = 574;
        public static final int PRIME_FULL_UPPER_LIMIT = 633;
        public static final int PRIME_FULL_LOWER_LIMIT = 599;

    // Kick Timer Constants Units Seconds
        public static final int RETRACT_WAIT = 1;        // 1 second
        public static final int KICK_WAIT = 3;           // 3 seconds (1 Second Out + 2 Seconds Back)

    // Scissor Arm Timer Constants Units Seconds
        public static final double M_ACTIVATE_SCISSOR = 100;   // Final 20 Seconds - Activate Scissor Arm

    // Autonomour Timer Constant Units Seconds
        public static final double M_AUTO_TIMELIMIT = 15;

    // Autonomous Mode Constants - 1 = Defense/Default, 2 = Midfield, 3 = Offense
        public static final int M_AUTONOMOUS_DEFENSE = 1;
        public static final int M_AUTONOMOUS_MIDFIELD = 2;
        public static final int M_AUTONOMOUS_OFFENSE = 3;

    // Autonomous Controls...
        public static final int M_DIRECTION_FORWARD = -1;
        public static final int M_DIRECTION_NEUTRAL = 0;
        public static final int M_DIRECTION_REVERSE = 1;
        public static final boolean M_TANKDRIVE = true;
        public static final boolean M_ARCADEDRIVE = false;
        public static final double M_DISTANCE_TO_BALL = 1000;
        public static final double M_DISTANCE_TO_90 = 500;
        public static final double M_DISTANCE_TO_GOAL = 1500;
        public static final double M_SPEED_STOP = 0;
        public static final double M_SPEED_LOW = .15;
        public static final double M_SPEED_MEDIUM = .30;
        public static final double M_SPEED_HIGH = .45;
        public static final int AUTO_KICK_LOOP_PRIME = 25;
        public static final int AUTO_KICK_LOOP_FIRE = 75;
        public static final int AUTO_KICK_LOOP_UNLOAD = 80;
        public static final int AUTO_KICK_LOOP_RETURN = 150;

        public static final int AUTO_KICK_LIMIT = 200;

        


}
