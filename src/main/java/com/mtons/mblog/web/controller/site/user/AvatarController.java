/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.mtons.mblog.web.controller.site.user;

import com.mtons.mblog.base.context.AppContext;
import com.mtons.mblog.base.data.Data;
import com.mtons.mblog.base.upload.FileRepo;
import com.mtons.mblog.base.utils.FilePathUtils;
import com.mtons.mblog.base.utils.OssImageUtils;
import com.mtons.mblog.modules.data.AccountProfile;
import com.mtons.mblog.modules.service.UserService;
import com.mtons.mblog.web.controller.BaseController;
import com.mtons.mblog.web.controller.site.Views;
import com.mtons.mblog.base.utils.ImageUtils;
import org.hibernate.bytecode.enhance.internal.PersistentAttributesHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * @author langhsu
 *
 */
@Controller
@RequestMapping("/user")
public class AvatarController extends BaseController {
	@Autowired
	private AppContext appContext;
	@Autowired
	private UserService userService;
	@Autowired
	private FileRepo fileRepo;

	@RequestMapping(value = "/avatar", method = RequestMethod.GET)
	public String view() {
		return view(Views.USER_AVATAR);
	}
	
	@RequestMapping(value = "/avatar", method = RequestMethod.POST)
	public String post(String path, Float x, Float y, Float width, Float height, ModelMap model) {
		AccountProfile profile = getProfile();
		if (StringUtils.isEmpty(path)) {
			model.put("data", Data.failure("请选择图片"));
			return view(Views.USER_AVATAR);
		}
		
		if (width != null && height != null) {
			String root = fileRepo.getRoot();
			File temp = new File(fileRepo.download(path));
			File scale = null;
			try {
		        // 在目标目录下生成截图
		        String scalePath = root + "/" + profile.getId() + ".jpg";
				OssImageUtils.cutImage(temp, scalePath, x.intValue(), y.intValue(), width.intValue());

				// 对结果图片进行压缩
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				OssImageUtils.scaleImage(new File(scalePath), outputStream, 100);
				String ava100 = fileRepo.store(new ByteArrayInputStream(outputStream.toByteArray()));
				AccountProfile user = userService.updateAvatar(profile.getId(), ava100);
				putProfile(user);
				scale = new File(scalePath);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				//TODO 这里应该采取事件发布机制进行回收资源
				fileRepo.deleteFile(path);
				temp.delete();
				if (scale != null) {
					scale.delete();
				}
			}
		}
		return "redirect:/user/profile";
	}
	
	public String getAvaPath(long uid, int size) {
		String base = FilePathUtils.getAvatar(uid);
		return String.format("/%s_%d.jpg", base, size);
	}
}
