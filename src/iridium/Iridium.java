package iridium;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.text.DefaultEditorKit;

import net.iharder.dnd.FileDrop;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;

public class Iridium {
	public static String FarbeH2, FarbeS2;
	public static String NameB = "Iridium";
	private static int FarbeH1, FarbeS1;
	private static String[] FarbListe = {"Weiß", "Grün", "Blau", "Schwarz", "Rot"};
	private static String[] FarbListeS = {"Schwarz", "Grün", "Weiß", "Blau", "Rot"};
	private static int indexein = 0;
	private static boolean dontShow = false;
	
	public static JFrame meinFrame = new JFrame("Iridium");
	public static JPanel Jp1 = new JPanel ();
	public static JPanel Jp3 = new JPanel ();
	private static JComboBox FarbeH = new JComboBox(FarbListe);
	private static JComboBox FarbeS = new JComboBox(FarbListeS);
	private static JButton anwenden = new JButton("Anwenden");
	private static JTextField Name = new JTextField(10);
	public static JTextField Feld = new JTextField();
	private static JLabel OptionFarbeH = new JLabel("Hintergrundfarbe");
	private static JLabel OptionFarbeS = new JLabel("Schriftfarbe");
	private static JLabel OptionName = new JLabel("Ihr Name");
	private static JButton Programme = new JButton("Prgramme");
	public static ImageIcon icon = new ImageIcon("iridium\\icon.png");
	private static ArrayList<String> Histo = new ArrayList<>();
	public static JTextPane Aus = new JTextPane();
	private static JScrollPane scrol = new JScrollPane(Aus);
	private static File file = new File("iridium/Iridiumconfig.txt");

	public static void main(String[] args)
	{
		new methods();
		if (System.getProperty("os.name").contains("Mac"))
			System.setProperty("apple.laf.useScreenMenuBar", "true");

		MenuBar menue = new MenuBar();
		Menu shortcut = new Menu("shortcut");
		Menu clear = new Menu("clear");
		Menu Hilfe = new Menu("help");
		Menu Optionen = new Menu("option");
		MenuItem shortcuti = new MenuItem("start");
		shortcut.add(shortcuti);
		MenuItem getshort = new MenuItem("get shortcut");
		shortcut.add(getshort);
		MenuItem cleari = new MenuItem("clear");
		clear.add(cleari);
		MenuItem Hilfei = new MenuItem("open");
		Hilfe.add(Hilfei);
		MenuItem Optioneni = new MenuItem("open");
		Optionen.add(Optioneni);
		menue.add(shortcut);
		menue.add(clear);
		menue.add(Hilfe);
		menue.add(Optionen);

		Aus.setEditable(false);

		File folder = new File("iridium");
		File Dateien = new File("iridium/Dateien");
		File Musik = new File("iridium/Musik");
		File Programmeinfo = new File("iridium/Programmeconfig.txt");

		try {
			Scanner sc = new Scanner(file);
			if (sc.hasNext())
			{
				NameB = sc.nextLine();

				FarbeH2 = sc.nextLine();
			
				FarbeS2 = sc.nextLine();

				if (sc.nextLine().equals("J"))
				dontShow = true;

			}
			else
			{
				String a = "";
				for(int i = 3;i < methods.Lesen("iridium/Iridiumconfig.txt", false).size(); i++)
					a += methods.Lesen("iridium/Iridiumconfig.txt", false).get(i) + " ";
				methods.Schreiben(NameB + " " + "0 " + "0 " + "N " + " Shortcut " ,"iridium/Iridiumconfig.txt" ,false ,true);
				FarbeH2 = "0";
				FarbeS2 = "0";
				System.out.println("!!");
			}
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}
		if (!folder.exists())
			folder.mkdir();
		if (!Dateien.exists())
			Dateien.mkdir();
		if (!Musik.exists())
			Musik.mkdir();
		if (!file.exists())
		{
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if (!Programmeinfo.exists())
		{
			try {
				Programmeinfo.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		//Fenster main
		meinFrame.setSize(600,400);
		meinFrame.setLocationRelativeTo(null);
		if (!dontShow)
			meinFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		else
			meinFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		meinFrame.setVisible(true);
		meinFrame.setMenuBar(menue);
		meinFrame.add(scrol);
		meinFrame.setIconImage(icon.getImage());
		meinFrame.setMinimumSize(new Dimension(600, 400));
		meinFrame.setSize(601, 401);
		meinFrame.setSize(600, 400);

		new FileDrop(scrol, files -> {
				for (File a : files) {
					if (a.getName().contains(".mp3"))
						methods.Spiele(a.getAbsolutePath());
					else {
						try {
							BodyContentHandler handler = new BodyContentHandler();
							AutoDetectParser parser = new AutoDetectParser();
							org.apache.tika.metadata.Metadata metadata = new org.apache.tika.metadata.Metadata();
							InputStream stream = new FileInputStream(a);
							parser.parse(stream, handler, metadata);
							methods.AusgabeFeld(handler.toString(), a.getName(), true, false, a.getAbsolutePath(),false);
						} catch (Exception e) {
							methods.update("Ein Fehler ist aufgetreten oder der Dateityp wird nich unterstützt! unterschtützt .txt und .mp3", false);
						}
					}
				}
		});

		if (!dontShow) {
			WindowListener close = new WindowAdapter() {

				public void windowClosing(WindowEvent e) {
					JCheckBox checkbox = new JCheckBox("Nicht mehr anzeigen");
					String message = "Sind sie sicher das sie Iridium Beenden wollen?";
					Object[] params = {message, checkbox};
					int n = JOptionPane.showConfirmDialog(scrol, params, "Beenden?", JOptionPane.YES_NO_OPTION);
					dontShow = checkbox.isSelected();

					if (dontShow)
						methods.Schreiben(methods.ALzuSt(methods.Lesen("iridium/Iridiumconfig.txt", false),3,"J"),"iridium/Iridiumconfig.txt", false, true);

					if (n == 0)
						System.exit(3);

				}
			};
			meinFrame.addWindowListener(close);
		}
	
		//events
		Color col = methods.FarbeHintergrund(FarbeH2);
		meinFrame.setBackground(col);
	    //Jp1.setBackground(col);
	    Aus.setBackground(col);
	    //Jp3.setBackground(col);

	    methods.Farbe = methods.Farbeschrift(FarbeS2);

		methods.update("Wilkommen " + NameB, false);
		methods.firstup();
		methods.updatek();
		methods.Mouse();


		//eingabe
		Feld.addActionListener(e -> {
				String fe = Feld.getText();
				Histo.add(0,fe);
				indexein = -1;
				methods.runCommand(fe);
		});
		
		//optionenfenster und event
		Optioneni.addActionListener(e -> {
				JPanel Jp4 = new JPanel ();
				JPanel Jp5 = new JPanel ();
				JPanel Jp6 = new JPanel();
				JPanel Jp7 = new JPanel();
				JPanel Jp8 = new JPanel();
				JFrame OptionenF = new JFrame("Iridium");
				OptionenF.setSize(300,400);
				OptionenF.setResizable(false);
				OptionenF.setLocationRelativeTo(null);
				OptionenF.setVisible(true);
				OptionenF.setLayout(new GridLayout(10,2));
				OptionenF.setIconImage(icon.getImage());
				OptionenF.setLocation(meinFrame.getLocation());
				Name.setText(NameB);
				Jp4.add(OptionFarbeH);
				Jp4.add(FarbeH);
				Jp5.add(OptionFarbeS);
				Jp5.add(FarbeS);
				Jp6.add(OptionName);
				Jp6.add(Name);
				Jp8.add(Programme);
				Jp7.add(anwenden);

				FarbeH.setSelectedIndex(Integer.parseInt(FarbeH2));
				FarbeS.setSelectedIndex(Integer.parseInt(FarbeS2));
				OptionenF.add(Jp4);
				OptionenF.add(Jp5);
				OptionenF.add(Jp6);
				OptionenF.add(Jp8);
				OptionenF.add(Jp7);
		});

		getshort.addActionListener(e -> {
			methods.getShortcut();
			methods.posi = 0;
			methods.p = 0;
			methods.firstup();});

		Programme.addActionListener(e -> {
				ArrayList<String> a = methods.getProgramm();
				JFrame links = new JFrame("Iridium");
				JTextField[] Pname = new JTextField[10];
				JTextField[] Plink = new JTextField[10];
				JPanel[] Jps = new JPanel[11];
				Jps[0] = new JPanel();
				JLabel co = new JLabel("Command     ");
				Jps[0].add(co);
				JLabel ab = new JLabel("     Absoluterpfad");
				Jps[0].add(ab);
				JButton fr = new JButton();
				JButton anwenden = new JButton("Anwenden");
				ImageIcon help = new ImageIcon("iridium/Button-help-icon.png");
				fr.setIcon(help);
				Jps[0].add(fr);
				fr.setOpaque(false);
				fr.setContentAreaFilled(false);
				fr.setBorderPainted(false);
				links.add(Jps[0]);
				int j = 0;
				for (int i = 0;i < 10;i++)
				{
					Jps[i+1] = new JPanel();
					Pname[i] = new JTextField(8);
					Plink[i] = new JTextField(12);
					Plink[i].setToolTipText(" Pfad zu .exe");
					Pname[i].setToolTipText("Start Command");
					try {
						Pname[i].setText(a.get(j));
						Plink[i].setText(a.get(j + 1));
						j += 2;
					} catch (Exception ignor)
					{

					}
					Jps[i+1].add(Pname[i]);
					Jps[i+1].add(Plink[i]);
					links.add(Jps[i+1]);
				}
				links.add(anwenden);

				links.setSize(300,370);
				links.setResizable(false);
				links.setLocationRelativeTo(null);
				links.setVisible(true);
				links.setLayout(new GridLayout(12,1));
				links.setIconImage(icon.getImage());

				fr.addActionListener(ee -> {
						ArrayList<String> ac = new ArrayList<>();
						ac.add("Mit der Funktion Programme kann mann Commands erstellen um .exe dateien zu Öffnen Links wird der Command und rechts der Absoltue Pfad zur .exe eingetragen. Wenn mann mehr als zehn Commands erstellen will kann mann dies mitt Commands tun (siehe hilfe).Es können auch Commands mehrmals verwendet werden.");
						methods.AusgabeFeld(ac, "Hilfe zu Programme", false, true);
				});

				anwenden.addActionListener(eee -> {
						System.out.println("vor");
						String ad = "";
						for (int i = 0; i < 10;i++)
						{
							ad += Pname[i].getText() + " ";
							ad += Plink[i].getText() + " ";
								methods.addProgramm(ad);
								ad = "";
						}
				});
		});
		
		Hilfei.addActionListener(e -> methods.Hilfe());

		cleari.addActionListener(e -> {
			methods.clear();
			methods.posi = 0;
			methods.p = 0;
			methods.firstup();});

		shortcuti.addActionListener(e -> methods.Short());
		
	//Obtionen
		FarbeH.addActionListener(e -> {
				FarbeH1 = FarbeH.getSelectedIndex();
				FarbeH2 = Integer.toString(FarbeH1);
				methods.HintergrundF(FarbeH2,FarbeS2);
		});
		
		FarbeS.addActionListener(e -> {
				FarbeS1 = FarbeS.getSelectedIndex();
				FarbeS2 = Integer.toString(FarbeS1);
				methods.SchriftF(FarbeH2, FarbeS2);
    	});

		anwenden.addActionListener(e -> {
				String B;
				NameB =	Name.getText();
				B = NameB + " " + FarbeH2 + " " + FarbeS2 + " ";
				for(int i = 3;i < methods.Lesen("iridium/Iridiumconfig.txt", false).size(); i++)
					B += methods.Lesen("iridium/Iridiumconfig.txt", false).get(i) + " ";
				methods.Schreiben(B, "iridium/Iridiumconfig.txt", false ,true);
		});

		Feld.addKeyListener(new KeyListener()  {

			public void keyTyped(KeyEvent e) {

			}

			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 38)
				{
					indexein++;
					if (indexein == Histo.size())
						indexein = 0;
					try {
						Feld.setText(Histo.get(indexein));
					} catch (IndexOutOfBoundsException ignored) {

					}
				}

				if (e.getKeyCode() == 40)
				{
					indexein--;
					if (indexein == -1)
						indexein = Histo.size()-1;
					try {
						Feld.setText(Histo.get(indexein));
					} catch (IndexOutOfBoundsException ignored) {

					}
				}
			}

			public void keyReleased(KeyEvent e) {

			}
		});
		
    }
}
