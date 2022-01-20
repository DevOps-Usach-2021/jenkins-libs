def call(Map pipelineParams) {
//   if (buildNumber % 2 == 0) {
    pipeline {
      agent any
      stages {
        stage('Saludo') {
          steps {
            script{
                utils.saludo(Nombre: pipelineParams.Saludo)
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