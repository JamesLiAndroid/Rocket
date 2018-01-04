package cn.hikyson.rocket.parser;

import android.content.Context;
import android.text.TextUtils;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import cn.hikyson.rocket.task.LaunchTask;

/**
 * Created by kysonchao on 2017/12/28.
 */
public class TaskParser {

    public static List<LaunchTask> parse(Context context, String filePath) throws Throwable {
        List<LaunchTask> taskList = new ArrayList<>();
        Element root = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(context.getAssets().open(filePath)).getDocumentElement();
        NodeList taskElements = root.getElementsByTagName("task");
        if (taskElements == null || taskElements.getLength() == 0) {
            return taskList;
        }
        for (int i = 0; i < taskElements.getLength(); i++) {
            Element delegateElement = (Element) taskElements.item(i);
            String clzName = delegateElement.getTextContent();
            final String taskNameDisplay = delegateElement.getAttribute("name");
            Class<?> clz = Class.forName(clzName);
            LaunchTask taskInstance = (LaunchTask) clz.newInstance();
            if (!TextUtils.isEmpty(taskNameDisplay) && !taskNameDisplay.equals(taskInstance.taskName())) {
                throw new IllegalStateException("Task name in assets must same as taskName in launchTask class, or u can just don't declare name field in assets");
            }
            taskList.add(taskInstance);
        }
        return taskList;
    }
}
