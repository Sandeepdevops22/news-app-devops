pipeline {
    agent { 
        // Use the label of your Jenkins agent (slave3) that has Maven, Git, and necessary permissions.
        label 'slave3' 
    }
    
    stages {
        // 1. Source Checkout (Initial SCM Checkout is done by Jenkins automatically)
        stage('News-App-Checkout') {
            steps {
                // Perform a manual checkout into a dedicated directory if needed for Maven structure
                // Note: The main SCM checkout already places files in the workspace. This step
                // is often redundant if the 'dir' structure isn't specifically required.
                sh 'rm -rf news-app-devops'
                sh 'git clone https://github.com/Sandeepdevops22/news-app-devops.git'
                echo "git clone completed"
            }
        }
        
        // 2. Build Stage
        stage('Build') {
            steps {
                // Executes clean and package goals, generating the initial WAR file
                sh 'mvn clean package'
            }
        }
        
        // 3. Test Stage
        stage('Test') {
            steps {
                // Executes only the test goal (package already ran the tests once)
                sh 'mvn test'
            }
        }
        
        // 4. Versioning and Rebuild Stage
        stage('Version-Build') {
            steps {
                script {
                    // Set project version using Jenkins BUILD_NUMBER (e.g., 1.0.17)
                    def version = "1.0.${env.BUILD_NUMBER}"
                    echo "Setting project version to ${version}"
                    
                    // Update pom.xml version
                    sh "mvn versions:set -DnewVersion=${version} -DgenerateBackupPoms=false"
                    
                    // Rebuild and package with the new version
                    sh "mvn clean package"
                }
            }
        }
        
        // 5. Local Deployment to Tomcat
        stage('Deploy') {
            steps {
                // Copy the newly versioned WAR file to the Tomcat webapps directory on the agent
                // NOTE: This requires 'sudo' permissions on the agent and is generally for local testing.
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
