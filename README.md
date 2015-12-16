A experimental sample devtool to run on Oracle Java Cloud Service.

#Feaures
* Easily invoke the capture of WebLogic Diagnositics Image across the domain. 
* Automatically upload the captured files to Oracle Storage Cloud Service.
* View and download the uploaded files in Storage Cloud Service through web browser or REST API.

#Building the application using Developer Cloud Service Hudson
  Please see the Wiki for how to create the Developer Cloud build job.  
    
#Deploying the application to Java Cloud Service  
  The war file will be archived inside the job history. Please deploy this to the WebLogic admin server and all the managed servers  inside your WebLogic domain.  
  You can download this war file and deploy to JCS WebLogic manually, or create a Developer Cloud Deploy configuration to deploy this file to JCS. If you use Developer Cloud Deploy, it will only deploy to the Managed Servers, so after deploying is done, you need to manually extend the target to the WebLogic admin Server.  
  
#Using the application
## Access from the browser
Access by https://<JCS admin server public ip address>/jfrmanager/  
## Access by REST API
You can invoke the diagnostic image capture with REST and recieve a html file that includes the links to the uploaded file. A sample Hudson job for invoking the rest api is in the wiki.
