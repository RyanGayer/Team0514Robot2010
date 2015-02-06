/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Dashboard;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationEnhancedIO;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Compressor;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Team0514Robot extends IterativeRobot {

    Timer t = new Timer();
    int i = 0;
    int o = 0;
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        t.start();
        i = i + 1;
        o = o + 1;
        System.out.println("Auto Counter = " + i);
        System.out.println("Op Counter = " + o);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

        if (isAutonomous()){
            i = i + 1;
        System.out.println("Autonomous Mode");
        System.out.println("Counter = " + i);
        System.out.println(t.get());
        }

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
       if (isOperatorControl()){
           o = o + 1;
       System.out.println("Operator Mode");
       System.out.println("Counter = " + o);
       System.out.println(t.get());
       }
    }
    
}
