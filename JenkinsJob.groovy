// Import Jenkins
import jenkins.model.Jenkins;
// Get Jenkins instance
def j = Jenkins.getInstance();
// Get the job we wan't to trigger
def job = j.getItem("reference-job");
// Finally we schedule a new build which starts directly (the zero in the argument)
job.scheduleBuild2(0)