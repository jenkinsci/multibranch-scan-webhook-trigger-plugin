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
package com.igalg.jenkins.plugins.mswt.locator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import com.cloudbees.hudson.plugins.folder.computed.ComputedFolder;
import com.igalg.jenkins.plugins.mswt.trigger.ComputedFolderWebHookTrigger;

import hudson.triggers.Trigger;
import hudson.triggers.TriggerDescriptor;

@SuppressWarnings("rawtypes")
public class ComputedFolderWebHookTriggerLocatorTest {
	  
	private List<ComputedFolder> computedFolderList;
	  private final WorkflowMultiBranchProject multibranchJobWithNoToken = createJobWorkflowMultiBranchProject("abc");
	  private final WorkflowMultiBranchProject multibranchJobWithToken = createJobWorkflowMultiBranchProject("token");
	  private final WorkflowMultiBranchProject multibranchJobWithNoTrigger = createJobWorkflowMultiBranchProjectWithouTrigger();
	  private ComputedFolderLocator computedFolderLocator;

	  @Before
	  public void before() {
		computedFolderList = new ArrayList<>();
		computedFolderList.add(multibranchJobWithNoToken);
		computedFolderList.add(multibranchJobWithToken);
		computedFolderList.add(multibranchJobWithNoTrigger);
		computedFolderLocator = new ComputedFolderLocator() {
			
			@Override
			public List<ComputedFolder> getAllComputedFolders(){
				return computedFolderList;
			}
		};
	  }

  @BeforeEach
  void setUpStaticMocks() {
  }

  @AfterEach
  void tearDownStaticMocks() {
  }

	  @Test
	  public void testThatJobsWithMatchedTokenFoundedAndNotMatchNotFounded() {
	    final String givenToken = "token";

	    List<String> jobsToTrigger = getJobNameList(ComputedFolderWebHookTriggerLocator.locateJobsWithTrigger(computedFolderLocator, givenToken));

	    assertThat(jobsToTrigger.contains(multibranchJobWithToken.getFullName())).isTrue(); 	        
	    assertThat(jobsToTrigger.contains(multibranchJobWithNoToken.getFullName())).isFalse();      
	  }

	  @Test
	  public void testThatJobsWithoutTriggerAreNotLocated() {
	    final String givenToken = "token";

	    List<String> jobsToTrigger = getJobNameList(ComputedFolderWebHookTriggerLocator.locateJobsWithTrigger(computedFolderLocator, givenToken));

	   
	    assertThat(jobsToTrigger.contains(multibranchJobWithNoTrigger.getFullName())).isFalse();
	        
	  }
	  
	  
	  private WorkflowMultiBranchProject createJobWorkflowMultiBranchProjectWithouTrigger() {
			WorkflowMultiBranchProject mock = Mockito.mock(WorkflowMultiBranchProject.class);
			Mockito.when(mock.getFullName())
	    	.thenReturn("WorkflowMultiBranchProject_noTrigger");
		    return mock;
		}  
	private WorkflowMultiBranchProject createJobWorkflowMultiBranchProject(String token) {
		WorkflowMultiBranchProject mock = Mockito.mock(WorkflowMultiBranchProject.class);
		Mockito.when(mock.getFullName()).thenReturn("WorkflowMultiBranchProject_" + token);
	     Map<TriggerDescriptor, Trigger<?>> triggers = new HashMap<>();
	     TriggerDescriptor typeDescr = mock(TriggerDescriptor.class);
	     ComputedFolderWebHookTrigger trigger = new ComputedFolderWebHookTrigger(token);
	    triggers.put(typeDescr, trigger);
	    when(mock.getTriggers()).thenReturn(triggers);
	    return mock;
	}



	  private List<String> getJobNameList(List<LocatedComputedFolder>  jobsToTrigger) {
	    List<String> names = new ArrayList<>();
	    for ( LocatedComputedFolder locatedComputedFolder : jobsToTrigger)
	      names.add(locatedComputedFolder.getFullName());
	    return names;
	  }

	  

}
