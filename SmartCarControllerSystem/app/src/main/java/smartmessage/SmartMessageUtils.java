package smartmessage;


/**
 * Created by fuxiaoyong on 16/10/26.
 */

public class SmartMessageUtils {

    /**
     * 把字符串信息转化为控制命令
     * @param str 输入的标示信息
     * @return 控制命令
     */
    public static byte[] cmd_Message(String str){

        byte[] cmd  = null;

        if(str.contains("p")){
            cmd = SmartMessage.CMD_STOP;
        }else if(str.contains("Forw")){
            cmd = SmartMessage.CMD_FORWARD;
        }else if(str.contains("LEFT")){

            //这个指令要顶替下U旋转
            cmd = SmartMessage.CMD_LEFT;
        }else if(str.contains("RIGH")){
            cmd = SmartMessage.CMD_RIGHT;
        }

       return cmd;
    }

    public static void mySleep(String str) throws InterruptedException {

        if(str.contains("p")){
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else if(str.contains("Forw")){

            Thread.sleep(1000);
        }else if(str.contains("LEFT")){

            //这个指令要顶替下U旋转
            Thread.sleep(200);
        }else if(str.contains("RIGH")){
            Thread.sleep(200);
        }
    }


    public static int [] byte2Int(byte buff[]){
        int re[] = new int[buff.length];
        for(int i = 0;i<buff.length;i++){
            re[i] = buff[i]&0xFF;
        }
        return re;
    }

}
