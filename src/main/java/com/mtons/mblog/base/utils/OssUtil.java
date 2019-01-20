//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.mtons.mblog.base.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class OssUtil {
    private static Logger logger = LoggerFactory.getLogger(OssUtil.class);

    public OssUtil() {
    }

    private static void OSSExceptionLoggerPrint(OSSException oe) throws Exception {
        logger.error("oss服务端异常...\n");
        logger.error("errorCode:\n", oe.getErrorCode());
        logger.error("errorMsg:\n", oe.getErrorMessage());
        logger.error("requestId:\n", oe.getRequestId());
        logger.error("hostId:\n", oe.getHostId());
        throw new Exception(oe.getErrorMessage(), oe);
    }

    private static void clientExceptionLoggerPrint(ClientException ce) throws Exception {
        logger.error("oss客户端异常...\n");
        logger.error("errorCode:\n", ce.getErrorCode());
        logger.error("errorMsg:\n", ce.getErrorMessage());
        throw new Exception(ce.getErrorMessage(), ce);
    }

    public static void putObject(OSSClient client, PutObjectRequest putObjectRequest) throws Exception, IOException {
        try {
            try {
                logger.debug("上传object到名为：" + putObjectRequest.getBucketName() + "的bucket");
                PutObjectResult putObjectResult = client.putObject(putObjectRequest);
                if (putObjectRequest.getCallback() != null) {
                    byte[] buffer = new byte[1024];
                    putObjectResult.getCallbackResponseBody().read(buffer);
                    putObjectResult.getCallbackResponseBody().close();
                }
            } catch (OSSException var8) {
                OSSExceptionLoggerPrint(var8);
            } catch (ClientException var9) {
                clientExceptionLoggerPrint(var9);
            }

        } finally {
            ;
        }
    }

    public static void putObjectByFileSize(OSSClient client, Integer fileSize, PutObjectRequest putObjectRequest) throws Exception, IOException {
        try {
            try {
                logger.debug("上传object到名为：" + putObjectRequest.getBucketName() + "的bucket");
                PutObjectResult putObjectResult = client.putObject(putObjectRequest);
                if (putObjectRequest.getCallback() != null) {
                    byte[] buffer = new byte[fileSize];
                    putObjectResult.getCallbackResponseBody().read(buffer);
                    putObjectResult.getCallbackResponseBody().close();
                }
            } catch (OSSException var9) {
                OSSExceptionLoggerPrint(var9);
            } catch (ClientException var10) {
                clientExceptionLoggerPrint(var10);
            }

        } finally {
            ;
        }
    }

    public static String getOssResourceUri(String bucketName, String endPoint, String key) {
        return String.format("https://%s.%s/%s", bucketName, endPoint, key);
    }

    public static String getOssImgUrl(String bucketName, String endPoint, String key) {
        return !StringUtils.isEmpty(key) ? String.format("http://%s.%s/%s", bucketName, endPoint, key) : null;
    }

    public static String[] parseImgUrl(String fullPath) {
        return new String[]{fullPath.substring(fullPath.indexOf("http://")+7,fullPath.indexOf(".")),fullPath.substring(fullPath.lastIndexOf("/")+1,fullPath.length())};
    }

    public static String getPrefixOssResourceUri(String bucketName, String endPoint) {
        return String.format("https://%s.%s/", bucketName, endPoint);
    }
}
