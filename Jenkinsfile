pipeline {
    agent { label 'slave3' }

    stages {

        stage('News-App-Checkout') {
            steps {
                sh 'rm -rf news-app-devops'
                sh 'git clone https://github.com/Sandeepdevops22/news-app-devops.git'
                echo "Git clone completed"
            }
        }

        stage('Build') {
            steps {
                sh 'cd news-app-devops && mvn clean package'
            }
        }

        stage('Test') {
            steps {
                sh 'cd news-app-devops && mvn test'
            }
        }

        stage('Version-Build') {
            steps {
                script {
                    def version = "1.0.${env.BUILD_NUMBER}"
                    echo "Setting project version to ${version}"

                    sh """
                        cd news-app-devops
                        mvn versions:set -DnewVersion=${version} -DgenerateBackupPoms=false
                        mvn clean package
                    """
                }
            }
        }

        stage('Deploy') {
            steps {
                sh """
                    sudo cp /home/slave3/workspace/News-app_feature-1/target/news-app.war /opt/tomcat10/webapps/
                """
                echo "Build deployed to Tomcat"
            }
        }
    }
}
        
        
