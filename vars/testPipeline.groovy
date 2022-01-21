def call(Map pipelineParams) {
//   if (buildNumber % 2 == 0) {
    pipeline {
      agent any
      stages {
        stage('1. Saludo') {
          steps {
            script{
                utils.saludo(Nombre: pipelineParams.Saludo)
            }
          }
        }
        stage('2. Test env') {
          steps {
            script{
                utils.printEnv()
            }
          }
        }
      }
    }
//   }
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