package com.moma.socket.handle;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

import com.arcsoft.*;
import com.arcsoft.utils.BufferInfo;
import com.moma.dboper.InsertFRRecord;
import com.moma.pool.MMDBThreadPoolUtils;
import com.moma.socket.exception.DetecetionException;
import com.moma.socket.exception.ExtractFRFeatureException;
import com.moma.utils.*;
import com.moma.FFMR.MMFR;
import com.moma.socket.exception.PackageException;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.moma.socket.send.ComputerToTerminalThread;
import javax.imageio.ImageIO;

public class Compute  extends Thread{
    private Socket socket;
    private Storage s = null;

    private byte[] head = new byte[3];
    private byte[] packetlength =new byte[4];
    private byte[] type=new byte[4];
    private byte[] w=new byte[4];
    private byte[] h=new byte[4];
    public Compute(Socket socket,Storage s) {
        this.socket = socket;
        this.s = s;

    }

    public  void run() {
        long time = System.currentTimeMillis();
        MMFR mmfr=null;
        InetAddress address = socket.getInetAddress();
        ComputerToTerminalThread return_thread = null;
        try {
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            inputStream.read(head,0,3);
            if(!"eaae02".equals(FeatureUtils.bytesToHexString(head))) {
                System.out.println(Thread.currentThread().getName()+"   error-data");
                socket.close();
                new PackageException();
                return;
            }
            inputStream.read(packetlength,0,4);
            inputStream.read(type,0,4);
            inputStream.read(w,0,4);
            inputStream.read(h,0,4);
            int pack_len = FeatureUtils.bytesToInt(packetlength,0);
            int type_  = FeatureUtils.bytesToInt(type,0);
            int w_ = FeatureUtils.bytesToInt(w,0);
            int h_ = FeatureUtils.bytesToInt(h,0);
//            System.out.println(pack_len);
//            System.out.println(type_);
//            System.out.println(w_);
//            System.out.println(h_);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte buffer[] = new byte[1024];
            int remainingBytes = pack_len;
            //循环读取
            while (remainingBytes > 0) {
                int bytesRead = inputStream.read(buffer);
                if (bytesRead < 0) {
                    throw new PackageException();
                }
                baos.write(buffer, 0, bytesRead);
                remainingBytes -= bytesRead;
            }
            byte[] image_data = baos.toByteArray();
            System.out.println("请求图片====数据读取完毕");
            ASVLOFFSCREEN inputImg = new ASVLOFFSCREEN();
            inputImg.u32PixelArrayFormat = ASVL_COLOR_FORMAT.ASVL_PAF_NV21;
            inputImg.i32Width = w_;
            inputImg.i32Height = h_;
            inputImg.pi32Pitch[0] = inputImg.i32Width;
            inputImg.pi32Pitch[1] = inputImg.i32Width;
            inputImg.ppu8Plane[0] = new Memory(inputImg.pi32Pitch[0] * inputImg.i32Height);
            inputImg.ppu8Plane[0].write(0, image_data, 0, inputImg.pi32Pitch[0] * inputImg.i32Height);
            inputImg.ppu8Plane[1] = new Memory(inputImg.pi32Pitch[1] * inputImg.i32Height / 2);
            inputImg.ppu8Plane[1].write(0, image_data, inputImg.pi32Pitch[0] * inputImg.i32Height, inputImg.pi32Pitch[1] * inputImg.i32Height / 2);
            inputImg.ppu8Plane[2] = Pointer.NULL;
            inputImg.ppu8Plane[3] = Pointer.NULL;
            FaceInfo[] faceInfo=null;
            AFR_FSDK_FACEMODEL faceFeature=null;
            double[] res=null;


            mmfr = s.pop();
            faceInfo = mmfr.doFaceDetection(inputImg);

            if(faceInfo.length == 0){
                throw new DetecetionException();
            }
            faceFeature= mmfr.extractFRFeature(inputImg,faceInfo[0]);
            System.out.println(address+"特征提取完毕");
            if(faceFeature ==null){
                throw new ExtractFRFeatureException();
            }
            res  = mmfr.compareFaceSimilarityMany(faceFeature);
            String no = mmfr.getNoList().get((int)res[2]-1);

            if(mmfr!=null){
                s.push(mmfr);
                mmfr = null;
                System.out.println("计算资源个数"+String.valueOf(s.queues.size()));
            }

            String ss = "置信度:"+String.valueOf(res[1])+" | 谁:: id:"+String.valueOf((int)res[2]+ "| NO::" +no);
            System.out.println(socket.getInetAddress().getHostAddress()+"###请求   处理请求时间::::"+String.valueOf(System.currentTimeMillis()-time)+"  "+ss);

            if(res[1]>LocalInfo.ACC){
//                //0 表示出 1 表示进
                if( LocalInfo.MAP.get(socket.getInetAddress().getHostAddress())!=null &&
                        LocalInfo.MAP.get(socket.getInetAddress().getHostAddress())!=(int)res[2]){
                    MMDBThreadPoolUtils.executor.execute(new InsertFRRecord(no,type_));
                    LocalInfo.MAP.put(socket.getInetAddress().getHostAddress(),(int)res[2]);
                }else {

                    System.out.println("同一个人 不用插入数据库::NO::"+String.valueOf(no));
                }

            }
            return_thread = new ComputerToTerminalThread(address.getHostAddress(), res);
            return_thread.setPriority(6);
            return_thread.start();

        }catch (PackageException e){
            System.err.println("数据包出错");
            e.printStackTrace();
        }catch (ExtractFRFeatureException e){
            System.err.println("特征提取失败");
        }catch (DetecetionException e){
            System.err.println("人脸检测失败");
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(mmfr!=null){
                    s.push(mmfr);
                    System.out.println("计算资源个数"+String.valueOf(s.queues.size()));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try{

                if(return_thread ==null){
                    System.err.println("异常验证返回请求");
                    double[] res = new double[]{0, 0, 0};
                    return_thread = new ComputerToTerminalThread(address.getHostAddress(), res);
                    return_thread.setPriority(7);
                    return_thread.start();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                if (socket != null|| !socket.isClosed())
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

}