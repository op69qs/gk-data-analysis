package org.indicatorsLib;

import org.indicatorsLib.util.PageData;
import org.junit.After;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.Assert.assertEquals;

public class BaseControllerIdentityTest {

    @After
    public void tearDown() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void authenticatedHeaderOverwritesAllLegacyUserIdFields() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(BaseController.ANALYSIS_USER_ID_HEADER, "server-user");
        request.addHeader(BaseController.ANALYSIS_GUOKU_ID_HEADER, "GK-001");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        PageData pageData = new PageData();
        pageData.put("userId", "spoofed-user");
        pageData.put("USERID", "spoofed-user");
        pageData.put("ADD_USERID", "spoofed-user");

        new TestController().apply(pageData);

        assertEquals("server-user", pageData.getString("userId"));
        assertEquals("server-user", pageData.getString("USERID"));
        assertEquals("server-user", pageData.getString("ADD_USERID"));
        assertEquals("server-user", pageData.getString("MODIFY_USERID"));
    }

    private static class TestController extends BaseController {
        private void apply(PageData pageData) {
            applyCurrentUser(pageData);
        }
    }
}
