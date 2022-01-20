// vars/evenOrOdd.groovy
def call(int buildNumber) {
  if (buildNumber % 2 == 0) {
    pipeline {
      agent any
      stages {
        stage('Saludo') {
          steps {
            script{
                utils.saludo
            }
          }
        }
      }
    }
  } 
//   else {
//     pipeline {
//       agent any
//       stages {
//         stage('Odd Stage') {
//           steps {
//             echo "The build number is odd"
//           }
//         }
//       }
//     }
//   }
}