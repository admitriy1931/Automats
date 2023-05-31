package algorithms;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.*;

import java.io.FileOutputStream;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import javafx.scene.text.Text;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
import java.util.HashSet;
import java.util.List;

public class StringSubstringGenerator {
    private StringSubstringGenerator() {
    }

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final int MIN_LENGTH = 4;
    private static final int MAX_LENGTH = 5;
    private static final double ver1 = 0.7; //вероятность того, что генератор выдаст строку из одного слова
    private static final double ver2 = 0.8; //вероятность того, что генератор выдаст подстроку из строки
    static Map<String, String> stringAndSubstring = new HashMap<>();

    public static void func1() {
        for (int i = 0; i < 30; i++) {
            Random random = new Random();
            double randomNumber = random.nextDouble();
            double randomNumber2 = random.nextDouble();
            if (randomNumber <= ver1) {
                String w = String.valueOf(generateWorld(MIN_LENGTH, MAX_LENGTH));
                int n1 = random.nextInt(w.length() - 3) + 1; // первый индекс
                int diff = random.nextInt(2) + 2; // разница между индексами (2 или 3)
                int n2 = n1 + diff;

                if (randomNumber2 <= ver2){
                    String substr = w.substring(n1,n2);
                    stringAndSubstring.put(w,substr);
                }
                else{
                    String noSubstr = String.valueOf(generateSubstring());
                    while(true){
                        if (!w.contains(noSubstr)){
                            noSubstr = String.valueOf(generateSubstring());
                            break;
                        }
                    }
                    stringAndSubstring.put(w, noSubstr);
                }
            } else {
                String[] w2 = new String[2];
                w2[0] = generateWorld(MIN_LENGTH, MAX_LENGTH).toString();
                w2[1] = generateWorld(MIN_LENGTH, MAX_LENGTH).toString();

                int n1 = random.nextInt(MIN_LENGTH - 3) + 1; // первый индекс
                int diff = random.nextInt(2) + 2; // разница между индексами (2 или 3)
                int n2 = n1 + diff;

                if (randomNumber2 <= ver2){
                    int rnd = random.nextInt(2);
                    if (rnd == 0){
                        String substr = w2[0].substring(n1,n2);
                        stringAndSubstring.put(w2[0] + " " + w2[1] ,substr);
                    }
                    else{
                        String substr = w2[1].substring(n1,n2);
                        stringAndSubstring.put(w2[0] + " " + w2[1] ,substr);
                    }

                }
                else{
                    String noSubstr = String.valueOf(generateSubstring());
                    while(true){
                        if (!w2[0].contains(noSubstr) && !w2[1].contains(noSubstr)){
                            noSubstr = String.valueOf(generateWorld(n1,n2));
                            break;
                        }
                    }
                    stringAndSubstring.put(w2[0] + " " + w2[1], noSubstr);
                }

            }

        }
    }

    public static void printStringndSubstring() throws DocumentException {
        func1();
//        Document document = new Document();
//
//        try {
//            PdfWriter.getInstance(document, new FileOutputStream("src/main/resources/output.pdf"));
//            document.open();
//            int i = 0;
//            for (Map.Entry<String, String> entry : stringAndSubstring.entrySet()) {
//                String str = entry.getKey();
//                String substring = entry.getValue();
//                List<String> output = algorithms.KnuthMorrisPratt.KMP(substring, str);
//                Font font1 = new Font(Font.FontFamily.HELVETICA, 14);
//                document.add(new Paragraph("Option " + i, font1));
//                Text textSubstring = new Text(substring).setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD));
//
//// Создание объекта Text для строки str
//                Text textStr = new Text(str).setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD));
//
//// Создание объекта Paragraph с объединенными объектами Text
//                Paragraph paragraph = new Paragraph()
//                        .add("Is a ")
//                        .add(textSubstring)
//                        .add(" substring of a string ")
//                        .add(textStr)
//                        .add("?");
//
//                i+=1;
//                System.out.println(i);
//            }
//            document.close();
//            System.out.println("файл создан");
//
//        } catch (DocumentException | FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        for (Map.Entry<String, String> entry : stringAndSubstring.entrySet()) {
            String str = entry.getKey();
            String substring = entry.getValue();

            System.out.println("String: " + entry.getKey() + " " + "Substring: " + entry.getValue());
//            System.out.println(output);
//            for (String e: output) {
//                System.out.println(e);
//
//            }

        }
    }

    public static StringBuilder generateWorld(int minLen, int maxLen){
        SecureRandom random = new SecureRandom();
        int length = minLen + random.nextInt(maxLen - minLen + 1);
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < length; j++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb;
    }
    public static String generateSubstring() {
        Random random = new Random();
        int length = random.nextInt(2) + 2; // генерируем случайную длину в диапазоне от 2 до 3
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char c = CHARACTERS.charAt(random.nextInt(CHARACTERS.length()));
            sb.append(c);
        }
        return sb.toString();
    }
}
