package iridium;

import java.awt.*;
import java.net.URL;
import java.util.*;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.sun.speech.freetts.*;
import com.sun.speech.freetts.Voice;

public class Methods {
	private static Call call = new Call();
	private static String[] splitted ;

	public Methods() {
		call.setCommand("clear", new Command(() -> Presentation.clear(),0,""));
		call.setCommand("ping", new Command(() -> Presentation.update("pong", false),0,""));
		call.setCommand("hi",  new Command(() -> Presentation.update("Hi", false),0,""));
		call.setCommand("timer", new Command(() -> {
			int a = Integer.parseInt(splitted[1].trim());
			timer(a);
		},1,""));
		call.setCommand("say", new Command(() -> {
				String a1 = " ";
				for (int i = 1; i < splitted.length; i++)
					a1 += splitted[i];
				sagen(a1);
		},Integer.MAX_VALUE,":"));
		call.setCommand("calculator",new Command(() -> {
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
		},3,"cal"));
		call.setCommand("play",new Command(() ->  Player.Spiele(splitted), Integer.MAX_VALUE,""));
		call.setCommand("stop",new Command(() -> Player.Stop(),0,""));
		call.setCommand("next",new Command(() -> Player.stop = false,0,""));
		call.setCommand("create",new Command(() -> Filehandeling.Datei(splitted[1]),1,"touch"));
		call.setCommand("delete",new Command(() -> Filehandeling.Löschen(splitted[1]),1,"rm"));
		call.setCommand("list",new Command(() -> Filehandeling.getFiles("iridium/Docs", true),0,"ls"));
		call.setCommand("open",new Command(() -> {if(splitted.length > 1) Iridium.Aus.add(new Editor(Filehandeling.Lesen(splitted[1], true), splitted[1], true,"",Iridium.Aus)); else Presentation.update("Argument requiert", false);},1,"edit"));
		call.setCommand("short",new Command(() -> {
				if (splitted.length > 1) {
					if (splitted[1].equals("-o")) {
						getShortcut();
						return;
					}
					else if (splitted[1].equals("-s")) {
						SetShort(splitted);
						return;
					}
				}
				Methods.Short(splitted);
		},Integer.MAX_VALUE,"sc"));
		call.setCommand("background",new Command(() -> {
		if (splitted[1].equals("rot") || splitted[1].equals("blau") || splitted[1].equals("grün") || splitted[1].equals("weiß") || splitted[1].equals("schwarz") || splitted[1].equals("gelb"))
			Presentation.FarbeHintergrund(splitted[1]);
				else
					Presentation.update("Farbe nicht Verfügbar", false);
		},1,"bg"));
		call.setCommand("font",new Command(() -> {
		if (splitted[1].equals("rot") || splitted[1].equals("blau") || splitted[1].equals("grün") || splitted[1].equals("weiß") || splitted[1].equals("schwarz") || splitted[1].equals("gelb"))
			Presentation.Farbeschrift(splitted[1]);
				else
					Presentation.update("Farbe nicht Verfügbar", false);
		},1,""));
		call.setCommand("spotify",new Command(() -> open("https://open.spotify.com"),0,""));
		call.setCommand("link",new Command(() -> {
			if (!splitted[1].contains("https://"))
					splitted[1] = "https://" + splitted[1];
				open(splitted[1]);
		},1,"web"));
		call.setCommand("start",new Command(() -> Programmstart.ProgrammStart(splitted[1]),1,"launch"));
		call.setCommand("addprogramm",new Command(() -> Programmstart.addProgramm(splitted),Integer.MAX_VALUE,"add"));
		call.setCommand("help",new Command(() -> {
		    if (splitted.length > 1)
			    if (call.checkCommands(splitted[1]))
				    Presentation.update(Filehandeling.Lesen(Iridium.class.getClassLoader().getResource("help/"+splitted[1].trim()+".txt").getPath(),false),false);
		        else
		        Presentation.update("command not found",false);
            else
                Presentation.update("enter help and the command you want help for",false);
		},1,"man"));
		call.setCommand("commands",new Command(() -> Presentation.update(call.getCommands(),false),0,""));
		call.setCommand("quit",new Command(() -> System.exit(0),0,"exit"));
		call.setCommand("QR",new Command(() -> {
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
		},Integer.MAX_VALUE,""));
		call.setCommand("update",new Command(() ->  {if(splitted.length>1&&splitted[1].equals("-keep"))Updater.update(true);
		else Updater.update(false);},1,""));
		call.setCommand("settings",new Command(() -> Iridium.Aus.add(new Options(1,Iridium.Aus)),0,""));
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
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(time * 1000);
					Presentation.update("Finished", true);
				} catch (Exception e){}
			}
		});
		t.start();
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

	public static void Short(String[] argument) {
		String a = "";
		for (int i = 1;i < argument.length;i++)
			a += argument[i] + " ";
		System.out.println(ConfHandler.getConf("shortcut:")+ a);
		Methods.runCommand(ConfHandler.getConf("shortcut:") + a);
	}

	private static void SetShort(String[] a) {
		if (call.checkCommands(a[2]))
		{
			String ab = "";
			for (int i = 2;i < a.length;i++)
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