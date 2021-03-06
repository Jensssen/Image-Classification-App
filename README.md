# Rock-Paper-Scissor-App
This repository contains my Rock Paper Scissor Classification App Project. 
It has been programmed using Kotlin and it uses AWS Sagemaker as a backend service to perform inference. The classification network is base on [MobileNet V2](https://arxiv.org/pdf/1801.04381.pdf) and fine tuned with manually recorded images using Keras.
 
# General Idea and Motivation behind the Project
The general motivation behind this project was, to learn more about different AWS Services. In addition, I wanted to try out, writing a new App using Kotlin. To this point in time, I only developed apps in native Java code (eg. [SpotFoxx](https://github.com/Jensssen/SpotFoxx)). 

Since I am a Machine Learning Engineer and ML is awesome, I decided to build an image based classification app. However, the ML part of this project was keeped as minimal as possible, so that I have more time to focus on the AWS backend part and a little bit on the Kotlin app. 

# Tech stack
The following list shows most of the different programming languages, frameworks and tools that have been used for this project.

- Kotlin                    -> Used to programm the app
- Python                    -> Used inside of the Lambda Function, Sagemaker and NN training
- Keras                     -> Used for the NN training
- Git                       -> Version Control 
- AWS Amplify               -> Great help to develop the App backend
- AWS Services
  - IAM                     -> Identity management inside of AWS
  - Cognito                 -> App user identity management
  - S3                      -> Storage of training data and NN Model
  - Lambda                  -> Used to access Sagemaker Endpoint
  - Sagemaker               -> Used to host endpoint that performs inference


# Final Result
The following Link leads to a YouTube Video that demonstrates the App.

[VIDEO](https://youtu.be/dJI1shZAKDk)

# Architecture
The following image show the underlying project architecture.
A small remark: The architecture has not been developed in a way that it is very cost efficient. For example the kotlin app could call the Sagemaker Endpoint directly and no lambda function is needed in between. Instead, I wanted to try out many different things eg. how lambda functions work. Therefore, I implemented it like this. 
![alt text](https://github.com/Jensssen/Image-Classification-App/blob/master/images/Rock_Paper_Scissor.png)

# Machine Learning Related Code
The ML related code can be found in an extra repository which can be found [here](https://github.com/Jensssen/rock_paper_scissor_classification). As already mentioned, the main focus of this project was not ML. Therefore, the ML part is keeped very minimal. 