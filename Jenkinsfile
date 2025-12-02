pipeline {
    agent { label 'java' }

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

                    sh "mvn versions:set -DnewVersion=${version}"
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

        stage('Push the artifacts into Jfrog Artifactory') {
            steps {
                script {

                    // Timestamp for folder structure
                    def currentDate = new java.text.SimpleDateFormat("yyyy-MM-dd_HH-mm").format(new Date())

                    // ✔ FIXED — include the repository name
                    def targetPath = "generic-local/NewsApp/${currentDate}/"

                    // Configure Artifactory
                    rtServer(
                        id: 'Artifactory',
                        url: 'https://trialea6yjn.jfrog.io/artifactory',
                        credentialsId: 'jfrog-credentials-id'
                    )

                    // Upload artifact from target/ folder
                    rtUpload(
                        serverId: 'Artifactory',
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

    post {
        success {
            archiveArtifacts artifacts: 'target/*.war', fingerprint: true
        }
    }
}
