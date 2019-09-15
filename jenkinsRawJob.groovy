node {
    stage "Create build output"
      
// Advice: don't define M2_HOME in general. Maven will autodetect its root fine.
withEnv(["JAVA_HOME=${ tool 'localJDK' }", "PATH+MAVEN=${tool 'localMaven'}/bin:${env.JAVA_HOME}/bin"]) {

    // Apache Maven related side notes:
    // --batch-mode : recommended in CI to inform maven to not run in interactive mode (less logs)
    // -V : strongly recommended in CI, will display the JDK and Maven versions in use.
    //      Very useful to be quickly sure the selected versions were the ones you think.
    // -U : force maven to update snapshots each time (default : once an hour, makes no sense in CI).
    // -Dsurefire.useFile=false : useful in CI. Displays test errors in the logs directly (instead of
    //                            having to crawl the workspace files to see the cause).
    bat "mvn --batch-mode -V -U -e clean deploy -Dsurefire.useFile=false"

}

	git url: "https://github.com/riXab/groovy-pipeline-scripting.git"
    // Make the output directory.
    bat "mkdir -p output"

    // Write an useful file, which is needed to be archived.
    writeFile file: "output/usefulfile.txt", text: "This file is useful, need to archive it."

    // Write an useless file, which is not needed to be archived.
    writeFile file: "output/uselessfile.md", text: "This file is useless, no need to archive it."

    stage "Archive build output"
    
    // Archive the build output artifacts.
    archiveArtifacts artifacts: 'output/*.txt', excludes: 'output/*.md'
}