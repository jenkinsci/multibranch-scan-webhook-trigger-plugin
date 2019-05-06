/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2019 igalg
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.igalg.jenkins.plugins.mswt;

import static com.google.common.collect.ImmutableMap.of;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ComputedFolderWebHookRequestReceiverTest {

  ComputedFolderWebHookRequestReceiver computedFolderWebHookRequestReceiver;
  
  @Before
  public void init() {
	  computedFolderWebHookRequestReceiver = new ComputedFolderWebHookRequestReceiver();
  }
  
  @Test
  public void testThatNoTokenReturnsNull() {
     
     Map<String, List<String>> headers = newHashMap();
     Map<String, String[]> parameterMap = newHashMap();
     String token = computedFolderWebHookRequestReceiver.getGivenToken(headers, parameterMap);
     assertThat(token).isNull();
  }

  @Test
  public void testThatParameterTokenReturnsThatToken() {
    Map<String, List<String>> headers = newHashMap();
    Map<String, String[]> parameterMap = of("token", new String[] {"token-parameter"});
    final String token = computedFolderWebHookRequestReceiver.getGivenToken(headers, parameterMap);
    assertThat(token).isEqualTo("token-parameter");
  }

  @Test
  public void testThatHeaderTokenReturnsThatToken() {
     Map<String, List<String>> headers = of("token", (List<String>) newArrayList("token-header"));
     Map<String, String[]> parameterMap = newHashMap();
     String token = computedFolderWebHookRequestReceiver.getGivenToken(headers, parameterMap);
     assertThat(token).isEqualTo("token-header");
  }
}
