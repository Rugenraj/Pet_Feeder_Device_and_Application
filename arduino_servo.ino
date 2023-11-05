#include <Arduino.h>
#if defined(ESP32)
  #include <WiFi.h>
#elif defined(ESP8266)
  #include <ESP8266WiFi.h>
#endif
#include <Firebase_ESP_Client.h>
#include <ESP32Servo.h>
 
//Provide the token generation process info.
#include "addons/TokenHelper.h"
//Provide the RTDB payload printing info and other helper functions.
#include "addons/RTDBHelper.h"
 
// Insert your network credentials
#define WIFI_SSID "ChrisAngel@unifi"
#define WIFI_PASSWORD "blueteddy88"
 
// Insert Firebase project API Key
#define API_KEY "AIzaSyDU3Rd1o_TFHgrE4GdqJyCtLeYxg9Ur-WI"
 
// Insert RTDB URLefine the RTDB URL */
#define DATABASE_URL "https://mini-project-d76ba-default-rtdb.asia-southeast1.firebasedatabase.app/" 
 
//Define Firebase Data object
FirebaseData fbdo;
 
FirebaseAuth auth;
FirebaseConfig config;
 
unsigned long sendDataPrevMillis = 0;
bool signupOK = false;

int InfraredPin = 35;
int servoPin = 32;
Servo myServo;  
int pos = 0;

String movementstatus="";

void setup(){
  Serial.begin(115200);
  pinMode(InfraredPin, INPUT);
  myServo.attach(servoPin);


  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to Wi-Fi");
  while (WiFi.status() != WL_CONNECTED){
    Serial.print(".");
    delay(300);
  }
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();
 
  /* Assign the api key (required) */
  config.api_key = API_KEY;
 
  /* Assign the RTDB URL (required) */
  config.database_url = DATABASE_URL;
 
  /* Sign up */
  if (Firebase.signUp(&config, &auth, "", "")){
    Serial.println("ok");
    signupOK = true;
  }
  else{
    Serial.printf("%s\n", config.signer.signupError.message.c_str());
  }
 
  /* Assign the callback function for the long running token generation task */
  config.token_status_callback = tokenStatusCallback; //see addons/TokenHelper.h
  
  Firebase.begin(&config, &auth);
  Firebase.reconnectWiFi(true);
}
 
void loop(){
  if (Firebase.ready() && signupOK && (millis() - sendDataPrevMillis > 5000 || sendDataPrevMillis == 0)){
    sendDataPrevMillis = millis();
    // Write an Int number on the database path test/int
  //int fire = digitalRead(fsensor);// read Flame sensor
   int IR = digitalRead(InfraredPin); 
     //Sense of pet 1st time
    if(IR==LOW)
    {
      Serial.println("Pet detected! Feeding time!");
      movementstatus="Your pet is waiting for food";
      Firebase.RTDB.setString(&fbdo, "objstatus", movementstatus);

    if (Firebase.RTDB.getInt(&fbdo, "/pos")) {
        pos = fbdo.intData();
        Serial.println("Angle value = ");
        Serial.println(pos);
    }
    else {
      Serial.println(fbdo.errorReason());
    }
    
    if(pos==90) 
    {
        //Open first compartment
        for (pos = 0; pos <= 90; pos += 1) // goes from 0 degrees to 90 degree in steps of 1 degree
        {  
            myServo.write(pos);    // tell servo to go to position in variable 'pos'
        }
          delay(2000);
    }
    else if(pos==180) 
    {
      //Open second compartment
        for (pos = 0; pos <= 180; pos += 1) // goes from 0 degrees to 90 degree in steps of 1 degree
        {  
            myServo.write(pos);    // tell servo to go to position in variable 'pos'
        }
            delay(2000);
    }
    else
    {
          myServo.write(0);
          delay(2000);
    }

    } //IR=LOW

    else
    {
      Serial.println("There's no pet");
      movementstatus="No Object";
      Firebase.RTDB.setString(&fbdo, "objstatus", movementstatus);
      myServo.write(0);
      delay(2000);
    }

}//firebase ready
}
