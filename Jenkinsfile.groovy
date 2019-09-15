#!groovy

// imports
import hudson.plugins.git.*
import hudson.plugins.git.extensions.*
import hudson.plugins.git.extensions.impl.*
import jenkins.model.Jenkins

// parameters
echo "Start Groovy.."
echo "Set Parameter for Job - MyJob_3"
def jobParameters = [
  name:          'MyJob_4',
  description:   'Build of my STG environment : https://stg.mycompany.com',
  repository:    'https://github.com/riXab/groovy-pipeline-scripting.git',
  branch:        'master'
]

// define repo configuration
def branchConfig                =   [new BranchSpec(jobParameters.branch)]
def userConfig                  =   [new UserRemoteConfig(jobParameters.repository, null, null, jobParameters.credentialId)]
//def userConfig                  =   [new UserRemoteConfig(jobParameters.repository, null, null, /*jobParameters.credentialId*/null)]
//def cleanBeforeCheckOutConfig   =   new CleanBeforeCheckout()
//def sparseCheckoutPathConfig    =   new SparseCheckoutPaths([new SparseCheckoutPath("Jenkinsfile")])
//def cloneConfig                 =   new CloneOption(true, true, null, 3)
//def extensionsConfig            =   [cleanBeforeCheckOutConfig,sparseCheckoutPathConfig,cloneConfig]
def scm                         =   new GitSCM(userConfig, branchConfig, false, [], null, null, null)
//def scm                         =   new GitSCM(userConfig, branchConfig, false, [], null, null, /*extensionsConfig*/null)

echo "Define flow"
// define SCM flow
def flowDefinition = new org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition(scm, "Jenkinsfile")

// set lightweight checkout
flowDefinition.setLightweight(true)

// get Jenkins instance
Jenkins jenkins = Jenkins.getInstance()

echo "Create Job"
// create the job
def job = new org.jenkinsci.plugins.workflow.job.WorkflowJob(jenkins,jobParameters.name)

echo "Define Job"
// define job type
job.definition = flowDefinition

// set job description
job.setDescription(jobParameters.description)

echo "Save Job"
// save to disk
jenkins.save()

echo "Reload Job"
jenkins.reload()