import com.cloudbees.groovy.cps.NonCPS
import hudson.model.FreeStyleProject

@NonCPS
   def createFreestyleProject(){
       def parent = Jenkins.getInstance()
       //Instantiate a new project
       def project = new FreeStyleProject(parent, "my-free-stylING");
        //Set a description for the project
       project.setDescription("Just a placeholder for a description")
        //Create a parameter for the project
       //def parameterDefinitions = new ArrayList<ParameterDefinition>();
       //def name = "ParameterOne"
       //def defaultValue = "1"
       //def description = "Just a placeholder for a parameter description"
      // parameterDefinitions.add(new StringParameterDefinition(name, defaultValue, description))
        //Create a job property for the project
       //def jobProperty = new ParametersDefinitionProperty(parameterDefinitions)
        //Adding and saving the job property to the project
     //  project.addProperty(jobProperty)
	 
		project.getSCM("https://github.com/riXab/groovy-pipeline-scripting.git")
		
       project.save()
       parent.reload()

}

return this