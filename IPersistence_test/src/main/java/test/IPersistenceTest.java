package test;

import com.batic.io.Resource;
import com.batic.sqlSession.SqlSession;
import com.batic.sqlSession.SqlSessionFactory;
import com.batic.sqlSession.SqlSessionFactoryBuilder;
import dao.IUserDao;
import org.junit.Test;
import pojo.User;

import java.io.InputStream;
import java.util.List;

public class IPersistenceTest {

    @Test
    public void test() throws Exception {
        InputStream inputStream = Resource.getResourceAsSteam("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

//        List<User> userList = sqlSession.selectList("user.selectList");
//        for (User user : userList) {
//            System.out.println(user);
//        }
//
//        User user1 = new User();
//        user1.setId(2);
//        User data = sqlSession.selectOne("user.selectOne",user1);
//
//
//        System.out.println(data);

        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        User condition = new User();
        condition.setId(2);
        User userInfo = userDao.findByCondition(condition);
        System.out.println(userInfo);

        List<User> all = userDao.findAll();
        for (User user : all) {
            System.out.println(user);
        }

    }

}
