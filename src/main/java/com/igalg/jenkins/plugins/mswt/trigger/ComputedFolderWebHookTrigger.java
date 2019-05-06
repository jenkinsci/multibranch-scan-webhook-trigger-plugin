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
package com.igalg.jenkins.plugins.mswt.trigger;

import java.util.logging.Logger;

import org.kohsuke.stapler.DataBoundConstructor;

import com.cloudbees.hudson.plugins.folder.computed.ComputedFolder;

import hudson.Extension;
import hudson.model.Item;
import hudson.model.Queue;
import hudson.triggers.Trigger;
import hudson.triggers.TriggerDescriptor;


public class ComputedFolderWebHookTrigger extends Trigger<ComputedFolder<?>> {

    private static final Logger LOGGER = Logger.getLogger(ComputedFolderWebHookTrigger.class.getName());

    

    
    private final String token;

    
    public String getToken() {
		return token;
	}



    @DataBoundConstructor
    public ComputedFolderWebHookTrigger(String token) {
        this.token = token;
        LOGGER.info("setting token:"+token);
        
    }

 
    public ComputedFolderWebHookTriggerResult trigger() {
    	Queue.Item  queueItem = job.scheduleBuild2(0);
    	return new ComputedFolderWebHookTriggerResult(queueItem);
    }
    
   
    
    

    /**
     * Our {@link hudson.model.Descriptor}
     */
    @Extension
    @SuppressWarnings("unused") // instantiated by Jenkins
    public static class DescriptorImpl extends TriggerDescriptor {
        /**
         * {@inheritDoc}
         */
        public boolean isApplicable(Item item) {
            return item instanceof ComputedFolder;
        }

        /**
         * {@inheritDoc}
         */
        public String getDisplayName() {
            return "Scan by webhook";
        }
    }


}
