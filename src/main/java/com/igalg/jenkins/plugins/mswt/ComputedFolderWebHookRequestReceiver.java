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

import static hudson.util.HttpResponses.okJSON;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.StaplerRequest;

import com.google.common.annotations.VisibleForTesting;
import com.igalg.jenkins.plugins.mswt.locator.ComputedFolderLocator;
import com.igalg.jenkins.plugins.mswt.locator.ComputedFolderWebHookTriggerLocator;
import com.igalg.jenkins.plugins.mswt.locator.JenkinsInstanceComputedFolderLocator;
import com.igalg.jenkins.plugins.mswt.locator.LocatedComputedFolder;
import com.igalg.jenkins.plugins.mswt.trigger.ComputedFolderWebHookTrigger;
import com.igalg.jenkins.plugins.mswt.trigger.ComputedFolderWebHookTriggerResult;

import hudson.Extension;
import hudson.model.UnprotectedRootAction;
import hudson.security.csrf.CrumbExclusion;







@Extension
public class ComputedFolderWebHookRequestReceiver extends CrumbExclusion implements UnprotectedRootAction {

  private static final String NO_JOBS_MSG = "Did not find any jobs with multibranch scan webhook trigger configured."
          + "you need to pass token like ...multibranch-webhook-trigger/invoke?token=TOKENHERE. ";
          
  private static final String URL_NAME = "multibranch-webhook-trigger";

  private static final String TOKEN_PARAM = "token";
  
  private static final Logger LOGGER =  Logger.getLogger(ComputedFolderWebHookRequestReceiver.class.getName());

  
  
  
  
  @Override
  public boolean process(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)throws IOException, ServletException {
    final String pathInfo = request.getPathInfo();
    
    if (pathInfo != null && pathInfo.startsWith("/" + URL_NAME + "/")) {
      chain.doFilter(request, response);
      return true;
    }
    return false;
  }
  
  public HttpResponse doInvoke(final StaplerRequest request) {
    Map<String, String[]> parameterMap = null;
    Map<String, List<String>> headers = null;
   
    headers = getHeaders(request);
    parameterMap = request.getParameterMap();
    
    final String givenToken = getGivenToken(headers, parameterMap);
    return doInvoke(givenToken);
  }
 
  
  
  @VisibleForTesting
  String getGivenToken(final Map<String, List<String>> headers, final Map<String, String[]> parameterMap) {
    if (parameterMap.containsKey(TOKEN_PARAM)) {
      return parameterMap.get(TOKEN_PARAM)[0];
    }
    if (headers.containsKey(TOKEN_PARAM)) {
      return headers.get(TOKEN_PARAM).get(0);
    }
    return null;
  }

  @VisibleForTesting
  Map<String, List<String>> getHeaders(final StaplerRequest request) {
    Map<String, List<String>> headers = new HashMap<>();
    Enumeration<String> headersEnumeration = request.getHeaderNames();
    while (headersEnumeration.hasMoreElements()) {
      String headerName = headersEnumeration.nextElement();
      headers.put(headerName.toLowerCase(), Collections.list(request.getHeaders(headerName)));
    }
    return headers;
  }

  @VisibleForTesting
  HttpResponse doInvoke(final String givenToken) {
	ComputedFolderLocator computedFolderLocator = new JenkinsInstanceComputedFolderLocator();
    List<LocatedComputedFolder> locatedList = ComputedFolderWebHookTriggerLocator.locateJobsWithTrigger(computedFolderLocator,givenToken);
    Map<String, Object> triggerResultsMap = new HashMap<String, Object>();
    
    if (locatedList.isEmpty()) {
      LOGGER.log(INFO, NO_JOBS_MSG);
      triggerResultsMap.put("ANY", NO_JOBS_MSG);
    }
    
    for (LocatedComputedFolder locatedComputedFolder : locatedList) {
      try {
        LOGGER.log(INFO, "Triggering " + locatedComputedFolder.getFullName());
        ComputedFolderWebHookTrigger trigger = locatedComputedFolder.getTrigger();
        ComputedFolderWebHookTriggerResult triggerResults =	trigger.trigger();
        triggerResultsMap.put(locatedComputedFolder.getFullName(), triggerResults);
      } catch (Throwable t) {
        LOGGER.log(SEVERE, locatedComputedFolder.getFullName(), t);
        final String msg =  "Exception occurred, full stack trace in Jenkins server log. Thrown in: " + t.getStackTrace()[0].getClassName() + ":" + t.getStackTrace()[0].getLineNumber();
        triggerResultsMap.put(locatedComputedFolder.getFullName(), msg);
      }
    }

    Map<String, Object> response = new HashMap<>();
    response.put("triggerResults", triggerResultsMap);
    return okJSON(response);
  }

  @Override
  public String getIconFileName() {
    return null;
  }

  @Override
  public String getDisplayName() {
    return null;
  }

  @Override
  public String getUrlName() {
    return URL_NAME;
  }

 
}
