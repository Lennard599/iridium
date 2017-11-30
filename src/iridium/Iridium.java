package iridium;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;

import net.iharder.dnd.FileDrop;

public class Iridium {
	public static final String version ="0.1.0";

	public static JFrame meinFrame = new JFrame("Iridium "+version);
	public static ImageIcon icon = new ImageIcon("iridium\\icon.png");
	public static JTextPane Aus = new JTextPane();
	private static JScrollPane scroll = new JScrollPane(Aus);

	public static void main(String[] args) {
		if(args.length > 0){
			String a = "";
			Updater.delet(args[0]);
			for (int i = 1; i < args.length; i++)
				a += args[i]+" ";
			Presentation.update(a,false);
		}

		new methods();
		if (System.getProperty("os.name").contains("Mac"))
			System.setProperty("apple.laf.useScreenMenuBar", "true");

		File g = javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory();

		ConfHandler ch = new ConfHandler();

		ch.ReadConf();

		try {
			File h = new File(g.getPath() + "new.txt");
			h.createNewFile();
		}catch (Exception e){
			System.out.println("Error: " + e);
		}

		//Menu
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

		//Fenster main
		meinFrame.setSize(600,400);
		meinFrame.setLocationRelativeTo(null);
		if (!ConfHandler.dontShow)
			meinFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		else
			meinFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		scroll.setViewportBorder(null);
		scroll.setBorder(null);
		Aus.addStyle("Menlo", null);
		meinFrame.setVisible(true);
		meinFrame.setMenuBar(menue);
		meinFrame.add(scroll);
		meinFrame.setIconImage(icon.getImage());
		meinFrame.setMinimumSize(new Dimension(600, 400));
		meinFrame.setSize(601, 401);
		meinFrame.setSize(600, 400);
        Aus.setEditable(false);

        //Filedrop
		new FileDrop(scroll, files -> {
				for (File a : files) {
					if (a.getName().contains(".mp3"))
						Player.Spiele(a.getAbsolutePath());
					else {
							try {
								XWPFDocument docx = new XWPFDocument(new FileInputStream(a.getAbsolutePath()));
								XWPFWordExtractor we = new XWPFWordExtractor(docx);

								methods.AusgabeFeld(we.getText(), a.getName(), false, false, "", true);

								System.out.println(we.getText());
							} catch (Exception e) {
								Presentation.update("Error filetype not supported", true);
							}
					}
				}
		});

		if (!ConfHandler.dontShow) {
			WindowListener close = new WindowAdapter() {

				public void windowClosing(WindowEvent e) {
					JCheckBox checkbox = new JCheckBox("Nicht mehr anzeigen");
					String message = "Sind sie sicher das sie Iridium Beenden wollen?";
					Object[] params = {message, checkbox};
					int n = JOptionPane.showConfirmDialog(scroll, params, "Beenden?", JOptionPane.YES_NO_OPTION);
					ConfHandler.dontShow = checkbox.isSelected();

					if (ConfHandler.dontShow)
						Filehandeling.Schreiben(methods.ALzuSt(Filehandeling.Lesen("iridium/Iridiumconfig.txt", false),3,"J"),"iridium/Iridiumconfig.txt", false, true);

					if (n == 0)
						System.exit(3);

				}
			};
			meinFrame.addWindowListener(close);
		}
	
		//events
		Color col = Presentation.FarbeHintergrund(ConfHandler.FarbeH2);
		meinFrame.setBackground(col);
	    Aus.setBackground(col);

	    Presentation.Farbe = Presentation.Farbeschrift(ConfHandler.FarbeS2);

		Presentation.update("Wilkommen " + ConfHandler.NameB, false);
		Presentation.firstup();
		Presentation.updatek();
		Presentation.Mouse();
		
		//optionenfenster und event
		Optioneni.addActionListener(e -> {
                JLabel OptionName = new JLabel("Ihr Name");
                String[] FarbListe = {"Weiß", "Grün", "Blau", "Schwarz", "Rot"};
                String[] FarbListeS = {"Schwarz", "Grün", "Weiß", "Blau", "Rot"};
                JComboBox FarbeH = new JComboBox(FarbListe);
                JComboBox FarbeS = new JComboBox(FarbListeS);
                JButton anwenden = new JButton("Anwenden");
                JLabel OptionFarbeH = new JLabel("Hintergrundfarbe");
                JLabel OptionFarbeS = new JLabel("Schriftfarbe");
                JTextField Name = new JTextField(10);
                JButton Programme = new JButton("Prgramme");

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
				Name.setText(ConfHandler.NameB);
				Jp4.add(OptionFarbeH);
				Jp4.add(FarbeH);
				Jp5.add(OptionFarbeS);
				Jp5.add(FarbeS);
				Jp6.add(OptionName);
				Jp6.add(Name);
				Jp8.add(Programme);
				Jp7.add(anwenden);

				FarbeH.setSelectedIndex(Integer.parseInt(ConfHandler.FarbeH2));
				FarbeS.setSelectedIndex(Integer.parseInt(ConfHandler.FarbeS2));
				OptionenF.add(Jp4);
				OptionenF.add(Jp5);
				OptionenF.add(Jp6);
				OptionenF.add(Jp8);
				OptionenF.add(Jp7);

                FarbeH.addActionListener(ee -> {
                int FarbeH1 = FarbeH.getSelectedIndex();
                ConfHandler.FarbeH2 = Integer.toString(FarbeH1);
                Presentation.HintergrundF(ConfHandler.FarbeH2,ConfHandler.FarbeS2);
            });

                FarbeS.addActionListener(ee -> {
                int FarbeS1 = FarbeS.getSelectedIndex();
                ConfHandler.FarbeS2 = Integer.toString(FarbeS1);
                Presentation.SchriftF(ConfHandler.FarbeH2, ConfHandler.FarbeS2);
            });

                anwenden.addActionListener(ee -> {
                String B;
                ConfHandler.NameB =	Name.getText();
                B = ConfHandler.NameB + " " + ConfHandler.FarbeH2 + " " + ConfHandler.FarbeS2 + " ";
                for(int i = 3;i < Filehandeling.Lesen("iridium/Iridiumconfig.txt", false).size(); i++)
                    B += Filehandeling.Lesen("iridium/Iridiumconfig.txt", false).get(i) + " ";
                Filehandeling.Schreiben(B, "iridium/Iridiumconfig.txt", false ,true);
            });

                Programme.addActionListener(ee -> {
                ArrayList<String> a = Programmstart.getProgramm();
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
                JButton anwendenp = new JButton("Anwenden");
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
                links.add(anwendenp);

                links.setSize(300,370);
                links.setResizable(false);
                links.setLocationRelativeTo(null);
                links.setVisible(true);
                links.setLayout(new GridLayout(12,1));
                links.setIconImage(icon.getImage());

                fr.addActionListener(eee -> {
                    ArrayList<String> ac = new ArrayList<>();
                    ac.add("Mit der Funktion Programme kann mann Commands erstellen um .exe dateien zu Öffnen Links wird der Command und rechts der Absoltue Pfad zur .exe eingetragen. Wenn mann mehr als zehn Commands erstellen will kann mann dies mitt Commands tun (siehe hilfe).Es können auch Commands mehrmals verwendet werden.");
                    methods.AusgabeFeld(ac, "Hilfe zu Programme", false, true);
                });

                anwendenp.addActionListener(eee -> {
                    System.out.println("vor");
                    String ad = "";
                    for (int i = 0; i < 10;i++)
                    {
                        ad += Pname[i].getText() + " ";
                        ad += Plink[i].getText() + " ";
                        Programmstart.addProgramm(ad);
                        ad = "";
                    }
                });
            });


        });

		getshort.addActionListener(e -> {
			methods.getShortcut();
			Presentation.posi = 0;
			Presentation.p = 0;
			Presentation.firstup();});

		Hilfei.addActionListener(e -> methods.Hilfe());

		cleari.addActionListener(e -> {
			Presentation.clear();
			Presentation.posi = 0;
			Presentation.p = 0;
			Presentation.firstup();});

		shortcuti.addActionListener(e -> methods.Short());
		
    }
}
