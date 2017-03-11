package com.xiamu.dao;

import com.xiamu.BaseDao;
import com.xiamu.entity.tables.pojos.User;
import com.xiamu.entity.tables.records.UserRecord;
import org.jooq.Table;
import org.springframework.stereotype.Repository;
import java.util.List;
import static com.xiamu.entity.Tables.USER;


/**
 * Description:
 *
 * @author haoyuan.yang
 * @version 1.0
 * @date: 2017/3/11
 * @since JDK 1.8
 */
@Repository
public class UserDao extends BaseDao<UserRecord> {
    @Override
    public Table<UserRecord> table() {
        return USER;
    }


    /**
     * Description:
     *
     * @author haoyuan.yang
     * @version 1.0
     * @date: 2017/3/11 18:04
     * @since JDK 1.8
     */
    public List<User> findAllUsers() {
        return dsl.selectFrom(table()).fetch().into(User.class);
    }
}
