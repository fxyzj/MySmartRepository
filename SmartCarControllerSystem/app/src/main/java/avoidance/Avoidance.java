package avoidance;

import java.util.logging.Handler;

import smartmessage.SmartMessage;
import smartmessage.SmartMessageUtils;

/**
 * Created by fuxiaoyong on 16/11/8.
 *
 * 这个类用于实现小车的避障
 * 传入12个声呐数据
 * 最后通过函数给出控制指令
 *
 *
 */

public class Avoidance {




    private int distance = 30;//用于设置小车离障碍物的距离

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public byte[] getBuffer1() {
        return buffer1;
    }

    public void setBuffer1(byte[] buffer1) {
        this.buffer1 = buffer1;
    }

    private byte[] buffer1;

    public Avoidance(byte[] buffer1) {
        this.buffer1 = buffer1;
    }


    public  byte[] cmd(byte[] bufferIn){

        int buffer[] = SmartMessageUtils.byte2Int(bufferIn);

        if(buffer == null)
            return SmartMessage.CMD_STOP;

        byte [] cmd = SmartMessage.CMD_FORWARD;

        int flag = 0; //

        int data = 0; //用于标记哪些声呐位置测量的距离小于distance


        for(int i=0;i<7;i++){
            if(buffer[i]< distance){

                data |= 0x01<<i;

            }
        }

        if((data & 0x1C)!=0){
            flag = 1;
        }

        if(flag == 1){
            switch (data & 0x63) {
                case 0x01:
                case 0x02:
                case 0x03:
                    cmd =  SmartMessage.CMD_RIGHT;//其实返回转向的话需要又一个旋转时间，让这个动作完成
                    break;
                case 0x20:
                case 0x40:
                case 0x60:
                    cmd= SmartMessage.CMD_LEFT;//其实返回转向的话需要又一个旋转时间，让这个动作完成
                    break;
                default:

                    if (buffer[0] <= buffer[6]) {
                       cmd = SmartMessage.CMD_RIGHT;
                    } else if (buffer[0] > buffer[6]) {
                        cmd = SmartMessage.CMD_LEFT;
                    }//其实返回转向的话需要又一个旋转时间，让这个动作完成


                    break;
            }

        } else
        {
            cmd = SmartMessage.CMD_FORWARD;
        }

        return cmd;

    }




}
