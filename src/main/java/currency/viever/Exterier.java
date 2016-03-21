package currency.viever;
import java.io.*;
import java.awt.*;
import java.nio.*;
import java.nio.charset.*;
import java.util.*;

class Exterier implements Serializable {
	private Color gradientColor = new Color(42, 0, 101);
	// Holds text in current locale
	// Achive by general keys:
	private HashMap<String, String> locale = 
		new HashMap<String, String>();
	// English language sets bt default
	Exterier() {
		setEngLocale();
	}
	void setGradientColor(Color grdnt) {
		gradientColor = grdnt;
	}
	Color getGradientColor() {
		return gradientColor;
	}
	// Return map with current locale text:
	HashMap<String, String> getCurrentLocale() {
		return locale;
	}
	// Set default language:
	void setEngLocale() {
		locale = new HashMap<String, String>() {{
			put("Viev", "Viev");
			put("Background", "Background");
			put("Language", "Language");
			put("English", "English");
			put("Russian", "Russian");
			put("Refresh", "Refresh");
			put("To UAH", "To UAH");
			put("Convert", "Convert");
			put("To USD", "To USD");
			put("To EUR", "To EUR");
			put("To PLN", "To PLN");
			put("Choose currency", "Chose currency");
			put("Official", "Official");
			put("NBU", "NBU");
			put("VTB Bank", "VTB Bank");
			put("OTP Bank", "OTP Bank");
			put("Prominvestbank", "Prominvestbank");
			put("UniCredit Bank", "UniCredit Bank");
			put("PrivatBank", "PrivatBank");
			put("Oschadbank", "Oschadbank");
			put("Type here", "Type here");
			put("Get equivalent", "Get equivalent");
			put("Please select currency in the top box",
				"Please select currency in the top box");
			put("Unnable to connect to website, try again later",
				"Unnable to connect to website, try again later");
			put("Please type amount of money you want to convert",
				"Please type amount of money you want to convert");
			put("Currency", "Currency");
			put("Official bank", "Official bank");
			put("Commercial banks", "Commercial banks");
			put("Converter", "Converter");
			put("Choose", "Choose");
			put("Buy Price", "Buy Price");
			put("Sell Price", "Sell Price");
			put("Choose bank", "Choose bank");
		}};
	}
	// Set Russian language:
	void setRussLocale() {
		try {
			locale = new HashMap<String, String>() {{
				put("Viev", decodeString("Вид"));
				put("Background", decodeString("Фон"));
				put("Language", decodeString("Язык"));
				put("English", decodeString("Английский"));
				put("Russian", decodeString("Русский"));
				put("Refresh", decodeString("Обновить"));
				put("To UAH", decodeString("В UAH"));
				put("Convert", decodeString("Перевести"));
				put("To USD", decodeString("В USD"));
				put("To EUR", decodeString("В EUR"));
				put("To PLN", decodeString("В PLN"));
				put("Choose currency", decodeString("Выберите валюту"));
				put("Official", decodeString("Официальный"));
				put("NBU", decodeString("НБУ"));
				put("VTB Bank", decodeString("ВТБ Банк"));
				put("OTP Bank", decodeString("ОТП Банк"));
				put("Prominvestbank", decodeString("Проминвестбанк"));
				put("UniCredit Bank", decodeString("ЮниКредит Банк"));
				put("PrivatBank", decodeString("ПриватБанк"));
				put("Oschadbank", decodeString("Ощадбанк"));
				put("Type here", decodeString("Введите количество"));
				put("Get equivalent", decodeString("Эквивалент"));
				put("Please select currency in the top box",
					decodeString("Пожалуйста виберете валюту в меню сверху"));
				put("Unnable to connect to website, try again later",
					decodeString("Невозможно подключится к сайту, попробуйте позже"));
				put("Please type amount of money you want to convert",
					decodeString("Введите количество денег которые нужно перевести"));
				put("Currency", decodeString("Валюта"));
				put("Official bank", decodeString("Официальный банк"));
				put("Commercial banks", decodeString("Коммерческие банки"));
				put("Converter", decodeString("Конвертер"));
				put("Choose", decodeString("Выберите"));
				put("Buy Price", decodeString("Цена покупки"));
				put("Sell Price", decodeString("Цена прожажи"));
				put("Choose bank", decodeString("Выберите банк"));
			}};
		}	catch(CharacterCodingException e) {
			// If will be problems with decoding, return
			// standart US locale
			setEngLocale();
		}
	}
	// Decode input string in UTF-8:
	private String decodeString(String input) 
	throws CharacterCodingException {
		Charset charsetE = Charset.forName(Charset.defaultCharset().toString());
		CharsetEncoder encoder = charsetE.newEncoder();
		Charset charsetD = Charset.forName("UTF-8");
		CharsetDecoder decoder = charsetD.newDecoder();
		ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(input));
		CharBuffer cbuf = decoder.decode(bbuf);
		return cbuf.toString();
	}
}