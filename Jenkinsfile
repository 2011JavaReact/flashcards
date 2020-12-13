pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        sh 'mvn package'
      }
    }

    stage('deploy') {
      steps {
        sh 'cp target/Flashcards.war /usr/share/tomcat/webapps/api.war'
      }
    }
  }
}