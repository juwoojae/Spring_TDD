package com.example.spring_tdd.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Util 클래스의 경우 보통 static 메소드로 작성한다. 이 경우는 mock 객체 없이 직접 호출해서
 * 테스트를 수행하게 된다.
 */
class ValidationUitlTest {

    @Test
    void isEmail_test1() {

        //given
        String email = "test@test.com";

        //when
        boolean result = ValidationUtil.isEmail(email);
        //then
        assertTrue(result);
    }
    @Test
    void isEmail_test2() {
        //given
        String email = "test.com";

        //when
        boolean result = ValidationUtil.isEmail(email);
        //then
        assertFalse(result);
    }
}