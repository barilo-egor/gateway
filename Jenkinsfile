// Требуется наличие следующих переменных в Jenkins:
// - SSH_CRED_ID - идентификатор SSH ключа
// - GATEWAY_DEPLOY_PATH - путь на сервере, куда необходимо расположить собранные проекты
// - GATEWAY_DEPLOY_HOST - IP адрес сервера, на который будут отправлены проекты
// - SSH_USER - пользователь SSH
// - SSH_PORT - порт SSH
pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                sh 'chmod +x gradlew'
                sh './gradlew clean bootJar --no-daemon'
            }
        }
        stage('Deploy') {
            steps {
                sshagent([env.SSH_CRED_ID]) {
                    sh "scp -P ${SSH_PORT} build/libs/gateway.jar ${SSH_USER}@${GATEWAY_DEPLOY_HOST}:${GATEWAY_DEPLOY_PATH}/"
                    sh "ssh -p ${SSH_PORT} ${SSH_USER}@${GATEWAY_DEPLOY_HOST} 'cd /srv/gateway && docker rollout --wait 60 --timeout 60 gateway'"
                }
            }
        }
    }

    post {
        success {
            echo 'Сборка и деплой успешно завершены!'
        }
        failure {
            echo 'Ошибка при сборке или деплое.'
        }
    }
}
