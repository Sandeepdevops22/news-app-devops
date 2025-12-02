pipeline {
    agent none

    stages {

        stage('Build') {
            agent { label 'Java' }
            steps {
                sh "mvn clean package"
            }
        }

        stage('Test') {
            agent { label 'Java' }
            steps {
                sh "mvn test"
            }
        }

        stage('Versioning') {
            agent { label 'Java' }
            steps {
                echo "version"
            }
        }

        stage('Deploy') {
            agent { label 'Java' }
            steps {
                sh """
                /usr/bin/sudo cp /home/ubuntu/news-app-devops/target/news-app.war /opt/tomcat10/webapps/
                """
            }
        }
    }
}
