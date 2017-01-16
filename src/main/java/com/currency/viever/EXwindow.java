package currency.viever;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.*;

public class EXwindow {
	private ExecutorService exec = Executors.newSingleThreadExecutor();
	private String[] bankNames = {
		"NBU", "VTB Bank", "OTP Bank", "Prominvestbank",
		"UniCredit Bank", "PrivatBank", "Oschadbank"
	};
	private String[] currencyNames = {
		"USD", "EUR", "PLN"
	};
	//private Color gradientColor = new Color(42, 0, 101);
	private Exterier exterier = new Exterier();
	private HashMap<String, String> currentLocale = 
		exterier.getCurrentLocale();
	// Stores all buyNsell prices of every bank
	// Key = bank name, Value = array(first item - buy price,
	// second item - sell price):
	private HashMap<String, String[]> allValues = 
		new HashMap<String, String[]>();
	private ReadURL readURLcom = new ReadURL("https://www.rbc.ua/rus/currency/USD");
	private ReadURL readURLoff = new ReadURL("http://minfin.com.ua/currency/nbu/");

	private JFrame window = new JFrame();
	private JMenuBar menuBar = new JMenuBar();
	private JMenu 
		viev = new JMenu("Viev"),
		language = new JMenu("Language");
	private JMenuItem 
		background = new JMenuItem("Background"),
		english = new JMenuItem("English"),
		russian = new JMenuItem("Russian");
	private JButton 
		refresh = new JButton("Refresh"),
		toUAHButton = new JButton("To UAH"),
		toCurrButton = new JButton("Convert");
	private JTextField 
		offCur = new JTextField(),
		vtbBuy = new JTextField(),
		vtbSell = new JTextField(),
		otpBuy = new JTextField(),
		otpSell = new JTextField(),
		promBuy = new JTextField(),
		promSell = new JTextField(),
		uniBuy = new JTextField(),
		uniSell = new JTextField(),
		priBuy = new JTextField(),
		priSell = new JTextField(),
		oscBuy = new JTextField(),
		oscSell = new JTextField(),
		setMoney = new JTextField(8),
		getMoney = new JTextField(8);
	private JComboBox<String> currencyBox = new JComboBox<String>(currencyNames);
	private JComboBox<String> banksBox = new JComboBox<String>(bankNames);
	private JLabel errors = new JLabel();
	private JPanel mainPane = new JPanel(new FlowLayout()) {
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Point point1 = new Point(0, 0);
			Point point2 = new Point(
				getWidth(),
				getHeight());
			final GradientPaint gp = new GradientPaint(
				point1, new Color(255, 255, 255),
				point2, exterier.getGradientColor(),
				true);
			final Graphics2D g2 = (Graphics2D)g;
			g2.setPaint(gp);
			g.fillRect(0, 0, getWidth(), getHeight());
		}
	};
	private JPanel 
		choseCurr = new JPanel(new GridLayout(1, 3)),
		paneOff = new JPanel(new GridLayout(1, 2)),
		paneCom = new JPanel(new GridLayout(6, 5)),
		converter = new JPanel(new FlowLayout());
	private JLabel 
		labelCurr = new JLabel("Chose currency", JLabel.CENTER),
		labelOff = new JLabel("NBU", JLabel.CENTER),
		labelVtb = new JLabel("VTB Bank", JLabel.CENTER),
		labelOtp = new JLabel("OTP Bank", JLabel.CENTER),
		labelProm = new JLabel("Prominvestbank", JLabel.CENTER),
		labelUni = new JLabel("UniCredit Bank", JLabel.CENTER),
		labelPri = new JLabel("PrivatBank", JLabel.CENTER),
		labelOsc = new JLabel("Oschadbank", JLabel.CENTER),
		labelConvType = new JLabel("Type here", JLabel.RIGHT),
		labelConvGet = new JLabel("Get equivalent", JLabel.RIGHT);
	private JLabel[] priceLabels = {
		new JLabel("Buy Price", JLabel.RIGHT),
		new JLabel("Sell Price", JLabel.RIGHT),
		new JLabel("Buy Price", JLabel.RIGHT),
		new JLabel("Sell Price", JLabel.RIGHT),
		new JLabel("Buy Price", JLabel.RIGHT),
		new JLabel("Sell Price", JLabel.RIGHT),
		new JLabel("Buy Price", JLabel.RIGHT),
		new JLabel("Sell Price", JLabel.RIGHT),
		new JLabel("Buy Price", JLabel.RIGHT),
		new JLabel("Sell Price", JLabel.RIGHT),
		new JLabel("Buy Price", JLabel.RIGHT),
		new JLabel("Sell Price", JLabel.RIGHT)
	};
	private TitledBorder
		choseCurrBorder = new TitledBorder("Currency"),
		paneOffBorder = new TitledBorder("Official bank"),
		paneComBorder = new TitledBorder("Commercial banks"),
		converterBorder = new TitledBorder("Converter");
	//
	// Creating ActionListeners for components:
	//
	private ActionListener currencyBoxActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JComboBox cb = (JComboBox)e.getSource();
			if(cb.getSelectedItem() == null) {
				return;
			}
			String item = (String)cb.getSelectedItem();
			if(item.equals("USD")) {
				errors.setText("");
				toCurrButton.setText(currentLocale.get("To USD"));
				readURLoff.setDestUrl("http://minfin.com.ua/currency/nbu/");
				readURLcom.setDestUrl("https://www.rbc.ua/rus/currency/USD");
				exec.execute(new Runnable() {
					@Override
					public void run() {
						loadUSDvalues();
					}
				});
			} 	else if(item.equals("EUR")) {
				errors.setText("");
				toCurrButton.setText(currentLocale.get("To EUR"));
				readURLoff.setDestUrl("http://minfin.com.ua/currency/nbu/");
				readURLcom.setDestUrl("https://www.rbc.ua/rus/currency/EUR");
				exec.execute(new Runnable() {
					@Override
					public void run() {
						loadEURvalues();
					}
				});
			}	else if(item.equals("PLN")) {
				errors.setText("");
				toCurrButton.setText(currentLocale.get("To PLN"));
				readURLoff.setDestUrl("http://minfin.com.ua/currency/nbu/");
				readURLcom.setDestUrl("http://minfin.com.ua/currency/banks/pln/");
				exec.execute(new Runnable() {
					@Override
					public void run() {
						loadPLNvalues();
					}
				});
			}
		}
	};
	private ActionListener backgroundActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			Color backgroundColor = JColorChooser.showDialog(
				null, "Select background color", exterier.getGradientColor());
			if(backgroundColor == null) {
				return;
			}
			exterier.setGradientColor(backgroundColor);
			mainPane.repaint();
			// Saves color state on disk
			saveExterier();
		}
	};
	private ActionListener englishActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			exterier.setEngLocale();
			initLocale();
			saveExterier();
		}
	};
	private ActionListener russianActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			exterier.setRussLocale();
			initLocale();
			saveExterier();
		}
	};
	private ActionListener refreshActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				if(currencyBox.getSelectedItem() == null) {
					errors.setText(currentLocale.get(
						"Please select currency in the top box"));
					return;
				}
				readURLcom.reloadHTML();
				readURLoff.reloadHTML();
				String i = (String)currencyBox.getSelectedItem();
				if(i.equals("USD")) {
					errors.setText("");
					loadUSDvalues();
					display();
				}	else if(i.equals("EUR")) {
					errors.setText("");
					loadEURvalues();
					display();
				}	else if(i.equals("PLN")) {
					errors.setText("");
					loadPLNvalues();
					display();
				}
			}	catch(UnknownHostException uhe) {
				errors.setText(currentLocale.get(
					"Unnable to connect to website, try again later"));
			}	catch(NoRouteToHostException nrthe) {
				errors.setText(currentLocale.get(
					"Unnable to connect to website, try again later"));
			}	catch(Exception ex) {
				errors.setText(currentLocale.get(
					"Unnable to connect to website, try again later"));
			}
		}
	};
	private ActionListener convertActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				float first = Float.parseFloat(setMoney.getText());
				errors.setText("");
				for(int x = 0; x < bankNames.length; x++) {
					// First it takes bank name from "bankNames", than 
					// by this name get array with prices from "allValues"
					// and takes only first element(whitch is buy price) and 
					// converts it in Float
					if(x == banksBox.getSelectedIndex()) {
						getMoney.setText((first * Float.parseFloat(allValues.get(bankNames[x])[0])) + "");
					}
				}
			} 	catch(NumberFormatException nfe) {
				errors.setText(currentLocale.get(
					"Please type amount of money you want to convert"));
			}
		}
	};
	private ActionListener toUAHActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				float first = Float.parseFloat(setMoney.getText());
				errors.setText("");
				for(int x = 0; x < bankNames.length; x++) {
					// First it takes bank name from "bankNames", than 
					// by this name get array with prices from "allValues"
					// and takes only first element(whitch is buy price) and 
					// converts it in Float
					if(x == banksBox.getSelectedIndex()) {
						if(bankNames[x].equals("NBU")) {
							getMoney.setText((first / Float.parseFloat(allValues.get(bankNames[x])[0])) + "");
						}	else {
							getMoney.setText((first / Float.parseFloat(allValues.get(bankNames[x])[1])) + "");
						}
					}
				}
			} 	catch(NumberFormatException nfe) {
				errors.setText(currentLocale.get(
					"Please type amount of money you want to convert"));
			}
		}
	};
	public EXwindow() {
		// Creating folder to hold serializable objects:
		File doc = new File("doc");
		doc.mkdir();
		// Checks for serialized Exterier object
		// if it, then loads it and set to background
		// and current locale
		File checkOut = new File("doc/exterier.out");
		if(checkOut.exists()) {
			exterier = loadExterier();
		}
		initLocale();
		// Setting size and borders for components:
		window.setTitle("Currency Viever");
		mainPane.setPreferredSize(new Dimension(600, 525));
		choseCurr.setBorder(choseCurrBorder);
		choseCurr.setPreferredSize(new Dimension(400, 50));
		paneOff.setBorder(paneOffBorder);
		paneOff.setPreferredSize(new Dimension(400, 50));
		paneCom.setBorder(paneComBorder);
		paneCom.setPreferredSize(new Dimension(599, 300));
		converter.setBorder(converterBorder);
		converter.setPreferredSize(new Dimension(550, 100));
		// Making JTextFields not editable
		// and transperent:
		offCur.setEditable(false);
		offCur.setOpaque(false);
		vtbBuy.setEditable(false);
		vtbBuy.setOpaque(false);
		vtbSell.setEditable(false);
		vtbSell.setOpaque(false);
		otpBuy.setEditable(false);
		otpBuy.setOpaque(false);
		otpSell.setEditable(false);
		otpSell.setOpaque(false);
		promBuy.setEditable(false);
		promBuy.setOpaque(false);
		promSell.setEditable(false);
		promSell.setOpaque(false);
		uniBuy.setEditable(false);
		uniBuy.setOpaque(false);
		uniSell.setEditable(false);
		uniSell.setOpaque(false);
		priBuy.setEditable(false);
		priBuy.setOpaque(false);
		priSell.setEditable(false);
		priSell.setOpaque(false);
		oscBuy.setEditable(false);
		oscBuy.setOpaque(false);
		oscSell.setEditable(false);
		oscSell.setOpaque(false);
		setMoney.setOpaque(false);
		getMoney.setEditable(false);
		getMoney.setOpaque(false);
		errors.setHorizontalAlignment(JLabel.CENTER);
		// Adding Listeners to buttons
		background.addActionListener(backgroundActionListener);
		english.addActionListener(englishActionListener);
		russian.addActionListener(russianActionListener);
		refresh.addActionListener(refreshActionListener);
		currencyBox.addActionListener(currencyBoxActionListener);
		toUAHButton.addActionListener(convertActionListener);
		toCurrButton.addActionListener(toUAHActionListener);
		// Changing Fonts in JLabels
		Font italic14 = new Font("", Font.ITALIC, 14);
		labelCurr.setFont(italic14);
		labelOff.setFont(italic14);
		labelVtb.setFont(italic14);
		labelOtp.setFont(italic14);
		labelProm.setFont(italic14);
		labelUni.setFont(italic14);
		labelPri.setFont(italic14);
		labelOsc.setFont(italic14);
		labelConvType.setFont(italic14);
		labelConvGet.setFont(italic14);
		errors.setFont(new Font("", Font.ITALIC + Font.BOLD, 18));
		errors.setForeground(Color.WHITE);
		// Adding menus to menubar:
		menuBar.add(viev);
		// Adding menuItems to menus:
		viev.add(background);
		viev.add(language);
		language.add(english);
		language.add(russian);
		// Adding Components to panels:
		choseCurr.add(labelCurr);
		// Setting first element to "Chose bank"
		currencyBox.setSelectedIndex(-1);
		choseCurr.add(currencyBox);
		choseCurr.add(refresh);
		// Makes Currency panel transperent:
		choseCurr.setOpaque(false);
		// Official bank panel:
		paneOff.add(labelOff);
		paneOff.add(offCur);
		// Makes Official Bank panel transperent
		paneOff.setOpaque(false);
		// Commercial banks panel:
		paneCom.add(labelVtb);
		paneCom.add(priceLabels[0]);
		paneCom.add(vtbBuy);
		paneCom.add(priceLabels[1]);
		paneCom.add(vtbSell);
		paneCom.add(labelOtp);
		paneCom.add(priceLabels[2]);
		paneCom.add(otpBuy);
		paneCom.add(priceLabels[3]);
		paneCom.add(otpSell);
		paneCom.add(labelProm);
		paneCom.add(priceLabels[4]);
		paneCom.add(promBuy);
		paneCom.add(priceLabels[5]);
		paneCom.add(promSell);
		paneCom.add(labelUni);
		paneCom.add(priceLabels[6]);
		paneCom.add(uniBuy);
		paneCom.add(priceLabels[7]);
		paneCom.add(uniSell);
		paneCom.add(labelPri);
		paneCom.add(priceLabels[8]);
		paneCom.add(priBuy);
		paneCom.add(priceLabels[9]);
		paneCom.add(priSell);
		paneCom.add(labelOsc);
		paneCom.add(priceLabels[10]);
		paneCom.add(oscBuy);
		paneCom.add(priceLabels[11]);
		paneCom.add(oscSell);
		converter.add(labelConvType);
		converter.add(setMoney);
		converter.add(labelConvGet);
		converter.add(getMoney);
		converter.add(banksBox);
		converter.add(toUAHButton);
		converter.add(toCurrButton);
		// Makes Commercial Banks panel and Converter panel transperent:
		paneCom.setOpaque(false);
		converter.setOpaque(false);
		// Main panel with graphics:
		mainPane.add(choseCurr);
		mainPane.add(paneOff);
		mainPane.add(paneCom);
		mainPane.add(converter);
		mainPane.add(errors);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		// When program starts, it opens 
		// in the middle of the screen:
		window.setBounds((screenSize.width - 600) / 2, (screenSize.height - 655) / 2, 
			600, 655);
		window.setJMenuBar(menuBar);
		window.add(BorderLayout.CENTER, mainPane);
		window.setVisible(true);
		// Blocks resizing of window:
		window.setResizable(false);
	}
	// Method to display all USD values:
	private void display() {
		offCur.setText(allValues.get("NBU")[0].trim());
		vtbBuy.setText(allValues.get("VTB Bank")[0]);
		vtbSell.setText(allValues.get("VTB Bank")[1]);
		otpBuy.setText(allValues.get("OTP Bank")[0]);
		otpSell.setText(allValues.get("OTP Bank")[1]);
		promBuy.setText(allValues.get("Prominvestbank")[0]);
		promSell.setText(allValues.get("Prominvestbank")[1]);
		uniBuy.setText(allValues.get("UniCredit Bank")[0]);
		uniSell.setText(allValues.get("UniCredit Bank")[1]);
		priBuy.setText(allValues.get("PrivatBank")[0]);
		priSell.setText(allValues.get("PrivatBank")[1]);
		oscBuy.setText(allValues.get("Oschadbank")[0]);
		oscSell.setText(allValues.get("Oschadbank")[1]);
	}
	private synchronized void loadUSDvalues() {
		allValues.clear();
		try {
			allValues.put("NBU", readURLoff.getCurrentXR(
				"<a href=\"/currency/nbu/usd/\">ДОЛЛАР</a>",
				"<a href=\"/currency/nbu/eur/\">ЕВРО</a>", 
				"<td.*?>\\s*?(.*?)<"));
			allValues.put("VTB Bank", readURLcom.getCurrentXR(
				"<td>ВТБ Банк</td>", "</tr>",
				"<td>ВТБ Банк</td>.*?<td>(.*?)</td>.*?<td>(.*?)</td>"));
			allValues.put("OTP Bank", readURLcom.getCurrentXR(
				"<td>OTP Bank</td>", "</tr>",
				"<td>OTP Bank</td>.*?<td>(.*?)</td>.*?<td>(.*?)</td>"));
			allValues.put("Prominvestbank", readURLcom.getCurrentXR(
				"<td>Проминвестбанк</td>", "</tr>",
				"<td>Проминвестбанк</td>.*?<td>(.*?)</td>.*?<td>(.*?)</td>"));
			allValues.put("UniCredit Bank", readURLcom.getCurrentXR(
				"<td>UniCredit Bank</td>", "</tr>",
				"<td>UniCredit Bank</td>.*?<td>(.*?)</td>.*?<td>(.*?)</td>"));
			allValues.put("PrivatBank", readURLcom.getCurrentXR(
				"<td>Приватбанк</td>", "</tr>",
				"<td>Приватбанк</td>.*?<td>(.*?)</td>.*?<td>(.*?)</td>"));
			allValues.put("Oschadbank", readURLcom.getCurrentXR(
				"<td>Ощадбанк</td>", "</tr>",
				"<td>Ощадбанк</td>.*?<td>(.*?)</td>.*?<td>(.*?)</td>"));
			display();
			readURLcom.releaseHTML();
			readURLoff.releaseHTML();
		}	catch(UnknownHostException uhe) {
				errors.setText("Unnable to connect to website, try again later");
		}	catch(NoRouteToHostException nrthe) {
			errors.setText("Unnable to connect to website, try again later");
		}
	}
	private synchronized void loadEURvalues() {
		allValues.clear();
		try {
			allValues.put("NBU", readURLoff.getCurrentXR(
				"<a href=\"/currency/nbu/eur/", 
				"<a href=\"/currency/nbu/rub/", 
				"<td.*?>\\s*?(.*?)<"));
			allValues.put("VTB Bank", readURLcom.getCurrentXR(
				"<td>ВТБ Банк</td>", "</tr>",
				"<td>ВТБ Банк</td>.*?<td>(.*?)</td>.*?<td>(.*?)</td>"));
			allValues.put("OTP Bank", readURLcom.getCurrentXR(
				"<td>OTP Bank</td>", "</tr>",
				"<td>OTP Bank</td>.*?<td>(.*?)</td>.*?<td>(.*?)</td>"));
			allValues.put("Prominvestbank", readURLcom.getCurrentXR(
				"<td>Проминвестбанк</td>", "</tr>",
				"<td>Проминвестбанк</td>.*?<td>(.*?)</td>.*?<td>(.*?)</td>"));
			allValues.put("UniCredit Bank", readURLcom.getCurrentXR(
				"<td>UniCredit Bank</td>", "</tr>",
				"<td>UniCredit Bank</td>.*?<td>(.*?)</td>.*?<td>(.*?)</td>"));
			allValues.put("PrivatBank", readURLcom.getCurrentXR(
				"<td>Приватбанк</td>", "</tr>",
				"<td>Приватбанк</td>.*?<td>(.*?)</td>.*?<td>(.*?)</td>"));
			allValues.put("Oschadbank", readURLcom.getCurrentXR(
				"<td>Ощадбанк</td>", "</tr>",
				"<td>Ощадбанк</td>.*?<td>(.*?)</td>.*?<td>(.*?)</td>"));
			display();
			readURLcom.releaseHTML();
			readURLoff.releaseHTML();
		}	catch(UnknownHostException uhe) {
				errors.setText("Unnable to connect to website, try again later");
		}	catch(NoRouteToHostException nrthe) {
			errors.setText("Unnable to connect to website, try again later");
		}
	}
	private synchronized void loadPLNvalues() {
		allValues.clear();
		try {
			allValues.put("NBU", readURLoff.getCurrentXR(
				"<a href=\"/currency/nbu/pln/",
				"<a href=\"/currency/nbu/chf/",
				"<td.*?>\\s*?(.*?)<"));
			allValues.put("VTB Bank", readURLcom.getCurrentXR(
				"</span>ВТБ Банк</a>", "</tr>",
				"</span>ВТБ Банк</a>.*?>.*?>(.*?)</td>.*?</td>.*?>(.*?)</td>"));
			allValues.put("OTP Bank", new String[] { "---", "---" });
			allValues.put("Prominvestbank", new String[] { "---", "---" });
			allValues.put("UniCredit Bank", readURLcom.getCurrentXR(
				"</span>UniCredit Bank</a>", "</tr>",
				"</span>UniCredit Bank</a>.*?>.*?>(.*?)</td>.*?</td>.*?>(.*?)</td>"));
			allValues.put("PrivatBank", readURLcom.getCurrentXR(
				"</span>ПриватБанк</a>", "</tr>",
				"</span>ПриватБанк</a>.*?>.*?>(.*?)</td>.*?</td>.*?>(.*?)</td>"));
			allValues.put("Oschadbank", new String[] { "---", "---" });
			display();
			readURLcom.releaseHTML();
			readURLoff.releaseHTML();
		}	catch(UnknownHostException uhe) {
				errors.setText("Unnable to connect to website, try again later");
		}	catch(NoRouteToHostException nrthe) {
			errors.setText("Unnable to connect to website, try again later");
		}
	}
	//
	// Refresh all text in program
	// in choosen language 
	//
	@SuppressWarnings("unchecked")
	private void initLocale() {
		currentLocale = exterier.getCurrentLocale();
		// Init menubar:
		viev.setText(currentLocale.get("Viev"));
		language.setText(currentLocale.get("Language"));
		english.setText(currentLocale.get("English"));
		russian.setText(currentLocale.get("Russian"));
		background.setText(currentLocale.get("Background"));
		// Init borders for panels:
		choseCurrBorder.setTitle(currentLocale.get("Currency"));
		paneOffBorder.setTitle(currentLocale.get("Official bank"));
		paneComBorder.setTitle(currentLocale.get("Commercial banks"));
		converterBorder.setTitle(currentLocale.get("Converter"));
		// Init buttons:
		refresh.setText(currentLocale.get("Refresh"));
		toUAHButton.setText(currentLocale.get("To UAH"));
		toCurrButton.setText(currentLocale.get("Convert"));
		// Init labels:
		labelCurr.setText(currentLocale.get("Choose currency"));
		labelOff.setText(currentLocale.get("NBU"));
		labelVtb.setText(currentLocale.get("VTB Bank"));
		labelOtp.setText(currentLocale.get("OTP Bank"));
		labelProm.setText(currentLocale.get("Prominvestbank"));
		labelUni.setText(currentLocale.get("UniCredit Bank"));
		labelPri.setText(currentLocale.get("PrivatBank"));
		labelOsc.setText(currentLocale.get("Oschadbank"));
		labelConvType.setText(currentLocale.get("Type here"));
		labelConvGet.setText(currentLocale.get("Get equivalent"));
		for(int x = 0; x < priceLabels.length; x++) {
			if(x % 2 == 0) {
				priceLabels[x].setText(currentLocale.get("Buy Price"));
			}	else {
				priceLabels[x].setText(currentLocale.get("Sell Price"));
			}
		}
		// Init comboboxes:
		for(int x = 0; x < bankNames.length; x++) {
			banksBox.addItem(currentLocale.get(bankNames[x]));
			banksBox.removeItemAt(0);
		}
		currencyBox.setRenderer(new PromptComboBoxRenderer(
			currentLocale.get("Choose")));
		banksBox.setRenderer(new PromptComboBoxRenderer(
			currentLocale.get("Choose bank")));
		// Setting first element to "Chose bank"
		banksBox.setSelectedIndex(-1);
	}
	// Loads Exterier object from input stream
	// and returns it
	private Exterier loadExterier() {
		try {
			ObjectInputStream ois = new ObjectInputStream(
				new FileInputStream("doc/exterier.out"));
			try {
				return (Exterier)ois.readObject();
			}	finally {
				ois.close();
			}
		}	catch(ClassNotFoundException e) {
			throw new RuntimeException(e);
		}	catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	//
	// Save Exterier object on hard-drive
	//
	private void saveExterier() {
		try {
				ObjectOutputStream ois = new ObjectOutputStream(
					new FileOutputStream("doc/exterier.out"));
				try {
					ois.writeObject(exterier);
				} finally {
					ois.close();
				}
			}	catch(IOException ioe) {
				throw new RuntimeException(ioe);
			}
	}
	// Create class to set first element in JComboBox
	// can't be choosen, it also dissapears when one of other
	// elements will be chosen 
	class PromptComboBoxRenderer extends BasicComboBoxRenderer {
		private String prompt;
		public PromptComboBoxRenderer(String prompt) {
			this.prompt = prompt;
		}
		@Override
		public Component getListCellRendererComponent(JList list, Object value, 
			int index, boolean isSelected, boolean hasFocus) {
			super.getListCellRendererComponent(
				list, value, index, isSelected, hasFocus);
			if(value == null) {
				setText(prompt);
			}
			return this;
		}
	}
}
