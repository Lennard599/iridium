package iridium;

import javax.swing.text.JTextComponent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TextareaEngine extends KeyAdapter {
    int posi = 0;
    String out_l, out_r = "";
    JTextComponent out;
    boolean newl;
    String prefix;
    String cursor;
    static String output;
    static Runnable event;

    public TextareaEngine(JTextComponent out, boolean newl, String prefix, String out_l, String cursor, Runnable event){
        this.out = out;
        this.newl = newl;
        this.prefix = prefix;
        this.cursor = cursor;
        this.out_l = out_l;
        out.setEditable(false);
        this.event = event;
        posi = out_l.length();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            e.consume();
        }
        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            e.consume();
        }
        if (e.getKeyCode() == KeyEvent.VK_DELETE) {
            e.consume();
        }
        if (e.getKeyCode() == 37){
            if(posi > 0)
                posi--;
            String local_out = out_l + out_r;
            out_l = local_out.substring(0, posi);
            out_r = local_out.substring(posi);
            out.setText(prefix + out_l + cursor + out_r);
        }

        if (e.getKeyCode() == 39) {
            if(out_r.length() != 0)
                posi++;
            String local_out = out_l + out_r;
            out_l = local_out.substring(0, posi);
            out_r = local_out.substring(posi);
            out.setText(prefix + out_l + cursor + out_r);
        }

        if ( e.getKeyCode() != 10 & e.getKeyCode() != 38 & e.getKeyCode() != 40 & e.getKeyCode() != 20 & e.getKeyCode() != 16 & e.getKeyCode() != 0 & e.getKeyCode() != 18 & e.getKeyCode() != 17 & e.getKeyCode() != 37 & e.getKeyCode() != 39 & e.getKeyCode() != 157 & e.getKeyCode() != 8 & e.getKeyCode() != 38 & e.getKeyCode() != 40) {
            out_l += e.getKeyChar();
            posi++;

            out.setText(prefix + out_l + cursor + out_r);
        }
        if (e.getKeyCode() == 8) {
            if (out_l.length() > 0){
                posi--;

                out_l = out_l.substring(0, out_l.length() - 1);

                out.setText(prefix + out_l + cursor + out_r);
            }
        }
        if ( e.getKeyCode() == 10){
            output = out_l + out_r;
            if (newl) {
                prefix += out_l + out_r;
                prefix += "\n";
            }
            out.setText("");
            if (newl)
                out.setText(prefix);
            posi = 0;
            event.run();
        }
    }

    public static String getOutput(){
        return output;
    }
}
