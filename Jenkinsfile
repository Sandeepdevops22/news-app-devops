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

        // Cleaned up Artifactory Stage (Use this instead of rtServer/rtUpload steps)
stage('Push the artifacts into Jfrog Artifactory') {
    steps {
        script {
            def currentDate = new java.text.SimpleDateFormat("yyyy-MM-dd_HH-mm").format(new Date())
            def targetPath = "NewsApp/${currentDate}/"
            
            // This wrapper provides authentication for all steps inside it
            withArtifactory(
                url: 'https://trialea6yjn.jfrog.io/', // Use the base URL
                credentialsId: 'jfrog-credentials-id'
            ) {
                rtUpload(
                    spec: """
                    {
                        "files": [
                            {
                                "pattern": "target/news-app.war", // Target the WAR file
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
