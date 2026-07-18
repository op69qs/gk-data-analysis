package org.jeecg.modules.indexlib;

import org.junit.Test;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class IndexSchemeProductionContractTest {

    @Test
    public void productionControllerExposesOnlyApprovedMethods() throws Exception {
        Class<?> controller = Class.forName(
                "org.jeecg.modules.indexlib.controller.IndexSchemeController");
        Set<String> actual = Arrays.stream(controller.getDeclaredMethods())
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .map(Method::getName)
                .collect(Collectors.toSet());

        assertEquals(new HashSet<>(Arrays.asList(
                "getIndexInfo", "deleteScheme", "selectSchemeTable")), actual);
    }

    @Test
    public void productionControllerUsesApprovedUrls() throws Exception {
        Class<?> controller = Class.forName(
                "org.jeecg.modules.indexlib.controller.IndexSchemeController");
        assertArrayEquals(new String[]{"indexSchemeController"},
                controller.getAnnotation(RequestMapping.class).value());
        assertPostMapping(controller, "getIndexInfo", "getIndexInfo");
        assertPostMapping(controller, "deleteScheme", "/deleteScheme");
        assertPostMapping(controller, "selectSchemeTable", "/selectSchemeTable");
    }

    private void assertPostMapping(Class<?> controller, String methodName, String url)
            throws Exception {
        Method method = controller.getDeclaredMethod(
                methodName, com.alibaba.fastjson.JSONObject.class);
        assertArrayEquals(new String[]{url}, method.getAnnotation(PostMapping.class).value());
    }
}
