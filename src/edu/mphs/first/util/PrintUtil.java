/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mphs.first.util;

import edu.mphs.first.interfaces.DigitalSideCarInterface;
import edu.wpi.first.wpilibj.DriverStationLCD;

/**
 *
 * @author marnold
 */
public class PrintUtil {

    public static void printMSG(int m_debugLevel, String msg){

            switch (m_debugLevel) {
                case 0:
                    //Debug Off - no action
                    break;
                case 1:
                    //Debug On - print action
                    System.out.print("Team0514 Robot :: " + msg + "\n");
                    break;
            }
        }

       public static void printMSG(int m_debugLevel, String msg, double m_startTime){

            switch (m_debugLevel) {
                case 0:
                    //Debug Off - no action
                    break;
                case 1:
                    //Debug On - print action
                    System.out.print("Team0514 Robot: " + m_startTime + " : " + msg + "\n");
                    break;
            }
        }

       public static void printMSG(int m_debugLevel, String msg, double m_startTime, double m_accumulated){

            switch (m_debugLevel) {
                case 0:
                    //Debug Off - no action
                    break;
                case 1:
                    //Debug On - print action
                    System.out.print("Team0514 Robot: " + m_startTime + " : " + m_accumulated + " : " + msg + "\n");
                    break;
            }
        }

       public static void printLCD(DriverStationLCD m_dsLCD, int line, int column, String msg){
           if (DigitalSideCarInterface.M_LCD_LEVEL == 1){
               if (line > 6){
                   line = 6;
                }
               if (line < 2){
                   line = 2;
               }
               switch (line){
                   case 2:
                       m_dsLCD.println(DriverStationLCD.Line.kUser2,1,msg);
                       m_dsLCD.updateLCD();
                       break;
                   case 3:
                       m_dsLCD.println(DriverStationLCD.Line.kUser3,1,msg);
                       m_dsLCD.updateLCD();
                       break;
                   case 4:
                       m_dsLCD.println(DriverStationLCD.Line.kUser4,1,msg);
                       m_dsLCD.updateLCD();
                       break;
                   case 5:
                       m_dsLCD.println(DriverStationLCD.Line.kUser5,1,msg);
                       m_dsLCD.updateLCD();
                       break;
                   case 6:
                       m_dsLCD.println(DriverStationLCD.Line.kUser6,1,msg);
                       m_dsLCD.updateLCD();
                       break;
               }
           }
       }
}
