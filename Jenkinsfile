pipeline {
    agent none

    stages {

        stage('Build') {
            agent { label 'java' }
            steps {
                sh "mvn clean package"
            }
        }

        stage('Test') {
            agent { label 'java' }
            steps {
                sh "mvn test"
            }
        }

        stage('Versioning') {
            agent { label 'java' }
            steps {
                echo "version"
            }
        }

        stage('Deploy') {
            agent { label 'java' }
            steps {
                sh """
                /usr/bin/sudo cp /home/ubuntu/news-app-devops/target/news-app.war /opt/tomcat10/webapps/
                """
            }
        }
    }
}
