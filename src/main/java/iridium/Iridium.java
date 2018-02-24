package iridium;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Method;
import javax.swing.*;

public class Iridium {
	public static final String version ="0.4.2";

	public static JFrame meinFrame = new JFrame("Iridium "+version);
	public static ImageIcon icon = new ImageIcon(Iridium.class.getClassLoader().getResource("icon.png").getFile());
	public static JTextPane Aus = new JTextPane();
	public static JScrollPane scroll = new JScrollPane(Aus);
	public static JPanel status = new JPanel();

	public static void main(String[] args) {
		if(args.length > 0){
			String a = "";
			Updater.delet(args[0]);
			for (int i = 1; i < args.length; i++)
				a += args[i]+" ";
			Presentation.update(a,false);
		}

		new Methods();
		Programmstart.readPrograms();
		if (System.getProperty("os.name").contains("Mac")) {
			String className = "com.apple.eawt.FullScreenUtilities";
			String methodName = "setWindowCanFullScreen";

			try {
				Class<?> clazz = Class.forName(className);
				Method method = clazz.getMethod(methodName, new Class<?>[] {
						Window.class, boolean.class });
				method.invoke(null, meinFrame, true);
			} catch (Throwable t) {
				System.err.println("Full screen mode is not supported");
				t.printStackTrace();
			}
			System.setProperty("apple.laf.useScreenMenuBar", "true");
		}

		new ConfHandler();
		Filehandeling.enableFiledrop();

		//Menu
		MenuBar menue = new MenuBar();
		Menu shortcut = new Menu("shortcut");
		Menu clear = new Menu("clear");
		Menu Hilfe = new Menu("help");
		Menu Optionen = new Menu("settings");
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
		meinFrame.setSize(620,400);
		meinFrame.setLocationRelativeTo(null);
		if (ConfHandler.getConf("dontshow:").trim().equals("N"))
			meinFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		else
			meinFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		scroll.setViewportBorder(null);
		scroll.setBorder(null);
		meinFrame.setMenuBar(menue);
		meinFrame.add(scroll, BorderLayout.CENTER);
		meinFrame.setIconImage(icon.getImage());
        meinFrame.setVisible(true);
		meinFrame.setMinimumSize(new Dimension(620, 400));
		meinFrame.setSize(621, 401);
        Aus.setEditable(false);

		LayoutManager overlay = new OverlayLayout(Aus);
		Aus.setLayout(overlay);
		Aus.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Aus.removeAll();
				meinFrame.revalidate();
				meinFrame.repaint();
			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}
		});

		FlowLayout layout = new FlowLayout();
        meinFrame.add(status, BorderLayout.SOUTH);
        status.setBackground(Color.BLACK);
        layout.setVgap(0);
        JPanel date = new JPanel(layout);
        JPanel path = new JPanel(layout);
		status.setLayout(new BoxLayout(status, BoxLayout.LINE_AXIS));
		status.add(path);
		status.add(Box.createHorizontalGlue());
		status.add(date);

		JLabel Left = new JLabel("/Docs*");
		Sensor sensor = new Sensor(Left);
		Thread t1 = new Thread(sensor);
		t1.start();
		Left.setForeground(Color.WHITE);
		Left.setFont(new Font("monospace", Font.PLAIN, 14));
		path.add(Left);
		path.setBackground(new Color(209,58,130));
		path.setPreferredSize(new Dimension(180,20));
		path.setMaximumSize(path.getPreferredSize());

		JLabel date_L = new JLabel("Placeholder");
		Clock clock = new Clock(date_L);
		Thread t = new Thread(clock);
		t.start();
		date_L.setForeground(Color.WHITE);
		date_L.setFont(new Font("monospace", Font.PLAIN, 14));
		date.add(date_L);
		date.setBackground(new Color(46,140,207));
		date.setPreferredSize(new Dimension(160,20));
		date.setMaximumSize(date.getPreferredSize());
		meinFrame.setSize(600, 400);

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

		Genaeral.addActionListener(e -> Aus.add(new Options(0, Aus)));
        visuals.addActionListener(e -> Aus.add(new Options(1, Aus)));
        Programs.addActionListener(e -> Aus.add(new Options(2, Aus)));

		getshort.addActionListener(e -> {
			Methods.getShortcut();
			Presentation.posi = 0;
			Presentation.p = 0;
			Presentation.firstup();});

		cleari.addActionListener(e -> {
			Presentation.clear();
			Presentation.posi = 0;
			Presentation.p = 0;
			Presentation.firstup();});

		shortcuti.addActionListener(e -> Methods.Short(new String[]{""}));

		Hilfei.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Aus.add(new HelpPanel(Aus));
			}
		});
		
    }
}