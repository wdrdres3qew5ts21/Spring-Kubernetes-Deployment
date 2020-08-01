/*
เราตั้งใจสร้าง List ขึ้นมาเพื่อให้เป็น Template สำหรับ choice ในการเลือก
Environment Server สำหรับ Deploy เช่นบน Dev Server อาจจะมี IP
อยู่คนล่ะเครื่องกับ Production และ Database ก็อาจจะไมไ่ด้แยกแค่ระดับ Schema 
แต่อาจจะแยกเป็นคนล่ะ DB อยู่คนล่ะที่กันเลย ซึ่งเราจะใช้ตัวแปรที่เป็น Constant เหล่านี้ในการทำเงื่อนไข
ว่าถ้าหากเป็น Dev Server ตัวแปร DB Server ก็จะมี Value คนล่ะอย่างกับ Prodcution นั่นเอง
*/
def getServerEnvironmentList(){
  return ['DEV', 'SIT', 'UAT', 'PROD']
}
// Pipeline Declarative คือ Syntax ที่ขึ้นต้นด้วยคำว่า Pipeline
pipeline {
    // agent :คือการบอกว่า จะจำกัดเงื่อนไขในการสั่ง execute task ไปที่ใดบ้างถ้าใช้ any ก็คือไม่มีข้อจำกัดใดๆ
    agent any
    // enviroment :คือการสร้างตัวแปรระดับ global ซึ่งจะไมไ่ด้จำกัด scope แค่ภายใน block ของแต่ล่ะ stage แต่จะเห็นทั้ง Pipeline
    // ซึ่งการเรียกตัวแปรตรงนี้จะพิเศษกว่าการกำหนด env ภายใน stages เพราะจะไม่ต้องนำหน้าด้วย env. แต่จะเรียกชื่อตัวแปรได้เลยภายใน ${ variable } 
    environment {
        AZ_AKZ_USER = credentials('AZ_AKZ_USER')
        AZ_AKZ_PASSWORD = credentials('AZ_AKZ_PASSWORD')
        AZ_AKZ_TENANT = credentials('AZ_AKZ_TENANT')
        CONTAINER_IMAGE = 'dev/spring-random-pod'
        AZ_AKS_NAME = "kube-devops"
        AZ_AKS_RESOUCE_GROUP = "Elasticsearch-Stack"
        // ชื่อของเครื่องที่ต้องการจะ Hold Approve ก่อนที่จะ Deploy ขึ้นไป
        PRODUCTION_SERVER = "PROD"
    }

    /* 
    stages :คือ root path หลักในการประกอบงานระดับ stage เข้ามาด้วยกันซึ่งถ้าถามว่า stage จะสื่อถึงอะไร ?
    stage ก็เปรียบเสมือนสายพานผลิตแบบยาวๆซึ่งจะเริ่มตั้งแต่ การ load อะไหล่เข้ามากองรวมกัน การประกอบอะไหล่
    และพอประกอบเสร็จก็เทสว่าอะไหล่หลายส่วนทำงานร่วมกันได้มั้ย โดยส่งไปที่ห้องทดลองแรกทดสอบกันน้ำ 
    และก็ส่งไปห้องกันกระแทก ทดสอบ long term ว่าอุปกรณ์ใช้งานได้จริงๆไหมหากเปิดทำงานต่อเนื่องกัน ซึ่งตรงนี้คือ stage นั่นเอง
    แต่ถ้าเรามาดูที่ steps ที่อยู่ใน stage ก็จะเป้นขั้นตอนย่อยๆซึ่งต้องการความละเอียดทุกๆขั้นตอนในการทำงานตัวอย่างเช่น
    การโหลดอะไหล่เข้ามา ก็ต้องดูว่าแล้วอะไหล่อะไรที่ต้องใช้ จะโหลดจากไหน ? โหลดเสร็จแล้วต้องส่งไปที่สายพานไหนต่อ
    แล้วกล่องที่ใช้แพ็คอะไหล่หลังจาก load ชื่อว่าอะไร ? คนต่อไปหากจะใช้ของที่ pack มาต้องหากล่องชื่อว่าอะไรนั่นเอง
    */
    stages {
        // เป็นส่วนทดสอบตัวแปร env ต่างๆซึ่ง Jenkins จะมีตัวแปรบางตัวที่มาพร้อมอยู่แล้วเป็น Global Variable ก็จะเรียกใช้ชื่อตัวแปรเหล่านั้นได้เลย
        // จากตัวอย่างคือเรามีการใช้ Plugin GIT (Pre-Load) มาหใ้แล้วเราไม่ต้องลงที่เราใช้ Git ได้ก็เพราะ Plugin Git นั่นเอง
        // สามารถเข้าไปดูตัวแปรจาก Plugin ได้ที่ส่วนของ Environment https://plugins.jenkins.io/git/
        // stage จะเป็นการตั้งชื่อเฉยๆให้เห็นบน Ocean Blue GUI
        stage('Setup Default ENV') {
            steps {
                echo '=========== Verify Branch ============'
                echo "${GIT_BRANCH}"
                script {
                    // ตั้ง default tag สำหรับ branch อื่นๆที่ไมไ่ด้ build ผ่าน master
                    // ซึ่งตัวแปร env ที่เราสร้างเองจะนำหน้าด้วย env.ENV_NAME เสมอและจากนั้นตัวแปรก็จะมองเห็นระดับ global ข้ามไปหานอก block code ได้
                    env.BUILD_BRANCH = "${GIT_BRANCH}-"
                    env.TAG_VERSION = "${BUILD_ID}"
                }
                echo "${env.BUILD_BRANCH}"
            }
        }

        stage('Input Data For Production') {
            // when condition จะทำงานก็ต่อเมื่อใช้ Multibranch Pipeline เท่านั้นซึ่งถ้าเงื่อนไขตรงก็จะสามารถทำงานได้ถ้าไม่ตรงก็จะ skip ไปทั้ง stage เลย
            when {
                branch 'master'
            }

            steps {
                // script directive คือส่วนที่ทำให้เราสามารถเขียน logic ตัวแปร programming ผสมกับ shell script ได้ โดย syntax ก็จะเป็น Groovy
                script {
                    // โค้ดส่วนนี้คือการให้ git ดึงเวอร์ชั่นทั้งหมดของ Tag ใน Git ในแสดงทาง Standard Output และเก็บลงไปในตัวแปร
                    // sh() คือ method ที่ให้เราเขียน shell script ได้ https://www.jenkins.io/doc/pipeline/steps/workflow-durable-task-step/#sh-shell-script
                    def git_tags = sh(script: 'git tag | sort -r', returnStdout: true)
                    //  input จะเป็น method  พิเศษที่ทำให้เราสามารถเพิ่ม Input GUI ใน Ocean Blue เพื่อป้อนค่าเข้าไปได้ https://www.jenkins.io/doc/pipeline/steps/pipeline-input-step/
                    // ซึ่งหลักการด้านล่างคือการมี choice ให้เลือกจาก Tag และ Build ID กับดึง list ของ server ต่างๆมาใช้ส่วน boolean, password เป็นตัวอย่างเฉยๆ
                    def input_params = input message: 'Tag Versioning',
                        parameters : [
                            choice(name: 'TAG_VERSION', choices: "${git_tags}", description: 'เลือก Tags ที่ต้องการ'),
                            text(name: 'BUILD_ID', defaultValue:"${BUILD_ID}"),
                            choice(name: 'SERVER_ENVIRONMENT', choices: getServerEnvironmentList(), description: 'เลือก Server Enviroment'),
                            booleanParam(name: 'TOGGLE', defaultValue: true, description: 'Toggle this value'),
                            password(name: 'PASSWORD', defaultValue: 'SECRET', description: 'Enter a password')
                        ]
                    env.BUILD_ID = input_params.BUILD_ID
                    env.BUILD_BRANCH = ''
                    env.SERVER_ENVIRONMENT = input_params.SERVER_ENVIRONMENT
                    env.TAG_VERSION = input_params.TAG_VERSION
                    env.TOGGLE = input_params.TOGGLE
                    env.PASSWORD = input_params.PASSWORD
                }
            }
        }

        stage('Build Java Project') {
            /* 
            build ทดลองใส่ตัวแปรบิ้วตรงนี้
            agent ตรงนี้เราจะพิเศษกว่าคือเราต้องการนำ source code ที่เป็น Java ของเราส่งเข้าไป Build ใน Jenkins เพื่อจะส่ง .jar ไปให้ Docker ต่อไป
            ซึ่งโปรเจคของผมใช้เป็น maven ผมก็จะใช้ maven image ในการ compile Spring Boot ให้ได้ .jar และส่ง .jar นี้ไปให้อีก Stage หนึ่ง
            ที่จะทำหน้าที่เฉพาะคือการ Build Container Image นั่นเอง
            */ 
            agent {
                docker {
                    image 'maven:3.6.3-jdk-11'
                    args '-v /root/.m2:/root/.m2'
                }
            }

            steps {
                script {
                    sh 'echo ==='
                    sh 'mvn -B -DskipTests clean package'
                    /*
                    archiveArtifacts :เราสามารถเลือกได้ด้วยว่าอยากเก็บ artifact ที่ build ออกมาเข้าไปใน Jenkins 
                    หรือไม่ซึ่งก็จะแสดงบน GUI ของ Jenkins เป้นไฟล์ .jar ให้สามารถ Download ได้เช่นเดียวกันด้วย
                    */ 
                    // archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
                    /* 
                    stash :นั่นอาจจะดูสับสนกับ archiveArtifacts แต่ว่า Stash นั้นจะไม่ได้เป็นการเซฟ Artifact แบบถาวรแล้วให้ Download ได้
                    แต่มันเป้นแค่การเซฟ stage ของไฟล์ดังกล่าวแพ็คลงกล่องเพื่อให้สามารถส่งไปยังอีก Stage ได้โดยที่ของไม่หายไป เพราะไม่อย่างนั้นจะพบว่า
                    ของที่เคย build มาหายหมดเลย ซึ่งก็เป็นเช่นนั้นเพราะว่าการ Build ใน Docker ทุกอย่างก็จะอยุ่แค่ใน layer ชั่วคราวถ้าไม่เซฟก็จะหายไปนั่นเอง
                    */ 
                    stash name: 'java-artifact', includes: '**/target/*.jar'
                }
            }
        }

        stage('Build Docker') {
            // Stage นี้จะทำงานก็ต่อเมื่อ Branch ที่โดน Trigger อยู่ในเงื่อน Or คือ Branch ใด Branch หนึ่งจากรายการดั่งต่อไปนี้นั่นเอง
            when {
                anyOf {
                    branch 'master'
                    branch 'dev'
                }
            }

            steps {
                script {
                    sh 'echo ============= Build Docker Image and Push ==================='
                    // unstash :คือคำสั่งที่ทำการ unpack ของจากขั้นตอนก่อนหน้านั่นเองซึ่งเราก็จะต้องระบุชื่อของกล่งอที่ pack มาให้ถูกต้องดว้ยเช่นกัน
                    unstash 'java-artifact'
                    // ในขั้นตอนนี้คือการประกอบตัวแปรของ image ที่เราเตรียมจะ build และตั้งชื่อ tag ให้ตรงกับบน container registry เพื่อที่จะ push ขึ้นไป
                    def FULL_CONTAINER_IMAGE_PATH = "${AZ_CONTAINER_REGISTRY_URL}/${CONTAINER_IMAGE}:${env.BUILD_BRANCH}${env.TAG_VERSION}"
                    env.FULL_CONTAINER_IMAGE_PATH = FULL_CONTAINER_IMAGE_PATH.replaceAll('/', "\\\\/")
                    // เลข ID ด้านหลังคือการ reference Credentials ของ Container Registry ที่เราเก็บไว้ใน Jenkins Server นั่นเอง
                    // ที่ต้องระบุเพราะว่าเวลาเรา Push container image ไปเก็บใน Private Registry เราก็จะต้องระบุ Credentials ด้วยนั่นเองเช่น
                    // URL ที่ Login เข้าไป username + password หรืออาจจะเป็น public key, access token ต่างๆขึ้นกับ policy security
                    docker.withRegistry("https://${AZ_CONTAINER_REGISTRY_URL}", '77ae6c02-d40b-4bae-82bf-ade4eeff03e3') {
                        def newApp = docker.build "${env.FULL_CONTAINER_IMAGE_PATH}"
                        newApp.push()
                    }
                }
            }
        }

        stage('Deploy to Development') {
            when {
                branch 'dev'
            }

            steps {
                echo '=========== We are Development branch ========='
            }
        }

        stage('Deploy to Kubernetes') {
            // when {
            //     branch 'master'
            // }

            agent {
                docker {
                    // ตรงส่วนนี้คือการใช้ Azure CLI Container ของ Microsoft ในการ Login เพื่อรับ Credentials และทำการสั่ง Deploy 
                    // แต่กระนั้น Jenkins ก็มีปัญหากับเรื่อง root permission แบบ fix code บน Azure CLI ลองเข้าไปอ่านได้ที่ลิ้งค์นี้คับ
                    // https://github.com/Azure/azure-cli/issues/14151
                    image 'mcr.microsoft.com/azure-cli:2.8.0'
                    args '--user root'
                }
            }

            steps {
               /*
               script ตรงนี้จะเป็นส่วนที่ใช้ในการ Deploy ไปยัง Kubernetes ซึ่งการจะ Deploy ได้นั้นก็หมายความว่าเราต้องมี Kubernetes Resource 
               ให้พร้อมใช้งานเริม่จากการสร้าง Deployment -> ReplicaSet -> Pod และทำการเปิด Service ให้ทำสามารถ Access ได้และจากนั้นก็ค่อย
               ทำการ Apply Ingress Resource ให้ชี้ไปยัง Kubernetes Servcie เป็นส่วนสุดท้ายเราก็จะทำการ Update Kubernetes Deployment
               Version ใหม่ได้แล้ว แต่ของเราจะใช้ annotation change cause บน Kubernetes ให้เกิดประโยชน์มากขึ้นเพื่อที่ตอนเราใช้ kube rollout history
               เราจะสามารถดูได้ว่า Version ของ Image ที่ Deploy ไปแล้วอยู่ที่ Version ใดบวกกับ Commit Message ล่าสุดคืออะไร
               */
                script {
                    sh "pwd"
                    def COMMIT_MESSAGE = sh(script: 'git log -1 --pretty=%B', returnStdout: true).trim()
                    // replace อักขระพิเศษออกไปป้องกัน sed มีปัญหา
                    env.COMMIT_MESSAGE = COMMIT_MESSAGE.replaceAll("(\')|(\")|(/)|(\\\\)|(\\()|(\\))|(\n)|(\t)", '')
                    sh "echo ${env.COMMIT_MESSAGE}"
                    sh "echo ============ AKS Credential ==============="
                    sh "az login --service-principal -u ${AZ_AKZ_USER} -p ${AZ_AKZ_PASSWORD} -t ${AZ_AKZ_TENANT}"
                    // ติดตั้ง AKS CLI บน Azure CLI Container เพื่อให้ใช้คำสั่ง K8S ได้
                    sh "az aks install-cli"
                    sh "az aks get-credentials -n ${AZ_AKS_NAME} -g ${AZ_AKS_RESOUCE_GROUP}"
                    // เลือก YAML สำหรับ Deploy ไปยัง K8S ให้ตรงกับ Enviroment ของเครื่องเช่น DEV, Staging, Production
                    // จะ Hold การ Auto Deploy เมื่อการบิ้วแล้วส่งไปยังเครื่องนั้นตรงกับชื่อเคร่องที่ Hold ไว้ (เพราะสำคัญห้าม Auto Deploy)
                    if ("${env.SERVER_ENVIRONMENT}" == "${PRODUCTION_SERVER}"){
                        // จะถามยืนยันก่อน Deploy ถ้าเป็น master build และมีการตั้งเวลากำหนดไว้ว่าต้องทำการยืนยันภายใน 2 ชั่วโมง
                         timeout(time: 2, unit: 'HOURS') {
                            input message: 'Approve Deploy to Production?', ok: 'Yes'
                        }
                        env.K8S_DEPLOY_YAML_PROFILE = "k8s-deployment-production.yaml"
                        env.K8S_SERVICE_YAML_PROFILE = "k8s-service-production.yaml"
                    } else {
                        env.K8S_DEPLOY_YAML_PROFILE = "k8s-deployment.yaml"
                        env.K8S_SERVICE_YAML_PROFILE = "k8s-service-nodeport.yaml"
                    }
                    // ใช้กำหนด docker image ที่จะรัน pod
                    // sed คือ Stream Editor บน Linux ใช้ในการแก้ไข Text File ตาม Pattern Regex ที่พบซึ่งก็จะคล้ายๆกับ AWK เช่นเดียวกัน
                    // จากโค้ดชุดนี้คือเราต้องการที่ replace ตัวแปรที่เป็น Environment ต่างๆให้อยู่ใน Deployment ของ Kubernetes
                    // ซึ่งจริงๆแล้วเราจะใช้ Helm ก็ได้เพื่อที่จะได้มีไฟล์ values.yaml เป็น Template แล้วไม่ต้องแก้ไขด้วย sed ทีล่ะคำโดย -i หมายถึงยืนยันการแก้ไข
                    sh "sed -i 's/AZ_CONTAINER_REGISTRY_URL/${AZ_CONTAINER_REGISTRY_URL}/g' ${env.K8S_DEPLOY_YAML_PROFILE}"
                    sh "sed -i 's/IMAGE_GIT_BRANCH-/${env.BUILD_BRANCH}/g' ${env.K8S_DEPLOY_YAML_PROFILE}"
                    sh "sed -i 's/IMAGE_BUILD_ID/${env.TAG_VERSION}/g' ${env.K8S_DEPLOY_YAML_PROFILE}"
                    // ตั้งแต่บรรทัดนี้ไปจะเป็นการ Replace ตัวแปรใน Kubernetes yaml ตาม Pattern จากตัวแปรของเรานั่นเอง
                    // กำหนด change cause ของ rollout history
                    sh "sed -i 's/ENV_CHANGE_CAUSE_MESSAGE/[IMAGE] ${env.FULL_CONTAINER_IMAGE_PATH} - ${env.COMMIT_MESSAGE}/g' ${env.K8S_DEPLOY_YAML_PROFILE}"
                     // กำหนด env ของ pod ให้ทุกตัวนำด้วย ENV_
                    sh "sed -i 's/ENV_SERVER_ENVIRONMENT/${env.SERVER_ENVIRONMENT}/g' ${env.K8S_DEPLOY_YAML_PROFILE}"
                    sh "sed -i 's/ENV_GIT_BRANCH/${GIT_BRANCH}/g' ${env.K8S_DEPLOY_YAML_PROFILE}"
                    sh "sed -i 's/ENV_BUILD_ID/${env.TAG_VERSION}/g' ${env.K8S_DEPLOY_YAML_PROFILE}"
                    // สั่ง apply resource ไปยัง K8S
                    sh "echo =========================================="
                    sh "echo ============ Deploy to Kubernetes to ${env.SERVER_ENVIRONMENT} API ============="
                    // สร้าง Deployment Resouce
                    sh "kubectl apply -f ${env.K8S_DEPLOY_YAML_PROFILE}"
                    // สร้าง Service Resouce สำหรับทำ Service Discovery
                    sh "kubectl apply -f ${env.K8S_SERVICE_YAML_PROFILE} --record=true"
                }
            }
        }
    }
}
