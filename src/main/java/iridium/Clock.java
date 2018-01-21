package iridium;

import java.util.Calendar;
import javax.swing.JLabel;

public class Clock implements Runnable {

    JLabel jb;

    //Constructor takes the clock JLabel
    public Clock(JLabel jb) {
        this.jb = jb;
    }

    public void run() {
        while (true) {
            try {
                //Thread sleeps & updates ever 1 second, so the clock changes every 1 second.
                jb.setText(currentDate());
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }
        }
    }

    //Gets the current Date and Time.
    public String currentDate() {
        Calendar now = Calendar.getInstance();
        int mth = now.get(Calendar.MONTH)+1;
        int day = now.get(Calendar.DAY_OF_MONTH);
        int hrs = now.get(Calendar.HOUR_OF_DAY);
        int min = now.get(Calendar.MINUTE);
        int sec = now.get(Calendar.SECOND);
        String time = zero(day) + "/" + zero(mth) + "   " + zero(hrs) + ":" + zero(min) + ":" + zero(sec);
        return time;
    }

    //Sets the zeroes needed within our hh/mm/ss clock.
    public String zero(int num) {
        String number = (num < 10) ? ("0" + num) : ("" + num);
        return number;
    }
}
