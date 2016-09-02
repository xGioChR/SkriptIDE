package com.skriptide.codemanage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * Created by Liz3ga on 27.08.2016.
 */
public class CodeReader {

    File mainF;


    public CodeReader(File f) {

        mainF = f;
    }

    public String getCode() {

        String code = "";
        try {
            InputStream stream = new FileInputStream(mainF);

            InputStreamReader input = new InputStreamReader(stream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(input);

            try {

                String line = "";

                do {
                    line = reader.readLine();
                    if(line != null) {

                       code = code + line + System.getProperty("line.separator");
                    }
                }
                while (line != null);

                reader.close();
                input.close();
                stream.close();
                return code;
            } catch (IOException e) {
                e.printStackTrace();
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }


}
