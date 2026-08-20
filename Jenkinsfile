pipeline{

     agent any


     stages{

        // stage for checkout with scm = github repo

        stage('checkout'){

            steps{

                checkout scm
            }
        }

        // stage for the testing 

        stage('test'){

            steps{

                sh'mvn test'
            }
        }

        stage('package'){

            steps{

                sh'maven package'
            }
        }


     }
}