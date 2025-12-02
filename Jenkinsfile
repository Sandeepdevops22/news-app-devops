pipeline {
    agent { label 'java' }
    stages {
        stage('News-App-Checkout') {
            steps {
                sh 'rm -rf news-app-devops'
                sh 'git clone https://github.com/Devopsdemo2025/news-app-devops.git'
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
   sh "sudo scp /home/slave1/workspace/p_MultiBranch_Pipeline_feature-1/target/news-app.war  jenkins@13.233.20.29:/opt/apache-tomcat-11.0.14/webapps/" 
      echo "build deployed"
    }
}
       
    }
    post {
    success {
        archiveArtifacts artifacts: 'target/*.war', fingerprint: true
    }
}
    
}
