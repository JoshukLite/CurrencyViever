package currency.viever;
import java.util.*;
import java.net.*;

class ValuesLoader {
	private ReadURL readURLcom;
	private ReadURL readURLoff;
	private HashMap<String, String[]> currencyValues = 
		new HashMap<String, String[]>();
	ValuesLoader(ReadURL o, ReadURL c) {
		readURLoff = o;
		readURLcom = c;
	}
	HashMap<String, String[]> loadUSDvalues() throws 
	UnknownHostException, NoRouteToHostException {
		currencyValues.clear();
		currencyValues.put("NBU", readURLoff.getCurrentXR(
			"<a href=\"/currency/nbu/usd/\">ДОЛЛАР</a>",
			"<a href=\"/currency/nbu/eur/\">ЕВРО</a>", 
			"<td.*?>\\s*?(.*?)<"));
		currencyValues.put("VTB Bank", readURLcom.getCurrentXR(
			"<td>ВТБ Банк</td>", "</tr>",
			"<td>ВТБ Банк</td>.*?</td>.*?<td>(.*?)</td>.*?<td>(.*?)</td>"));
		currencyValues.put("OTP Bank", readURLcom.getCurrentXR(
			"<td>OTP Bank</td>", "</tr>",
			"<td>OTP Bank</td>.*?</td>.*?<td>(.*?)</td>.*?<td>(.*?)</td>"));
		currencyValues.put("Prominvestbank", readURLcom.getCurrentXR(
			"<td>Проминвестбанк</td>", "</tr>",
			"<td>Проминвестбанк</td>.*?</td>.*?<td>(.*?)</td>.*?<td>(.*?)</td>"));
		currencyValues.put("UniCredit Bank", readURLcom.getCurrentXR(
			"<td>UniCredit Bank</td>", "</tr>",
			"<td>UniCredit Bank</td>.*?</td>.*?<td>(.*?)</td>.*?<td>(.*?)</td>"));
		currencyValues.put("PrivatBank", readURLcom.getCurrentXR(
			"<td>Приватбанк</td>", "</tr>",
			"<td>Приватбанк</td>.*?</td>.*?<td>(.*?)</td>.*?<td>(.*?)</td>"));
		currencyValues.put("Oschadbank", readURLcom.getCurrentXR(
			"<td>Ощадбанк</td>", "</tr>",
			"<td>Ощадбанк</td>.*?</td>.*?<td>(.*?)</td>.*?<td>(.*?)</td>"));
		readURLcom.releaseHTML();
		readURLoff.releaseHTML();
		return currencyValues;
	}
	HashMap<String, String[]> loadEURvalues() throws 
	UnknownHostException, NoRouteToHostException {
		currencyValues.clear();
		currencyValues.put("NBU", readURLoff.getCurrentXR(
			"<a href=\"/currency/nbu/eur/", 
			"<a href=\"/currency/nbu/rub/", 
			"<td.*?>\\s*?(.*?)<"));
		currencyValues.put("VTB Bank", readURLcom.getCurrentXR(
			"<td>ВТБ Банк</td>", "</tr>",
			"<td>ВТБ Банк</td>.*?</td>.*?<td>(.*?)</td>.*?<td>(.*?)</td>"));
		currencyValues.put("OTP Bank", readURLcom.getCurrentXR(
			"<td>OTP Bank</td>", "</tr>",
			"<td>OTP Bank</td>.*?</td>.*?<td>(.*?)</td>.*?<td>(.*?)</td>"));
		currencyValues.put("Prominvestbank", readURLcom.getCurrentXR(
			"<td>Проминвестбанк</td>", "</tr>",
			"<td>Проминвестбанк</td>.*?</td>.*?<td>(.*?)</td>.*?<td>(.*?)</td>"));
		currencyValues.put("UniCredit Bank", readURLcom.getCurrentXR(
			"<td>UniCredit Bank</td>", "</tr>",
			"<td>UniCredit Bank</td>.*?</td>.*?<td>(.*?)</td>.*?<td>(.*?)</td>"));
		currencyValues.put("PrivatBank", readURLcom.getCurrentXR(
			"<td>Приватбанк</td>", "</tr>",
			"<td>Приватбанк</td>.*?</td>.*?<td>(.*?)</td>.*?<td>(.*?)</td>"));
		currencyValues.put("Oschadbank", readURLcom.getCurrentXR(
			"<td>Ощадбанк</td>", "</tr>",
			"<td>Ощадбанк</td>.*?</td>.*?<td>(.*?)</td>.*?<td>(.*?)</td>"));
		readURLcom.releaseHTML();
		readURLoff.releaseHTML();
		return currencyValues;
	}
	HashMap<String, String[]> loadPLNvalues() throws 
	UnknownHostException, NoRouteToHostException {
		currencyValues.clear();
		currencyValues.put("NBU", readURLoff.getCurrentXR(
			"<a href=\"/currency/nbu/pln/",
			"<a href=\"/currency/nbu/chf/",
			"<td.*?>\\s*?(.*?)<"));
		currencyValues.put("VTB Bank", readURLcom.getCurrentXR(
			"</span>ВТБ Банк</a>", "</tr>",
			"</span>ВТБ Банк</a>.*?>.*?>(.*?)</td>.*?</td>.*?>(.*?)</td>"));
		currencyValues.put("OTP Bank", new String[] { "---", "---" });
		currencyValues.put("Prominvestbank", new String[] { "---", "---" });
		currencyValues.put("UniCredit Bank", readURLcom.getCurrentXR(
			"</span>UniCredit Bank</a>", "</tr>",
			"</span>UniCredit Bank</a>.*?>.*?>(.*?)</td>.*?</td>.*?>(.*?)</td>"));
		currencyValues.put("PrivatBank", readURLcom.getCurrentXR(
			"</span>ПриватБанк</a>", "</tr>",
			"</span>ПриватБанк</a>.*?>.*?>(.*?)</td>.*?</td>.*?>(.*?)</td>"));
		currencyValues.put("Oschadbank", new String[] { "---", "---" });
		readURLcom.releaseHTML();
		readURLoff.releaseHTML();
		return currencyValues;
	}
}