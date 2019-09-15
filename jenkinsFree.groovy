#!groovy

// imports
import hudson.plugins.git.*
import hudson.plugins.git.extensions.*
import hudson.plugins.git.extensions.impl.*
import jenkins.model.Jenkins
import hudson.triggers.*
import hudson.model.FreeStyleProject
//import hudson.triggers.TimerTrigger

// parameters
echo "Start Groovy.."
echo "Set Parameter for Job - Test Job"

def repository = "https://github.com/riXab/groovy-pipeline-scripting.git";
job = Jenkins.instance.createProject(FreeStyleProject, 'TestJob')
job.setDescription("Free style with Groovy - raw script")
job.displayName = 'SomeTestJob(TESTING groovy)'

job.scm = new hudson.plugins.git.GitSCM(repository)
//job.addTrigger(new TimerTrigger("* * * * *"))
def spec = "* * * * *";
hudson.triggers.TimerTrigger newCron = new hudson.triggers.TimerTrigger(spec);
newCron.start(job, true);
job.addTrigger(newCron);
//job.buildTrigger("my-groove")

def jdk = tool name: 'localJDK'
env.JAVA_HOME = "${jdk}"

withEnv(["PATH+MAVEN=${tool 'localMaven'}/bin"]) {
				bat 'mvn -B verify'
			}
 archive '**/*.war'


echo "Save Job"
job.save()
echo "Reload Job"
Jenkins.instance.reload()

//return this