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
import hudson.model.FreeStyleBuild;

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
//       def parameterDefinitions = new ArrayList<ParameterDefinition>();
//       def name = "ParameterOne"
//       def defaultValue = "1"
//       def description = "Just a placeholder for a parameter description"
//       parameterDefinitions.add(new StringParameterDefinition(name, defaultValue, description))
        //Create a job property for the project
//       def jobProperty = new ParametersDefinitionProperty(parameterDefinitions)
        //Adding and saving the job property to the project
//       project.addProperty(jobProperty)
	 println "Property Set"
	   
	   //set build trigger cron to run daily
	 project.addTrigger(new hudson.triggers.SCMTrigger("* * * * *"))

	 	//SET JDK Path
//	dis = new hudson.model.JDK.DescriptorImpl();
	//dis.setInstallations( new hudson.model.JDK("localJDK", "C://Program/ Files//Java//jdk1.8.0_172"));
	 
	//SET MAven Path
	//def desc = parent.getDescriptor("hudson.tasks.Maven")
  //  def minst =  new hudson.tasks.Maven.MavenInstallation("localMaven", "C://Users//rishasha//Documents//Workspace//apache-maven-3.3.9");
   // desc.setInstallations(minst)
   // desc.save()
	

	

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



println "Trying for Build Configuration now.."

if (scm instanceof hudson.plugins.git.GitSCM){
	println "Yes, for scm being instance of GitSCM"
}

println "Trying something out.."
//project.scheduleBuild2(0)
//set build steps
//FreeStyleBuild build = jenkins.buildAndAssertSuccess(project);
//def command = "echo Set Build to Maven Install";
//project.getBuildersList().add(Functions.isWindows() ? new BatchFile(command) : new Shell(command))


//set build steps
def buildersList = project.getBuildersList()
//buildersList.add(new hudson.tasks.Maven("clean package", "localMaven", false, new jenkins.mvn.DefaultSettingsProvider(), new jenkins.mvn.DefaultGlobalSettingsProvider(), false))
buildersList.add(new hudson.tasks.Maven("clean package", "localMaven"))
	




//set post build steps
def publishersList = project.getPublishersList()
//publishersList.add(new hudson.tasks.BuildTrigger("my-groove, MyJob_3", false))
publishersList.add(new hudson.tasks.ArtifactArchiver("**/*.war", "", false, false))
publishersList.add(new hudson.tasks.BuildTrigger("my-style-check, asf", false))
//publishersList.add(new hudson.tasks.BuildTrigger("", false))
// Get Jenkins instance
//def j = Jenkins.getInstance();
// Get the job we wan't to trigger
//def job = j.getItem("asf");
// Finally we schedule a new build which starts directly (the zero in the argument)
//job.scheduleBuild2(0)

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

@NonCPS
def startBuild(){
	// Get Jenkins instance
def j = Jenkins.getInstance();
// Get the job we wan't to trigger
def job = j.getItem("my-style");
// Finally we schedule a new build which starts directly (the zero in the argument)
job.scheduleBuild2(0)
}

@NonCPS
def createCheckstyleProject(){
	
	  def repository = "https://github.com/riXab/groovy-pipeline-scripting.git";
       def parent = Jenkins.getInstance()
       //Instantiate a new project
       def project = new FreeStyleProject(parent, "my-style-check");
        //Set a description for the project
       project.setDescription("Code Quality Check")
	   
	   
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

	   def buildersList = project.getBuildersList()
		buildersList.add(new hudson.tasks.Maven("checkstyle:checkstyle", "localMaven"))
		
		def publishersList = project.getPublishersList()
//publishersList.add(new hudson.tasks.BuildTrigger("my-groove, MyJob_3", false))
publishersList.add(new hudson.plugins.checkstyle.CheckStylePublisher())


	   project.save()
parent.reload() 

}


@NonCPS
def createStageDeploymentProject(){
	

       def parent = Jenkins.getInstance()
       //Instantiate a new project
       def project = new FreeStyleProject(parent, "my-style-stage");
        //Set a description for the project
       project.setDescription("Deploy war to Stage")
	   


	   def buildersList = project.getBuildersList()
	buildersList.add(new hudson.plugins.copyartifact.CopyArtifact("my-style", "**/*.war"))
	//buildersList.add(new hudson.plugins.copyartifact.CopyArtifact("my-style")
	//buildersList.add(new hudson.plugins.copyartifact.CopyArtifact("my-style", "**/*.war", "", "", new hudson.plugins.copyartifact.StatusBuildSelector(), false))
	//buildersList.add(new hudson.plugins.copyartifact.CopyArtifact("my-style", "**/*.war", new hudson.plugins.copyartifact.BuildSelector(),  "", "", false, false))
		echo "Fetching the artifact from developed job"
		

	   project.save()
parent.reload() 

}




return this