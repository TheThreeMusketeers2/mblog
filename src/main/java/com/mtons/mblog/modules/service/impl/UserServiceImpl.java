/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.mtons.mblog.modules.service.impl;

import com.mtons.mblog.base.lang.EntityStatus;
import com.mtons.mblog.base.lang.MtonsException;
import com.mtons.mblog.modules.data.AccountProfile;
import com.mtons.mblog.modules.data.UserVO;
import com.mtons.mblog.modules.entity.User;
import com.mtons.mblog.modules.repository.RoleDao;
import com.mtons.mblog.modules.repository.UserDao;
import com.mtons.mblog.modules.utils.BeanMapUtils;
import com.mtons.mblog.base.utils.MD5;
import com.mtons.mblog.modules.data.BadgesCount;
import com.mtons.mblog.modules.service.NotifyService;
import com.mtons.mblog.modules.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;

@Service
@Transactional(readOnly = true)
@CacheConfig(cacheNames = "usersCaches")
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;
	@Autowired
	private NotifyService notifyService;

	@Autowired
	private RoleDao roleDao;

	@Override
	@Transactional
	public AccountProfile login(String username, String password) {
		User po = userDao.findByUsername(username);
		AccountProfile u = null;

		Assert.notNull(po, "账户不存在");

//		Assert.state(po.getStatus() != Const.STATUS_CLOSED, "您的账户已被封禁");

		Assert.state(StringUtils.equals(po.getPassword(), password), "密码错误");

		po.setLastLogin(Calendar.getInstance().getTime());
		userDao.save(po);
		u = BeanMapUtils.copyPassport(po);

		BadgesCount badgesCount = new BadgesCount();
		badgesCount.setNotifies(notifyService.unread4Me(u.getId()));

		u.setBadgesCount(badgesCount);
		return u;
	}

	@Override
	@Transactional
	public AccountProfile getProfileByName(String username) {
		User po = userDao.findByUsername(username);
		AccountProfile u = null;

		Assert.notNull(po, "账户不存在");

//		Assert.state(po.getStatus() != Const.STATUS_CLOSED, "您的账户已被封禁");
		po.setLastLogin(Calendar.getInstance().getTime());

		u = BeanMapUtils.copyPassport(po);

		BadgesCount badgesCount = new BadgesCount();
		badgesCount.setNotifies(notifyService.unread4Me(u.getId()));

		u.setBadgesCount(badgesCount);

		return u;
	}

	@Override
	@Transactional
	public UserVO register(UserVO user) {
		Assert.notNull(user, "Parameter user can not be null!");

		Assert.hasLength(user.getUsername(), "用户名不能为空!");
		Assert.hasLength(user.getPassword(), "密码不能为空!");

		User check = userDao.findByUsername(user.getUsername());

		Assert.isNull(check, "用户名已经存在!");

		User po = new User();

		BeanUtils.copyProperties(user, po);

		if (StringUtils.isBlank(po.getName())) {
			po.setName(user.getUsername());
		}

		Date now = Calendar.getInstance().getTime();
		po.setPassword(MD5.md5(user.getPassword()));
		po.setStatus(EntityStatus.ENABLED);
		po.setCreated(now);
		po.setSignature("我相信午后的阳光总是能给人带来希望...");

		userDao.save(po);

		return BeanMapUtils.copy(po, 0);
	}

	@Override
	@Transactional
	@CacheEvict(key = "#user.getId()")
	public AccountProfile update(UserVO user) {
		User po = userDao.findOne(user.getId());
		if (null != po) {
			po.setName(user.getName());
			po.setSignature(user.getSignature());
			userDao.save(po);
		}
		return BeanMapUtils.copyPassport(po);
	}

	@Override
	@Transactional
	@CacheEvict(key = "#id")
	public AccountProfile updateEmail(long id, String email) {
		User po = userDao.findOne(id);

		if (null != po) {
			if (email.equals(po.getEmail())) {
				throw new MtonsException("邮箱地址没做更改");
			}

			User check = userDao.findByEmail(email);

			if (check != null && check.getId() != po.getId()) {
				throw new MtonsException("该邮箱地址已经被使用了");
			}
			po.setEmail(email);
			userDao.save(po);
		}

		return BeanMapUtils.copyPassport(po);
	}

	@Override
	@Cacheable(key = "#userId")
	public UserVO get(long userId) {
		User po = userDao.findOne(userId);
		UserVO ret = null;
		if (po != null) {
			ret = BeanMapUtils.copy(po, 0);
		}
		return ret;
	}

	
	@Override
	public List<UserVO> findHotUserByLastIn(){
		List<UserVO> rets = new ArrayList<>();
		List<User> list = userDao.findTop12ByOrderByLastLoginDesc();
		for (User po : list) {
			UserVO u = BeanMapUtils.copy(po , 0);
			rets.add(u);
		}
		return rets;
	}
	
	@Override
	public UserVO getByUsername(String username) {
		return BeanMapUtils.copy(userDao.findByUsername(username), 0);
	}

	@Override
	public UserVO getByEmail(String email) {
		return BeanMapUtils.copy(userDao.findByEmail(email), 0);
	}

	@Override
	@Transactional
	@CacheEvict(key = "#id")
	public AccountProfile updateAvatar(long id, String path) {
		User po = userDao.findOne(id);
		if (po != null) {
			po.setAvatar(path);
			userDao.save(po);
		}
		return BeanMapUtils.copyPassport(po);
	}

	@Override
	@Transactional
	public void updatePassword(long id, String newPassword) {
		User po = userDao.findOne(id);

		Assert.hasLength(newPassword, "密码不能为空!");

		if (null != po) {
			po.setPassword(MD5.md5(newPassword));
			userDao.save(po);
		}
	}

	@Override
	@Transactional
	public void updatePassword(long id, String oldPassword, String newPassword) {
		User po = userDao.findOne(id);

		Assert.hasLength(newPassword, "密码不能为空!");

		if (po != null) {
			Assert.isTrue(MD5.md5(oldPassword).equals(po.getPassword()), "当前密码不正确");
			po.setPassword(MD5.md5(newPassword));
			userDao.save(po);
		}
	}

	@Override
	@Transactional
	public void updateStatus(long id, int status) {
		User po = userDao.findOne(id);

		if (po != null) {
			po.setStatus(status);
			userDao.save(po);
		}
	}

	@Override
	public Page<UserVO> paging(Pageable pageable) {
		Page<User> page = userDao.findAllByOrderByIdDesc(pageable);
		List<UserVO> rets = new ArrayList<>();

		for (User po : page.getContent()) {
			UserVO u = BeanMapUtils.copy(po , 1);
			rets.add(u);
		}

		return new PageImpl<>(rets, pageable, page.getTotalElements());
	}

	@Override
	public Map<Long, UserVO> findMapByIds(Set<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			return Collections.emptyMap();
		}
		List<User> list = userDao.findAllByIdIn(ids);
		Map<Long, UserVO> ret = new HashMap<>();

		list.forEach(po -> {
			ret.put(po.getId(), BeanMapUtils.copy(po, 0));
		});
		return ret;
	}

}
