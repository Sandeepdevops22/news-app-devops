pipeline {
    agent { label 'slave3' }

    stages {

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

                    sh """
                        mvn versions:set -DnewVersion=${version} -DgenerateBackupPoms=false
                        mvn clean package
                    """
                }
            }
        }

        stage('Deploy') {
            steps {
                sh "sudo cp /home/slave3/workspace/News-app_feature-1/target/news-app.war /opt/tomcat10/webapps/"
                echo "Build deployed successfully."
            }
        }
    }
}
