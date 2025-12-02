pipeline {
    agent any

    stages {
        stage('checkout') {
            steps {
                sh "rm -rf news-app-devops"
              sh  "git clone https://github.com/vinayak432/news-app-devops/"
            }
        }
   stage(' build') {
            steps {
                sh "mvn clean package"
            }
        }
         stage('Test') {
            steps {
                sh "mvn test"
            }
        }
 //    stage('version build') {
   //         steps {
     //           sh "mvn "
       //     }
        //}
         stage('deploy tomcat') {
            steps {
                sh "sudo cp /var/lib/jenkins/workspace/jenkinsfilef12_feature-2/target/news-app.war /opt/tomcat10/webapps/"
            }
        }
  // stage('Upload Artifact to JFrog') {
    //steps {
      //  sh 'mvn deploy'
    //}
//}
    }
}
