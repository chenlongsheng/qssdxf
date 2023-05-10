package cn.cdsoft.common.utils;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by LJR on 2018/12/27.
 */
@Service
@Lazy(false)
public class UserCenterToeknUtils implements SessionIdGenerator {

    @Override
    public Serializable generateId(Session session) {
        System.out.println(session);
        String id = "aaa";
        return id;

    }
}
