package com.hbe.smartcarcontroller;

import com.hbe.bluetooth.HBEBT;
import com.hbe.bluetooth.HBEBTListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;

@SuppressLint("HandlerLeak")
public class SmartCARControllerActivity extends Activity implements HBEBTListener {
	
	SmartCARControllerView mView;
	HBEBT mBluetooth;
	int mSpeed;
	
	byte[] mBuff = new byte[100];
	int mBuffLen = 0;
	
	boolean mThreadRun;
	int mLastCMD = -1;
	int mLastSendCMD = -1;
	
	boolean mSensor, mUltra;

	public Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			super.handleMessage(msg);

			switch(msg.what){
			case SmartMessage.TOUCH:
				mBluetooth.conntect();
				break;
			case SmartMessage.DIRECT:
				mLastCMD = msg.arg1;
				break;
			case SmartMessage.SPEED:
				mSpeed = msg.arg1;
				break;
			case SmartMessage.SENSOR_ON:
				mSensor = true;
				sendSensor();
				break;
			case SmartMessage.SENSOR_OFF:
				mSensor = false;
				sendSensor();
				break;
			case SmartMessage.ULTRA_ON:
				mUltra = true;
				sendSensor();
				break;
			case SmartMessage.ULTRA_OFF:
				mUltra = false;
				sendSensor();
				break;
			}
		}
	};
	
	private void sendSensor(){
		byte[] cmd = SmartMessage.CMD_SENSOR.clone();
		if(mUltra) cmd[4] = 0x01;
		if(mSensor) cmd[5] = (byte)0xFF;
		cmd[6] = getCheckSum(cmd);
		mBluetooth.sendData(cmd);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mView = new SmartCARControllerView(this, mHandler);
		setContentView(mView);
		
		mBluetooth = new HBEBT(this);
		mBluetooth.setListener(this);
		
		mSensor = false;
		mUltra = false;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		mThreadRun = false;
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		mThreadRun = true;
		new ControlThread().start();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mBluetooth.disconnect();
		super.onDestroy();
	}

	@Override
	public void onConnected() {
		// TODO Auto-generated method stub
		mView.setConnected();
	}

	@Override
	public void onConnecting() {
		// TODO Auto-generated method stub
		mView.setConnecting();
	}

	@Override
	public void onConnectionFailed() {
		// TODO Auto-generated method stub
		mBluetooth.disconnect();
	}

	@Override
	public void onConnectionLost() {
		// TODO Auto-generated method stub
		mBluetooth.disconnect();
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		mView.setConnect();
	}
	
	public void setSensor(byte[] buff){

		short acc_x, acc_y, acc_z;
		short gyro_x, gyro_y, gyro_z;
		int encoder_l, encoder_r;
		
		acc_x = (short)((buff[5]&0xFF));
		acc_x |= (short)((buff[4]&0xFF) << 8);
		acc_y = (short)((buff[7]&0xFF));
		acc_y |= (short)((buff[6]&0xFF) << 8);
		acc_z = (short)((buff[9]&0xFF));
		acc_z |= (short)((buff[8]&0xFF) << 8);
		mView.setAccel(acc_x, acc_y, acc_z);
		
		gyro_x = (short)((buff[11]&0xFF));
		gyro_x |= (short)((buff[10]&0xFF) << 8);
		gyro_y = (short)((buff[13]&0xFF));
		gyro_y |= (short)((buff[12]&0xFF) << 8);
		gyro_z = (short)((buff[15]&0xFF));
		gyro_z |= (short)((buff[14]&0xFF) << 8);
		mView.setGyro(gyro_x, gyro_y, gyro_z);
		
		encoder_l = (int)((buff[17]&0xFF));
		encoder_l |= (int)((buff[16]&0xFF) << 8);
		encoder_r = (int)((buff[19]&0xFF));
		encoder_r |= (int)((buff[18]&0xFF) << 8);
		mView.setEncoder(encoder_l, encoder_r);
		
		mView.setInfrared(buff[20]);
	}
	
	public void setUltra(byte[] buff){

		mView.setUltra(buff[4]&0xFF, buff[5]&0xFF, buff[6]&0xFF, buff[7]&0xFF, buff[8]&0xFF, 
				buff[9]&0xFF, buff[10]&0xFF, buff[11]&0xFF, buff[12]&0xFF, buff[13]&0xFF, 
				buff[14]&0xFF, buff[15]&0xFF);
	}

	@Override
	public void onReceive(byte[] buffer) {

		for(int n=0;n<buffer.length;n++){
			byte buff = buffer[n];
			
			if(mBuffLen==0 && buff!=0x76){
				return;
			} else if(mBuffLen==1 && buff!=0x00){
				mBuffLen = 0;
				return;
			} else if(mBuffLen>1 && buff==0x00 && mBuff[mBuffLen-1]==0x76){
				mBuffLen = 2;
				mBuff[0] = 0x76;
				mBuff[1] = 0x00;
			} else {
				mBuff[mBuffLen++] = buff;
				
				if(mBuffLen==22 && mBuff[2]==0x33){
					byte[] send = new byte[mBuffLen];
					
					for(int i=0;i<mBuffLen;i++){
						send[i] = mBuff[i];
					}
					setSensor(send);
					mBuffLen = 0;
				} else if(mBuffLen==17 && mBuff[2]==0x3C){
					byte[] send = new byte[mBuffLen];
					
					for(int i=0;i<mBuffLen;i++){
						send[i] = mBuff[i];
					}
					setUltra(send);
					mBuffLen = 0;
				}
			}
		}
	}
	
	public void sendCarPacket(int array){
		byte[] cmd;
		
		switch(array){
		case SmartMessage.TOP_LEFT:
			cmd = SmartMessage.CMD_FORWARD_LEFT.clone();
			cmd[5] = (byte)mSpeed; 
			cmd[6] = getCheckSum(cmd);
			mBluetooth.sendData(cmd);
			break;
		case SmartMessage.TOP_CENTER:
			cmd = SmartMessage.CMD_FORWARD.clone();
			cmd[5] = (byte)mSpeed;
			cmd[6] = getCheckSum(cmd);
			mBluetooth.sendData(cmd);
			break;
		case SmartMessage.TOP_RIGHT:
			cmd = SmartMessage.CMD_FORWARD_RIGHT.clone();
			cmd[5] = (byte)mSpeed; 
			cmd[6] = getCheckSum(cmd);
			mBluetooth.sendData(cmd);
			break;
		case SmartMessage.LEFT:
			cmd = SmartMessage.CMD_LEFT.clone();
			cmd[5] = (byte)mSpeed; 
			cmd[6] = getCheckSum(cmd);
			mBluetooth.sendData(cmd);
			break;
		case SmartMessage.RIGHT:
			cmd = SmartMessage.CMD_RIGHT.clone();
			cmd[5] = (byte)mSpeed; 
			cmd[6] = getCheckSum(cmd);
			mBluetooth.sendData(cmd);
			break;
		case SmartMessage.BOTTOM_LEFT:
			cmd = SmartMessage.CMD_BACKWARD_LEFT.clone();
			cmd[5] = (byte)mSpeed; 
			cmd[6] = getCheckSum(cmd);
			mBluetooth.sendData(cmd);
			break;
		case SmartMessage.BOTTOM_CENTER:
			cmd = SmartMessage.CMD_BACKWARD.clone();
			cmd[5] = (byte)mSpeed; 
			cmd[6] = getCheckSum(cmd);
			mBluetooth.sendData(cmd);
			break;
		case SmartMessage.BOTTOM_RIGHT:
			cmd = SmartMessage.CMD_BACKWARD_RIGHT.clone();
			cmd[5] = (byte)mSpeed; 
			cmd[6] = getCheckSum(cmd);
			mBluetooth.sendData(cmd);
			break;
		case SmartMessage.CENTER:
			cmd = SmartMessage.CMD_STOP.clone();
			cmd[5] = 0; 
			cmd[6] = getCheckSum(cmd);
			mBluetooth.sendData(cmd);
			break;
		}
	}
	
	public byte getCheckSum(byte[] buff){
		int ret = (buff[2]&0xFF)+(buff[4]&0xFF)+(buff[5]&0xFF);

		return (byte)ret;
	}
	
	class ControlThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(mThreadRun){
				try {
					if(mLastCMD != mLastSendCMD){
						mLastSendCMD = mLastCMD;
						sendCarPacket(mLastSendCMD);
					}
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			super.run();
		}
		
	}

}
