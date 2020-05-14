package dao.impl;

import com.batic.io.Resource;
import com.batic.sqlSession.SqlSession;
import com.batic.sqlSession.SqlSessionFactory;
import com.batic.sqlSession.SqlSessionFactoryBuilder;
import dao.IUserDao;
import pojo.User;

import java.io.InputStream;
import java.util.List;

public class UserDaoImpl implements IUserDao {


    @Override
    public List<User> findAll() throws Exception {
        InputStream inputStream = Resource.getResourceAsSteam("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        List<User> userList = sqlSession.selectList("user.selectList");
        return userList;
    }

    @Override
    public User findByCondition(User user) throws Exception {
        InputStream inputStream = Resource.getResourceAsSteam("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        User data = (User)sqlSession.selectOne("user.selectOne",user);
        return data;
    }
}
