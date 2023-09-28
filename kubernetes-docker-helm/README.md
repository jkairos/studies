# Installation Guide

This guide will walk you through the installation process for Docker, Helm, Gradle, and Minikube on your system.

## Docker

[Docker](https://www.docker.com/) is a platform for developing, shipping, and running applications in containers. Follow the steps below to install Docker:

1. **Linux**:
    - Visit the [Docker documentation](https://docs.docker.com/install/linux/docker-ce/ubuntu/) for installation instructions on various Linux distributions.

2. **Windows**:
    - Download and install [Docker Desktop for Windows](https://www.docker.com/products/docker-desktop).

3. **macOS**:
    - Download and install [Docker Desktop for Mac](https://www.docker.com/products/docker-desktop).

## Helm

[Helm](https://helm.sh/) is a package manager for Kubernetes that makes it easy to deploy and manage applications on a Kubernetes cluster. Follow the steps below to install Helm:

1. **Linux/macOS**:
    - Open a terminal and run the following commands:
      ```
      curl -fsSL -o get_helm.sh https://raw.githubusercontent.com/helm/helm/master/scripts/get-helm-3
      chmod 700 get_helm.sh
      ./get_helm.sh
      ```

2. **Windows**:
    - Download the [Windows installer](https://get.helm.sh/helm-v3.0.0-windows-amd64.zip) and follow the installation instructions.

## Gradle

[Gradle](https://gradle.org/) is a build automation tool that is often used for Java and Android development. Follow the steps below to install Gradle:

1. **Linux/macOS**:
    - Open a terminal and run the following commands to install Gradle via [SDKMAN!](https://sdkman.io/):
      ```
      sdk install gradle
      ```

2. **Windows**:
    - Download the [latest Gradle distribution](https://gradle.org/releases/) and follow the installation instructions for Windows.

## Minikube

[Minikube](https://minikube.sigs.k8s.io/) is a tool that makes it easy to run Kubernetes locally for development and testing purposes. Follow the steps below to install Minikube:

1. **Linux**:
    - Open a terminal and run the following commands to install Minikube via [Kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl-linux/):
      ```
      curl -LO https://storage.googleapis.com/minikube/releases/latest/minikube-linux-amd64
      sudo install minikube-linux-amd64 /usr/local/bin/minikube
      ```

2. **macOS**:
    - Open a terminal and run the following commands to install Minikube via [Homebrew](https://brew.sh/):
      ```
      brew install minikube
      ```

3. **Windows**:
    - Download the [Minikube installer](https://minikube.sigs.k8s.io/docs/start/) for Windows and follow the installation instructions.

---

You should now have Docker, Helm, Gradle, and Minikube installed on your system. Enjoy working with containers, Kubernetes, and building projects with Gradle!

## Building and Starting the application locally

```
./gradlew bootRun
```
## Accessing Swagger UI

```
http://localhost:8080/swagger-ui/index.html#
```
## Building the docker image and deploying in minikube

1.**Create the helm configuration**:
```
helm create demo-backend
```

2.**Rename the crated demo-backend folder to helm**:

3.**Change the values.yaml file**:
```
# Default values for demo-backend.
# This is a YAML-formatted file.
# Declare variables to be passed into your templates.

replicaCount: 3

image:
  repository: backend-ktdemo
  pullPolicy: IfNotPresent
  # Overrides the image tag whose default is the chart appVersion.
  tag: "1.0.0-SNAPSHOT"

event:
  listener:
    retryTime: 15000

actuator:
  port: 9001

logging:
  serviceLevel: INFO
  rootLevel: WARN

imagePullSecrets: []
nameOverride: ""
fullnameOverride: ""

serviceAccount:
  # Specifies whether a service account should be created
  create: true
  # Annotations to add to the service account
  annotations: {}
  # The name of the service account to use.
  # If not set and create is true, a name is generated using the fullname template
  name: ""

podAnnotations: {}

podSecurityContext: {}
  # fsGroup: 2000

securityContext: {}
  # capabilities:
  #   drop:
  #   - ALL
  # readOnlyRootFilesystem: true
  # runAsNonRoot: true
  # runAsUser: 1000

liveness:
  initialDelaySeconds: 15
  periodSeconds: 15
  timeoutSeconds: 5
  successThreshold: 1
  failureThreshold: 3
# readiness: should wait 30 + (15 * 10) = 180 seconds | 3 min
readiness:
  initialDelaySeconds: 30
  periodSeconds: 10
  timeoutSeconds: 5
  successThreshold: 1
  failureThreshold: 15

service:
  type: NodePort
  port: 8080
  nodePort: 30088

ingress:
  enabled: false
  className: ""
  annotations: {}
    # kubernetes.io/ingress.class: nginx
    # kubernetes.io/tls-acme: "true"
  hosts:
    - host: chart-example.local
      paths:
        - path: /
          pathType: ImplementationSpecific
  tls: []
  #  - secretName: chart-example-tls
  #    hosts:
  #      - chart-example.local

resources: {}
  # We usually recommend not to specify default resources and to leave this as a conscious
  # choice for the user. This also increases chances charts run on environments with little
  # resources, such as Minikube. If you do want to specify resources, uncomment the following
  # lines, adjust them as necessary, and remove the curly braces after 'resources:'.
  # limits:
  #   cpu: 100m
  #   memory: 128Mi
  # requests:
  #   cpu: 100m
  #   memory: 128Mi

autoscaling:
  enabled: false
  minReplicas: 1
  maxReplicas: 100
  targetCPUUtilizationPercentage: 80
  # targetMemoryUtilizationPercentage: 80

nodeSelector: {}

tolerations: []

affinity: {}
```

4.**Change the deployment.yaml file**:
```
apiVersion: apps/v1
kind: Deployment
metadata:
  name: {{ include "demo-backend.fullname" . }}
  labels:
    {{- include "demo-backend.labels" . | nindent 4 }}
spec:
  {{- if not .Values.autoscaling.enabled }}
  replicas: {{ .Values.replicaCount }}
  {{- end }}
  selector:
    matchLabels:
      {{- include "demo-backend.selectorLabels" . | nindent 6 }}
  template:
    metadata:
      {{- with .Values.podAnnotations }}
      annotations:
        {{- toYaml . | nindent 8 }}
      {{- end }}
      labels:
        {{- include "demo-backend.selectorLabels" . | nindent 8 }}
    spec:
      {{- with .Values.imagePullSecrets }}
      imagePullSecrets:
        {{- toYaml . | nindent 8 }}
      {{- end }}
      serviceAccountName: {{ include "demo-backend.serviceAccountName" . }}
      securityContext:
        {{- toYaml .Values.podSecurityContext | nindent 8 }}
      containers:
        - name: {{ .Chart.Name }}
          securityContext:
            {{- toYaml .Values.securityContext | nindent 12 }}
          image: "{{ .Values.image.repository }}:{{ .Values.image.tag | default .Chart.AppVersion }}"
          imagePullPolicy: {{ .Values.image.pullPolicy }}
          ports:
          - name: http
            containerPort: {{ .Values.service.port | default 8080 }}
            protocol: TCP
          env:
          - name: ACTUATOR_PORT
            value: "{{ .Values.actuator.port }}"
          - name: EVENT_LISTENER_RETRY_TIME
            value: "{{ .Values.event.listener.retryTime }}"
          - name: SERVICE_LOGGING_LEVEL
            value: "{{ .Values.logging.serviceLevel }}"
          - name: ROOT_LOGGING_LEVEL
            value: "{{ .Values.logging.rootLevel }}"
          livenessProbe:
            httpGet:
              path: {{ .Values.liveness.path | default "/actuator/health/livenessState" }}
              port: {{ .Values.actuator.port | default "http" }}
              scheme: HTTP
          readinessProbe:
            httpGet:
              path: {{ .Values.readiness.path | default "/actuator/health/readinessState" }}
              port: {{ .Values.actuator.port | default "http" }}
              scheme: HTTP
          resources:
            {{- toYaml .Values.resources | nindent 12 }}
      {{- with .Values.nodeSelector }}
      nodeSelector:
        {{- toYaml . | nindent 8 }}
      {{- end }}
      {{- with .Values.affinity }}
      affinity:
        {{- toYaml . | nindent 8 }}
      {{- end }}
      {{- with .Values.tolerations }}
      tolerations:
        {{- toYaml . | nindent 8 }}
      {{- end }}
```

5.**Change the service.yaml file**:
```
apiVersion: v1
kind: Service
metadata:
  name: {{ include "demo-backend.fullname" . }}
  labels:
    {{- include "demo-backend.labels" . | nindent 4 }}
spec:
  type: {{ .Values.service.type }}
  ports:
  - port: {{ .Values.service.port }}
    nodePort: {{ .Values.service.nodePort }}
    targetPort: http
    protocol: TCP
    name: http
  selector:
    {{- include "demo-backend.selectorLabels" . | nindent 4 }}
```

6.**Start minikube**:
```
minikube start
```

7.**Associate minikube environment to your current shell**:
```
eval $(minikube docker-env)
```

8.**Build the docker image**:
```
./gradlew docker
```

9.**Check if the docker image is created**:
```
docker images
```

10.**Deploy the application in kubernetes**:
```
helm install demo-backend ./helm/
```

11.**Run the command below and wait till you see 1/1 READY**:
```
kubectl get pods
```

12.**Access minikube dashboard**:
```
minikube dashboard
```

