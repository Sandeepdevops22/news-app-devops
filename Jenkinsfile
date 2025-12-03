pipeline {
    agent { label 'slave3'}
    stages {
        stage('News-App-Checkout') {
            steps {
                sh 'rm -rf news-app-devops'
                sh 'git clone https://github.com/Sandeepdevops22/news-app-devops.git'
                echo "git clone completed"
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Version-Build') {
            steps {
                script {
                    def version = "1.0.${env.BUILD_NUMBER}"
                    echo "Setting project version to ${version}"
                    sh "mvn versions:set -DnewVersion=${version} -DgenerateBackupPoms=false"
                    
                    // Rebuild and package with the new version
                    sh "mvn clean package"
                }
            }
        }
        stage('Deploy') {
            steps {
                sh "sudo cp /home/slave3/workspace/News-app_feature-1/target/news-app.war /opt/tomcat10/webapps/"
                echo "build deployed"
            }
        }
        
        // 6. Push Artifacts to JFrog Artifactory (The corrected stage)
        stage('Push the artifacts into Jfrog Artifactory') {
            steps {
                script {
                    def currentDate = new java.text.SimpleDateFormat("yyyy-MM-dd_HH-mm").format(new Date())
                    // Repository name (generic-local) is often required in the target path
                    def targetPath = "generic-local/NewsApp/${currentDate}/" 
                    
                    // The recommended Artifactory wrapper for authenticated context
                    withArtifactory(
                        // Base URL of your JFrog instance
                        url: 'https://trialea6yjn.jfrog.io/', 
                        // The ID of the credential stored in Jenkins Credentials Manager
                        credentialsId: 'jfrog-credentials-id'
                    ) {
                        rtUpload(
                            // This uses the authenticated context established by withArtifactory
                            // Target path is relative to the Maven project root
                            spec: """
                            {
                                "files": [
                                    {
                                        "pattern": "target/*.war", 
                                        "target": "${targetPath}"
                                    }
                                ]
                            }
                            """
                        )
                    }
                }
            }
        }
    }
    
    // Post-build actions
    post {
        success {
            // Archive the final WAR file as a Jenkins artifact
            archiveArtifacts artifacts: 'target/*.war', fingerprint: true
            echo "Pipeline finished successfully!"
        }
        failure {
            echo "Pipeline failed in one of the stages."
        }
    }
}
