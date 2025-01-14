package org.devops

def testCoverage() {
    try {
        echo "Running tests..."
        sh 'npm test'
    } catch (Exception e) {
        error "Test execution failed: ${e.message}"
    }
}

def analisisSonar(String gitName, String sourceDir) {
    def scannerHome = tool 'sonar-scanner'
    if (scannerHome) {
        try {
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
        } catch (Exception e) {
            error "SonarQube analysis failed: ${e.message}"
        }
    } else {
        error 'SonarQube Scanner not found'
    }
}