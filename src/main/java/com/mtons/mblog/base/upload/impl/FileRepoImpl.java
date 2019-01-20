/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.mtons.mblog.base.upload.impl;

import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * @author langhsu
 *
 */
//@Service
public class FileRepoImpl extends AbstractFileRepo {
	/**
	 * 存储图片
	 *
	 * @param inputStream
	 * @return
	 * @throws
	 */
	@Override
	public String store(InputStream inputStream) throws Exception {
		return null;
	}

	@Override
	public String getRoot() {
		return appContext.getRoot();
	}

	/**
	 * 下载图片
	 *
	 * @param path
	 */
	@Override
	public String download(String path) {
		return path;
	}
}
