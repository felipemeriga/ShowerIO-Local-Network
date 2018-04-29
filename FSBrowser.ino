/*
  SmartShower - Example WebServer with SPIFFS backend for esp8266
  Copyright (c) 2018 Felipe Ramos. All rights reserved.
  This file is part of the ESP8266WebServer library for Arduino environment.

  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 2.1 of the License, or (at your option) any later version.
  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.
  You should have received a copy of the GNU Lesser General Public
  License along with this library; if not, write to the Free Software
  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

  upload the contents of the data folder with MkSPIFFS Tool ("ESP8266 Sketch Data Upload" in Tools menu in Arduino IDE)
  or you can upload the contents of a folder if you CD in that folder and run the following command:
  for file in `ls -A1`; do curl -F "file=@$PWD/$file" esp8266fs.local/edit; done

  access the sample web page at http://esp8266fs.local
  edit the page by going to http://esp8266fs.local/edit
*/
#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <ESP8266WebServer.h>
#include <ESP8266mDNS.h>
#include <FS.h>
#include <DNSServer.h>
#include <EEPROM.h>
#include <Ticker.h>

const byte DNS_PORT = 53;


#define buttonPin D2  // the number of the pushbutton pin
#define rele D1      // the number of the LED pin
#define Led_Aviso D0

Ticker botao;
Ticker tyme;

// Variables will change:
int releState = LOW;         // the current state of the output pin
int buttonState;             // the current reading from the input pin
int lastButtonState = LOW;   // the previous reading from the input pin
int led_S = LOW;


unsigned long lastDebounceTime = 0;  // the last time the output pin was toggled
unsigned long debounceDelay = 25;    // the debounce time; increase if the output flickers



enum Estado_Botao {
  desligado,
  iniciar_banho,
  pausar_banho,
  continuar_banho
};


enum Estado_Banho {
  habilitado,
  desabilitado
};

Estado_Botao Estado_Bot = desligado;
Estado_Banho Banho = habilitado;


//posiçoes para armazenar variaveis na EEPROM
int address_tempo = 0;
int address_espera = 1;
int address_pausa = 2;


byte armazenado;
byte minutos = EEPROM.read(address_tempo); //tempo de banho
byte minutos_espera = EEPROM.read(address_espera); //tempo de espera até o banho ser habilitado novamente
byte minutos_pausa = EEPROM.read(address_pausa); // tempo que o banho pode ficar pausado
int tempo = (int)minutos * 60;
int tempo_espera = (int)minutos_espera * 60;
int tempo_de_pausa = (int)minutos_pausa * 60;

#define DBG_OUTPUT_PORT Serial
DNSServer dnsServer;
const char* ssid = "merry";
const char* password = "12345678";
const char* host = "esp8266fs";

ESP8266WebServer server(80);
//holds the current upload
File fsUploadFile;

//format bytes
String formatBytes(size_t bytes) {
  if (bytes < 1024) {
    return String(bytes) + "B";
  } else if (bytes < (1024 * 1024)) {
    return String(bytes / 1024.0) + "KB";
  } else if (bytes < (1024 * 1024 * 1024)) {
    return String(bytes / 1024.0 / 1024.0) + "MB";
  } else {
    return String(bytes / 1024.0 / 1024.0 / 1024.0) + "GB";
  }
}

String getContentType(String filename) {
  if (server.hasArg("download")) return "application/octet-stream";
  else if (filename.endsWith(".htm")) return "text/html";
  else if (filename.endsWith(".html")) return "text/html";
  else if (filename.endsWith(".css")) return "text/css";
  else if (filename.endsWith(".js")) return "application/javascript";
  else if (filename.endsWith(".png")) return "image/png";
  else if (filename.endsWith(".gif")) return "image/gif";
  else if (filename.endsWith(".jpg")) return "image/jpeg";
  else if (filename.endsWith(".ico")) return "image/x-icon";
  else if (filename.endsWith(".xml")) return "text/xml";
  else if (filename.endsWith(".pdf")) return "application/x-pdf";
  else if (filename.endsWith(".zip")) return "application/x-zip";
  else if (filename.endsWith(".gz")) return "application/x-gzip";
  return "text/plain";
}

bool handleFileRead(String path) {
  DBG_OUTPUT_PORT.println("handleFileRead: " + path);
  if (path.endsWith("/")) path += "index.html";
  String contentType = getContentType(path);
  File file = SPIFFS.open(path, "r");
  DBG_OUTPUT_PORT.println(contentType);
  server.streamFile(file, contentType);
  return true;
}

void selectDurationTime() {

  DBG_OUTPUT_PORT.println("Set Shower Selected Duration Timer to:" + server.arg("time").toInt());
  DBG_OUTPUT_PORT.println(server.arg("time").toInt());
  EEPROM.write(address_tempo, server.arg("time").toInt());
  EEPROM.commit();
  server.send(204, "");
}

void setActualShowerTimePlus() {

  DBG_OUTPUT_PORT.println("Set Actual Bath Plus time:" + server.arg("plusTime").toInt());
  DBG_OUTPUT_PORT.println(server.arg("plusTime").toInt());
  minutos = minutos + server.arg("plusTime").toInt();
  if (minutos > 45) {
    minutos = 45;
  }
  EEPROM.write(address_tempo, minutos);
  EEPROM.commit();
  server.send(204, "");
}

void setActualShowerTimeLess() {

  DBG_OUTPUT_PORT.println("Set Actual Bath Less time:" + server.arg("lessTime").toInt());
  DBG_OUTPUT_PORT.println(server.arg("lessTime").toInt());
  minutos = minutos - server.arg("lessTime").toInt();;
  if (minutos < 1) {
    minutos = 1;
  }
  if (minutos > 45) {
    minutos = 1;
  }
  EEPROM.write(address_tempo, minutos);
  EEPROM.commit();
  server.send(204, "");
}

void selectOffTime() {
  DBG_OUTPUT_PORT.println("Set Selected Actual Off Bath time:" + server.arg("time").toInt());
  DBG_OUTPUT_PORT.println(server.arg("time").toInt());
  minutos_espera = server.arg("time").toInt();
  EEPROM.write(address_espera, minutos_espera);
  EEPROM.commit();
  server.send(204, "");
}

void setActualOffTimePlus() {

  DBG_OUTPUT_PORT.println("Set Actual  aditional Off time:" + server.arg("plusTime").toInt());
  DBG_OUTPUT_PORT.println(server.arg("plusTime").toInt());
  minutos_espera = minutos_espera + server.arg("plusTime").toInt();
  if (minutos_espera > 45) {
    minutos_espera = 45;
  }
  EEPROM.write(address_espera, minutos_espera);
  EEPROM.commit();
  server.send(204, "");
}

void setActualOffTimeLess() {

  DBG_OUTPUT_PORT.println("Set Actual less Off time:" + server.arg("lessTime").toInt());
  DBG_OUTPUT_PORT.println(server.arg("lessTime").toInt());
  minutos_pausa = minutos_pausa - server.arg("lessTime").toInt() ;
  if (minutos_pausa < 1) {
    minutos_pausa = 1;
  }
  if (minutos_pausa > 45) {
    minutos_pausa = 1;
  }
  EEPROM.write(address_pausa, minutos_pausa);
  EEPROM.commit();
  server.send(204, "");
}

void selectPausedTime() {
  DBG_OUTPUT_PORT.println("Set Selected Actual Paused Bath time:" + server.arg("time").toInt());
  DBG_OUTPUT_PORT.println(server.arg("time").toInt());
  minutos_pausa = server.arg("time").toInt();
  EEPROM.write(address_pausa, minutos_pausa);
  EEPROM.commit();
  server.send(204, "");
}

void setActualPausedTimePlus() {

  DBG_OUTPUT_PORT.println("Set Actual  aditional Paused time:" + server.arg("plusTime").toInt());
  DBG_OUTPUT_PORT.println(server.arg("plusTime").toInt());
  minutos_pausa = minutos_pausa + server.arg("plusTime").toInt();
  if (minutos_pausa > 45) {
    minutos_pausa = 45;
  }
  EEPROM.write(address_pausa, minutos_pausa);
  EEPROM.commit();
  server.send(204, "");
}

void setActualPausedTimeLess() {

  DBG_OUTPUT_PORT.println("Set Actual less Paused time:" + server.arg("lessTime").toInt());
  DBG_OUTPUT_PORT.println(server.arg("lessTime").toInt());
  minutos_pausa = minutos_pausa - server.arg("lessTime").toInt();
  if (minutos_pausa < 1) {
    minutos_pausa = 1;
  }
  if (minutos_pausa > 45) {
    minutos_pausa = 1;
  }
  EEPROM.write(address_pausa, minutos_pausa);
  EEPROM.commit();
  server.send(204, "");
}

void logica_botao() {

  int reading = digitalRead(buttonPin);

  if (reading != lastButtonState) {
    // reset the debouncing timer
    lastDebounceTime = millis();
  }

  if ((millis() - lastDebounceTime) > debounceDelay) {

    if (reading != buttonState) {
      buttonState = reading;

      // only toggle the LED if the new button state is HIGH
      if (buttonState == LOW) {
        if (Banho == habilitado && Estado_Bot == desligado ) {
          Estado_Bot = iniciar_banho;
          digitalWrite(rele, HIGH);

        }

        else if (Banho == habilitado && (Estado_Bot == iniciar_banho || Estado_Bot == continuar_banho) ) {
          Estado_Bot = pausar_banho;
          minutos_pausa = EEPROM.read(address_pausa);
          tempo_de_pausa = minutos_pausa * 60;
          digitalWrite(rele, LOW);
        }

        else if (Banho == habilitado && Estado_Bot == pausar_banho ) {
          Estado_Bot = continuar_banho;
          digitalWrite(rele, HIGH);
        }
      }

    }
  }

  lastButtonState = reading;
}


void logica_tempo() {
  if (Banho == habilitado && Estado_Bot == iniciar_banho) {
    minutos = EEPROM.read(address_tempo);
    tempo = minutos * 60;
    Estado_Bot = continuar_banho;
  }

  else if (Banho == habilitado && Estado_Bot == continuar_banho) {
    tempo = tempo - 1;
    if (tempo < 60 && tempo > 20)
    {
      led_S = HIGH;
    }
    else if (tempo < 20) {

      if (led_S == LOW) {
        led_S = HIGH;
      } else {
        led_S = LOW;
      }


    }
    digitalWrite(Led_Aviso, led_S);
  }

  else if (Banho == habilitado && Estado_Bot == pausar_banho && tempo_de_pausa > 0) {
    tempo = tempo;
    tempo_de_pausa = tempo_de_pausa - 1;
  }
  else if (Banho == habilitado && Estado_Bot == pausar_banho && tempo_de_pausa <= 0) {
    Banho = desabilitado;
    Estado_Bot = desligado;
    tempo = 0;
    minutos_espera = EEPROM.read(address_espera);
    tempo_espera = (int)minutos_espera * 60;
    digitalWrite(rele, LOW);
  }

  if (tempo == 0 && Banho == habilitado) {
    Banho = desabilitado;
    Estado_Bot = desligado;
    minutos_espera = EEPROM.read(address_espera);
    tempo_espera = (int)minutos_espera * 60;
    digitalWrite(rele, LOW);
    digitalWrite(Led_Aviso, LOW);
  }

  else if (tempo == 0 && Banho == desabilitado) {
    if (tempo_espera > 0) {
      tempo_espera = tempo_espera - 1;
    }
    else if (tempo_espera <= 0) {
      Banho = habilitado;
      minutos = EEPROM.read(address_tempo);
      tempo = (int)minutos * 60;
      minutos_espera = EEPROM.read(address_espera);
      tempo_espera = (int)minutos_espera * 60;
    }
  }

}

void setup(void) {

  pinMode(buttonPin, INPUT);
  pinMode(rele, OUTPUT);
  pinMode(Led_Aviso, OUTPUT);

  digitalWrite(rele, LOW);
  digitalWrite(Led_Aviso, LOW);


  botao.attach(0.2, logica_botao);
  tyme.attach(1, logica_tempo);

  DBG_OUTPUT_PORT.begin(115200);
  DBG_OUTPUT_PORT.print("\n");
  DBG_OUTPUT_PORT.setDebugOutput(true);
  SPIFFS.begin();
  {
    Dir dir = SPIFFS.openDir("/");
    while (dir.next()) {
      String fileName = dir.fileName();
      size_t fileSize = dir.fileSize();
      DBG_OUTPUT_PORT.printf("FS File: %s, size: %s\n", fileName.c_str(), formatBytes(fileSize).c_str());
    }
    DBG_OUTPUT_PORT.printf("\n");
  }

  //WIFI INIT
  WiFi.softAP(ssid, password);
  //   WiFi.mode(WIFI_STA);
  //   WiFi.begin(ssid, password);
  IPAddress apip = WiFi.softAPIP();

  //---------- DNS Server
  dnsServer.setTTL(300);
  dnsServer.setErrorReplyCode(DNSReplyCode::ServerFailure);
  dnsServer.start(DNS_PORT, "www.smartshower.com", apip); // replace it with desired DNS name
  //-----------------

  DBG_OUTPUT_PORT.println("");
  DBG_OUTPUT_PORT.print("Connected! IP address: ");
  DBG_OUTPUT_PORT.println(WiFi.localIP());

  //EEPROM Start Function
  EEPROM.begin(512);

  armazenado = EEPROM.read(address_tempo);
  if (armazenado > 45 || armazenado < 1 ) {
    minutos = 7;
    EEPROM.write(address_tempo, minutos);
    EEPROM.commit();
  }
  else {
    minutos = armazenado;
  }

  armazenado = EEPROM.read(address_espera);
  if (armazenado > 45 || armazenado < 1 ) {
    minutos_espera = 5;
    EEPROM.write(address_espera, minutos_espera);
    EEPROM.commit();
  }
  else {
    minutos_espera = armazenado;
  }

  armazenado = EEPROM.read(address_pausa);
  if (armazenado > 45 || armazenado < 1 ) {
    minutos_pausa = 3;
    EEPROM.write(address_pausa, minutos_pausa);
    EEPROM.commit();
  }
  else {
    minutos_pausa = armazenado;
  }

  //  if (!MDNS.begin("esp8266")) {             // Start the mDNS responder for esp8266.local
  //    Serial.println("Error setting up MDNS responder!");
  //  }

  server.on ( "/selectDurationTime", selectDurationTime);
  server.on ( "/setActualShowerTimePlus", setActualShowerTimePlus);
  server.on ( "/setActualShowerTimeLess", setActualShowerTimeLess);
  server.on ( "/selectOffTime", selectOffTime);
  server.on ( "/setActualOffTimePlus", setActualOffTimePlus);
  server.on ( "/setActualOffTimeLess", setActualOffTimeLess);
  server.on ( "/selectPausedTime", selectPausedTime);
  server.on ( "/setActualPausedTimePlus", setActualPausedTimePlus);
  server.on ( "/setActualPausedTimeLess", setActualPausedTimeLess);

  //create file
  server.onNotFound([]() {
    if (!handleFileRead(server.uri()))
      server.send(404, "text/plain", "FileNotFound");
  });


  server.begin();
  DBG_OUTPUT_PORT.println("HTTP server started");

}



void loop(void) {
  dnsServer.processNextRequest();
  server.handleClient();
}
