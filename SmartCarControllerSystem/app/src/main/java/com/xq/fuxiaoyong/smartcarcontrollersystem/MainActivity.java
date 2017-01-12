package com.xq.fuxiaoyong.smartcarcontrollersystem;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hbe.bluetooth.HBEBT;
import com.hbe.bluetooth.HBEBTListener;
import com.iflytek.cloud.InitListener;

import java.util.ArrayList;
import java.util.List;

import FileOperation.FileService;
import baowei.cross.Voice2WordResultTask;
import baowei.cross.Word2VoiceCrossFactory;
import baowei.voice2word.DataResultDealWith;
import baowei.voice2word.MyInitListener;
import baowei.voice2word.Voice2Word;
import smartcarData.Coordinate;
import smartcarData.RecordRolls;
import smartcarData.SmartCarState;
import smartcarData.SmartCarUtils;
import smartcarData.TurnsOfWheel;
import smartcarData.Ultrasonic;
import smartmessage.SmartMessage;

public class MainActivity extends AppCompatActivity implements HBEBTListener,DataResultDealWith {


    /**
     *记录转角是状态信息
     */
    List<SmartCarState> carStateList = new ArrayList<SmartCarState>();

    /**
     * 临时记录小车转弯前的状态，因为有多个转弯的地方所以一个list数组存储好了
     */
    List<SmartCarState> beforeTurnStateList = new ArrayList<SmartCarState>();
    /**
     *
     */
   SmartCarState tempCarState = new SmartCarState();

    /**
     * 用于标志是否达到终点信息
     */
    private boolean disFlag = true;

    private boolean midleFlag = true;
    /**
     * 和语音处理有关，负责将声音转化为文字
     */
    // 初始化默认监听器
    InitListener initListener = new MyInitListener(this);
    // 构造Voice2World
    Voice2Word voice2Word = new Voice2Word(this, this, initListener);
    /**
     * <pre>
     * 语音识别的结果，表示小车要执行的任务
     * 在onReceive里面，遍历这个任务队列，去完成小车行驶的命令
     * </pre>
     */
    List<Voice2WordResultTask> voice2WordResultTasksList = new ArrayList<Voice2WordResultTask>();
    // 用于在交叉路口，发出声音
    Word2VoiceCrossFactory voiceCrossFactory = new Word2VoiceCrossFactory(this);
    /**
     * 记录小车传过来的数据
     */
    public byte[] mBuff = new byte[100];
    private int mBuffLen = 0;
    /**
     * 用于保存，与障碍物的声呐距离
     */
    public byte m7Sensors[] = new byte[7];

    /**
     * 各种控件的定义
     */
    // 声呐数据的显示
    private TextView sensorTxt;
    // 蓝牙连接状态的显示
    public TextView blueTextView;

    /**
     * 蓝牙连接
     */
    private HBEBT mBluetooth;
    /**
     * <pre>
     * 控制小车旋转角度的实现
     * 单例模式，在onCreate()函数里面，完成赋值
     * </pre>
     */
    public RecordRolls recordRolls;
    public RecordRolls recordRolls1=new RecordRolls();


    /**
     * <pre>
     * 用于记录小车车轮旋转的圈数
     * 在onReceive()函数里面，完成赋值
     * </pre>
     */
    TurnsOfWheel turnsOfWheel = new TurnsOfWheel(0,0);
    /**
     * <pre>
     * 用于判断是否要转弯，默认是不需要旋转的，只有到达交叉路口，才开启旋转
     * 判断转弯是否完成  ，刚开始没有旋转那么就是完成啊
     * </pre>
     */
    public boolean isRoll = true;

    public boolean secondFlag = true;

    /**
     * 用于处理耗时的操作
     */
    public Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    // 用于实现小车与蓝牙的连接
                    mBluetooth.conntect();
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    // 不包含左右，就重新调用，语音识别的程序
                    voice2Word.voice2Word();
                    break;

                default:
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 基本的界面显示
        setContentView(R.layout.activity_main);
        // 蓝牙的连接状态
        blueTextView = (TextView) findViewById(R.id.buleConnect);
        // 声呐数据的显示
        sensorTxt = (TextView) findViewById(R.id.sensorTxt);

        // 创建蓝牙连接对象
        mBluetooth = new HBEBT(this);
        mBluetooth.setListener(this);
        // 小车旋转角度的实现
        recordRolls = new RecordRolls();

    }

    @Override
    public void onConnected() {
        blueTextView.setText("Connected");
    }

    @Override
    public void onDisconnected() {
        blueTextView.setText("DisConnected");
    }

    @Override
    public void onConnecting() {
        blueTextView.setText("Connecting");
    }

    @Override
    public void onConnectionFailed() {
        blueTextView.setText("onConnectionFailed");
    }

    @Override
    public void onConnectionLost() {
        blueTextView.setText("onConnectionLost");
    }

    @Override
    public void onReceive(byte[] buffer) {

        for (int n = 0; n < buffer.length; n++) {
            byte buff = buffer[n];

            // 第一个数据包数据不是0X76的话，校验，直接退出
            if (mBuffLen == 0 && buff != 0x76) {
                return;
            } else if (mBuffLen == 1 && buff != 0x00) {
                mBuffLen = 0;
                return;
            } else if (mBuffLen > 1 && buff == 0x00
                    && mBuff[mBuffLen - 1] == 0x76) {
                mBuffLen = 2;
                mBuff[0] = 0x76;
                mBuff[1] = 0x00;
            } else {
                mBuff[mBuffLen++] = buff;
                if (mBuffLen == 22 && mBuff[2] == 0x33) {
                    /**
                     * 这里包含陀螺仪加速度传感器 其中 以及轮子旋转编码器
                     * sendSensor[16]、[17]、[18]、[19]为小车编码器数值 具体获取看setEncoder
                     */
                    byte[] sendSensor = new byte[mBuffLen];

                    // 22个数据
                    for (int i = 0; i < mBuffLen; i++) {
                        sendSensor[i] = mBuff[i];
                    }

                    // 获取左右轮转的圈数
                    // 只是对于sendSensor[16]、[17]、[18]、[19]，做处理
                    // 去获取，小车编码器的数值（小车的轮子转的圈数）
                    turnsOfWheel = setEncoder(sendSensor);

                    // 获取右轮子旋转的距离
                    double distanceR = SmartCarUtils
                            .distanceFromWheel(turnsOfWheel.getRightTurns());
                    // 获取左轮子旋转的距离
                    double distanceL = SmartCarUtils
                            .distanceFromWheel(turnsOfWheel.getLeftTurns());
                    // 获取偏离的角度
                    double degeree = SmartCarUtils
                            .currentAngleRelativeToStartpoint(turnsOfWheel,
                                    25.1);



                    // 用于执行语音识别的结果
                    for (int i = 0; i < voice2WordResultTasksList.size(); i++) {
                        Voice2WordResultTask resultTask = voice2WordResultTasksList
                                .get(i);
                        // 如果这个命令没有执行，就执行
                        // 如果，已经执行。就什么都不做
                        if (resultTask.isFlag() == false) {

                            // 向左或者是向右旋转
                            // 因为为了保证RecordRolls类，在某一个时刻只有一个旋转在进行
                            // 根据recordRolls.isFlag()，来判断是否可以进行旋转
                            // 第一次，进入recordRolls.isFlag()为true，开启旋转，
                            // 并将recordRolls的旋转标志设置为false
                            // 这样第二次遍历语音的任务队列的时候，就不会进入下面的if了
                            if (recordRolls.isFlag()) { // 如果上一次转角结束，那么才可以开启新的转动
                                dataSend(SmartMessage.CMD_STOP);

                                // 将RecordRolls类的旋转标志设置为false，保证一个时刻只有一个旋转命令
                                recordRolls.startNewRoate();
                                // 设置开始旋转时候的轮子的圈数
                                recordRolls.setStartTurnsOfWheel(turnsOfWheel);
                                // dataSend(SmartMessage.CMD_LEFT);
                                // 发送识别的语音命令(向左或者是向右旋转)
                                dataSend(resultTask.word2Command());
                            }
                            // 每一次，进行循环。
                            // 都要将当前的小车旋转的轮子的圈数，记录下来。以判断是否到达要旋转的角度
                            recordRolls.setCurrentTurnsOfWheel(turnsOfWheel);
                            if (recordRolls.finishRoll(90)) {
                                dataSend(SmartMessage.CMD_STOP);


                                /**
                                 * 这里获得上一次停止没有转角前的状态信息
                                 *
                                 */
                                // 将当前状态存储到数据文件中

                                SmartCarState lastState = beforeTurnStateList.get(beforeTurnStateList.size()-1);
                                tempCarState = new SmartCarState(lastState.getCarCoord()
                                        ,lastState.getCar_current_angle()+90,turnsOfWheel);
                                //初始坐标
                                carStateList.add(tempCarState);


                                // 转弯结束后，则将此值设为true
                                isRoll = false;


                                // 转弯过后则往前走
                                dataSend(SmartMessage.CMD_FORWARD);
                                // 修改标准，标注任务已经执行
                                voice2WordResultTasksList.get(i).setFlag(true);
                            }
                        }

                    }
                    /**
                     * 角度控制代码,转弯处 当目前坐标点的距离和转弯处的距离，相差为10cm的时候，让小车停止
                     */
                    if (isRoll == true && distanceL>600) {// 如果允许转弯那么开启转弯
                        // 防止下次回调函数，再次调用
                        isRoll = false;
                        // 先停止小车
                        dataSend(SmartMessage.CMD_STOP);

                        tempCarState = SmartCarUtils.currentState(
                                new SmartCarState(new Coordinate(0,0),90,new TurnsOfWheel(0,0)), turnsOfWheel);
                        //初始坐标
                        beforeTurnStateList.add(tempCarState);


                        // 语音提示，到达交叉路口，应该怎么走
                        // Word2Voice word2Voice = new Word2Voice("58059c69",
                        // this);
                        // word2Voice.startSpeaking("到达交叉路口，请问应该怎么走?");
                        voiceCrossFactory.arriveCross();
                        try {

                            Thread.sleep(9000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // 调用语音识别
                        voice2Word.voice2Word();

                    }

                    /**
                     * 中间点
                     */
                    if (midleFlag && (distanceL >= 850)) {

                        if(carStateList.size()>0) {
                            SmartCarState smartCarState2 = SmartCarUtils.currentState(
                                    carStateList.get(carStateList.size() - 1), turnsOfWheel);

                            // 将当前状态存储到数据文件中


                            //初始坐标
                            carStateList.add(smartCarState2);

                            //转角前信息记录
                            beforeTurnStateList.add(smartCarState2);
                        }

                        for(SmartCarState s :carStateList) {
                            FileService.saveFileToSdCard("current.txt",s.toString().getBytes());
                        }



                        if(recordRolls1.isFlag()) {
                            // 发送停止指令
                            dataSend(SmartMessage.CMD_STOP);
                            // 将RecordRolls类的旋转标志设置为false，保证一个时刻只有一个旋转命令
                            recordRolls1.startNewRoate();
                            // 设置开始旋转时候的轮子的圈数
                            recordRolls1.setStartTurnsOfWheel(turnsOfWheel);
                            dataSend(SmartMessage.CMD_LEFT);

                        }

                        // 都要将当前的小车旋转的轮子的圈数，记录下来。以判断是否到达要旋转的角度

                        recordRolls1.setCurrentTurnsOfWheel(turnsOfWheel);
                        if (recordRolls1.finishRoll(120)) {
                            dataSend(SmartMessage.CMD_STOP);


                            /**
                             * 这里获得上一次停止没有转角前的状态信息
                             *
                             */
                            // 将当前状态存储到数据文件中

                            SmartCarState lastState = beforeTurnStateList.get(beforeTurnStateList.size()-1);
                            tempCarState = new SmartCarState(lastState.getCarCoord()
                                    ,lastState.getCar_current_angle()+90,turnsOfWheel);
                            //初始坐标
                            carStateList.add(tempCarState);

                            midleFlag = false;


                            // 转弯过后则往前走
                            dataSend(SmartMessage.CMD_FORWARD);
                            // 修改标准，标注任务已经执行

                        }
                    }

                    /**
                     * 判断是否到达终点
                     */
                    if(disFlag==true&&distanceL > 950){
                        dataSend(SmartMessage.CMD_STOP);

                        SmartCarState smartCarState3 = SmartCarUtils.currentState(
                                carStateList.get(carStateList.size()-1), turnsOfWheel);
                        // 将当前状态存储到数据文件中


                        //初始坐标
                        carStateList.add(smartCarState3);

                        //转角前信息记录
                        beforeTurnStateList.add(smartCarState3);


                        for(SmartCarState s :carStateList) {
                            FileService.saveFileToSdCard("current.txt",s.toString().getBytes());
                        }

                        disFlag=false;
                        // 提示到达终点信息
                        voiceCrossFactory.arriveDistination();

                    }

                    mBuffLen = 0;

                } else if (mBuffLen == 17 && mBuff[2] == 0x3C) {

                    // 这里获取的是12个超声波数据

                    byte[] sendUltra = new byte[mBuffLen];

                    for (int i = 0; i < mBuffLen; i++) {
                        sendUltra[i] = mBuff[i];
                    }

                    // 第五个元素开始才是，需要的超声波数据
                    for (int i = 0; i < 7; i++) {
                        m7Sensors[i] = mBuff[i + 4];
                    }

                    // dataSend(Avoidance.cmd(m7Sensors));
                    sensorTxt.setText(String.format("%d", m7Sensors[1] & 0xFF));
                    mBuffLen = 0;
                }
            }

        }

    }


    /**
     * 每个拐点执行动作的函数的封装
     */

    public static void rollAtGivenPoint(double distance,TurnsOfWheel turnsOfWheel){
        RecordRolls recordRolls1 = new RecordRolls();
    }








    /**
     * 用于获取小车，与障碍物的距离。也就是超声波的数据
     *
     * @param buff
     * @return
     */
    public Ultrasonic setUltrsonic(byte[] buff) {

        Ultrasonic ultrasonic = null;

        int[] ultr = new int[12];
        for (int i = 4; i <= 15; i++) {
            ultr[i] = (buff[i] & 0xFF);
        }

        ultrasonic = new Ultrasonic(ultr);
        return ultrasonic;
    }

    /**
     * 用于获取左右轮子转的圈数
     *
     * @param buff
     * @return
     */
    public TurnsOfWheel setEncoder(byte[] buff) {

        TurnsOfWheel turnsOfWheel = new TurnsOfWheel();

        int encoder_l = (int) ((buff[17] & 0xFF));
        encoder_l |= (int) ((buff[16] & 0xFF) << 8);
        int encoder_r = (int) ((buff[19] & 0xFF));
        encoder_r |= (int) ((buff[18] & 0xFF) << 8);

        turnsOfWheel.setRightTurns(encoder_r);
        turnsOfWheel.setLeftTurns(encoder_l);

        return turnsOfWheel;

    }

    /**
     * 蓝牙连接
     *
     * @param V
     */
    public void bluetoothConnectClick(View V) {
        Message message = new Message();
        message.what = 0;
        mHandler.sendMessage(message);
    }

    /**
     * 左转
     *
     * @param V
     */
    public void leftClick(View V) {
        dataSend(SmartMessage.CMD_LEFT);
    }

    /**
     * 右转
     *
     * @param V
     */
    public void rightClick(View V) {
        dataSend(SmartMessage.CMD_RIGHT);
    }

    /**
     * 停止
     *
     * @param V
     */
    public void stopClick(View V) {
        dataSend(SmartMessage.CMD_STOP);

    }

    /**
     * 向前
     *
     * @param V
     */
    public void forwardClick(View V) {
        dataSend(SmartMessage.CMD_FORWARD);
    }

    // 向后
    public void backwardClick(View V) {
        dataSend(SmartMessage.CMD_BACKWARD);
    }

    /**
     * 与小车通信，用于接收传感器的数据
     *
     * @param v
     */
    public void dataConnect(View v) {
        sendSensor();
    }

    /**
     * 发送传感器接受指令，用于开启传感器数据的传送
     */

    private void sendSensor() {
        byte[] cmd = SmartMessage.CMD_SENSOR.clone();
        cmd[4] = 0x01;
        cmd[5] = (byte) 0xFF;
        cmd[6] = getCheckSum(cmd);
        mBluetooth.sendData(cmd);
    }

    /**
     * 发送各种命令的工具函数
     *
     * @param cmd
     *            控制命令
     */
    public void dataSend(byte[] cmd) {

        if (cmd != null) {
            byte speed = 2;
            cmd[5] = speed;
            cmd[6] = getCheckSum(cmd);
            mBluetooth.sendData(cmd);
        }
    }

    /**
     * 校验
     *
     * @param buff
     * @return
     */
    public byte getCheckSum(byte[] buff) {
        int ret = (buff[2] & 0xFF) + (buff[4] & 0xFF) + (buff[5] & 0xFF);
        return (byte) ret;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 发送命令线程
     */

    // 语音的按钮的点击函数

    public void yuyinClick(View view) {
        // 构造World2Voice
        // Word2Voice word2Voice = new Word2Voice("58059c69", this);
        // 设置发音人(可以不设置，有默认的)
        // word2Voice.setVoiceName("aisjiuxu");
        // 使用工具方法
        // word2Voice.startSpeaking("已到达终点位置,导航结束");

        voice2Word.voice2Word();

    }

    /**
     * 语音识别的结果
     */
    @Override
    public void dataResultDealWith(String dataResult) {
        // 记录下来语音识别的结果
        // Toast.makeText(this, dataResult, Toast.LENGTH_SHORT).show();
        // 对识别语音结果，做出命令的解析
        // 处理语音识别的结果,并给小车发出相应的指令
        if (dataResult.contains("左")) {
            // 将语音识别的结果，封装为Voice2WordResultTask
            // 并放入voice2WordResultTaskList中
            Voice2WordResultTask resultTask = new Voice2WordResultTask(
                    dataResult);
            voice2WordResultTasksList.add(resultTask);
        } else if (dataResult.contains("右")) {
            // 将语音识别的结果，封装为Voice2WordResultTask
            // 并放入voice2WordResultTaskList中
            Voice2WordResultTask resultTask = new Voice2WordResultTask(
                    dataResult);
            voice2WordResultTasksList.add(resultTask);
        } else {
            // 没有包含向左或者向右的信息，再次询问
            // 发出声音,询问下一步的去向
            // Word2Voice word2Voice = new Word2Voice("58059c69", this);
            // word2Voice.startSpeaking("您好，请告诉我是向左转，还是向右转，谢谢?");
            voiceCrossFactory.leftOrRight();

            // 放在下一次监听到一句话的信息，应该让线程在这里睡眠10秒
            // 然后再启动语音监听模式
            try {
                Thread.sleep(7000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 再次开启语音模式，接受信息
            // 交给Handle处理
            Message hMessage = new Message();
            hMessage.what = 4;
            mHandler.sendMessage(hMessage);
        }

    }
}
