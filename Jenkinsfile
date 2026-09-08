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
                    withEnv([
                        'JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64',
                        'PATH+JAVA=/usr/lib/jvm/java-21-openjdk-amd64/bin'
                    ]) {
                        withSonarQubeEnv('SonarQube-Server') {
                            sh 'java -version'
                            sh 'mvn -version'
                            sh 'mvn clean verify org.sonarsource.scanner.maven:sonar-maven-plugin:5.2.0.4988:sonar -Dsonar.projectKey=assign2 -Dsonar.projectName=Assign2'
                        }
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

        stage('Approval') {
            steps {
                script {
                    def approval = input(
                        message: 'Do you want to continue with the build?',
                        parameters: [
                            choice(
                                name: 'APPROVAL',
                                choices: ['Yes', 'No']
                            )
                        ]
                    )

                    if (approval == 'No') {
                        error "Approval rejected. Pipeline failed."
                    }

                    echo "Approval granted."
                }
            }
        }

        stage('Build') {
            steps {
                dir('app') {
                    sh 'mvn package -DskipTests'
                }
            }
        }

        stage('Upload Artifact to Nexus') {
            steps {
                dir('app') {
                    withCredentials([
                        usernamePassword(
                            credentialsId: 'nexus-credentials',
                            usernameVariable: 'NEXUS_USER',
                            passwordVariable: 'NEXUS_PASSWORD'
                        )
                    ]) {
                        sh '''
                            set +x
                            trap 'rm -f nexus-settings.xml' EXIT

                            {
                                printf '%s\\n' '<settings>'
                                printf '%s\\n' '  <servers>'
                                printf '%s\\n' '    <server>'
                                printf '%s\\n' '      <id>nexus-releases</id>'
                                printf '      <username>%s</username>\\n' "$NEXUS_USER"
                                printf '      <password>%s</password>\\n' "$NEXUS_PASSWORD"
                                printf '%s\\n' '    </server>'
                                printf '%s\\n' '  </servers>'
                                printf '%s\\n' '</settings>'
                            } > nexus-settings.xml

                            mvn deploy \
                                -DskipTests \
                                -s nexus-settings.xml
                        '''
                    }
                }
            }
        }
    }
}
