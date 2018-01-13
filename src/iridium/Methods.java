package iridium;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.*;

import javax.swing.*;
import java.util.Timer;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.sun.speech.freetts.*;
import com.sun.speech.freetts.Voice;

public class Methods {
	private static Call call = new Call();
	private static int sec = 0;
	private static String[] splitted ;

	public Methods() {
	    call.setCommand("updatehelp", () -> Updater.updateHelp());
		call.setCommand("clear", () -> Presentation.clear());
		call.setCommand("ping", () -> Presentation.update("pong", false));
		call.setCommand("hi",  () -> Presentation.update("Hi", false));
		call.setCommand("timer", () -> {
			int a = Integer.parseInt(splitted[1]);
			timer(a);
		});
		call.setCommand("say",  () -> {
				String a1 = " ";
				for (int i = 1; i < splitted.length; i++)
					a1 += splitted[i];
				sagen(a1);
		});
		call.setCommand("calculator",  () -> {
				double z1, z2 = 0;

				try {
					z1 = Double.parseDouble(splitted[1]);
					z2 = Double.parseDouble(splitted[3]);

					if (z1 * z2 < Integer.MAX_VALUE)
						Presentation.update(String.valueOf(rechner(z1, splitted[2], z2)), false);
					else
						Presentation.update("0,0", false);
				} catch (java.lang.NumberFormatException e) {
					Presentation.update("Falsche Parameter", false);
				}
		});
		call.setCommand("play",  () ->  Player.Spiele(splitted));
		call.setCommand("stop",  () -> Player.Stop());
		call.setCommand("next",  () -> Player.stop = false);
		call.setCommand("create",  () -> Filehandeling.Datei(splitted[1]));
		call.setCommand("delete",  () -> Filehandeling.Löschen(splitted[1]));
		call.setCommand("list",  () -> Filehandeling.getFiles("iridium/Docs", true));
		call.setCommand("open",  () -> {if(splitted.length > 1) new OutputField(Filehandeling.Lesen(splitted[1], true), splitted[1], true, true,""); else Presentation.update("Argument requiert", false);});
		call.setCommand("setshort",  () -> SetShort(splitted));
		call.setCommand("short", () -> {
				if (splitted.length > 1) {
					if (splitted[1].equals("-o"))
						getShortcut();
				}
				else
					Methods.Short();
		});
		call.setCommand("background", () -> {
		if (splitted[1].equals("rot") || splitted[1].equals("blau") || splitted[1].equals("grün") || splitted[1].equals("weiß") || splitted[1].equals("schwarz") || splitted[1].equals("gelb"))
			Presentation.FarbeHintergrund(splitted[1]);
				else
					Presentation.update("Farbe nicht Verfügbar", false);
		});
		call.setCommand("font", () -> {
		if (splitted[1].equals("rot") || splitted[1].equals("blau") || splitted[1].equals("grün") || splitted[1].equals("weiß") || splitted[1].equals("schwarz") || splitted[1].equals("gelb"))
			Presentation.Farbeschrift(splitted[1]);
				else
					Presentation.update("Farbe nicht Verfügbar", false);
		});
		call.setCommand("spotify", () -> open("https://open.spotify.com"));
		call.setCommand("link", () -> {
			if (!splitted[1].contains("https://"))
					splitted[1] = "https://" + splitted[1];
				open(splitted[1]);
		});
		call.setCommand("start", () -> Programmstart.ProgrammStart(splitted[1]));
		call.setCommand("addprogramm", () -> Programmstart.addProgramm(splitted));
		call.setCommand("help",  () -> {
		    if (splitted.length > 1)
			    if (call.checkCommands(splitted[1]))
				    Presentation.update(Filehandeling.Lesen("iridium/help/"+splitted[1].trim()+".txt",false),false);
		        else
		        Presentation.update("command not found",false);
            else
                Presentation.update("enter help and the command you want help for",false);
		});
		call.setCommand("commands", () -> Presentation.update(call.getCommands(),false));
		call.setCommand("quit", () -> System.exit(0));
		call.setCommand("QR", () -> {
			String text = "";
			int size = 250;
			String name = "";
			Color col = Color.BLACK;
			ErrorCorrectionLevel level = ErrorCorrectionLevel.L;

			ArrayList<String> a = new ArrayList<>();
			HashMap<String, Color> b = new HashMap<>();
			b.put("red", Color.RED);
			b.put("blue", Color.BLUE);
			b.put("black", Color.BLACK);
			b.put("yellow", Color.YELLOW);
			b.put("green", Color.GREEN);
			for (int i = 0;i < splitted.length;i++)
				a.add(splitted[i]);

			if(a.contains("-size") && !a.contains("\\-size"))
				size = Integer.parseInt(a.get(a.indexOf("-size") + 1));

			if(a.contains("-color") && !a.contains("\\-color"))
				col = b.get(a.get(a.indexOf("-color")+1));

			if(a.contains("-level") && !a.contains("\\-level"))
				level = ErrorCorrectionLevel.valueOf(a.get(a.indexOf("-level") + 1));

			name = splitted[splitted.length-2];
			text = splitted[splitted.length-1];

			System.out.println(text +" "+ size + " " + name +" "+ col);
			try {
				MyQRCode.createQRImage(text, size, name, col, level, "png");
			} catch (Exception ignore){}
		});
		call.setCommand("update",  () ->  {if(splitted.length>1&&splitted[1].equals("-keep"))Updater.update(true);
		else Updater.update(false);});

	}

	private static void open(String urlString) {
		try {
			Desktop.getDesktop().browse(new URL(urlString).toURI());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void timer(int time) {
		Presentation.update("Started", true);
		Timer t = new Timer();
		TimerTask tt = new TimerTask() {
			public void run() {
				sec++;
				if (sec == time) {
					t.cancel();
					sec = 0;
					Presentation.update("Finished", true);
				}
			}
		};
		t.scheduleAtFixedRate(tt, 1000, 1000);
	}

	private static void sagen(String a1) {
		Voice voice;
		VoiceManager vm = VoiceManager.getInstance();
		voice = vm.getVoice("kevin16");
		voice.allocate();

		voice.speak(a1);
	}

	private static double rechner(double a, String b, double c) {
		if (b.equals("+")) {
			return a + c;
		} else if (b.endsWith("-")) {
			return a - c;
		}
		if (b.equals("*")) {
			return a * c;
		} else if (b.endsWith("/")) {
			return a / c;
		} else {
			return 000;
		}
	}

	public static void Short() {
		Methods.runCommand(ConfHandler.getConf("shortcut:"));
	}

	private static void SetShort(String[] a) {
		if (call.checkCommands(a[1]))
		{
			String ab = "";
			for (int i = 1;i < a.length;i++)
				ab += a[i] + " ";
			System.out.println(ab);
			ConfHandler.setConf("shortcut:", ab);
		}
		else
			Presentation.update("Command not found", false);
	}

	public static void getShortcut() {
		Presentation.update(ConfHandler.getConf("shortcut:"), false);
	}

	public static void runCommand(String f) {
		splitted = f.split("\\s+");
        call.runCommand(splitted);
	}
}