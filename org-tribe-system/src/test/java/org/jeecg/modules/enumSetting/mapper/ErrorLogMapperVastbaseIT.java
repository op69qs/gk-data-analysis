package org.jeecg.modules.enumSetting.mapper;

import org.jeecg.modules.util.PageData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ErrorLogMapperVastbaseIT {

    @Autowired
    private ErrorLogMapper errorLogMapper;

    @Test
    public void queriesExecShellTaskThroughRealMyBatisMapper() {
        PageData parameters = new PageData();
        parameters.put("page", 0);
        parameters.put("rows", 10);

        Integer count = errorLogMapper.getCount(parameters);
        List<Map<String, Object>> rows = errorLogMapper.getData(parameters);

        assertNotNull(count);
        assertNotNull(rows);
        assertTrue(rows.size() <= 10);
        assertEquals(Math.min(count, 10), rows.size());
    }
}
