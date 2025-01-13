package org.devops

def testCoverage() {
    sh 'npm test'
}

def analisisSonar(String gitName, String sourceDir) {
    def scannerHome = tool 'sonar-scanner'
    if (scannerHome) {
        withSonarQubeEnv('sonar-scanner') {
            sh """
                ${scannerHome}/bin/sonar-scanner \
                -Dsonar.projectKey=${gitName} \
                -Dsonar.projectName=${gitName} \
                -Dsonar.sources=${sourceDir} \
                -Dsonar.tests=src/_test \
                -Dsonar.exclusions=**/*.test.js \
                -Dsonar.testExecutionReportPaths=./test-report.xml \
                -Dsonar.javascript.lcov.reportPaths=./coverage/lcov.info
            """
        }
    } else {
        error 'SonarQube Scanner not found'
    }
}