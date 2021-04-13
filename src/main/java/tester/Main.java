package tester;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws URISyntaxException, IOException {

        URL url = Main.class.getResource("/myo"); // Ищется в resources target
        File file = new File(url.toURI());

        for (String str : Files.readAllLines(Paths.get(file.getPath()))) { // return readAllLines(path, StandardCharsets.UTF_8);

            System.out.println("String: ");
            System.out.println(str);

            System.out.println("Bytes: ");

            byte[] strBytes = str.getBytes();

            int i = 0;
            for (byte b : strBytes) {
                i = printByte(strBytes, i, b);
            }

            System.out.println();

            System.out.println("String from bytes: ");

            String restoration = new String(strBytes, StandardCharsets.UTF_8);

            System.out.println(restoration);

            System.out.println("---------------------");
        }


        FileInputStream fis = new FileInputStream(file.getPath());

        System.out.println("Characters printed:");

        byte b;

        while((b=(byte)fis.read())!=-1){
            System.out.print(b);


            if(b == (byte)'\n'){
                System.out.println();
            }else
                System.out.print(", ");
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
