package iridium;

import java.awt.*;
import java.io.File;
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
		},1, 1,""));
		call.setCommand("say", new Command(() -> {
				String a1 = " ";
				for (int i = 1; i < splitted.length; i++)
					a1 += splitted[i];
				sagen(a1);
		},Integer.MAX_VALUE,":"));
		call.setCommand("calculator",new Command(() -> {
				double z1, z2;
				try {
					String input = "";
					for (int i = 1;i < splitted.length;i++)
						input += splitted[i];
					input.replaceAll("-", "|-").replaceAll("/+", "|+").replaceAll("//", "|//").replaceAll("/*", "|/*");
					String[] values = splitted[1].split("|");
					if (values.length >= 3) {
                        z1 = Double.parseDouble(values[0]);
                        z2 = Double.parseDouble(values[2]);

                        if (z1 + z2 < Integer.MAX_VALUE | z1 * z2 < Integer.MAX_VALUE)
                            Presentation.update(String.valueOf(rechner(z1, values[1], z2)), false);
                        else
                            Presentation.update("overflow", false);
                    }
                    else
                        Presentation.update("wrong parameter", false);
                } catch (java.lang.NumberFormatException e) {
					Presentation.update("wrong parameter", false);
				}
		},Integer.MAX_VALUE,"calc"));
		call.setCommand("play",new Command(() ->  Player.Spiele(splitted),1, Integer.MAX_VALUE,""));
		call.setCommand("stop",new Command(() -> Player.Stop(),0,""));
		call.setCommand("next",new Command(() -> Player.stop = false,0,""));
		call.setCommand("create",new Command(() -> Filehandeling.create(splitted[1]),1,1,"touch"));
		call.setCommand("move", new Command(() -> Filehandeling.move(splitted[1], splitted[2]),2,2, "mv"));
		call.setCommand("delete",new Command(() -> Filehandeling.Löschen(splitted[1]),1,1,"rm"));
		call.setCommand("list",new Command(() -> Filehandeling.getFiles(true),0,"ls"));
		call.setCommand("changeDiretory",new Command(() -> Filehandeling.changeDiretory(splitted[1]),1,1,"cd"));
		call.setCommand("pwd", new Command(() -> Filehandeling.pwd(),0,""));
		call.setCommand("open",new Command(() -> Filehandeling.open(splitted[1]),1,1,"edit"));
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
			if (splitted.length == 4) {
				if (splitted[1].matches("\\b(1?[0-9]{1,2}|2[0-4][0-9]|25[0-5])\\b") && splitted[2].matches("\\b(1?[0-9]{1,2}|2[0-4][0-9]|25[0-5])\\b") && splitted[3].matches("\\b(1?[0-9]{1,2}|2[0-4][0-9]|25[0-5])\\b")) {
					ConfHandler.setConf("background:", splitted[1] + " " + splitted[2] + " " + splitted[3]);
					ConfHandler.writeConf();
					Presentation.setColor(Iridium.Aus, new Color(Integer.valueOf(splitted[1]), Integer.valueOf(splitted[2]), Integer.valueOf(splitted[3])));
					Presentation.setColor(Iridium.status, new Color(Integer.valueOf(splitted[1]), Integer.valueOf(splitted[2]), Integer.valueOf(splitted[3])));
				}
				else
					Presentation.update("the value has to be between 0 and 255", false);
			}
			else
				Presentation.update("Please use space not , between values", false);
		},3,3,"bg"));
		call.setCommand("font",new Command(() -> {
			if (splitted.length == 4) {
				if (splitted[1].matches("\\b(1?[0-9]{1,2}|2[0-4][0-9]|25[0-5])\\b") && splitted[2].matches("\\b(1?[0-9]{1,2}|2[0-4][0-9]|25[0-5])\\b") && splitted[3].matches("\\b(1?[0-9]{1,2}|2[0-4][0-9]|25[0-5])\\b")) {
					ConfHandler.setConf("fontcolor:", splitted[1] + " " + splitted[2] + " " + splitted[3]);
					ConfHandler.writeConf();
					Presentation.Farbeschrift(splitted[1]+" "+splitted[2]+" "+splitted[3]);
				}
				else
					Presentation.update("the value has to be between 0 and 255", false);
			}
			else
				Presentation.update("Please use space not , between values", false);
		},1,3,""));
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
				    Presentation.update(Filehandeling.Lesen(new File(Iridium.class.getClassLoader().getResource("help/"+splitted[1].trim()+".txt").getPath())),false);
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
	    ArrayList<String> s = new ArrayList<>();
		splitted = f.split("\\s+");
		String a = "";
		for (int i = 0;i < splitted.length;i++) {
            if (splitted[i].contains("\"")) {
                a += splitted[i].replace("\"","");
                i++;
                while (!splitted[i].contains("\"")) {
                    a += " " + splitted[i];
                    i++;
                }
                a += " " + splitted[i].replace("\"","");
                s.add(a);
            }
            else
                s.add(splitted[i]);
        }
        splitted = new String[s.size()];
        splitted = s.toArray(splitted);
        call.runCommand(splitted);
	}
}