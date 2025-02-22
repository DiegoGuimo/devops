package org.devops

def analisisSonar(gitName) {
    def scannerHome = tool 'sonar-scanner'
    if(scannerHome) {
        withSonarQubeEnv('sonar-scanner') {
            sh "${scannerHome}/bin/sonar-scanner \
            -Dsonar.projectKey=${gitName} \
            -Dsonar.projectName=${gitName} \
            -Dsonar.sources=${env.SOURCE} \
            -Dsonar.tests=src/__test__ \
            -Dsonar.exclusions=***/*.test.js,**/*.txt \
            -Dsonar.testExecutionReportPaths=./test-report.xml \
            -Dsonar.javascript.lcov.reportPaths=./coverage/lcov.info"
        }
    } else {
        error 'SonarQube Scanner not found'
    }
}
return this
