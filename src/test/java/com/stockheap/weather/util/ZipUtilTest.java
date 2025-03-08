package com.stockheap.weather.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
public class ZipUtilTest {

    @Test
    public void testValidZipPatternsUS()
    {
        boolean ok = ZipUtil.isValidZipCode("US", "94121");
        assertTrue(ok);
        ok = ZipUtil.isValidZipCode("US", "941211");
        assertFalse(ok);
    }

    @Test
    public void testValidZipPatternsCA()
    {
        boolean ok = ZipUtil.isValidZipCode("CA", "K4A 9Z9");
        assertTrue(ok);
        ok = ZipUtil.isValidZipCode("CA", "94121");
        assertFalse(ok);
    }

    @Test
    public void testValidZipPatternsGB()
    {
        boolean ok = ZipUtil.isValidZipCode("GB", "L1 8JQ");
        assertTrue(ok);
        ok = ZipUtil.isValidZipCode("GB", "K4A 9Z9");
        assertFalse(ok);
    }

    @Test
    public void testValidZipPatternsDE()
    {
        boolean ok = ZipUtil.isValidZipCode("DE", "12529");
        assertTrue(ok);
        ok = ZipUtil.isValidZipCode("DE", "K4A 9Z9");
        assertFalse(ok);
    }


    @Test
    public void testValidZipPatternsMX()
    {
        boolean ok = ZipUtil.isValidZipCode("MX", "20329");
        assertTrue(ok);
        ok = ZipUtil.isValidZipCode("MX", "K4A 9Z9");
        assertFalse(ok);
    }

    @Test
    public void testValidZipPatternsPT()
    {
        boolean ok = ZipUtil.isValidZipCode("PT", "1000-017");
        assertTrue(ok);
        ok = ZipUtil.isValidZipCode("PT", "K4A 9Z9");
        assertFalse(ok);
    }

    @Test
    public void testValidZipPatternsES()
    {
        boolean ok = ZipUtil.isValidZipCode("ES", "08830");
        assertTrue(ok);
        ok = ZipUtil.isValidZipCode("PT", "K4A 9Z9");
        assertFalse(ok);
    }
}
