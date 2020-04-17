package ru.performanceLab;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


public class Main {

    public static void main(String[] args) {

            while (true) {
                System.out.println("Введите команду");
                System.out.println("Для вывода справки введите \"--help\" или \"-h\"");

                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    String input = reader.readLine();

                    if (input.equals("--help") || input.equals("-h")) {
                        System.out.println("Для вывода актуального курса доллара введите USD");
                        System.out.println("Для вывода актуального курса евро введите EUR");
                        System.out.println("Для вывода актуального курса фунта введите GBP");
                        System.out.println("Для выхода из программы введите: \"-e\" или \"--exit\"");
                        System.out.println("-----------------------------");
                    } else if (input.equals("-e") || input.equals("--exit")) {
                        reader.close();
                        System.exit(0);
                    } else if (input.equals("USD") || input.equals("EUR") || input.equals("GBP")) {
                        URL url = new URL("http://www.cbr.ru/scripts/XML_daily.asp");
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.connect();
                        InputStream bis = new BufferedInputStream(conn.getInputStream());
                        printCurrency(bis, input);
                        conn.disconnect();
                    } else {
                        System.out.println("Вы ввели неверную команду");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }

    public static void printCurrency(InputStream inputStream, String currency) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newDefaultInstance();
            DocumentBuilder dBuild = dbFactory.newDocumentBuilder();
            Document doc = dBuild.parse(inputStream);
            NodeList nodeList = doc.getElementsByTagName("Valute");
            int currencyCode;
            if (currency.equals("USD")) {
                currencyCode = 10;
            } else if (currency.equals("EUR")) {
                currencyCode = 11;
            } else {
                currencyCode = 2;
            }
            Node node = nodeList.item(currencyCode);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                System.out.println("Курс валют для " +
                        element.getElementsByTagName("CharCode").item(0).getTextContent() + ": " +
                        element.getElementsByTagName("Value").item(0).getTextContent() +
                        " рублей");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


