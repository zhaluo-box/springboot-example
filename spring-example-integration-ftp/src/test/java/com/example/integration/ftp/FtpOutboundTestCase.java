package com.example.integration.ftp;

// 有没有很眼熟，spring的ftp也是对apache的ftp进行了一次包装，但是代码减少了不少

import org.apache.commons.net.ftp.FTPClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.integration.ftp.outbound.FtpMessageHandler;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.integration.ftp.session.FtpSession;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by xulong on 2017/9/16.
 * xulong1@126.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:ftp/ftp.xml"})
public class FtpOutboundTestCase {

    @Resource(name = "channel")
    private MessageChannel channel;

    @Resource
    private Environment environment;

    @Resource
    private DefaultFtpSessionFactory ftpSessionFactory;

    private Logger logger = LoggerFactory.getLogger(FtpOutboundTestCase.class);

    @Resource(name = "targetDir")
    private AtomicReference targetDir;

    private void sendFile(String path, String filePath) throws IOException, InterruptedException {
        FtpSession session = ftpSessionFactory.getSession();
        if (logger.isDebugEnabled()) {
            logger.debug("current session is:[{}]", session.hashCode());
        }

        FTPClient ftpClient = session.getClientInstance();
        boolean success = ftpClient.changeWorkingDirectory(path);
        if (logger.isDebugEnabled()) {
            logger.debug("切换工作目录是否成功：【{}】", success);
        }
        if (!success) {
            session.mkdir(path);
        }
        // TODO session.exists有严重的bug，跟踪源码发现的
//        if (!session.exists(path)) {
//            session.mkdir(path);
//        }
        // 此处要修改值，否则上传的目录地址还是默认的目录地址
        targetDir.set(path);

        File localFile = new File(filePath);
        Message<File> message = MessageBuilder.withPayload(localFile).build();

        channel.send(message);
        Thread.sleep(2000L);
        session.close();
    }

    public void ss(){
        FtpMessageHandler ftpMessageHandler = new FtpMessageHandler(ftpSessionFactory);
    }

    @Test
    public void testUploadFile() throws InterruptedException, IOException {
        String path = "/home/zhauo/dev_software/temp/";// 模拟每天动态生成一个yyyyMMdd的目录，当天的所有文件都上传到该目录下
        String localFilePath = "/home/zhauo/dev_software/temp/reader/aa.txt";
        sendFile(path, localFilePath);
        path = "/home/zhauo/dev_software/temp/writer/";
        sendFile(path, localFilePath);
    }

}