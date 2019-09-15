def pipeline
node('master') {
	checkout scm
	echo "****Checkout"
	//def jobName = ''
    //git url: 'https://github.com/riXab/reference-groovy-pipeline.git'
    echo "****load pipeline"
	pipeline = load 'Jenkinsfile-FreeStyle.groovy'
    pipeline.createFreestyleProject()
}
