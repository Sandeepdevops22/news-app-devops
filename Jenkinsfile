pipeline {
    agent { label 'java' }
    stages {
        stage('News-App-Checkout') {
            steps {
                sh 'rm -rf news-app-devops'
                sh 'https://github.com/pradeepreddy-hub/news-app-devops.git'
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
                    // Example: version = 1.0.<BUILD_NUMBER>
                    def version = "1.0.${env.BUILD_NUMBER}"
                    echo "Setting project version to ${version}"
                    
                    // Update pom.xml version
                    sh "mvn versions:set -DnewVersion=${version}"
                    
                    // Build with new version
                    sh "mvn clean package"
                }
            }
        }
        stage('Deploy') {
    steps {
   sh "sudo cp /home/ubuntu/news-app-devops/target/news-app.war /opt/tomcat10/webapps/"
      echo "build deployed"
    }
}
        // 6.3: Push the artifacts to Jfrog repository
stage('Push the artifacts into Jfrog Artifactory') {
    steps {
        script {
            // Get the current date and time in the format: yyyy-MM-dd_HH-mm
            def currentDate = new java.text.SimpleDateFormat("yyyy-MM-dd_HH-mm").format(new Date())

            // Define the target path with the timestamp
            def targetPath = "NewsApp/${currentDate}/"

            // Configure the Artifactory server
            rtServer(
                id: 'Artifactory',
                url: 'https://trialyth1ui.jfrog.io/artifactory',
                credentialsId: 'jfrog-credentials-id'   // must match Jenkins credentials
            )

            // Upload the artifact to JFrog Artifactory with the timestamped path
            rtUpload(
                serverId: 'Artifactory',
                spec: """
                {
                    "files": [
                        {
                            "pattern": "*.war",
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
