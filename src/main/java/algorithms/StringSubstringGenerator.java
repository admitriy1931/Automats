package algorithms;
import java.io.IOException;
import java.util.*;

import java.io.FileOutputStream;
import java.util.List;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;


public class StringSubstringGenerator {
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final int MIN_LENGTH = 4;
    private static final int MAX_LENGTH = 5;
    private static double ver2; //вероятность того, что генератор выдаст подстроку из строки
    private static int wordCount; //колличество слов в генерируемом предложении
    private static int optionCount; //колличество вариантов
    static Map<String, String> stringAndSubstring = new HashMap<>();
    public StringSubstringGenerator(int wordCount, int optionCount, double ver) {
        this.wordCount = wordCount;
        this.optionCount = optionCount;
        this.ver2 = ver;
    }

    public void func1() {
        for (int i = 0; i < optionCount; i++) {
            Random random = new Random();
            double randomNumber2 = random.nextDouble();
            String w = generateWords();
            String[] ww = w.split(" ");
            if (randomNumber2 <= ver2){
                int randomW = random.nextInt(ww.length);
                String wSub = ww[randomW];
                Random r = new Random();
                int startIndex = r.nextInt(wSub.length() - 2); // начальный индекс
                int endIndex = startIndex + r.nextInt(2) + 2; // конечный индекс (2 или 3 символа)
                String subStr = wSub.substring(startIndex, endIndex);
                stringAndSubstring.put(w, subStr);

            }
            else{
                String a = generateSubstring();
                while (!IsStrContainsSubstring(ww, a)){
                    a= generateWords();
                }
                stringAndSubstring.put(w, a);
            }
        }
    }
    public static Boolean IsStrContainsSubstring(String[] str, String substr) {
        for (String e : str) {
            if (e.contains(substr)) {
                return false;
            }
        }
        return true;
    }
    public void printStringndSubstring2() throws IOException {
        int i = 0;
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("output2.pdf"));
        document.open();
        for (Map.Entry<String, String> entry : stringAndSubstring.entrySet()) {

            List<String> output = algorithms.KnuthMorrisPratt.KMP(entry.getValue(), entry.getKey());

            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            Font mainFont = new Font(Font.HELVETICA, 14);
            Chunk optionChunk = new Chunk("Option " + i + "\n", mainFont);
            Chunk isAChunk = new Chunk("Is a ", FontFactory.getFont(FontFactory.HELVETICA));
            Chunk valueChunk = new Chunk(entry.getValue(), boldFont);
            Chunk substringChunk = new Chunk(" substring of a string ", FontFactory.getFont(FontFactory.HELVETICA));
            Chunk keyChunk = new Chunk(entry.getKey(), boldFont);
            Chunk questionMarkChunk = new Chunk(" ?\n" + "\n",FontFactory.getFont(FontFactory.HELVETICA));
            Paragraph paragraph = new Paragraph();
            paragraph.add(optionChunk);
            paragraph.add(isAChunk);
            paragraph.add(valueChunk);
            paragraph.add(substringChunk);
            paragraph.add(keyChunk);
            paragraph.add(questionMarkChunk);
            document.add(paragraph);
///////////////////////////////
            System.out.println("str: "+ entry.getKey() + "sub: " + entry.getValue());
            System.out.println(output);

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            //PdfPCell cell = new PdfPCell(new Paragraph("Text"));
            //table.addCell(cell);

            for (int j = 0; j < 5; j++) {
                table.addCell("Str " + j + ", Col 1");
                table.addCell("Str " + j + ", Col 2");
                table.addCell("Str " + j + ", Col 3");
            }

            document.add(table);

            i++;
        }
        document.close();


    }

    public void printStringndSubstring() throws IOException {
        int i = 0;
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("output.pdf"));
        document.open();
        for (Map.Entry<String, String> entry : stringAndSubstring.entrySet()) {
            //String f = "Option " + i + " \n" + "Is a "  + entry.getValue() + " substring of a string " + entry.getKey() + " ?";
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            Font mainFont = new Font(Font.HELVETICA, 14);
            Chunk optionChunk = new Chunk("Option " + i + "\n", mainFont);
            Chunk isAChunk = new Chunk("Is a ", FontFactory.getFont(FontFactory.HELVETICA));
            Chunk valueChunk = new Chunk(entry.getValue(), boldFont);
            Chunk substringChunk = new Chunk(" substring of a string ", FontFactory.getFont(FontFactory.HELVETICA));
            Chunk keyChunk = new Chunk(entry.getKey(), boldFont);
            Chunk questionMarkChunk = new Chunk(" ?\n" + "\n",FontFactory.getFont(FontFactory.HELVETICA));
            Paragraph paragraph = new Paragraph();
            paragraph.add(optionChunk);
            paragraph.add(isAChunk);
            paragraph.add(valueChunk);
            paragraph.add(substringChunk);
            paragraph.add(keyChunk);
            paragraph.add(questionMarkChunk);
            document.add(paragraph);
            i++;

        }
        document.close();
    }

    public static String generateWords(){
        int n = wordCount; // Количество слов в предложении
        Random rand = new Random();
        StringBuilder wordBuilder = new StringBuilder();
        for (int i = 0; i < n; i++) {
            // Генерируем слово случайной длины от 4 до 5 символов
            int len = rand.nextInt((MAX_LENGTH - MIN_LENGTH) + 1) + MIN_LENGTH;
            for (int j = 0; j < len; j++) {
                char ch = CHARACTERS.charAt(rand.nextInt(CHARACTERS.length())); // Выбираем случайный символ из допустимых

                wordBuilder.append(ch);
            }
            if (i != n-1){
                wordBuilder.append(" ");
            }
            else{
                continue;
            }
        }
        return wordBuilder.toString();
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
