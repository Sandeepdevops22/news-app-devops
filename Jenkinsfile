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
   sh "sudo cp /home/slave3/workspace/News-app_feature-1/target/news-app.war /opt/tomcat10/webapps/"
      echo "build deployed"
    }
}
        def currentDate = new java.text.SimpleDateFormat("yyyy-MM-dd_HH-mm").format(new Date())

// FIXED — Add repository name at the beginning
def targetPath = "generic-local/NewsApp/${currentDate}/"

// Configure JFrog Artifactory
rtServer(
    id: 'Artifactory',
    url: 'https://trialea6yjn.jfrog.io/artifactory',
    credentialsId: 'jfrog-credentials-id'
)

// Upload the .war file from target/ folder
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
