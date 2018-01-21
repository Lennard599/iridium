package iridium;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.util.Util;

import javax.swing.*;
import java.text.DecimalFormat;

public class Sensor implements Runnable{

    JLabel jlb;
    SystemInfo si = new SystemInfo();
    HardwareAbstractionLayer hardware = si.getHardware();

    public void run() {
            while (true){
                    jlb.setText(getsensor());
            }
        }

        public Sensor(JLabel jlb){
            this.jlb = jlb;
        }

        public String getsensor(){
            CentralProcessor aa = hardware.getProcessor();
            Util.sleep(1000);
            String a = "CPU:" + Double.parseDouble(new DecimalFormat("##.#").format(100*aa.getSystemCpuLoad())) + "% / RAM:" + Double.parseDouble(new DecimalFormat("##.#").format((double)hardware.getMemory().getAvailable()/1000000000)) +"GB";
            return a;
        }
}
