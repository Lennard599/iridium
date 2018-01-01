package iridium;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Iridium {
	public static final String version ="0.1.1";

	public static JFrame meinFrame = new JFrame("Iridium "+version);
	public static ImageIcon icon = new ImageIcon("iridium\\icon.png");
	public static JTextPane Aus = new JTextPane();
	public static JScrollPane scroll = new JScrollPane(Aus);

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

		new ConfHandler();
		Filehandeling.enableFiledrop();

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
		MenuItem Genaeral = new MenuItem("General");
        MenuItem visuals = new MenuItem("Visuals");
        MenuItem Programs = new MenuItem("Programs");
		Optionen.add(Genaeral);
        Optionen.add(visuals);
        Optionen.add(Programs);
		menue.add(shortcut);
		menue.add(clear);
		menue.add(Hilfe);
		menue.add(Optionen);

		//Fenster main
		meinFrame.setSize(600,400);
		meinFrame.setLocationRelativeTo(null);
		if (ConfHandler.getConf("dontshow:").trim().equals("N"))
			meinFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		else
			meinFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		scroll.setViewportBorder(null);
		scroll.setBorder(null);
		meinFrame.setMenuBar(menue);
		meinFrame.add(scroll);
		meinFrame.setIconImage(icon.getImage());
        meinFrame.setVisible(true);
		meinFrame.setMinimumSize(new Dimension(600, 400));
		meinFrame.setSize(601, 401);
		meinFrame.setSize(600, 400);
        Aus.setEditable(false);

        if (ConfHandler.getConf("dontshow:").trim().equals("N")) {
			WindowListener close = new WindowAdapter() {

				public void windowClosing(WindowEvent e) {
					JCheckBox checkbox = new JCheckBox("don't show again");
					String message = "are you sure you want to quit?";
					Object[] params = {message, checkbox};
					int n = JOptionPane.showConfirmDialog(scroll, params, "Quit?", JOptionPane.YES_NO_OPTION);
					if (checkbox.isSelected())
						ConfHandler.setConf("dontshow:", "Y");

					if (!ConfHandler.getConf("dontshow:").trim().equals("N"))
                        ConfHandler.writeConf();

					if (n == 0)
						System.exit(0);

				}
			};
			meinFrame.addWindowListener(close);
		}

		new Presentation();

		Genaeral.addActionListener(e -> Options.Option(0));
        visuals.addActionListener(e -> Options.Option(1));
        Programs.addActionListener(e -> Options.Option(2));

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