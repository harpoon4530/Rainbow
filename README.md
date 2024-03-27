


## To Build/Run on OS X
You need to have OpenJDK installed: _openjdk-21_

https://docs.oracle.com/en/java/javase/21/install/installation-jdk-macos.html#GUID-EB197354-E07E-4C6A-8AF6-642E23241D39

You need to have mac ports installed on your machine
Follow the instructions to install _maven_.
https://ports.macports.org/port/maven3/

Download dependencies

    mvn dependency:resolve

    mvn clean package

    mvn clean package

    java -cp ./target/DirectoryJ-1.0-SNAPSHOT.jar org.personio.App

    java -cp ./target/PersonIo.jar org.personio.App

Use the Postman collection under the Postman directory to send requests



### OS X build instructions
**Install docker:**

You will need to have both VirtualBox and HomeBrew installed and running


https://docs.docker.com/desktop/install/mac-install/

[Updated Instructions
](https://desktop.docker.com/mac/main/amd64/122432/Docker.dmg?_gl=1*1djsy22*_ga*MjAyNzUwMzM5NC4xNjk5ODM5MTIx*_ga_XJWPQMJYHQ*MTY5OTgzOTEyMC4xLjEuMTY5OTg0MTcyNC41Ny4wLjA.
)

    brew install --cask docker virtualbox

    brew install docker-machine docker docker-compose
    brew install docker
    brew install --cask docker
    brew install --cask dockmate


Download and install VirtualBox

https://www.virtualbox.org/wiki/Downloads


###Configure docker-machine on macOS

[comment]: <> (Create a default machine &#40;if you don't have one, see: docker-machine ls&#41;:)

[comment]: <> (_docker-machine create --driver virtualbox default_)

[comment]: <> (Then set-up the environment for the Docker client:)

[comment]: <> (_eval "$&#40;docker-machine env default&#41;"_)

[comment]: <> (Go to VirtualBox and start the *deamon*)

[comment]: <> (Then double-check by listing containers:)

[comment]: <> (_docker ps_)



### This builds the package

    docker context use desktop-linux

    docker build --tag directory .

### To run the docker image; ensure you have K8 running

    docker run -p 8080:8080 -d directory

    docker ps

    docker exec -it {image} bash

    docker kill {image}

    docker image rm -f {image}

    docker system prune -a

    docker kill {image}

Docker versions may cause issues depending on Intel vs ARM chips etc.
Make sure you get install the correct version

### To Build/Run on Kubernetes

Ensure you have XCode installed

**Install Kubernetes**

https://kubernetes.io/docs/tasks/tools/install-kubectl-macos/

    brew install kubectl

**Install Minikube**

    brew install minikube

**To Check the cluster**
    
    kubectl cluster-info


**Download the Repo:**

https://github.com/harpoon4530/Directory/archive/refs/heads/main.zip

^^ Better ways to set it up like using SSH keys etc


## To Run

### Start Kubernetes
      minikube delete
      minikube start
      kubectl cluster-info

[comment]: <> (      minikube start --driver=docker)

[comment]: <> (Kubernetes control plane is running at https://192.168.64.2:8443)

[comment]: <> (CoreDNS is running at https://192.168.64.2:8443/api/v1/namespaces/kube-system/services/kube-dns:dns/proxy)



### Start MySQL


**Run MySQL in Kubernetes**

This needs to be done before starting the application; since it depends on a valid MySQL connection.

0. Log into the correct directory
    
        cd mysql-kube
   
1. Create persistent volume
   
        kubectl apply -f mysql-pv.yaml
   
2. Create the Persistent Volume Claim

        kubectl apply -f mysql-pvc.yaml

3. Create the Deployment

        kubectl apply -f mysql-deployment.yaml

4. Create the Service
   
        kubectl apply -f mysql-service.yaml


5. Check if the kubernetes objects were successfully created with:
    
    **Deployment:**

        kubectl get deployments

    **Pod:**
   
        kubectl get pods
        kubectl get pods -l app=mysql 
        kubectl describe pvc mysql-pv
        kubectl describe deployment mysql
      
         kubectl logs 

   **Services:**
        
           kubectl get services

   **To check the status and log in manually, execute:**

[//]: # (     docker run -d --name mysql-container -e MYSQL_ROOT_PASSWORD=root -p 3306:3306 mysql:latest)
[//]: # (     kubectl run -it --rm --image=mysql:latest --restart=Never mysql-client -- mysql -h mysql --password="password")

[//]: # (     kubectl run -it --rm --image=mysql:latest --restart=Never mysql-client -- mysql -h mysql --password="password")

        kubectl get pods
        kubectl exec --stdin --tty mysql-{instance} -- /bin/bash
        mysql -ppassword

   **To Delete**

      kubectl delete deployment,svc mysql
      kubectl delete pvc mysql-pv-claim
      kubectl delete pv mysql-pv-volume

### Start App

MySQL credentials are: (<user:password>)    (<root:password>) 


## Perform the following steps to deploy spring boot app on Minikube :

1. Clone this repository
2. From the terminal cd into your project directory and build project using ``` mvn clean package ```
3. Start docker on the system
4. Start minikube using ``` minikube start --driver=docker ```
5. Enable docker env using command :  ``` eval $(minikube docker-env)  ```  [Command Reference](https://minikube.sigs.k8s.io/docs/commands/docker-env/)
6. Build docker image in minikube : ``` docker build -t directory:latest . ```
7. Run built image ``` docker-compose up ```
7. To see the created image run command : ``` minikube image ls ```
8. To deploy on kubernetes cluster run command : ``` helm install mychart ytchart ```
9. To see deployed helm chart : ``` helm ls ```
10. Check deployments : ``` kubectl get all ```
11. To connect the database run ``` kubectl get services ``` and copy my-sql service name. Then run command like this : ``` minikube service mychart-mysql-service --url ```
12. Then connect the database using the IP address and port returned by Step 10.  (If you get error while connecting database, then watch the video for more information)
13. To call Rest api's, open a new Terminal, and run command : ``` minikube tunnel ``` and call api from the Postman or any of your favourite tool.
14. Remove or delete deployed setup from kubernetes cluster : ``` helm uninstall mychart ```
15. Stop minikube using : ``` minikube stop ```






User created with the following creds:
username: test
password: user
HTTP header: {Authorization: Basic dGVzdDp1c2Vy}

To POST a new JSON file; use the example from the Postman collection:

*Post - Valid JSON*


### Improvements
CI/CD
Config params
Better logging
Metrics tracking
Proper class abstraction
Exception handling
More tests
Linter tool
Logrotated



# sudo apt-get install nginx
# test file and updated config for db connection for docker images
