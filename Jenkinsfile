pipeline { 
    agent { 
        label "linux" 
    }
    
    tools { 
        maven 'maven-3'
        jdk 'java-11'
    }
    
    environment {
        GIT_BRANCH_REF = "${GIT_BRANCH}"
        GIT_LOCAL_BRANCH = "${GIT_BRANCH}"
    }
    
    stages { 
        
        stage ('clone') {
            
            steps {
        
                script {
                    GIT_LOCAL_BRANCH = GIT_BRANCH_REF.replace("refs/heads/", "")
                    GIT_LOCAL_BRANCH = GIT_LOCAL_BRANCH.replace("origin/", "")
                }
        
                echo 'Building Branch: ' + GIT_LOCAL_BRANCH
        
                git poll: false,
                          branch: "${GIT_LOCAL_BRANCH}",
                          credentialsId: 'GIT_SSH',
                          url: 'ssh://git@192.168.1.100:3322/gerrit/cs-alg-graph-bfs.git'
                 
            }
                
        }
        
        stage ('verify') {
            steps {
                withMaven (
                    maven: 'maven-3',
                    options: [
                        artifactsPublisher(disabled: true), 
                        findbugsPublisher(disabled: true), 
                        openTasksPublisher(disabled: true),
                        jacocoPublisher(disabled: false),
                        dependenciesFingerprintPublisher(disabled: true),
                        junitPublisher(disabled: true),
                        spotbugsPublisher(disabled: true)
                    ]
                ){
                    sh "mvn -e -ntp clean verify"
                }
            }
        }

        stage('sonar') {
        
            steps {
                withSonarQubeEnv(installationName: 'sonar') {
                    withMaven (
                        maven: 'maven-3',
                        options: [
                            artifactsPublisher(disabled: true), 
                            findbugsPublisher(disabled: true), 
                            openTasksPublisher(disabled: true),
                            jacocoPublisher(disabled: true),
                            dependenciesFingerprintPublisher(disabled: true),
                            junitPublisher(disabled: true),
                            spotbugsPublisher(disabled: true)
                        ]
                    ){
                        sh "mvn -e -ntp sonar:sonar"
                    }
                }
            }

        }

		stage("quality") {
			steps {
				timeout(time: 5, unit: 'MINUTES') {
					waitForQualityGate abortPipeline: true
				}
			}
		}

        stage('deploy') {
            steps {
                withMaven (
                    maven: 'maven-3',
                    options: [
                        artifactsPublisher(disabled: true), 
                        findbugsPublisher(disabled: true), 
                        openTasksPublisher(disabled: true),
                        jacocoPublisher(disabled: true),
                        dependenciesFingerprintPublisher(disabled: true),
                        junitPublisher(disabled: true),
                        spotbugsPublisher(disabled: true)
                    ]
                ){
                    sh "mvn -e -ntp -DskipTests=true -Dmaven.skip.test=true clean deploy"
                }
            }
        }

    }
}
