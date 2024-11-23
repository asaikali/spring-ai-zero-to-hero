# Google Vertex  

To run the sample application you will to configure your machine using the 
gcloud cli so that apps can access gemini. This is a one time setup, done with
the gcloud cli. run the three commands below.

```shell
gcloud config set project ${PROJECT_ID} 
gcloud auth application-default set-quota-project ${PROJECT_ID}
gcloud auth application-default login
``` 
Copy the `src/main/resources/creds-template.
yaml` to `src/main/resources/creds.yaml` and add project id and location.

Run the application, it is better to run the app from the IDE so that you can 
put breakpoints.

