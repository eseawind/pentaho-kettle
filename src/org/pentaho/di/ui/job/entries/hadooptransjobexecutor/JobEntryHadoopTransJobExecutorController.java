/*
 * Copyright (c) 2011 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the GNU Lesser General Public License, Version 2.1. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.gnu.org/licenses/lgpl-2.1.txt. The Original Code is Pentaho 
 * Data Integration.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the GNU Lesser Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
 */
package org.pentaho.di.ui.job.entries.hadooptransjobexecutor;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.util.StringUtil;
import org.pentaho.di.core.vfs.KettleVFS;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.job.entries.hadooptransjobexecutor.JobEntryHadoopTransJobExecutor;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.ui.core.PropsUI;
import org.pentaho.di.ui.core.gui.WindowProperty;
import org.pentaho.di.ui.job.entries.hadoopjobexecutor.UserDefinedItem;
import org.pentaho.di.ui.repository.dialog.SelectObjectDialog;
import org.pentaho.ui.xul.components.XulMenuList;
import org.pentaho.ui.xul.components.XulTextbox;
import org.pentaho.ui.xul.containers.XulDialog;
import org.pentaho.ui.xul.impl.AbstractXulEventHandler;
import org.pentaho.ui.xul.util.AbstractModelList;

public class JobEntryHadoopTransJobExecutorController extends AbstractXulEventHandler {

  private static final Class<?> PKG = JobEntryHadoopTransJobExecutor.class;

  public static final String JOB_ENTRY_NAME = "jobEntryName"; //$NON-NLS-1$
  public static final String HADOOP_JOB_NAME = "hadoopJobName"; //$NON-NLS-1$
  public static final String MAP_TRANS = "mapTrans"; //$NON-NLS-1$
  public static final String COMBINER_TRANS = "combinerTrans"; //$NON-NLS-1$
  public static final String REDUCE_TRANS = "reduceTrans"; //$NON-NLS-1$

  public static final String MAP_TRANS_INPUT_STEP_NAME = "mapTransInputStepName"; //$NON-NLS-1$
  public static final String MAP_TRANS_OUTPUT_STEP_NAME = "mapTransOutputStepName"; //$NON-NLS-1$
  public static final String COMBINER_TRANS_INPUT_STEP_NAME = "combinerTransInputStepName"; //$NON-NLS-1$
  public static final String COMBINER_TRANS_OUTPUT_STEP_NAME = "combinerTransOutputStepName"; //$NON-NLS-1$
  public static final String REDUCE_TRANS_INPUT_STEP_NAME = "reduceTransInputStepName"; //$NON-NLS-1$
  public static final String REDUCE_TRANS_OUTPUT_STEP_NAME = "reduceTransOutputStepName"; //$NON-NLS-1$

  public static final String OUTPUT_KEY_CLASS = "outputKeyClass"; //$NON-NLS-1$
  public static final String OUTPUT_VALUE_CLASS = "outputValueClass"; //$NON-NLS-1$
  public static final String INPUT_FORMAT_CLASS = "inputFormatClass"; //$NON-NLS-1$
  public static final String OUTPUT_FORMAT_CLASS = "outputFormatClass"; //$NON-NLS-1$
  public static final String WORKING_DIRECTORY = "workingDirectory"; //$NON-NLS-1$
  public static final String INPUT_PATH = "inputPath"; //$NON-NLS-1$
  public static final String OUTPUT_PATH = "outputPath"; //$NON-NLS-1$
  public static final String BLOCKING = "blocking"; //$NON-NLS-1$
  public static final String LOGGING_INTERVAL = "loggingInterval"; //$NON-NLS-1$
  public static final String HDFS_HOSTNAME = "hdfsHostname"; //$NON-NLS-1$
  public static final String HDFS_PORT = "hdfsPort"; //$NON-NLS-1$
  public static final String JOB_TRACKER_HOSTNAME = "jobTrackerHostname"; //$NON-NLS-1$
  public static final String JOB_TRACKER_PORT = "jobTrackerPort"; //$NON-NLS-1$
  public static final String NUM_MAP_TASKS = "numMapTasks"; //$NON-NLS-1$
  public static final String NUM_REDUCE_TASKS = "numReduceTasks"; //$NON-NLS-1$

  public static final String USER_DEFINED = "userDefined"; //$NON-NLS-1$

  public static final String MAPPER_STORAGE_TYPE = "mapperStorageType"; //$NON-NLS-1$
  public static final String COMBINER_STORAGE_TYPE = "combinerStorageType"; //$NON-NLS-1$
  public static final String REDUCER_STORAGE_TYPE = "reducerStorageType"; //$NON-NLS-1$

  private String jobEntryName;
  private String hadoopJobName;

  private String outputKeyClass;
  private String outputValueClass;
  private String inputFormatClass;
  private String outputFormatClass;

  private String workingDirectory;
  private String hdfsHostname;
  private String hdfsPort;
  private String jobTrackerHostname;
  private String jobTrackerPort;
  private String inputPath;
  private String outputPath;

  private int numMapTasks = 1;
  private int numReduceTasks = 1;

  private boolean blocking;
  private int loggingInterval = 60;

  private String mapRepositoryDir = "";
  private String mapRepositoryFile = "";
  private ObjectId mapRepositoryReference;
  private String mapTrans = "";
  
  private String combinerRepositoryDir = "";
  private String combinerRepositoryFile = "";
  private ObjectId combinerRepositoryReference;
  private String combinerTrans = "";
  
  private String reduceRepositoryDir = "";
  private String reduceRepositoryFile = "";
  private ObjectId reduceRepositoryReference;
  private String reduceTrans = "";

  private String mapTransInputStepName = "";
  private String mapTransOutputStepName = "";
  private String combinerTransInputStepName = "";
  private String combinerTransOutputStepName = "";
  private String reduceTransInputStepName = "";
  private String reduceTransOutputStepName = "";

  private String mapperStorageType = "";
  private String combinerStorageType = "";
  private String reducerStorageType = "";

  private Shell shell;
  private Repository rep;

  private JobEntryHadoopTransJobExecutor jobEntry;

  private AbstractModelList<UserDefinedItem> userDefined = new AbstractModelList<UserDefinedItem>();

  public JobEntryHadoopTransJobExecutorController() throws Throwable {
  }

  public void accept() {

    String validationErrors = "";
    if (StringUtil.isEmpty(jobEntryName)) {
      validationErrors += BaseMessages.getString(PKG, "JobEntryHadoopTransJobExecutor.JobEntryName.Error") + "\n";
    }
    if (StringUtil.isEmpty(hadoopJobName)) {
      validationErrors += BaseMessages.getString(PKG, "JobEntryHadoopTransJobExecutor.HadoopJobName.Error") + "\n";
    }
    if (numMapTasks <= 0) {
      validationErrors += BaseMessages.getString(PKG, "JobEntryHadoopTransJobExecutor.NumMapTasks.Error") + "\n";
    }

    if (!StringUtil.isEmpty(validationErrors)) {
      openErrorDialog(BaseMessages.getString(PKG, "Dialog.Error"), validationErrors);
      // show validation errors dialog
      return;
    }

    // common/simple
    jobEntry.setName(jobEntryName);
    jobEntry.setHadoopJobName(hadoopJobName);

    // Save only one method of accessing the transformation
    if (mapRepositoryReference != null) {
      jobEntry.setMapRepositoryReference(mapRepositoryReference);
      jobEntry.setMapRepositoryDir(null);
      jobEntry.setMapRepositoryFile(null);
      jobEntry.setMapTrans(null);
    } else if (!Const.isEmpty(mapRepositoryDir) && !Const.isEmpty(mapRepositoryFile)) {
      jobEntry.setMapRepositoryDir(mapRepositoryDir);
      jobEntry.setMapRepositoryFile(mapRepositoryFile);
      jobEntry.setMapRepositoryReference(null);
      jobEntry.setMapTrans(null);
    } else {
      jobEntry.setMapTrans(mapTrans);
      jobEntry.setMapRepositoryDir(null);
      jobEntry.setMapRepositoryFile(null);
      jobEntry.setMapRepositoryReference(null);
    }

    jobEntry.setMapInputStepName(mapTransInputStepName);
    jobEntry.setMapOutputStepName(mapTransOutputStepName);

    // Save only one method of accessing the transformation
    if (combinerRepositoryReference != null) {
      jobEntry.setCombinerRepositoryReference(combinerRepositoryReference);
      jobEntry.setCombinerRepositoryDir(null);
      jobEntry.setCombinerRepositoryFile(null);
      jobEntry.setCombinerTrans(null);
    } else if (!Const.isEmpty(combinerRepositoryDir) && !Const.isEmpty(combinerRepositoryFile)) {
      jobEntry.setCombinerRepositoryDir(combinerRepositoryDir);
      jobEntry.setCombinerRepositoryFile(combinerRepositoryFile);
      jobEntry.setCombinerRepositoryReference(null);
      jobEntry.setCombinerTrans(null);
    } else {
      jobEntry.setCombinerTrans(combinerTrans);
      jobEntry.setCombinerRepositoryDir(null);
      jobEntry.setCombinerRepositoryFile(null);
      jobEntry.setCombinerRepositoryReference(null);
    }

    jobEntry.setCombinerInputStepName(combinerTransInputStepName);
    jobEntry.setCombinerOutputStepName(combinerTransOutputStepName);    
    
    
    // Save only one method of accessing the transformation
    if (reduceRepositoryReference != null) {
      jobEntry.setReduceRepositoryReference(reduceRepositoryReference);
      jobEntry.setReduceRepositoryDir(null);
      jobEntry.setReduceRepositoryFile(null);
      jobEntry.setReduceTrans(null);
    } else if (!Const.isEmpty(reduceRepositoryDir) && !Const.isEmpty(reduceRepositoryFile)) {
      jobEntry.setReduceRepositoryDir(reduceRepositoryDir);
      jobEntry.setReduceRepositoryFile(reduceRepositoryFile);
      jobEntry.setReduceRepositoryReference(null);
      jobEntry.setReduceTrans(null);
    } else {
      jobEntry.setReduceTrans(reduceTrans);
      jobEntry.setReduceRepositoryDir(null);
      jobEntry.setReduceRepositoryFile(null);
      jobEntry.setReduceRepositoryReference(null);
    }

    jobEntry.setReduceInputStepName(reduceTransInputStepName);
    jobEntry.setReduceOutputStepName(reduceTransOutputStepName);
    // advanced config
    jobEntry.setBlocking(isBlocking());
    jobEntry.setLoggingInterval(loggingInterval);
    jobEntry.setInputPath(getInputPath());
    jobEntry.setInputFormatClass(getInputFormatClass());
    jobEntry.setOutputPath(getOutputPath());
    jobEntry.setOutputKeyClass(getOutputKeyClass());
    jobEntry.setOutputValueClass(getOutputValueClass());
    jobEntry.setOutputFormatClass(getOutputFormatClass());
    jobEntry.setHdfsHostname(getHdfsHostname());
    jobEntry.setHdfsPort(getHdfsPort());
    jobEntry.setJobTrackerHostname(getJobTrackerHostname());
    jobEntry.setJobTrackerPort(getJobTrackerPort());
    jobEntry.setNumMapTasks(getNumMapTasks());
    jobEntry.setNumReduceTasks(getNumReduceTasks());
    jobEntry.setUserDefined(userDefined);
    jobEntry.setWorkingDirectory(getWorkingDirectory());

    jobEntry.setChanged();

    cancel();
  }

  public void init() throws Throwable {
    if (jobEntry != null) {
      // common/simple
      setName(jobEntry.getName());
      setJobEntryName(jobEntry.getName());
      setHadoopJobName(jobEntry.getHadoopJobName());

      if (rep == null) {
        ((XulMenuList) getXulDomContainer().getDocumentRoot().getElementById("mapper-storage-type")).setDisabled(true);
        ((XulMenuList) getXulDomContainer().getDocumentRoot().getElementById("combiner-storage-type")).setDisabled(true);
        ((XulMenuList) getXulDomContainer().getDocumentRoot().getElementById("reducer-storage-type")).setDisabled(true);
      }

      // Load the map transformation into the UI
      if (jobEntry.getMapTrans() != null || rep == null) {
        ((XulMenuList) getXulDomContainer().getDocumentRoot().getElementById("mapper-storage-type")).setSelectedIndex(0);
        setMapTrans(jobEntry.getMapTrans());
      } else if (jobEntry.getMapRepositoryReference() != null) {
        ((XulMenuList) getXulDomContainer().getDocumentRoot().getElementById("mapper-storage-type")).setSelectedIndex(2);
        setMapRepositoryReference(jobEntry.getMapRepositoryReference());
        // Load the repository directory and file for displaying to the user
        try {
          TransMeta transMeta = rep.loadTransformation(getMapRepositoryReference(), null);
          if (transMeta != null && transMeta.getRepositoryDirectory() != null) {
            setMapTrans(buildRepositoryPath(transMeta.getRepositoryDirectory().getPath(), transMeta.getName()));
          }
        } catch (KettleException e) {
          // The transformation cannot be loaded from the repository
          setMapRepositoryReference(null);
        }
      } else {
        ((XulMenuList) getXulDomContainer().getDocumentRoot().getElementById("mapper-storage-type")).setSelectedIndex(1);
        setMapRepositoryDir(jobEntry.getMapRepositoryDir());
        setMapRepositoryFile(jobEntry.getMapRepositoryFile());
        setMapTrans(buildRepositoryPath(getMapRepositoryDir(), getMapRepositoryFile()));
      }
      setMapTransInputStepName(jobEntry.getMapInputStepName());
      setMapTransOutputStepName(jobEntry.getMapOutputStepName());

      // Load the combiner transformation into the UI
      if (jobEntry.getCombinerTrans() != null || rep == null) {
        ((XulMenuList) getXulDomContainer().getDocumentRoot().getElementById("combiner-storage-type")).setSelectedIndex(0);
        setCombinerTrans(jobEntry.getCombinerTrans());
      } else if (jobEntry.getCombinerRepositoryReference() != null) {
        ((XulMenuList) getXulDomContainer().getDocumentRoot().getElementById("combiner-storage-type")).setSelectedIndex(2);
        setCombinerRepositoryReference(jobEntry.getCombinerRepositoryReference());
        // Load the repository directory and file for displaying to the user
        try {
          TransMeta transMeta = rep.loadTransformation(getCombinerRepositoryReference(), null);
          if (transMeta != null && transMeta.getRepositoryDirectory() != null) {
            setCombinerTrans(buildRepositoryPath(transMeta.getRepositoryDirectory().getPath(), transMeta.getName()));
          }
        } catch (KettleException e) {
          // The transformation cannot be loaded from the repository
          setCombinerRepositoryReference(null);
        }
      } else {
        ((XulMenuList) getXulDomContainer().getDocumentRoot().getElementById("combiner-storage-type")).setSelectedIndex(1);
        setCombinerRepositoryDir(jobEntry.getCombinerRepositoryDir());
        setCombinerRepositoryFile(jobEntry.getCombinerRepositoryFile());
        setCombinerTrans(buildRepositoryPath(getCombinerRepositoryDir(), getCombinerRepositoryFile()));
      }

      setCombinerTransInputStepName(jobEntry.getCombinerInputStepName());
      setCombinerTransOutputStepName(jobEntry.getCombinerOutputStepName());
      
      // Load the reduce transformation into the UI
      if (jobEntry.getReduceTrans() != null || rep == null) {
        ((XulMenuList) getXulDomContainer().getDocumentRoot().getElementById("reducer-storage-type")).setSelectedIndex(0);
        setReduceTrans(jobEntry.getReduceTrans());
      } else if (jobEntry.getReduceRepositoryReference() != null) {
        ((XulMenuList) getXulDomContainer().getDocumentRoot().getElementById("reducer-storage-type")).setSelectedIndex(2);
        setReduceRepositoryReference(jobEntry.getReduceRepositoryReference());
        // Load the repository directory and file for displaying to the user
        try {
          TransMeta transMeta = rep.loadTransformation(getReduceRepositoryReference(), null);
          if (transMeta != null && transMeta.getRepositoryDirectory() != null) {
            setReduceTrans(buildRepositoryPath(transMeta.getRepositoryDirectory().getPath(), transMeta.getName()));
          }
        } catch (KettleException e) {
          // The transformation cannot be loaded from the repository
          setReduceRepositoryReference(null);
        }
      } else {
        ((XulMenuList) getXulDomContainer().getDocumentRoot().getElementById("reducer-storage-type")).setSelectedIndex(1);
        setReduceRepositoryDir(jobEntry.getReduceRepositoryDir());
        setReduceRepositoryFile(jobEntry.getReduceRepositoryFile());
        setReduceTrans(buildRepositoryPath(getReduceRepositoryDir(), getReduceRepositoryFile()));
      }

      setReduceTransInputStepName(jobEntry.getReduceInputStepName());
      setReduceTransOutputStepName(jobEntry.getReduceOutputStepName());
      
      userDefined.clear();
      if (jobEntry.getUserDefined() != null) {
        userDefined.addAll(jobEntry.getUserDefined());
      }
      setBlocking(jobEntry.isBlocking());
      setLoggingInterval(jobEntry.getLoggingInterval());
      setInputPath(jobEntry.getInputPath());
      setInputFormatClass(jobEntry.getInputFormatClass());
      setOutputPath(jobEntry.getOutputPath());
      setOutputKeyClass(jobEntry.getOutputKeyClass());
      setOutputValueClass(jobEntry.getOutputValueClass());
      setOutputFormatClass(jobEntry.getOutputFormatClass());
      setHdfsHostname(jobEntry.getHdfsHostname());
      setHdfsPort(jobEntry.getHdfsPort());
      setJobTrackerHostname(jobEntry.getJobTrackerHostname());
      setJobTrackerPort(jobEntry.getJobTrackerPort());
      setNumMapTasks(jobEntry.getNumMapTasks());
      setNumReduceTasks(jobEntry.getNumReduceTasks());
      setWorkingDirectory(jobEntry.getWorkingDirectory());
    }
  }

  public void closeErrorDialog() {
    XulDialog errorDialog = (XulDialog) getXulDomContainer().getDocumentRoot().getElementById("hadoop-error-dialog");
    errorDialog.hide();
  }

  /**
   * This method exists for consistency
   * 
   * @param dir
   *          Null is unacceptable input, a blank string will be returned
   * @param file
   *          Null is unacceptable input, a blank string will be returned
   * @return
   */
  private String buildRepositoryPath(String dir, String file) {
    if (dir == null || file == null) {
      return "";
    }

    if (dir.endsWith("/")) {
      return dir + file;
    }

    return dir + "/" + file;
  }

  public void setShell(Shell shell) {
    this.shell = shell;
  }

  public void setRepository(Repository rep) {
    this.rep = rep;
  }

  public void cancel() {
    XulDialog xulDialog = (XulDialog) getXulDomContainer().getDocumentRoot().getElementById("job-entry-dialog");

    Shell shell = (Shell) xulDialog.getRootObject();
    if (!shell.isDisposed()) {
      WindowProperty winprop = new WindowProperty(shell);
      PropsUI.getInstance().setScreen(winprop);
      ((Composite) xulDialog.getManagedObject()).dispose();
      shell.dispose();
    }
  }

  private interface StringResultSetter {
    public void set(String val);
  }

  private interface ObjectIdResultSetter {
    public void set(ObjectId val);
  }

  public void mapTransBrowse() {
    if (getMapperStorageType().equalsIgnoreCase("local")) { //$NON-NLS-1$
      browseLocalFilesystem(new StringResultSetter() {
        @Override
        public void set(String val) {
          JobEntryHadoopTransJobExecutorController.this.setMapTrans(val);
          JobEntryHadoopTransJobExecutorController.this.setMapRepositoryDir(null);
          JobEntryHadoopTransJobExecutorController.this.setMapRepositoryFile(null);
          JobEntryHadoopTransJobExecutorController.this.setMapRepositoryReference(null);
        }
      }, mapTrans);
    } else if (getMapperStorageType().equalsIgnoreCase("repository")) { //$NON-NLS-1$
      browseRepository(new StringResultSetter() {
        public void set(String val) {
          JobEntryHadoopTransJobExecutorController.this.setMapTrans(val);
          JobEntryHadoopTransJobExecutorController.this.setMapRepositoryReference(null);
        }
      }, new StringResultSetter() {
        public void set(String val) {
          JobEntryHadoopTransJobExecutorController.this.setMapRepositoryDir(val);
        }
      }, new StringResultSetter() {
        public void set(String val) {
          JobEntryHadoopTransJobExecutorController.this.setMapRepositoryFile(val);
        }
      });
    } else if (getMapperStorageType().equalsIgnoreCase("reference")) { //$NON-NLS-1$
      browseRepository(new StringResultSetter() {
        public void set(String val) {
          JobEntryHadoopTransJobExecutorController.this.setMapTrans(val);
          JobEntryHadoopTransJobExecutorController.this.setMapRepositoryDir(null);
          JobEntryHadoopTransJobExecutorController.this.setMapRepositoryFile(null);
        }
      }, new ObjectIdResultSetter() {
        public void set(ObjectId val) {
          JobEntryHadoopTransJobExecutorController.this.setMapRepositoryReference(val);
        }
      });
    }
  }

  public void combinerTransBrowse() {
    if (getCombinerStorageType().equalsIgnoreCase("local")) { //$NON-NLS-1$
      browseLocalFilesystem(new StringResultSetter() {
        @Override
        public void set(String val) {
          JobEntryHadoopTransJobExecutorController.this.setCombinerTrans(val);
          JobEntryHadoopTransJobExecutorController.this.setCombinerRepositoryDir(null);
          JobEntryHadoopTransJobExecutorController.this.setCombinerRepositoryFile(null);
          JobEntryHadoopTransJobExecutorController.this.setCombinerRepositoryReference(null);
        }
      }, combinerTrans);
    } else if (getCombinerStorageType().equalsIgnoreCase("repository")) { //$NON-NLS-1$
      browseRepository(new StringResultSetter() {
        public void set(String val) {
          JobEntryHadoopTransJobExecutorController.this.setCombinerTrans(val);
          JobEntryHadoopTransJobExecutorController.this.setCombinerRepositoryReference(null);
        }
      }, new StringResultSetter() {
        public void set(String val) {
          JobEntryHadoopTransJobExecutorController.this.setCombinerRepositoryDir(val);
        }
      }, new StringResultSetter() {
        public void set(String val) {
          JobEntryHadoopTransJobExecutorController.this.setCombinerRepositoryFile(val);
        }
      });
    } else if (getCombinerStorageType().equalsIgnoreCase("reference")) { //$NON-NLS-1$
      browseRepository(new StringResultSetter() {
        public void set(String val) {
          JobEntryHadoopTransJobExecutorController.this.setCombinerTrans(val);
          JobEntryHadoopTransJobExecutorController.this.setCombinerRepositoryDir(null);
          JobEntryHadoopTransJobExecutorController.this.setCombinerRepositoryFile(null);
        }
      }, new ObjectIdResultSetter() {
        public void set(ObjectId val) {
          JobEntryHadoopTransJobExecutorController.this.setCombinerRepositoryReference(val);
        }
      });
    }
  }  
  
  public void reduceTransBrowse() {
    if (getReducerStorageType().equalsIgnoreCase("local")) { //$NON-NLS-1$
      browseLocalFilesystem(new StringResultSetter() {
        @Override
        public void set(String val) {
          JobEntryHadoopTransJobExecutorController.this.setReduceTrans(val);
          JobEntryHadoopTransJobExecutorController.this.setReduceRepositoryDir(null);
          JobEntryHadoopTransJobExecutorController.this.setReduceRepositoryFile(null);
          JobEntryHadoopTransJobExecutorController.this.setReduceRepositoryReference(null);
        }
      }, reduceTrans);
    } else if (getReducerStorageType().equalsIgnoreCase("repository")) { //$NON-NLS-1$
      browseRepository(new StringResultSetter() {
        public void set(String val) {
          JobEntryHadoopTransJobExecutorController.this.setReduceTrans(val);
          JobEntryHadoopTransJobExecutorController.this.setReduceRepositoryReference(null);
        }
      }, new StringResultSetter() {
        public void set(String val) {
          JobEntryHadoopTransJobExecutorController.this.setReduceRepositoryDir(val);
        }
      }, new StringResultSetter() {
        public void set(String val) {
          JobEntryHadoopTransJobExecutorController.this.setReduceRepositoryFile(val);
        }
      });
    } else if (getReducerStorageType().equalsIgnoreCase("reference")) { //$NON-NLS-1$
      browseRepository(new StringResultSetter() {
        public void set(String val) {
          JobEntryHadoopTransJobExecutorController.this.setReduceTrans(val);
          JobEntryHadoopTransJobExecutorController.this.setReduceRepositoryDir(null);
          JobEntryHadoopTransJobExecutorController.this.setReduceRepositoryFile(null);
        }
      }, new ObjectIdResultSetter() {
        public void set(ObjectId val) {
          JobEntryHadoopTransJobExecutorController.this.setReduceRepositoryReference(val);
        }
      });
    }
  }

  public void browseLocalFilesystem(StringResultSetter setter, String originalTransformationName) {
    XulDialog xulDialog = (XulDialog) getXulDomContainer().getDocumentRoot().getElementById("job-entry-dialog");
    Shell shell = (Shell) xulDialog.getRootObject();

    FileDialog dialog = new FileDialog(shell, SWT.OPEN);
    dialog.setFilterExtensions(Const.STRING_TRANS_FILTER_EXT);
    dialog.setFilterNames(Const.getTransformationFilterNames());
    String prevName = jobEntry.environmentSubstitute(originalTransformationName);
    String parentFolder = null;
    try {
      parentFolder = KettleVFS.getFilename(KettleVFS.getFileObject(jobEntry.environmentSubstitute(jobEntry.getFilename())).getParent());
    } catch (Exception e) {
      // not that important
    }
    if (!Const.isEmpty(prevName)) {
      try {
        if (KettleVFS.fileExists(prevName)) {
          dialog.setFilterPath(KettleVFS.getFilename(KettleVFS.getFileObject(prevName).getParent()));
        } else {

          if (!prevName.endsWith(".ktr")) {
            prevName = "${" + Const.INTERNAL_VARIABLE_JOB_FILENAME_DIRECTORY + "}/" + Const.trim(originalTransformationName) + ".ktr";
          }
          if (KettleVFS.fileExists(prevName)) {
            setter.set(prevName);
            return;
          }
        }
      } catch (Exception e) {
        dialog.setFilterPath(parentFolder);
      }
    } else if (!Const.isEmpty(parentFolder)) {
      dialog.setFilterPath(parentFolder);
    }

    String fname = dialog.open();
    if (fname != null) {
      File file = new File(fname);
      String name = file.getName();
      String parentFolderSelection = file.getParentFile().toString();

      if (!Const.isEmpty(parentFolder) && parentFolder.equals(parentFolderSelection)) {
        setter.set("${" + Const.INTERNAL_VARIABLE_JOB_FILENAME_DIRECTORY + "}/" + name);
      } else {
        setter.set(fname);
      }
    }
  }

  public void browseRepository(StringResultSetter transSetter, StringResultSetter repoDirSetter, StringResultSetter repoFileSetter) {
    browseRepository(transSetter, repoDirSetter, repoFileSetter, null);
  }

  public void browseRepository(StringResultSetter transSetter, ObjectIdResultSetter repoReferenceSetter) {
    browseRepository(transSetter, null, null, repoReferenceSetter);
  }

  private void browseRepository(StringResultSetter transSetter, StringResultSetter repoDirSetter, StringResultSetter repoFileSetter,
      ObjectIdResultSetter repoReferenceSetter) {
    if (rep != null) {
      XulDialog xulDialog = (XulDialog) getXulDomContainer().getDocumentRoot().getElementById("job-entry-dialog");
      Shell shell = (Shell) xulDialog.getRootObject();
      SelectObjectDialog sod = new SelectObjectDialog(shell, rep, true, false);
      String transname = sod.open();
      if (transname != null) {
        // Both location and reference should have this for display purposes
        if (transSetter != null) {
          transSetter.set(buildRepositoryPath(sod.getDirectory().getPath(), sod.getObjectName()));
        }

        // Location should have these set
        if (repoDirSetter != null) {
          repoDirSetter.set(sod.getDirectory().getPath());
        }
        if (repoFileSetter != null) {
          repoFileSetter.set(transname);
        }

        // Reference should have this set
        if (repoReferenceSetter != null) {
          repoReferenceSetter.set(sod.getObjectId());
        }
      }
    }
  }

  public void newUserDefinedItem() {
    userDefined.add(new UserDefinedItem());
  }

  public AbstractModelList<UserDefinedItem> getUserDefined() {
    return userDefined;
  }

  @Override
  public String getName() {
    return "jobEntryController"; //$NON-NLS-1$
  }

  public String getJobEntryName() {
    return jobEntryName;
  }

  public void setJobEntryName(String jobEntryName) {
    String previousVal = this.jobEntryName;
    String newVal = jobEntryName;

    this.jobEntryName = jobEntryName;
    firePropertyChange(JobEntryHadoopTransJobExecutorController.JOB_ENTRY_NAME, previousVal, newVal);
  }

  public String getHadoopJobName() {
    return hadoopJobName;
  }

  public void setHadoopJobName(String hadoopJobName) {
    String previousVal = this.hadoopJobName;
    String newVal = hadoopJobName;

    this.hadoopJobName = hadoopJobName;
    firePropertyChange(JobEntryHadoopTransJobExecutorController.HADOOP_JOB_NAME, previousVal, newVal);
  }

  public String getMapTrans() {
    return mapTrans;
  }

  public void setMapTrans(String mapTrans) {
    String previousVal = this.mapTrans;
    String newVal = mapTrans;

    this.mapTrans = mapTrans;
    firePropertyChange(JobEntryHadoopTransJobExecutorController.MAP_TRANS, previousVal, newVal);
  }

  public String getCombinerTrans() {
    return combinerTrans;
  }

  public void setCombinerTrans(String combinerTrans) {
    String previousVal = this.combinerTrans;
    String newVal = combinerTrans;

    this.combinerTrans = combinerTrans;
    firePropertyChange(JobEntryHadoopTransJobExecutorController.COMBINER_TRANS, previousVal, newVal);
  }  
  
  public String getReduceTrans() {
    return reduceTrans;
  }

  public void setReduceTrans(String reduceTrans) {
    String previousVal = this.reduceTrans;
    String newVal = reduceTrans;

    this.reduceTrans = reduceTrans;
    firePropertyChange(JobEntryHadoopTransJobExecutorController.REDUCE_TRANS, previousVal, newVal);
  }

  public String getMapTransInputStepName() {
    return mapTransInputStepName;
  }

  public void setMapTransInputStepName(String mapTransInputStepName) {
    String previousVal = this.mapTransInputStepName;
    String newVal = mapTransInputStepName;

    this.mapTransInputStepName = mapTransInputStepName;
    firePropertyChange(JobEntryHadoopTransJobExecutorController.MAP_TRANS_INPUT_STEP_NAME, previousVal, newVal);
  }

  public String getMapTransOutputStepName() {
    return mapTransOutputStepName;
  }

  public void setMapTransOutputStepName(String mapTransOutputStepName) {
    String previousVal = this.mapTransOutputStepName;
    String newVal = mapTransOutputStepName;

    this.mapTransOutputStepName = mapTransOutputStepName;
    firePropertyChange(JobEntryHadoopTransJobExecutorController.MAP_TRANS_OUTPUT_STEP_NAME, previousVal, newVal);
  }
  
  public String getCombinerTransInputStepName() {
    return combinerTransInputStepName;
  }

  public void setCombinerTransInputStepName(String combinerTransInputStepName) {
    String previousVal = this.combinerTransInputStepName;
    String newVal = combinerTransInputStepName;

    this.combinerTransInputStepName = combinerTransInputStepName;
    firePropertyChange(JobEntryHadoopTransJobExecutorController.COMBINER_TRANS_INPUT_STEP_NAME, previousVal, newVal);
  }

  public String getCombinerTransOutputStepName() {
    return combinerTransOutputStepName;
  }

  public void setCombinerTransOutputStepName(String combinerTransOutputStepName) {
    String previousVal = this.combinerTransOutputStepName;
    String newVal = combinerTransOutputStepName;

    this.combinerTransOutputStepName = combinerTransOutputStepName;
    firePropertyChange(JobEntryHadoopTransJobExecutorController.COMBINER_TRANS_OUTPUT_STEP_NAME, previousVal, newVal);
  }  
  
  public String getReduceTransInputStepName() {
    return reduceTransInputStepName;
  }

  public void setReduceTransInputStepName(String reduceTransInputStepName) {
    String previousVal = this.reduceTransInputStepName;
    String newVal = reduceTransInputStepName;

    this.reduceTransInputStepName = reduceTransInputStepName;
    firePropertyChange(JobEntryHadoopTransJobExecutorController.REDUCE_TRANS_INPUT_STEP_NAME, previousVal, newVal);
  }

  public String getReduceTransOutputStepName() {
    return reduceTransOutputStepName;
  }

  public void setReduceTransOutputStepName(String reduceTransOutputStepName) {
    String previousVal = this.reduceTransOutputStepName;
    String newVal = reduceTransOutputStepName;

    this.reduceTransOutputStepName = reduceTransOutputStepName;
    firePropertyChange(JobEntryHadoopTransJobExecutorController.REDUCE_TRANS_OUTPUT_STEP_NAME, previousVal, newVal);
  }

  public void invertBlocking() {
    setBlocking(!isBlocking());
  }

  public JobEntryHadoopTransJobExecutor getJobEntry() {
    return jobEntry;
  }

  public void setJobEntry(JobEntryHadoopTransJobExecutor jobEntry) {
    this.jobEntry = jobEntry;
  }

  public String getOutputKeyClass() {
    return outputKeyClass;
  }

  public void setOutputKeyClass(String outputKeyClass) {
    String previousVal = this.outputKeyClass;
    String newVal = outputKeyClass;

    this.outputKeyClass = outputKeyClass;
    firePropertyChange(OUTPUT_KEY_CLASS, previousVal, newVal);
  }

  public String getOutputValueClass() {
    return outputValueClass;
  }

  public void setOutputValueClass(String outputValueClass) {
    String previousVal = this.outputValueClass;
    String newVal = outputValueClass;

    this.outputValueClass = outputValueClass;
    firePropertyChange(OUTPUT_VALUE_CLASS, previousVal, newVal);
  }

  public String getInputFormatClass() {
    return inputFormatClass;
  }

  public void setInputFormatClass(String inputFormatClass) {
    String previousVal = this.inputFormatClass;
    String newVal = inputFormatClass;

    this.inputFormatClass = inputFormatClass;
    firePropertyChange(INPUT_FORMAT_CLASS, previousVal, newVal);
  }

  public String getOutputFormatClass() {
    return outputFormatClass;
  }

  public void setOutputFormatClass(String outputFormatClass) {
    String previousVal = this.outputFormatClass;
    String newVal = outputFormatClass;

    this.outputFormatClass = outputFormatClass;
    firePropertyChange(OUTPUT_FORMAT_CLASS, previousVal, newVal);
  }

  public String getWorkingDirectory() {
    return workingDirectory;
  }

  public void setWorkingDirectory(String workingDirectory) {
    String previousVal = this.workingDirectory;
    String newVal = workingDirectory;

    this.workingDirectory = workingDirectory;
    firePropertyChange(WORKING_DIRECTORY, previousVal, newVal);
  }

  public String getHdfsHostname() {
    return hdfsHostname;
  }

  public void setHdfsHostname(String hdfsHostname) {
    String previousVal = this.hdfsHostname;
    String newVal = hdfsHostname;

    this.hdfsHostname = hdfsHostname;
    firePropertyChange(HDFS_HOSTNAME, previousVal, newVal);
  }

  public String getHdfsPort() {
    return hdfsPort;
  }

  public void setHdfsPort(String hdfsPort) {
    String previousVal = this.hdfsPort;
    String newVal = hdfsPort;

    this.hdfsPort = hdfsPort;
    firePropertyChange(HDFS_PORT, previousVal, newVal);
  }

  public String getJobTrackerHostname() {
    return jobTrackerHostname;
  }

  public void setJobTrackerHostname(String jobTrackerHostname) {
    String previousVal = this.jobTrackerHostname;
    String newVal = jobTrackerHostname;

    this.jobTrackerHostname = jobTrackerHostname;
    firePropertyChange(JOB_TRACKER_HOSTNAME, previousVal, newVal);
  }

  public String getJobTrackerPort() {
    return jobTrackerPort;
  }

  public void setJobTrackerPort(String jobTrackerPort) {
    String previousVal = this.jobTrackerPort;
    String newVal = jobTrackerPort;

    this.jobTrackerPort = jobTrackerPort;
    firePropertyChange(JOB_TRACKER_PORT, previousVal, newVal);
  }

  public String getInputPath() {
    return inputPath;
  }

  public void setInputPath(String inputPath) {
    String previousVal = this.inputPath;
    String newVal = inputPath;

    this.inputPath = inputPath;
    firePropertyChange(INPUT_PATH, previousVal, newVal);
  }

  public String getOutputPath() {
    return outputPath;
  }

  public void setOutputPath(String outputPath) {
    String previousVal = this.outputPath;
    String newVal = outputPath;

    this.outputPath = outputPath;
    firePropertyChange(OUTPUT_PATH, previousVal, newVal);
  }

  public boolean isBlocking() {
    return blocking;
  }

  public void setBlocking(boolean blocking) {
    boolean previousVal = this.blocking;
    boolean newVal = blocking;

    this.blocking = blocking;
    firePropertyChange(BLOCKING, previousVal, newVal);
  }

  public int getLoggingInterval() {
    return loggingInterval;
  }

  public void setLoggingInterval(int loggingInterval) {
    int previousVal = this.loggingInterval;
    int newVal = loggingInterval;

    this.loggingInterval = loggingInterval;
    firePropertyChange(LOGGING_INTERVAL, previousVal, newVal);
  }

  public int getNumMapTasks() {
    return numMapTasks;
  }

  public void setNumMapTasks(int numMapTasks) {
    int previousVal = this.numMapTasks;
    int newVal = numMapTasks;

    this.numMapTasks = numMapTasks;
    firePropertyChange(NUM_MAP_TASKS, previousVal, newVal);
  }

  public int getNumReduceTasks() {
    return numReduceTasks;
  }

  public void setNumReduceTasks(int numReduceTasks) {
    int previousVal = this.numReduceTasks;
    int newVal = numReduceTasks;

    this.numReduceTasks = numReduceTasks;
    firePropertyChange(NUM_REDUCE_TASKS, previousVal, newVal);
  }

  public void setMapperStorageType(String mapperStorageType) {
    switch (((XulMenuList) getXulDomContainer().getDocumentRoot().getElementById("mapper-storage-type")).getSelectedIndex()) {
    case 0: { // Local
      mapperStorageTypeChanged("local");
    }
      break;

    case 1: { // By name
      mapperStorageTypeChanged("repository");
    }
      break;

    case 2: { // By reference
      mapperStorageTypeChanged("reference");
    }
      break;
    }

    firePropertyChange(MAPPER_STORAGE_TYPE, null, mapperStorageType);
  }

  public String getMapperStorageType() {
    return mapperStorageType;
  }
  
  private void mapperStorageTypeChanged(String newStorageType) {
    // Only execute this code if the storage type has changed
    if (!this.mapperStorageType.equals(newStorageType)) {
      this.mapperStorageType = newStorageType;

      // Disable the text box?
      if (this.mapperStorageType.equals("reference")) {
        ((XulTextbox) getXulDomContainer().getDocumentRoot().getElementById("jobentry-map-transformation")).setReadonly(true);
      } else {
        ((XulTextbox) getXulDomContainer().getDocumentRoot().getElementById("jobentry-map-transformation")).setReadonly(false);
      }

      // Clear current settings
      setMapRepositoryDir(null);
      setMapRepositoryFile(null);
      setMapRepositoryReference(null);
      setMapTrans("");
    }
  }

  

  
  public void setCombinerStorageType(String mapperStorageType) {
    switch (((XulMenuList) getXulDomContainer().getDocumentRoot().getElementById("combiner-storage-type")).getSelectedIndex()) {
    case 0: { // Local
      combinerStorageTypeChanged("local");
    }
      break;

    case 1: { // By name
      combinerStorageTypeChanged("repository");
    }
      break;

    case 2: { // By reference
      combinerStorageTypeChanged("reference");
    }
      break;
    }

    firePropertyChange(COMBINER_STORAGE_TYPE, null, combinerStorageType);
  }

  public String getCombinerStorageType() {
    return combinerStorageType;
  }
  
  private void combinerStorageTypeChanged(String newStorageType) {
    // Only execute this code if the storage type has changed
    if (!this.combinerStorageType.equals(newStorageType)) {
      this.combinerStorageType = newStorageType;

      // Disable the text box?
      if (this.combinerStorageType.equals("reference")) {
        ((XulTextbox) getXulDomContainer().getDocumentRoot().getElementById("jobentry-combiner-transformation")).setReadonly(true);
      } else {
        ((XulTextbox) getXulDomContainer().getDocumentRoot().getElementById("jobentry-combiner-transformation")).setReadonly(false);
      }

      // Clear current settings
      setCombinerRepositoryDir(null);
      setCombinerRepositoryFile(null);
      setCombinerRepositoryReference(null);
      setCombinerTrans("");
    }
  }
  
  public void setReducerStorageType(String reducerStorageType) {
    switch (((XulMenuList) getXulDomContainer().getDocumentRoot().getElementById("reducer-storage-type")).getSelectedIndex()) {
    case 0: { // Local
      reducerStorageTypeChanged("local");
    }
      break;

    case 1: { // By name
      reducerStorageTypeChanged("repository");
    }
      break;

    case 2: { // By reference
      reducerStorageTypeChanged("reference");
    }
      break;
    }

    firePropertyChange(REDUCER_STORAGE_TYPE, null, reducerStorageType);
  }

  public String getReducerStorageType() {
    return reducerStorageType;
  }

  private void reducerStorageTypeChanged(String newStorageType) {
    // Only execute this code if the storage type has changed
    if (!this.reducerStorageType.equals(newStorageType)) {
      this.reducerStorageType = newStorageType;

      // Disable the text box?
      if (this.reducerStorageType.equals("reference")) {
        ((XulTextbox) getXulDomContainer().getDocumentRoot().getElementById("jobentry-reduce-transformation")).setReadonly(true);
      } else {
        ((XulTextbox) getXulDomContainer().getDocumentRoot().getElementById("jobentry-reduce-transformation")).setReadonly(false);
      }

      // Clear current settings
      setReduceRepositoryDir(null);
      setReduceRepositoryFile(null);
      setReduceRepositoryReference(null);
      setReduceTrans("");
    }
  }

  public String getMapRepositoryDir() {
    return mapRepositoryDir;
  }

  public void setMapRepositoryDir(String mapRepositoryDir) {
    this.mapRepositoryDir = mapRepositoryDir;
  }

  public String getMapRepositoryFile() {
    return mapRepositoryFile;
  }

  public void setMapRepositoryFile(String mapRepositoryFile) {
    this.mapRepositoryFile = mapRepositoryFile;
  }

  public ObjectId getMapRepositoryReference() {
    return mapRepositoryReference;
  }

  public void setMapRepositoryReference(ObjectId mapRepositoryReference) {
    this.mapRepositoryReference = mapRepositoryReference;
  }
  
  public String getCombinerRepositoryDir() {
    return combinerRepositoryDir;
  }

  public void setCombinerRepositoryDir(String combinerRepositoryDir) {
    this.combinerRepositoryDir = combinerRepositoryDir;
  }

  public String getCombinerRepositoryFile() {
    return combinerRepositoryFile;
  }

  public void setCombinerRepositoryFile(String combinerRepositoryFile) {
    this.combinerRepositoryFile = combinerRepositoryFile;
  }

  public ObjectId getCombinerRepositoryReference() {
    return combinerRepositoryReference;
  }

  public void setCombinerRepositoryReference(ObjectId combinerRepositoryReference) {
    this.combinerRepositoryReference = combinerRepositoryReference;
  }  
  
  public String getReduceRepositoryDir() {
    return reduceRepositoryDir;
  }

  public void setReduceRepositoryDir(String reduceRepositoryDir) {
    this.reduceRepositoryDir = reduceRepositoryDir;
  }

  public String getReduceRepositoryFile() {
    return reduceRepositoryFile;
  }

  public void setReduceRepositoryFile(String reduceRepositoryFile) {
    this.reduceRepositoryFile = reduceRepositoryFile;
  }

  public ObjectId getReduceRepositoryReference() {
    return reduceRepositoryReference;
  }

  public void setReduceRepositoryReference(ObjectId reduceRepositoryReference) {
    this.reduceRepositoryReference = reduceRepositoryReference;
  }

  public void openErrorDialog(String title, String message) {
    XulDialog errorDialog = (XulDialog) getXulDomContainer().getDocumentRoot().getElementById("hadoop-error-dialog");
    errorDialog.setTitle(title);

    XulTextbox errorMessage = (XulTextbox) getXulDomContainer().getDocumentRoot().getElementById("hadoop-error-message");
    errorMessage.setValue(message);

    errorDialog.show();
  }

}