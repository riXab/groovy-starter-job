#!groovy

// imports
import jenkins.model.Jenkins

// parameters
def systemMessage = "Insert your Jenkins system message here."

// get Jenkins instance
Jenkins jenkins = Jenkins.getInstance()

echo "setting system Message"
// set Jenkins system message
jenkins.setSystemMessage(systemMessage)

// save current Jenkins state to disk
jenkins.save()
jenkins.reload()