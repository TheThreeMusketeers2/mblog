/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.mtons.mblog.base.upload;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author langhsu
 *
 */
public interface FileRepo {
	/**
	 * 存储图片
	 * @param file
	 * @param basePath
	 * @return
	 * @throws IOException
	 */
	String store(MultipartFile file, String basePath) throws Exception;
    /**
     * 存储图片
     * @param inputStream
     * @return
     * @throws IOException
     */
	String store(InputStream inputStream) throws Exception;
	
	/**
	 * 存储压缩图片
	 * @param file
	 * @param basePath
	 * @return
	 * @throws IOException
	 */
	String storeScale(MultipartFile file, String basePath, int maxWidth) throws Exception;

	/**
	 * 存储压缩图片
	 * @param file
	 * @param basePath
	 * @return
	 * @throws IOException
	 */
	String storeScale(MultipartFile file, String basePath, int width, int height) throws Exception;
	
	/**
	 * 获取图片大小
	 *
	 * @param storePath
	 * @return
	 */
	int[] imageSize(String storePath);

	String getRoot();

	/**
	 * 存储路径
	 * @param storePath
	 */
	void deleteFile(String storePath);
    /**
     * 下载图片
     * @param path
     */
	String download(String path);
}
