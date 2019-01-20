package com.mtons.mblog.base.upload.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.PutObjectRequest;
import com.mtons.mblog.base.utils.OssImageUtils;
import com.mtons.mblog.base.utils.OssUtil;
import com.mtons.mblog.config.OssConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.UUID;

/**
 * @author luohao
 * @Description:
 */

@Component
public class OssFileRepolmpl extends AbstractFileRepo{

    @Autowired(required = false)
    private OssConfig config;

    @Autowired(required = false)
    private OSSClient ossClient;

    @Override
    public String getRoot() {
        return appContext.getRoot();
    }

    @Override
    public String store(MultipartFile file, String basePath) throws Exception {
        validateFile(file);
        Assert.isTrue(config!=null&&ossClient!=null,"未配置oss");
        Assert.notNull(file,"获取到的文件为空,停止上传");
        return store(file.getInputStream());
    }

    @Override
    public String storeScale(MultipartFile file, String basePath, int maxWidth) throws Exception {
        validateFile(file);
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        OssImageUtils.scaleImageByWidth(file, byteOutputStream, maxWidth);
        return store(new ByteArrayInputStream(byteOutputStream.toByteArray()));
    }

    @Override
    public String storeScale(MultipartFile file, String basePath, int width, int height) throws Exception {
        validateFile(file);
        ByteArrayOutputStream outputStream =new ByteArrayOutputStream();
        OssImageUtils.scale(file, outputStream, width, height);
        return store(new ByteArrayInputStream(outputStream.toByteArray()));
    }

    @Override
    public int[] imageSize(String storePath) {
        String root = getRoot();

        File dest = new File(root + storePath);
        int[] ret = new int[2];

        try {
            // 读入文件
            BufferedImage src = ImageIO.read(dest);
            int w = src.getWidth();
            int h = src.getHeight();

            ret = new int[]{w, h};

        } catch (IOException e) {
            e.printStackTrace();
            log.error(e);
        }
        return ret;
    }

    @Override
    public void deleteFile(String storePath) {
        String[] strings = OssUtil.parseImgUrl(storePath);
//        boolean found = ossClient.doesObjectExist(strings[0], strings[1]);
//        if(found){
            ossClient.deleteObject(strings[0], strings[1]);
            log.info("fileRepo delete " + storePath);
//        }
    }

    /**
     * 下载图片
     *
     * @param path
     */
    @Override
    public String download(String path) {
        String[] strings = OssUtil.parseImgUrl(path);
        String downLoadPath = appContext.getRoot()+strings[1];
        ossClient.getObject(new GetObjectRequest(strings[0], strings[1]), new File(downLoadPath));
        return downLoadPath;
    }

    @Override
    public String store(InputStream inputStream) throws Exception {
        String objectName = UUID.randomUUID().toString();
        try {
            PutObjectRequest req = new PutObjectRequest(config.getBucketName(), objectName, inputStream);
            OssUtil.putObject(ossClient, req);
            return OssUtil.getOssImgUrl(config.getBucketName(),config.getEndpoint(), objectName);
        } catch (Exception e) {
            log.error(e);
            throw e;
        }finally {
            inputStream.close();
        }
    }

}
