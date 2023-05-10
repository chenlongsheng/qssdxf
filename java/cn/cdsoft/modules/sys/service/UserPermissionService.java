package cn.cdsoft.modules.sys.service;

import cn.cdsoft.common.service.ServiceResult;
import cn.cdsoft.modules.sys.dao.MenuDao;
import cn.cdsoft.common.utils.StringUtils;
import cn.cdsoft.modules.sys.dao.UserPermissionDao;
import cn.cdsoft.modules.sys.entity.Menu;
import org.apache.shiro.session.Session;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.SerializeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by LJR on 2019/1/23.
 */
@Service
public class UserPermissionService {

    @Autowired
    private UserPermissionDao userPermissionDao;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private MenuDao menuDao;

    private String keyPrefix = "admin_shiro_redis_session:";


    public ServiceResult getUserPermission(String token) {
        ServiceResult result = new ServiceResult();
        if(StringUtils.isBlank(token)){
            result.setMessage("token不能为空");
            return result;
        }
        String key = this.keyPrefix + token;
        byte[] value = redisManager.get(key.getBytes());
        Session session = (Session) SerializeUtils.deserialize(value);
        String userId = session.getAttribute("org.apache.shiro.subject.support.DefaultSubjectContext_PRINCIPALS_SESSION_KEY").toString();
        Menu m = new Menu();
        m.setUserId(userId);
        List<Menu> menuList = menuDao.findByUserId(m);
        result.setSuccess(true);
        result.setMessage("查询成功");
        result.setData(menuList);
        return result;
    }
}
