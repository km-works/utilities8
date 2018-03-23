/*
 *  Copyright (C) 2005-2017 Christian P. Lerch, Vienna, Austria.
 * 
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free
 *  Software Foundation; either version 3 of the License, or (at your option)
 *  any later version.
 * 
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more 
 *  details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this distribution. If not, see <http://www.gnu.org/licenses/>.
 */
package kmworks.util.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.util.Arrays;
import java.util.Properties;
import static kmworks.util.config.PropertyMap.kv;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author cpl
 */
public class PropertyMapConverterTest {
    
    private void pln(String s) {
        System.out.println(s);
    }
    
    public PropertyMapConverterTest() {
    }

    /** Test series 01.
     *  Verify x == toProperties(fromProperties(x))
     */
    @Test
    public void test_01a() {
        Properties p1 = new Properties();
        p1.put("key1", "some string value");
        p1.put("key2", "123");
        p1.put("key3", "true");
        p1.put("key4", false);
        pln(p1.toString());
        PropertyMap pm = PropertyMapConverter.fromProperties(p1);
        pln(pm.toString());
        Properties p2 = PropertyMapConverter.toProperties(pm);
        pln(p2.toString());
        assertEquals(p1, p2);
    }
    
    @Test
    public void test_01b() {
        Properties p1 = new Properties();
        p1.put("key1", "some string value");
        p1.put("key2", 123);
        p1.put("key3", "true");
        p1.put("key4", false);
        pln(p1.toString());
        PropertyMap pm = PropertyMapConverter.fromProperties(p1);
        pln(pm.toString());
        Properties p2 = PropertyMapConverter.toProperties(pm);
        pln(p2.toString());
        assertEquals(p1, p2);
    }
    
    /** Test series 02.
     *  Verify x == fromProperties(toProperties(x))
     */
    @Test
    public void test_02a() {
        PropertyMap pm1 = PropertyMap.of(Arrays.asList(
            kv("key1", "some string value"),
            kv("key2", 123),
            kv("key3", true),
            kv("key4", false)
        ));
        pln(pm1.toString());
        Properties prop = PropertyMapConverter.toProperties(pm1);
        pln(prop.toString());
        PropertyMap pm2 = PropertyMapConverter.fromProperties(prop);
        pln(pm2.toString());
        assertEquals(pm1, pm2);
    }
    
    @Test
    public void test_02b() {
        PropertyMap pm1 = PropertyMap.of(Arrays.asList(
            kv("key1", "some string value"),
            kv("key2", 123),
            kv("key3", "true"),
            kv("key4", false)
        ));
        pln(pm1.toString());
        Properties prop = PropertyMapConverter.toProperties(pm1);
        pln(prop.toString());
        PropertyMap pm2 = PropertyMapConverter.fromProperties(prop);
        pln(pm2.toString());
        assertEquals(pm1, pm2);
    }
    
    /** Test series 03.
     *  Verify x == toConfig(fromConfig(x))
     */
    @Test
    public void test_03a() {
        Config c1 = ConfigFactory.parseMap(PropertyMap.of(Arrays.asList(
            kv("key1", "some string value"),
            kv("key2", 123),
            kv("key3", true),
            kv("key4", false)
        )).asMap());
        //pln("c1: "+c1.toString());
        PropertyMap pm = PropertyMapConverter.fromConfig(c1);
        //pln("pm: "+pm.toString());
        Config c2 = PropertyMapConverter.toConfig(pm);
        //pln("c2: "+c2.toString());
        assertEquals(c1, c2);
    }
    
    /** Test series 04.
     *  Verify x == fromConfig(toConfig(x))
     */
    @Test
    public void test_04b() {
        PropertyMap pm1 = PropertyMap.of(Arrays.asList(
            kv("key1", "some string value"),
            kv("key2", 123),
            kv("key3", true),
            kv("key4", false)
        ));
        Config c1 = PropertyMapConverter.toConfig(pm1);
        PropertyMap pm2 = PropertyMapConverter.fromConfig(c1);
        assertEquals(pm1, pm2);
    }
    
    @Test
    public void test_temp() {
    }
}
