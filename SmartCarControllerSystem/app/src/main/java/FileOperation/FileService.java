package FileOperation;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by fuxiaoyong on 16/10/25.
 */

public class FileService {



    public static boolean saveFileToSdCard(String fileName , byte []data){
        boolean flag = false;

        //先判断sdcard的状态
        String state = Environment.getExternalStorageState();

        //表示ascard卡挂载在手机上，并且可以读写
        FileOutputStream fileOutputStream = null;
        //获得sdsdard的根目录  、/mmt.sdcard
        File root  = Environment.getExternalStorageDirectory();


        if(state.equals(Environment.MEDIA_MOUNTED)){
            File file = new File(root.getAbsolutePath()+"/SmartCarStorage");

            if(!file.exists()){
                file.mkdir();
            }


            try {
                fileOutputStream = new FileOutputStream(new File(file,fileName),true);
                fileOutputStream.write(data,0,data.length);
                fileOutputStream.flush();
                flag = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(fileOutputStream !=null ){
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return flag;
    }





    public static String readContextFromScCard(String fileName){
        //输出字符串
        String outString = null;

         FileInputStream inputStream = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        File root = Environment.getExternalStorageDirectory();
        if(Environment.getExternalStorageState().
                equals(Environment.MEDIA_MOUNTED)){

            File file = new File(root.getAbsolutePath()+"/txt/");

            File file2 = new File(file,fileName);

            int len = 0;
            byte[] data = new byte[1024];

            if(file2.exists()){
                try {
                    inputStream = new FileInputStream(file2);

                    while((len = inputStream.read(data)) != -1 ){
                        outputStream.write(data,0,len);
                    }
                    return outString =  new String(outputStream.toByteArray());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(inputStream != null){
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }


        }

        return null;
    }


    public static ArrayList<String> readCmdFromScCard(String fileName){

        String encoding = "utf-8";

        InputStreamReader inputStreamReader;
        ArrayList<String> cmd_List = null;
        int i = 0;


        File root = Environment.getExternalStorageDirectory();
        if(Environment.getExternalStorageState().
                equals(Environment.MEDIA_MOUNTED)){

            File file = new File(root.getAbsolutePath()+"/txt/");

            File file2 = new File(file,fileName);
            if(file2.isFile()&&file2.exists()){//判断文件是否存在

                try {
                    inputStreamReader = new InputStreamReader(
                            new FileInputStream(file2),encoding);

                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    //创建存储对象
                    cmd_List = new ArrayList<String>();

                    String lineTxt = null;
                    while((lineTxt = bufferedReader.readLine())!= null){
                        cmd_List.add(i,lineTxt);
                        i++;
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return cmd_List;
    }



}
