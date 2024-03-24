## To Build/Run on OS X
You need to have OpenJDK installed: _openjdk-21_

https://docs.oracle.com/en/java/javase/21/install/installation-jdk-macos.html#GUID-EB197354-E07E-4C6A-8AF6-642E23241D39

You need to have mac ports installed on your machine
Follow the instructions to install _maven_.
https://ports.macports.org/port/maven3/

Download dependencies
_mvn dependency:resolve_

_mvn clean package_

_mvn clean package_
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

_brew install --cask docker virtualbox_

_brew install docker-machine docker docker-compose_
_brew install docker_
_brew install --cask docker_
_brew install --cask dockmate_


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

_docker context use desktop-linux_

_docker build --tag directory ._

### To run the docker image; ensure you have K8 running
_docker run -p 8080:8080 -d directory_

docker ps

docker exec -it {image} bash

docker kill {image}

docker image rm -f {image}

Docker versions may cause issues depending on Intel vs ARM chips etc.
Make sure you get install the correct version

### To Build/Run on Kubernetes

Ensure you have XCode installed

**Install Kubernetes**

https://kubernetes.io/docs/tasks/tools/install-kubectl-macos/

_brew install kubectl_

**Install Minikube**

_brew install minikube_

_kubectl cluster-info_


**Download the Repo:**

https://github.com/harpoon4530/Directory/archive/refs/heads/main.zip

^^ Better ways to set it up like using SSH keys etc





## To Run

### Start Kubernetes


### Start MySQL


**Run MySQL in Kubernetes**

This needs to be done before starting the application; since it depends on a valid MySQL connection.


### Start App





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



# sudo apt-get install nginx
# test file and updated config for db connection for docker images
