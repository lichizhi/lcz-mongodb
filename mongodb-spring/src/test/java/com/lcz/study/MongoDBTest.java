package com.lcz.study;

import com.lcz.study.model.UserDO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

/**
 * mongodb分页排序方案
 * 方案一：limit，skip，sort。
 * 首先：count()总数
 *  第一页：db.books.find().limit(3);
 *  第二页：db.books.find().limit(3).skip(3);
 *
 * 方案二：
 * 首先：count()总数
 *  第一页：db.books.find().limit(3);
 *  第二页：db.books.find({_id: {$gt: <第一页的最后一个 ID>}}).limit(3).sort({_id: 1});
 *  id是有顺序的，而且不重复。（id+增量）
 *
 * 方案三：
 * 固定限制只显示10页,每页50条，这样避免了count(),skip效率不会太低。
 *
 * 分析：
 *  方案一性能最差，count和skip一个也没避开。可以跨页翻页，完整性较好。
 *  方案二性能适中，避开了skip。但数据翻页不能跨页翻页。按顺序翻页
 *  方案三性能最好，但是数据完整性较差。
 *
 *
 *  通常情况下，用户不会要求在web端完整的显示大数据，可以做出合理的解释。所以通常选择第三种。
 */
@SpringBootTest(classes = {MongoDBApplication.class})
public class MongoDBTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void insert() {
        UserDO user = new UserDO();
        user.setAge(20);
        user.setName("test");
        user.setEmail("123@qq.com");
        UserDO user1 = mongoTemplate.insert(user);
        System.out.println(user1);
    }

    @Test
    public void update() {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is("test"));
        Update update = Update.update("age", 23);
        mongoTemplate.updateMulti(query, update, UserDO.class);
        System.out.println("更新成功");
    }

    @Test
    public void remove() {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is("test"));
        mongoTemplate.remove(query, UserDO.class);
        System.out.println("删除成功");
    }

    @Test
    public void find() {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is("test"));
        List<UserDO> userDOS = mongoTemplate.find(query, UserDO.class);
        userDOS.forEach(System.out::println);
    }

}
