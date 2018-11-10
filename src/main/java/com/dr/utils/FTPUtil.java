package com.dr.utils;

import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class FTPUtil {

    //需要ip，username，password,写在db.properties中
    private static final String FTPIP=PropertiesUtils.readByKey("ftp.server.ip");
    private static final String FTPUSER=PropertiesUtils.readByKey("ftp.server.user");
    private static final String FTPPASSWORD=PropertiesUtils.readByKey("ftp.server.password");

    private String ftpIp;
    private String ftpUser;
    private String ftpPass;
    private Integer port;

    public FTPUtil(String ftpIp, String ftpUser, String ftpPass, Integer port) {
        this.ftpIp = ftpIp;
        this.ftpUser = ftpUser;
        this.ftpPass = ftpPass;
        this.port = port;
    }

    /**
     * 将图片上传到ftp
     * */
    public static boolean uploadFile(List<File> fileList) throws IOException {
        FTPUtil ftpUtil = new FTPUtil(FTPIP,FTPUSER,FTPPASSWORD,21);
        System.out.println("开始连接FTP服务器....");
        ftpUtil.uploadFile("img",fileList);
        return false;
    }

    /**
     *
     * @Param:remotePath:ftp远程路径
     * */
    public boolean uploadFile(String remotePath,List<File> fileList) throws IOException {
        FileInputStream fileInputStream = null;
        //要上传，先连接，封装一connectFTPServer方法
        if (connectFTPServer(ftpIp, ftpUser, ftpPass)) {
            try {
                //上传路径
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                //字符编码
                ftpClient.setControlEncoding("UTF-8");
                //文件类型：二进制
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalActiveMode();//打开被动传输
                //遍历文件
                for (File file : fileList) {
                    fileInputStream = new FileInputStream(file);
                    //保存文件
                    ftpClient.storeFile(file.getName(), fileInputStream);
                }
                System.out.println("文件上传成功");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("文件上传出错");
            } finally {
                fileInputStream.close();
                ftpClient.disconnect();
            }
        }
        return false;
    }

    /**
     * 连接ftp服务器
     * */
    FTPClient ftpClient = null;
    public boolean connectFTPServer(String ip,String user,String password){
       ftpClient = new FTPClient();

       //调用connect方法连接
        try {
            ftpClient.connect(ip);
            return ftpClient.login(user,password);//login方法返回值类型为false
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("连接FTP服务器异常...");
        }
        return false;
    }

    public String getFtpIp() {
        return ftpIp;
    }

    public void setFtpIp(String ftpIp) {
        this.ftpIp = ftpIp;
    }

    public String getFtpUser() {
        return ftpUser;
    }

    public void setFtpUser(String ftpUser) {
        this.ftpUser = ftpUser;
    }

    public String getFtpPass() {
        return ftpPass;
    }

    public void setFtpPass(String ftpPass) {
        this.ftpPass = ftpPass;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }
}
