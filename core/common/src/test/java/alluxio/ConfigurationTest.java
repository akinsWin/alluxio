/*
 * The Alluxio Open Foundation licenses this work under the Apache License, version 2.0
 * (the "License"). You may not use this work except in compliance with the License, which is
 * available at www.apache.org/licenses/LICENSE-2.0
 *
 * This software is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied, as more fully set forth in the License.
 *
 * See the NOTICE file distributed with this work for information regarding copyright ownership.
 */

package alluxio;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.junit.After;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Unit tests for the {@link Configuration} class.
 */
public class ConfigurationTest {
  @Rule
  public final ExpectedException mThrown = ExpectedException.none();

  @After
  public void after() {
    ConfigurationTestUtils.resetConfiguration();
  }

  @Test
  public void defaultHomeCorrectlyLoaded() {
    String alluxioHome = Configuration.get(PropertyKey.HOME);
    Assert.assertEquals("/mnt/alluxio_default_home", alluxioHome);
  }

  @Test
  public void getInt() {
    Configuration.set(PropertyKey.WEB_THREADS, "1");
    Assert.assertEquals(1, Configuration.getInt(PropertyKey.WEB_THREADS));
  }

  @Test
  public void getMalformedIntThrowsException() {
    Configuration.set(PropertyKey.WEB_THREADS, "9448367483758473854738"); // bigger than MAX_INT
    mThrown.expect(RuntimeException.class);
    Configuration.getInt(PropertyKey.WEB_THREADS);
  }

  @Test
  public void getLong() {
    Configuration.set(PropertyKey.WEB_THREADS, "12345678910"); // bigger than MAX_INT
    Assert.assertEquals(12345678910L, Configuration.getLong(PropertyKey.WEB_THREADS));
  }

  @Test
  public void getMalformedLongThrowsException() {
    Configuration.set(PropertyKey.WEB_THREADS,
        "999999999999999999999999999999999999"); // bigger than MAX_LONG
    mThrown.expect(RuntimeException.class);
    Configuration.getLong(PropertyKey.WEB_THREADS);
  }

  @Test
  public void getDouble() {
    Configuration.set(PropertyKey.WEB_THREADS, "1.1");
    Assert.assertEquals(1.1, Configuration.getDouble(PropertyKey.WEB_THREADS),
        /*tolerance=*/0.0001);
  }

  @Test
  public void getMalformedDoubleThrowsException() {
    Configuration.set(PropertyKey.WEB_THREADS, "1a");
    mThrown.expect(RuntimeException.class);
    Configuration.getDouble(PropertyKey.WEB_THREADS);
  }

  @Test
  public void getFloat() {
    Configuration.set(PropertyKey.WEB_THREADS, "1.1");
    Assert.assertEquals(1.1, Configuration.getFloat(PropertyKey.WEB_THREADS), /*tolerance=*/0.0001);
  }

  @Test
  public void getMalformedFloatThrowsException() {
    Configuration.set(PropertyKey.WEB_THREADS, "1a");
    mThrown.expect(RuntimeException.class);
    Configuration.getFloat(PropertyKey.WEB_THREADS);
  }

  @Test
  public void getTrueBoolean() {
    Configuration.set(PropertyKey.WEB_THREADS, "true");
    Assert.assertTrue(Configuration.getBoolean(PropertyKey.WEB_THREADS));
  }

  @Test
  public void getTrueBooleanUppercase() {
    Configuration.set(PropertyKey.WEB_THREADS, "True");
    Assert.assertTrue(Configuration.getBoolean(PropertyKey.WEB_THREADS));
  }

  @Test
  public void getTrueBooleanMixcase() {
    Configuration.set(PropertyKey.WEB_THREADS, "tRuE");
    Assert.assertTrue(Configuration.getBoolean(PropertyKey.WEB_THREADS));
  }

  @Test
  public void getFalseBoolean() {
    Configuration.set(PropertyKey.WEB_THREADS, "false");
    Assert.assertFalse(Configuration.getBoolean(PropertyKey.WEB_THREADS));
  }

  @Test
  public void getFalseBooleanUppercase() {
    Configuration.set(PropertyKey.WEB_THREADS, "False");
    Assert.assertFalse(Configuration.getBoolean(PropertyKey.WEB_THREADS));
  }

  @Test
  public void getFalseBooleanMixcase() {
    Configuration.set(PropertyKey.WEB_THREADS, "fAlSe");
    Assert.assertFalse(Configuration.getBoolean(PropertyKey.WEB_THREADS));
  }

  @Test
  public void getMalformedBooleanThrowsException() {
    Configuration.set(PropertyKey.WEB_THREADS, "x");
    mThrown.expect(RuntimeException.class);
    Configuration.getBoolean(PropertyKey.WEB_THREADS);
  }

  @Test
  public void getList() {
    Configuration.set(PropertyKey.WEB_THREADS, "a,b,c");
    Assert.assertEquals(
        Lists.newArrayList("a", "b", "c"), Configuration.getList(PropertyKey.WEB_THREADS, ","));
  }

  private enum TestEnum {
    VALUE
  }

  @Test
  public void getEnum() {
    Configuration.set(PropertyKey.WEB_THREADS, "VALUE");
    Assert.assertEquals(
        TestEnum.VALUE, Configuration.getEnum(PropertyKey.WEB_THREADS, TestEnum.class));
  }

  @Test
  public void getMalformedEnum() {
    Configuration.set(PropertyKey.WEB_THREADS, "not_a_value");
    mThrown.expect(RuntimeException.class);
    Configuration.getEnum(PropertyKey.WEB_THREADS, TestEnum.class);
  }

  @Test
  public void getBytes() {
    Configuration.set(PropertyKey.WEB_THREADS, "10b");
    Assert.assertEquals(10, Configuration.getBytes(PropertyKey.WEB_THREADS));
  }

  @Test
  public void getBytesKb() {
    Configuration.set(PropertyKey.WEB_THREADS, "10kb");
    Assert.assertEquals(10 * Constants.KB, Configuration.getBytes(PropertyKey.WEB_THREADS));
  }

  @Test
  public void getBytesMb() {
    Configuration.set(PropertyKey.WEB_THREADS, "10mb");
    Assert.assertEquals(10 * Constants.MB, Configuration.getBytes(PropertyKey.WEB_THREADS));
  }

  @Test
  public void getBytesGb() {
    Configuration.set(PropertyKey.WEB_THREADS, "10gb");
    Assert.assertEquals(10 * (long) Constants.GB, Configuration.getBytes(PropertyKey.WEB_THREADS));
  }

  @Test
  public void getBytesGbUppercase() {
    Configuration.set(PropertyKey.WEB_THREADS, "10GB");
    Assert.assertEquals(10 * (long) Constants.GB, Configuration.getBytes(PropertyKey.WEB_THREADS));
  }

  @Test
  public void getBytesTb() {
    Configuration.set(PropertyKey.WEB_THREADS, "10tb");
    Assert.assertEquals(10 * Constants.TB, Configuration.getBytes(PropertyKey.WEB_THREADS));
  }

  @Test
  public void getBytespT() {
    Configuration.set(PropertyKey.WEB_THREADS, "10pb");
    Assert.assertEquals(10 * Constants.PB, Configuration.getBytes(PropertyKey.WEB_THREADS));
  }

  @Test
  public void getMalformedBytesThrowsException() {
    Configuration.set(PropertyKey.WEB_THREADS, "100a");
    mThrown.expect(RuntimeException.class);
    Configuration.getBoolean(PropertyKey.WEB_THREADS);
  }

  @Test
  public void getClassTest() { // The name getClass is already reserved.
    Configuration.set(PropertyKey.WEB_THREADS, "java.lang.String");
    Assert.assertEquals(String.class, Configuration.getClass(PropertyKey.WEB_THREADS));
  }

  @Test
  public void getMalformedClassThrowsException() {
    Configuration.set(PropertyKey.WEB_THREADS, "java.util.not.a.class");
    mThrown.expect(RuntimeException.class);
    Configuration.getClass(PropertyKey.WEB_THREADS);
  }

  @Test
  public void variableSubstitution() {
    Configuration.merge(ImmutableMap.of(
        PropertyKey.HOME, "value",
        PropertyKey.LOGS_DIR, "${alluxio.home}"));
    String substitution = Configuration.get(PropertyKey.HOME);
    Assert.assertEquals("value", substitution);
  }

  @Test
  public void twoVariableSubstitution() {
    Configuration.merge(ImmutableMap.of(
        PropertyKey.MASTER_HOSTNAME, "value1",
        PropertyKey.MASTER_RPC_PORT, "value2",
        PropertyKey.MASTER_ADDRESS, "${alluxio.master.hostname}:${alluxio.master.port}"));
    String substitution = Configuration.get(PropertyKey.MASTER_ADDRESS);
    Assert.assertEquals("value1:value2", substitution);
  }

  @Test
  public void recursiveVariableSubstitution() {
    Configuration.merge(ImmutableMap.of(
        PropertyKey.HOME, "value",
        PropertyKey.LOGS_DIR, "${alluxio.home}",
        PropertyKey.SITE_CONF_DIR, "${alluxio.logs.dir}"));
    String substitution2 = Configuration.get(PropertyKey.SITE_CONF_DIR);
    Assert.assertEquals("value", substitution2);
  }

  @Test
  public void systemVariableSubstitution() throws Exception {
    try (SetAndRestoreSystemProperty c =
        new SetAndRestoreSystemProperty(PropertyKey.MASTER_HOSTNAME.toString(), "new_master")) {
      Configuration.defaultInit();
      Assert.assertEquals("new_master", Configuration.get(PropertyKey.MASTER_HOSTNAME));
    }
  }

  @Test
  public void userFileBufferBytesOverFlowException() {
    mThrown.expect(IllegalArgumentException.class);
    Configuration.set(PropertyKey.USER_FILE_BUFFER_BYTES,
        String.valueOf(Integer.MAX_VALUE + 1) + "B");
  }

  @Test
  public void setUserFileBufferBytesMaxInteger() {
    Configuration.set(PropertyKey.USER_FILE_BUFFER_BYTES, String.valueOf(Integer.MAX_VALUE) + "B");
    Assert.assertEquals(Integer.MAX_VALUE,
        (int) Configuration.getBytes(PropertyKey.USER_FILE_BUFFER_BYTES));
  }

  @Test
  public void setUserFileBufferBytes1GB() {
    Configuration.set(PropertyKey.USER_FILE_BUFFER_BYTES, "1GB");
    Assert.assertEquals(1073741824,
        (int) Configuration.getBytes(PropertyKey.USER_FILE_BUFFER_BYTES));
  }
}
