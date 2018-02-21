package iridium;

import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Player {
    public static boolean stop = true;
    private static AdvancedPlayer play;
    private static String aa = "";
    private static int frame = 0;
    private static FileInputStream f;
    private static boolean erste = false;

    //extra thread für player
    private static Runnable Ru = new Runnable() {
        public void run() {
            while (t1.isAlive()) {
                try {
                    if (!stop) {
                        f = new FileInputStream(aa);
                        play = new AdvancedPlayer(f);
                        play.setPlayBackListener(new PlaybackListener() {
                            public void playbackFinished(PlaybackEvent playbackEvent) {
                                super.playbackFinished(playbackEvent);
                                frame += playbackEvent.getFrame() / (1000 / 44.1);
                                stop = true;
                            }
                        });

                        play.play(frame, Integer.MAX_VALUE);
                        play.stop();
                    }
                } catch (Exception ignore) {

                }
            }
        }
    };

    private static Thread t1 = new Thread(Ru);

    //abspielen von mp3 als array angegeben
    public static void Spiele(String[] name) {
        if (stop) {
            aa = "";
            for (int i = 1; i < name.length; i++)
                aa += name[i] + " ";
            aa = aa.substring(0, aa.length() - 1);
            aa = "iridium/Music/" + aa + ".mp3";
            Spiele(aa);
        }
    }

    //abspielen von mp3 mit gegebenem path
    public static void Spiele(String path) {
        if (stop) {
            java.nio.file.Path p = Filehandeling.toPath(path);
            try {
                p = p.toRealPath(LinkOption.NOFOLLOW_LINKS);
                aa = p.toAbsolutePath().toString();
            } catch (IOException e){
                Presentation.update("File not Found", false);
            }
            try {
                f = new FileInputStream(aa);
                play = new AdvancedPlayer(f);
                frame = 0;
            } catch (Exception e) {
                Presentation.update("Mp3 not found", false);
            }
            stop = false;
        }

        if (!erste) {
            t1.start();
            erste = true;
        }
    }

    //pausiert wenn läuft
    public static void Stop() {
        stop = true;
        try {
            play.stop();
            play.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
