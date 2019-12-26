package com.randomapps.randomapp.utils;

import android.content.Context;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ListHandler {

    String assetsListFile = "lists.json";
    String localListContainerFile = "lists.json";

    public ArrayList<String> GetListNamesFromAssetsFile(FragmentActivity activity){
        ArrayList<String> listNames  = new ArrayList<>();

        String fileContent = ReadAssetsFileContent(activity, assetsListFile);

        try {
            JSONObject listObject = new JSONObject(fileContent);
            JSONArray listArray = (JSONArray) listObject.get("lists");
            for (int i = 0; i < listArray.length(); i++) {
                Iterator<String> keys = listArray.getJSONObject(i).keys();
                listNames.add(keys.next());
            }
            return listNames;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return listNames;
    }

    public ArrayList<String> GetListNames(FragmentActivity activity){
        ArrayList<String> listNames  = new ArrayList<>();
        String fileContent = null;
        try {
            fileContent = ReadLocalFileContent(activity);
        } catch (IOException e) {
            e.printStackTrace();
            fileContent = null;
        }
        if(fileContent != null) {
            try {
                JSONObject listObject = new JSONObject(fileContent);
                JSONArray listArray = (JSONArray) listObject.get("lists");
                for (int i = 0; i < listArray.length(); i++) {
                    Iterator<String> keys = listArray.getJSONObject(i).keys();
                    listNames.add(keys.next());
                }
                return listNames;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return listNames;
    }

    public HashMap<String, List<String>> GetListData(FragmentActivity activity){
        HashMap<String, List<String>> listData  = new HashMap<>();
        ArrayList<String> listNames = GetListNames(activity);
        for (String listName: listNames) {
            ArrayList<String> listItems = GetListItems(activity, listName);
            listData.put(listName, listItems);
        }
        return listData;
    }

    public ArrayList<String> GetListItems(FragmentActivity activity, String listName){
        ArrayList<String> listItems  = null;

        String fileContent = null;
        try {
            fileContent = ReadLocalFileContent(activity);
        } catch (IOException e) {
            e.printStackTrace();
            fileContent = null;
        }
        try {
            JSONObject listObject = new JSONObject(fileContent);
            JSONArray listArray = (JSONArray) listObject.get("lists");

            JSONArray listItemsArray = null;
            for (int i = 0; i < listArray.length(); i++) {
                Iterator<String> keys = listArray.getJSONObject(i).keys();
                String listNameInArray = keys.next();
                if(listNameInArray.equals(listName)){
                    listItemsArray = (JSONArray) listArray.getJSONObject(i).get(listName);
                    break;
                }
            }

            if(listItemsArray != null){
                listItems = new ArrayList<>();
                for (int j = 0; j < listItemsArray.length(); j++) {
                    listItems.add(listItemsArray.getString(j));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            listItems = null;
        }
        return listItems;
    }

    private String ReadAssetsFileContent(FragmentActivity activity, String fileName){
        String content = null;

        try {
            InputStream is = activity.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            content = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            Toast.makeText(activity,"Error in loading the local lists", Toast.LENGTH_LONG).show();
            return null;
        }
        return content;
    }

    private String ReadLocalFileContent(FragmentActivity activity) throws IOException {
        String content = null;
        FileInputStream fileIpStream = null;
        BufferedReader buffer = null;
        InputStreamReader streamReader = null;

        try {
            fileIpStream = activity.openFileInput(localListContainerFile);
            streamReader = new InputStreamReader(fileIpStream);
            buffer = new BufferedReader(streamReader);
            StringBuffer stringBuffer = new StringBuffer();

            String line;
            while ((line = buffer.readLine()) != null){
                stringBuffer.append(line + "\n");
            }

            content = stringBuffer.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            content = null;
        } catch (IOException e) {
            e.printStackTrace();
            content = null;
        } finally {
            if(streamReader != null) streamReader.close();
            if(buffer != null) buffer.close();
            if(fileIpStream != null) fileIpStream.close();
        }

        return content;
    }

    public void AddNewList(FragmentActivity activity, String listName, ArrayList<String> listItems) {

        //Get the updated fileContent with new List
        String updatedContent = GetUpdatedFileContentWithNewList(activity, listName, listItems);
        UpdateFileWithContents(activity, localListContainerFile, updatedContent);
    }

    private void UpdateFileWithContents(FragmentActivity activity, String fileName, String updatedContent) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = activity.openFileOutput(fileName, Context.MODE_PRIVATE);
            fileOutputStream.write(updatedContent.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String GetUpdatedFileContentWithNewList(FragmentActivity activity, String listName, ArrayList<String> listItems) {
        String updatedFileContents = null;
        String fileContent = null;
        try {
            fileContent = ReadLocalFileContent(activity);
        } catch (IOException e) {
            e.printStackTrace();
            fileContent = null;
        }
        if(fileContent != null){
            try {
                FileOutputStream fileOutputStream = activity.openFileOutput(localListContainerFile, Context.MODE_PRIVATE);
                JSONObject listObject = new JSONObject(fileContent);
                JSONArray listArray = (JSONArray) listObject.get("lists");

                //new list Array object
                JSONArray newListArray = new JSONArray();
                for (String item: listItems) {
                    newListArray.put(item);
                }
                JSONObject newListObject = new JSONObject();
                newListObject.put(listName, newListArray);
                listArray.put(newListObject);

                updatedFileContents = listObject.toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {

            //1st time file creation
            JSONObject listObject = new JSONObject();
            try {
                listObject.put("lists", new JSONArray());
                JSONArray listArray = listObject.getJSONArray("lists");
                listArray.put(GetJSONListObject(listName, listItems));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            updatedFileContents = listObject.toString();
        }

        return updatedFileContents;
    }

    JSONObject GetJSONListObject(String listName, ArrayList<String> items){
        JSONObject newListObject = new JSONObject();
        JSONArray newListArray = new JSONArray();
        for (String item: items) {
            newListArray.put(item);
        }
        try {
            newListObject.put(listName, newListArray);
        } catch (JSONException e) {
            e.printStackTrace();
            newListObject = null;
        }

        return newListObject;
    }
}
