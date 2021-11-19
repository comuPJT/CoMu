pipeline {
	agent any
	stages {
		stage('spring-build-deploy') {
			steps {
				script {
					try {
						mattermostSend (
							color: "#ff7f00",
							message: "spring-build-deploy 시작: ${env.JOB_NAME} #${env.BUILD_NUMBER} (<${env.BUILD_URL}|Link to build>)"
						)
						sh 'docker rm -f backend'
						sh 'docker build -t dockerize-spring-boot-app ./backend'
						sh 'docker run --name backend --network mysql -d -p 8080:8080 dockerize-spring-boot-app'
						sh 'docker network connect front-back backend'
						sh 'docker network connect redis-back backend'
					} catch(e) {
						currentBuild.result = "FAILURE"
					} finally {
						if(currentBuild.result == "FAILURE") {
							mattermostSend (
								color: "danger",
								message: "spring-build-deploy 실패: ${env.JOB_NAME} #${env.BUILD_NUMBER} (<${env.BUILD_URL}|Link to build>)"
							)
						} else {
							mattermostSend (
								color: "good",
								message: "spring-build-deploy 성공: ${env.JOB_NAME} #${env.BUILD_NUMBER} (<${env.BUILD_URL}|Link to build>)"
							)
						}
					}
				}
			}
		}
		stage('vue-build-deploy') {
			steps {
				script {
					try {
						mattermostSend (
							color: "#ff7f00",
							message: "vue-build-deploy 시작: ${env.JOB_NAME} #${env.BUILD_NUMBER} (<${env.BUILD_URL}|Link to build>)"
						)
						sh 'docker rm -f frontend'
						sh 'docker build -t dockerize-vuejs-app ./frontend'
						sh 'docker container prune -f'
						sh 'docker rmi $(docker images -f dangling=true -q)'
						sh 'docker run --name frontend --network front-back -d -p 80:80 -p 443:443 -v /home/ubuntu/cert/:/etc/letsencrypt/ dockerize-vuejs-app'
					} catch(e) {
						currentBuild.result = "FAILURE"
					} finally {
						if(currentBuild.result == "FAILURE") {
							mattermostSend (
								color: "danger",
								message: "vue-build-deploy 실패: ${env.JOB_NAME} #${env.BUILD_NUMBER} (<${env.BUILD_URL}|Link to build>)"
							)
						} else {
							mattermostSend (
								color: "good",
								message: "vue-build-deploy 성공: ${env.JOB_NAME} #${env.BUILD_NUMBER} (<${env.BUILD_URL}|Link to build>)"
							)
						}
					}
				}
			}
		}
	}
}
