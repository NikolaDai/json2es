package com.w3dai.json2es;

import java.io.IOException;

public class Json2esActionTest {

    public static void main(String[] args) throws IOException, InterruptedException
    {
        CreateIndexFromData createIndexFromData = new CreateIndexFromData();
        createIndexFromData.CreateIndex();
        Json2esAction json2esAction = new Json2esAction();
        json2esAction.readJasonAndWriteToES("./dataSet02.txt");
    }
}
