#!groovy

// imports
import hudson.plugins.git.*
import hudson.plugins.git.extensions.*
import hudson.plugins.git.extensions.impl.*
import jenkins.model.Jenkins
import hudson.model.FreeStyleProject

// parameters
echo "Start Groovy.."
echo "Set Parameter for Job - Test Job"

def repository = "https://github.com/riXab/groovy-pipeline-scripting.git";
job = Jenkins.instance.createProject(FreeStyleProject, 'TestJob')
job.setDescription("Free style with Groovy - raw script")
job.displayName = 'SomeTestJob(TESTING groovy)'

job.scm = new hudson.plugins.git.GitSCM(repository)


echo "Save Job"
job.save()
echo "Reload Job"
//job.reload()

//return this