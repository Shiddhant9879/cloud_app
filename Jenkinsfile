pipeline {

    agent any

    tools {
        maven 'Maven3'
    }

    stages {

        stage('checkout') {
            steps {
                echo 'checkout the code'
            }
        }

        stage('test') {
            steps {
                bat 'mvn test'
            }
        }

        stage('package') {
            steps {
                bat 'mvn package'
            }
        }
    }
}