package tester;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws URISyntaxException, IOException {

        URL url = Main.class.getResource("/myo"); // Ищется в resources target
        File file = new File(url.toURI());

        /*Ожидаемые  результаты: восстановленная с учетом кодировки строка из потока байт соответствует исходной
        (2 раза xor == исходная строка, читаемая при восстановлении из потока байт);
         getBytes в корректной кодировке и побайтовое чтение дают одинаковый результат
         (sic. Здесь в getBytes не будет байт, отвечающих за перевод строки, т.к. readAllLines их "съедает")*/

        //url.toPath выдает path-часть, начиная с leading slash, для Unix он мержится с root folder, для переносимости лучше использовать getPath
        for (String str : Files.readAllLines(Paths.get(file.getPath()))) { // return readAllLines(path, StandardCharsets.UTF_8);

            //Вывод строки файла в кодировке UTF-8 в виде строки
            System.out.println("String UTF-8: ");
            System.out.println(str);

            //Вывод строки файла в кодировке UTF-8 в виде потока байт
            System.out.println("Bytes UTF-8 from string: ");

            byte[] strBytes = str.getBytes();

            int i = 0;
            for (byte b : strBytes) {
                i = printByte(strBytes, i, b);
            }

            System.out.println();

            //Вывод строки файла в кодировке UTF-8, восстановленной из потока байт (2 раза применённая операция xor вернет
            //значения оригинальных байт)
            System.out.println("String from bytes (round): ");

            String restoration = new String(strBytes, StandardCharsets.UTF_8);

            System.out.println(restoration);

            System.out.println("---------------------");
        }

        System.out.println(url.getPath());

        FileInputStream fis = new FileInputStream(file.getPath());

        //Чтение UTF-8 файла целиком в виде потока байт, кодировка не учитывается.
        //Байты будут совпадать с прочитанным ранее (за исключением отсутствия символов перевода строки)

        System.out.println("Characters printed:");

        byte b;

        while ((b = (byte) fis.read()) != -1) {
            System.out.print(b);


            if (b == (byte) '\n') {
                System.out.println();
            } else
                System.out.print(", ");
        }

        System.out.println();
        System.out.println("---------------------");


        // Создаём файл с текстом в UTF-16 == первой строке входного файла
        // Ожидаемый результат: побайтовое чтение тех же символов даст другой результат, потому что другая кодировка

        try (OutputStream outputStream = new FileOutputStream("output.txt");
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream,
                     StandardCharsets.UTF_16))) {
            String str = Files.readAllLines(Paths.get(file.getPath())).get(0);
            writer.write(str);
            writer.newLine();
        }

        try (BufferedReader bufferedReader = new BufferedReader
                (new InputStreamReader(new FileInputStream("output.txt"), StandardCharsets.UTF_16))) {


            String utf16str = bufferedReader.readLine();
            System.out.println("String UTF-16: ");
            System.out.println(utf16str);

            //Вывод строки файла в кодировке UTF-16 в виде потока байт
            System.out.println("Bytes UTF-16: ");

            byte[] strBytes = utf16str.getBytes(StandardCharsets.UTF_16);

            int i = 0;
            for (byte b1 : strBytes) {
                i = printByte(strBytes, i, b1);
            }

        }

    }

    private static int printByte(byte[] strBytes, int i, byte b) {
        System.out.print(b);
        if (i != strBytes.length - 1)
            System.out.print(", ");
        i++;
        return i;
    }
}
