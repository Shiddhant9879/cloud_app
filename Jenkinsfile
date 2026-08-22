pipeline{

     agent any


     stages{

        // stage for checkout with scm = github repo

        stage('checkout'){

            steps{

              echo 'checkout the code'
            }
        }

        // stage for the testing 

        stage('test'){

            steps{

                bat'mvn test'
            }
        }

        stage('package'){

            steps{

                bat'mavn package'
            }
        }


     }
}