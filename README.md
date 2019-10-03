Test project for developing
Spark Jobs for Kubernetes based on pattern Operator

First read this: 
https://github.com/GoogleCloudPlatform/spark-on-k8s-operator/

Below my notes: 

//Kubernets
kubectl cluster-info
//View all workloads & Services on cluster
kubectl get pods
kubectl get all

//helm Initialization
helm init 
//One suggest from  https://github.com/helm/helm/issues/3055  
//Worked- 
kubectl create serviceaccount --namespace kube-system tiller
kubectl create clusterrolebinding tiller-cluster-rule --clusterrole=cluster-admin --serviceaccount=kube-system:tiller
kubectl patch deploy --namespace kube-system tiller-deploy -p '{"spec":{"template":{"spec":{"serviceAccount":"tiller"}}}}'
//Anouther suggest - not tested 
//@viane Try the following steps. (You'll probably need to kubectl delete the tiller service and deployment.)
//$ kubectl create serviceaccount --namespace kube-system tiller
//$ kubectl create clusterrolebinding tiller-cluster-rule --clusterrole=cluster-admin --serviceaccount=kube-system:tiller
//$ helm init --service-account tiller

//Add the rights
kubectl create clusterrolebinding <user>-cluster-admin-binding --clusterrole=cluster-admin --user=<user>@<domain>


helm version
helm list  -  list of RELEASES

//Install SparkOperator
helm repo add incubator http://storage.googleapis.com/kubernetes-charts-incubator
helm repo update
helm install incubator/sparkoperator --namespace spark-operator --set sparkJobNamespace=default


helm list 
helm status <spark-operator-release-name>  //release name from previos step

//According to KNOWING issues in
//https://github.com/GoogleCloudPlatform/click-to-deploy/issues/559
kubectl apply -f manifest/spark-rbac.yaml


//Run example 
kubectl apply -f examples/spark-pi.yaml
//Restart(delete + submit)
kubectl replace -f ~/spark-on-k-operator/examples/spark-pi.yaml --force
//delete resources and config
kubectl delete -f ~/spark-on-k8s-operator/examples/spark-pi.yaml 

//Check config
kubectl get sparkapplications spark-pi -o=yaml

//To check events for the SparkApplication object, run the following command:

kubectl describe sparkapplication spark-pi

//Delete 
kubectl get all
kubectl delete

//Connect to Docker instance 
kubectl exec -it <POD_NAME> -c <CONTAINER NAME> bash

//Turn of VMs via resize the cluster
gcloud container clusters resize my-spark-cluster --num-nodes=0 --zone us-central1-a

//Create public storage for JARs
gsutil -m acl -r set public-read gs://abelash-spark-apps
gsutil -m defacl set public-read gs://abelash-spark-apps


//DoCKER 
//https://cloud.google.com/container-registry/docs/quickstart?hl=ru
//"INI" local repo in Google Cloud Shell. Mandatory if using Cloud Shell
docker run busybox date
//List current docker images 
docker image ls
//Build native spark images - very important to chage dir to main spark dir 
cd ~/spark
//Build
./bin/docker-image-tool.sh -r gcr.io/my-project-app-engint -t my-gcr-tag build
//Push to GCR
docker push gcr.io/my-project-app-engint/spark:my-gcr-tag

//BUILD PROJECT abelashapps
//1. Upload jar to gsutil cp gs://abelash-spark-apps/jars/* ./
//Copy jars to Cloudshell
gsutil cp gs://abelash-spark-apps/jars/* ~/sparkoperator/my-spark-docker/jars/
cd ~/sparkoperator/my-spark-docker/
docker build -t gcr.io/my-project-app-engint/sparkoperator:abelashapps .
docker push gcr.io/my-project-app-engint/sparkoperator:abelashapps
//RUN Project on Kubernetes 
kubectl apply -f ~/sparkoperator/myexamples/spark-pi-abelashapps.yaml 
//Jump into the Container 
kubectl exec -it spark-pi-abelashapps-driver -c spark-kubernetes-driver bash
//Restart the abelashapps
 




//DATAPROC
//https://cloud.google.com/dataproc/docs/guides/submit-job
gcloud dataproc jobs submit spark \
    --cluster cluster-name --region region \
    --class org.apache.spark.examples.SparkPi \
    --jars file:///usr/lib/spark/examples/jars/spark-examples.jar \
    -- 1000   
//Last line "1000"  is args   

gcloud dataproc jobs submit spark     --cluster cluster-2db4 --region us-central1     \
	--class examples.SparkPiBe     
	--jars gs://dataproc-53cbd6e1-95aa-43ef-a08d-34e0f3edf77d-us-central1/jars/testhbasespark_2.11-0.3.jar  \
	-- 1000

//Access to UI via CloudShell
//https://cloud.google.com/dataproc/docs/concepts/accessing/cluster-web-interfaces
gcloud compute ssh cluster-2db4-m  --project=my-project-app-engint --zone=us-central1-a  -- -4 -N -L 8080:cluster-2db4-m:4040





