package org.jeecg.modules.system.controller;

import org.jeecg.common.util.RedisUtil;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LocalAccountSelfServiceDisabledTest {
    @Test
    public void retiredEndpointsHaveNoHandlerEvenWithoutAuthenticationFilter() throws Exception {
        MockMvc mvc = MockMvcBuilders.standaloneSetup(new SysUserController()).build();
        for (String endpoint : new String[]{"checkOnlyUser", "querySysUser", "passwordChange"}) {
            mvc.perform(get("/sys/user/" + endpoint)).andExpect(status().isNotFound());
        }
        for (String endpoint : new String[]{"register", "phoneVerification"}) {
            mvc.perform(post("/sys/user/" + endpoint)
                    .contentType(MediaType.APPLICATION_JSON).content("{}"))
                    .andExpect(status().isNotFound());
        }
    }

    @Test
    public void registrationAndRecoverySmsAreRejectedBeforeRedisOrDelivery() throws Exception {
        LoginController controller = new LoginController();
        RedisUtil redis = mock(RedisUtil.class);
        when(redis.get("13800000000")).thenReturn("cached-code");
        ReflectionTestUtils.setField(controller, "redisUtil", redis);
        MockMvc mvc = MockMvcBuilders.standaloneSetup(controller).build();
        for (String mode : new String[]{"1", "2", "invalid"}) {
            mvc.perform(post("/sys/sms").contentType(MediaType.APPLICATION_JSON)
                    .content("{\"mobile\":\"13800000000\",\"smsmode\":\"" + mode + "\"}"))
                    .andExpect(status().isGone());
        }
        verifyZeroInteractions(redis);
    }

    @Test
    public void loginSmsStillUsesExistingFlow() throws Exception {
        LoginController controller = new LoginController();
        RedisUtil redis = mock(RedisUtil.class);
        when(redis.get("13800000000")).thenReturn("cached-code");
        ReflectionTestUtils.setField(controller, "redisUtil", redis);
        MockMvcBuilders.standaloneSetup(controller).build()
                .perform(post("/sys/sms").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"mobile\":\"13800000000\",\"smsmode\":\"0\"}"))
                .andExpect(status().isOk());
        verify(redis).get("13800000000");
    }
}
