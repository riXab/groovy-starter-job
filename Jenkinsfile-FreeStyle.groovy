import com.cloudbees.groovy.cps.NonCPS
import hudson.model.FreeStyleProject
import hudson.plugins.git.*
import hudson.plugins.git.extensions.*
import hudson.plugins.git.extensions.impl.*
import jenkins.model.Jenkins
import hudson.triggers.TimerTrigger
import hudson.model.*
import jenkins.model.*
import hudson.slaves.*
import javaposse.jobdsl.plugin.*
import java.util.Collections
import java.util.List
import hudson.plugins.git.extensions.GitSCMExtension

def jdk = tool name: 'localJDK'
env.JAVA_HOME = "${jdk}"

@NonCPS
 def createFreestyleProject(){
	   def repository = "https://github.com/riXab/groovy-pipeline-scripting.git";
       def parent = Jenkins.getInstance()
       //Instantiate a new project
       def project = new FreeStyleProject(parent, "my-style");
        //Set a description for the project
       project.setDescription("Just a placeholder for a description")
        //Create a parameter for the project
       def parameterDefinitions = new ArrayList<ParameterDefinition>();
       def name = "ParameterOne"
       def defaultValue = "1"
       def description = "Just a placeholder for a parameter description"
       parameterDefinitions.add(new StringParameterDefinition(name, defaultValue, description))
        //Create a job property for the project
       def jobProperty = new ParametersDefinitionProperty(parameterDefinitions)
        //Adding and saving the job property to the project
       project.addProperty(jobProperty)
	 println "Property Set"
	   
	   //set build trigger cron to run daily
	 project.addTrigger(new TimerTrigger("* * * * *"))


//set SCM
List<BranchSpec> branches = Collections.singletonList(new BranchSpec("*/master"));
List<SubmoduleConfig> submoduleCnf = Collections.<SubmoduleConfig>emptyList();
// We are using predefined user id jenkins. You change it in the global config
List<UserRemoteConfig> usersconfig = Collections.singletonList(
        new UserRemoteConfig(repository, '', '', '')
)
List<GitSCMExtension> gitScmExt = new ArrayList<GitSCMExtension>();
def scm = new GitSCM(usersconfig, branches, false, submoduleCnf, null, null, gitScmExt)
project.setScm(scm)

//set build steps
def jdk = tool name: 'localJDK'
			env.JAVA_HOME = "${jdk}"
			
			//def mvnHome = tool 'localMaven'
			//bat "${mvnHome}\\bin\\mvn -B -Dmaven.test.failure.ignore verify"
			//OR:
			withEnv(["PATH+MAVEN=${tool 'localMaven'}/bin"]) {
				bat 'mvn -B verify'
			}
			//step([$class: 'ArtifactArchiver', artifacts: '**/target/*.jar', fingerprint: true])
			//step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml'])
			archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true


//set post build steps
def publishersList = project.getPublishersList()
publishersList.add(new hudson.tasks.BuildTrigger("my-groove", true))

project.save()
parent.reload()   //Jenkins.instance.reload() -- same thing.. Don't do 'restart'


	 //project.getSCM("https://github.com/riXab/groovy-pipeline-scripting.git")
//		def branchConfig = [new BranchSpec("master")]
//		def userConfig = [new UserRemoteConfig(repository, null, null, null)]
//		project.setSCM(new hudson.plugins.git.GitSCM(repository))
		//project.setSCM(new hudson.plugins.git.GitSCM(userConfig, branchConfig, false, [], null, null, null))
//	println "SCM Set"	
  //     project.save()
    //   

}


def buildMaven(){
	
	withEnv(["PATH+MAVEN=${tool 'localMaven'}/bin"]) {
				bat 'mvn -o clean package'
	}	
}

return this