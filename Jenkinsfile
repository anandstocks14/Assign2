pipeline {
    agent any

    options {
        skipDefaultCheckout(true)
    }

    parameters {
        gitParameter(
            name: 'BRANCH',
            type: 'PT_BRANCH',
            branchFilter: 'origin/(.*)',
            defaultValue: 'development'
        )
    }

    stages {
        stage('Fetch') {
            steps {
                echo "Selected branch: ${params.BRANCH}"
            }
        }

        stage('Checkout') {
            steps {
                deleteDir()

                git branch: "${params.BRANCH}",
                    url: 'https://github.com/anandstocks14/Assign2.git'

                echo "Successfully checked out: ${params.BRANCH}"
            }
        }

        stage('SonarQube Analysis') {
            steps {
                dir('app') {
                    withSonarQubeEnv('SonarQube-Server') {
                        sh '''
                            mvn clean verify sonar:sonar \
                            -Dsonar.projectKey=assign2 \
                            -Dsonar.projectName=Assign2
                        '''
                    }
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 10, unit: 'MINUTES') {
                    script {
                        def result = waitForQualityGate()

                        echo "Quality Gate Status: ${result.status}"

                        if (result.status != 'OK') {
                            error "FAILED: Code coverage is below 80%"
                        }

                        echo "PASSED: Code coverage is 80% or higher"
                    }
                }
            }
        }
    }
}
