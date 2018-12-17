package com.w3dai.json2es;

import java.io.IOException;

public class Json2esActionTest {

    public static void main(String[] args) throws IOException, InterruptedException
    {
        Json2esAction json2esActio = new Json2esAction();
        json2esActio.readJasonAndWriteToES("./dataSet02.txt");
    }
}
