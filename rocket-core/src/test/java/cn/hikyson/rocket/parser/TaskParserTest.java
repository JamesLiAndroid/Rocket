package cn.hikyson.rocket.parser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import cn.hikyson.rocket.BuildConfig;
import cn.hikyson.rocket.RocketApp;

/**
 * Created by kysonchao on 2017/12/28.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, application = RocketApp.class)
public class TaskParserTest {
    @Test
    public void parse() throws Exception {
//        try {
//            List<ConditionTask> conditionTasks = TaskParser.parse(RuntimeEnvironment.application, "task_list.xml");
//            assertTrue(conditionTasks.size() == 5);
//        } catch (Throwable throwable) {
//            assertTrue(throwable.getLocalizedMessage(), false);
//        }
    }

}