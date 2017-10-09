package savedata;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by domin on 6 Sep 2017.
 */
public class DataManager {
    private Class saveClass;
    private JSONObject jsonObject;
    private String fileName = "res/savedata/~.json";

    public DataManager(Class saveClass) {
        this.saveClass = saveClass;
        fileName = getFileName();

        List<Field> fieldList = new ArrayList<Field>();
        fieldList.addAll(Arrays.asList(saveClass.getDeclaredFields()));

        jsonObject = new JSONObject(getJSONString(fileName));
        setClass(jsonObject, fieldList);

    }

    private String getFileName(){
        String initialDir = "res/savedata/";
        String fileName = saveClass.getSimpleName();
        String packageDir = saveClass.getName().replace("." + fileName, "/");
        return initialDir + packageDir + fileName + ".json";
    }

    private String getJSONString(String fileName){
        String jsonData = "";
        BufferedReader br = null;
        File file = new File(fileName);
        StringBuilder builder = new StringBuilder(128);
        boolean fileIsEmpty = false;
        try {
            String line;
            br = new BufferedReader(new FileReader(fileName));

            boolean isFinished = (line = br.readLine()) == null;
            fileIsEmpty = isFinished;

            while (!isFinished) {
                builder.append(line).append("\n");
                isFinished = (line = br.readLine()) == null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        if (file.exists() && !fileIsEmpty) {
            jsonData = builder.toString();
        } else if (!file.exists()){
            try {
                jsonData = "{}";
                file.getParentFile().mkdir();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            jsonData = "{}";
        }
        return jsonData;
    }

    private void setClass(JSONObject obj, List<Field> fields){

        for (Field field:fields){
            String type = field.getType().getName();
            if (Modifier.isPrivate(field.getModifiers()))
                continue;

            try {
                if (obj.opt(field.getName()) != null)
                    field.set(null, obj.opt(field.getName()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (JSONException e){
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveClass(){
        List<Field> fieldList = new ArrayList<Field>();
        fieldList.addAll(Arrays.asList(saveClass.getDeclaredFields()));

        for (Field field : fieldList) {
            if (Modifier.isPrivate(field.getModifiers()))
                continue;

            try {
                jsonObject.put(field.getName(), field.get(null));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        writeJSON();
    }

    private void writeJSON(){
        String jsonSource = jsonObject.toString();

        BufferedWriter bw = null;
        FileWriter fw = null;

        try {

            fw = new FileWriter(fileName);
            bw = new BufferedWriter(fw);
            bw.write(jsonSource);

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }
    }
}
