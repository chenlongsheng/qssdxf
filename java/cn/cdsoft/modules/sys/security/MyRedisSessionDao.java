package cn.cdsoft.modules.sys.security;

import cn.cdsoft.common.web.Servlets;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.crazycake.shiro.SerializeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MyRedisSessionDao extends RedisSessionDAO {
	private static Logger logger = LoggerFactory.getLogger(RedisSessionDAO.class);
	private RedisManager redisManager;
//    private String keyPrefix = "shiro_redis_session:";
	private String keyPrefix = "cdfire:";

	public MyRedisSessionDao() {
	}

	public void update(Session session) throws UnknownSessionException {
		this.saveSession(session);
	}

	private void saveSession(Session session) throws UnknownSessionException {
		if (session != null && session.getId() != null) {
			byte[] key = this.getByteKey(session.getId());
			byte[] value = SerializeUtils.serialize(session);
			session.setTimeout((long) (this.redisManager.getExpire() * 1000));

			System.out.println("this.redisManager.getExpire()===" + this.redisManager.getExpire());
			this.redisManager.set(key, value, this.redisManager.getExpire());
		} else {
			logger.error("session or session id is null");
		}
	}

	public void delete(Session session) {
		if (session != null && session.getId() != null) {
			this.redisManager.del(this.getByteKey(session.getId()));
		} else {
			logger.error("session or session id is null");
		}
	}

	public Collection<Session> getActiveSessions() {
		Set<Session> sessions = new HashSet();
		Set<byte[]> keys = this.redisManager.keys(this.keyPrefix + "*");
		if (keys != null && keys.size() > 0) {
			Iterator i$ = keys.iterator();

			while (i$.hasNext()) {
				byte[] key = (byte[]) i$.next();
				Session s = (Session) SerializeUtils.deserialize(this.redisManager.get(key));
				sessions.add(s);
			}
		}

		return sessions;
	}

	protected Serializable doCreate(Session session) {
		// 固定生成的sessionid 为用户中心下发的token
		Serializable sessionId = SystemAuthorizingRealm.threadLocal.get();
		if (sessionId != null) {
			SystemAuthorizingRealm.threadLocal.remove();
			this.assignSessionId(session, sessionId);
			this.saveSession(session);
			return sessionId;
		}
		return null;
	}

	protected Session doReadSession(Serializable sessionId) {
		HttpServletRequest request = Servlets.getRequest();
		Session s = null;
		if (request != null) {
			String uri = request.getServletPath();
			// 如果是静态文件，则不获取SESSION
			if (Servlets.isStaticFile(uri)) {
				return null;
			}
			s = (Session) request.getAttribute("session_" + sessionId);
		}
		if (s != null) {
			return s;
		}
		if (sessionId == null) {
			logger.error("session id is null");
			return null;
		} else {
			Session session = (Session) SerializeUtils.deserialize(this.redisManager.get(this.getByteKey(sessionId)));
			if (request != null && session != null) { // 解决shiro多次调用doReadSession时间很长的问题
				request.setAttribute("session_" + sessionId, session);
			}
			return session;
		}
	}

	private byte[] getByteKey(Serializable sessionId) {
		String preKey = this.keyPrefix + sessionId;
		return preKey.getBytes();
	}

	public RedisManager getRedisManager() {
		return this.redisManager;
	}

	public void setRedisManager(RedisManager redisManager) {
		this.redisManager = redisManager;
		this.redisManager.init();
	}

	public String getKeyPrefix() {
		return this.keyPrefix;
	}

	public void setKeyPrefix(String keyPrefix) {
		this.keyPrefix = keyPrefix;
	}
}
