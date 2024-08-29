
# The Rainbow Take-Home Assignment

Please create a __private__ version of this repo, complete the objectives, and once you
are finished, send a link to your repo to us.

# The Assignment

Part of what an insurance company needs to have in its backend is a
record system. As an insurer, we need to keep an up-to-date record of each of our policy-holder's
data points that go into the calculation of their rate. When a policy-holder updates
their information, I.E. they change addresses, or add/remove new employees to their team
we will be notified and we must keep our records up to date.

The current version of the repo is an extremely simplified version of exactly that. `GET /api/v1/record/{id}`
will retrieve a record, which is just a json mapping strings to strings. and `POST /api/v1/record/{id}`
will either create a new record or modify an existing record. However, it isn't enough to
just keep a record of the current record state but we must maintain a reference to how the state
has changed to be in full compliance.

Say that the policy-holder buys their insurance on the start of the year, and then two months later
changes the address of their business but doesn't tell us about this change until 4 months after that.
Since we were technically held liable if there was a claim event, we need to charge the customer the
difference for the 4 months since they changed addresses. To do so accurately, we need to know the
version of the records that we knew about them at the two points of time: at the time when the change happened
and at the time when we were told of the change.

In this project, you'll make a simplified version of this system. We've implemented an in-memory key-value store with no history.
At a high-level your goal is to do two things to this existing codebase:

1. Change the storage backend to sqlite, and persist the data across turning off and on the server.
2. Add the time travel component so we can easily look up the state of each records at different timesteps.

The sections below outline these two objectives in more detail. You may use whatever libraries and tools
you like to achieve this even as far as building this in an entirely different language.

## Objective: Switch To Sqlite

The current implementation does not store the data. The data is lost once the server
process is killed. You should change the code so that all changes are persisted on
to sqlite.

Once you're done, the data should be persistent on to a sqlite file as the server
is running. The server should tolerate restarting the process without data loss.

## Objective: Add Time Travel
This part is far more open-ended. You might need to make major changes across nearly
all files of the codebase. You'll be adding persistentence to the records.

You should create a set of `/api/v2` endpoints that enable you to do run gets, creates, and updates.
Unlike in v1, records are now versioned. Full requirements:

- You should have endpoints that allow the api client to get records at different versions. (not just
  the latest version).
- You should be able to add modifications on top of the latest version.
- There should be a way to get a list of the different versions too.
- `/api/v1` should still work after these changes with identical behavior as before.

# Reccommendations

We expect you to work as if this task was a normal project at work. So please write
your code in a way that fits your intuitive notion of operating within best practices.
Additionally, you should at the very least have a different commmit for each individual objective,
ideally more as you go through process of completing the take-home. Also we like
to see your thought process and fixes as you make changes. So don't be afraid of
committing code that you later edit. No need to squash those commits.

Many parts of the assignment is intentionally ambiguious. If you have a question, definitely
reach out. But for many of these ambiguiuties, we want to see how you independently make
software design decisions.

# FAQ
_Can I Use Another Language?_
Definitely, we've had multiple people complete this assignment in Python and Java. You can pick whatever
language you'd like although you should aim to replicate the functionality in the boilerplate.

_Did you really end up implementing something like this at Rainbow?_
Yes, but unfortunately it wasn't as simple as this in practice. For insurance a number of requirements force us
to maintain historic records across many different object types. So in fact we implemented this across multiple different
tables in our database.


# Reference -- The Current API

There are only two API endpoints `GET /api/v1/records/{id}` and `POST /api/v1/records/{id}`, all ids must be positive integers.

### `GET /api/v1/records/{id}`

This endpoint will return the record if it exists.

```bash
> GET /api/v1/records/2323 HTTP/1.1

< HTTP/1.1 200 OK
< Content-Type: application/json; charset=utf-8
{"id":2323,"data":{"david":"hey","davidx":"hey"}}
```

```bash
> GET /api/v1/records/32 HTTP/1.1

< HTTP/1.1 400 Bad Request
< Content-Type: application/json; charset=utf-8
{"error":"record of id 32 does not exist"}
```

### `POST /api/v1/records/{id}`

This endpoint will create a record if a does not exists.
Otherwise it will update the record.

The payload is a json object mapping strings to strings
and nulls. Values that are null indicate that the
backend must delete that key of the record.

```bash
# Creating a record
> POST /api/v1/records/1 HTTP/1.1
{"hello":"world"}

< HTTP/1.1 200 OK
< Content-Type: application/json; charset=utf-8
{"id":1,"data":{"hello":"world"}}


# Updating that record
> POST /api/v1/records/1 HTTP/1.1
{"hello":"world 2","status":"ok"}

< HTTP/1.1 200 OK
< Content-Type: application/json; charset=utf-8
{"id":1,"data":{"hello":"world 2","status":"ok"}}


# Deleting a field
> POST /api/v1/records/1 HTTP/1.1
{"hello":null}

< HTTP/1.1 200 OK
< Content-Type: application/json; charset=utf-8
{"id":1,"data":{"status":"ok"}}
```


public static void main(String[] args) {
System.out.println("Hello World");
}

LinkedHashmap map = new LinkedHashMap<Integer, ObjectType>();

public void map(Object object) {


public class ObjectType {
enum Type {
INTEGER,
BOOLEAN,
OBJECT,
ARRAY,
MAP,
...
}
Type type;

    boolean bool;
    integer int;
    String s;
    Map<Key, ObjectType> map;
    List<ObjectType> list
}

ObjectType objType = new ObjectType();


public ObjectType convert(Object obj) {

ObjectType objType = new ObjectType();

switch(Object.instance()) {
case String:
objType.string = obj;
objType.type = ObjectType.STRING;        
break;
case Integer:
objType.string = obj;
objType.type = ObjectType.INTEGER;    
break;
case Map:
Map<Key, ObjectType> map = new HashMap<Key, ObjectType>();
for (Entry e: obj.entrySet() {
ObjectType o1 = convert(e.getValue());    
map.put(e.getKey(), o1);

      if (o1.equals(map)) {
        break;
      }
    }
   
    break;

case List:
break;

    default:
      break;


    return objectType; 
}


}


}





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

    java -cp ./target/Rainbow-1.0-SNAPSHOT.jar org.rainbow.App

    java -cp ./target/Rainbow.jar org.rainbow.App

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
      minikube dashboard
      kubectl cluster-info

[comment]: <> (      minikube start --driver=docker)

[comment]: <> (Kubernetes control plane is running at https://192.168.64.2:8443)

[comment]: <> (CoreDNS is running at https://192.168.64.2:8443/api/v1/namespaces/kube-system/services/kube-dns:dns/proxy)


### Namespace
    kubectl config set-context --current --namespace=personio
    # Validate it
    kubectl config view --minify | grep namespace:


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
        kubectl get svc
        kubectl get pods -l app=mysql 
        kubectl describe pvc mysql-pv
        kubectl describe deployment mysql
      
         kubectl logs 
    
    **Check to see if service is running:**

         mysql --host localhost  --port 30000 -u root -ppassword

   **Services:**
        
           kubectl get services

   **To check the status and log in manually, execute:**

[//]: # (     docker run -d --name mysql-container -e MYSQL_ROOT_PASSWORD=root -p 3306:3306 mysql:latest)
[//]: # (     kubectl run -it --rm --image=mysql:latest --restart=Never mysql-client -- mysql -h mysql --password="password")

[//]: # (     kubectl run -it --rm --image=mysql:latest --restart=Never mysql-client -- mysql -h mysql --password="password")

        kubectl get pods
        kubectl exec --stdin --tty mysql-{instance} -- /bin/bash
        kubectl exec --stdin --tty mysql-{instance} -- mysql -ppassword
        mysql -ppassword

   **To Delete**

      kubectl delete deployment,svc mysql
      kubectl delete pvc mysql-pv-claim
      kubectl delete pv mysql-pv-volume

      minikube delete && minikube start && docker system prune -a && docker build --tag directory .

### Start App

       kubectl apply -f app-deployment.yaml
       kubectl get deployments
       kubectl get pods
       kubectl get svc
         

#### MySQL credentials are: (<user:password>)    (<root:password>) 

     mysql -uroot -ppassword

### Kubernets dashboard
       kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.7.0/aio/deploy/recommended.yaml
       kubectl -n kubernetes-dashboard port-forward svc/kubernetes-dashboard-kong-proxy 8443:443
       
       Dashboard will be available at:
       https://localhost:8443


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
password: password
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
