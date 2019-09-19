def pipeline
node('master') {
	checkout scm
	echo "****Checkout"
	//def jobName = ''
    //git url: 'https://github.com/riXab/reference-groovy-pipeline.git'
    echo "****load pipeline"
	//load 'configMavenAutoInstaller.groovy'
	pipeline = load 'Jenkinsfile-FreeStyle.groovy'
	echo "Create Checkstyle"
	pipeline.createCheckstyleProject()
	echo "Create Build Job"
    pipeline.createFreestyleProject()
	echo "Start the BUILD"
	pipeline.startBuild()
}
