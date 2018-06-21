package com.example.jonathan.arbaeen.classes;

/**
 * Created by Jonathan on 9/4/2017.
 */

public class QuiblaCalculator
{

    public static  double  MECCA_LATITUDE = 21.41666;
    public static double  MECCA_LONGITUDE = 39.81666;


    public static double doCalculate(double cityLat,double cityLong)
    {
        double a = cityLat ;
        double b = MECCA_LATITUDE;
        double c = (cityLong - MECCA_LONGITUDE);

        double cotan = (MyMath.dSin(a)*MyMath.dCos(c) - MyMath.dCos(a) * MyMath.dTan(b)) / MyMath.dSin(c);
        double newAngle = MyMath.dACot(cotan);

        if(cityLong > MECCA_LONGITUDE)
            newAngle += 180;

        return newAngle;
    }

}
