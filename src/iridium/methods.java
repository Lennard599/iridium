package iridium;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.net.URL;
import java.util.*;

import javax.swing.*;
import java.awt.Toolkit;
import java.io.*;
import java.util.Timer;

import com.sun.speech.freetts.*;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
import org.omg.PortableInterceptor.INACTIVE;


public class methods extends Iridium {
	private static Call call = new Call();
	private static int sec = 0;
	private static String prefix = "<html><font color=yellow>#-# </font>";
	private static String Inhalt = "";
	private static String aa = "";
	public static String Farbe = "";
	private static boolean erste = false;
	private static boolean stop = true;
	private static FileInputStream f;
	private static AdvancedPlayer play;
	private static int frame = 0;
	private static String[] splited ;
	private static String cursor =  "<font color=yellow>&#9611</font>";

	//private static String cursor = "<span style=\"color: yellow\">_</span>";
	private static String out_l,out_r = "";
	private static ArrayList<String> history = new ArrayList<>();
	public static int posi,p = 0;

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

	public methods() {
		call.setCommand("clear", () -> clear());
		call.setCommand("ping", () -> update("pong", false));
		call.setCommand("hi",  () -> update("Hi", false));
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
						update(String.valueOf(rechner(z1, splited[2], z2)), false);
					else
						update("0,0", false);
				} catch (java.lang.NumberFormatException e) {
					update("Falsche Parameter", false);
				}
		});
		call.setCommand("play",  () ->  Spiele(splited));
		call.setCommand("stop",  () -> Stop());
		call.setCommand("weiter",  () -> stop = false);
		call.setCommand("create",  () -> Datei(splited[1]));
		call.setCommand("delete",  () -> Löschen(splited[1]));
		call.setCommand("list",  () -> getFiles("iridium/Dateien", true));
		call.setCommand("open",  () -> AusgabeFeld(methods.Lesen(splited[1], true), splited[1], true, true));
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
					HintergrundF(splited[1], FarbeS2);
				else
					update("Farbe nicht Verfügbar", false);
		});
		call.setCommand("font", () -> {
		if (splited[1].equals("rot") || splited[1].equals("blau") || splited[1].equals("grün") || splited[1].equals("weiß") || splited[1].equals("schwarz") || splited[1].equals("gelb"))
					SchriftF(FarbeH2, splited[1]);
				else
					update("Farbe nicht Verfügbar", false);
		});
		call.setCommand("spotify", () -> open("https://open.spotify.com"));
		call.setCommand("link", () -> {
		if (!splited[1].contains("https://"))
					splited[1] = "https://" + splited[1];
				open(splited[1]);
		});
		call.setCommand("start", () -> ProgrammStart(splited[1]));
		call.setCommand("addprogramm", () -> addProgramm(splited));
		call.setCommand("help",  () -> update(call.getCommands(),false));
				//methods.Hilfe();
		call.setCommand("quit", () -> System.exit(1));
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

	public static void update(String in, boolean user) {

		/*if (user) {
			Inhalt += Farbe + in + "<br>";
			Aus.setContentType("text/html");
			Aus.setText(Inhalt);
		}*/

		if (!user) {
			Inhalt += Farbe + in + "<br>";
			Aus.setContentType("text/html");
			Aus.setText(Inhalt);
		}
	}

	public static void copyToClipboard(String text) {
		Thread th = new Thread( () -> {
			Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
			StringSelection clip = new StringSelection(text);
			clpbrd.setContents(clip, null);
		});
		th.start();

	}

	public static void Mouse(){
		Aus.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 3){
					Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
					Transferable t = clpbrd.getContents( null );
					try {
						if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
							Object o = t.getTransferData(DataFlavor.stringFlavor);
							String data = (String) t.getTransferData(DataFlavor.stringFlavor);
							out_l += data;
							Aus.setText(Inhalt + out_l + cursor + out_r);
						}
					}catch (Exception ignore){

					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (Aus.getSelectedText() != null) {
					String s = Aus.getSelectedText();

					copyToClipboard(s);
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}
		});
	}

	public static void firstup(){
		prefix = "<html><font color=yellow>" + NameB + "@" + System.getProperty("os.name") + ": " + "</font>";
		out_l = "";
		Aus.setContentType("text/html");
		Aus.setText(Inhalt + prefix + Farbe + cursor);
	}

	public static void updatek(){
	    Aus.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
            	System.out.println(e.getKeyCode());
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
	    			Aus.setText(Inhalt + prefix + Farbe + out_l + cursor + out_r);
				}
				if (e.getKeyCode() == 38)
				{
					if(!(history.size()==0)) {
						if (p >= history.size())
							p = 0;
						out_r = "";
						out_l = history.get(p);
						p++;
						Aus.setContentType("text/html");
						Aus.setText(Inhalt + prefix + Farbe + out_l + cursor + out_r);
					}
				}


				if (e.getKeyCode() == 39) {
					if(out_r.length() != 0)
						posi++;
					String local_out = out_l + out_r;
					out_l = local_out.substring(0, posi);
					out_r = local_out.substring(posi);
					Aus.setText(Inhalt + prefix + Farbe + out_l + cursor + out_r);
				}
				if (e.getKeyCode() == 40){
					if(!(history.size()==0)) {
						if (p == 0)
							p = history.size();
						out_r = "";
						p--;
						out_l = history.get(p);
						Aus.setContentType("text/html");
						Aus.setText(Inhalt + prefix + Farbe + out_l + cursor + out_r);
					}
				}


				if ( e.getKeyCode() != 10 & e.getKeyCode() != 38 & e.getKeyCode() != 40 & e.getKeyCode() != 20 & e.getKeyCode() != 16 & e.getKeyCode() != 0 & e.getKeyCode() != 18 & e.getKeyCode() != 17 & e.getKeyCode() != 37 & e.getKeyCode() != 39 & e.getKeyCode() != 157 & e.getKeyCode() != 8) {
                    out_l += e.getKeyChar();
                    posi++;

                    Aus.setContentType("text/html");
                    Aus.setText(Inhalt + prefix + Farbe + out_l + cursor + out_r);
                }
                if (e.getKeyCode() == 8) {
                    if (out_l.length() > 0){
							posi--;

							out_l = out_l.substring(0, out_l.length() - 1);

							Aus.setContentType("text/html");
							Aus.setText(Inhalt + prefix + Farbe + out_l + cursor + out_r);
					}
                }
                if ( e.getKeyCode() == 10){
                    Inhalt += prefix + Farbe + out_l + out_r + "<br>";
                    Aus.setContentType("text/html");
                    Aus.setText(Inhalt);
                    runCommand(out_l + out_r);
                    history.add(0, out_l + out_r);
                    posi = 0;
                    p = 0;
                    firstup();
                }
            }
        });
    }

	public static Color FarbeHintergrund(String farbe) {
		Color output = null;

		if (farbe.equals("2") || farbe.equals("blau")) {
			output = Color.BLUE;
			FarbeH2 = "2";
		}
		if (farbe.equals("1") || farbe.equals("grün")) {
			output = Color.GREEN;
			FarbeH2 = "1";
		}
		if (farbe.equals("0") || farbe.equals("weiß")) {
			output = Color.WHITE;
			FarbeH2 = "0";
		}
		if (farbe.equals("3") || farbe.equals("schwarz")) {
			output = Color.BLACK;
			FarbeH2 = "3";
		}
		if (farbe.equals("4") || farbe.equals("rot")) {
			output = Color.RED;
			FarbeH2 = "4";
		}
		if (farbe.equals("5") || farbe.equals("gelb")) {
			output = Color.YELLOW;
			FarbeH2 = "5";
		}

		return output;
	}

	public static String Farbeschrift(String farbe) {
		if (farbe.equals("3") || farbe.equals("blau")) {
			FarbeS2 = "3";
			return "<font color=blue>";
		}
		if (farbe.equals("1") || farbe.equals("grün")) {
			FarbeS2 = "1";
			return "<font color=green>";
		}
		if (farbe.equals("2") || farbe.equals("weiß")) {
			FarbeS2 = "2";
			return "<font color=white>";
		}
		if (farbe.equals("0") || farbe.equals("schwarz")) {
			FarbeS2 = "0";
			return "<font color=black>";
		}
		if (farbe.equals("4") || farbe.equals("rot")) {
			FarbeS2 = "4";
			return "<font color=red>";
		}
		if (farbe.equals("5") || farbe.equals("gelb")) {
			FarbeS2 = "5";
			return "<font color=yellow>";
		}
		return "";
	}

	private static void timer(int time) {
		update("start", false);
		Timer t = new Timer();
		TimerTask tt = new TimerTask() {
			public void run() {
				sec++;
				if (sec == time) {
					t.cancel();
					sec = 0;
					Inhalt += "Fertig";
					Aus.setContentType("text/html");
					Aus.setText(Inhalt + prefix + Farbe + out_l + cursor + out_r);
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

	public static void Spiele(String[] name) {
		if (stop) {
			aa = "";
			for (int i = 1; i < name.length; i++)
				aa += name[i] + " ";
			aa = aa.substring(0, aa.length() - 1);
			aa = "iridium/Musik/" + aa + ".mp3";
			Spiele(aa);
		}
	}

	public static void Spiele(String Path) {
		if (stop) {
			aa = Path;
			try {
				f = new FileInputStream(aa);
				play = new AdvancedPlayer(f);
				frame = 0;
			} catch (Exception e) {
				update("Mp3 nicht gefunden", false);
			}
			stop = false;
		}

		if (!erste) {
			t1.start();
			erste = true;
		}
	}

	private static void Stop() {
		stop = true;
		try {
			play.stop();
			play.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void Datei(String Name) {
		File folder = new File("iridium/Dateien");
		File file = new File("iridium/Dateien/" + Name + ".txt");
		if (!folder.exists()) {
			folder.mkdir();
		}
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		update("Erfolgreich", false);
	}

	private static void Löschen(String Name) {
		File file = new File("iridium/Dateien/" + Name);

		if (file.delete())
			update("Erfolgreich", false);
		else
			update("Fehler!!", false);

	}

	private static File[] getFiles(String wo, boolean out) {
		String inhalt = "";
		File folder = new File(wo);
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
			if (file.isFile()) {
				inhalt += file.getName() + "  ";
			}
		}
		if (out)
			update(inhalt, false);
		return listOfFiles;
	}

	public static void Schreiben(String text, String wo, boolean rela, boolean newl) {
		String path;
		String[] split = text.split("\\s+");
		if (rela)
			path = "iridium/Dateien/" + wo + ".txt";
		else
			path = wo;
		File file = new File(path);
		try {
			FileWriter fw = new FileWriter(file);
			if (!newl)
				for (String a : split)
					fw.write(" " + a);
			else
				for (String a : split)
					fw.write(a + System.lineSeparator());

			fw.close();
		} catch (IOException e3) {
			e3.printStackTrace();
		}
	}

	public static ArrayList<String> Lesen(String wo, boolean rela) {
		String P;
		ArrayList<String> Lesen = new ArrayList<>();
		if (rela)
			P = "iridium/Dateien/" + wo + ".txt";
		else
			P = wo;
		File f = new File(P);

		try {
			Scanner sc = new Scanner(f);
			while (sc.hasNext()) {
				Lesen.add(sc.next());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return Lesen;
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
		Ausgabe.setIconImage(icon.getImage());

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
					Schreiben(text.getText(), title, true, false);
				else
					Schreiben(text.getText(), Path, false, false);
				Ausgabe.dispose();
			Inhalt += "Gespeichert";
			Aus.setContentType("text/html");
			Aus.setText(Inhalt + prefix + Farbe + out_l + cursor + out_r);
		});

	}

	public static void AusgabeFeld(String a, String title, boolean be, boolean rela, String Path, boolean html) {
		String in = "";
		JFrame Ausgabe = new JFrame(title);
		Ausgabe.setVisible(true);
		Ausgabe.setSize(450, 600);
		Ausgabe.setResizable(false);
		Ausgabe.setLocationRelativeTo(null);
		Ausgabe.setLayout(new BorderLayout());
		Ausgabe.setIconImage(icon.getImage());

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
					Schreiben(text.getText(), title, true, false);
				else
					Schreiben(text.getText(), Path, false, false);
				Ausgabe.dispose();
				Inhalt += "Gespeichert";
				Aus.setContentType("text/html");
				Aus.setText(Inhalt + prefix + Farbe + out_l + cursor + out_r);
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

	public static void clear() {
		Inhalt = "";
		Aus.setText("");
	}

	public static void Short() {
		String cut = "";
		int index;
		ArrayList<String> a = Lesen("iridium/Iridiumconfig.txt", false);
		index = a.indexOf("Shortcut");
		for (int i = index + 1; i < a.size(); i++)
			cut += a.get(i) + " ";

		methods.runCommand(cut);

	}

	private static void SetShort(String[] a) {
		if (call.checkCommands(a[1]))
		{
			String ab = "";
			ArrayList<String> aa = Lesen("iridium/Iridiumconfig.txt", false);

			int index = aa.indexOf("Shortcut");

			for (int i = 0;i < aa.size();i++) {
				if (index+1 > i)
					ab += aa.get(i) + " ";
			}

			for (int i = 1;i < a.length;i++)
				ab += a[i] + " ";
			System.out.println(ab);
			Schreiben(ab, "iridium/Iridiumconfig.txt", false, true);
		}
		else
			update("Fehler!!", false);

	}

	public static void getShortcut() {
		ArrayList<String> a = Lesen("iridium/Iridiumconfig.txt", false);
		int index;
		String aa = "Momentaner Shortcut : ";
		index = a.indexOf("Shortcut");
		for (int i = index + 1; i < a.size(); i++)
			aa += a.get(i) + " ";
		update(aa, false);

	}

	public static void SchriftF(String farbeh, String farbes) {
		String a;
		methods.Farbe = Farbeschrift(farbes);
		a = NameB + " " + farbeh + " " + farbes + " ";
		for (String b : methods.Lesen("iridium/Iridiumconfig.txt", false))
			a += b + " ";
		Schreiben(a, "iridium/Iridiumconfig.txt", false, true);
	}

	public static void HintergrundF(String farbeh, String farbes) {
		String a;
		Color col = FarbeHintergrund(farbeh);
		a = NameB + " " + farbeh + " " + farbes + " ";
		for (String b : methods.Lesen("iridium/Iridiumconfig.txt", false))
			a += b + " ";
		Schreiben(a, "iridium/Iridiumconfig.txt", false, true);
		meinFrame.setBackground(col);
		Jp1.setBackground(col);
		Aus.setBackground(col);
		Jp3.setBackground(col);
	}

	private static void ProgrammStart(String Programm) {
		String b = "";
		ArrayList<String> a = Lesen("iridium/Programmeconfig.txt", false);
		int pos = a.indexOf(Programm);
		for (int i = pos + 1; i < a.size(); i++) {
			if (!a.get(i).equals("ende"))
				b += a.get(i) + " ";
			else
				break;
		}
		try {
			new ProcessBuilder(b).start();
		} catch (Exception ignore) {

		}

	}

	public static void addProgramm(String[] a) {
		String b = "";
		ArrayList<String> c = Lesen("iridium/Programmeconfig.txt", false);
		if (!c.contains(a[3]) || !c.contains(a[2])) {
			for (int i = 0; i < c.size(); i++)
				b += c.get(i) + " ";

			for (int i = 1; i < a.length; i++)
				b += a[i] + " ";
			b += "ende";
			SchreibenP(b);
		}
	}

	public static void addProgramm(String a) {
		boolean weiter = true;
		if (a.contains(":/")) {
			String b = "";
			ArrayList<String> c = Lesen("iridium/Programmeconfig.txt", false);
			ArrayList<String> d = getProgramm();
			for (String e : d)
				if (a.contains(e)) {
					weiter = false;
				}
			if (weiter) {
				for (String e : c)
					b += e + " ";

				b += a + " ";
				b += "ende";
				SchreibenP(b);
			}
		}
	}

	public static void SchreibenP(String b) {
		File file = new File("iridium/Programmeconfig.txt");
		try {
			FileWriter fw = new FileWriter(file);
			fw.write(b + System.lineSeparator());
			fw.close();
		} catch (IOException e3) {
			e3.printStackTrace();
		}
	}

	public static ArrayList<String> getProgramm() {
		ArrayList<String> a = new ArrayList<>();
		String b = "";
		File f = new File("iridium/Programmeconfig.txt");
		try {
			Scanner sc = new Scanner(f);
			a.add(sc.next());
			while (sc.hasNext()) {
				if (!sc.hasNext("ende"))
					b += sc.next();
				else {
					a.add(b);
					sc.next();
					if (sc.hasNext())
						a.add(sc.next());
					b = "";
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return a;
	}

	public static void runCommand(String f) {
		splited = f.split("\\s+");
		String input = Feld.getText();

		update(input, true);
		Feld.setText("");
		call.runCommand(splited);
	}
}


	

