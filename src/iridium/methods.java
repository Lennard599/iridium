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

public class methods {
	private static Call call = new Call();
	private static int sec = 0;
	private static String[] splited ;

	public methods() {
		call.setCommand("clear", () -> Presentation.clear());
		call.setCommand("ping", () -> Presentation.update("pong", false));
		call.setCommand("hi",  () -> Presentation.update("Hi", false));
		call.setCommand("timer", () -> {
			int a = Integer.parseInt(splited[1]);
			timer(a);
		});
		call.setCommand("say",  () -> {
				String a1 = " ";
				for (int i = 1; i < splited.length; i++)
					a1 += splited[i];
				sagen(a1);
		});
		call.setCommand("calculator",  () -> {
				double z1, z2 = 0;

				try {
					z1 = Double.parseDouble(splited[1]);
					z2 = Double.parseDouble(splited[3]);

					if (z1 * z2 < Integer.MAX_VALUE)
						Presentation.update(String.valueOf(rechner(z1, splited[2], z2)), false);
					else
						Presentation.update("0,0", false);
				} catch (java.lang.NumberFormatException e) {
					Presentation.update("Falsche Parameter", false);
				}
		});
		call.setCommand("play",  () ->  Player.Spiele(splited));
		call.setCommand("stop",  () -> Player.Stop());
		call.setCommand("weiter",  () -> Player.stop = false);
		call.setCommand("create",  () -> Filehandeling.Datei(splited[1]));
		call.setCommand("delete",  () -> Filehandeling.Löschen(splited[1]));
		call.setCommand("list",  () -> Filehandeling.getFiles("iridium/Dateien", true));
		call.setCommand("open",  () -> AusgabeFeld(Filehandeling.Lesen(splited[1], true), splited[1], true, true));
		call.setCommand("setshort",  () -> SetShort(splited));
		call.setCommand("short", () -> {
				if (splited.length > 1) {
					if (splited[1].equals("-o"))
						getShortcut();
				}
				else
					methods.Short();
		});
		call.setCommand("background", () -> {
		if (splited[1].equals("rot") || splited[1].equals("blau") || splited[1].equals("grün") || splited[1].equals("weiß") || splited[1].equals("schwarz") || splited[1].equals("gelb"))
			Presentation.HintergrundF(splited[1], Iridium.FarbeS2);
				else
					Presentation.update("Farbe nicht Verfügbar", false);
		});
		call.setCommand("font", () -> {
		if (splited[1].equals("rot") || splited[1].equals("blau") || splited[1].equals("grün") || splited[1].equals("weiß") || splited[1].equals("schwarz") || splited[1].equals("gelb"))
			Presentation.SchriftF(Iridium.FarbeH2, splited[1]);
				else
					Presentation.update("Farbe nicht Verfügbar", false);
		});
		call.setCommand("spotify", () -> open("https://open.spotify.com"));
		call.setCommand("link", () -> {
		if (!splited[1].contains("https://"))
					splited[1] = "https://" + splited[1];
				open(splited[1]);
		});
		call.setCommand("start", () -> Programmstart.ProgrammStart(splited[1]));
		call.setCommand("addprogramm", () -> Programmstart.addProgramm(splited));
		call.setCommand("help",  () -> Presentation.update(call.getCommands(),false));
				//methods.Hilfe();
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
			for (int i = 0;i < splited.length;i++)
				a.add(splited[i]);

			if(a.contains("-size") && !a.contains("\\-size"))
				size = Integer.parseInt(a.get(a.indexOf("-size") + 1));

			if(a.contains("-color") && !a.contains("\\-color"))
				col = b.get(a.get(a.indexOf("-color")+1));

				level = ErrorCorrectionLevel.valueOf(a.get(a.indexOf("-level") + 1));

			name = splited[splited.length-2];
			text = splited[splited.length-1];

			System.out.println(text +" "+ size + " " + name +" "+ col);
			try {
				MyQRCode.createQRImage(text, size, name, col, level, "png");
			} catch (Exception ignore){}
		});
		call.setCommand("update",  () ->  {if(splited.length>1&&splited[1].equals("-keep"))Updater.update(true);
		else Updater.update(false);});

	}

	private static void open(String urlString) {
		try {
			Desktop.getDesktop().browse(new URL(urlString).toURI());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String ALzuSt(ArrayList<String> a, int stelle, String wert) {
		while (a.size() <= stelle)
			a.add("");

		String b = "";
		a.set(stelle, wert);
		for (String c : a)
			b += c + " ";
		return b;
	}

	private static void timer(int time) {
		Presentation.update("Start <br>", true);
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

	public static void AusgabeFeld(ArrayList<String> a, String title, boolean be, boolean html) {
		AusgabeFeld(a, title, be, true, "", html);
	}

	public static void AusgabeFeld(ArrayList<String> a, String title, boolean be, boolean rela, String Path, boolean html) {
		String in = "";
		JFrame Ausgabe = new JFrame(title);
		Ausgabe.setVisible(true);
		Ausgabe.setSize(450, 600);
		Ausgabe.setResizable(false);
		Ausgabe.setLocationRelativeTo(null);
		Ausgabe.setLayout(new BorderLayout());
		Ausgabe.setIconImage(Iridium.icon.getImage());

		if (be) {
			Ausgabe.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			WindowListener close = new WindowAdapter() {

				public void windowClosing(WindowEvent e) {
					int confirm = JOptionPane.showConfirmDialog(Ausgabe,
							"Ohne Speichern Beenden?",
							"Beenden?", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);

					if (confirm == 0)
						Ausgabe.dispose();

				}
			};
			Ausgabe.addWindowListener(close);

		} else
			Ausgabe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		JButton Speichern = new JButton("Speichern und Beenden");

		if (be)
			Ausgabe.add(Speichern, BorderLayout.SOUTH);

		JTextPane text = new JTextPane();
		JScrollPane scroll = new JScrollPane(text);


		Ausgabe.add(scroll, BorderLayout.CENTER);

		if (!be)
			text.setEditable(false);

		for (String d : a) {
			in += " " + d;
		}
		if (html)
			text.setContentType("text/html");
		text.setText(in);

		Speichern.addActionListener(e -> {
				if (rela)
					Filehandeling.Schreiben(text.getText(), title, true, false);
				else
					Filehandeling.Schreiben(text.getText(), Path, false, false);
				Ausgabe.dispose();
				Presentation.update("Saved", true);
		});

	}

	public static void AusgabeFeld(String a, String title, boolean be, boolean rela, String Path, boolean html) {
		JFrame Ausgabe = new JFrame(title);
		Ausgabe.setVisible(true);
		Ausgabe.setSize(450, 600);
		Ausgabe.setResizable(false);
		Ausgabe.setLocationRelativeTo(null);
		Ausgabe.setLayout(new BorderLayout());
		Ausgabe.setIconImage(Iridium.icon.getImage());

		if (be) {
			Ausgabe.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			WindowListener close = new WindowAdapter() {

				public void windowClosing(WindowEvent e) {
					int confirm = JOptionPane.showConfirmDialog(Ausgabe,
							"Ohne Speichern Beenden?",
							"Beenden?", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);

					if (confirm == 0)
						Ausgabe.dispose();

				}
			};
			Ausgabe.addWindowListener(close);

		} else
			Ausgabe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		JButton Speichern = new JButton("Speichern und Beenden");

		if (be)
			Ausgabe.add(Speichern, BorderLayout.SOUTH);

		JTextPane text = new JTextPane();
		JScrollPane scroll = new JScrollPane(text);


		Ausgabe.add(scroll, BorderLayout.CENTER);

		if (!be)
			text.setEditable(false);

		if (html)
			text.setContentType("text/html");
		text.setText(a);

		Speichern.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (rela)
					Filehandeling.Schreiben(text.getText(), title, true, false);
				else
					Filehandeling.Schreiben(text.getText(), Path, false, false);
				Ausgabe.dispose();
				Presentation.update("saved", true);
			}
		});

	}

	public static void Hilfe() {
		ArrayList<String> Commands = new ArrayList<>();
		Commands.add("Ping..................ausgabe:pong <br>");
		Commands.add("Hi....................ausgabe:Hi <br>");
		Commands.add("Timer -Zeit in s-    <br>");
		Commands.add("Sagen -Text-...............TTS <br>");
		Commands.add("stelle dich vor	");
		Commands.add("Rechner -Z1 Operator Z2- <br>");
		Commands.add("Erstellen -Dateiname-..... Ort:Iridiumverzeichniss <br>");
		Commands.add("Löschen -Dateiname-<br>");
		Commands.add("Liste //ls.................liest files aus Ort:Iridiumverzeichniss <br>");
		Commands.add("Öffnen -Dateiname- <br>");
		Commands.add("SetShortcut -Befehl-...... Shortcut anlegen <br>");
		Commands.add("Shortcut // sc.............ausführen Shortcut <br>");
		Commands.add("Shortcut -a................Momentaner Shortcut <br>");
		Commands.add("Hintergrund -farbe- <br>");
		Commands.add("Schrift -farbe- <br>");
		Commands.add("Play -Liedtitel-..........ruft mp3 aus musik auf <br>");
		Commands.add("Stop....................Stopt momentanes Lied <br>");
		Commands.add("Weiter..................Startet gestopptes Lied wieder <br>");
		Commands.add("Iridium Unterstützt Drag and Drop von .txt und .mp3 <br>");
		Commands.add("Beenden.................Iridium wird Beendet <br>");

		AusgabeFeld(Commands, "Hilfe", false, true);
	}

	public static void Short() {
		String cut = "";
		int index;
		ArrayList<String> a = Filehandeling.Lesen("iridium/Iridiumconfig.txt", false);
		index = a.indexOf("Shortcut");
		for (int i = index + 1; i < a.size(); i++)
			cut += a.get(i) + " ";

		methods.runCommand(cut);

	}

	private static void SetShort(String[] a) {
		if (call.checkCommands(a[1]))
		{
			String ab = "";
			ArrayList<String> aa = Filehandeling.Lesen("iridium/Iridiumconfig.txt", false);

			int index = aa.indexOf("Shortcut");

			for (int i = 0;i < aa.size();i++) {
				if (index+1 > i)
					ab += aa.get(i) + " ";
			}

			for (int i = 1;i < a.length;i++)
				ab += a[i] + " ";
			System.out.println(ab);
			Filehandeling.Schreiben(ab, "iridium/Iridiumconfig.txt", false, true);
		}
		else
			Presentation.update("Fehler!!", false);

	}

	public static void getShortcut() {
		ArrayList<String> a = Filehandeling.Lesen("iridium/Iridiumconfig.txt", false);
		int index;
		String aa = "Momentaner Shortcut : ";
		index = a.indexOf("Shortcut");
		for (int i = index + 1; i < a.size(); i++)
			aa += a.get(i) + " ";
		Presentation.update(aa, false);

	}

	public static void runCommand(String f) {
		splited = f.split("\\s+");
		String input = Iridium.Feld.getText();

		Presentation.update(input, true);
		Iridium.Feld.setText("");
		call.runCommand(splited);
	}
}